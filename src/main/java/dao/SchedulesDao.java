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

    public boolean importSchedule(String fileName, String delimeter) {
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
                    if(data.length != 4) {
                        System.out.println("File chua du lieu khong hop le");
                    }

                    String subjectID = data.length > 0 ? data[0] : "";
                    String name = data.length > 1 ? data[1] : "";
                    String room = data.length > 2 ? data[2] : "";
                    String classID = data.length > 3 ? data[3] : "";

                    if(subjectID.length() == 0 || classID.length() == 0) {
                        System.out.println("Thong tin khong hop le");
                        row = bufferedReader.readLine();
                        continue;
                    }
                    addSubjectToSchedule(subjectID, name, room, classID);
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
            return false;
        }
        return true;
    }

    public  static void getSchedule(String classID)  {
        if(classID.length() == 0) {
            System.out.println("Ma loi ko hop le");
            return;
        }
        scheduleSession = HibernateUtil.getSessionFactory().openSession();
        try{
            String hql = "SELECT s FROM SchedulesEntity c, SubjectsEntity s  WHERE c.classId='" + classID + "' and c.subjectId=s.subjectId";
            Query query = scheduleSession.createQuery(hql);
            List<SubjectsEntity> schedule = query.getResultList();
            // result here
            Collections.sort(schedule, (s1, s2) -> {
                return s1.getSubjectId().compareTo(s2.getSubjectId());
            });
            // result here
            for (int i = 0; i < schedule.size(); i++) {
                System.out.println(schedule.get(i).getSubjectId() +  "  "  +  schedule.get(i).getName()  +  "  "  +  schedule.get(i).getRoom());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        scheduleSession.close();

        return;
    }
}
