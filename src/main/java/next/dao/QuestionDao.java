package next.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import next.model.Question;
import next.support.db.ConnectionManager;

public class QuestionDao {

	public void insert(Question question) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		
		try {
			con = ConnectionManager.getConnection();
			String sql = "INSERT INTO QUESTIONS (writer, title, contents, createdDate, countOfComment) VALUES (?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, question.getWriter());
			pstmt.setString(2, question.getTitle());
			pstmt.setString(3, question.getContents());
			pstmt.setTimestamp(4, new Timestamp(question.getTimeFromCreateDate()));
			pstmt.setInt(5, question.getCountOfComment());

			pstmt.executeUpdate();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}

			if (con != null) {
				con.close();
			}
		}		
	}

	public List<Question> findAll() throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT questionId, writer, title, createdDate, countOfComment FROM QUESTIONS " + 
					"order by questionId desc";
//			
//			ReadTemplate<List<Question>> template = new ReadTemplate<List<Question>>(sql) {
//				
//				@Override
//				public List<Question> read(ResultSet rs) throws SQLException {
//					List<Question> list = new ArrayList<Question>();
//					while(rs.next()){
//						long questionId = rs.getLong("questionId");
//						String writer = rs.getString("writer");
//						String title = rs.getString("title");
//						String contents = rs.getString("contents");
//						long createdDate = rs.getLong("createdDate");
//						Date date = new Date(createdDate);
//						int countOfComment = rs.getInt("countOfComment");
//						list.add(new Question(questionId, writer, title, contents, date, countOfComment));
//					}
//					return list;
//				}
//			};
//			
//			return (List<Question>) template.execute();
			
			
			pstmt = con.prepareStatement(sql);

			rs = pstmt.executeQuery();

			List<Question> questions = new ArrayList<Question>();
			Question question = null;

			while (rs.next()) {
				question = new Question(
						rs.getLong("questionId"),
						rs.getString("writer"),
						rs.getString("title"),
						null,
						rs.getTimestamp("createdDate"),
						rs.getInt("countOfComment"));
				questions.add(question);
			}

			return questions;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
		}
	}

	public Question findById(long questionId) throws SQLException {
		Connection con = null;
		con = ConnectionManager.getConnection();
		String sql = "SELECT questionId, writer, title, contents, createdDate, countOfComment FROM QUESTIONS " + 
				"WHERE questionId = ?";
		
//		ReadTemplate<Question> template = new ReadTemplate<Question>(sql, questionId) {
//			
//			@Override
//			public Question read(ResultSet rs) throws SQLException {
//				Question question = null;
//				while(rs.next()){
//					long questionId = rs.getLong("questionId");
//					String writer = rs.getString("writer");
//					String title = rs.getString("title");
//					String contents = rs.getString("contents");
//					long createdDate = rs.getLong("createdDate");
//					Date date = new Date(createdDate);
//					int countOfComment = rs.getInt("countOfComment");
//					question= new Question(questionId, writer, title, contents, date, countOfComment);
//				}
//				return question;
//			}
//		};
//		
//		return (Question)template.execute();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();

			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, questionId);

			rs = pstmt.executeQuery();

			Question question = null;
			if (rs.next()) {
				question = new Question(
						rs.getLong("questionId"),
						rs.getString("writer"),
						rs.getString("title"),
						rs.getString("contents"),
						rs.getTimestamp("createdDate"),
						rs.getInt("countOfComment"));
			}

			return question;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
		}
	}
	

}
