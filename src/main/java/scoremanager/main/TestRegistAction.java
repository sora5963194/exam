//package scoremanager.main;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//import bean.Subject;
//import bean.Teacher;
//import bean.Test;
//import dao.ClassNumDao;
//import dao.StudentlistDao;
//import dao.SubjectDao;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import tool.Action;
//
//public class TestRegistAction extends Action {
//    @Override
//    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//        HttpSession session = req.getSession();
//        Teacher teacher = (Teacher) session.getAttribute("user");
//        
//        // 1. ローカル変数の初期化
//        String entYearStr = ""; // 入力された入学年度
//        String classNum = "";   // 入力されたクラス番号
//        String subjectCd = "";  // 入力された科目コード（※科目名ではなくコードで扱うのが一般的です）
//        String countStr = "";   // 入力された回数
//        
//        int entYear = 0;
//        int count = 0;
//        
//        ClassNumDao classNumDao = new ClassNumDao();
//        SubjectDao subjectDao = new SubjectDao();
//        StudentlistDao studentlistDao = new StudentlistDao();
//        
//        // 2. リクエストパラメーターの取得（JSPのname属性に合わせて調整してください）
//        entYearStr = req.getParameter("f1"); // 入学年度
//        classNum = req.getParameter("f2");   // クラス番号
//        subjectCd = req.getParameter("f3");  // 科目コード
//        countStr = req.getParameter("f4");   // 回数
//        
//        // 3. DBからプルダウン用のデータ取得
//        // クラス一覧の取得とソート
//        List<String> class_list = classNumDao.filter(teacher.getSchool());
//        Collections.sort(class_list);
//        
//        // 科目一覧の取得とソート
//        List<Subject> subject_list = subjectDao.filter(teacher.getSchool().getCd());
//        subject_list.sort(Comparator.comparing(Subject::getCd));
//        
//        
//     // 入学年度一覧の取得（TESTテーブルに登録がある年度だけを取得）
//        dao.TestDao testDao = new dao.TestDao(); // TestDaoをインスタンス化
//        List<Integer> entYearList = testDao.getEntYearList(teacher.getSchool());
//        
////        // 入学年度一覧の取得（StudentListから重複を除いて作成）
////        List<Student> studentList = studentlistDao.filter();
////        List<Integer> entYearList = new ArrayList<>();
////        for (Student student : studentList) {
////            int year = student.getEntYear();
////            if (!entYearList.contains(year)) {
////                entYearList.add(year);
////            }
////        }
////        Collections.sort(entYearList);
//        
//        // ★【成績登録用の独自処理】「回数（1〜5）」のリストを作成
//        List<Integer> countList = new ArrayList<>();
//        for (int i = 1; i <= 2; i++) {
//            countList.add(i);
//        }
//        
//        // 4. ビジネスロジック（文字列から数値への変換）
//        if (entYearStr != null && !entYearStr.equals("")) {
//            entYear = Integer.parseInt(entYearStr);
//        }
//        if (countStr != null && !countStr.equals("")) {
//            count = Integer.parseInt(countStr);
//        }
//        
//
//     // 5. 検索ボタンが押された場合の処理（条件に合う成績一覧を取得してJSPに送る）
//         if (entYear > 0 && classNum != null && !classNum.equals("") && subjectCd != null && !subjectCd.equals("") && count > 0) {
//        // 選択された科目コードから、Subjectオブジェクトを取得するわ
//        	 Subject subject = subjectDao.get(subjectCd, teacher.getSchool());
////         Subject subject = subjectDao.get(subjectCd); 
//                    
//        // 条件（入学年度、クラス、科目、回数、学校）をすべて指定して、成績リストをゴソッと取得
//         List<Test> testList = testDao.filter(entYear, classNum, subject, count, teacher.getSchool());
//                    
//        // JSP（画面）側が「test_list」という名前で中身をループ表示できるように、リクエストにセットするわ
//         req.setAttribute("test_list", testList);
//         }
//        
////        // 5. 検索ボタンが押された場合の処理（ここに将来、学生一覧をDAOから取得する処理が入ります）
////        if (entYear > 0 && classNum != null && !classNum.equals("") && subjectCd != null && !subjectCd.equals("") && count > 0) {
////            // TODO: ここで条件に合う学生と成績のリストを取得して req.setAttribute("test_list", ...) する
////        }
//        
//        // 6. レスポンス値をセット（JSP側へデータを送る）
//        req.setAttribute("f1", entYearStr); // 選択された値を保持用
//        req.setAttribute("f2", classNum);
//        req.setAttribute("f3", subjectCd);
//        req.setAttribute("f4", countStr);
//        
//        // プルダウンの選択肢リストをセット
//        req.setAttribute("entYearList", entYearList); // 入学年度
//        req.setAttribute("cNumList", class_list);     // クラス番号
//        req.setAttribute("list", subject_list);       // 科目
//        req.setAttribute("countList", countList);     // 回数（★追加）
//        
//        // 7. JSPへフォワード
//        req.getRequestDispatcher("test_regist.jsp").forward(req, res);
//    }
//}




package scoremanager.main;
 
import java.util.Comparator;
import java.util.List;

import bean.Subject;
import bean.Teacher;
import bean.Test;
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
        String entYearStr = "";
        String classNum = "";
        String subjectCd = "";
        String countStr = "";
 
        int entYear = 0;
        int count = 0;
 
        // DAO
        TestDao testDao = new TestDao();
        SubjectDao subjectDao = new SubjectDao();
 
        // 2. パラメータ取得
        entYearStr = req.getParameter("f1");
        classNum = req.getParameter("f2");
        subjectCd = req.getParameter("f3");
        countStr = req.getParameter("f4");
 
        // 3. プルダウン用データ取得（TESTテーブル基準）
 
        // 入学年度
        List<Integer> entYearList =
                testDao.getEntYearList(teacher.getSchool());
 
        // クラス番号
        List<String> class_list =
        		testDao.getClassNumList(teacher.getSchool());
 
        // 科目一覧
        List<Subject> subject_list =
                testDao.getSubjectList(teacher.getSchool());
 
        // 科目コード順ソート
        subject_list.sort(Comparator.comparing(Subject::getCd));
 
        // 回数
        List<Integer> countList =
        		testDao.getNoList(teacher.getSchool());
 
        // 4. String → int変換
        if (entYearStr != null && !entYearStr.isEmpty()) {
            entYear = Integer.parseInt(entYearStr);
        }
 
        if (countStr != null && !countStr.isEmpty()) {
            count = Integer.parseInt(countStr);
        }
 
        // 5. 検索処理
        if (entYear > 0
                && classNum != null && !classNum.isEmpty()
                && subjectCd != null && !subjectCd.isEmpty()
                && count > 0) {
 
            // 科目取得
            Subject subject =
                    subjectDao.get(subjectCd, teacher.getSchool());
 
            // 成績一覧取得
            List<Test> testList =
                    testDao.filter(
                            entYear,
                            classNum,
                            subject,
                            count,
                            teacher.getSchool());
 
            // JSPへ渡す
            req.setAttribute("test_list", testList);
        }
 
        // 6. 入力値保持
        req.setAttribute("f1", entYearStr);
        req.setAttribute("f2", classNum);
        req.setAttribute("f3", subjectCd);
        req.setAttribute("f4", countStr);
 
        // 7. プルダウンリスト
        req.setAttribute("entYearList", entYearList);
        req.setAttribute("cNumList", class_list);
        req.setAttribute("list", subject_list);
        req.setAttribute("countList", countList);
 
        // 8. JSPへフォワード
        req.getRequestDispatcher("test_regist.jsp")
           .forward(req, res);
    }
}
 