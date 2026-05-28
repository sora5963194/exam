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

public class SubjectUpdateExecuteAction extends Action{
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// ローカル変数の指定 1
		HttpSession session = req.getSession(); // セッション
		Teacher teacher = (Teacher)session.getAttribute("user");
		String subject_cd = ""; // 入力された科目コード
		String subject_name = ""; // 入力された科目名
		String old_name = ""; // 変更前の科目名
		Subject subject = new Subject();
		SubjectDao subjectDao = new SubjectDao(); // 科目Dao
		Map<String, String> errors = new HashMap<>(); // エラーメッセージ
		
		// リクエストパラメーターの取得 2
		subject_cd = req.getParameter("cd");
		subject_name = req.getParameter("name");
		old_name = req.getParameter("old_name");
		
		//エラー対策
		if(subjectDao.get(subject_cd, teacher.getSchool()) == null) {
			errors.put("1","科目が存在していません");
			// 元の入力値を保持
			req.setAttribute("cd", subject_cd);
			req.setAttribute("name", old_name);
			// リクエストにエラーメッセージをセット
			req.setAttribute("errors", errors);
			//入力画面に戻す
			req.getRequestDispatcher("subject_update.jsp").forward(req, res);
			return;
		}else {
		// subjectに科目情報をセット
		subject.setCd(subject_cd);
		subject.setName(subject_name);
		subject.setSchool(teacher.getSchool());
		// saveメソッドで情報を登録
		subjectDao.save(subject);
		}
		
		// JSPへフォワード 7
		if (errors.isEmpty()) { // エラーメッセージがない場合
			// 登録完了画面にフォワード
			req.getRequestDispatcher("subject_update_done.jsp").forward(req, res);
		} else { // エラーメッセージがある場合
			// 登録画面にフォワード
			req.getRequestDispatcher("subject_update.jsp").forward(req, res);
		}

}
}
