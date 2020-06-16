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

        login();

        System.out.printf( "Chon: " );

        Scanner sc = new Scanner(System.in);

        String choose = sc.nextLine();

        switch (choose) {
            case "1":
                System.out.println("Them Sinh Vien");

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
}
