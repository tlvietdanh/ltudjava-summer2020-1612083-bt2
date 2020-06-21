package dao;

import model.SpecialstudentsEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

public class SpecialStudentDao {
    Session specialSession;

    public String requestStudyAgains(String mssv, String classID, String subjectID, int type) {
        specialSession = HibernateUtil.getSessionFactory().openSession();

        // check is student exist in the class
        String hql = "FROM SpecialstudentsEntity c WHERE c.classId='"+classID+"' and c.studentId='"+mssv+"' and c.subjectId='"+subjectID +"'";
        Query query = specialSession.createQuery(hql);
        if(query.getResultList().size() > 0) {
            return "Dữ liệu đã tồn tại!";
        }
        Transaction transaction = null;
        try {
            SpecialstudentsEntity specialstudentsEntity = new SpecialstudentsEntity();
            specialstudentsEntity.setClassId(classID);
            specialstudentsEntity.setStudentId(mssv);
            specialstudentsEntity.setSubjectId(subjectID);
            specialstudentsEntity.setType(type);

            transaction = specialSession.beginTransaction();
            specialSession.save(specialstudentsEntity);
            transaction.commit();
            specialSession.close();
            return "Thêm thành công!";
        }catch (Exception e) {
            e.printStackTrace();
        }
        specialSession.close();
        return "Đã có lỗi xảy ra, xin vui lòng thử lại!";
    }
}
