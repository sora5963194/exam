package scoremanager.main;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tool.Action;

public class ClassUpdateAction extends Action{
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// ローカル変数の指定 1
		String class_name = ""; // 入力されたクラス名
		
		// リクエストパラメーターの取得 2
		class_name = req.getParameter("cd");
		
		// レスポンス値をセット 6
		// リクエストにクラス名をセット
		req.setAttribute("name", class_name);
		
		// JSPへフォワード 7
		req.getRequestDispatcher("class_update.jsp").forward(req,res);
		
}
}