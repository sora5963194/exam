package scoremanager.main;

import bean.Teacher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class ClassDeleteAction extends Action{
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// ローカル変数の指定 1
		HttpSession session = req.getSession();// セッション
		Teacher teacher = (Teacher)session.getAttribute("user");
		String class_name = ""; // 入力されたクラス名
		
		// リクエストパラメーターの取得 2
		class_name = req.getParameter("cd");
		
		// 学校コード取得
		String schoolCd = teacher.getSchool().getCd();
		
		// リクエストにデータをセット
		req.setAttribute("school_cd", schoolCd);
		req.setAttribute("class_name", class_name);
		
		// JSPへフォワード 7
		req.getRequestDispatcher("class_delete.jsp").forward(req,res);

}
}