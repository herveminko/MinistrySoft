package jw.ministry.soft.modules.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate utility class.
 * 
 * @author Hervé Minko.
 *
 */
public class HibernateUtil {

	private static final SessionFactory sessionFactory = buildSessionFactory();

	/**
	 * Build a new hibernate session factory.
	 * 
	 * @return the SessionFactory instance.
	 */
	@SuppressWarnings("deprecation")
	private static SessionFactory buildSessionFactory() {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			return new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		if (sessionFactory != null) {
			try {
				// Close caches and connection pools
				getSessionFactory().close();
			} catch (NullPointerException npe) {

			}
		}
	}

}