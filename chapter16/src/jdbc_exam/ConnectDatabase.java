package jdbc_exam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDatabase {
	public static Connection makeConnection(String id, String password) {
		// 11g : jdbc:oracle:thin:@127.0.0.1:1521:orcl
		// 서버위치 :포트번호:SID
		// 12c : jdbc:oracle:thin:@127.0.0.1:1521/pdborcl
		// 서버위치:포트번호/서비스이름
		
		String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
		Connection con = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
//			System.out.println("드라이버 적재 성공");

			con = DriverManager.getConnection(url, id, password);
//			System.out.println("데이터베이스 연결 성공");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버를 찾을 수 없습니다.");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("연결에 실패하였습니다.");
			e.printStackTrace();
		}
		return con;
	}

	public static void main(String[] args) throws SQLException {
		Connection con = makeConnection("scott", "tiger");
//		Statement s = con.createStatement();
//		String select = "select empno, ename from emp order by empno";
//		ResultSet rs = s.executeQuery(select);
		
		Statement stmt = con.createStatement();
		StringBuffer sb = new StringBuffer();
		sb.append("select empno, ename, sal, to_char(hiredate, \'YYYY/MM/DD\') \"hiredate\" from emp");
		ResultSet rs = stmt.executeQuery(sb.toString());
		
		System.out.println("empno\tename\tsal\tsysdate");
		System.out.println("----------------------------------------");
		try {
			while (rs.next()) {
			int empno = rs.getInt("empno");
			String ename = rs.getString("ename");
			int sal = rs.getInt("sal");
			String sysdate = rs.getString("hiredate");
	
			System.out.println(empno + "\t" + ename + "\t" + sal + "\t" + sysdate);
			}
		}catch (SQLException s) {//오류시
			System.err.println("쿼리문 [ERROR] \n" + s.getMessage());
		}finally {//오류와 관계 없이
			if (con != null) con.close();
		}
	}
}