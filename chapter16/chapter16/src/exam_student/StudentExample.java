package exam_student;

/*입력예제
홍길동
hhhh
h001
02
900702
01000000001
홍홍홍
h@h.h
 */

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class StudentExample {
	private static Scanner sc = new Scanner(System.in);

	// 메뉴 출력 메소드
	public static void showMenu() {
		System.out.println("=========================================================================="
				+ "=======================================================================");
		System.out.println("선택하세요");
		System.out.println("1. 데이터 조회");
		System.out.println("2. 데이터 입력");
		System.out.println("3. 데이터 삭제");
		System.out.println("4. 데이터 검색");
		System.out.println("5. 데이터 수정");
		System.out.println("6. 프로그램 종료");
		System.out.print("선택>>");
	}

	// 메인 메소드. 번호 선택에 따른 메소드 호출, 종료
	public static void main(String[] args) {
		StudentDAO dao = StudentDAO.getInstance();
		int menuChoice;

		while (true) {
			showMenu();
			menuChoice = sc.nextInt();
			sc.nextLine();

			switch (menuChoice) {
			case 1:
				read(dao);// 조회
				break;
			case 2:
				create(dao);// 추가
				break;
			case 3:
				delete(dao);// 삭제
				break;
			case 4:
				search(dao);// 검색
				break;
			case 5:
				updateData(dao);
				break;
			case 6:
				System.out.println("프로그램을 종료합니다.");
				System.exit(0);
				break;
			}
		}
	}

	// 추가 시킬 데이터를 입력받는 메소드
	private static StudentVO inputData(String mode, StudentDAO dao) throws Exception {
		// 스캐너로 입력받을 변수
		String sd_num = null, sd_name = null, sd_id = null, sd_passwd = null, s_num = null, sd_phone = null,
				sd_address = null, sd_email = null;

		// 임시로 String에 생년월일을 6자리로 입력받아 java.sql.Date 형으로 변환
		String birth = null;

		// Date
		String year, month, date;

		// java.sql.Date
		Date sd_birth = null;

		System.out.print("학생명:");
		sd_name = sc.nextLine().trim();
		System.out.print("학생id:");
		sd_id = sc.nextLine().trim();
		System.out.print("학생password:");
		sd_passwd = sc.nextLine().trim();
		System.out.print("학과번호:");
		s_num = sc.nextLine().trim();

		System.out.print("생년월일(6자리):");// yymmdd
		birth = sc.nextLine().trim();

		if (birth.substring(0, 1).equals("0"))// 0으로 시작하면 2000년대생
			year = 20 + birth.substring(0, 2);// date타입에 삽입할 형식 yyyy
		else
			year = 19 + birth.substring(0, 2);
		month = birth.substring(2, 4);// mm
		date = birth.substring(4);// dd
		birth = year + "-" + month + "-" + date;// yy-mm-dd
		sd_birth = Date.valueOf(birth);

		System.out.print("전화번호:");
		sd_phone = sc.nextLine().trim();
		System.out.print("주소:");
		sd_address = sc.nextLine().trim();
		System.out.print("e-mail:");
		sd_email = sc.nextLine().trim();

		sd_num = dao.studentNum(s_num);// 학생번호 생성 메소드 호출
		System.out.println("학생번호는 [" + sd_num + "]으로 생성 되었습니다.");

		StudentVO svo = new StudentVO(0, sd_num, sd_name, sd_id, sd_passwd, s_num, sd_birth, sd_phone, sd_address,
				sd_email);
		return svo;
	}

	// 조회 메소드
	private static void read(StudentDAO dao) {
		try {
			// ArrrayList에 담아서 for-each 문으로 출력
			ArrayList<StudentVO> svo = dao.getStudentTotal();

			System.out.println("\n=================================================================STUDENT 데이터 출력"
					+ "=================================================================");
			System.out.println("번호\t학번\t\t이름\tid\t\t비밀번호\t학과번호\t학과명\t\t생일\t\t전화번호\t\t주소\t\t\t메일\t\t\t등록일자");
			if (svo.size() > 0) {
				for (StudentVO vo : svo) {
					System.out.printf("%-2s\t", vo.getNo());
					System.out.printf("%-8s\t", vo.getSd_num());
					System.out.printf("%-2s\t", vo.getSd_name());
					System.out.printf("%-12s\t", vo.getSd_id());
					System.out.printf("%-7s\t", vo.getSd_passwd());
					System.out.printf("%s\t", vo.getS_num());
					System.out.printf("%-10s\t", vo.getS_name());
					System.out.printf("%-7s\t", vo.getSd_birth());
					System.out.printf("%-10s\t", vo.getSd_phone());
					if (vo.getSd_address().length() <= 4)
						System.out.printf("%-30s\t", vo.getSd_address());
					else
						System.out.printf("%-20s\t", vo.getSd_address());

					System.out.printf("%-20s\t", vo.getSd_email());
					System.out.printf("%s\n", vo.getSd_date());
				}
			} else// 원소가 0일 경우
				System.out.println("학생 정보가 존재하지 않습니다.");
		} catch (Exception e) {// 예외시 catch
			e.printStackTrace();
		}
	}

	// 데이터 생성 메소드
	private static void create(StudentDAO dao) {
		try {

			// 정보를 입력받는 inputdata 호출
			StudentVO svo = inputData("create", dao);

			// 쿼리문 실행 메소드 호출, boolean 리턴
			boolean result = dao.studentInsert(svo);
			if (result)// true 성공
				System.out.println("학생정보 입력 성공.");
			else// false 실패
				System.out.println("학생정보 입력 실패");
		} catch (Exception e) {// 예외시 catch
			e.printStackTrace();
		}
	}

	// 삭제시 번호 입력받는 메소드
	private static int inputDataNo() {
		int no;
		System.out.print("번호 입력>>");
		no = sc.nextInt();
		return no;
	}

	// 삭제 메소드
	private static void delete(StudentDAO dao) {
		try {
			// svo.no 필드에 삭제할 no를 담아 보낸다.
			StudentVO svo = new StudentVO();
			// 삭제할 번호 입력받는 메소드 호출
			int no = inputDataNo();
			svo.setNo(no);
			// 삭제 쿼리문 실행 메소드 호출, 인자로 svo 보냄
			boolean result = dao.studentDelete(svo);
			if (result)// 삭제성공 true
				System.out.println("학생 정보 삭제 성공.");
			else// 삭제 실패 false
				System.out.println("학생 정보 삭제 실패.");
		} catch (Exception e) {// 예외시 catch
			e.printStackTrace();
		}
	}

	private static String searchData() {
		System.out.print("검색 대상(학생명, 아이디, 등록일) 입력:");
		String search = sc.nextLine();
		String word = null;
		if (search.equalsIgnoreCase("등록일")) {
			System.out.print("검색 단어 입력(YYYY-MM-DD):");
			word = sc.nextLine();
		} else {
			System.out.print("검색 단어 입력:");
			word = sc.nextLine();
		}

		String data = search + "," + word;
		return data;
	}

	// 검색 메소드
	private static void search(StudentDAO dao) {
		try {
			String[] result = searchData().split(",");
			ArrayList<StudentVO> list = dao.getStudentSearch(result[0], result[1]);

			System.out.println("\n=================================================================STUDENT 데이터 출력"
					+ "=================================================================");
			System.out.println("번호\t학번\t\t이름\tid\t\t비밀번호\t학과번호\t학과명\t\t생일\t\t전화번호\t\t주소\t\t\t메일\t\t\t등록일자");
			if (list.size() > 0) {
				for (StudentVO vo : list) {
					System.out.printf("%-2s\t", vo.getNo());
					System.out.printf("%-8s\t", vo.getSd_num());
					System.out.printf("%-2s\t", vo.getSd_name());
					System.out.printf("%-12s\t", vo.getSd_id());
					System.out.printf("%-7s\t", vo.getSd_passwd());
					System.out.printf("%s\t", vo.getS_num());
					System.out.printf("%-10s\t", vo.getS_name());
					System.out.printf("%-7s\t", vo.getSd_birth());
					System.out.printf("%-10s\t", vo.getSd_phone());
					if (vo.getSd_address().length() <= 4)
						System.out.printf("%-30s\t", vo.getSd_address());
					else
						System.out.printf("%-20s\t", vo.getSd_address());

					System.out.printf("%-20s\t", vo.getSd_email());
					System.out.printf("%s\n", vo.getSd_date());
				}
			} else {
				System.out.println("검색된 학생 데이터가 존재하지 않습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//학생정보 수정 메소드. 수정하지 않을 값은 Enter 키로 skip 한다.
	private static void updateData(StudentDAO dao) {
		String sd_num = "";
		
		//HashMap에 <컬럼명, 값> 을 담아 dao.studentUpdate 메소드로 보낸다.
		HashMap<String, String> hm = new HashMap<String, String>();
		try {
			System.out.print("수정할 학생");
			sd_num = inputDataNo() + "";
			
			//입력하지 않으면 값이 "" 가 되기 때문에
			System.out.println("수정하지 않을 정보는 [Enter]키로 넘겨 주세요.");
			System.out.print("이름:");
			hm.put("sd_name", sc.nextLine() + sc.nextLine());
			System.out.print("id:");
			hm.put("sd_id", sc.nextLine());
			System.out.print("password:");
			hm.put("sd_passwd", sc.nextLine());
			System.out.print("학과번호:");
			hm.put("s_num", sc.nextLine());

			System.out.print("생년월일(yymmdd):");
			hm.put("sd_birth", sc.nextLine());

			System.out.print("전화번호:");
			hm.put("sd_phone", sc.nextLine());
			System.out.print("주소:");
			hm.put("sd_address", sc.nextLine());
			System.out.print("e-mail:");
			hm.put("sd_email", sc.nextLine());
			
			//value = "" 인 값을 제거하기 위한 작업
			//keyset으로 set에 key값 가져온다.
			Set <String> keys = hm.keySet();
			
			//while(itr.hasnext()) 안에서 삭제를 하면 에러가 나기 때문에 for문을 돌리기 위해 key를 ArrayList로 잠시 옮겨줌
			ArrayList<String> al = new ArrayList<String>();
			Iterator<String> itr = keys.iterator();
			while (itr.hasNext()) 
				al.add(itr.next());//Set -> ArrayList
			
			//key 값이 저장 되어있는 ArrayList 사이즈 만큼 for문 돌림
			for (int i = 0 ; i < al.size() ; i++) {
				if (hm.get(al.get(i)).equals(""))//만약 value가 "" 이라면
					hm.remove(al.get(i));//삭제
			}
			//이제 hm 에는 value가 빈 값이 없다.

			boolean result = dao.studentUpdate(hm, sd_num);
			if (result)
				System.out.println("학생정보 수정 성공");
			else
				System.out.println("학생정보 수정 실패");
		} catch (Exception e) {
			System.out.println("error = [" + e + "]");
		}

	}
}
