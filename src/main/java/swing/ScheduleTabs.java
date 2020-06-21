/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing;

import dao.ClassesDao;
import dao.SchedulesDao;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import model.ClassesEntity;
import model.SchedulesEntity;
import model.StudentsEntity;
import model.SubjectsEntity;

/**
 *
 * @author black
 */
public class ScheduleTabs extends javax.swing.JPanel {

    ClassesDao classesDao = new ClassesDao();
    List<ClassesEntity> classesName;
    SchedulesDao schedulesDao = new SchedulesDao();
    String[] columnNames = {"STT",
            "Mã môn",
            "Tên môn",
            "Phòng học",
    };
    boolean loading = false;
    Container f = this;
    /**
     * Creates new form ClassTabs
     */
    public ScheduleTabs() {
        initComponents();
        Object[][] data = new Object[0][columnNames.length];
        loading_conponent.setVisible(false);
        table_schedule.setModel(new javax.swing.table.DefaultTableModel(
                data,
                columnNames
        ) {
            boolean[] canEdit = new boolean [] {
                    false, false, false, false
            };
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
    }

    public void initData() {
        initSelectBoxData();
        initTableData();
    }
    
    void initSelectBoxData() {
        classesName = classesDao.getListClass();
        if(classesName == null || classesName.size() == 0) {
            class_select_box.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{""}));
            return;
        }
        String[] name = new String[classesName.size()];
        for (int i = 0; i < classesName.size(); i++) {
            name[i] = classesName.get(i).getClassId();
        }
        class_select_box.setModel(new javax.swing.DefaultComboBoxModel<>(name));
    }

    class GetTable extends Thread {
        public void run() {
            try {
                String selectedString = (String) class_select_box.getSelectedItem();
                List<SubjectsEntity> classData = schedulesDao.getSchedule(selectedString);
                if(classData == null) {
                    classData = new ArrayList<>();
                }

                Object[][] data = new Object[classData.size()][columnNames.length];

                for (int i = 0; i < classData.size(); i++) {
                    SubjectsEntity s = classData.get(i);
                    data[i][0] = i + 1;
                    data[i][1] = s.getSubjectId();
                    data[i][2] = s.getName();
                    data[i][3] = s.getRoom();
                }

                table_schedule.setModel(new javax.swing.table.DefaultTableModel(
                        data,
                        columnNames
                ){
                    boolean[] canEdit = new boolean [] {
                            false, false, false, false
                    };
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit [columnIndex];
                    }
                });
                loading_conponent.setVisible(false);
            }catch (Exception e) {
                e.printStackTrace();
                loading_conponent.setVisible(false);
                JOptionPane.showMessageDialog(f.getParent(), "Hệ thống đã xảy ra lỗi, xin vui lỏng thử lại sau!");
            }
        }
    }
    
    void initTableData() {
        String selectedString = (String) class_select_box.getSelectedItem();
        if(selectedString == null || selectedString.equals("")) return;

        loading_conponent.setVisible(true);
        GetTable getTable = new GetTable();
        getTable.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table_schedule = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        class_select_box = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        loading_conponent = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(900, 650));

        jScrollPane1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        table_schedule.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        table_schedule.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "MSSV", "Họ và tên", "Giới tính"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table_schedule);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Quản lý thời khóa biểu");
        jPanel1.add(jLabel1, java.awt.BorderLayout.CENTER);
        jPanel1.add(jSeparator1, java.awt.BorderLayout.PAGE_END);

        jPanel2.setPreferredSize(new java.awt.Dimension(900, 50));

        jButton1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jButton1.setText("Import danh sách thời khóa biểu");
        jButton1.setPreferredSize(new java.awt.Dimension(300, 35));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        class_select_box.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        class_select_box.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "[\"hihi\"]", "awd" }));
        class_select_box.setPreferredSize(new java.awt.Dimension(38, 35));
        class_select_box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                class_select_boxActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel2.setText("Mã lớp: ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(class_select_box, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 389, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(class_select_box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel2))
        );

        loading_conponent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Rolling-1s-32px.gif"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(loading_conponent)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(loading_conponent, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void class_select_boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_class_select_boxActionPerformed
        // TODO add your handling code here:
        JComboBox cb = (JComboBox)evt.getSource();
        initTableData();
    }//GEN-LAST:event_class_select_boxActionPerformed

    class ImportDate extends Thread {
        public void run (){
            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result;
                result = fileChooser.showOpenDialog(f.getParent());
                if (result == JFileChooser.APPROVE_OPTION) {
                    // user selects a file
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    String info = schedulesDao.importSchedule(selectedFile.getAbsolutePath(), ",");
                    if(info.equals(SchedulesDao.SCHEDULE_SUCCESS)) {
                        initData();
                    }
                    JOptionPane.showMessageDialog(f.getParent(), info);
                }
                jButton1.setIcon(null);
                loading = false;

            }catch (Exception e) {
                e.printStackTrace();
                jButton1.setIcon(null);
                loading = false;
                JOptionPane.showMessageDialog(f.getParent(), "Hệ thống đã xảy ra lỗi, xin vui lỏng thử lại sau!");
            }
        }
    }


    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        ClassLoader cldr = this.getClass().getClassLoader();
        java.net.URL imageURL   = cldr.getResource("Rolling-1s-24px.gif");
        ImageIcon img = new ImageIcon(imageURL);
        jButton1.setIcon(img);
        loading = true;
        ImportDate importDate = new ImportDate();
        importDate.start();


    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> class_select_box;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel loading_conponent;
    private javax.swing.JTable table_schedule;
    // End of variables declaration//GEN-END:variables
}
