package scoremanager.main;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class SubjectDeleteExecuteAction extends Action{
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// ローカル変数の指定 1
			HttpSession session = req.getSession(); // セッション
			Teacher teacher = (Teacher)session.getAttribute("user");
			String subject_cd = ""; // 入力された科目コード
			String subject_name = ""; // 入力された科目名
			Subject subject = new Subject();
			SubjectDao subjectDao = new SubjectDao(); // 科目Dao
			
			// リクエストパラメーターの取得 2
			subject_cd = req.getParameter("subject_cd");
			subject_name = req.getParameter("subject_name");
			
			// subjectに科目情報をセット
			subject.setCd(subject_cd);
			subject.setName(subject_name);
			subject.setSchool(teacher.getSchool());
			// saveメソッドで情報を登録
			subjectDao.delete(subject);
			
			// JSPへフォワード 7
			req.getRequestDispatcher("subject_delete_done.jsp").forward(req,res);

}
}