package scoremanager.main;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestRegistExecuteAction extends Action {

    @Override
    public void execute(
            HttpServletRequest req,
            HttpServletResponse res)
            throws Exception {

        HttpSession session =
                req.getSession();

        Teacher teacher =
                (Teacher)session.getAttribute("user");

        School school =
                teacher.getSchool();

        String subjectCd =
                req.getParameter("subject");

        int count =
                Integer.parseInt(
                        req.getParameter("count"));

        SubjectDao subjectDao =
                new SubjectDao();

        Subject subject =
                subjectDao.get(
                        subjectCd,
                        school);

        TestDao testDao =
                new TestDao();

        String[] students =
                req.getParameterValues(
                        "regist");

        StudentDao studentDao =
                new StudentDao();

        for(String no : students){

            Student student =
                    studentDao.get(no);

            String pointStr =
                    req.getParameter(
                    "point_" + no);

            if(pointStr == null ||
               pointStr.equals("")){

                continue;
            }

            int point =
                    Integer.parseInt(
                            pointStr);

            Test test =
                    testDao.get(
                            student,
                            subject,
                            school,
                            count);

            if(test == null){

                test = new Test();

                test.setStudent(
                        student);

                test.setSubject(
                        subject);

                test.setSchool(
                        school);

                test.setNo(
                        count);

                test.setClassNum(
                        student.getClassNum());
            }

            test.setPoint(
                    point);

            testDao.save(
                    test);
        }

        req.getRequestDispatcher(
                "test_regist_done.jsp")
        .forward(
                req,
                res);
    }
}