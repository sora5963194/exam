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
    
    /**
     * saveメソッド: 成績情報をデータベースに保存（新規追加 または 上書き更新）する
     */
    public boolean save(Test test) throws Exception {
        // データベースへのコネクションを確立
        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;

        try {
            // 🌟 まずは、データベースにすでに同じデータがあるか get メソッドで探す
            Test old = get(test.getStudent(), test.getSubject(), test.getSchool(), test.getNo());

            if (old == null) {
                // データの箱（old）が空っぽだった ＝ まだ登録されていない ＝ INSERT（新規追加）
                String sql = "INSERT INTO TEST (STUDENT_NO, SUBJECT_CD, SCHOOL_CD, NO, POINT, CLASS_NUM) VALUES (?, ?, ?, ?, ?, ?)";
                statement = connection.prepareStatement(sql);
                // マトリョーシカの箱を開けて、中身をセットしていくわよ
                statement.setString(1, test.getStudent().getNo());
                statement.setString(2, test.getSubject().getCd());
                statement.setString(3, test.getSchool().getCd());
                statement.setInt(4, test.getNo());
                statement.setInt(5, test.getPoint());
                statement.setString(6, test.getClassNum());
            } else {
                // データがすでに見つかった ＝ 登録済み ＝ UPDATE（点数とクラス番号だけ上書き更新）
                String sql = "UPDATE TEST SET POINT = ?, CLASS_NUM = ? WHERE STUDENT_NO = ? AND SUBJECT_CD = ? AND SCHOOL_CD = ? AND NO = ?";
                statement = connection.prepareStatement(sql);
                // まず変更する値（点数、クラス）をセット
                statement.setInt(1, test.getPoint());
                statement.setString(2, test.getClassNum());
                // 次に条件（誰の、どのテストか）をセット
                statement.setString(3, test.getStudent().getNo());
                statement.setString(4, test.getSubject().getCd());
                statement.setString(5, test.getSchool().getCd());
                statement.setInt(6, test.getNo());
            }

            // クエリの実行（保存！）
            count = statement.executeUpdate();

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

        // 保存できた件数が0より大きければ成功（true）を返すわ
        return count > 0;
    }
}
