package dao;

import model.RemarkEntity;
import model.RemarkeventEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RemarkEventDao {
    Session remarkEventSession;
    SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");


    public String createRemarkEvent(String startDate, String endDate) {
        try {
            Date start = formater.parse(startDate);
            Date end = formater.parse(endDate);

            if(start.compareTo(end) >= 0 ) {
                System.out.println("Invalid Date");
                return "Dữ liệu không hợp lệ!";
            }
            remarkEventSession = HibernateUtil.getSessionFactory().openSession();

            String sb = "select e from RemarkeventEntity e";
            Query query = remarkEventSession.createQuery(sb);
            // query.setTimestamp("date", new java.sql.Date(end.getTime()));

            List<RemarkeventEntity> result = query.getResultList();
            for (int i = 0; i < result.size(); i++) {
                Date date = result.get(i).getEndTime();
                String endDate1 = formater.format(date);
                if(endDate.compareTo(endDate1) <= 0) {
                    remarkEventSession.close();
                    return "Đữ liệu đã tồn tại!";
                }
            }



            Transaction transaction = null;

            RemarkeventEntity remarkeventEntity = new RemarkeventEntity();

            remarkeventEntity.setStartTime(new java.sql.Date(start.getTime()));
            remarkeventEntity.setEndTime(new java.sql.Date(end.getTime()));

            transaction = remarkEventSession.beginTransaction();

            remarkEventSession.save(remarkeventEntity);

            transaction.commit();

            remarkEventSession.close();

            return "Tạo thành công!";
        } catch (ParseException e) {
            e.printStackTrace();
            remarkEventSession.close();
            System.out.println("Invalid Date");
            return "Dữ liệu không hợp lệ";
        } catch (Exception e) {
            e.printStackTrace();
            remarkEventSession.close();
            return "Hệ thống đang có lỗi, xin thử lại sau!";
        }
    }

    public String createRemart(String time, String studentID, String classID, String subjectID, int type, String reason, double newScore) {
        if(classID.length() == 0 || subjectID.length() == 0 || studentID.length() == 0) {
            System.out.println("Ma loi ko hop le");
            return "Dữ liệu không hợp lệ";
        }
        RemarkeventEntity event = checkValidTime(time);
        if(event == null) {
            System.out.println("Khong co dot phuc khao dang dien ra!");
            return "Không có đợt phúc khảo nào đang diễn ra!";
        }

        try {
            remarkEventSession = HibernateUtil.getSessionFactory().openSession();

            String hql = "select r from RemarkEntity r where r.type="+type+" and r.classId = '"+ classID +"' and r.subjectId='"+subjectID+"' and r.studentId='"+ studentID +"'";
            Query query = remarkEventSession.createQuery(hql);
            List<RemarkEntity> list = query.getResultList();
            if(list.size() > 0) {
                return "Phúc khảo đã tồn tại";
            }
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
            return "Tạo phúc khảo thành công!";
        } catch (Exception e) {
            e.printStackTrace();
            remarkEventSession.close();
        }

        return "Đã có lỗi xảy ra, xin vui lòng thử lai sau";
    }

    public RemarkeventEntity checkValidTime(String time) {
        try {
            Date currentTime = formater.parse(time);
            java.sql.Date mDate = new java.sql.Date(currentTime.getTime());
            remarkEventSession = HibernateUtil.getSessionFactory().openSession();

            String sb = "select e from RemarkeventEntity e where e.startTime <= :date and e.endTime >= :date";
            Query query = remarkEventSession.createQuery(sb);
            query.setParameter("date", mDate);

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

    public List<String> getListClass() {
        try {
            remarkEventSession = HibernateUtil.getSessionFactory().openSession();

            String hql = "select r from RemarkEntity r group by r.classId";
            Query query = remarkEventSession.createQuery(hql);
            List<RemarkEntity> list = query.getResultList();

            List<String> result = new ArrayList<>();
            for(int i = 0; i < list.size(); i++) {
                result.add(list.get(i).getClassId());
            }
            remarkEventSession.close();
            return result;
        }catch (Exception e) {
            e.printStackTrace();
        }
        remarkEventSession.close();
        return null;

    }

    public List<String> getListSubject(String classId) {
        try {
            remarkEventSession = HibernateUtil.getSessionFactory().openSession();

            String hql = "select r from RemarkEntity r where r.classId = '"+ classId +"' group by r.subjectId";
            Query query = remarkEventSession.createQuery(hql);
            List<RemarkEntity> list = query.getResultList();

            List<String> result = new ArrayList<>();
            for(int i = 0; i < list.size(); i++) {
                result.add(list.get(i).getSubjectId());
            }
            remarkEventSession.close();
            return result;
        }catch (Exception e) {
            e.printStackTrace();
        }
        remarkEventSession.close();
        return null;
    }

    public List<String> getListStudent(String classId, String subjectID) {
        try {
            remarkEventSession = HibernateUtil.getSessionFactory().openSession();

            String hql = "select r from RemarkEntity r where r.classId = '"+ classId +"' and r.subjectId='"+subjectID+"' group by r.studentId";
            Query query = remarkEventSession.createQuery(hql);
            List<RemarkEntity> list = query.getResultList();

            List<String> result = new ArrayList<>();
            for(int i = 0; i < list.size(); i++) {
                result.add(list.get(i).getStudentId());
            }
            remarkEventSession.close();
            return result;
        }catch (Exception e) {
            e.printStackTrace();
        }
        remarkEventSession.close();
        return null;
    }

    public List getRemark(String classID, String subjectID, String studentID, int type) {
        try {
            remarkEventSession = HibernateUtil.getSessionFactory().openSession();

            String hql = "select r, s.name, c.name from RemarkEntity r, StudentsEntity s, SubjectsEntity c where r.type="+type+" and s.studentId = r.studentId and c.subjectId=r.subjectId and r.classId = '"+ classID +"' and r.subjectId='"+subjectID+"' and r.studentId='"+ studentID +"' group by r.studentId";
            Query query = remarkEventSession.createQuery(hql);
            List list = query.getResultList();
            remarkEventSession.close();
            if(list.size() == 0) {
                return null;
            }
            return list;
        }catch (Exception e) {
            e.printStackTrace();
        }
        remarkEventSession.close();
        return null;
    }

    public String updateRemarkStatus(String classID, String subjectID, String studentID, int status, int type) {
        try {
            remarkEventSession = HibernateUtil.getSessionFactory().openSession();

            String hql = "select r from RemarkEntity r  where r.type="+type+" and r.classId='"+classID+"' and r.subjectId='"+subjectID+"' and r.studentId='"+studentID+"'";
            Query query = remarkEventSession.createQuery(hql);
            List<RemarkEntity> result = query.getResultList();

            if(result.size() == 0) {
                remarkEventSession.close();
                return "Cập nhật lỗi";
            }
            RemarkEntity remarkEntity = result.get(0);
            Transaction transaction = null;
            transaction = remarkEventSession.beginTransaction();

            remarkEntity.setStatus(status);

            remarkEventSession.update(remarkEntity);

            transaction.commit();

            remarkEventSession.close();

            return "Cập nhật thành công!";
        }catch (Exception e) {
            e.printStackTrace();
        }
        remarkEventSession.close();
        return null;
    }
}
