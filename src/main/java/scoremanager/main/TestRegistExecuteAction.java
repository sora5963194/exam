package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

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

        // JSPのパラメータから科目コードと回数を回収
        String subjectCd = req.getParameter("subject");
        String countStr = req.getParameter("count");
        String classNum = (String) session.getAttribute("selectedClassNum");
        
        // 学生番号の配列を回収
        String[] studentNoArray = req.getParameterValues("regist");

        if (studentNoArray != null && subjectCd != null && countStr != null) {
            int count = Integer.parseInt(countStr);
            
            Subject subject = new Subject();
            subject.setCd(subjectCd);

            // ─── 1. 点数のバリデーションチェック ───
            Map<Integer, String> pointErrors = new HashMap<>();
            Map<String, String> inputPoints = new HashMap<>();
            int index = 1; // JSPの行番号（st.count）と連動
            boolean hasError = false;

            for (String studentNo : studentNoArray) {
                String pointStr = req.getParameter("point_" + studentNo);
                inputPoints.put(studentNo, pointStr); // 入力された値を記憶
                
                if (pointStr != null && !pointStr.equals("")) {
                    
                    // 正規表現で、半角数字（0-9）のみで構成されているかチェック
                    // これにより全角数字（０〜９）や文字が混入していた場合は、一発でエラーにするわ
                    if (!pointStr.matches("^[0-9]+$")) {
                        pointErrors.put(index, "0〜100の範囲で入力してください");
                        hasError = true;
                    } else {
                        // 半角数字であることが確定している場合のみ、数値変換と範囲チェックを行う
                        try {
                            int point = Integer.parseInt(pointStr);
                            // 0〜100の範囲外チェック
                            if (point < 0 || point > 100) {
                                pointErrors.put(index, "0〜100の範囲で入力してください");
                                hasError = true;
                            }
                        } catch (NumberFormatException e) {
                            pointErrors.put(index, "0〜100の範囲で入力してください");
                            hasError = true;
                        }
                    }
                }
                index++;
            }

            // ─── 2. 点数に1つでも間違った入力があった場合の処理 ───
            if (hasError) {
                // エラー内容と入力値をセッションに保管して、元の検索画面へリダイレクト
                session.setAttribute("point_errors", pointErrors);
                session.setAttribute("input_points", inputPoints);

                String entYearStr = (String) session.getAttribute("selectedEntYear");
                String cNum = (String) session.getAttribute("selectedClassNum");
                String subCd = (String) session.getAttribute("selectedSubjectCd");
                String cntStr = (String) session.getAttribute("selectedCount");

                res.sendRedirect("TestRegist.action?f1=" + entYearStr + "&f2=" + cNum + "&f3=" + subCd + "&f4=" + cntStr);
                return;
            }

            // ─── 3. すべて合格なら通常通り安全に保存処理 ───
            for (String studentNo : studentNoArray) {
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
                    test.setClassNum(classNum != null ? classNum : "");

                    // UPSERTを実行
                    testDao.save(test);
                }
            }
        }

        // 正常終了時は完了画面へ
        res.sendRedirect("test_regist_done.jsp");
    }
}