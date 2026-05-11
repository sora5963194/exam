package scoremanager.main;

import bean.Student;
import dao.StudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tool.Action;


public class StudentUpdateExecuteAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// ローカル変数の指定 1
		int ent_year = 0; // 選択された入学年度
		String no = ""; // 入力された学生番号
		String name = ""; // 入力された氏名
		String class_num = ""; // 選択されたクラス番号
		String is_attend = ""; // 選択された在学フラグ
		boolean isAttend = false;
		Student student = new Student();
		StudentDao studentDao = new StudentDao();

		// リクエストパラメーターの取得 2
		ent_year = Integer.parseInt(req.getParameter("ent_year"));
		no = req.getParameter("no");
		name = req.getParameter("name");
		class_num = req.getParameter("class_num");
		is_attend = req.getParameter("is_attend");

		// DBからデータ取得 3
		// なし

		// ビジネスロジック 4
		if (is_attend != null) {
			isAttend = true;
		}
		// studentに学生情報をセット
		student.setName(name);
		student.setEntYear(ent_year);
		student.setClassNum(class_num);
		student.setAttend(isAttend);
		student.setNo(no);
		
		// 変更内容を保存
		studentDao.save(student);

		// レスポンス値をセット 6
		// なし

		// JSPへフォワード 7
		req.getRequestDispatcher("student_update_done.jsp").forward(req,res);
	}

}
