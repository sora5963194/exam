package scoremanager.main;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 
        // 4. 検索ボタンが押された（または登録エラーからリダイレクトで戻った）場合の処理
        boolean isSearchPressed = (entYearStr != null || classNum != null || subjectCd != null || countStr != null);
        
        if (isSearchPressed) {
            // 👉 【追加要件】どれか一つでも未入力（"0"、空文字、null）だった場合のチェック
            if (entYear <= 0 || classNum == null || classNum.equals("0") || classNum.equals("") || 
                subjectCd == null || subjectCd.equals("0") || subjectCd.equals("") || count <= 0) {
                
                // JSPの ${errors.get("a")} に表示させるため、キー "a" でエラーメッセージを格納
                Map<String, String> searchErrors = new HashMap<>();
                searchErrors.put("a", "入学年度とクラスと科目と回数を選択してください");
                req.setAttribute("errors", searchErrors);
                
            } else {
                // 4項目すべて正常に入力されている場合の検索・表示処理
                Subject subject = new Subject();
                subject.setCd(subjectCd);
                subject.setSchool(teacher.getSchool());
                
                // データベースから成績・生徒一覧を取得
                List<Test> test_list = testDao.filter(entYear, classNum, subject, count, teacher.getSchool());
                
                // 点数バリデーションエラーによるリダイレクト戻りがあるかセッションを確認
                @SuppressWarnings("unchecked")
                Map<Integer, String> pointErrors = (Map<Integer, String>) session.getAttribute("point_errors");
                @SuppressWarnings("unchecked")
                Map<String, String> inputPoints = (Map<String, String>) session.getAttribute("input_points");
                
                if (pointErrors != null) {
                    // 点数エラーメッセージ（行番号キー）をリクエストに復元
                    req.setAttribute("errors", pointErrors);
                    session.removeAttribute("point_errors"); // 使い終わったら削除
                }
                
                if (inputPoints != null) {
                    // ユーザーが入力していた不正な点数や文字列を、画面の入力欄にそのまま保持・復元させる
                    for (Test test : test_list) {
                        String studentNo = test.getStudent().getNo();
                        if (inputPoints.containsKey(studentNo)) {
                            String ptStr = inputPoints.get(studentNo);
                            if (ptStr != null && !ptStr.equals("")) {
                                try {
                                    int pt = Integer.parseInt(ptStr);
                                    test.setPoint(pt);
                                    test.setNo(count); // JSPの <c:if test="${test.no != 0}"> の条件を通過させて値を表示
                                } catch (NumberFormatException e) {
                                    // 文字が入力されていた場合はJSP側で空欄として扱わせる
                                    test.setNo(0);
                                }
                            } else {
                                test.setNo(0);
                            }
                        }
                    }
                    session.removeAttribute("input_points"); // 使い終わったら削除
                }
                
                req.setAttribute("testlist", test_list);
                
                for (Subject s : subject_list) {
                    if (s.getCd().equals(subjectCd)) {
                        req.setAttribute("subject_name", s.getName());
                        break;
                    }
                }
                
                // 登録エラー時に元の画面を復元するための検索条件をセッションに一時退避
                session.setAttribute("selectedEntYear", entYearStr);
                session.setAttribute("selectedClassNum", classNum);
                session.setAttribute("selectedSubjectCd", subjectCd);
                session.setAttribute("selectedCount", countStr);
            }
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
 
        // 6. JSPへフォワード（ここは元々エラーが起きないルートよ）
        req.getRequestDispatcher("test_regist.jsp").forward(req, res);
    }
}