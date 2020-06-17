package dao;

import model.ClassesEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.io.*;
import java.util.List;

public class ClassesDao {

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
        try{
            classesSession = HibernateUtil.getSessionFactory().openSession();
            String hql = "SELECT * FROM ClassesEntity c left join StudentsEntity s on c.studentId=s.studentId WHERE c.classId='" + classID + "'";
            Query query = classesSession.createQuery(hql);
            List result = query.getResultList();
        } catch (Exception e) {

        }


        return true;
    }
}
