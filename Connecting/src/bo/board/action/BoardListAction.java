package bo.board.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import bo.board.db.BoardBean;
import bo.board.db.BoardDAO;


public class BoardListAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		

		BoardDAO boarddao = new BoardDAO();
		List<BoardBean> boardlist = new ArrayList<BoardBean>();		
		
		/*
		 * �Խù� �ۼ� ��: 0;
		 * ���� ���� ��: 1;
		 * ���� ���� �ӹ� ��: 2;
		 * ���� ���� �ӹ� ��: 3;
		 */
		String orderby = request.getParameter("orderby");
		System.out.println("orderby: " + orderby);		
		
		// �α��� ���� �� �Ķ���� page�� �����ϴ�. �׷��� �ʱ� ���� �ʿ�.
		int page = 1; // ������ ������
		int limit = 5; // �� �������� ������ �Խ��� �� ����� ��
		
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page").trim());
		}		
		System.out.println("������ ������ - page: " + page);
		
		// �߰�
		if (request.getParameter("limit") != null) {
			limit = Integer.parseInt(request.getParameter("limit").trim());
		}
		System.out.println("�� �������� ������ �Խù��� ���� - limit: " + limit);
				
		// �� ���� ����
		int listcount = boarddao.getListCount();
		
		// ����Ʈ�� �޾ƿɴϴ�.		
		if (orderby == null || orderby.equals("") || orderby.equals("0")) {
			boardlist = boarddao.getBoardListOrderByDefault(page, limit);			
			request.setAttribute("orderby", "0");
		} else if (orderby.equals("1")) {
			boardlist = boarddao.getBoardListOrderByHeart(page, limit);			
			request.setAttribute("orderby", "1");
		} else if (orderby.equals("2")) {
			boardlist = boarddao.getBoardListOrderByStartDate(page, limit);			
			request.setAttribute("orderby", "2");
		} else if (orderby.equals("3")) {
			boardlist = boarddao.getBoardListOrderByEndDate(page, limit);			
			request.setAttribute("orderby", "3");
		}
		
		/*
		 * �� ������ ����: (DB�� ����� �� ����Ʈ�� �� + �� ���������� �����ִ� ����Ʈ�� �� - 1) / �� ���������� �����ִ� ����Ʈ�� ��
		 * 
		 * ���� ��� �� ���������� �����ִ� ����Ʈ�� ���� 10 ���� ���...
		 * 		��1) DB�� ����� �� ����Ʈ�� ���� 0�̸� �� �������� ���� 0������
		 * 		��2) DB�� ����� �� ����Ʈ�� ���� (01~10)�̸�, �� �������� ���� 1������
		 * 		��3) DB�� ����� �� ����Ʈ�� ���� (11~20)�̸�, �� �������� ���� 2������
		 * 		��4) DB�� ����� �� ����Ʈ�� ���� (21~30)�̸�, �� �������� ���� 3������
		 */
		int maxpage = (listcount + limit - 1) / limit; // limit�� �� �������� ������ ���� ����
		
		System.out.println("�� �������� ������ ���� ����: " + limit + ",    �ʿ��� �� ������ ����: " + maxpage);
		
		/*
		 * startpage: ���� ������ �׷쿡�� �� ó���� ǥ�õ� ������ ���� �ǹ�.
		 * ([1], [11], [21] ��...) ������ �������� 30���� ���,
		 * [1][2][3]...[30]���� �� ǥ���ϱ⿡�� �ʹ� ���� ������ ���� �� ���������� 10������ �������� �̵��� �� �ְ� ǥ��.
		 * 
		 * ��) ������ �׷��� �Ʒ��� ���� ���,
		 * 		[1][2][3][4][5][6][7][8][9][10]
		 * ������ �׷��� ���� �������� startpage��, ������ �������� endpage�� ���մϴ�.
		 * 
		 * ����, 1~10�������� ������ Ÿ������ ������ �׷��� [1][2]...[10]���� ǥ�õǰ�,
		 * 11~20�������� ������ ��Ÿ������ ������ �׷��� [11][12]...[20]���� ǥ�õȴ�. 
		 */
		
		// page == ������ ������
		int startpage = ((page - 1) / 10) * 10 + 1;
		System.out.println("���� ����¡�׷쿡 ������ ���� ��������? - startpage: " + startpage);
		
		int endpage = startpage + 10 - 1;
		System.out.println("���� ����¡�׷쿡 ������ ������ ��������? - endpage: " + endpage);
		
		/*
		 * ������ �׷��� ������ ������ ���� �ִ� ������ ���̴�.
		 * ����, ������ ������ �׷��� [21]~[30]�� ��쿡��
		 * ����������(startpage==21), ������������(endpage==30) ������, 
		 * �ִ� �ʿ��� ������(maxpage)�� 25���, [21][22]...[25]������ ǥ�õǵ��� �ؾߵȴ�.
		 */
		if (endpage > maxpage) {
			endpage = maxpage;
		}
		
		String state = request.getParameter("state");
		System.out.println("state: " + state);		
		
		if (state == null) {
			System.out.println("state == null");
			request.setAttribute("page", page); // ���� ������ ������
			request.setAttribute("maxpage", maxpage); // �ʿ��� �� �������� ����
			
			// ���� �������� ǥ���� ù ������ ��
			request.setAttribute("startpage", startpage);
			
			// ���� �������� ǥ���� �� ������ ��
			request.setAttribute("endpage", endpage);
			
			// �� ���� ����
			request.setAttribute("listcount", listcount);
			
			// �ش� �������� �� ����� ���� �ִ� ����Ʈ
			request.setAttribute("boardlist", boardlist);
			
			// �� �������� ������ �ִ� ���ñ� ����
			request.setAttribute("limit", limit);
			
			ActionForward forward = new ActionForward();
			forward.setRedirect(false);
			
			// �� ��� �������� �̵��ϱ� ���� ��� ����
			forward.setPath("board/boardList.jsp");
			return forward;			
		} else {
			System.out.println("state == ajax");
			
			// ������ request�� ��Ҵ� ���� JsonObject�� ����ϴ�.
			JsonObject object = new JsonObject();
			object.addProperty("page", page);
			object.addProperty("maxpage", maxpage); // �ʿ��� �� ������ ����
			object.addProperty("startpage", startpage);
			object.addProperty("endpage", endpage);
			object.addProperty("listcount", listcount); // �� ���� ����
			object.addProperty("limit", limit);
			
			/*
			 * JsonObject�� List ������ ���� �� �ִ� addProperty �޼���� ����
			 * void com.google.gson.JsonObject.add(String property, JsonElement value) �޼��带 �̿��Ͽ� �����ؾ� ��
			 * List ������ JsonElement�� �ٲ� ��߸� object�� ������ �� ����
			 * List => JsonElement
			 */
			
			JsonElement je = new Gson().toJsonTree(boardlist);
			System.out.println("JsonElement type - je.toString(): \n" + je.toString());
			object.add("boardlist", je);
			
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().append(object.toString());
			System.out.println("Java���� JSP/AJAX�� ������ ��� JsonObject�� toString(): \n" + object.toString());
			return null;
		}
		
	}

}
