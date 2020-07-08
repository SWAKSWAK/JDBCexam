package jdbc_exam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLInsertTest {
	public static void main(String[] args) {
//		addBook("솩솩", "솩솩북", "2020", 50000);
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

			System.out.println("쿼리문 확힌 : " + sb);

			// sb는 StringBuffer 이기 때문에 sb만 쓰면 오류남. toString 으로 String 으로 변환 해야함
			int insertCount = stmt.executeUpdate(sb.toString());*/
			
			
			//PreparedStatement   선쿼리 후변수
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
				System.out.println("레코드 추가 성공");
			else
				System.out.println("레코드 추가 실패");

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
			System.out.println("\n**** books 테이블 데이터 출력 ****");
			System.out.println("책번호 / 제목 / 출출판사 / 출판연도 / 가격\n");

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
				if (stmt != null)//객체가 생성되었다면 실행, 객체가 생성되지 않았는데 close() 하면 에러남
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
			int rs = stmt.executeUpdate(sb.toString());//삭제/입력 하면 그만큼 실행된 행의 수를 가져온다.*/
			
			//Preparestatement
			StringBuffer sb = new StringBuffer();
			sb.append ("delete from books where book_id=?");
			
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setInt(1, bookID);
			
			int deleteCount = pstmt.executeUpdate();
			
			if (deleteCount == 1)//한 행씩만 작업하므로 삭제가 성공되면 1을 리턴한다.
				System.out.println("레코드 삭제 성공");
			else 
				System.out.println("레코드 삭제 실패");
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