package my.mypage.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import my.mypage.db.Member;
import my.mypage.db.MemberDAO;



public class MemberUpdateProcessAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		Member m = new Member();
		m.setId(id);
		m.setPassword(pass);
		m.setName(name);
		m.setEmail(email);
		
		MemberDAO mdao = new MemberDAO();
		int result = mdao.updateInfo(m);
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		out.println("<script>");
		//������ �� ���
		if (result == 1) {
			out.println("alert('�����Ǿ����ϴ�.');");
			out.println("location.href='memberInfo.my';");
		} else {
			out.println("alert('ȸ�� ���� ������ �����߽��ϴ�.');");
			out.println("history.back()");//��й�ȣ�� ������ �ٸ� �����ʹ� ���� �Ǿ� �ֽ��ϴ�.
		}
		out.println("</script>");
		out.close();
		return null;
	}

}
