package exam_subject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * DAO(Data Access Object) 클래스가 데이터 처리의  궁극적인 단계이다
 * 
 * CRUD 프로그램 구현
 * 기본적인 데이터 처리 기능인 입력 (create, insert),
 * 조회(Read(또는 Retrieve), Select), 수정(Update),
 * 삭제(Delete) 기능을 구현한 데이터베이스 프로그램
 */
public class SubjectDAO {

	// 데이터베이스 연결 관련 상수 선언
	private static final String JDBC_URL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
	private static final String USER = "javauser";
	private static final String PASSWD = "java1234";

	private static SubjectDAO instance = null;// 자신의 타입

	public static SubjectDAO getInstance() {
		if (instance == null)// 이미 생성됐다면 instance 리턴
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

	// 학과 테이블에 데이터 입력 메서드
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
			System.out.println("쿼리 error = [ " + se + " ]");
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
				System.out.println("디비 연동 해제 error = [ " + e + " ]");
			}
		}
		return success;
	}

	// 학과 테이블에 데이터 수정 메서드
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
			System.out.println("쿼리 error = [ " + se + " ]");
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
				System.out.println("디비 연동 해제 error = [ " + e + " ]");
			}
		}
		return success;
	}

	// 학과 테이블에 데이터 삭제 메서드
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
			System.out.println("쿼리 error = [ " + se + " ]");
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
				System.out.println("디비 연동 해제 error = [ " + e + " ]");
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
			System.out.println("쿼리 error = [ " + se + " ]");
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
				System.out.println("DB 연동 해제 error = [ " + e + " ]");
			}
		}
		return list;
	}

	// 학과 테이블에서 모든 레코드를 반환 매서드
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

			// ResultSet의 결과에서 모든 행을 각각의 SubjectVO 객체에 저장
			while (rs.next()) {
				// 한 행의 학과 정보를 저장할 VO객체 생성
				svo = new SubjectVO();
				// 한 행의 학과 정보를 VO객체에 저장
				svo.setNo(rs.getInt("no"));
				svo.setS_num(rs.getString("s_num"));
				svo.setS_name(rs.getString("s_name"));

				// ArrayList 객체에 원소로 추가
				list.add(svo);
			}
		} catch (SQLException se) {
			System.out.println("쿼리 error = [ " + se + " ]");
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
				System.out.println("DB 연동 해제 error = [ " + e + " ]");
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
			System.out.println("쿼리 getSubjectNum error = [ " + se + " ]");
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
				System.out.println("DB 연동 해제 error = [ " + e + " ]");
			}
		}
		return subjectNumber;
	}
}
