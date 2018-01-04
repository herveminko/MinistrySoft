package jw.ministry.soft.modules.data.dao;

// Generated 25 août 2014 22:09:51 by Hibernate Tools 4.3.1

import static org.hibernate.criterion.Example.create;

import java.util.List;

import jw.ministry.soft.modules.data.dto.Publisher;
import jw.ministry.soft.modules.utils.HibernateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Home object for domain model class Publisher.
 * @see jw.ministry.soft.modules.data.dto.Publisher
 * @author Hibernate Tools
 */
public class PublisherHome {

	private static final Log log = LogFactory.getLog(PublisherHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		return HibernateUtil.getSessionFactory();
	}

	public void persist(Publisher transientInstance) {
		log.debug("persisting Publisher instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void persist(Session session, Publisher transientInstance) {
		log.debug("persisting Publisher instance");
		Transaction trans = null;
		try {
			 trans=session.beginTransaction();
			session.persist(transientInstance);
			trans.commit();
			
			log.debug("persist successful");
		} catch (RuntimeException re) {
			trans.rollback();
			log.error("persist failed", re);
			throw re;
		} 
	}

	public void attachDirty(Publisher instance) {
		log.debug("attaching dirty Publisher instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Publisher instance) {
		log.debug("attaching clean Publisher instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Publisher persistentInstance) {
		log.debug("deleting Publisher instance");
		Session session = null;
		Transaction trans = null;
		try {
			
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			session.delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		} finally {
			trans.commit();
			session.close();
		}
	}

	public Publisher merge(Publisher detachedInstance) {
		log.debug("merging Publisher instance");
		try {
			Publisher result = (Publisher) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public Publisher findById(Session session, java.lang.Integer id) {
		log.debug("getting Publisher instance with id: " + id);
		try {
			Publisher instance = (Publisher) session
					.get("jw.ministry.soft.modules.data.dto.Publisher", id);
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

	public Publisher findById(java.lang.Integer id) {
		log.debug("getting Publisher instance with id: " + id);
		try {
			Publisher instance = (Publisher) sessionFactory.getCurrentSession()
					.get("jw.ministry.soft.modules.data.dto.Publisher", id);
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

	public List<Publisher> findByExample(Publisher instance) {
		log.debug("finding Publisher instance by example");
		try {
			List<Publisher> results = (List<Publisher>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"jw.ministry.soft.modules.data.dto.Publisher")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	public List<Publisher> findByExample(Session session, Publisher instance) {
		log.debug("finding Publisher instance by example");
		try {
			List<Publisher> results = (List<Publisher>) session
					.createCriteria(
							"jw.ministry.soft.modules.data.dto.Publisher")
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
