package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class SubjectCreateExecuteAction extends Action{
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// ローカル変数の指定 1
		HttpSession session = req.getSession(); // セッション
		Teacher teacher = (Teacher)session.getAttribute("user");
		String subject_cd = ""; // 入力された科目コード
		String subject_name = ""; // 入力された科目名
		Subject subject = new Subject();
		SubjectDao subjectDao = new SubjectDao(); // 科目Dao
		Map<String, String> errors = new HashMap<>(); // エラーメッセージ
		
		subject_cd = req.getParameter("cd");
		subject_name = req.getParameter("name");
		
		if (subjectDao.get(subject_cd , teacher.getSchool()) != null) { // 学生番号が重複している場合
			errors.put("1", "科目コードが重複しています");
			// リクエストにエラーメッセージをセット
			req.setAttribute("errors", errors);
		} else {
			// subjectに科目情報をセット
			subject.setCd(subject_cd);
			subject.setName(subject_name);
			subject.setSchool(teacher.getSchool());
			// saveメソッドで情報を登録
			subjectDao.save(subject);
		}
		
		// レスポンス値をセット 6
		// エラー時にstudent_create.jspに戻った時に入力したのと同じ内容を表示するため
		// リクエストに科目コードをセット
		req.setAttribute("cd", subject_cd);
		// リクエストに科目名をセット
		req.setAttribute("name", subject_name);
		
		// JSPへフォワード 7
		if (errors.isEmpty()) { // エラーメッセージがない場合
			// 登録完了画面にフォワード
			req.getRequestDispatcher("subject_create_done.jsp").forward(req, res);
		} else { // エラーメッセージがある場合
			// 登録画面にフォワード
			req.getRequestDispatcher("SubjectCreate.action").forward(req, res);
		}
	

}
}
