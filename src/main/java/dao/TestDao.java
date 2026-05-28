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
import bean.TestListSubject;

public class TestDao extends Dao {

    // 成績1件取得
    public Test get(
            Student student,
            Subject subject,
            School school,
            int no
    ) throws Exception {

        Test test=null;

        Connection con=getConnection();

        String sql=

        "SELECT * FROM TEST "+
        "WHERE STUDENT_NO=? "+
        "AND SUBJECT_CD=? "+
        "AND SCHOOL_CD=? "+
        "AND NO=?";

        PreparedStatement st=
                con.prepareStatement(sql);

        st.setString(1, student.getNo());
        st.setString(2, subject.getCd());
        st.setString(3, school.getCd());
        st.setInt(4, no);

        ResultSet rs=st.executeQuery();

        if(rs.next()){
            test=new Test();
            test.setStudent(student);
            test.setSubject(subject);
            test.setSchool(school);
            test.setNo(rs.getInt("NO"));
            test.setPoint(rs.getInt("POINT"));
            test.setClassNum(rs.getString("CLASS_NUM"));
        }

        rs.close();
        st.close();
        con.close();

        return test;
    }


    // 科目別成績一覧
    public List<TestListSubject> filterSubject(
            int entYear,
            String classNum,
            String subjectCd
    ) throws Exception{

        List<TestListSubject> list=new ArrayList<>();

        Connection con=getConnection();

        String sql=

        "SELECT " +
        "s.ent_year," +
        "s.class_num," +
        "s.no," +
        "s.name," +
        "MAX(CASE WHEN t.no=1 THEN t.point END) point1," +
        "MAX(CASE WHEN t.no=2 THEN t.point END) point2 "+
        "FROM student s "+
        "INNER JOIN test t "+
        "ON s.no=t.student_no "+
        "AND t.subject_cd=? "+
        "WHERE s.ent_year=? "+
        "AND s.class_num=? "+
        "GROUP BY "+
        "s.ent_year,s.class_num,s.no,s.name";

        PreparedStatement st=con.prepareStatement(sql);

        st.setString(1, subjectCd);
        st.setInt(2, entYear);
        st.setString(3, classNum);

        ResultSet rs=st.executeQuery();

        while(rs.next()){

            TestListSubject obj=new TestListSubject();

            obj.setEntYear(rs.getInt("ent_year"));
            obj.setClassNum(rs.getString("class_num"));
            obj.setStudentNo(rs.getString("no"));
            obj.setStudentName(rs.getString("name"));

            obj.setPoint1(
                    rs.getObject("point1")==null
                    ? "-"
                    : rs.getString("point1"));

            obj.setPoint2(
                    rs.getObject("point2")==null
                    ? "-"
                    : rs.getString("point2"));

            list.add(obj);
        }

        rs.close();
        st.close();
        con.close();

        return list;
    }


    // 科目名取得
    public String getSubjectName(String cd) throws Exception{

        String name="";

        Connection con=getConnection();

        String sql="SELECT NAME FROM SUBJECT WHERE CD=?";

        PreparedStatement st=con.prepareStatement(sql);

        st.setString(1, cd);

        ResultSet rs=st.executeQuery();

        if(rs.next()){
            name=rs.getString("NAME");
        }

        rs.close();
        st.close();
        con.close();

        return name;
    }


    // 成績登録
    public boolean save(Test test) throws Exception {

        Connection con=getConnection();

        PreparedStatement st=null;

        int count=0;

        try{

            Test old=get(
                    test.getStudent(),
                    test.getSubject(),
                    test.getSchool(),
                    test.getNo());

            if(old==null){

                st=con.prepareStatement(
                "insert into test(student_no,subject_cd,school_cd,no,point,class_num) values(?,?,?,?,?,?)"
                );

                st.setString(1, test.getStudent().getNo());
                st.setString(2, test.getSubject().getCd());
                st.setString(3, test.getSchool().getCd());
                st.setInt(4, test.getNo());
                st.setInt(5, test.getPoint());
                st.setString(6, test.getClassNum());

            }else{

                st=con.prepareStatement(
                "update test set point=? where student_no=? and subject_cd=? and school_cd=? and no=?"
                );

                st.setInt(1, test.getPoint());
                st.setString(2, test.getStudent().getNo());
                st.setString(3, test.getSubject().getCd());
                st.setString(4, test.getSchool().getCd());
                st.setInt(5, test.getNo());
            }

            count=st.executeUpdate();

        }finally{

            if(st!=null) st.close();
            if(con!=null) con.close();
        }

        return count>0;
    }
}