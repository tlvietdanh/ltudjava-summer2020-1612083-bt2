package dao;

import model.AccountsEntity;
import model.StudentsEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.List;

public class StudentsDao {

    public static Session studentSession;


    public static boolean createStudent(String mssv, String name, int gender, String identyfy) {
        if(mssv.length() == 0) {
            return false;
        }
        studentSession = HibernateUtil.getSessionFactory().openSession();

        String checkStudent = "FROM StudentsEntity a Where a.studentId= '" + mssv + "'";
        Query query = studentSession.createQuery(checkStudent);


        if(query.getResultList().size() > 0) {
            System.out.println("Tai khoan da ton tai!");
            return false;
        }


        String hql = "FROM AccountsEntity a Where a.username='" + mssv + "'";
        Query checkAccount = studentSession.createQuery(hql);
        List<AccountsEntity> account = checkAccount.getResultList();
        if(account.size() == 0) {
            int wait = -1;
            wait = AccountsDao.createAccount(mssv, mssv, 0);
            while (true) {
                if(wait != -1) {
                    break;
                }
            }
        }
        account = checkAccount.getResultList();

        Transaction transaction = null;
        try {
            transaction = studentSession.beginTransaction();

            StudentsEntity student = new StudentsEntity();
            student.setStudentId(mssv);
            student.setAccountId(account.get(0).getAccountId());
            student.setName(name);
            student.setGender(gender);
            student.setIdentifyId(identyfy);


            studentSession.save(student);

            transaction.commit();
            System.out.println("Created Student " + mssv);
            studentSession.close();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        studentSession.close();
        return false;
    }

    private static List<StudentsEntity> getStudents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List result = session.createQuery("from StudentsEntity ", StudentsEntity.class).list();
            session.close();
            return result;
        }
    }
}
