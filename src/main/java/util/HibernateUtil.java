package util;

import model.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        Properties settings = new Properties();

        settings.put(Environment.USER, "root");
        settings.put(Environment.PASS, "13101998");

        configuration.addProperties(settings);
        configuration.addAnnotatedClass(AccountsEntity.class);
        configuration.addAnnotatedClass(ClassesEntity.class);
        configuration.addAnnotatedClass(StudentsEntity.class);
        configuration.addAnnotatedClass(SubjectsEntity.class);
        configuration.addAnnotatedClass(SchedulesEntity.class);
        configuration.addAnnotatedClass(SpecialstudentsEntity.class);
        configuration.addAnnotatedClass(ScoresEntity.class);


        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        return sessionFactory;
    }
}

