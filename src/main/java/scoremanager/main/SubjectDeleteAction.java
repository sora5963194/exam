package scoremanager.main;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class SubjectDeleteAction extends Action{
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// ローカル変数の指定 1
		HttpSession session = req.getSession();// セッション
		Teacher teacher = (Teacher)session.getAttribute("user");
		String subject_cd = "" ; // 科目コード
		String subject_name = ""; // 科目名
		Subject subject = new Subject();
		SubjectDao subjectDao = new SubjectDao();
		
		// リクエストパラメーターの取得 2
		subject_cd = req.getParameter("cd");
		
		// DBからデータ取得 3
		// 科目の詳細データを取得
		subject = subjectDao.get(subject_cd , teacher.getSchool());
		
		// ビジネスロジック 4
		// sbuject_beanから科目名の取得
		subject_name = subject.getName();
		
		// レスポンス値をセット 6
		// リクエストに科目コードをセット
		req.setAttribute("subject_cd", subject_cd);
		// リクエストに科目名をセット
		req.setAttribute("subject_name", subject_name);
		
		
		// JSPへフォワード 7
		req.getRequestDispatcher("subject_delete.jsp").forward(req,res);

}
}