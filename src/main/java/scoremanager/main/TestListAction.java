package scoremanager.main;

import java.util.ArrayList;
import java.util.List;

import bean.Student;
import bean.Subject;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentlistDao;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestListAction extends Action{
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		HttpSession session = req.getSession();//セッションの中のuserという名前のTeacher beanを取り出した
		Teacher teacher = (Teacher)session.getAttribute("user");
		// ローカル変数の指定 1
		String entYearStr = ""; // 入力された入学年度
		String classNum = ""; // 入力されたクラス番号
		String subjectName = ""; // 入力された科目名
		String studentNo = ""; //入力された学籍番号
		
		int entYear = 0; // 入学年度
		ClassNumDao classNumDao = new ClassNumDao(); // クラス番号Daoを初期化
		SubjectDao subjectDao = new SubjectDao(); // 科目Dao
		StudentlistDao studentlistDao = new StudentlistDao();//学生一覧Dao
		
		//リクエストパラメーターの取得2
		entYearStr = req.getParameter("f1");
		classNum = req.getParameter("f2");
		subjectName = req.getParameter("f3");
		studentNo = req.getParameter("f4");
		
		//DBからデータ取得3
		//ログインユーザーの学校コードをもとにクラスの一覧を取得
		List<String> class_list = classNumDao.filter(teacher.getSchool());
		//ログインユーザーの学校コードをもとに科目の一覧を取得
		List<Subject> subject_list =subjectDao.filter(teacher.getSchool().getCd());
		
		// 学生一覧取得
		List<Student> studentList = studentlistDao.filter();

		// 入学年度一覧
		List<Integer> entYearList = new ArrayList<>();

		for (Student student : studentList) {

		    // 入学年度取得
		    int year = student.getEntYear();

		    // 重複していなければ追加
		    if (!entYearList.contains(year)) {
		        entYearList.add(year);
		    }
		}
		
		//ビジネスロジック4
		if(entYearStr != null) {
			//数値に変換
			entYear = Integer.parseInt(entYearStr);
		}
		
		//レスポンス値をセット６
		//リクエストに入学年度をセット
		req.setAttribute("f1", entYear);
		
		//リクエストにクラス番号をセット
		req.setAttribute("f2", classNum);
		
		//リクエストに科目名をセット
		req.setAttribute("f3", subjectName);
		
		//リクエストに学籍番号をセット
		req.setAttribute("f4", studentNo);
		
		//リクエストにデータをセット
		req.setAttribute("cNumlist", class_list);
		req.setAttribute("entYearSet", entYearList);
		req.setAttribute("list", subject_list);
		
		//JSPへフォワード７
		req.getRequestDispatcher("test_list.jsp").forward(req,res);

}
}