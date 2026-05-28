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
import bean.TestListSubject;
import dao.ClassNumDao;
import dao.StudentlistDao;
import dao.SubjectDao;
import dao.TestListSubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestListSubjectExecuteAction extends Action{
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		HttpSession session = req.getSession();//セッションの中のuserという名前のTeacher beanを取り出した
		Teacher teacher = (Teacher)session.getAttribute("user");
		
		// ローカル変数の指定 1
		int entYear = 0; // 入力された入学年度
		String classNum = ""; // 入力されたクラス番号
		String subjectCd = ""; // 入力された科目コード
		
		ClassNumDao classNumDao = new ClassNumDao();
        SubjectDao subjectDao = new SubjectDao();
        StudentlistDao studentlistDao = new StudentlistDao();
        TestListSubjectDao testDao = new TestListSubjectDao();
        Map<String, String> errors = new HashMap<>(); // エラーメッセージ
		
		// リクエストパラメーターの取得 2
        String entYearStr = req.getParameter("f1");
		classNum = req.getParameter("f2");
		subjectCd = req.getParameter("f3");
		if (entYearStr != null && !entYearStr.equals("")) {
			entYear = Integer.parseInt(entYearStr);
		}
		
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
     	
     	// 成績一覧取得
     	List<TestListSubject> tlslist = new ArrayList<>();
     	// 入学年度、クラス、科目のいずれかが未入力の場合
     	if (entYear == 0 || classNum == null || classNum.equals("0") 
     			|| subjectCd == null|| subjectCd.equals("0")) {
			errors.put("1", "入学年度とクラスと科目を選択してください");
		} 
     	// エラーがない場合のみ検索処理
     	if (errors.isEmpty()) {
			// 科目取得
	     	Subject subject = null;
	     	for (Subject s : subject_list) {
				if (s.getCd().equals(subjectCd)) {
					subject = s;
					break;
				}
			}
	     	
			// 成績検索
			tlslist = testDao.filter(entYear,classNum,subject,teacher.getSchool());
			// 成績検索の結果が0件の場合
			if (tlslist.size() == 0) {
				errors.put("2","学生情報が存在しませんでした");
			}
				// 科目名
			    req.setAttribute("subject_name",subject.getName());
		}
     	
     	// JSPへセット
		req.setAttribute("f1",entYear);

		req.setAttribute("f2",classNum);

		req.setAttribute("f3",subjectCd);

		req.setAttribute("cNumlist",class_list);

		req.setAttribute("entYearSet",entYearList);

		req.setAttribute("list",subject_list);

		req.setAttribute("tlslist",tlslist);
		
		req.setAttribute("errors", errors);
		
		// JSPへフォワード
		// エラーがある場合
		if (errors.containsKey("1")) {
		    req.getRequestDispatcher("test_list.jsp").forward(req, res);
		} else {
		    // 正常時
		    req.getRequestDispatcher("test_list_subject.jsp").forward(req, res);
		}
		
}
}
