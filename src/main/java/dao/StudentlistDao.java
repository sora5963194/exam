package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Student;

public class StudentlistDao extends Dao {

	/**
	 * 学生一覧取得
	 *
	 * @return 学生リスト
	 * @throws Exception
	 */
	public List<Student> filter() throws Exception {

		// 学生リスト
		List<Student> list = new ArrayList<Student>();

		// DB接続
		Connection connection = getConnection();

		// プリペアードステートメント
		PreparedStatement statement = null;

		try {

			// SQL文
			statement = connection.prepareStatement(
				"select * from student"
			);

			// SQL実行
			ResultSet resultSet = statement.executeQuery();

			// SchoolDao
			SchoolDao schoolDao = new SchoolDao();

			// リザルトセット処理
			while (resultSet.next()) {

				Student student = new Student();

				student.setNo(resultSet.getString("no"));
				student.setName(resultSet.getString("name"));
				student.setEntYear(resultSet.getInt("ent_year"));
				student.setClassNum(resultSet.getString("class_num"));
				student.setAttend(resultSet.getBoolean("is_attend"));

				// 学校情報セット
				student.setSchool(
					schoolDao.get(resultSet.getString("school_cd"))
				);

				// リストへ追加
				list.add(student);
			}

		} catch (Exception e) {

			throw e;

		} finally {

			// statement close
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}

			// connection close
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
		}
		return list;
	}
}

