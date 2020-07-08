package jdbc_exam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLInsertTest {
	public static void main(String[] args) {
//		addBook("�޼�", "�޼޺�", "2020", 50000);
		readBookAll();
//		deleteBook(9);
//		readBookAll();
	}

	private static void addBook(String title, String publisher, String year, int price) {
		Connection con = ConnectDatabase.makeConnection("javauser", "java1234");
//		Statement stmt = null;
		PreparedStatement pstmt = null;
		try {
			//Statement
			/*stmt = con.createStatement();

			StringBuffer sb = new StringBuffer();
			sb.append("INSERT INTO books (book_id, title, publisher, ");
			sb.append("year, price) VALUES (books_seq.nextval, ");
			sb.append("'" + title + "','" + publisher + "','");
			sb.append(year + "'," + price + ")");

			System.out.println("������ Ȯ�� : " + sb);

			// sb�� StringBuffer �̱� ������ sb�� ���� ������. toString ���� String ���� ��ȯ �ؾ���
			int insertCount = stmt.executeUpdate(sb.toString());*/
			
			
			//PreparedStatement   ������ �ĺ���
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT INTO books (book_id, title, publisher, ");
			sb.append("year, price) VALUES (books_seq.nextval,?,?,?,?)");

			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, title);
			pstmt.setString(2, publisher);
			pstmt.setString(3, year);
			pstmt.setInt(4, price);
			
			int insertCount = pstmt.executeUpdate();
			
			if (insertCount == 1)
				System.out.println("���ڵ� �߰� ����");
			else
				System.out.println("���ڵ� �߰� ����");

		} catch (SQLException e) {
			// System.out.println (e.getMessage());
			e.printStackTrace();
			System.exit(0);
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private static void readBookAll() {
		Connection con = ConnectDatabase.makeConnection("javauser", "java1234");
		Statement stmt = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select book_id, title, publisher, year, price ");
			sb.append(" from books order by book_id");

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sb.toString());
			System.out.println("\n**** books ���̺� ������ ��� ****");
			System.out.println("å��ȣ / ���� / �����ǻ� / ���ǿ��� / ����\n");

			while (rs.next()) {
				System.out.print(rs.getInt("book_id") + " / ");
				System.out.print(rs.getString("title") + " / ");
				System.out.print(rs.getString("publisher") + " / ");
				System.out.print(rs.getString("year") + " / ");
				System.out.println(rs.getInt("price") + " / ");
			}
		} catch (SQLException e) {
			// System.out.println (e.getMessage());
			e.printStackTrace();
			System.exit(0);
		} finally {
			try {
				if (stmt != null)//��ü�� �����Ǿ��ٸ� ����, ��ü�� �������� �ʾҴµ� close() �ϸ� ������
					stmt.close();
				if (con != null)
					con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private static void deleteBook(int bookID) {
		Connection con = ConnectDatabase.makeConnection("javauser", "java1234");
//		Statement stmt = null;
		PreparedStatement pstmt = null;
		
		try {
			/*//Statement
			StringBuffer sb = new StringBuffer();
			sb.append("delete from books where book_id=" + bookID);
			
			stmt = con.createStatement();
			int rs = stmt.executeUpdate(sb.toString());//����/�Է� �ϸ� �׸�ŭ ����� ���� ���� �����´�.*/
			
			//Preparestatement
			StringBuffer sb = new StringBuffer();
			sb.append ("delete from books where book_id=?");
			
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setInt(1, bookID);
			
			int deleteCount = pstmt.executeUpdate();
			
			if (deleteCount == 1)//�� �྿�� �۾��ϹǷ� ������ �����Ǹ� 1�� �����Ѵ�.
				System.out.println("���ڵ� ���� ����");
			else 
				System.out.println("���ڵ� ���� ����");
		}catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}