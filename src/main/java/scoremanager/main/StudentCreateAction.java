package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import bean.Teacher;
import dao.ClassNumDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;



public class StudentCreateAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		HttpSession session = req.getSession();//セッションの中のuserという名前のTeacher beanを取り出した
		Teacher teacher = (Teacher)session.getAttribute("user");

		// ローカル変数の指定 1
		ClassNumDao classNumDao = new ClassNumDao(); // クラス番号Daoを初期化
		LocalDate todaysDate = LocalDate.now(); // LocalDateインスタンスを取得
		int year = todaysDate.getYear(); // 現在の年を取得

		// リクエストパラメーターの取得 2
		// なし

		// DBからデータ取得 3
		// ログインユーザーの学校コードをもとにクラス番号の一覧を取得
		List<String> list = classNumDao.filter(teacher.getSchool());

		// ビジネスロジック 4
		// リストを初期化
		List<Integer> entYearSet = new ArrayList<>();
		// 10年前から10年後まで年をリストに追加
		for (int i = year -10 ; i <= year + 10 ; i++) {
			entYearSet.add(i);
		}

		// レスポンス値をセット 6
		// リクエストにデータをセット
		
		//リクエストにデータをセット
		req.setAttribute("class_num_set", list);
		req.setAttribute("ent_year_set", entYearSet);

		// JSPへフォワード 7
		req.getRequestDispatcher("student_create.jsp").forward(req,res);
	}

}

