package scoremanager.main;
import java.util.List;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class SubjectListAction extends Action{
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		HttpSession session = req.getSession();//セッションの中のuserという名前のTeacher beanを取り出した
		Teacher teacher = (Teacher)session.getAttribute("user");
		
		SubjectDao subjectDao = new SubjectDao(); // 科目Dao
		
		//DBからデータ取得3
		//ログインユーザーの学校コードをもとに科目の一覧を取得
		List<Subject> list =subjectDao.filter(teacher.getSchool().getCd());
		
		//リクエストにデータをセット
		req.setAttribute("subject", list);
		
		//JSPにフォワード
		req.getRequestDispatcher("subject_list.jsp").forward(req,res);

}
}
