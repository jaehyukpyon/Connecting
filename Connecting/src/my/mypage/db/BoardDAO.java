/*
 DAO(Data Access Object) Ŭ����
 -������ ���̽��� �����Ͽ� ���ڵ��� �߰�, ����, ���� �۾��� �̷������ Ŭ���� �Դϴ�.
 -� ActionŬ������ ȣ��Ǵ��� �׿� �ش��ϴ� ������ ���̽� ���� ó����
 	DAOŬ�������� �̷������ �˴ϴ�.
 */

package my.mypage.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class BoardDAO {
	
	private DataSource ds;
	
	//�����ڿ��� JNDI ���ҽ��� �����Ͽ� Connection ��ü�� ���ɴϴ�.
	public BoardDAO() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		}catch (Exception e) {
			System.out.println("DB���� ����:" + e);
		}
	}

	
	
	public List<Board> getMyBoard(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Board> boardlist = new ArrayList<Board>();
		
		try {
			String sql = "select * "
					+ "from board where id = ?";
			
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);;
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Board board = new Board();
				board.setBoard_id(rs.getInt("board_id"));
				board.setCategory(rs.getInt("category"));
				board.setLoc(rs.getInt("loc"));
				board.setId(rs.getString("id"));
				board.setTitle(rs.getString("title"));
				board.setHost_name(rs.getString("host_name"));
				board.setAddress(rs.getString("address"));
				board.setStart_date(rs.getString("start_date"));
				board.setEnd_date(rs.getString("end_date"));
				board.setStart_time(rs.getString("start_time"));
				board.setEnd_time(rs.getString("end_time"));
				board.setWrite_date(rs.getString("write_date"));
				board.setContent(rs.getString("content"));
				board.setBoard_img(rs.getString("board_img"));
				boardlist.add(board);
			}
			
			
		} catch (Exception ex) {
			System.out.println("getListCount() ����: " + ex);
		} finally {
			if (rs != null)
				try {
					rs.close();
				}catch(SQLException ex) {
					ex.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				}catch(SQLException ex) {
					ex.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
		}

		return boardlist;
		
	}
	

	
	
	
	
	
	
}
