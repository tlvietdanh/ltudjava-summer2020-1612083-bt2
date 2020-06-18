package org.example;

/**
 * Hello world!
 *
 */
import dao.*;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
        AccountsDao accountsDao = new AccountsDao();
        // login();
        String choose = "";
        while (choose != "exit") {


            System.out.printf("Chon: ");

            Scanner sc = new Scanner(System.in);

            choose = sc.nextLine();

            switch (choose) {
                case "0":
                    login();
                    break;
                case "1":
                    System.out.println("Them Sinh Vien");
                    addStudent();
                    break;
                case "2":
                    System.out.println("Import danh sach lop");
                    importClass();
                    break;
                case "3":
                    System.out.println("Import thoi khoa bieu");
                    importSchedules();
                    break;
                case "4":
                    System.out.println("Xin bo mon");
                    requestSpecial(-1);
                    break;
                case "5":
                    System.out.println("Xin hoc lai");
                    requestSpecial(1);
                    break;
                case "6":
                    ClassesDao s = new ClassesDao();
                    s.danhsachlop("17HCB");
                    break;
                case "7":
                    ClassesDao s1 = new ClassesDao();
                    s1.danhsachmon("18HCB", "CTT001");
                    break;
                case "8":
                    SchedulesDao.getSchedule("18HCB");
                    break;
                case "9":
                    ScoreDao score = new ScoreDao();
                    score.importTranscript("D:\\javaproject\\ltudjava-summer2020-1612083-bt2\\src\\bangdiem.csv", ",");
                    break;
                case "10":
                    ClassesDao s2 = new ClassesDao();
                    s2.xembangdiem("18HCB", "CTT001");
                    break;
                case "11":
                    ScoreDao scoreDao = new ScoreDao();
                    scoreDao.editScore("1742005", "18HCB", "CTT001", 9,9,9, 9);
                    break;
                case "12":
                    ScoreDao s3 = new ScoreDao();
                    s3.xemdiem("18HCB", "CTT001", "1742005");
                    break;
                case "13":
                    // AccountsDao.createAccount("giaovu", "giaovu", 1);
                    AccountsDao.changePassword("giaovu", "giaovu", "giaovumoi");
                    break;
                case "14":
                    RemarkEventDao r = new RemarkEventDao();
                    r.createRemarkEvent("18/06/2020", "19/06/2020");
                    break;
                case "15":
                    RemarkEventDao r1 = new RemarkEventDao();
                    r1.createRemart("19/06/2020", "1842002", "18HCB", "CTT001", 1, "Em Gioi", 10);
                    break;
                default:
                    break;
            }
        }


    }

    public static void login() {
        System.out.println( " ======================== Danh nhap ==========================" );

        Scanner sc = new Scanner(System.in);
        System.out.printf( "username: " );
        String username = sc.nextLine();
        System.out.println("");
        System.out.printf( "password: " );
        String password = sc.nextLine();

        try {
            boolean check = AccountsDao.login(username, password);
            if(check) {
                System.out.println("Login Successful!!!");
            }
            else {
                System.out.println("Login Failed!!!");
                login();
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Login Failed!!!");
            login();
            e.printStackTrace();
        }
    }

    public static void addStudent() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap mssv: ");

        String mssv = sc.nextLine();

        System.out.printf("Nhap ho va ten: ");

        String name = sc.nextLine();

        System.out.printf("Nhap cmnd: ");

        String id = sc.nextLine();
        StudentsDao s = new StudentsDao();
        // s.createStudent(mssv, name, 0, id);

    }

    public static void importClass() {
        ClassesDao classesDao = new ClassesDao();

        classesDao.importClasses("D:\\javaproject\\ltudjava-summer2020-1612083-bt2\\src\\danhsachlop.csv", ",");
    }

    public static void importSchedules() {
        SchedulesDao schedulesDao = new SchedulesDao();

        schedulesDao.importSchedule("D:\\javaproject\\ltudjava-summer2020-1612083-bt2\\src\\thoikhoabieu.csv", ",");
    }

    public static void requestSpecial(int type) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap mssv: ");

        String mssv = sc.nextLine();

        System.out.printf("Nhap class: ");

        String classID = sc.nextLine();

        System.out.printf("Nhap ma mon: ");

        String subject = sc.nextLine();


        SpecialStudentDao s = new SpecialStudentDao();
        s.requestStudyAgains(mssv, classID, subject, type);
    }

}
