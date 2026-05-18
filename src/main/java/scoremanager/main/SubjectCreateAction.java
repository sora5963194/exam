package scoremanager.main;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tool.Action;

public class SubjectCreateAction extends Action{
	
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		
		// JSPへフォワード 7
		req.getRequestDispatcher("subject_create.jsp").forward(req,res);

}
}
