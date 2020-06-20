package dao;

import model.RemarkEntity;
import model.RemarkeventEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RemarkEventDao {
    Session remarkEventSession;
    SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");


    public boolean createRemarkEvent(String startDate, String endDate) {
        try {
            Date start = formater.parse(startDate);
            Date end = formater.parse(endDate);

            if(start.compareTo(end) == 1 ) {
                System.out.println("Invalid Date");
                return false;
            }

            remarkEventSession = HibernateUtil.getSessionFactory().openSession();

            Transaction transaction = null;

            RemarkeventEntity remarkeventEntity = new RemarkeventEntity();

            remarkeventEntity.setStartTime(new java.sql.Date(start.getTime()));
            remarkeventEntity.setEndTime(new java.sql.Date(end.getTime()));

            transaction = remarkEventSession.beginTransaction();

            remarkEventSession.save(remarkeventEntity);

            transaction.commit();

            remarkEventSession.close();

            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Invalid Date");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createRemart(String time, String studentID, String classID, String subjectID, int type, String reason, double newScore) {
        if(classID.length() == 0 || subjectID.length() == 0 || studentID.length() == 0) {
            System.out.println("Ma loi ko hop le");
            return false;
        }
        RemarkeventEntity event = checkValidTime(time);
        if(event == null) {
            System.out.println("Khong co dot phuc khao dang dien ra!");
            return false;
        }

        try {
            remarkEventSession = HibernateUtil.getSessionFactory().openSession();

            RemarkEntity r = new RemarkEntity();
            r.setRemarkEventId(event.getRemarkEventId());
            r.setClassId(classID);
            r.setStudentId(studentID);
            r.setSubjectId(subjectID);
            r.setType(type);
            r.setReason(reason);
            r.setNewScore(newScore);
            r.setStatus(0);

            Transaction transaction = null;
            transaction = remarkEventSession.beginTransaction();
            remarkEventSession.save(r);
            transaction.commit();

            remarkEventSession.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public RemarkeventEntity checkValidTime(String time) {
        try {
            Date currentTime = formater.parse(time);
            java.sql.Date mDate = new java.sql.Date(currentTime.getTime());
            remarkEventSession = HibernateUtil.getSessionFactory().openSession();

            String sb = "select e from RemarkeventEntity e where e.status=1 and e.startTime <= :date and e.endTime >= :date";
            Query query = remarkEventSession.createQuery(sb);
            query.setTimestamp("date", mDate);

            List<RemarkeventEntity> result = query.getResultList();
            if(result.size() > 0) {
                remarkEventSession.close();
                return result.get(result.size() - 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        remarkEventSession.close();
        return null;
    }

    public List getListRemark() {
        try {
            remarkEventSession = HibernateUtil.getSessionFactory().openSession();
            String hql = "select r, s.name, c.name from RemarkEntity r, StudentsEntity s, SubjectsEntity c where s.studentId = r.studentId and c.subjectId=r.subjectId";
            Query query = remarkEventSession.createQuery(hql);
            List result = query.getResultList();

            remarkEventSession.close();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        remarkEventSession.close();
        return null;
    }
}
