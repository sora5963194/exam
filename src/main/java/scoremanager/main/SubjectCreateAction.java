package scoremanager.main;

import bean.Teacher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class SubjectCreateAction extends Action{
	
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		HttpSession session = req.getSession();//セッションの中のuserという名前のTeacher beanを取り出した
		Teacher teacher = (Teacher)session.getAttribute("user");
		
		// JSPへフォワード 7
		req.getRequestDispatcher("subject_create.jsp").forward(req,res);

}
}
