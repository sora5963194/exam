package scoremanager.main;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;
 
public class TestRegistAction extends Action {
 
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
 
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        
        
        // 1. ローカル変数の初期化
        String entYearStr = req.getParameter("f1"); // 入学年度
        String classNum = req.getParameter("f2");   // クラス番号
        String subjectCd = req.getParameter("f3");  // 科目コード
        String countStr = req.getParameter("f4");   // 回数
 
        int entYear = 0;
        int count = 0;
 
        ClassNumDao classNumDao = new ClassNumDao();
        SubjectDao subjectDao = new SubjectDao();
        TestDao testDao = new TestDao(); 
 
        // 2. DBからプルダウン用のデータ取得
        List<String> class_list = classNumDao.filter(teacher.getSchool());
        Collections.sort(class_list);
 
        List<Subject> subject_list = subjectDao.filter(teacher.getSchool().getCd());
        subject_list.sort(Comparator.comparing(Subject::getCd));
 
        List<Integer> entYearList = testDao.getEntYearList(teacher.getSchool());
 
        List<Integer> countList = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            countList.add(i);
        }
 
        // 3. 数値への変換
        if (entYearStr != null && !entYearStr.equals("")) {
            entYear = Integer.parseInt(entYearStr);
        }
        if (countStr != null && !countStr.equals("")) {
            count = Integer.parseInt(countStr);
        }
 
        // 4. 検索ボタンが押された場合の処理
        if (entYear > 0 && classNum != null && !classNum.equals("") && subjectCd != null && !subjectCd.equals("") && count > 0) {
            
            Subject subject = new Subject();
            subject.setCd(subjectCd);
            subject.setSchool(teacher.getSchool());
            
            // データベースから成績・生徒一覧を取得
            List<Test> test_list = testDao.filter(entYear, classNum, subject, count, teacher.getSchool());
            
            // 🌟 修正ポイント1: JSPが手ぐすね引いて待っている "testlist"（アンダースコアなし）でセット！
            req.setAttribute("testlist", test_list);
            
            // 🌟 修正ポイント2: JSPの ${subject_name} のために、選択された科目名を探してセット！
            for (Subject s : subject_list) {
                if (s.getCd().equals(subjectCd)) {
                    req.setAttribute("subject_name", s.getName());
                    break;
                }
            }
            
            session.setAttribute("selectedClassNum", classNum);
        }
 
        // 5. レスポンス値をセット
        req.setAttribute("f1", entYearStr); 
        req.setAttribute("f2", classNum);
        req.setAttribute("f3", subjectCd);
        req.setAttribute("f4", countStr);
 
        req.setAttribute("entYearList", entYearList); 
        req.setAttribute("cNumList", class_list);     
        req.setAttribute("list", subject_list);       
        req.setAttribute("countList", countList);     
 
        // 6. JSPへフォワード
        req.getRequestDispatcher("test_regist.jsp").forward(req, res);
    }
}