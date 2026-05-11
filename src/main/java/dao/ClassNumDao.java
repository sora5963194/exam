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
		
	}
	
	public boolean save(ClassNum classNum , String newClassNum) throws Exception{
		
	}

}
