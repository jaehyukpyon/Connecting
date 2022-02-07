package my.mypage.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import my.mypage.db.MemberDAO;


public class MemberImageUploadAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String realFolder = "";
		
		//WebContent�Ʒ��� �� ���� ����
		String saveFolder = "memberupload";
		
		int fileSize = 5 * 1024 * 1024; // ���ε��� ������ �ִ� ������ �Դϴ�. 5MB
		
		//���� ���� ��θ� �����մϴ�.
		ServletContext sc = request.getServletContext();
		realFolder = sc.getRealPath(saveFolder);
		System.out.println(realFolder);
		try {
			MultipartRequest multi = new MultipartRequest(request, realFolder, fileSize, "utf-8",
					new DefaultFileRenamePolicy());
			
			String id = multi.getParameter("id");
			String profile = multi.getFilesystemName("profile");
			
			System.out.println("����=" + profile);
			
			
			MemberDAO mdao = new MemberDAO();
			int result = mdao.updateImg(profile,id);
			
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			//������ �� ���
			if (result == 1) {
				out.println("alert('�����Ǿ����ϴ�.');");
				out.println("location.href='memberInfo.my';");
			} else {
				out.println("alert('ȸ�� ���� ������ �����߽��ϴ�.');");
				out.println("history.back()");
			}
			out.println("</script>");
			out.close();
			return null;
		}catch(IOException ex) {
			ActionForward forward = new ActionForward();
			forward.setPath("error/error.jsp");
			request.setAttribute("message", "������ ���� ���ε� �����Դϴ�.");
			forward.setRedirect(false);
			return forward;
		}
	}

}
