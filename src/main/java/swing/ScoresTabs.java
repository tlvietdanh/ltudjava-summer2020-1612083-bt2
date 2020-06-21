/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing;
import dao.ClassesDao;
import dao.ScoreDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.TableColumn;

import model.*;
import org.example.Controller;
import swing.Dialog.UpdateScore;

/**
 *
 * @author black
 */
public class ScoresTabs extends JPanel {
    
    ScoreDao scoreDao = new ScoreDao();
    ClassesDao classesDao = new ClassesDao();
    List<SpecialstudentsEntity> special = new ArrayList<>();
    String[] columnNames = {"STT",
            "MSSV",
            "Họ tên",
            "Điểm GK",
            "Điểm CK",
            "Điểm khác",
            "Điểm tổng",
            "Kết quả"};
    boolean studentType = Controller.user.userType == 0 ? true : false;
    Container f = this;
    boolean loading = false;
    /**
     * Creates new form ClassTabs
     */
    public ScoresTabs() {
        initComponents();
        
        loading_component.setVisible(false);


        if(studentType) {
            jButton1.setVisible(false);
            jButton2.setVisible(false);
            numDLable.setVisible(false);
            numFail.setVisible(false);
            numPass.setVisible(false);
            perFail.setVisible(false);
            perPass.setVisible(false);
            jLabel8.setVisible(false);
            jLabel11.setVisible(false);
            numR.setVisible(false);
        }
    }

    public void initData() {
        initSelectBoxData();
        initTableData();
    }
    
    
    public void initSelectBoxData() {

        List<String> className;

        if(studentType) {
           className = scoreDao.getListClassOfStudent(Controller.user.username);
        }
        else {
            className = scoreDao.getListClass();
        }
        if(className == null || className.size() == 0) {
            class_select_box.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {""}));
            initSubjectData("");
            return;
        }

        String[] name = new String[className.size()];

        className.toArray(name);

        class_select_box.setModel(new javax.swing.DefaultComboBoxModel<>(name));
        initSubjectData(className.get(0));
    }
    
    public void initSubjectData(String classID) {
        List<String> subjectsName;
        if(studentType) {
            subjectsName = scoreDao.getListSubjectOfStudent(Controller.user.username,classID);
        }
        else {
            subjectsName = scoreDao.getListSubject(classID);
        }
        if(subjectsName == null || subjectsName.size() == 0) {
            subject_select_box.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {""}));
            return;
        }
        String[] subject = new String[subjectsName.size()];
        subjectsName.toArray(subject);

        subject_select_box.setModel(new javax.swing.DefaultComboBoxModel<>(subject));

    }

    class GetData extends Thread {
        public void run() {
            try {
                String selectedClass = (String) class_select_box.getSelectedItem();
                String selectedSubject = (String) subject_select_box.getSelectedItem();
                List listScore;
                if(studentType) {
                    listScore = scoreDao.xemdiem(selectedClass, selectedSubject, Controller.user.username);

                }
                else {
                    listScore = classesDao.xembangdiem(selectedClass, selectedSubject);
                }


                Object[][] data = new Object[ listScore != null ? listScore.size() : 0][columnNames.length];
                if(listScore != null) {
                    int i = 0, numP = 0, numF = 0;
                    Iterator itr = listScore.iterator();

                    while(itr.hasNext()){
                        Object[] obj = (Object[]) itr.next();
                        ScoresEntity score = (ScoresEntity) obj[0];
                        String name = (String) obj[1];
                        data[i][0] = i + 1;
                        data[i][1] = score.getStudentId();
                        data[i][2] = name;
                        data[i][3] = score.getMidTermScore();
                        data[i][4] = score.getEndTermScore();
                        data[i][5] = score.getOtherScore();
                        data[i][6] = score.getTotalScore();
                        data[i][7] = score.getTotalScore() >= 5.0 ? "Đậu" : "Rớt";
                        if(score.getTotalScore() >= 5.0) {
                            numP++;
                        }
                        else {
                            numF++;
                        }
                        i++;
                    }
                    numPass.setText(numP+"");
                    numFail.setText(numF+"");
                    if(numP != 0 && numF !=0) {
                        double per1 = (double) numP / (numP + numF) * 100;
                        double per2 = (double) numF / (numP + numF) * 100;

                        perPass.setText(per1 + "%");
                        perFail.setText(per2 + "%");
                    }
                    else {
                        perPass.setText(0 + "%");
                        perFail.setText(0 + "%");
                    }

                }

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
                loading_component.setVisible(false);
            }catch (Exception e) {
                e.printStackTrace();
                loading_component.setVisible(false);
                JOptionPane.showMessageDialog(f.getParent(), "Hệ thống đã xảy ra lỗi, xin vui lỏng thử lại sau!");
            }
        }
    }

    public void initTableData() {
        String selectedClass = (String) class_select_box.getSelectedItem();
        String selectedSubject = (String) subject_select_box.getSelectedItem();
        if(selectedClass == null || selectedSubject == null || selectedClass.equals("") || selectedSubject.equals("")) {
            loading_component.setVisible(false);
        }
        loading_component.setVisible(true);
        GetData getData = new GetData();
        getData.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_class = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        class_select_box = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        subject_select_box = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        numDLable = new javax.swing.JLabel();
        numPass = new javax.swing.JLabel();
        numR = new javax.swing.JLabel();
        numFail = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        perPass = new javax.swing.JLabel();
        perFail = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        loading_component = new javax.swing.JLabel();

        jTextField1.setText("jTextField1");

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

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Quản lý bảng điểm");
        jPanel1.add(jLabel1, java.awt.BorderLayout.CENTER);
        jPanel1.add(jSeparator1, java.awt.BorderLayout.PAGE_END);

        jPanel2.setPreferredSize(new java.awt.Dimension(900, 50));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel2.setText("Mã lớp: ");

        class_select_box.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        class_select_box.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "[\"hihi\"]", "awd" }));
        class_select_box.setPreferredSize(new java.awt.Dimension(38, 35));
        class_select_box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                class_select_boxActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jButton1.setText("Import bảng điểm");
        jButton1.setPreferredSize(new java.awt.Dimension(187, 35));
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

        subject_select_box.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        subject_select_box.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "[\"hihi\"]", "awd" }));
        subject_select_box.setPreferredSize(new java.awt.Dimension(38, 35));
        subject_select_box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subject_select_boxActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel3.setText("Mã môn học: ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(class_select_box, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(subject_select_box, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(subject_select_box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(class_select_box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jButton2.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jButton2.setText("Chỉnh sửa điểm");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        numDLable.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        numDLable.setText("Số lượng sinh viên đậu:");

        numPass.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        numPass.setForeground(new java.awt.Color(0, 153, 0));
        numPass.setText("0");

        numR.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        numR.setText("Số lượng sinh viên rớt:");

        numFail.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        numFail.setForeground(new java.awt.Color(204, 0, 51));
        numFail.setText("0");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setText("Phần trăm sinh viên đậu:");

        perPass.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        perPass.setForeground(new java.awt.Color(0, 153, 0));
        perPass.setText("0%");

        perFail.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        perFail.setForeground(new java.awt.Color(204, 0, 51));
        perFail.setText("0%");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel11.setText("Phần trăm sinh viên rớt:");

        loading_component.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Rolling-1s-32px.gif"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
                    .addComponent(jButton2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(numDLable)
                            .addComponent(numR))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(numFail)
                            .addComponent(numPass))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel11))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(perFail)
                            .addComponent(perPass))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(loading_component)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(numDLable)
                            .addComponent(numPass))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(numR)
                            .addComponent(numFail)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(perPass))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(perFail)))
                    .addComponent(loading_component, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addGap(26, 26, 26))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void class_select_boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_class_select_boxActionPerformed
        String selectedClass = (String) class_select_box.getSelectedItem();
        initSubjectData(selectedClass);
        initTableData();
    }//GEN-LAST:event_class_select_boxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

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
                    String info = scoreDao.importTranscript(selectedFile.getAbsolutePath(), ",");
                    if(info.equals(ScoreDao.SCORE_SUCCESS)) {
                        initSelectBoxData();
                        initTableData();
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
        if(loading) return;
        ClassLoader cldr = this.getClass().getClassLoader();
        java.net.URL imageURL   = cldr.getResource("Rolling-1s-24px.gif");
        ImageIcon img = new ImageIcon(imageURL);
        jButton1.setIcon(img);
        loading = true;
        ImportDate importDate = new ImportDate();
        importDate.start();
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void subject_select_boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subject_select_boxActionPerformed
        // TODO add your handling code here:
        initTableData();
    }//GEN-LAST:event_subject_select_boxActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
        if(loading) return;
        UpdateScore addStudent = new UpdateScore();
        addStudent.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        addStudent.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        addStudent.setSize(new Dimension(456, 610));
        addStudent.setLocationRelativeTo(evt.getComponent().getParent());
        addStudent.setVisible(true);
        addStudent.addWindowListener(new WindowAdapter()
        {
            public void windowClosed(WindowEvent e)
            {
                initData();
            }
        });

    }//GEN-LAST:event_jButton2MouseClicked

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> class_select_box;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel loading_component;
    private javax.swing.JLabel numDLable;
    private javax.swing.JLabel numFail;
    private javax.swing.JLabel numPass;
    private javax.swing.JLabel numR;
    private javax.swing.JLabel perFail;
    private javax.swing.JLabel perPass;
    private javax.swing.JComboBox<String> subject_select_box;
    private javax.swing.JTable table_class;
    // End of variables declaration//GEN-END:variables

}
