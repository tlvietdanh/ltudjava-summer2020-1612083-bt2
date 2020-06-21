/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

import dao.RemarkEventDao;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import model.RemarkEntity;
import model.StudentsEntity;
import swing.Dialog.CreateRemarkEvent;
import swing.Dialog.UpdateRemakeStatus;

import javax.swing.*;

/**
 *
 * @author black
 */
public class RemarkTabs extends javax.swing.JPanel {
    String[] columnNames = {"STT",
                            "MSSV",
                            "Họ tên",
                            "Môn",
                            "Cột điểm",
                            "Điểm mong muốn",
                            "Lý do",
                            "Trạng thái"};
    RemarkEventDao rkd = new RemarkEventDao();
    Container f = this;
    boolean loading = false;
    /**
     * Creates new form ClassTabs
     */
    public RemarkTabs() {
        initComponents();
        
        Object[][] data = new Object[0][columnNames.length];
        
        table_class.setModel(new javax.swing.table.DefaultTableModel(
            data,
            columnNames
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        loading_conponent.setVisible(false);
        initTableData();
    }
    
    class GetTableData extends Thread {
        public void run() 
        { 
            try
            { 
                List remarkData = rkd.getListRemark();

                if(remarkData == null || remarkData.size() == 0) {
                    return;
                }
                
                Object[][] data = new Object[remarkData.size()][columnNames.length];

                Iterator itr = remarkData.iterator();
                int i = 0;
                while (itr.hasNext()) {
                    Object[] objects = (Object[]) itr.next();
                    RemarkEntity r = (RemarkEntity) objects[0];
                    String name = (String) objects[1];
                    String subname = (String) objects[2];
                    data[i][0] = i + 1;
                    data[i][1] = r.getStudentId();
                    data[i][2] = name;
                    data[i][3] = subname;
                    switch (r.getType()) {
                        case 0:
                            data[i][4] = "Điểm giữa kỳ";
                            break;
                        case 1:
                            data[i][4] = "Điểm cuối kỳ";
                            break;
                        case 2:
                            data[i][4] = "Điểm khác";
                            break;
                        case 3:
                            data[i][4] = "Điểm tổng kết";
                            break;
                    }
                    data[i][5] = r.getNewScore();
                    data[i][6] = r.getReason();
                    switch (r.getStatus()) {
                        case 0:
                            data[i][7] = "Chưa cập nhật";
                            break;
                        case 1:
                            data[i][7] = "Đã cập nhật";
                            break;
                        case 2:
                            data[i][7] = "Đã từ chối";
                            break;
                    }
                    i++;
                }
                table_class.setModel(new javax.swing.table.DefaultTableModel(
                    data,
                    columnNames
                ) {
                    boolean[] canEdit = new boolean [] {
                        false, false, false, false, false
                    };

                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit [columnIndex];
                    }

                });
                loading_conponent.setVisible(false);

            } 
            catch (Exception e) 
            {
                e.printStackTrace();
                // Throwing an exception 
                System.out.println ("Exception is caught");
                loading_conponent.setVisible(false);
                JOptionPane.showMessageDialog(f.getParent(), "Hệ thống đã xảy ra lỗi, xin vui lỏng thử lại sau!");
            } 
        } 
    }
    
    void initTableData() {
        loading_conponent.setVisible(true);
        GetTableData getTableData = new GetTableData();
        getTableData.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_class = new javax.swing.JTable();
        add_remark = new javax.swing.JButton();
        update_remark = new javax.swing.JButton();
        loading_conponent = new javax.swing.JLabel();

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Quản lý phúc khảo");
        jPanel1.add(jLabel1, java.awt.BorderLayout.CENTER);
        jPanel1.add(jSeparator1, java.awt.BorderLayout.PAGE_END);

        jScrollPane1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        table_class.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        table_class.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "MSSV", "Họ và tên", "Giới tính", "CMND"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table_class);

        add_remark.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        add_remark.setText("Tạo đợt phúc khảo");
        add_remark.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                add_remarkMouseClicked(evt);
            }
        });
        add_remark.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_remarkActionPerformed(evt);
            }
        });

        update_remark.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        update_remark.setText("Cập nhật kết quả phúc khảo");
        update_remark.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                update_remarkMouseClicked(evt);
            }
        });
        update_remark.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                update_remarkActionPerformed(evt);
            }
        });

        loading_conponent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Rolling-1s-32px.gif"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(update_remark)
                                .addGap(394, 394, 394)
                                .addComponent(add_remark))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addComponent(loading_conponent)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(update_remark)
                    .addComponent(add_remark))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(loading_conponent)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void add_remarkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_add_remarkMouseClicked
        // TODO add your handling code here:
        CreateRemarkEvent addStudent = new CreateRemarkEvent();
        addStudent.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        addStudent.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        addStudent.setSize(new Dimension(350, 400));
        addStudent.setLocationRelativeTo(evt.getComponent().getParent());
        addStudent.setVisible(true);
    }//GEN-LAST:event_add_remarkMouseClicked

    private void add_remarkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_remarkActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_add_remarkActionPerformed

    private void update_remarkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_update_remarkMouseClicked
        // TODO add your handling code here:
        List remarkData = rkd.getListRemark();

        if(remarkData == null || remarkData.size() == 0) {
            JOptionPane.showMessageDialog(f.getParent(), "Không có phúc khảo đang mở!");
            return;
        }

        UpdateRemakeStatus addStudent = new UpdateRemakeStatus();
        addStudent.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        addStudent.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        addStudent.setSize(new Dimension(456, 720));
        addStudent.setLocationRelativeTo(evt.getComponent().getParent());
        addStudent.setVisible(true);
         addStudent.addWindowListener(new WindowAdapter()
        {
            public void windowClosed(WindowEvent e)
            {
                initTableData();
            }
        });
    }//GEN-LAST:event_update_remarkMouseClicked

    private void update_remarkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_update_remarkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_update_remarkActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add_remark;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel loading_conponent;
    private javax.swing.JTable table_class;
    private javax.swing.JButton update_remark;
    // End of variables declaration//GEN-END:variables
}
