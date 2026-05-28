package bean;

import java.io.Serializable;
import java.util.Map;

public class TestListSubject extends User implements Serializable{
	/**
	 * 科目名
	 */
	private int entYear ;
	/**
	 * 学生番号
	 */
	private String studentNo ;
	/**
	 * 学生名
	 */
	private String studentName ;
	/**
	 * クラス番号
	 */
	private String classNum ;
	/**
	 * Map<回目,点数>
	 */
	private Map<Integer,Integer> points ;
	
	public int getEntYear() {
		return entYear;
	}
	public void setEntYear(int entYear) {
		this.entYear = entYear;
	}
	public String getStudentNo() {
		return studentNo;
	}
	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getClassNum() {
		return classNum;
	}
	public void setClassNum(String classNum) {
		this.classNum = classNum;
	}
	public Map<Integer, Integer> getPoints() {
		return points;
	}
	public void setPoints(Map<Integer, Integer> points) {
		this.points = points;
	}
	// 追加
    public int getPoint(int testNo) {
        if (points != null && points.containsKey(testNo)) {
            return points.get(testNo);
        }
        return -1;
    }
	
	

}
