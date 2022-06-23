package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.InquiryDto;
import helper.DaoHelper;
import vo.Inquiry;

public class InquiryDao {

	private static InquiryDao instance = new InquiryDao();
	private InquiryDao() {}
	public static InquiryDao getInstance() {
		return instance;
	}
	
	private DaoHelper helper = DaoHelper.getInstance();
	
	public void insertInquiry(Inquiry inquiry) throws SQLException {
		String sql = "insert into hta_inquiries "
				   + "(inquiry_no, inquiry_title, inquiry_content, user_no) "
				   + "values "
				   + "(inquiries_seq.nextval, ?, ?, ?) ";
		
		helper.insert(sql, inquiry.getTitle(), inquiry.getContent(), inquiry.getUserNo());
	}
	
	public int getTotalRows(int userNo) throws SQLException {
	      String sql = "select count(*) cnt "
	               + "from hta_inquiries "
	               + "where inquiry_deleted = 'N' "
	               + "and user_no = ? ";
	      
	      return helper.selectOne(sql, rs -> {
	         return rs.getInt("cnt");
	      }, userNo);
	   }
	   
    public int getTotalRows(String keyword, int userNo) throws SQLException {
	      String sql = "select count(*) cnt "
	               + "from hta_inquiries "
	               + "where inquiry_deleted = 'N' and inquiry_title like '%' || ? || '%' "
	               + "and user_no = ? ";
	      
	      return helper.selectOne(sql, rs -> {
	         return rs.getInt("cnt");
	      }, keyword, userNo);
    }
    
	/**
	 * 모든 1:1 문의글 조회하기 
	 * @return inquiry 게시글 정보
	 * @throws SQLException
	 */
	public List<InquiryDto> getAllInquiries() throws SQLException {
		String sql = "SELECT I.INQUIRY_NO, I.USER_NO, U.USER_NAME, I.INQUIRY_TITLE, I.INQUIRY_CONTENT, I.INQUIRY_DELETED, I.INQUIRY_CREATED_DATE, I.INQUIRY_UPDATED_DATE, I.INQUIRY_ANSWER_CONTENT,I.INQUIRY_ANSWER_CREATED_DATE, I.INQUIRY_ANSWER_UPDATED_DATE, I.INQUIRY_ANSWER_STATUS "
				   + "FROM HTA_INQUIRIES I, HTA_USERS U "
				   + "WHERE I.USER_NO = U.USER_NO ";
		return helper.selectList(sql, rs-> {
			InquiryDto inquiry = new InquiryDto();
					
			inquiry.setNo(rs.getInt("inquiry_no"));
			inquiry.setUserNo(rs.getInt("user_no"));
			inquiry.setUserName(rs.getString("user_name"));
			inquiry.setTitle(rs.getString("inquiry_title"));
			inquiry.setContent(rs.getString("inquiry_content"));
			inquiry.setDeleted(rs.getString("inquiry_deleted"));
			inquiry.setCreatedDate(rs.getDate("inquiry_created_date"));
			inquiry.setUpdatedDate(rs.getDate("inquiry_updated_date"));
			inquiry.setAnswerContent(rs.getString("inquiry_answer_content"));
			inquiry.setAnswerCreatedDate(rs.getDate("inquiry_answer_created_date"));
			inquiry.setAnswerUpdatedDate(rs.getDate("inquiry_answer_updated_date"));
			inquiry.setAnswerStatus(rs.getString("inquiry_answer_status"));
					
			return inquiry;
		});
	}
	
	/**
	 * 페이징처리한 1:1문의 게시글 조회하기
	 * @param beginIndex 페이지 시작번호
	 * @param endIndex 페이지 끝번호
	 * @return inquiry 게시글 정보
	 * @throws SQLException
	 */
	public List<InquiryDto> getInquiries(int beginIndex, int endIndex, int userNo) throws SQLException {
		String sql = "SELECT I.INQUIRY_NO, U.USER_NO, U.USER_NAME, I.INQUIRY_TITLE, I.INQUIRY_CONTENT, I.INQUIRY_DELETED, I.INQUIRY_CREATED_DATE, I.INQUIRY_UPDATED_DATE, I.INQUIRY_ANSWER_CONTENT,I.INQUIRY_ANSWER_CREATED_DATE, I.INQUIRY_ANSWER_UPDATED_DATE, I.INQUIRY_ANSWER_STATUS "
				   + "FROM (SELECT INQUIRY_NO, USER_NO, INQUIRY_TITLE, INQUIRY_CONTENT, INQUIRY_DELETED, INQUIRY_CREATED_DATE, INQUIRY_UPDATED_DATE, INQUIRY_ANSWER_CONTENT, INQUIRY_ANSWER_CREATED_DATE, INQUIRY_ANSWER_UPDATED_DATE, INQUIRY_ANSWER_STATUS, "
				   + "		ROW_NUMBER() OVER (ORDER BY INQUIRY_NO DESC) ROW_NUMBER "
				   + "		FROM HTA_INQUIRIES "
				   + "      where USER_NO = ?"
				   + "		and INQUIRY_DELETED = 'N') I , HTA_USERS U "
				   + "WHERE I.USER_NO = U.USER_NO "
				   + "AND I.ROW_NUMBER >= ? AND I.ROW_NUMBER <= ? "
				 
				   + "ORDER BY I.INQUIRY_NO DESC ";
		return helper.selectList(sql, rs-> {
			InquiryDto inquiry = new InquiryDto();
					
			inquiry.setNo(rs.getInt("inquiry_no"));
			inquiry.setUserNo(rs.getInt("user_no"));
			inquiry.setUserName(rs.getString("user_name"));
			inquiry.setTitle(rs.getString("inquiry_title"));
			inquiry.setContent(rs.getString("inquiry_content"));
			inquiry.setDeleted(rs.getString("inquiry_deleted"));
			inquiry.setCreatedDate(rs.getDate("inquiry_created_date"));
			inquiry.setUpdatedDate(rs.getDate("inquiry_updated_date"));
			inquiry.setAnswerContent(rs.getString("inquiry_answer_content"));
			inquiry.setAnswerCreatedDate(rs.getDate("inquiry_answer_created_date"));
			inquiry.setAnswerUpdatedDate(rs.getDate("inquiry_answer_updated_date"));
			inquiry.setAnswerStatus(rs.getString("inquiry_answer_status"));
					
			return inquiry;
		},userNo, beginIndex, endIndex);
	}
	
	/**
	 * 페이징처리 + 검색 1:1 문의 게시글 조회하기
	 * @param beginIndex 페이지 시작번호
	 * @param endIndex 페이지 끝번호
	 * @param keyword 검색어
	 * @return inquiry 게시글 정보
	 * @throws SQLException
	 */
	public List<InquiryDto> getInquiries(int beginIndex, int endIndex, String keyword, int userNo) throws SQLException {
		String sql = "SELECT I.INQUIRY_NO, U.USER_NO, U.USER_NAME, I.INQUIRY_TITLE, I.INQUIRY_CONTENT, I.INQUIRY_DELETED, I.INQUIRY_CREATED_DATE, I.INQUIRY_UPDATED_DATE, I.INQUIRY_ANSWER_CONTENT,I.INQUIRY_ANSWER_CREATED_DATE, I.INQUIRY_ANSWER_UPDATED_DATE, I.INQUIRY_ANSWER_STATUS "
				   + "FROM (SELECT INQUIRY_NO, USER_NO, INQUIRY_TITLE, INQUIRY_CONTENT, INQUIRY_DELETED, INQUIRY_CREATED_DATE, INQUIRY_UPDATED_DATE, INQUIRY_ANSWER_CONTENT, INQUIRY_ANSWER_CREATED_DATE, INQUIRY_ANSWER_UPDATED_DATE, INQUIRY_ANSWER_STATUS, "
				   + "		ROW_NUMBER() OVER (ORDER BY INQUIRY_NO DESC) ROW_NUMBER "
				   + "		FROM HTA_INQUIRIES "
				   + "      where USER_NO = ? "
				   + "		and INQUIRY_DELETED = 'N' and INQUIRY_TITLE like '%' || ? || '%') I , HTA_USERS U "
				   + "WHERE I.USER_NO = U.USER_NO "
				   + "AND I.ROW_NUMBER >= ? AND I.ROW_NUMBER <= ? "
				   + "AND U.USER_NO = ? "
				   + "ORDER BY I.INQUIRY_NO DESC ";
		return helper.selectList(sql, rs-> {
			InquiryDto inquiry = new InquiryDto();
					
			inquiry.setNo(rs.getInt("inquiry_no"));
			inquiry.setUserNo(rs.getInt("user_no"));
			inquiry.setUserName(rs.getString("user_name"));
			inquiry.setTitle(rs.getString("inquiry_title"));
			inquiry.setContent(rs.getString("inquiry_content"));
			inquiry.setDeleted(rs.getString("inquiry_deleted"));
			inquiry.setCreatedDate(rs.getDate("inquiry_created_date"));
			inquiry.setUpdatedDate(rs.getDate("inquiry_updated_date"));
			inquiry.setAnswerContent(rs.getString("inquiry_answer_content"));
			inquiry.setAnswerCreatedDate(rs.getDate("inquiry_answer_created_date"));
			inquiry.setAnswerUpdatedDate(rs.getDate("inquiry_answer_updated_date"));
			inquiry.setAnswerStatus(rs.getString("inquiry_answer_status"));
					
			return inquiry;
		}, userNo, keyword, beginIndex, endIndex);
	}
	
	/**
	 * 1:1 문의 게시글 번호를 입력받아 게시글 정보를 반환
	 * @param inquiryNo 1:1 문의 게시글 번호
	 * @return inquiry 게시글 정보
	 * @throws SQLException
	 */
	public InquiryDto getInquiryByNo(int inquiryNo) throws SQLException {
		String sql = "SELECT I.INQUIRY_NO, U.USER_NO, U.USER_NAME, I.INQUIRY_TITLE, I.INQUIRY_CONTENT, I.INQUIRY_DELETED, I.INQUIRY_CREATED_DATE, I.INQUIRY_UPDATED_DATE, I.INQUIRY_ANSWER_CONTENT,I.INQUIRY_ANSWER_CREATED_DATE, I.INQUIRY_ANSWER_UPDATED_DATE, I.INQUIRY_ANSWER_STATUS "
				   + "FROM HTA_INQUIRIES I, HTA_USERS U "
				   + "WHERE I.INQUIRY_NO = ? "
				   + "AND I.USER_NO = U.USER_NO ";
		
		return helper.selectOne(sql, rs-> { 
			
			InquiryDto inquiry = new InquiryDto();
			
			inquiry.setNo(rs.getInt("inquiry_no"));
			inquiry.setUserNo(rs.getInt("user_no"));
			inquiry.setUserName(rs.getString("user_name"));
			inquiry.setTitle(rs.getString("inquiry_title"));
			inquiry.setContent(rs.getString("inquiry_content"));
			inquiry.setDeleted(rs.getString("inquiry_deleted"));
			inquiry.setCreatedDate(rs.getDate("inquiry_created_date"));
			inquiry.setUpdatedDate(rs.getDate("inquiry_updated_date"));
			inquiry.setAnswerContent(rs.getString("inquiry_answer_content"));
			inquiry.setAnswerCreatedDate(rs.getDate("inquiry_answer_created_date"));
			inquiry.setAnswerUpdatedDate(rs.getDate("inquiry_answer_updated_date"));
			inquiry.setAnswerStatus(rs.getString("inquiry_answer_status"));		
			return inquiry;
			
		}, inquiryNo);
	}
	
	public void updateInquiry(InquiryDto inquiry) throws SQLException {
		String sql = "UPDATE HTA_INQUIRIES "
				   + "SET "
				   + " 		INQUIRY_TITLE = ?, "
				   + "		INQUIRY_CONTENT = ?, "
				   + "		INQUIRY_DELETED = ?, "
				   + "		INQUIRY_UPDATED_DATE = SYSDATE "
				   + "WHERE INQUIRY_NO = ? ";
		
	    helper.update(sql, inquiry.getTitle(), inquiry.getContent(), inquiry.getDeleted(), inquiry.getNo());
	}
	
}
