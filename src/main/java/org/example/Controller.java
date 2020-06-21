package org.example;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.ImageIcon;
import javax.swing.JButton;
import swing.Student.StudentDashBoard;
import swing.TeacherDashBoard;
import swing.login.Login;

public class Controller {


    
    public static User user = null;
    private static TeacherDashBoard teacherDashBoard;
    private static StudentDashBoard studentDashBoard;
    private static Login login;
    public static boolean loading = false;
    public static JButton button;
    
    
    public static void handleLogin() {

        if(user == null) {
            login = new Login();
            login.setVisible(true);
            if(teacherDashBoard!=null) {
                teacherDashBoard.setVisible(false);
                teacherDashBoard.dispose();
            }
            if(studentDashBoard!=null) {
                studentDashBoard.setVisible(false);
                studentDashBoard.dispose();
            }
        }
        else if(user.userType == 1){
            teacherDashBoard = new TeacherDashBoard();
            teacherDashBoard.setVisible(true);
            button.setIcon(null);

            if(login!=null) {
                login.setVisible(false);
                login.dispose();
            }
            if(studentDashBoard != null) {
                studentDashBoard.setVisible(false);
                studentDashBoard.dispose();
            }
        }
        else {
            studentDashBoard = new StudentDashBoard();
            studentDashBoard.setVisible(true);
            button.setIcon(null);
            if(login!=null) {
                login.setVisible(false);
                login.dispose();
            }
            if(teacherDashBoard != null) {
                teacherDashBoard.setVisible(false);
                teacherDashBoard.dispose();
            }
        }
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TeacherDashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TeacherDashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TeacherDashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TeacherDashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        // ClassTabs c = new ClassTabs();
        /* Create and display the form */

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                handleLogin();
            }
        });
    }
    
}
