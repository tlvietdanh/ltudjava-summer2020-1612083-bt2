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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.SubjectsEntity;

public class ClassesDao<T> {

    public static final String CLASSES_ERROR = "Hệ thống đang có lỗi, xin vui lòng thử lai sau!";
    public static final String CLASSES_ERROR_FILE = "Tập tin không hợp lệ";
    public static final String CLASSES_ERROR_DATA = "Dữ liệu không hợp lệ";
    public static final String CLASSES_SUCCESS = "Nhập dữ liệu thành công";
    public static final String CLASSES_ERROR_EXISTS = "Dữ liệu đã tồn tại";

    
    public static Session classesSession;
    
    private boolean checkCSVData(File f, String delimeter) {
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(f));
           
            String row = bufferedReader.readLine();

            while (row != null) {
                String data[] = row.split(delimeter);
                if(data.length != 5) {
                    return false;
                }
                row = bufferedReader.readLine();
            }
        } catch (Exception ex) {
            Logger.getLogger(ClassesDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public String importClasses(String fileName, String delimeter) {
        if( fileName.length() == 0) {
            return CLASSES_ERROR_FILE;
        }

        File csvFile = new File(fileName);
        if (csvFile.isFile()) {
            // create BufferedReader and read data from csv
            try {
                if(!checkCSVData(csvFile, delimeter)){
                    return CLASSES_ERROR_FILE;
                }


                BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile));
                Scanner scan = new Scanner(csvFile);
                // String row = bufferedReader.readLine();
                String row = scan.next();

                while (row != null) {
                    String data[] = row.split(delimeter);
                    

                    String mssv = data.length > 0 ? data[0] : "";
                    String name = data.length > 1 ? data[1] : "";
                    String gender = data.length > 2 ? data[2] : "";
                    String id = data.length > 3 ? data[3] : "";
                    String classID = data.length > 4 ? data[4] : "";

                    if(mssv.length() == 0 || classID.length() == 0) {
                        row = bufferedReader.readLine();
                        continue;
                    }
                    addStudentToClass(mssv, name, gender.equals("Nam") ? 1 : 0, id, classID);
                    row = bufferedReader.readLine();
                }
                System.out.println("Import thanh cong!!!");
                return CLASSES_SUCCESS;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("Da xay ra loi trong qua trinh doc file");
                return CLASSES_ERROR_FILE;
            } catch (IOException e) {
                System.out.println("Da xay ra loi trong qua trinh doc file");
                e.printStackTrace();
                return CLASSES_ERROR_FILE;
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        else {
            System.out.println("File khong hop le!!");
        }

        return CLASSES_ERROR_FILE;
    }

    public String addStudentToClass(String mssv, String name, int gender, String id, String classID) {
        if(mssv.length() == 0 || classID.length() == 0) {
            return CLASSES_ERROR_DATA;
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
                return CLASSES_ERROR_EXISTS;
            }

            ClassesEntity classes = new ClassesEntity();
            classes.setClassId(classID);
            classes.setStudentId(mssv);

            transaction = classesSession.beginTransaction();
            classesSession.save(classes);
            transaction.commit();
            classesSession.close();
        } catch (Exception e) {
            classesSession.close();
            return CLASSES_ERROR;
        }
        return CLASSES_SUCCESS;
    }

    public List<StudentsEntity> danhsachlop(String classID) {
        if(classID.length() == 0) {
            return null;
        }
        try{
            classesSession = HibernateUtil.getSessionFactory().openSession();

            String hql = "SELECT s FROM ClassesEntity c left join StudentsEntity s on c.studentId=s.studentId WHERE c.classId='" + classID + "'";
            Query query = classesSession.createQuery(hql);
            List<StudentsEntity> listStudent = query.getResultList();
            Collections.sort(listStudent, (s1, s2) -> {
                return s1.getStudentId().compareTo(s2.getStudentId());
            });
            // result here
            classesSession.close();
            return listStudent;
        } catch (Exception e) {
            classesSession.close();
            e.printStackTrace();
        }
        return null;
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
            classesSession.close();
            return listStudent;

        } catch (Exception e) {
            e.printStackTrace();
        }
        classesSession.close();

        return null;
    }

    public List xembangdiem(String classID, String subjectID) {
        if(classID.length() == 0 || subjectID.length() == 0) {
            return null;
        }
        try{
            classesSession = HibernateUtil.getSessionFactory().openSession();

            // lay  danh  sach hoc sinh
            String getListStudent = "Select c, s.name from ScoresEntity c,  StudentsEntity s where s.studentId=c.studentId  and  c.classId='"+classID+"' and c.subjectId='" + subjectID + "' order by s.studentId";
            Query query = classesSession.createQuery(getListStudent);
            List listScore = query.getResultList();


          

            classesSession.close();
            return listScore;

        } catch (Exception e) {
            e.printStackTrace();
        }
        classesSession.close();

        return null;
    }

    public List<ClassesEntity> getListClass() {
        try {
            classesSession = HibernateUtil.getSessionFactory().openSession();
            String hql = "select c from ClassesEntity c group by c.classId";
            Query q = classesSession.createQuery(hql);
            List<ClassesEntity> result = q.getResultList();
            classesSession.close();
            return result;

        } catch (Exception e) {
            classesSession.close();
            e.printStackTrace();
        }
        return null;
    }
    
    public List<SubjectsEntity> getListSubject(String classId) {
        try {
            classesSession = HibernateUtil.getSessionFactory().openSession();
            String hql = "select m from SchedulesEntity s, SubjectsEntity m where m.subjectId = s.subjectId and s.classId='"+classId+"' group by m.subjectId";
            Query q = classesSession.createQuery(hql);
            List<SubjectsEntity> result = q.getResultList();
            classesSession.close();
            return result;

        } catch (Exception e) {
            classesSession.close();
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getClassOfStudent(String studentID) {
        if(studentID.length() == 0) {
            System.out.println("Ma loi ko hop le");
            return null;
        }
        classesSession = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "SELECT c from ClassesEntity c where c.studentId='" + studentID + "'";
            Query query = classesSession.createQuery(hql);
            List<ClassesEntity> result = query.getResultList();

            List<String> finalResult = new ArrayList<>();

            for (int i = 0; i < result.size(); i++) {
                finalResult.add(result.get(i).getClassId());
            }

            classesSession.close();

            return finalResult;

        } catch (Exception e) {
            e.printStackTrace();
        }
        classesSession.close();
        return null;
    }

    public List<SpecialstudentsEntity> getSpecialClassAndSubject( String studentID) {
        try {
            classesSession = HibernateUtil.getSessionFactory().openSession();
            String hql = "select s from SpecialstudentsEntity s where s.studentId='"+studentID+"'";
            Query q = classesSession.createQuery(hql);
            List<SpecialstudentsEntity> result = q.getResultList();
            classesSession.close();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            classesSession.close();
        }
        return null;
    }

}
