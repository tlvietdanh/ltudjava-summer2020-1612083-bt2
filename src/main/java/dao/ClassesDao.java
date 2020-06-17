package dao;

import model.ClassesEntity;
import model.ScoresEntity;
import model.SpecialstudentsEntity;
import model.StudentsEntity;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.mapping.Array;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ClassesDao<T> {

    public static Session classesSession;

    public boolean importClasses(String fileName, String delimeter) {
        if( fileName.length() == 0) {
            return false;
        }

        File csvFile = new File(fileName);
        if (csvFile.isFile()) {
            // create BufferedReader and read data from csv
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile));
                String row = bufferedReader.readLine();

                while (row != null) {
                    String data[] = row.split(delimeter);
                    if(data.length != 5) {
                        System.out.println("File chua du lieu khong hop le");
                    }

                    String mssv = data.length > 0 ? data[0] : "";
                    String name = data.length > 1 ? data[1] : "";
                    String gender = data.length > 2 ? data[2] : "";
                    String id = data.length > 3 ? data[3] : "";
                    String classID = data.length > 4 ? data[4] : "";

                    if(mssv.length() == 0 || classID.length() == 0) {
                        System.out.println("Thong tin khong hop le");
                        row = bufferedReader.readLine();
                        continue;
                    }
                    addStudentToClass(mssv, name, gender.equals("Nam") ? 1 : 0, id, classID);
                    row = bufferedReader.readLine();
                }
                System.out.println("Import thanh cong!!!");
                return true;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("Da xay ra loi trong qua trinh doc file");
                return false;
            } catch (IOException e) {
                System.out.println("Da xay ra loi trong qua trinh doc file");
                e.printStackTrace();
                return false;
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        else {
            System.out.println("File khong hop le!!");
        }

        return false;
    }

    public boolean addStudentToClass(String mssv, String name, int gender, String id, String classID) {
        if(mssv.length() == 0 || classID.length() == 0) {
            return false;
        }
        try {
            classesSession = HibernateUtil.getSessionFactory().openSession();

            Transaction transaction = null;

            // create student if not exists
            StudentsDao.createStudent(mssv, name, gender, id);
            // create classes
            String hql = "FROM ClassesEntity c WHERE c.classId='"+classID+"' and c.studentId='"+mssv+"'";
            Query query = classesSession.createQuery(hql);
            if(query.getResultList().size() > 0) {
                return false;
            }

            ClassesEntity classes = new ClassesEntity();
            classes.setClassId(classID);
            classes.setStudentId(mssv);

            transaction = classesSession.beginTransaction();
            classesSession.save(classes);
            transaction.commit();
            classesSession.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean danhsachlop(String classID) {
        if(classID.length() == 0) {
            System.out.println("Ma loi ko hop le");
            return false;
        }
        classesSession = HibernateUtil.getSessionFactory().openSession();
        try{
            String hql = "SELECT s FROM ClassesEntity c left join StudentsEntity s on c.studentId=s.studentId WHERE c.classId='" + classID + "'";
            Query query = classesSession.createQuery(hql);
            List<StudentsEntity> listStudent = query.getResultList();
            // result here
            Collections.sort(listStudent, (s1, s2) -> {
                return s1.getStudentId().compareTo(s2.getStudentId());
            });
            // result here
            for (int i = 0; i < listStudent.size(); i++) {
                System.out.println(listStudent.get(i).getStudentId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        classesSession.close();

        return true;
    }

    public List<StudentsEntity> danhsachmon(String classID, String subject) {
        if(classID.length() == 0 || subject.length() == 0) {
            System.out.println("Ma loi ko hop le");
            return null;
        }
        classesSession = HibernateUtil.getSessionFactory().openSession();
        try{

            // lay  danh  sach hoc sinh
            String getListStudent = "Select s from ClassesEntity c,  StudentsEntity s where s.studentId=c.studentId  and  c.classId='"+classID+"'";
            Query query = classesSession.createQuery(getListStudent);
            List<StudentsEntity> listStudent = query.getResultList();

            //  lay  danh  sach  xin  hoc lai
            String getListSpecialStudents = "Select s from SpecialstudentsEntity p,  StudentsEntity s where p.subjectId='"+subject+"' and p.type=1  and s.studentId = p.studentId";
            query = classesSession.createQuery(getListSpecialStudents);
            List<StudentsEntity> listAddSub = query.getResultList();

            //  lay  danh  sach  xin  huy mon
            String getListRemoveSbject = "Select s from SpecialstudentsEntity p,  StudentsEntity s where p.subjectId='"+subject+"' and p.type=-1  and s.studentId = p.studentId";
            query = classesSession.createQuery(getListRemoveSbject);
            List<StudentsEntity> listRemoveSub = query.getResultList();


            listStudent.addAll(listAddSub);
            listStudent.removeAll(listRemoveSub);
            Collections.sort(listStudent, (s1, s2) -> {
                return s1.getStudentId().compareTo(s2.getStudentId());
            });
            // result here
            for (int i = 0; i < listStudent.size(); i++) {
                System.out.println(listStudent.get(i).getStudentId());
            }
            classesSession.close();
            return listStudent;

        } catch (Exception e) {
            e.printStackTrace();
        }
        classesSession.close();

        return null;
    }

    public boolean xembangdiem(String classID, String subjectID) {
        if(classID.length() == 0 || subjectID.length() == 0) {
            System.out.println("Ma loi ko hop le");
            return false;
        }
        classesSession = HibernateUtil.getSessionFactory().openSession();
        try{

            // lay  danh  sach hoc sinh
            String getListStudent = "Select c, s.name from ScoresEntity c,  StudentsEntity s where s.studentId=c.studentId  and  c.classId='"+classID+"' and c.subjectId='" + subjectID + "' order by s.studentId";
            Query query = classesSession.createQuery(getListStudent);
            List listScore = query.getResultList();


            Iterator itr = listScore.iterator();
            while(itr.hasNext()){
                Object[] obj = (Object[]) itr.next();
                ScoresEntity score = (ScoresEntity) obj[0];
                String name = (String) obj[1];
                System.out.println(score.getStudentId() + "  " + name + "  " + score.getMidTermScore() + "  " + score.getEndTermScore() + "  " + score.getOtherScore() + "  " + score.getTotalScore());
            }

            classesSession.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        classesSession.close();

        return false;
    }



}
