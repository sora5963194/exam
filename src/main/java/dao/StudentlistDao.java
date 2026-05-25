package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.Student;

public class StudentlistDao extends Dao {

	/**
	 * 学生一覧取得
	 */
	public List<Student> filter() throws Exception {

		List<Student> list = new ArrayList<Student>();

		Connection connection = getConnection();

		PreparedStatement statement = null;

		try {

			statement = connection.prepareStatement(
				"select * from student"
			);

			ResultSet resultSet =
					statement.executeQuery();

			SchoolDao schoolDao =
					new SchoolDao();

			while (resultSet.next()) {

				Student student =
						new Student();

				student.setNo(
					resultSet.getString("no"));

				student.setName(
					resultSet.getString("name"));

				student.setEntYear(
					resultSet.getInt("ent_year"));

				student.setClassNum(
					resultSet.getString("class_num"));

				student.setAttend(
					resultSet.getBoolean("is_attend"));

				student.setSchool(
					schoolDao.get(
					resultSet.getString(
					"school_cd"))
				);

				list.add(student);
			}

		} catch (Exception e) {

			throw e;

		} finally {

			if (statement != null) {
				statement.close();
			}

			if (connection != null) {
				connection.close();
			}
		}

		return list;
	}


	/**
	 * 条件検索
	 */
	public List<Student> filter(
			int entYear,
			String classNum
			) throws Exception {

		List<Student> list =
				new ArrayList<Student>();

		Connection connection =
				getConnection();

		PreparedStatement statement =
				null;

		try {

			statement =
					connection.prepareStatement(

					"select * from student "
					+ "where ent_year=? "
					+ "and class_num=?"

					);

			statement.setInt(
					1,
					entYear);

			statement.setString(
					2,
					classNum);

			ResultSet resultSet =
					statement.executeQuery();

			SchoolDao schoolDao =
					new SchoolDao();

			while(resultSet.next()){

				Student student =
						new Student();

				student.setNo(
					resultSet.getString("no"));

				student.setName(
					resultSet.getString("name"));

				student.setEntYear(
					resultSet.getInt(
							"ent_year"));

				student.setClassNum(
					resultSet.getString(
							"class_num"));

				student.setAttend(
					resultSet.getBoolean(
							"is_attend"));

				student.setSchool(
					schoolDao.get(
					resultSet.getString(
					"school_cd")));

				list.add(student);
			}

		} catch(Exception e){

			throw e;

		} finally {

			if(statement!=null){
				statement.close();
			}

			if(connection!=null){
				connection.close();
			}
		}

		return list;
	}
}