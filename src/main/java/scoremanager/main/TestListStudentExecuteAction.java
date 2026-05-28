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

public class TestListStudentExecuteAction extends Action {

    @Override
    public void execute(
            HttpServletRequest req,
            HttpServletResponse res)
            throws Exception {

        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        String studentNo = req.getParameter("f4");

        Map<String, String> errors = new HashMap<>();

        // ★ バグ修正: 元コードはこのデータをセットしていなかった
        // test_list_student.jsp にも科目情報フォームがあるため、
        // 画面を表示するには必ずプルダウン用データが必要
        ClassNumDao classNumDao = new ClassNumDao();
        List<String> class_list =
                classNumDao.filter(teacher.getSchool());
        Collections.sort(class_list);

        SubjectDao subjectDao = new SubjectDao();
        List<Subject> subject_list =
                subjectDao.filter(teacher.getSchool().getCd());
        subject_list.sort(Comparator.comparing(Subject::getCd));

        StudentlistDao studentlistDao = new StudentlistDao();
        List<Student> allStudentList = studentlistDao.filter();

        List<Integer> entYearList = new ArrayList<>();
        for (Student s : allStudentList) {
            int year = s.getEntYear();
            if (!entYearList.contains(year)) {
                entYearList.add(year);
            }
        }
        Collections.sort(entYearList);

        req.setAttribute("cNumlist",   class_list);
        req.setAttribute("entYearSet", entYearList);
        req.setAttribute("list",       subject_list);

        // 検索条件の保持
        req.setAttribute("f1", 0);
        req.setAttribute("f2", "0");
        req.setAttribute("f3", "0");
        req.setAttribute("f4", studentNo != null ? studentNo : "");

        // 学生情報取得
        StudentDao studentDao = new StudentDao();
        Student student = studentDao.get(studentNo);

        if (student == null) {
            // 学生が存在しない場合
            // JSPの ${student.name} がnull参照しないようにダミーをセット
            errors.put("1", "学生が存在しません");
        } else {
            // 学生が見つかった場合は必ずセット（成績なしの場合でも氏名表示に使う）
            req.setAttribute("student", student);
            req.setAttribute("f4", studentNo);

            // 成績一覧取得
            TestListStudentDao dao = new TestListStudentDao();
            List<TestListStudent> testList = dao.filter(student);

            if (testList == null || testList.size() == 0) {
                // 成績情報がない場合
                errors.put("2", "成績情報が存在しません");
            } else {
                req.setAttribute("tlslist", testList);
            }
        }

        req.setAttribute("errors", errors);

        req.getRequestDispatcher("test_list_student.jsp").forward(req, res);
    }
}