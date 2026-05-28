package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import bean.ClassNum;
import bean.Teacher;
import dao.ClassNumDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class ClassCreateExecuteAction extends Action{
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// ローカル変数の指定 1
		HttpSession session = req.getSession(); // セッション
		Teacher teacher = (Teacher)session.getAttribute("user");
		String school_cd = ""; // 入力された学校コード
		String class_name = ""; // 入力されたクラス名
		ClassNum classNum = new ClassNum();
		ClassNumDao classNumDao = new ClassNumDao(); // クラスDao
		Map<String, String> errors = new HashMap<>(); // エラーメッセージ
		
		school_cd = req.getParameter("cd");
		class_name = req.getParameter("name");
		
		if (classNumDao.get(class_name , teacher.getSchool()) != null) { // クラス名が重複している場合
			errors.put("1", "クラス名が重複しています");
			// リクエストにエラーメッセージをセット
			req.setAttribute("errors", errors);
		}else {
			// classNumに科目情報をセット
			classNum.setClass_num(class_name);
			classNum.setSchool(teacher.getSchool());
			// saveメソッドで情報を登録
			classNumDao.save(classNum);
		}
		// レスポンス値をセット 6
		// エラー時にstudent_create.jspに戻った時に入力したのと同じ内容を表示するため
		// リクエストに科目コードをセット
		req.setAttribute("cd", school_cd);
		// リクエストに科目名をセット
		req.setAttribute("name", class_name);
		
		// JSPへフォワード 7
		if (errors.isEmpty()) { // エラーメッセージがない場合
			// 登録完了画面にフォワード
			req.getRequestDispatcher("class_create_done.jsp").forward(req, res);
		} else { // エラーメッセージがある場合
			// 登録画面にフォワード
			req.getRequestDispatcher("ClassCreate.action").forward(req, res);
		}

}
}