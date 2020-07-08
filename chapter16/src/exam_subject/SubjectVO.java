package exam_subject;

/*
 * VO(Value Object) Ŭ������ �����͸� ��� �����̳� ������ �ϴ� Ŭ������ ������ ������ ��������
 * ������� Ŭ�����̴�. VO Ŭ������ ���ø����̼� �������� �� �ܰ��� ����� ������ �����ϴ� ������ �����ϸ�,
 * �Ӽ�(attribute: �ʵ�), setter(������)�� getter(������)�� �����ȴ�.
 */
public class SubjectVO {
	private int no; // ��ȣ
	private String s_num; // �а���ȣ
	private String s_name; // �а���

	public SubjectVO() {
	}

	public SubjectVO(int no, String s_num, String s_name) {
		this.no = no;
		this.s_num = s_num;
		this.s_name = s_name;
	}

	public int getNo() {
		return no;
	}

	public String getS_name() {
		return s_name;
	}

	public String getS_num() {
		return s_num;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public void setS_name(String s_name) {
		this.s_name = s_name;
	}

	public void setS_num(String s_num) {
		this.s_num = s_num;
	}

	@Override
	public String toString() {
		return "[SUBJECT] no:" + no + ", s_num:" + s_num + ", s_name:" + s_name;
	}
}