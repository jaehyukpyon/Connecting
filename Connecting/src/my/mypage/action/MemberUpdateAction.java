package my.mypage.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import my.mypage.db.Member;
import my.mypage.db.MemberDAO;



public class MemberUpdateAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//HttpSession session = request.getSession();


		//String id = (String) session.getAttribute("id");
		ActionForward forward = new ActionForward();
		MemberDAO mdao = new MemberDAO();
		Member m = new Member();
		
		m = mdao.getUser("hh");
		
		//�� ���� �ҷ����� ������ ���
		if(m == null) {
			System.out.println("ȸ������ ���� ����");
			forward = new ActionForward();
			forward.setRedirect(false);
			request.setAttribute("message", "ȸ������ ���� �����Դϴ�.");
			forward.setPath("error/error.jsp");
			return forward;
		}
		
		
		System.out.println("ȸ������ ���� ����");
		
		
		request.setAttribute("memberInfo", m);
		forward.setRedirect(false);

		forward.setPath("updateForm.jsp");
		
		return forward;
	}

}
