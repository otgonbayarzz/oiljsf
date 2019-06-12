package db;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import model.*;

public class HibernateUtil {

	private static SessionFactory sessionFactory = null;

	static {
		try {
			loadSessionFactory();
		} catch (Exception e) {
			System.err.println("Exception while initializing hibernate util.. ");
			e.printStackTrace();
		}
	}

	public static void loadSessionFactory() {
		if (sessionFactory == null) {

			Configuration configuration = new Configuration();
			configuration.configure("..//db//hibernate.cfg.xml");

			configuration.addAnnotatedClass(User.class);
			configuration.addAnnotatedClass(Arm.class);
			configuration.addAnnotatedClass(DeliveryOrder.class);
			configuration.addAnnotatedClass(LocationConfig.class);
			configuration.addAnnotatedClass(Product.class);
			configuration.addAnnotatedClass(Tank.class);
			configuration.addAnnotatedClass(TankArmMap.class);

			ServiceRegistry srvcReg = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
					.build();
			sessionFactory = configuration.buildSessionFactory(srvcReg);
		}
	}

	public static Session getSession() throws HibernateException {

		Session retSession = null;
		loadSessionFactory();
		try {
			retSession = sessionFactory.openSession();
		} catch (Throwable t) {
			System.err.println("Exception while getting session.. ");
			t.printStackTrace();
		}
		if (retSession == null) {
			System.err.println("session is discovered null");
		}

		return retSession;
	}
}
