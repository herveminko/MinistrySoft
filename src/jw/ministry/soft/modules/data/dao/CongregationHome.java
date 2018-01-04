package jw.ministry.soft.modules.data.dao;

// Generated 18 févr. 2015 23:09:47 by Hibernate Tools 4.3.1

import static org.hibernate.criterion.Example.create;

import java.util.List;

import jw.ministry.soft.modules.data.dto.Congregation;
import jw.ministry.soft.modules.utils.HibernateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Home object for domain model class Congregation.
 *
 * @see jw.ministry.soft.modules.data.dao.Congregation
 * @author Hibernate Tools
 */
public class CongregationHome {

	private static final Log log = LogFactory.getLog(CongregationHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	private static SessionFactory getSessionFactory() {
		return HibernateUtil.getSessionFactory();
	}

	/**
	 * Insert a new congregation record.
	 *
	 * @param transientInstance
	 *            is the congregation record to insert.
	 */
	public void persist(Congregation transientInstance) {
		log.debug("persisting Congregation instance");
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

	/**
	 * Insert a new congregation record.
	 *
	 * @param transientInstance
	 *            is the congregation record to insert.
	 */
	public void persist(Session session, Congregation transientInstance) {
		log.debug("persisting Congregation instance");
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


	public void attachDirty(Congregation instance) {
		log.debug("attaching dirty Congregation instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Congregation instance) {
		log.debug("attaching clean Congregation instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Congregation persistentInstance) {
		log.debug("deleting Congregation instance");
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

	public Congregation merge(Congregation detachedInstance) {
		log.debug("merging Congregation instance");
		try {
			Congregation result = (Congregation) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Congregation findById(java.lang.Integer id) {
		log.debug("getting Congregation instance with id: " + id);
		try {
			Congregation instance = (Congregation) sessionFactory
					.getCurrentSession().get(
							"jw.ministry.soft.modules.data.dto.Congregation",
							id);
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

	public Congregation findById(Session session, java.lang.Integer id) {
		log.debug("getting Congregation instance with id: " + id);
		try {
			Congregation instance = (Congregation) session.get(
							"jw.ministry.soft.modules.data.dto.Congregation",
							id);
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

	public List<Congregation> findByExample(Congregation instance) {
		log.debug("finding Congregation instance by example");
		try {
			List<Congregation> results = (List<Congregation>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"jw.ministry.soft.modules.data.dto.Congregation")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<Congregation> findByExample(Session session, Congregation instance) {
		log.debug("finding Congregation instance by example");
		try {
			List<Congregation> results = (List<Congregation>) session
					.createCriteria(
							"jw.ministry.soft.modules.data.dto.Congregation")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<Congregation> getAllCongregations(Session session) {
		log.debug("finding all Congregation instances");
		try {

			Query query = session.createQuery("from Congregation");
			List<Congregation> results = query.list();
			log.debug("getAll Congregations successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
