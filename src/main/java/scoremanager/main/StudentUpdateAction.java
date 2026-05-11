package scoremanager.main;

import java.util.List;

import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;


public class StudentUpdateAction extends Action{

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// ローカル変数の指定 1
		HttpSession session = req.getSession();// セッション
		Teacher teacher = (Teacher)session.getAttribute("user");
		
		String student_no = "" ; // 学生番号
		String student_name = ""; // 氏名
		int entYearStr = 0; // 入学年度
		String class_num = ""; // クラス番号
		boolean isAttendStr = false; // 在学フラグ
		
		Student student = new Student();
		StudentDao studentDao = new StudentDao();
		ClassNumDao classNumDao = new ClassNumDao();

		// リクエストパラメーターの取得 2
		student_no = req.getParameter("no");

		// DBからデータ取得 3
		// 学生の詳細データを取得
		student = studentDao.get(student_no);
		// ログインユーザーの学校コードをもとにクラス番号の一覧を取得
		List<String> list = classNumDao.filter(teacher.getSchool());

		// ビジネスロジック 4
		// student_beanから入学年度の取得
		entYearStr = student.getEntYear();
		//student_beanから名前の取得
		student_name = student.getName();
		//sutdent_beanからクラス番号の取得
		class_num = student.getClassNum();
		//student_beanから在学フラグの取得
		isAttendStr = student.isAttend();

		// レスポンス値をセット 6
		// リクエストに入学年度をセット
		req.setAttribute("ent_year", entYearStr);
		// リクエストに学生番号をセット
		req.setAttribute("no", student_no);
		// リクエストに氏名をセット
		req.setAttribute("name", student_name);
		// リクエストにクラス番号をセット
		req.setAttribute("class_num", class_num);
		// リクエストにクラス番号の一覧をセット
		req.setAttribute("class_num_set", list);
		// リクエストに在学フラグをセット
		req.setAttribute("is_attend", isAttendStr);

		// JSPへフォワード 7
		req.getRequestDispatcher("student_update.jsp").forward(req,res);
	}

}
