package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bean.School;
import bean.Subject;
import bean.TestListSubject;

public class TestListSubjectDao extends Dao{
	private List<TestListSubject> postFilter(ResultSet rSet) throws Exception{
		//マップを初期化
        Map<String, TestListSubject> map = new LinkedHashMap<>();
		
		try {
			//リザルトセットを全件走査
			while (rSet.next()) {
				// 学生番号取得
                String studentNo = rSet.getString("student_no");
                // まだ未登録なら作成
                if (!map.containsKey(studentNo)) {
					//成績参照科目インスタンスを初期化
					TestListSubject test = new TestListSubject();
					//成績参照学生インスタンスに検索結果をセット
					test.setEntYear(rSet.getInt("ent_year"));
		            test.setStudentNo(studentNo);
		            test.setStudentName(rSet.getString("student_name"));
		            test.setClassNum(rSet.getString("class_num"));
		         // 点数Map初期化
                    test.setPoints(new LinkedHashMap<Integer, Integer>());
	            
                    map.put(studentNo, test);
                }
                // 既存データ取得
                TestListSubject test = map.get(studentNo);
                // 回数と点数を追加
                test.getPoints().put(
                        rSet.getInt("test_no"), //回数
                        rSet.getInt("point")); //点数
			}
		}finally {
	            if (rSet != null) {
	                rSet.close();
	            }
	        }

			return new ArrayList<>(map.values());
	    }
		
	public List<TestListSubject> filter(int entYear,String classNum,Subject subject,School school) throws Exception{
		//リストを初期化
		List<TestListSubject> list = new ArrayList<>();
		//コネクションを確立
		Connection connection =getConnection();
		//プリペアードステートメント
		PreparedStatement statement = null;
		//リザルトセット
		ResultSet rSet = null;
		
		try {
			String sql =
					"select " +
	                "s.ent_year, " +
	                "s.no as student_no, " +
	                "s.name as student_name, " +
	                "s.class_num, " +
	                "t.no as test_no, " +
	                "t.point " +
	                "from student s " +
	                "inner join test t " +
	                "on s.no = t.student_no " +
	                "where s.ent_year = ? " +
	                "and s.class_num = ? " +
	                "and t.subject_cd = ? " +
	                "and s.school_cd = ? " +
	                "order by s.no, t.no";
	                
			//プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement(sql);
			//プリペアードステートメントにバインド
            statement.setInt(1, entYear);
            statement.setString(2, classNum);
            statement.setString(3, subject.getCd());
            statement.setString(4, school.getCd());
            // SQL実行
            rSet = statement.executeQuery();
            // ResultSet → List変換
            list = postFilter(rSet);
            
            
		}finally {

            if (statement != null) {
                statement.close();
            }

            if (connection != null) {
                connection.close();
            }
        }

        return list;
    }
	

}

