package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;

public class TestDao extends Dao {

    /**
     * getメソッド: 学生、科目、学校、回数を指定して成績情報を1件取得する
     */
    public Test get(Student student, Subject subject, School school, int no) throws Exception {
        // テストインスタンスを初期化
        Test test = null;
        // データベースへのコネクションを確立
        Connection connection = getConnection();
        // プリペアードステートメント
        PreparedStatement statement = null;

        try {
            // SQL文の作成（スクリーンショット 2026-05-14 121831.png の物理名称を参照）
            String sql = "SELECT * FROM TEST WHERE STUDENT_NO = ? AND SUBJECT_CD = ? AND SCHOOL_CD = ? AND NO = ?";
            
            statement = connection.prepareStatement(sql);
            statement.setString(1, student.getNo());
            statement.setString(2, subject.getCd());
            statement.setString(3, school.getCd());
            statement.setInt(4, no);

            // クエリの実行
            ResultSet rSet = statement.executeQuery();

            if (rSet.next()) {
                test = new Test();
                // 取得した結果をBeanにセット
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
            // ステートメントとコネクションのクローズ
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return test;
    }
}