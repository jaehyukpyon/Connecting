package bo.board.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {

	private DataSource ds;

	public BoardDAO() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception e) {
			System.out.println("Oracle DB ���� ����: " + e.getMessage());
		}
	}

	public int getListCount() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int totalArticleNo = 0;
		
		try {
			con = ds.getConnection();
			
			String selectSQL = "SELECT COUNT(*) "
					         + "FROM BOARD_COPY";
			
			pstmt = con.prepareStatement(selectSQL);			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				totalArticleNo = rs.getInt(1);
			}
			
			System.out.println("DB�� ����� �� �Խñ��� ��: " + totalArticleNo + "��");
		} catch (Exception e) {
			System.out.println("BoardDAO - getListCount() exception - " + e.getMessage());
			e.printStackTrace();
		} finally {	
			try {
				if (rs != null) rs.close();
			} catch (Exception e) {
				System.out.println("ResultSet �ݴ� �� ���� �߻�.");
			}
			try {
				if (pstmt != null) pstmt.close();
			} catch (Exception e) {
				System.out.println("PreparedStatement �ݴ� �� ���� �߻�.");
			}
			try {
				if (con != null) con.close();
			} catch (Exception e) {
				System.out.println("Connection �ݴ� �� ���� �߻�.");
			}
		}
		return totalArticleNo;
	}

	public List<BoardBean> getBoardListOrderByDefault(int page, int limit) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		/*
		 * page: ���� ������
		 * limit: �� �������� �ִ� ���ñ� ��
		 * BOARD_RE_REF DESC, BOARD_RE_SEQ ASC �� ���� ���ĵ� ����,
		 * �������� �´� RNUM�� ���� ��ŭ �������� ������
		 */
		
		String selectSQL = "SELECT * " +
		                   "FROM (SELECT ROWNUM RNUM, J.* " +
		                   "      FROM (SELECT BOARD_COPY.*, NVL(HEART_NUM, 0) HEART_NUM " +
		                   "            FROM BOARD_COPY LEFT OUTER JOIN BOARD_HEART_COPY " +
		                   "            ON BOARD_COPY.BOARD_ID = BOARD_HEART_COPY.BOARD_ID " + 
		                   "            ORDER BY BOARD_COPY.BOARD_ID DESC) J " + 
		                   "      )" + 
		                   "WHERE RNUM >= ? AND RNUM <= ?";
		
		
		List<BoardBean> list = new ArrayList<>();
		
		int startrow = (page - 1) * limit + 1;
		int endrow = startrow + limit - 1;
		
		System.out.println("startrow: " + startrow);
		System.out.println("endrow: " + endrow);
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, startrow);
			pstmt.setInt(2, endrow);
			
			rs = pstmt.executeQuery();
			BoardBean board = null;
			
			while (rs.next()) {
				board = new BoardBean();
				
				board.setBoard_id(rs.getInt("BOARD_ID"));
				board.setCategory(rs.getInt("CATEGORY"));
				board.setLoc(rs.getInt("LOC"));
				board.setId(rs.getString("ID"));
				board.setTitle(rs.getString("TITLE"));
				board.setHost_name(rs.getString("HOST_NAME"));
				board.setAddress(rs.getString("ADDRESS"));
				board.setStart_date(rs.getString("START_DATE"));
				board.setEnd_date(rs.getString("END_DATE"));
				board.setStart_time(rs.getString("START_TIME"));
				board.setEnd_time(rs.getString("END_TIME"));
				board.setWrite_date(rs.getString("WRITE_DATE").substring(0, 10));
				board.setContent(rs.getString("CONTENT"));
				board.setBoard_img(rs.getString("BOARD_IMG"));
				
				board.setHeart_num(rs.getInt("HEART_NUM"));
				
				list.add(board);
			}
		} catch (Exception e) {
			System.out.println("BoardDAO - getBoardList(int page, int limit) exception - " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
			} catch (Exception e) {
				System.out.println("ResultSet �ݴ� �� ���� �߻�.");
			}
			try {
				if (pstmt != null) pstmt.close();
			} catch (Exception e) {
				System.out.println("PreparedStatement �ݴ� �� ���� �߻�.");
			}
			try {
				if (con != null) con.close();
			} catch (Exception e) {
				System.out.println("Connection �ݴ� �� ���� �߻�.");
			}
		}
		return list;
	}

	public List<BoardBean> getBoardListOrderByHeart(int page, int limit) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		/*
		 * page: ���� ������
		 * limit: �� �������� �ִ� ���ñ� ��
		 * BOARD_RE_REF DESC, BOARD_RE_SEQ ASC �� ���� ���ĵ� ����,
		 * �������� �´� RNUM�� ���� ��ŭ �������� ������
		 */
		
		String selectSQL = "SELECT * " +
		                   "FROM (SELECT ROWNUM RNUM, J.* " +
		                   "      FROM (SELECT BOARD_COPY.*, NVL(HEART_NUM, 0) HEART_NUM " +
		                   "            FROM BOARD_COPY LEFT OUTER JOIN BOARD_HEART_COPY " +
		                   "            ON BOARD_COPY.BOARD_ID = BOARD_HEART_COPY.BOARD_ID " + 
		                   "            ORDER BY HEART_NUM DESC, BOARD_COPY.BOARD_ID DESC) J " + 
		                   "      )" + 
		                   "WHERE RNUM >= ? AND RNUM <= ?";
		
		
		List<BoardBean> list = new ArrayList<>();
		
		int startrow = (page - 1) * limit + 1;
		int endrow = startrow + limit - 1;
		
		System.out.println("startrow: " + startrow);
		System.out.println("endrow: " + endrow);
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, startrow);
			pstmt.setInt(2, endrow);
			
			rs = pstmt.executeQuery();
			BoardBean board = null;
			
			while (rs.next()) {
				board = new BoardBean();
				
				board.setBoard_id(rs.getInt("BOARD_ID"));
				board.setCategory(rs.getInt("CATEGORY"));
				board.setLoc(rs.getInt("LOC"));
				board.setId(rs.getString("ID"));
				board.setTitle(rs.getString("TITLE"));
				board.setHost_name(rs.getString("HOST_NAME"));
				board.setAddress(rs.getString("ADDRESS"));
				board.setStart_date(rs.getString("START_DATE"));
				board.setEnd_date(rs.getString("END_DATE"));
				board.setStart_time(rs.getString("START_TIME"));
				board.setEnd_time(rs.getString("END_TIME"));
				board.setWrite_date(rs.getString("WRITE_DATE").substring(0, 10));
				board.setContent(rs.getString("CONTENT"));
				board.setBoard_img(rs.getString("BOARD_IMG"));
				
				board.setHeart_num(rs.getInt("HEART_NUM"));
				
				list.add(board);
			}
		} catch (Exception e) {
			System.out.println("BoardDAO - getBoardList(int page, int limit) exception - " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
			} catch (Exception e) {
				System.out.println("ResultSet �ݴ� �� ���� �߻�.");
			}
			try {
				if (pstmt != null) pstmt.close();
			} catch (Exception e) {
				System.out.println("PreparedStatement �ݴ� �� ���� �߻�.");
			}
			try {
				if (con != null) con.close();
			} catch (Exception e) {
				System.out.println("Connection �ݴ� �� ���� �߻�.");
			}
		}
		return list;
	}

	public List<BoardBean> getBoardListOrderByStartDate(int page, int limit) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		/*
		 * page: ���� ������
		 * limit: �� �������� �ִ� ���ñ� ��
		 * BOARD_RE_REF DESC, BOARD_RE_SEQ ASC �� ���� ���ĵ� ����,
		 * �������� �´� RNUM�� ���� ��ŭ �������� ������
		 */
		
		String selectSQL = "SELECT * " +
		                   "FROM (SELECT ROWNUM RNUM, J.* " +
		                   "      FROM (SELECT BOARD_COPY.*, NVL(HEART_NUM, 0) HEART_NUM " +
		                   "            FROM BOARD_COPY LEFT OUTER JOIN BOARD_HEART_COPY " +
		                   "            ON BOARD_COPY.BOARD_ID = BOARD_HEART_COPY.BOARD_ID " + 
		                   "            ORDER BY START_DATE ASC, BOARD_COPY.BOARD_ID DESC) J " + 
		                   "      )" + 
		                   "WHERE RNUM >= ? AND RNUM <= ?";
		
		
		List<BoardBean> list = new ArrayList<>();
		
		int startrow = (page - 1) * limit + 1;
		int endrow = startrow + limit - 1;
		
		System.out.println("startrow: " + startrow);
		System.out.println("endrow: " + endrow);
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, startrow);
			pstmt.setInt(2, endrow);
			
			rs = pstmt.executeQuery();
			BoardBean board = null;
			
			while (rs.next()) {
				board = new BoardBean();
				
				board.setBoard_id(rs.getInt("BOARD_ID"));
				board.setCategory(rs.getInt("CATEGORY"));
				board.setLoc(rs.getInt("LOC"));
				board.setId(rs.getString("ID"));
				board.setTitle(rs.getString("TITLE"));
				board.setHost_name(rs.getString("HOST_NAME"));
				board.setAddress(rs.getString("ADDRESS"));
				board.setStart_date(rs.getString("START_DATE"));
				board.setEnd_date(rs.getString("END_DATE"));
				board.setStart_time(rs.getString("START_TIME"));
				board.setEnd_time(rs.getString("END_TIME"));
				board.setWrite_date(rs.getString("WRITE_DATE").substring(0, 10));
				board.setContent(rs.getString("CONTENT"));
				board.setBoard_img(rs.getString("BOARD_IMG"));
				
				board.setHeart_num(rs.getInt("HEART_NUM"));
				
				list.add(board);
			}
		} catch (Exception e) {
			System.out.println("BoardDAO - getBoardList(int page, int limit) exception - " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
			} catch (Exception e) {
				System.out.println("ResultSet �ݴ� �� ���� �߻�.");
			}
			try {
				if (pstmt != null) pstmt.close();
			} catch (Exception e) {
				System.out.println("PreparedStatement �ݴ� �� ���� �߻�.");
			}
			try {
				if (con != null) con.close();
			} catch (Exception e) {
				System.out.println("Connection �ݴ� �� ���� �߻�.");
			}
		}
		return list;
	}

	public List<BoardBean> getBoardListOrderByEndDate(int page, int limit) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		/*
		 * page: ���� ������
		 * limit: �� �������� �ִ� ���ñ� ��
		 * BOARD_RE_REF DESC, BOARD_RE_SEQ ASC �� ���� ���ĵ� ����,
		 * �������� �´� RNUM�� ���� ��ŭ �������� ������
		 */
		
		String selectSQL = "SELECT * " +
		                   "FROM (SELECT ROWNUM RNUM, J.* " +
		                   "      FROM (SELECT BOARD_COPY.*, NVL(HEART_NUM, 0) HEART_NUM " +
		                   "            FROM BOARD_COPY LEFT OUTER JOIN BOARD_HEART_COPY " +
		                   "            ON BOARD_COPY.BOARD_ID = BOARD_HEART_COPY.BOARD_ID " + 
		                   "            ORDER BY END_DATE ASC, BOARD_COPY.BOARD_ID DESC) J " + 
		                   "      )" + 
		                   "WHERE RNUM >= ? AND RNUM <= ?";
		
		
		List<BoardBean> list = new ArrayList<>();
		
		int startrow = (page - 1) * limit + 1;
		int endrow = startrow + limit - 1;
		
		System.out.println("startrow: " + startrow);
		System.out.println("endrow: " + endrow);
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, startrow);
			pstmt.setInt(2, endrow);
			
			rs = pstmt.executeQuery();
			BoardBean board = null;
			
			while (rs.next()) {
				board = new BoardBean();
				
				board.setBoard_id(rs.getInt("BOARD_ID"));
				board.setCategory(rs.getInt("CATEGORY"));
				board.setLoc(rs.getInt("LOC"));
				board.setId(rs.getString("ID"));
				board.setTitle(rs.getString("TITLE"));
				board.setHost_name(rs.getString("HOST_NAME"));
				board.setAddress(rs.getString("ADDRESS"));
				board.setStart_date(rs.getString("START_DATE"));
				board.setEnd_date(rs.getString("END_DATE"));
				board.setStart_time(rs.getString("START_TIME"));
				board.setEnd_time(rs.getString("END_TIME"));
				board.setWrite_date(rs.getString("WRITE_DATE").substring(0, 10));
				board.setContent(rs.getString("CONTENT"));
				board.setBoard_img(rs.getString("BOARD_IMG"));
				
				board.setHeart_num(rs.getInt("HEART_NUM"));
				
				list.add(board);
			}
		} catch (Exception e) {
			System.out.println("BoardDAO - getBoardList(int page, int limit) exception - " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
			} catch (Exception e) {
				System.out.println("ResultSet �ݴ� �� ���� �߻�.");
			}
			try {
				if (pstmt != null) pstmt.close();
			} catch (Exception e) {
				System.out.println("PreparedStatement �ݴ� �� ���� �߻�.");
			}
			try {
				if (con != null) con.close();
			} catch (Exception e) {
				System.out.println("Connection �ݴ� �� ���� �߻�.");
			}
		}
		return list;
	}

	public BoardBean getDetail(int boardId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardBean board = null;
		
		String selectSQL = "SELECT * " +
		                   "FROM BOARD_COPY " +
				           "WHERE BOARD_ID = ?";		
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, boardId);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				board = new BoardBean();
				
				board.setBoard_id(rs.getInt("BOARD_ID"));
				board.setCategory(rs.getInt("CATEGORY"));
				board.setLoc(rs.getInt("LOC"));
				board.setId(rs.getString("ID"));
				board.setTitle(rs.getString("TITLE"));
				board.setHost_name(rs.getString("HOST_NAME"));
				board.setAddress(rs.getString("ADDRESS"));
				board.setStart_date(rs.getString("START_DATE"));
				board.setEnd_date(rs.getString("END_DATE"));
				board.setStart_time(rs.getString("START_TIME"));
				board.setEnd_time(rs.getString("END_TIME"));
				board.setWrite_date(rs.getString("WRITE_DATE").substring(0, 10));
				board.setContent(rs.getString("CONTENT"));
				board.setBoard_img(rs.getString("BOARD_IMG"));
			}
		} catch (Exception e) {
			System.out.println("BoardDAO - getDetail(int boardId) exception - " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
			} catch (Exception e) {
				System.out.println("ResultSet �ݴ� �� ���� �߻�.");
			}
			try {
				if (pstmt != null) pstmt.close();
			} catch (Exception e) {
				System.out.println("PreparedStatement �ݴ� �� ���� �߻�.");
			}
			try {
				if (con != null) con.close();
			} catch (Exception e) {
				System.out.println("Connection �ݴ� �� ���� �߻�.");
			}
		}
		return board;
	}
	
} // whole BoardDAO class ends
