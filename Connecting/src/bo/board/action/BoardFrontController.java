package bo.board.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BoardFrontController
 */
@WebServlet("*.bo")
public class BoardFrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardFrontController() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		doProcess(request, response);
	}
	
	private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * ��û�� ��ü url �߿��� ��Ʈ ��ȣ ���� ���� ������ ���ڿ����� ��ȯ�մϴ�.
		 * 		��) contextPath�� "/JspProject"�� ���,
		 * 		http://localhost:8088/JspProject/login.net���� ��û�ϸ�, RequestURI��
		 * 			"/JspProject/login.net"�� ��ȯ�˴ϴ�.
		 */
		String RequestURI = request.getRequestURI();
		System.out.println("RequestURI: " + RequestURI);
		
		// getContextPath() : ���ؽ�Ʈ ��ΰ� ��ȯ�ȴ�.
		// contextPath�� "/JspProject"�� ��ȯ�ȴ�.
		String contextPath = request.getContextPath();
		System.out.println("contextPath: " + contextPath);
		
		/*
		 * RequestURI���� ���ؽ�Ʈ ��� ���� ���� �ε��� ��ġ�� ���ں���,
		 * ������ ��ġ ���ڱ��� �����Ѵ�.
		 * command�� "/login.net"�� ��ȯ�ȴ�.
		 */
		String command = RequestURI.substring(contextPath.length());
		System.out.println("command: " + command);
		
		
		// �ʱ�ȭ
		Action action = null;
		
		switch (command) {			
			case "/BoardList.bo": // �α����� ���������� ���� �� �� �ּҷ� "redirect"�ż� �̵�. �� �ּҰ� �ٲ�. �׸��� boardList.jsp�� forward
				action = new BoardListAction();
				break;	
			case "/NoticeList.bo":
				action = new NoticeListAction();
				break;
			case "/BoardDetailAction.bo":
				action = new BoardDetailAction();
				break;
		} // switch ends
		
		ActionForward forward = action.execute(request, response);
		
		if (forward != null) {
			if (forward.isRedirect()) {
				response.sendRedirect(forward.getPath());
			} else {
				RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
				dispatcher.forward(request, response);
			}
		}
	} // doProcess ends

}
