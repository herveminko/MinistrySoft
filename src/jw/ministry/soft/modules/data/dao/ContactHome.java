package jw.ministry.soft.modules.data.dao;

// Generated 18 mai 2015 18:30:12 by Hibernate Tools 4.3.1

import static org.hibernate.criterion.Example.create;

import java.util.List;

import jw.ministry.soft.modules.data.dto.Contact;
import jw.ministry.soft.modules.utils.HibernateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Home object for domain model class Contact.
 * @see jw.ministry.soft.modules.data.dao2.Contact
 * @author Hibernate Tools
 */
public class ContactHome {

	private static final Log log = LogFactory.getLog(ContactHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	private static SessionFactory getSessionFactory() {
		return HibernateUtil.getSessionFactory();
	}

	public void persist(Contact transientInstance) {
		log.debug("persisting Contact instance");
		Session session = null;
		Transaction trans = null;
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			session.persist(transientInstance);
			trans.commit();

			log.debug("persist successful");
		} catch (RuntimeException re) {
			trans.rollback();
			log.error("persist failed", re);
			throw re;
		} finally {
			session.close();
		}
	}
	
	public void persist(Session session, Contact transientInstance) {
		log.debug("persisting Contact instance");
		Transaction trans = null;
		try {
			trans = session.beginTransaction();
			session.persist(transientInstance);
			trans.commit();

			log.debug("persist successful");
		} catch (RuntimeException re) {
			trans.rollback();
			log.error("persist failed", re);
			throw re;
		} 
	}

	public void attachDirty(Contact instance) {
		log.debug("attaching dirty Contact instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Contact instance) {
		log.debug("attaching clean Contact instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Contact persistentInstance) {
		log.debug("deleting Contact instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Contact merge(Contact detachedInstance) {
		log.debug("merging Contact instance");
		try {
			Contact result = (Contact) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Contact findById(java.lang.Integer id) {
		log.debug("getting Contact instance with id: " + id);
		try {
			Contact instance = (Contact) sessionFactory.getCurrentSession()
					.get("jw.ministry.soft.modules.data.dao2.Contact", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Contact> findByExample(Contact instance) {
		log.debug("finding Contact instance by example");
		try {
			List<Contact> results = (List<Contact>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"jw.ministry.soft.modules.data.dao2.Contact")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
