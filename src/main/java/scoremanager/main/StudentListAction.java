package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class StudentListAction extends Action{
	
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		HttpSession session = req.getSession();//セッションの中のuserという名前のTeacher beanを取り出した
		Teacher teacher = (Teacher)session.getAttribute("user");
		
		// ローカル変数の指定 1
		String entYearStr = ""; // 入力された入学年度
		String classNum = ""; // 入力されたクラス番号
		String isAttendStr = ""; // 入力された在学フラグ
		
		int entYear = 0; // 入学年度
		boolean isAttend = false; // 在学フラグ
		List<Student> students = null; // 学生リスト
		LocalDate todaysDate = LocalDate.now(); // LocalDateインスタンスを取得
		int year = todaysDate.getYear(); // 現在の年を取得
		StudentDao studentDao = new StudentDao(); // 学生Dao
		ClassNumDao classNumDao = new ClassNumDao(); // クラス番号Daoを初期化
		Map<String, String> errors = new HashMap<>(); // エラーメッセージ
		
		//リクエストパラメーターの取得2
		entYearStr = req.getParameter("f1");
		classNum = req.getParameter("f2");
		isAttendStr = req.getParameter("f3");
		
		//ビジネスロジック4
		if(entYearStr != null) {
			//数値に変換
			entYear = Integer.parseInt(entYearStr);
		}
		
		if(isAttendStr != null) { //在学フラグがnullじゃなかった場合(変数f3に文字列"t"が入力される)
			//在学フラグをtrueに変更
			isAttend = true;
		}
		
		//リストを初期化
		List<Integer> entYearSet = new ArrayList<>();
		//10年前から1年後まで年をリストに追加
		for (int i = year -10 ; i <= year + 1 ; i++) {
			entYearSet.add(i);
		}
		
		//DBからデータ取得3
		//ログインユーザーの学校コードをもとにクラス番号の一覧を取得
		List<String> list = classNumDao.filter(teacher.getSchool());
		
		System.out.println(list);
		
		if(entYear != 0 && !classNum.equals("0")) {
			//入学年度とクラス番号を指定
			students = studentDao.filter(teacher.getSchool(), entYear, classNum, isAttend);
		}
		else if(entYear != 0 && classNum.equals("0")) {
			//入学年度のみ指定
			students = studentDao.filter(teacher.getSchool(), entYear, isAttend);
		}
		else if(entYear == 0 && classNum == null || entYear == 0 && classNum.equals("0")) {
			//指定なしの場合
			//全学生情報を取得
			students = studentDao.filter(teacher.getSchool(), isAttend);
		}
		else {
			errors.put("f1", "クラスを指定する場合は入学年度も指定してください");
			req.setAttribute("errors" ,errors);
			//全学年情報を取得
			students = studentDao.filter(teacher.getSchool(), isAttend);
		}
		
		
		
		//レスポンス値をセット６
		//リクエストに入学年度をセット
		req.setAttribute("f1", entYear);
		
		//リクエストにクラス番号をセット
		req.setAttribute("f2", classNum);
		
		//在学フラグが送信されていた場合
		if(isAttendStr != null) {
			
			//在学フラグを立てる
			isAttend = true;
			
			//リクエストに在学フラグをセット
			req.setAttribute("f3", isAttendStr);
		}
		
		//リクエストに学生リストをセット
		req.setAttribute("students", students);
		
		//リクエストにデータをセット
		req.setAttribute("class_num_set", list);
		req.setAttribute("ent_year_set", entYearSet);
		
		//JSPへフォワード７
		req.getRequestDispatcher("student_list.jsp").forward(req,res);
	}

}
