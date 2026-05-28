package bean;

public class TestListSubject {

    private int entYear;
    private String classNum;
    private String studentNo;
    private String studentName;
    private String point1;
    private String point2;

    public int getEntYear() {
        return entYear;
    }

    public void setEntYear(int entYear) {
        this.entYear = entYear;
    }

    public String getClassNum() {
        return classNum;
    }

    public void setClassNum(String classNum) {
        this.classNum = classNum;
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

    public String getPoint1() {
        return point1;
    }

    public void setPoint1(String point1) {
        this.point1 = point1;
    }

    public String getPoint2() {
        return point2;
    }

    public void setPoint2(String point2) {
        this.point2 = point2;
    }

    /**
     * 未登録の場合:
     *   1回目 → "-" を返す（JSPがそのまま表示するので「-」と出る）
     *   2回目 → -1  を返す（JSPが != -1 で判定して「-」と出す）
     */
    public Object getPoint(int no) {
        String val = (no == 1) ? point1 : point2;
        if (val == null || val.equals("-")) {
            // 1回目は文字列「-」、2回目は数値-1を返す
            return (no == 1) ? "-" : -1;
        }
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return (no == 1) ? "-" : -1;
        }
    }
}