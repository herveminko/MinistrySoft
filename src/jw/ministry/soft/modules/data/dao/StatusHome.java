package jw.ministry.soft.modules.data.dao;

// Generated 25 août 2014 22:09:51 by Hibernate Tools 4.3.1

import static org.hibernate.criterion.Example.create;

import java.util.List;

import jw.ministry.soft.modules.data.dto.Status;
import jw.ministry.soft.modules.utils.HibernateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Home object for domain model class Status.
 * @see jw.ministry.soft.modules.data.dto.Status
 * @author Hibernate Tools
 */
public class StatusHome {

	private static final Log log = LogFactory.getLog(StatusHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		return HibernateUtil.getSessionFactory();
	}

	public void persist(Status transientInstance) {
		log.debug("persisting Status instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void persist(Session session, Status transientInstance) {
		log.debug("persisting Status instance");
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
	
	public void attachDirty(Status instance) {
		log.debug("attaching dirty Status instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Status instance) {
		log.debug("attaching clean Status instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Status persistentInstance) {
		log.debug("deleting Status instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Status merge(Status detachedInstance) {
		log.debug("merging Status instance");
		try {
			Status result = (Status) sessionFactory.getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Status findById(java.lang.Integer id) {
		log.debug("getting Status instance with id: " + id);
		try {
			Status instance = (Status) sessionFactory.getCurrentSession().get(
					"jw.ministry.soft.modules.data.dto.Status", id);
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

	public List<Status> findByExample(Status instance) {
		log.debug("finding Status instance by example");
		try {
			List<Status> results = (List<Status>) sessionFactory
					.getCurrentSession()
					.createCriteria("jw.ministry.soft.modules.data.dto.Status")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	public List<Status> findByExample(Session session, Status instance) {
		log.debug("finding Status instance by example");
		try {
			List<Status> results = session
					.createCriteria("jw.ministry.soft.modules.data.dto.Status")
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
