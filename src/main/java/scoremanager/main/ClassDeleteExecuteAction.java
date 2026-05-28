package scoremanager.main;

import bean.ClassNum;
import bean.Teacher;
import dao.ClassNumDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class ClassDeleteExecuteAction extends Action{
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// ローカル変数の指定 1
		HttpSession session = req.getSession(); // セッション
		Teacher teacher = (Teacher)session.getAttribute("user");
		String class_name = ""; // 入力されたクラス名
		ClassNum classnum = new ClassNum();
		ClassNumDao classnumDao = new ClassNumDao(); // クラスDao
		
		// リクエストパラメーターの取得 2
		class_name = req.getParameter("class_name");
		// subjectに科目情報をセット
		classnum.setClass_num(class_name);
		classnum.setSchool(teacher.getSchool());
		// deleteメソッドで情報を削除
		classnumDao.delete(classnum);
		
		// JSPへフォワード 7
		req.getRequestDispatcher("class_delete_done.jsp").forward(req,res);

}
}