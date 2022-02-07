package net.member.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.member.db.Member;
import net.member.db.MemberDAO;

public class MemberJoinProcessAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		
		Member m = new Member();
		m.setEMAIL(email);  m.setID(id);
		m.setNAME(name);	m.setPASSWORD(pass);
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		MemberDAO mdao = new MemberDAO();
		
		int result = mdao.insert(m);
		if(result==0) {
			System.out.println("ȸ�� ���� �����Դϴ�.");
			ActionForward forward = new ActionForward();
			forward.setRedirect(false);
			request.setAttribute("message", "ȸ�� ���� �����Դϴ�.");
			forward.setPath("error/error.jsp");
			return forward;
		}
		
		out.println("<script>");
		if (result == 1) { 
			out.println("alert('ȸ�� ������ �����մϴ�.');");
			out.println("location.href='login.net';");
		} else if (result == -1) {
			out.println("alert('���̵� �ߺ��Ǿ����ϴ�.');");
			out.println("history.back()");
		}
		out.println("</script>");
		out.close();
		return null;
	}

}
