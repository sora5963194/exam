package scoremanager.main;

import bean.ClassNum;
import bean.Teacher;
import dao.ClassNumDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class ClassUpdateExecuteAction extends Action{
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		HttpSession session = req.getSession(); // セッション
		Teacher teacher = (Teacher)session.getAttribute("user");
		String old_name = "";
		String class_name = "";// 入力されたクラス名
		ClassNum classNum = new ClassNum();
		ClassNumDao classNumDao = new ClassNumDao(); // クラスDao
		
		// リクエストパラメーターの取得 2
		old_name = req.getParameter("old_name");
		class_name = req.getParameter("name");
		
		// classNumに科目情報をセット
		classNum.setClass_num(class_name);
		classNum.setSchool(teacher.getSchool());
		// saveメソッドで情報を登録
		classNumDao.save(classNum,old_name);
		
		// JSPへフォワード 7
		req.getRequestDispatcher("class_update_done.jsp").forward(req,res);
	

}
}