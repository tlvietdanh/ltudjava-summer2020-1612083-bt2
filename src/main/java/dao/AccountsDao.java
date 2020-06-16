package dao;

import model.AccountsEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class AccountsDao {

    public AccountsDao() {
        // create giao vu va student admin
        createAccount("giaovu", "giaovu", 1);
        createAccount("1842001", "1842001", 0);
    }

    public boolean createAccount(String username, String password, int userType) {
        if(username.length() == 0 || username.length() == 0 || checkExistedUsername(username)) {
            return false;
        }
        //create account instance
        AccountsEntity accounts = new AccountsEntity();
        accounts.setUsername(username);
        accounts.setPassword(password);
        accounts.setUsertype(userType);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            accounts.setPassword(hashMD5Password(accounts.getPassword()));
            transaction = session.beginTransaction();

            session.save(accounts);

            transaction.commit();
            System.out.println("Created Account " + username);
            return true;
        }catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    private static List<AccountsEntity > getAccounts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery("from AccountsEntity ", AccountsEntity.class).list();
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

    private boolean checkExistedUsername(String username) {
        List<AccountsEntity> accounts = getAccounts();

        for (int i = 0; i < accounts.size(); i++) {
            AccountsEntity a = accounts.get(i);
            if(username.equals(a.getUsername())) {
                return true;
            }
        }
        return false;
    }
}
