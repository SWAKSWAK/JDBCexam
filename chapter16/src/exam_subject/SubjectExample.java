package exam_subject;

import java.util.ArrayList;
import java.util.Scanner;

public class SubjectExample {
	private static Scanner input = new Scanner(System.in);

	public static void showMenu() {
		System.out.println("==========================");
		System.out.println("선택하세요");
		System.out.println("1. 데이터 조회");
		System.out.println("2. 데이터 입력");
		System.out.println("3. 데이터 수정");
		System.out.println("4. 데이터 삭제");
		System.out.println("5. 데이터 검색(학과명)");
		System.out.println("6. 프로그램 종료");
		System.out.print("선택>>");
	}

	public static void main(String[] args) {
		SubjectDAO dao = SubjectDAO.getInstance();
		int menuChoice;

		while (true) {
			showMenu();
			menuChoice = input.nextInt();
			input.nextLine();

			switch (menuChoice) {
			case 1:
				read(dao);
				break;
			case 2:
				create(dao);
				break;
			case 3:
				update(dao);
				break;
			case 4:
				delete(dao);
				break;
			case 5:
				search(dao);
				break;
			case 6:
				System.out.println("프로그램을 종료합니다.");
				System.exit(0);
				break;
			}
		}
	}

	private static void read(SubjectDAO dao) {
		try {
			ArrayList<SubjectVO> svo = dao.getSubjectTotal();
			System.out.println("\n**** subject 테이블 데이터 출력 ****");
			System.out.println("번호\t학과번호\t학과명");
			if (svo.size() > 0) {
				for (SubjectVO sub : svo) {
					System.out.print(sub.getNo() + "\t");
					System.out.print(sub.getS_num() + "\t");
					System.out.println(sub.getS_name() + "\t");
				}
			} else
				System.out.println("학과 정보가 존재하지 않습니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static SubjectVO inputData(String mode, SubjectDAO dao) throws Exception {
		String s_num = null, s_name = null;
//		if (!mode.equals("search")) {
//			System.out.print("학과코드 입력 : ");
//			s_num = input.nextLine();
//		}
//		System.out.println("학과명 입력 : ");
//		s_name = input.nextLine();
//		SubjectVO sub = new SubjectVO(0, s_num, s_name);
//		// 생성자 NO 에 0을 주는건 중요하지 않다. 어차피 시퀀스 되어있으므로 저곳에 입력하는 값은 무관함
//		return sub;
		
		
		switch (mode){
			case "input":
				System.out.print("학과코드 입력 : ");
				s_num = dao.getSubjectNum();
				System.out.println(s_num);
				break;
			case "update":
				System.out.println("학과코드 입력");
				s_num = input.nextLine();
				break;
		}
		System.out.println("학과명 입력 : ");
		s_name = input.nextLine();
		SubjectVO sub = new SubjectVO(0, s_num, s_name);
		return sub;
		
	}

	private static int inputDataNo() {
		int no;
		System.out.println("번호 입력 : ");
		no = input.nextInt();
		return no;
	}

	private static void create(SubjectDAO dao) {
		try {
			SubjectVO svo = inputData("input", dao);
			boolean result = dao.subjectInsert(svo);
			if (result)
				System.out.println("학과 데이터 입력 성공.");
			else
				System.out.println("학과 데이터 입력 실패");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void update(SubjectDAO dao) {
		try {
			System.out.println("<수정할 내용>");
			SubjectVO svo = inputData("update", dao);
			System.out.print("수정될 ");
			int no = inputDataNo();
			svo.setNo(no);
			boolean result = dao.subjectUpdate(svo);
			if (result)
				System.out.println("학과 데이터 수정 성공.");
			else
				System.out.println("학과 데이터 입력 실패");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 다른 테이블과 관계가 형성되어있는 컬럼의 레코드(참조키)는 지울 수 없다.
	private static void delete(SubjectDAO dao) {
		try {
			SubjectVO svo = new SubjectVO();
			int no = inputDataNo();
			svo.setNo(no);
			boolean result = dao.subjectDelete(svo);
			if (result)
				System.out.println("학과 데이터 삭제 성공.");
			else
				System.out.println("학과 데이터 삭제 실패");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void search(SubjectDAO dao) {

		try {
//			System.out.print("검색학 학과명 입력>>");
//			String s_name = input.nextLine();
			System.out.print("검색할 ");
			SubjectVO svo = inputData("search", dao);
			System.out.println("검색 단어:"+svo.getS_name());
			ArrayList<SubjectVO> list = dao.getSubjectSearch(svo.getS_name());
			if (list.size() > 0) {
				System.out.println();
				System.out.println("**** 검색 결과 ****");
				System.out.println("번호\t학과번호\t학과명");
				for (SubjectVO vo : list) {
					System.out.print(vo.getNo() + "\t");
					System.out.print(vo.getS_num() + "\t");
					System.out.println(vo.getS_name() + "\t");
				}
			} else
				System.out.println("학과 정보가 존재하지 않습니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
