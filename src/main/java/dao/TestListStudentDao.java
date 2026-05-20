package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.Student;
import bean.TestListStudent;

public class TestListStudentDao extends Dao{
	private List<TestListStudent> postFilter(ResultSet rSet) throws Exception{
		List<TestListStudent> list = new ArrayList<>();

        while (rSet.next()) {
            TestListStudent test = new TestListStudent();

            test.setSubjectName(rSet.getString("subject_name"));
            test.setSubjectCd(rSet.getString("subject_cd"));
            test.setNum(rSet.getInt("num"));
            test.setPoint(rSet.getInt("point"));

            list.add(test);
        }
	        return list;
	    }

	public List<TestListStudent> filter(Student student) throws Exception{
		Connection con = getConnection();

	    PreparedStatement st = con.prepareStatement(
	    		"SELECT s.name AS subject_name,t.subject_cd,t.no,t.point"
	    		+ "FROM test t"
	    		+ "JOIN subject s ON t.subject_cd = s.cd"
	    		+ "WHERE t.student_no = ?"
	    		+ "ORDER BY t.subject_cd, t.no"
	    		);

	    st.setString(1, student.getNo());

	    ResultSet rs = st.executeQuery();

	    List<TestListStudent> list = postFilter(rs);

	    st.close();
	    con.close();

	    return list;
	
		
	}
}
