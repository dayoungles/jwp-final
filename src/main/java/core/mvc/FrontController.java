package core.mvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.JSON;
import next.model.Question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrontController extends HttpServlet implements Controller {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(FrontController.class);
	
	private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
	private static final String DEFAULT_API_PREFIX = "api";
	
	private RequestMapping rm;

	@Override
	public void init() throws ServletException {
		rm = (RequestMapping)getServletContext().getAttribute(ServletContextLoader.DEFAULT_REQUEST_MAPPING);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String requestUri = req.getRequestURI();
		logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);
		

		Controller controller = rm.findController(urlExceptParameter(req.getRequestURI()));
		
		String viewName;
		
		try {
			viewName = controller.execute(req, resp);
			
		} catch (Exception e) {
			throw new ServletException(e.getMessage());
		}
		movePage(req, resp, viewName);
	}

	void movePage(HttpServletRequest req, HttpServletResponse resp,
			String viewName) throws ServletException, IOException {
		if (viewName.startsWith(DEFAULT_API_PREFIX)) {
			
			return;
		}
		
		if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
			resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
			
			return;
		}
		
		RequestDispatcher rd = req.getRequestDispatcher(viewName);
		rd.forward(req, resp);
	}
	
	String urlExceptParameter(String forwardUrl) {
		int index = forwardUrl.indexOf("?");//get방식일 경우 주소만 떼는 작업. 
		if (index > 0) {
			return forwardUrl.substring(0, index);
		}
		
		return forwardUrl;//주소만 돌려준다. 
	}
	
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String url = request.getRequestURI();
		if(url.equals("/save.next")){
			String writer= request.getParameter("writer");
			String title = request.getParameter("title");
			String contents = request.getParameter("contents");
			Question question = new Question(writer, title, contents);
			QuestionDao dao = new QuestionDao();
			dao.insert(question);
			return "redirect:/";
		}else if(url.equals("/api/addanswer.next")){
			int qId = Integer.parseInt(request.getParameter("questionId"));
			String writer = request.getParameter("writer");
			String contents = request.getParameter("contents");
			Answer ans = new Answer(writer, contents, qId);
			AnswerDao dao = new AnswerDao();
			dao.insert(ans);//db에 답변을 입력했다.
			ArrayList<Answer> answerList = (ArrayList) dao.findAllByQuestionId(qId);
			//request.setAttribute("json", JSON.makeJSON(answerList));
			
			return "/api/show.next";//가는 길이 이 길이 아닐거야. 
		
		} else if(url.equals("/api/list.next")){
			QuestionDao qDao = new QuestionDao();
			List<Question> list= qDao.findAll();
			String json = JSON.makeJSON(list);
			response.getWriter().print(json);  
			return "api";
		}
				return "redirect:/";

	}
}
