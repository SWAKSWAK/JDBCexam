package exam_subject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * DAO(Data Access Object) Ŭ������ ������ ó����  �ñ����� �ܰ��̴�
 * 
 * CRUD ���α׷� ����
 * �⺻���� ������ ó�� ����� �Է� (create, insert),
 * ��ȸ(Read(�Ǵ� Retrieve), Select), ����(Update),
 * ����(Delete) ����� ������ �����ͺ��̽� ���α׷�
 */
public class SubjectDAO {

	// �����ͺ��̽� ���� ���� ��� ����
	private static final String JDBC_URL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
	private static final String USER = "javauser";
	private static final String PASSWD = "java1234";

	private static SubjectDAO instance = null;// �ڽ��� Ÿ��

	public static SubjectDAO getInstance() {
		if (instance == null)// �̹� �����ƴٸ� instance ����
			instance = new SubjectDAO();
		return instance;
	}

	private SubjectDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}

	private Connection getConnection() throws SQLException {
		Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWD);
		return con;
	}

	// �а� ���̺� ������ �Է� �޼���
	public boolean subjectInsert(SubjectVO svo) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("insert into subject(no, s_num, s_name) ");
		sql.append("values(subject_seq.nextval, ?, ?)");

		Connection con = null;
		PreparedStatement pstmt = null;
		boolean success = false;

		try {
			con = getConnection();

			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, svo.getS_num());
			pstmt.setString(2, svo.getS_name());

			int i = pstmt.executeUpdate();
			if (i == 1)
				success = true;
		} catch (SQLException se) {
			System.out.println("���� error = [ " + se + " ]");
			success = false;
		} catch (Exception e) {
			System.out.println("error = [ " + e + " ]");
			success = false;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println("��� ���� ���� error = [ " + e + " ]");
			}
		}
		return success;
	}

	// �а� ���̺� ������ ���� �޼���
	public boolean subjectUpdate(SubjectVO svo) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("update subject set s_num = ?, s_name = ? where no = ?");

		Connection con = null;
		PreparedStatement pstmt = null;
		boolean success = false;

		try {
			con = getConnection();

			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, svo.getS_num());
			pstmt.setString(2, svo.getS_name());
			pstmt.setInt(3, svo.getNo());

			int i = pstmt.executeUpdate();
			if (i == 1)
				success = true;
		} catch (SQLException se) {
			System.out.println("���� error = [ " + se + " ]");
			success = false;
		} catch (Exception e) {
			System.out.println("error = [ " + e + " ]");
			success = false;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println("��� ���� ���� error = [ " + e + " ]");
			}
		}
		return success;
	}

	// �а� ���̺� ������ ���� �޼���
	public boolean subjectDelete(SubjectVO svo) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("delete from subject where no = ?");

		Connection con = null;
		PreparedStatement pstmt = null;
		boolean success = false;

		try {
			con = getConnection();

			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, svo.getNo());

			int i = pstmt.executeUpdate();

			if (i == 1)
				success = true;
		} catch (SQLException se) {
			System.out.println("���� error = [ " + se + " ]");
			success = false;
		} catch (Exception e) {
			System.out.println("error = [ " + e + " ]");
			success = false;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println("��� ���� ���� error = [ " + e + " ]");
			}
		}
		return success;
	}

	public ArrayList<SubjectVO> getSubjectSearch(String s_name) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select no , s_num, s_name from subject ");
		sql.append("where s_name like ? order by no");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SubjectVO svo = null;
		ArrayList<SubjectVO> list = new ArrayList<SubjectVO>();

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, "%" + s_name + "%");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				svo = new SubjectVO();
				svo.setNo(rs.getInt("no"));
				svo.setS_num(rs.getString("s_num"));
				svo.setS_name(rs.getString("s_name"));

				list.add(svo);
			}
		} catch (SQLException se) {
			System.out.println("���� error = [ " + se + " ]");
		} catch (Exception e) {
			System.out.println("error = [ " + e + " ]");
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println("DB ���� ���� error = [ " + e + " ]");
			}
		}
		return list;
	}

	// �а� ���̺��� ��� ���ڵ带 ��ȯ �ż���
	public ArrayList<SubjectVO> getSubjectTotal() {
		StringBuffer sql = new StringBuffer();
		sql.append("select no, s_num, s_name from subject order by no");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SubjectVO svo = null;
		ArrayList<SubjectVO> list = new ArrayList<SubjectVO>();

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			// ResultSet�� ������� ��� ���� ������ SubjectVO ��ü�� ����
			while (rs.next()) {
				// �� ���� �а� ������ ������ VO��ü ����
				svo = new SubjectVO();
				// �� ���� �а� ������ VO��ü�� ����
				svo.setNo(rs.getInt("no"));
				svo.setS_num(rs.getString("s_num"));
				svo.setS_name(rs.getString("s_name"));

				// ArrayList ��ü�� ���ҷ� �߰�
				list.add(svo);
			}
		} catch (SQLException se) {
			System.out.println("���� error = [ " + se + " ]");
			se.printStackTrace();
		} catch (Exception e) {
			System.out.println("error = [ " + e + " ]");
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println("DB ���� ���� error = [ " + e + " ]");
			}
		}
		return list;
	}

	public String getSubjectNum() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select nvl(lpad(max(to_number(LTRIM(s_num,'0')))+1,2,'0'),'01') "
				+ "as subjectNum from subject");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String subjectNumber = "";

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			if (rs.next())
				subjectNumber = rs.getString("subjectNum");
		} catch (SQLException se) {
			System.out.println("���� getSubjectNum error = [ " + se + " ]");
		} catch (Exception e) {
			System.out.println("error = [ " + e + " ]");
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println("DB ���� ���� error = [ " + e + " ]");
			}
		}
		return subjectNumber;
	}
}
