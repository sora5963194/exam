package scoremanager.main;

import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestRegistExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        TestDao testDao = new TestDao();

        // JSPの隠しパラメータから科目コードと回数を回収
        String subjectCd = req.getParameter("subject");
        String countStr = req.getParameter("count");
        String classNum = (String) session.getAttribute("selectedClassNum");
        
        // JSPの <input type="hidden" name="regist"> から学生番号の配列を回収
        String[] studentNoArray = req.getParameterValues("regist");

        if (studentNoArray != null && subjectCd != null && countStr != null) {
            int count = Integer.parseInt(countStr);
            
            Subject subject = new Subject();
            subject.setCd(subjectCd);

            // 学生一人ひとりの点数をループで回収して保存
            for (String studentNo : studentNoArray) {
                // JSPの name="point_${test.student.no}" から点数を取得
                String pointStr = req.getParameter("point_" + studentNo);
                
                if (pointStr != null && !pointStr.equals("")) {
                    int point = Integer.parseInt(pointStr);

                    Test test = new Test();
                    
                    Student student = new Student();
                    student.setNo(studentNo);
                    test.setStudent(student);
                    
                    test.setSubject(subject);
                    test.setSchool(teacher.getSchool());
                    test.setNo(count);
                    test.setPoint(point);
                    // クラス番号は、今回は学生の主キー等から特定可能か、あるいはJSP側で送られていないため
                    // 必要であれば空文字、または学生データ等から補完する（ひとまず空文字か適当な値を設定）
                    test.setClassNum(classNum != null ? classNum : "");

                    // UPSERTを実行
                    testDao.save(test);
                }
            }
        }

        req.getRequestDispatcher("test_regist_done.jsp").forward(req, res);
    }
}