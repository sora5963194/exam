package scoremanager.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.TestListStudent;
import dao.ClassNumDao;
import dao.StudentDao;
import dao.StudentlistDao;
import dao.SubjectDao;
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
        SubjectDao subjectDao = new SubjectDao();
		ClassNumDao classNumDao = new ClassNumDao();
        TestListStudentDao dao = new TestListStudentDao();
        StudentlistDao studentlistDao = new StudentlistDao();
		Map<String, String> errors = new HashMap<>(); // エラーメッセージ
		
		//リクエストパラメーターの取得2
		studentNo = req.getParameter("f4");
		
		// クラス一覧
        List<String> class_list = classNumDao.filter(teacher.getSchool());
        Collections.sort(class_list);
        
        // 科目一覧
        List<Subject> subject_list = subjectDao.filter(teacher.getSchool().getCd());
        subject_list.sort(Comparator.comparing(Subject::getCd));
        
        // 入学年度一覧
        List<Student> studentList = studentlistDao.filter();
        List<Integer> entYearList = new ArrayList<>();
        
        for (Student studentl : studentList) {

        	// 入学年度取得
        	int year = studentl.getEntYear();

        	// 重複していなければ追加
        	if (!entYearList.contains(year)) {
                entYearList.add(year);
            }
        }
        // 入学年度を昇順ソート
     	Collections.sort(entYearList);
     	
     	// JSPへセット
        req.setAttribute("cNumlist", class_list);
        req.setAttribute("entYearSet", entYearList);
        req.setAttribute("list", subject_list);
		
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
