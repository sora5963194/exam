package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.TestListClass;

public class TestListClassDao extends Dao {

    public List<TestListClass> filter(
            int entYear,
            String classNum,
            String subjectCd)
            throws Exception {

        List<TestListClass> list =
                new ArrayList<>();

        Connection con =
                getConnection();

        String sql =

        "SELECT s.ent_year, " +
        "s.class_num, " +
        "s.no, " +
        "s.name, " +

        "MAX(CASE WHEN t.no=1 THEN t.point END) point1, " +
        "MAX(CASE WHEN t.no=2 THEN t.point END) point2 " +

        "FROM student s " +

        "LEFT JOIN test t " +
        "ON s.no=t.student_no " +
        "AND t.subject_cd=? " +

        "WHERE s.ent_year=? " +
        "AND s.class_num=? " +

        "GROUP BY " +
        "s.ent_year, " +
        "s.class_num, " +
        "s.no, " +
        "s.name " +

        "ORDER BY s.no";

        PreparedStatement st =
                con.prepareStatement(sql);

        st.setString(1, subjectCd);
        st.setInt(2, entYear);
        st.setString(3, classNum);

        ResultSet rs =
                st.executeQuery();

        while (rs.next()) {

            TestListClass obj =
                    new TestListClass();

            obj.setEntYear(
                rs.getInt("ent_year"));

            obj.setClassNum(
                rs.getString("class_num"));

            obj.setStudentNo(
                rs.getString("no"));

            obj.setStudentName(
                rs.getString("name"));

            obj.setPoint1(
                (Integer)rs.getObject("point1"));

            obj.setPoint2(
                (Integer)rs.getObject("point2"));

            list.add(obj);
        }

        rs.close();
        st.close();
        con.close();

        return list;
    }
}