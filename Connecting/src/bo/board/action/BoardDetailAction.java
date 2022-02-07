package bo.board.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bo.board.db.BoardBean;
import bo.board.db.BoardDAO;

public class BoardDetailAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int boardId = Integer.parseInt(request.getParameter("num").trim());
		BoardDAO dao = new BoardDAO();

		BoardBean boarddata = dao.getDetail(boardId);

		// boarddata == null; error �׽�Ʈ�� ���� �� ����
		// dao���� ���� ������ ���� ������ ��� null�� ��ȯ
		if (boarddata == null) {
			System.out.println("�Խñ� �󼼺��� ����...");
			ActionForward forward = new ActionForward();
			forward.setRedirect(true);
			// request.setAttribute("message", "�� �� �����͸� ���� ���߽��ϴ�.");
			forward.setPath("BoardList.bo");
			return forward;
		}
		System.out.println("�Խñ� �󼼺��� ����...");

		// boarddata ��ü�� request ��ü�� �����Ѵ�.
		request.setAttribute("boarddata", boarddata);

		ActionForward forward = new ActionForward();
		forward.setRedirect(false);

		// �� ���� ���� �������� �̵��ϱ� ���� ��θ� �����Ѵ�.
		forward.setPath("board/boardView.jsp");
		return forward;
	}

}
