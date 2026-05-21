package scoremanager.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.Student;
import bean.Teacher;
import bean.TestListStudent;
import dao.StudentDao;
import dao.TestListStudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestListStudentExecuteAction extends Action{
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		HttpSession session = req.getSession();//セッションの中のuserという名前のTeacher beanを取り出した
		Teacher teacher = (Teacher)session.getAttribute("user");
		
		// ローカル変数の指定 1
		String studentNo = ""; // 入力された学籍番号
		
        Student student = new Student();
        StudentDao studentDao = new StudentDao();
		TestListStudentDao dao = new TestListStudentDao();
		Map<String, String> errors = new HashMap<>(); // エラーメッセージ
		
		//リクエストパラメーターの取得2
		studentNo = req.getParameter("f4");
		
		//学生情報を取得
		student = studentDao.get(studentNo);
		
		//ビジネスロジック4
		if (student == null) {

		    errors.put("1", "学生が存在しません");
		    req.setAttribute("errors", errors);

		} else {
			// 成績一覧取得
		    List<TestListStudent> testList = dao.filter(student);
		
			if (testList == null || testList.size() == 0) { // 成績情報がない場合
			    errors.put("2", "成績情報が存在しません");
			    req.setAttribute("errors", errors);
			    
			} else {
	
	            // JSPへ成績リストを渡す
				// 学生情報
	            req.setAttribute("student", student);
	            // 学生番号
	            req.setAttribute("f4", studentNo);
	
	            // 成績一覧
	            req.setAttribute("tlslist", testList);
	        }
			}

        // JSPへフォワード
        req.getRequestDispatcher("test_list_student.jsp").forward(req, res);
}
}
