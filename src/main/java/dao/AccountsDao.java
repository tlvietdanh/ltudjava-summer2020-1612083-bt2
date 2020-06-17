package dao;

import model.AccountsEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class AccountsDao {

    public static Session accountSession;

    public AccountsDao() {
        // create giao vu va student admin
        // createAccount("giaovu", "giaovu", 1);
        // createAccount("1842001", "1842001", 0);
    }

    public static int createAccount(String username, String password, int userType) {
        if(username.length() == 0 || username.length() == 0) {
            return 0;
        }
        accountSession = HibernateUtil.getSessionFactory().openSession();

        String hql = "FROM AccountsEntity a Where a.username= '" + username + "'";
        Query query = accountSession.createQuery(hql);

        if(query.getResultList().size() > 0) {
            System.out.println("Tai khoan da ton tai!");
            return 0;
        }

        //create account instance
        AccountsEntity accounts = new AccountsEntity();
        accounts.setUsername(username);
        accounts.setPassword(password);
        accounts.setUsertype(userType);

        Transaction transaction = null;
        try {
            accounts.setPassword(hashMD5Password(accounts.getPassword()));
            transaction = accountSession.beginTransaction();

            accountSession.save(accounts);

            transaction.commit();
            System.out.println("Created Account " + username);
            accountSession.close();
            return 1;
        }catch (Exception e) {
            e.printStackTrace();
        }
        accountSession.close();
        return 0;
    }

    private static List<AccountsEntity > getAccounts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List result = session.createQuery("from AccountsEntity ", AccountsEntity.class).list();
            session.close();
            return result;
        }
    }

    private static String hashMD5Password(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte[] digest = md.digest();

        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }

    public static boolean login(String username, String password) throws NoSuchAlgorithmException {
        List<AccountsEntity> accounts = getAccounts();

        for (int i = 0; i < accounts.size(); i++) {
            AccountsEntity a = accounts.get(i);
            if(username.equals(a.getUsername()) && hashMD5Password(password).equals(a.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public static boolean changePassword(String oldPassword, String newPassword) {
        try {
            accountSession = HibernateUtil.getSessionFactory().openSession();

            String hashOldPass = hashMD5Password(oldPassword);
            String hql = "SELECT a from AccountsEntity a WHERE a.password=''";
            Query query = accountSession.createQuery(hql);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
