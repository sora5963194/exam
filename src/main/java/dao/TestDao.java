//package dao;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.List;
//
//import bean.School;
//import bean.Student;
//import bean.Subject;
//import bean.Test;
//
//public class TestDao extends Dao {
//
//    /**
//     * getメソッド: 学生、科目、学校、回数を指定して成績情報を1件取得する
//     */
//    public Test get(Student student, Subject subject, School school, int no) throws Exception {
//        // テストインスタンスを初期化
//        Test test = null;
//        // データベースへのコネクションを確立
//        Connection connection = getConnection();
//        // プリペアードステートメント
//        PreparedStatement statement = null;
//
//        try {
//            // SQL文の作成（スクリーンショット 2026-05-14 121831.png の物理名称を参照）
//            String sql = "SELECT * FROM TEST WHERE STUDENT_NO = ? AND SUBJECT_CD = ? AND SCHOOL_CD = ? AND NO = ?";
//            
//            statement = connection.prepareStatement(sql);
//            statement.setString(1, student.getNo());
//            statement.setString(2, subject.getCd());
//            statement.setString(3, school.getCd());
//            statement.setInt(4, no);
//
//            // クエリの実行
//            ResultSet rSet = statement.executeQuery();
//
//            if (rSet.next()) {
//                test = new Test();
//                // 取得した結果をBeanにセット
//                test.setStudent(student);
//                test.setSubject(subject);
//                test.setSchool(school);
//                test.setNo(rSet.getInt("NO"));
//                test.setPoint(rSet.getInt("POINT"));
//                test.setClassNum(rSet.getString("CLASS_NUM"));
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            // ステートメントとコネクションのクローズ
//            if (statement != null) {
//                statement.close();
//            }
//            if (connection != null) {
//                connection.close();
//            }
//        }
//
//        return test;
//    }
//    
//    /**
//     * saveメソッド: 成績情報をデータベースに保存（新規追加 または 上書き更新）する
//     */
//    public boolean save(Test test) throws Exception {
//        // データベースへのコネクションを確立
//        Connection connection = getConnection();
//        PreparedStatement statement = null;
//        int count = 0;
//
//        try {
//            // 🌟 まずは、データベースにすでに同じデータがあるか get メソッドで探す
//            Test old = get(test.getStudent(), test.getSubject(), test.getSchool(), test.getNo());
//
//            if (old == null) {
//                // データの箱（old）が空っぽだった ＝ まだ登録されていない ＝ INSERT（新規追加）
//                String sql = "INSERT INTO TEST (STUDENT_NO, SUBJECT_CD, SCHOOL_CD, NO, POINT, CLASS_NUM) VALUES (?, ?, ?, ?, ?, ?)";
//                statement = connection.prepareStatement(sql);
//                // マトリョーシカの箱を開けて、中身をセットしていくわよ
//                statement.setString(1, test.getStudent().getNo());
//                statement.setString(2, test.getSubject().getCd());
//                statement.setString(3, test.getSchool().getCd());
//                statement.setInt(4, test.getNo());
//                statement.setInt(5, test.getPoint());
//                statement.setString(6, test.getClassNum());
//            } else {
//                // データがすでに見つかった ＝ 登録済み ＝ UPDATE（点数とクラス番号だけ上書き更新）
//                String sql = "UPDATE TEST SET POINT = ?, CLASS_NUM = ? WHERE STUDENT_NO = ? AND SUBJECT_CD = ? AND SCHOOL_CD = ? AND NO = ?";
//                statement = connection.prepareStatement(sql);
//                // まず変更する値（点数、クラス）をセット
//                statement.setInt(1, test.getPoint());
//                statement.setString(2, test.getClassNum());
//                // 次に条件（誰の、どのテストか）をセット
//                statement.setString(3, test.getStudent().getNo());
//                statement.setString(4, test.getSubject().getCd());
//                statement.setString(5, test.getSchool().getCd());
//                statement.setInt(6, test.getNo());
//            }
//
//            // クエリの実行（保存！）
//            count = statement.executeUpdate();
//
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            // ステートメントとコネクションのクローズ
//            if (statement != null) {
//                statement.close();
//            }
//            if (connection != null) {
//                connection.close();
//            }
//        }
//
//        // 保存できた件数が0より大きければ成功（true）を返すわ
//        return count > 0;
//    }
//    
//    /**
//     * getEntYearListメソッド: 成績が登録されている学生の入学年度を重複なしで取得する
//     */
//    public List<Integer> getEntYearList(School school) throws Exception {
//        List<Integer> list = new ArrayList<>();
//        Connection connection = getConnection();
//        PreparedStatement statement = null;
//
//        try {
//            // TESTテーブルにある学生番号を使い、STUDENTテーブルから入学年度を重複なし(DISTINCT)で取得するわ
////            String sql = "SELECT DISTINCT s.ENT_YEAR FROM TEST t "
////                       + "INNER JOIN STUDENT s ON t.STUDENT_NO = s.NO "
////                       + "WHERE t.SCHOOL_CD = ? "
////                       + "ORDER BY s.ENT_YEAR ASC";
//        	String sql = "SELECT DISTINCT s.ENT_YEAR FROM TEST t "
//                    + "INNER JOIN STUDENT s ON TRIM(t.STUDENT_NO) = TRIM(s.NO) "
//                    + "WHERE t.SCHOOL_CD = ? "
//                    + "ORDER BY s.ENT_YEAR ASC";
//            
//            statement = connection.prepareStatement(sql);
//            statement.setString(1, school.getCd());
//            
//            ResultSet rSet = statement.executeQuery();
//            while (rSet.next()) {
//                list.add(rSet.getInt("ENT_YEAR"));
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            if (statement != null) statement.close();
//            if (connection != null) connection.close();
//        }
//        return list;
//    }
//
//    /**
//     * filterメソッド: 入学年度、クラス、科目、回数を指定して成績の一覧を取得する
//     */
//    public List<Test> filter(int entYear, String classNum, Subject subject, int num, School school) throws Exception {
//        List<Test> list = new ArrayList<>();
//        Connection connection = getConnection();
//        PreparedStatement statement = null;
//
//        try {
//            // 条件に合う成績データを、学生の情報(名前など)も含めて取得するわ
////            String sql = "SELECT t.STUDENT_NO, s.NAME AS STUDENT_NAME, t.POINT, t.CLASS_NUM "
////                       + "FROM TEST t "
////                       + "INNER JOIN STUDENT s ON t.STUDENT_NO = s.NO "
////                       + "WHERE s.ENT_YEAR = ? AND t.CLASS_NUM = ? AND t.SUBJECT_CD = ? AND t.NO = ? AND t.SCHOOL_CD = ? "
////                       + "ORDER BY t.STUDENT_NO ASC";
//        	String sql = "SELECT t.STUDENT_NO, s.NAME AS STUDENT_NAME, t.POINT, t.CLASS_NUM "
//                    + "FROM TEST t "
//                    + "INNER JOIN STUDENT s ON TRIM(t.STUDENT_NO) = TRIM(s.NO) "
//                    + "WHERE s.ENT_YEAR = ? AND t.CLASS_NUM = ? AND t.SUBJECT_CD = ? AND t.NO = ? AND t.SCHOOL_CD = ? "
//                    + "ORDER BY t.STUDENT_NO ASC";
//
//            statement = connection.prepareStatement(sql);
//            statement.setInt(1, entYear);
//            statement.setString(2, classNum);
//            statement.setString(3, subject.getCd());
//            statement.setInt(4, num);
//            statement.setString(5, school.getCd());
//
//            ResultSet rSet = statement.executeQuery();
//            while (rSet.next()) {
//                Test test = new Test();
//                
//                // 学生情報を組み立ててセット
//                Student student = new Student();
//                student.setNo(rSet.getString("STUDENT_NO"));
//                student.setName(rSet.getString("STUDENT_NAME"));
//                
//                test.setStudent(student);
//                test.setSubject(subject);
//                test.setSchool(school);
//                test.setNo(num);
//                test.setPoint(rSet.getInt("POINT"));
//                test.setClassNum(rSet.getString("CLASS_NUM"));
//
//                list.add(test);
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            if (statement != null) statement.close();
//            if (connection != null) connection.close();
//        }
//        return list;
//    }
//}



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
 
    /**
     * 成績1件取得
     */
    public Test get(Student student, Subject subject, School school, int no) throws Exception {
 
        Test test = null;
 
        Connection connection = getConnection();
        PreparedStatement statement = null;
 
        try {
 
            String sql =
                    "SELECT POINT, CLASS_NUM " +
                    "FROM TEST " +
                    "WHERE STUDENT_NO = ? " +
                    "AND SUBJECT_CD = ? " +
                    "AND SCHOOL_CD = ? " +
                    "AND NO = ?";
 
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
 
                test.setNo(no);
 
                test.setPoint(rSet.getInt("POINT"));
                test.setClassNum(rSet.getString("CLASS_NUM"));
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
 
        return test;
    }
 
    /**
     * 成績保存
     */
    public boolean save(Test test) throws Exception {
 
        Connection connection = getConnection();
        PreparedStatement statement = null;
 
        int count = 0;
 
        try {
 
            Test old =
                    get(
                            test.getStudent(),
                            test.getSubject(),
                            test.getSchool(),
                            test.getNo());
 
            if (old == null) {
 
                String sql =
                        "INSERT INTO TEST " +
                        "(STUDENT_NO, SUBJECT_CD, SCHOOL_CD, NO, POINT, CLASS_NUM) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
 
                statement = connection.prepareStatement(sql);
 
                statement.setString(1, test.getStudent().getNo());
                statement.setString(2, test.getSubject().getCd());
                statement.setString(3, test.getSchool().getCd());
                statement.setInt(4, test.getNo());
                statement.setInt(5, test.getPoint());
                statement.setString(6, test.getClassNum());
 
            } else {
 
                String sql =
                        "UPDATE TEST " +
                        "SET POINT = ?, CLASS_NUM = ? " +
                        "WHERE STUDENT_NO = ? " +
                        "AND SUBJECT_CD = ? " +
                        "AND SCHOOL_CD = ? " +
                        "AND NO = ?";
 
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
 
            if (statement != null) {
                statement.close();
            }
 
            if (connection != null) {
                connection.close();
            }
        }
 
        return count > 0;
    }
 
    /**
     * 入学年度一覧取得
     */
    public List<Integer> getEntYearList(School school) throws Exception {
 
        List<Integer> list = new ArrayList<>();
 
        Connection connection = getConnection();
        PreparedStatement statement = null;
 
        try {
 
            String sql =
                    "SELECT DISTINCT s.ENT_YEAR " +
                    "FROM TEST t " +
                    "INNER JOIN STUDENT s " +
                    "ON TRIM(t.STUDENT_NO) = TRIM(s.NO) " +
                    "WHERE t.SCHOOL_CD = ? " +
                    "ORDER BY s.ENT_YEAR ASC";
 
            statement = connection.prepareStatement(sql);
 
            statement.setString(1, school.getCd());
 
            ResultSet rSet = statement.executeQuery();
 
            while (rSet.next()) {
 
                list.add(rSet.getInt("ENT_YEAR"));
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
     * クラス番号一覧取得
     */
    public List<String> getClassNumList(School school) throws Exception {
 
        List<String> list = new ArrayList<>();
 
        Connection connection = getConnection();
        PreparedStatement statement = null;
 
        try {
 
            String sql =
                    "SELECT DISTINCT CLASS_NUM " +
                    "FROM TEST " +
                    "WHERE SCHOOL_CD = ? " +
                    "ORDER BY CLASS_NUM ASC";
 
            statement = connection.prepareStatement(sql);
 
            statement.setString(1, school.getCd());
 
            ResultSet rSet = statement.executeQuery();
 
            while (rSet.next()) {
 
                list.add(rSet.getString("CLASS_NUM"));
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
     * 科目一覧取得
     */
    public List<Subject> getSubjectList(School school) throws Exception {
 
        List<Subject> list = new ArrayList<>();
 
        Connection connection = getConnection();
        PreparedStatement statement = null;
 
        try {
 
            String sql =
                    "SELECT DISTINCT s.CD, s.NAME " +
                    "FROM TEST t " +
                    "INNER JOIN SUBJECT s " +
                    "ON t.SUBJECT_CD = s.CD " +
                    "WHERE t.SCHOOL_CD = ? " +
                    "ORDER BY s.CD ASC";
 
            statement = connection.prepareStatement(sql);
 
            statement.setString(1, school.getCd());
 
            ResultSet rSet = statement.executeQuery();
 
            while (rSet.next()) {
 
                Subject subject = new Subject();
 
                subject.setCd(rSet.getString("CD"));
                subject.setName(rSet.getString("NAME"));
 
                list.add(subject);
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
     * 回数一覧取得
     */
    public List<Integer> getNoList(School school) throws Exception {
 
        List<Integer> list = new ArrayList<>();
 
        Connection connection = getConnection();
        PreparedStatement statement = null;
 
        try {
 
            String sql =
                    "SELECT DISTINCT NO " +
                    "FROM TEST " +
                    "WHERE SCHOOL_CD = ? " +
                    "ORDER BY NO ASC";
 
            statement = connection.prepareStatement(sql);
 
            statement.setString(1, school.getCd());
 
            ResultSet rSet = statement.executeQuery();
 
            while (rSet.next()) {
 
                list.add(rSet.getInt("NO"));
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
    public List<Test> filter(
            int entYear,
            String classNum,
            Subject subject,
            int num,
            School school) throws Exception {
 
        List<Test> list = new ArrayList<>();
 
        Connection connection = getConnection();
        PreparedStatement statement = null;
 
        try {
 
            String sql =
                    "SELECT t.STUDENT_NO, " +
                    "s.NAME AS STUDENT_NAME, " +
                    "t.POINT, " +
                    "t.CLASS_NUM " +
                    "FROM TEST t " +
                    "INNER JOIN STUDENT s " +
                    "ON TRIM(t.STUDENT_NO) = TRIM(s.NO) " +
                    "WHERE s.ENT_YEAR = ? " +
                    "AND t.CLASS_NUM = ? " +
                    "AND t.SUBJECT_CD = ? " +
                    "AND t.NO = ? " +
                    "AND t.SCHOOL_CD = ? " +
                    "ORDER BY t.STUDENT_NO ASC";
 
            statement = connection.prepareStatement(sql);
 
            statement.setInt(1, entYear);
            statement.setString(2, classNum);
            statement.setString(3, subject.getCd());
            statement.setInt(4, num);
            statement.setString(5, school.getCd());
 
            ResultSet rSet = statement.executeQuery();
 
            while (rSet.next()) {
 
                Test test = new Test();
 
                Student student = new Student();
 
                student.setNo(rSet.getString("STUDENT_NO"));
                student.setName(rSet.getString("STUDENT_NAME"));
 
                test.setStudent(student);
 
                test.setSubject(subject);
 
                test.setSchool(school);
 
                test.setNo(num);
 
                test.setPoint(rSet.getInt("POINT"));
 
                test.setClassNum(rSet.getString("CLASS_NUM"));
 
                list.add(test);
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
}
 