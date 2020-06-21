package dao;

import model.ClassesEntity;
import model.SchedulesEntity;
import model.StudentsEntity;
import model.SubjectsEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.io.*;
import java.util.Collections;
import java.util.List;

public class SchedulesDao {
    public static Session scheduleSession;
    
    public static final String SCHEDULE_ERROR = "Hệ thống đang có lỗi, xin vui lòng thử lai sau!";
    public static final String SCHEDULE_ERROR_FILE = "Tập tin không hợp lệ";
    public static final String SCHEDULE_ERROR_DATA = "Dữ liệu không hợp lệ";
    public static final String SCHEDULE_SUCCESS = "Nhập dữ liệu thành công";
    public static final String SCHEDULE_ERROR_EXISTS = "Dữ liệu đã tồn tại";

    public String importSchedule(String fileName, String delimeter) {
        if( fileName.length() == 0) {
            return SCHEDULE_ERROR_FILE;
        }

        File csvFile = new File(fileName);
        if (csvFile.isFile()) {
            // create BufferedReader and read data from csv
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile));
                String row = bufferedReader.readLine();

                while (row != null) {
                    String data[] = row.split(delimeter);
                    if(data.length != 4) {
                        return SCHEDULE_ERROR_FILE;
                    }

                    String subjectID = data.length > 0 ? data[0] : "";
                    String name = data.length > 1 ? data[1] : "";
                    String room = data.length > 2 ? data[2] : "";
                    String classID = data.length > 3 ? data[3] : "";

                    if(subjectID.length() == 0 || classID.length() == 0) {
                        return SCHEDULE_ERROR_DATA;
                    }
                    addSubjectToSchedule(subjectID, name, room, classID);
                    row = bufferedReader.readLine();
                }
                System.out.println("Import thanh cong!!!");
                return SCHEDULE_SUCCESS;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("Da xay ra loi trong qua trinh doc file");
                return SCHEDULE_ERROR_FILE;
            } catch (IOException e) {
                System.out.println("Da xay ra loi trong qua trinh doc file");
                e.printStackTrace();
                return SCHEDULE_ERROR_FILE;
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        else {
            System.out.println("File khong hop le!!");
        }

        return SCHEDULE_ERROR;
    }

    public boolean addSubjectToSchedule(String subjectID, String name, String room, String classID) {
        if(subjectID.length() == 0 || classID.length() == 0) {
            return false;
        }
        try {
            scheduleSession = HibernateUtil.getSessionFactory().openSession();

            String hql = "FROM ClassesEntity c WHERE c.classId='"+classID+"'";
            Query query = scheduleSession.createQuery(hql);
            if(query.getResultList().size() == 0) {
                System.out.println("Lop hoc khong ton tai!");
                return false;
            }
            Transaction transaction = null;

            // create subject if not exists
            SubjectsDao.createSubject(subjectID, name, room);
            // create schedule
            SchedulesEntity schedulesEntity = new SchedulesEntity();
            schedulesEntity.setClassId(classID);
            schedulesEntity.setSubjectId(subjectID);

            transaction = scheduleSession.beginTransaction();
            scheduleSession.save(schedulesEntity);
            transaction.commit();
            scheduleSession.close();
        } catch (Exception e) {
            scheduleSession.close();
            return false;
        }
        return true;
    }

    public List<SubjectsEntity> getSchedule(String classID)  {
        if(classID.length() == 0) {
            System.out.println("Ma loi ko hop le");
            return null;
        }
        try{
            scheduleSession = HibernateUtil.getSessionFactory().openSession();
            String hql = "SELECT s FROM SchedulesEntity c, SubjectsEntity s  WHERE c.classId='" + classID + "' and c.subjectId=s.subjectId";
            Query query = scheduleSession.createQuery(hql);
            List<SubjectsEntity> schedule = query.getResultList();
            // result here
            Collections.sort(schedule, (s1, s2) -> {
                return s1.getSubjectId().compareTo(s2.getSubjectId());
            });
            // result here
            scheduleSession.close();
            return schedule;
        } catch (Exception e) {
            e.printStackTrace();
            scheduleSession.close();
        }

        return null;
    }
    
    public List<SchedulesEntity> getListClass() {
        try {
            scheduleSession = HibernateUtil.getSessionFactory().openSession();
            String hql = "select c from SchedulesEntity c group by c.classId";
            Query q = scheduleSession.createQuery(hql);
            List<SchedulesEntity> result = q.getResultList();
            scheduleSession.close();
            return result;

        } catch (Exception e) {
            scheduleSession.close();
            e.printStackTrace();
        }
        return null;
    }
}
