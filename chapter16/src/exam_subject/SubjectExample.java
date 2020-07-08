package exam_subject;

import java.util.ArrayList;
import java.util.Scanner;

public class SubjectExample {
	private static Scanner input = new Scanner(System.in);

	public static void showMenu() {
		System.out.println("==========================");
		System.out.println("�����ϼ���");
		System.out.println("1. ������ ��ȸ");
		System.out.println("2. ������ �Է�");
		System.out.println("3. ������ ����");
		System.out.println("4. ������ ����");
		System.out.println("5. ������ �˻�(�а���)");
		System.out.println("6. ���α׷� ����");
		System.out.print("����>>");
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
				System.out.println("���α׷��� �����մϴ�.");
				System.exit(0);
				break;
			}
		}
	}

	private static void read(SubjectDAO dao) {
		try {
			ArrayList<SubjectVO> svo = dao.getSubjectTotal();
			System.out.println("\n**** subject ���̺� ������ ��� ****");
			System.out.println("��ȣ\t�а���ȣ\t�а���");
			if (svo.size() > 0) {
				for (SubjectVO sub : svo) {
					System.out.print(sub.getNo() + "\t");
					System.out.print(sub.getS_num() + "\t");
					System.out.println(sub.getS_name() + "\t");
				}
			} else
				System.out.println("�а� ������ �������� �ʽ��ϴ�.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static SubjectVO inputData(String mode, SubjectDAO dao) throws Exception {
		String s_num = null, s_name = null;
//		if (!mode.equals("search")) {
//			System.out.print("�а��ڵ� �Է� : ");
//			s_num = input.nextLine();
//		}
//		System.out.println("�а��� �Է� : ");
//		s_name = input.nextLine();
//		SubjectVO sub = new SubjectVO(0, s_num, s_name);
//		// ������ NO �� 0�� �ִ°� �߿����� �ʴ�. ������ ������ �Ǿ������Ƿ� ������ �Է��ϴ� ���� ������
//		return sub;
		
		
		switch (mode){
			case "input":
				System.out.print("�а��ڵ� �Է� : ");
				s_num = dao.getSubjectNum();
				System.out.println(s_num);
				break;
			case "update":
				System.out.println("�а��ڵ� �Է�");
				s_num = input.nextLine();
				break;
		}
		System.out.println("�а��� �Է� : ");
		s_name = input.nextLine();
		SubjectVO sub = new SubjectVO(0, s_num, s_name);
		return sub;
		
	}

	private static int inputDataNo() {
		int no;
		System.out.println("��ȣ �Է� : ");
		no = input.nextInt();
		return no;
	}

	private static void create(SubjectDAO dao) {
		try {
			SubjectVO svo = inputData("input", dao);
			boolean result = dao.subjectInsert(svo);
			if (result)
				System.out.println("�а� ������ �Է� ����.");
			else
				System.out.println("�а� ������ �Է� ����");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void update(SubjectDAO dao) {
		try {
			System.out.println("<������ ����>");
			SubjectVO svo = inputData("update", dao);
			System.out.print("������ ");
			int no = inputDataNo();
			svo.setNo(no);
			boolean result = dao.subjectUpdate(svo);
			if (result)
				System.out.println("�а� ������ ���� ����.");
			else
				System.out.println("�а� ������ �Է� ����");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �ٸ� ���̺�� ���谡 �����Ǿ��ִ� �÷��� ���ڵ�(����Ű)�� ���� �� ����.
	private static void delete(SubjectDAO dao) {
		try {
			SubjectVO svo = new SubjectVO();
			int no = inputDataNo();
			svo.setNo(no);
			boolean result = dao.subjectDelete(svo);
			if (result)
				System.out.println("�а� ������ ���� ����.");
			else
				System.out.println("�а� ������ ���� ����");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void search(SubjectDAO dao) {

		try {
//			System.out.print("�˻��� �а��� �Է�>>");
//			String s_name = input.nextLine();
			System.out.print("�˻��� ");
			SubjectVO svo = inputData("search", dao);
			System.out.println("�˻� �ܾ�:"+svo.getS_name());
			ArrayList<SubjectVO> list = dao.getSubjectSearch(svo.getS_name());
			if (list.size() > 0) {
				System.out.println();
				System.out.println("**** �˻� ��� ****");
				System.out.println("��ȣ\t�а���ȣ\t�а���");
				for (SubjectVO vo : list) {
					System.out.print(vo.getNo() + "\t");
					System.out.print(vo.getS_num() + "\t");
					System.out.println(vo.getS_name() + "\t");
				}
			} else
				System.out.println("�а� ������ �������� �ʽ��ϴ�.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
