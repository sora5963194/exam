package scoremanager.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bean.Student;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.StudentDao;
import dao.StudentlistDao;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestRegistAction extends Action {

    @Override
    public void execute(
            HttpServletRequest req,
            HttpServletResponse res)
            throws Exception {

        HttpSession session=req.getSession();

        Teacher teacher=
                (Teacher)session.getAttribute("user");

        String f1=req.getParameter("f1");
        String f2=req.getParameter("f2");
        String f3=req.getParameter("f3");
        String f4=req.getParameter("f4");

        ClassNumDao classDao=
                new ClassNumDao();

        SubjectDao subjectDao=
                new SubjectDao();

        StudentlistDao studentlistDao=
                new StudentlistDao();

        StudentDao studentDao=
                new StudentDao();

        // クラス一覧
        List<String> cNumList=
                classDao.filter(
                        teacher.getSchool());

        Collections.sort(cNumList);

        req.setAttribute(
                "cNumList",
                cNumList);

        // 科目一覧
        req.setAttribute(
                "list",
                subjectDao.filter(
                        teacher.getSchool().getCd()
                )
        );

        // 入学年度一覧
        List<Student> allStudents=
                studentlistDao.filter();

        List<Integer> entYearList=
                new ArrayList<>();

        for(Student s:allStudents){

            if(!entYearList.contains(
                    s.getEntYear())){

                entYearList.add(
                        s.getEntYear());
            }
        }

        Collections.sort(entYearList);

        req.setAttribute(
                "entYearList",
                entYearList);

        // 回数
        List<Integer> countList=
                new ArrayList<>();

        countList.add(1);
        countList.add(2);

        req.setAttribute(
                "countList",
                countList);

        req.setAttribute("f1",f1);
        req.setAttribute("f2",f2);
        req.setAttribute("f3",f3);
        req.setAttribute("f4",f4);

        // 検索
        if(f1!=null &&
           f2!=null &&
           f3!=null &&
           f4!=null &&
           !f1.equals("0") &&
           !f2.equals("0") &&
           !f3.equals("0") &&
           !f4.equals("0")){

            List<Student> students=
                    studentDao.filter(
                            teacher.getSchool(),
                            Integer.parseInt(f1),
                            f2,
                            true
                    );

            List<Test> testlist=
                    new ArrayList<>();

            for(Student s:students){

                Test test=
                        new Test();

                test.setStudent(s);
                test.setClassNum(
                        s.getClassNum());

                test.setNo(
                        Integer.parseInt(f4)
                );

                testlist.add(test);
            }

            req.setAttribute(
                    "testlist",
                    testlist);
            
        }
        req.getRequestDispatcher(
                "test_regist.jsp")
        .forward(
                req,
                res);
    }
}

