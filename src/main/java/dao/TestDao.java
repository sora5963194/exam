package dao;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;
 
public class TestDao extends Dao {
 
//	getメソッド: 学生、科目、学校、回数を指定して成績情報を1件取得する
    public Test get(Student student, Subject subject, School school, int no) throws Exception {
        Test test = null;
        Connection connection = getConnection();
        PreparedStatement statement = null;
 
        try {
            String sql = "SELECT * FROM TEST WHERE TRIM(STUDENT_NO) = ? AND SUBJECT_CD = ? AND TRIM(SCHOOL_CD) = ? AND NO = ?";
        	
            statement = connection.prepareStatement(sql);
            statement.setString(1, student.getNo());
            statement.setString(2, subject.getCd());
            statement.setString(3, school.getCd());
            statement.setInt(4, no);
 
            ResultSet rSet = statement.executeQuery();
 
            if (rSet.next()) {
                test = new Test();
                test.setStudent(student);
                test.setSubject(subject);
                test.setSchool(school);
                test.setNo(rSet.getInt("NO"));
                test.setPoint(rSet.getInt("POINT"));
                test.setClassNum(rSet.getString("CLASS_NUM"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (statement != null) { statement.close(); }
            if (connection != null) { connection.close(); }
        }
        return test;
    }
 

//    saveメソッド: 成績情報をデータベースに保存（新規追加 または 上書き更新）する
    public boolean save(Test test) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;
 
        try {
            Test old = get(test.getStudent(), test.getSubject(), test.getSchool(), test.getNo());
 
            if (old == null) {
                // INSERT（新規追加）
                String sql = "INSERT INTO TEST (STUDENT_NO, SUBJECT_CD, SCHOOL_CD, NO, POINT, CLASS_NUM) VALUES (?, ?, ?, ?, ?, ?)";
                statement = connection.prepareStatement(sql);
                statement.setString(1, test.getStudent().getNo());
                statement.setString(2, test.getSubject().getCd());
                statement.setString(3, test.getSchool().getCd());
                statement.setInt(4, test.getNo());
                statement.setInt(5, test.getPoint());
                statement.setString(6, test.getClassNum());
            } else {
                // UPDATE（上書き更新）
                String sql = "UPDATE TEST SET POINT = ?, CLASS_NUM = ? WHERE STUDENT_NO = ? AND SUBJECT_CD = ? AND SCHOOL_CD = ? AND NO = ?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, test.getPoint());
                statement.setString(2, test.getClassNum());
                statement.setString(3, test.getStudent().getNo());
                statement.setString(4, test.getSubject().getCd());
                statement.setString(5, test.getSchool().getCd());
                statement.setInt(6, test.getNo());
            }
 
            count = statement.executeUpdate();
 
        } catch (Exception e) {
            throw e;
        } finally {
            if (statement != null) { statement.close(); }
            if (connection != null) { connection.close(); }
        }
        return count > 0;
    }


//     filterメソッド: 指定された条件の「生徒全員」と、もしあれば「成績データ」を紐付けて取得する

    public List<Test> filter(int entYear, String classNum, Subject subject, int num, School school) throws Exception {
        List<Test> list = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
        	String sql = "SELECT s.NO AS STUDENT_NO, s.NAME AS STUDENT_NAME, s.ENT_YEAR, s.CLASS_NUM, t.NO AS TEST_NO, t.POINT "
        	           + "FROM STUDENT s "
        	           + "LEFT OUTER JOIN TEST t "
        	           + "  ON TRIM(s.NO) = TRIM(t.STUDENT_NO) " 
        	           + "  AND t.SUBJECT_CD = ? "
        	           + "  AND t.NO = ? "
        	           + "  AND TRIM(t.SCHOOL_CD) = ? "        
        	           + "WHERE s.ENT_YEAR = ? "
        	           + "  AND TRIM(s.CLASS_NUM) = ? "         
        	           + "  AND TRIM(s.SCHOOL_CD) = ? "         
        	           + "ORDER BY s.NO ASC";

            statement = connection.prepareStatement(sql);
            // JOIN条件の「?」にセット
            statement.setString(1, subject.getCd());
            statement.setInt(2, num);
            statement.setString(3, school.getCd());
            // WHERE条件の「?」にセット
            statement.setInt(4, entYear);
            statement.setString(5, classNum);
            statement.setString(6, school.getCd());

            ResultSet rSet = statement.executeQuery();

            while (rSet.next()) {
                Test test = new Test();
                
                Student student = new Student();
                student.setNo(rSet.getString("STUDENT_NO"));
                student.setName(rSet.getString("STUDENT_NAME"));
                student.setEntYear(rSet.getInt("ENT_YEAR"));
                student.setClassNum(rSet.getString("CLASS_NUM"));
                student.setSchool(school);
                
                test.setStudent(student);
                test.setSubject(subject);
                test.setSchool(school);
                test.setClassNum(rSet.getString("CLASS_NUM"));
                
                int testNo = rSet.getInt("TEST_NO");
                test.setNo(testNo); 
                
                if (testNo != 0) {
                    test.setPoint(rSet.getInt("POINT"));
                } else {
                    // データがない場合は「-1」をセットして、JSP側で「未入力（空欄）」として扱えるようにする
                    test.setPoint(-1); 
                }

                list.add(test);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (statement != null) { statement.close(); }
            if (connection != null) { connection.close(); }
        }

        return list;
    }

    public List<Integer> getEntYearList(School school) throws Exception {
        List<Integer> list = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement statement = null;
        
        try {
            // 重複を除いた入学年度を昇順で取得するSQLよ
            String sql = "SELECT DISTINCT ENT_YEAR FROM STUDENT WHERE TRIM(SCHOOL_CD) = ? ORDER BY ENT_YEAR ASC";
            statement = connection.prepareStatement(sql);
            statement.setString(1, school.getCd());
            
            ResultSet rSet = statement.executeQuery();
            while (rSet.next()) {
                list.add(rSet.getInt("ENT_YEAR"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (statement != null) { statement.close(); }
            if (connection != null) { connection.close(); }
        }
        return list;
    }
}