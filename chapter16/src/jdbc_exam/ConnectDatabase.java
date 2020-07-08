package jdbc_exam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDatabase {
	public static Connection makeConnection(String id, String password) {
		// 11g : jdbc:oracle:thin:@127.0.0.1:1521:orcl
		// ������ġ :��Ʈ��ȣ:SID
		// 12c : jdbc:oracle:thin:@127.0.0.1:1521/pdborcl
		// ������ġ:��Ʈ��ȣ/�����̸�
		
		String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
		Connection con = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
//			System.out.println("����̹� ���� ����");

			con = DriverManager.getConnection(url, id, password);
//			System.out.println("�����ͺ��̽� ���� ����");
		} catch (ClassNotFoundException e) {
			System.out.println("����̹��� ã�� �� �����ϴ�.");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("���ῡ �����Ͽ����ϴ�.");
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
		}catch (SQLException s) {//������
			System.err.println("������ [ERROR] \n" + s.getMessage());
		}finally {//������ ���� ����
			if (con != null) con.close();
		}
	}
}