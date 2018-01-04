package jw.ministry.soft.modules.data.dao;

// Generated 18 mai 2015 18:30:12 by Hibernate Tools 4.3.1

import static org.hibernate.criterion.Example.create;

import java.util.List;

import jw.ministry.soft.modules.data.dto.Publishinggroup;
import jw.ministry.soft.modules.utils.HibernateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Home object for domain model class Publishinggroup.
 * @see jw.ministry.soft.modules.data.dao2.Publishinggroup
 * @author Hibernate Tools
 */
public class PublishinggroupHome {

	private static final Log log = LogFactory.getLog(PublishinggroupHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		return HibernateUtil.getSessionFactory();
	}
	
	public void persist(Publishinggroup transientInstance) {
		log.debug("persisting Publishinggroup instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void persist(Session session, Publishinggroup transientInstance) {
		log.debug("persisting Publishinggroup instance");
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

	public void attachDirty(Publishinggroup instance) {
		log.debug("attaching dirty Publishinggroup instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Publishinggroup instance) {
		log.debug("attaching clean Publishinggroup instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Publishinggroup persistentInstance) {
		log.debug("deleting Publishinggroup instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Publishinggroup merge(Publishinggroup detachedInstance) {
		log.debug("merging Publishinggroup instance");
		try {
			Publishinggroup result = (Publishinggroup) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Publishinggroup findById(java.lang.Integer id) {
		log.debug("getting Publishinggroup instance with id: " + id);
		try {
			Publishinggroup instance = (Publishinggroup) sessionFactory
					.getCurrentSession()
					.get("jw.ministry.soft.modules.data.dao2.Publishinggroup",
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

	public List<Publishinggroup> findByExample(Publishinggroup instance) {
		log.debug("finding Publishinggroup instance by example");
		try {
			List<Publishinggroup> results = (List<Publishinggroup>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"Publishinggroup")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	public List<Publishinggroup> findByExample(Session session, Publishinggroup instance) {
		log.debug("finding Publishinggroup instance by example");
		try {
			List<Publishinggroup> results = (List<Publishinggroup>) session
					.createCriteria(
							"jw.ministry.soft.modules.data.dto.Publishinggroup")
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
