package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.ClassNum;
import bean.School;

public class ClassNumDao extends Dao{
	public ClassNum get(String class_num, School school) throws Exception{
		// クラスインスタンスを初期化
		ClassNum classNum = null;
		//データベースへのコネクションを確立
		Connection connection =getConnection();
		//プリペアードステートメント
		PreparedStatement statement = null;
		
		try{
			//プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement("select * from class_num where class_num = ? and school_cd = ?");
			//プリペアードステートメントに科目コードをバインド
			statement.setString(1, class_num);
			statement.setString(2, school.getCd());
			// プリペアードステートメントを実行
			ResultSet rSet = statement.executeQuery();
			// データが存在した場合
			if (rSet.next()) {
				classNum = new ClassNum();
				// 値をセット
				classNum.setClass_num(rSet.getString("class_num"));
				School s = new School();
				s.setCd(rSet.getString("school_cd"));
				classNum.setSchool(s);
			} 
		}catch (Exception e) {
				throw e;
			} finally {
				// statementを閉じる
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException sqle) {
						throw sqle;
					}
				}
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException sqle) {
						throw sqle;
					}
				}
			}
		return classNum;
	}
	
	public List<String> filter(School school) throws Exception{
		System.out.println(school);
		//リストを初期化
		List<String> list = new ArrayList<>();
		
		//データベースのコネクションを確率
		Connection connection = getConnection();
		
		//プリペアードステートメント
		PreparedStatement statement = null;
		
		try {
			//プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement(
					"select class_num from class_num where school_cd=? order by class_num");
			
			//プリペアードステートメントに学校コードをバインド
			statement.setString(1, school.getCd());
			
			//プリペアードステートメントを実行
			ResultSet rSet = statement.executeQuery();
			
			//リザルトセットを全件走査
			while(rSet.next()) {
				
				//リストにクラス番号を追加
				list.add(rSet.getString("class_num"));
			}
		} catch (Exception e) {
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
	
	public boolean save(ClassNum classNum) throws Exception{
		//データベースへのコネクションを確立
		Connection connection =getConnection();
		//プリペアードステートメント
		PreparedStatement statement = null;
		//実行件数
		int count = 0;
		
		try {
			//プリペアードステートメントにINSERT文をセット
			statement = connection.prepareStatement("insert into class_num(school_cd, class_num) values(?, ?)");
			//プリペアードステートメントに値をバインド
			statement.setString(1, classNum.getSchool().getCd());
			statement.setString(2, classNum.getClass_num());
			// プリペアードステートメントを実行
			count = statement.executeUpdate();
			
		} catch (Exception e) {
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
 
		// 実行結果
		return count > 0;
		
	}
	
	public boolean save(ClassNum classNum , String newClassNum) throws Exception{
		
		//データベースへのコネクションを確立
		Connection connection =getConnection();
		//プリペアードステートメント
		PreparedStatement statement = null;
		//実行件数
		int count = 0;

		try {
			//プリペアードステートメントにUPDATE文をセット
			statement = connection.prepareStatement("update class_num set class_num=? where school_cd=? and class_num=?");

			//プリペアードステートメントに値をバインド
			statement.setString(1, classNum.getClass_num());
			statement.setString(2, classNum.getSchool().getCd());
			statement.setString(3, newClassNum);

			// プリペアードステートメントを実行
			count = statement.executeUpdate();

		} catch (Exception e) {
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
		// 実行結果
		return count > 0;
		
	}

}
