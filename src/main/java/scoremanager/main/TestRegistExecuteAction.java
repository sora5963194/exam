package scoremanager.main;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Teacher; // セッションを使うために追加
import bean.Test;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // セッションを使うために追加
import tool.Action;

public class TestRegistExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

        // ====== ログインユーザー（セッション）情報の取得 ======
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        
        String school_cd = "";
        if (teacher != null) {
            school_cd = teacher.getSchool().getCd(); 
        }

        // ====== 1. ローカル変数の指定 ======
        int no = 0; // 回数
        String class_num = ""; // 選択されたクラス番号
        String subject_cd = "";
        
        String[] student_no = null; //画面から回収する複数人分の学生番号の配列
        String[] point = null; //画面から回収する複数人分の点数の配列
        
        TestDao testDao = new TestDao();

        // ====== 2. リクエストパラメーターの取得 ======
        no = Integer.parseInt(req.getParameter("no"));
        class_num = req.getParameter("class_num");
        subject_cd = req.getParameter("subject_cd");
        point = req.getParameterValues("point");
        student_no = req.getParameterValues("student_no");
        

        // ====== 4. ビジネスロジック（データの組み立てと保存） ======
        if ("regist".equals(req.getParameter("mode"))) {
            
            for (int i = 0; i < student_no.length; i++) {
                
                Test test = new Test(); 
                
                // ⭕️ まず先に、配列からi番目の生徒番号と点数を取り出す！
                String stNo = student_no[i];
                String ptStr = point[i];
                
                // 1. 生徒の箱を用意して、生徒番号を入れる
                Student student = new Student();
                student.setNo(stNo); // ⭕️ no（回数）ではなく、stNo（生徒番号）を入れるのよ
                
                // 2. 科目の箱を用意して、科目コードを入れる
                Subject subject = new Subject();
                subject.setCd(subject_cd);

                // 3. 学校の箱を用意して、学校コードを入れる
                School school = new School();
                school.setCd(school_cd);
                
                // ⑤ 点数の文字を安全にチェックして、数字（int）に翻訳する
                int pt = 0;
                if (ptStr != null && !ptStr.isEmpty()) {
                    pt = Integer.parseInt(ptStr);
                }
                
                // ⑥ 成績の箱（test）に、合体させた箱と共通データを詰め込む
                test.setStudent(student);        
                test.setSubject(subject);  
                test.setSchool(school);        
                test.setNo(no);                  
                test.setPoint(pt);               
                test.setClassNum(class_num);    

                // ⑦ 保存！
                testDao.save(test);
            }
        }
        
        // ====== 6. レスポンス値をセット ======
        // なし

        // ====== 7. JSPへフォワード ======
        // ⚠️ 完了画面のJSPファイル名が間違っていないか確認しなさいね
        req.getRequestDispatcher("test_regist_done.jsp").forward(req,res);
    }
}



//入学年度とクラスを取得できていない問題を解決。
//山本さんが作ってくれた成績参照を参考にする。


