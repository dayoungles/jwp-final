package core.mvc;

import java.util.HashMap;
import java.util.Map;

import next.controller.ListController;
import next.controller.ShowController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestMapping {
	private static final Logger logger = LoggerFactory.getLogger(FrontController.class);
	private Map<String, Controller> mappings = new HashMap<String, Controller>();
	
	public void initMapping() {
		mappings.put("/list.next", new ListController());
		mappings.put("/show.next", new ShowController());
		mappings.put("/form.next", new ForwardController("form.jsp"));
		mappings.put("/save.next", new FrontController());
		mappings.put("/api/addanswer.next", new FrontController());
		mappings.put("/api/show.next", new FrontController());
		mappings.put("/api/list.next", new FrontController());
		logger.info("Initialized Mapping Completed!");
	}

	public Controller findController(String url) {//url을 받아서, mappings에서 url을 찾는다. 
		//근데 mappings에 url이 없으면? 내가 미리 넣어놔야 하나?
		return mappings.get(url);
	}

	void put(String url, Controller controller) {
		mappings.put(url, controller);
	}
	
	void get(String url, Controller controller){
		mappings.get(url);
	}
	

}
