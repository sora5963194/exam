package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Student;
import bean.TestListStudent;

public class TestListStudentDao extends Dao{
	private List<TestListStudent> postFilter(ResultSet rSet) throws Exception{
		//リストを初期化
		List<TestListStudent> list = new ArrayList<>();

        try {
        //リザルトセットを全件走査
		while (rSet.next()) {
			//成績参照学生インスタンスを初期化
			TestListStudent test = new TestListStudent();

			//成績参照学生インスタンスに検索結果をセット
			test.setSubjectName(rSet.getString("name"));
            test.setSubjectCd(rSet.getString("subject_cd"));
            test.setNum(rSet.getInt("no"));
            test.setPoint(rSet.getInt("point"));

            //リストに追加
            list.add(test);
        }
        }catch(SQLException | NullPointerException e) {
			e.printStackTrace();
		}
	        return list;
	    }

	public List<TestListStudent> filter(Student student) throws Exception{
		//リストを初期化
		List<TestListStudent> list = new ArrayList<>();
		//コネクションを確立
		Connection connection =getConnection();
		
		//プリペアードステートメント
		PreparedStatement statement = null;
		
		//リザルトセット
		ResultSet rSet = null;

		try {
		//プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement(
					"SELECT s.name AS subject_name, t.subject_cd, t.no, t.point "
				    + "FROM test t "
				    + "JOIN subject s ON t.subject_cd = s.cd "
				    + "WHERE t.student_no = ? "
				    + "ORDER BY t.subject_cd, t.no"
	    		);

		//プリペアードステートメントに学生番号をバインド
			statement.setString(1, student.getNo());

		// プリペアードステートメントを実行
			rSet = statement.executeQuery();

			list = postFilter(rSet);
		}catch (Exception e) {
			throw e;
		} finally {
			// プリペアードステートメントを閉じる
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
			// コネクションを閉じる
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

