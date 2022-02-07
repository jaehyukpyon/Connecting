package bo.board.action;


/*
 * ActionForward Ŭ������ Action �������̽����� ����� �����ϰ� ��� ���� ������ �̵��� �� ���Ǵ� Ŭ����
 * �� Ŭ������ Redirect ���� ���� �������� �������� ��ġ�� ���� �ֽ��ϴ�.
 * �� ������ FrontController���� ActionForward Ŭ���� Ÿ������ ��ȯ���� ��������,
 * �� ���� Ȯ���Ͽ� �ش��ϴ� ��û �������� �̵��մϴ�. 
 */
public class ActionForward {
	
	private boolean redirect = false;
	private String path = null;
	
	// property redirect�� is �޼���
	public boolean isRedirect() {
		return this.redirect;
	}
	
	// property redirect�� set �޼���
	public void setRedirect(boolean b) {
		this.redirect = b;
	}
	
	// property path�� get �޼���
	public String getPath() {
		return this.path;
	}
	
	// property path�� set �޼���
	public void setPath(String path) {
		this.path = path;
	}
	
}
