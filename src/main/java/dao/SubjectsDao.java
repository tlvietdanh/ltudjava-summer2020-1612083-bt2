package dao;

import model.SubjectsEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

public class SubjectsDao {
    public static Session subjectsSession;


    public static boolean createSubject(String subjectID, String name, String room) {
        if(subjectID.length() == 0) {
            return false;
        }
        subjectsSession = HibernateUtil.getSessionFactory().openSession();

        String hql = "FROM SubjectsEntity s Where s.subjectId= '" + subjectID + "'";
        Query query = subjectsSession.createQuery(hql);

        if(query.getResultList().size() > 0) {
            System.out.println("Mon hoc da ton tai!");
            return false;
        }

        //create account instance
        SubjectsEntity subject = new SubjectsEntity();
        subject.setSubjectId(subjectID);
        subject.setName(name);
        subject.setRoom(room);

        Transaction transaction = null;
        try {
            transaction = subjectsSession.beginTransaction();

            subjectsSession.save(subject);

            transaction.commit();
            System.out.println("Created Subject " + subjectID);
            subjectsSession.close();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        subjectsSession.close();
        return false;
    }
}
