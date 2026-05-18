package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Studentlist implements Serializable {

	/**
	 * 学生リスト
	 */
	private List<Student> list = new ArrayList<Student>();

	/**
	 * 学生リスト取得
	 */
	public List<Student> getList() {
		return list;
	}

	/**
	 * 学生リスト設定
	 */
	public void setList(List<Student> list) {
		this.list = list;
	}

	/**
	 * 学生追加
	 */
	public void add(Student student) {
		list.add(student);
	}
}