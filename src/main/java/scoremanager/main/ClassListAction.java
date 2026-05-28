package scoremanager.main;

import java.util.List;

import bean.Teacher;
import dao.ClassNumDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class ClassListAction extends Action{
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res)throws Exception {
		HttpSession session = req.getSession();//セッションの中のuserという名前のTeacher beanを取り出した
		Teacher teacher = (Teacher)session.getAttribute("user");
		
		ClassNumDao classNumDao = new ClassNumDao(); // 科目Dao
		
		//DBからデータ取得3
		//ログインユーザーの学校コードをもとにクラスの一覧を取得
		List<String> list =classNumDao.filter(teacher.getSchool());
		// 学校コード取得
		String schoolCd = teacher.getSchool().getCd();
		
		//リクエストにデータをセット
		req.setAttribute("classnum", list);
		req.setAttribute("cd", schoolCd);
		
		
		//JSPにフォワード
		req.getRequestDispatcher("class_list.jsp").forward(req,res);
	

}
}