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
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestListSubjectExecuteAction extends Action {

    @Override
    public void execute(
            HttpServletRequest req,
            HttpServletResponse res)
            throws Exception {

        HttpSession session = req.getSession();

        Teacher teacher =
                (Teacher)session.getAttribute("user");

        Map<String, String> errors =
                new HashMap<>();

        String f1 = req.getParameter("f1");
        String f2 = req.getParameter("f2");
        String f3 = req.getParameter("f3");

        // null対策
        if (f1 == null) f1 = "0";
        if (f2 == null) f2 = "0";
        if (f3 == null) f3 = "0";

        int entYear = 0;

        // 入力チェック
        if (f1.equals("0")
                || f2.equals("0")
                || f3.equals("0")) {

            errors.put(
                    "1",
                    "入学年度・クラス・科目を選択してください"
            );
        }

        // エラーなら元画面へ戻す
        if (!errors.isEmpty()) {

            req.setAttribute(
                    "errors",
                    errors
            );

            // プルダウン再生成
            ClassNumDao classNumDao =
                    new ClassNumDao();

            List<String> class_list =
                    classNumDao.filter(
                            teacher.getSchool());

            Collections.sort(class_list);

            SubjectDao subjectDao =
                    new SubjectDao();

            List<Subject> subject_list =
                    subjectDao.filter(
                            teacher.getSchool().getCd());

            subject_list.sort(
                    Comparator.comparing(
                            Subject::getCd));

            StudentlistDao studentlistDao =
                    new StudentlistDao();

            List<Student> studentList =
                    studentlistDao.filter();

            List<Integer> entYearList =
                    new ArrayList<>();

            for (Student student : studentList) {

                int year =
                        student.getEntYear();

                if (!entYearList.contains(year)) {

                    entYearList.add(year);
                }
            }

            Collections.sort(entYearList);

            req.setAttribute(
                    "cNumlist",
                    class_list);

            req.setAttribute(
                    "entYearSet",
                    entYearList);

            req.setAttribute(
                    "list",
                    subject_list);

            req.getRequestDispatcher(
                    "test_list.jsp")
            .forward(
                    req,
                    res);

            return;
        }

        // 入学年度変換
        entYear = Integer.parseInt(f1);

        // プルダウン用
        ClassNumDao classNumDao =
                new ClassNumDao();

        List<String> class_list =
                classNumDao.filter(
                        teacher.getSchool());

        Collections.sort(class_list);

        SubjectDao subjectDao =
                new SubjectDao();

        List<Subject> subject_list =
                subjectDao.filter(
                        teacher.getSchool().getCd());

        subject_list.sort(
                Comparator.comparing(
                        Subject::getCd));

        StudentlistDao studentlistDao =
                new StudentlistDao();

        List<Student> studentList =
                studentlistDao.filter();

        List<Integer> entYearList =
                new ArrayList<>();

        for (Student student : studentList) {

            int year =
                    student.getEntYear();

            if (!entYearList.contains(year)) {

                entYearList.add(year);
            }
        }

        Collections.sort(entYearList);

        // 値保持
        req.setAttribute("f1", entYear);
        req.setAttribute("f2", f2);
        req.setAttribute("f3", f3);

        req.setAttribute(
                "cNumlist",
                class_list);

        req.setAttribute(
                "entYearSet",
                entYearList);

        req.setAttribute(
                "list",
                subject_list);

        // 検索
        TestDao dao =
                new TestDao();

        List<TestListSubject> tlslist =
                dao.filterSubject(
                        entYear,
                        f2,
                        f3);

        req.setAttribute(
                "tlslist",
                tlslist);

        // 科目名
        String subjectName = "";

        for (Subject s : subject_list) {

            if (s.getCd().equals(f3)) {

                subjectName =
                        s.getName();

                break;
            }
        }

        req.setAttribute(
                "subject_name",
                subjectName);

        // 成績一覧（科目）へ
        req.getRequestDispatcher(
                "test_list_subject.jsp")
        .forward(
                req,
                res);
    }
}