package exam_student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class StudentDAO {
	// 데이터베이스 연결 관련 상수 선언
	private static final String JDBC_URL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";// 서버 url
	private static final String USER = "javauser";// 사용자
	private static final String PASSWD = "java1234";// 비밀번호

	private static StudentDAO instance = null;// 자신의 타입으로 생성할 예정

	public static StudentDAO getInstance() {
		if (instance == null)// 하나만 선언되어야 하므로 있을경우 바로 instance 리턴
			instance = new StudentDAO();
		return instance;
	}

	private StudentDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");// jdbc 드라이버
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}

	private Connection getConnection() throws SQLException {
		Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWD);// 오라클 서버 연결
		return con;
	}

	// 리스트 조회
	public ArrayList<StudentVO> getStudentTotal() {

		// 값을 추가할수 있기 때문에 StringBuffer
		StringBuffer sql = new StringBuffer();

		// innerjoin 이용해 학과명까지 출력
		sql.append("select st.no, sd_num, sd_name, sd_id, sd_passwd, st.s_num s_num, ");
		sql.append("su.s_name as s_name, sd_birth, sd_phone, sd_address, sd_email, sd_date ");
		sql.append("from student st inner join subject su on st.s_num = su.s_num order by no");

		// try문 안에서 하면 try 문에서만 쓸수 있기 때문에 try문 밖에서 선언
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StudentVO svo = null;

		// ArayList 에 담아 while (rs.next()) 를 통해 값을 담을 예정
		ArrayList<StudentVO> list = new ArrayList<StudentVO>();

		try {

			// try문 밖에서 선언했던 객체들 생성
			con = getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			// 다음 값이 없을때 까지 반복
			while (rs.next()) {
				svo = new StudentVO();

				// StudentVO의 set에 ResultSet의 get을 담는다는 뜻
				svo.setNo(rs.getInt("no"));
				svo.setSd_num(rs.getString("sd_num"));
				svo.setSd_name(rs.getString("sd_name"));
				svo.setSd_id(rs.getString("sd_id"));
				svo.setSd_passwd(rs.getString("sd_passwd"));
				svo.setS_num(rs.getString("s_num"));
				svo.setS_name(rs.getString("s_name"));
				svo.setSd_birth(rs.getDate("sd_birth"));
				svo.setSd_phone(rs.getString("sd_phone"));
				svo.setSd_address(rs.getString("sd_address"));
				svo.setSd_email(rs.getString("sd_email"));
				svo.setSd_date(rs.getDate("sd_date"));

				// 다 받았으면 ArrayList 원소에 추가
				list.add(svo);
			}
		} catch (SQLException se) {// SQLException문이 나올경우 catch
			System.out.println("쿼리 error = [" + se + "]");
			se.printStackTrace();
		} catch (Exception e) {// Exception문이 나올경우 catch
			System.out.println("error = [" + e + "]");
		} finally {// 결과와 상관없이 실행하는 finally
			try {
				// null이면 에러가 나기 때문, 그리고 null이면 이미 객체가 닫혔다는 뜻
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {// close() 에도 예외가 날 수 있다.
				System.out.println("DB 연동 해제 error = [" + e + "]");
			}
		}
		return list;
	}

	// 학생정보 추가
	public boolean studentInsert(StudentVO svo) throws Exception {

		// 값을 추가할수 있기 때문에 StringBuffer
		StringBuffer sql = new StringBuffer();
		sql.append("insert into student (no, sd_num, sd_name, sd_id, sd_passwd, "
				+ "s_num, sd_birth, sd_phone, sd_address, sd_email) "
				+ "values (student_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		// try문 안에서 하면 try 문에서만 쓸수 있기 때문에 try문 밖에서 선언
		Connection con = null;
		PreparedStatement pstmt = null;

		// boolean 값을 리턴하는 메소드다. 값이 추가되었으면 true
		boolean success = false;

		try {
			// try문 밖에 선언한 객체 생성
			con = getConnection();
			// StringBuffer 로 만든 sql 문을 pstmt 생성자에 보낸다. 여기서 '?' 가 작동될 값은 밑에 나와 있다.
			pstmt = con.prepareStatement(sql.toString());

			// StudentExample.create(dao) 메소드에서 받은 값을 pstmt의 set으로 설정
			// 설정된 값으로 쿼리문을 보낼거다.
			pstmt.setString(1, svo.getSd_num());
			pstmt.setString(2, svo.getSd_name());
			pstmt.setString(3, svo.getSd_id());
			pstmt.setString(4, svo.getSd_passwd());
			pstmt.setString(5, svo.getS_num());
			pstmt.setDate(6, svo.getSd_birth());
			pstmt.setString(7, svo.getSd_phone());
			pstmt.setString(8, svo.getSd_address());
			pstmt.setString(9, svo.getSd_email());

			// 쿼리문을 보내는 메소드 성공하면 1을 리턴해준다. 그리고 true
			int i = pstmt.executeUpdate();
			if (i == 1)
				success = true;
		} catch (SQLException se) {// SQLException문이 나올경우 catch, false
			System.out.println("쿼리 error = [" + se + "]");
			success = false;
		} catch (Exception e) {// Exception문이 나올경우 catch, false
			System.out.println("error = [" + e + "]");
			success = false;
		} finally {// 결과의 여부와 상관없는 finally
			try {// close() 마저 예외가 발생할 수 있다.
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println("DB 연동 해제 error = [" + e + "]");
			}
		}
		return success;
	}

	// 학생 정보 삭제
	public boolean studentDelete(StudentVO svo) {

		// 값을 추가할수 있기 때문에 StringBuffer
		StringBuffer sql = new StringBuffer();
		sql.append("delete from student where no = ?");

		// try문 안에서 하면 try 문에서만 쓸수 있기 때문에 try문 밖에서 선언
		Connection con = null;
		PreparedStatement pstmt = null;
		boolean success = false;

		try {

			// try문 밖에 선언한 객체 생성
			con = getConnection();
			// StringBuffer 로 만든 sql 문을 pstmt 생성자에 보낸다. 여기서 '?' 가 작동될 값은 밑에 나와 있다.
			pstmt = con.prepareStatement(sql.toString());

			// StudentExample.delete(dao) 메소드에서 받은 값을 pstmt의 set으로 설정
			// 설정된 값으로 쿼리문을 보낼거다.
			pstmt.setInt(1, svo.getNo());

			// 쿼리문 보내기, 성공하면 1 리턴
			int i = pstmt.executeUpdate();
			if (i == 1)
				success = true;
		} catch (SQLException se) {// SQLException문이 나올경우 catch, false
			System.out.println("쿼리 error = [" + se + "]");
			success = false;
		} catch (Exception e) {// Exception문이 나올경우 catch, false
			System.out.println("error = [" + e + "]");
			success = false;
		} finally {// 결과의 여부와 상관없는 finally
			try {// close() 마저 예외가 발생할 수 있다.
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {// SQLException문이 나올경우 catch
				System.out.println("DB 연동 해제 error = [" + e + "]");
			}
		}
		return success;
	}

	// 학생번호 자동생성 메소드 (등록년도(sysdate)+학과번호+0001) 부터 시작해 일치하는 값이 나오면 ++ 한다.
	public String studentNum(String s_num) {
		// ArrayList 에 임시로 학번을 담는다
		ArrayList<String> al = new ArrayList<String>();

		// 값을 추가할수 있기 때문에 StringBuffer
		StringBuffer sql = new StringBuffer();

		SimpleDateFormat sdf = new SimpleDateFormat("yy");
		String year = sdf.format(new java.util.Date());
		sql.append("select sd_num from student where substr(sd_num,1,2)=" + year + " and s_num= " + s_num);

		// try문 안에서 하면 try 문에서만 쓸수 있기 때문에 try문 밖에서 선언
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		// 기본값
		String studentNumber = "";
		try {

			// try문 밖에 선언한 객체 생성
			con = getConnection();
			stmt = con.createStatement();
			// StringBuffer 로 만든 sql 문을 stmt 쿼리문 전송 메소드에 보낸다.
			rs = stmt.executeQuery(sql.toString());
			studentNumber = year + s_num + "0001";
			int stn = Integer.parseInt(studentNumber);// int 로 형변환
			while (rs.next())
				al.add(rs.getString("sd_num"));

			Collections.sort(al);
			for (int i = 0; i < al.size(); i++) {
				if (al.get(i).equals(studentNumber)) {
					stn++;
				}
				studentNumber = stn + "";
				if (studentNumber.length() != 8) {
					while (studentNumber.length() != 8)
						studentNumber = "0" + studentNumber;
				}
			}

		} catch (SQLException se) {// SQLException문이 나올경우 catch
			System.out.println("쿼리 error = [" + se + "]");
		} catch (Exception e) {// Exception문이 나올경우 catch
			System.out.println("error = [" + e + "]");
		} finally {// 결과의 여부와 상관없는 finally
			try {// close() 마저 예외가 발생할 수 있다.
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();

			} catch (SQLException e) {// SQLException문이 나올경우 catch
				System.out.println("DB 연동 해제 error = [" + e + "]");
			}
		}
		return studentNumber;
	}

	public ArrayList<StudentVO> getStudentSearch(String searchObject, String searchWord) throws Exception {
		ArrayList<StudentVO> list = new ArrayList<StudentVO>();

		System.out.println("searchObject : " + searchObject);
		System.out.println("/ searchWord : " + searchWord);

		// 값을 추가할수 있기 때문에 StringBuffer
		StringBuffer sql = new StringBuffer();

		// innerjoin 이용해 학과명까지 출력
		sql.append("select st.no, sd_num, sd_name, sd_id, sd_passwd, st.s_num s_num, ");
		sql.append("su.s_name as s_name, sd_birth, sd_phone, sd_address, sd_email, sd_date ");
		sql.append("from student st inner join subject su on st.s_num = su.s_num ");

		// equalsIgnoreCase 는 영문의 대소문자와 상관없이 일치하면 true, 밑의 값은 한글이지만 알아가고자 사용함
		if (searchObject.equalsIgnoreCase("학생명"))
			sql.append("where sd_name like ? ");
		else if (searchObject.equalsIgnoreCase("아이디"))
			sql.append("where sd_id like ? ");
		else if (searchObject.equalsIgnoreCase("등록일"))
			sql.append("where to_char(sd_date, 'YYYY-MM-DD') like ? ");

		sql.append("order by no");

		// try문 안에서 하면 try 문에서만 쓸수 있기 때문에 try문 밖에서 선언
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StudentVO svo = null;

		try {

			// try문 밖에서 선언했던 객체들 생성
			con = getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, "%" + searchWord + "%");

			rs = pstmt.executeQuery();

			// 다음 값이 없을때 까지 반복
			while (rs.next()) {
				svo = new StudentVO();

				// StudentVO의 set에 ResultSet의 get을 담는다는 뜻
				svo.setNo(rs.getInt("no"));
				svo.setSd_num(rs.getString("sd_num"));
				svo.setSd_name(rs.getString("sd_name"));
				svo.setSd_id(rs.getString("sd_id"));
				svo.setSd_passwd(rs.getString("sd_passwd"));
				svo.setS_num(rs.getString("s_num"));
				svo.setS_name(rs.getString("s_name"));
				svo.setSd_birth(rs.getDate("sd_birth"));
				svo.setSd_phone(rs.getString("sd_phone"));
				svo.setSd_address(rs.getString("sd_address"));
				svo.setSd_email(rs.getString("sd_email"));
				svo.setSd_date(rs.getDate("sd_date"));

				// 다 받았으면 ArrayList 원소에 추가
				list.add(svo);
			}
		} catch (SQLException se) {// SQLException문이 나올경우 catch
			System.out.println("쿼리 error = [" + se + "]");
			se.printStackTrace();
		} catch (Exception e) {// Exception문이 나올경우 catch
			System.out.println("error = [" + e + "]");
		} finally {// 결과와 상관없이 실행하는 finally
			try {
				// null이면 에러가 나기 때문, 그리고 null이면 이미 객체가 닫혔다는 뜻
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {// close() 에도 예외가 날 수 있다.
				System.out.println("DB 연동 해제 error = [" + e + "]");
			}
		}
		return list;
	}

	public boolean studentUpdate(HashMap<String, String> hm, String sd_num) {
		StringBuffer sql = new StringBuffer();
		sql.append("update student set ");
		// 수정 여부
		boolean success = false;

		// StringBuffer 를 이용해 sql문 추가. 수정한 개수 만큼 while문이 돈다.
		Set<String> keys = hm.keySet();
		Iterator<String> itr = keys.iterator();
		while (itr.hasNext()) {
			String key = itr.next();// hm 의 key값
			String value = hm.get(key);// hm 의 value 값

			if (key.equals("sd_birth")) {// sd_birth 는 Date 타입 이므로 특별히 처리 해야함
				String year, month, date;
				if (value.substring(0, 1).equals("0"))// yymmdd의 yy를 2000년대 생과 1900년대생 나누기
					year = "20" + value.substring(0, 2);
				else
					year = "19" + value.substring(0, 2);
				month = value.substring(2, 4);// yymmdd -> mm
				date = value.substring(4);// yymmdd -> dd
				value = year + "-" + month + "-" + date;// yyyy-mm-dd
			}

			sql.append(key + " = '" + value + "' ");// <예> sd_id = 'SWAK'

			if (itr.hasNext())// 다음값이 있으면 , 출력
				sql.append(", ");
		}
		sql.append("where sd_num = " + sd_num);// 마지막 where 조건
//		System.out.println(sql.toString());//쿼리문 잘 작성 되었는지 확인용
		Connection con = null;
		Statement stmt = null;
		try {
			con = getConnection();
			stmt = con.createStatement();
			int i = stmt.executeUpdate(sql.toString());
			if (i > 0)
				success = true;
		} catch (SQLException se) {// SQLException문이 나올경우 catch, false
			System.out.println("쿼리 error = [" + se + "]");
			success = false;
		} catch (Exception e) {// Exception문이 나올경우 catch, false
			System.out.println("error = [" + e + "]");
			success = false;
		} finally {// 결과의 여부와 상관없는 finally
			try {// close() 마저 예외가 발생할 수 있다.
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println("DB 연동 해제 error = [" + e + "]");
			}
		}
		return success;

	}

}
