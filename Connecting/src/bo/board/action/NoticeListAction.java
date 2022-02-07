package bo.board.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import bo.board.db.NoticeBean;
import bo.board.db.NoticeDAO;

public class NoticeListAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		NoticeDAO dao = new NoticeDAO();
		List<NoticeBean> noticelist = dao.getAllNotices();
		
		if (noticelist != null) {
			JsonObject object = new JsonObject();
			object.addProperty("listcount", noticelist.size()); // �� ���� ����
			
			/*
			 * JsonObject�� List ������ ���� �� �ִ� addProperty �޼���� ����
			 * void com.google.gson.JsonObject.add(String property, JsonElement value) �޼��带 �̿��Ͽ� �����ؾ� ��
			 * List ������ JsonElement�� �ٲ� ��߸� object�� ������ �� ����
			 * List => JsonElement
			 */
			
			JsonElement je = new Gson().toJsonTree(noticelist);
			System.out.println("JsonElement type - je.toString(): \n" + je.toString());
			object.add("noticelist", je);
			
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().append(object.toString());
			System.out.println("Java���� JSP/AJAX�� ������ ��� JsonObject�� toString(): \n" + object.toString());
		} else {
			
		}
		return null;
	}

}
