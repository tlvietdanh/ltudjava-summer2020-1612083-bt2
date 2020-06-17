package org.example;

/**
 * Hello world!
 *
 */
import dao.*;
import model.AccountsEntity;

import java.security.NoSuchAlgorithmException;
import java.util.List;
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
                    System.out.println("Xin hoc lai");
                    requestSpecial(-1);
                    break;
                case "5":
                    System.out.println("Xin hoc lai");
                    requestSpecial(1);
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

        schedulesDao.importClasses("D:\\javaproject\\ltudjava-summer2020-1612083-bt2\\src\\thoikhoabieu.csv", ",");
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
