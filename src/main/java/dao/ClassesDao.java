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
        Transaction transaction = null;


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

                    classesSession = HibernateUtil.getSessionFactory().openSession();
                    // create student if not exists
                    StudentsDao.createStudent(mssv, name, gender.equals("Nam") ? 0 : 1, id);
                    // create classes
                    ClassesEntity classes = new ClassesEntity();
                    classes.setClassId(classID);
                    classes.setStudentId(mssv);

                    transaction = classesSession.beginTransaction();
                    classesSession.save(classes);
                    transaction.commit();
                    row = bufferedReader.readLine();
                    classesSession.close();
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

    private static List<ClassesEntity> getClasses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List result = session.createQuery("from ClassesEntity ", ClassesEntity.class).list();
            session.close();
            return result;
        }
    }
}
