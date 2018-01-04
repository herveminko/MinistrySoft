package jw.ministry.soft.modules.data.dao;

// Generated 25 août 2014 22:09:51 by Hibernate Tools 4.3.1

import static org.hibernate.criterion.Example.create;

import java.util.List;

import jw.ministry.soft.modules.data.dto.Territory;
import jw.ministry.soft.modules.utils.HibernateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Home object for domain model class Territory.
 * @see jw.ministry.soft.modules.data.dao.Territory
 * @author Hibernate Tools
 */
public class TerritoryHome {

	private static final Log log = LogFactory.getLog(TerritoryHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		return HibernateUtil.getSessionFactory();
	}

	public void persist(Territory transientInstance) {
		log.debug("persisting Territory instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void persist(Session session, Territory transientInstance) {
		log.debug("persisting Territory instance");
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

	public void update(Session session, Territory transientInstance) {
		log.debug("updating Territory instance");
		Transaction trans = null;
		try {
			 trans=session.beginTransaction();
			session.update(transientInstance);
			trans.commit();

			log.debug("updating successful");
		} catch (RuntimeException re) {
			trans.rollback();
			log.error("updating failed", re);
			throw re;
		}
	}

	public void persist(Session session, List<Territory> transientInstances) {
		log.debug("persisting Territory instance");
		Transaction trans = null;
		try {
			 trans=session.beginTransaction();
			 for (Territory transientInstance: transientInstances) {
				 session.persist(transientInstance);
			 }
			trans.commit();

			log.debug("persist successful");
		} catch (RuntimeException re) {
			trans.rollback();
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Territory instance) {
		log.debug("attaching dirty Territory instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Territory instance) {
		log.debug("attaching clean Territory instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Territory persistentInstance) {
		log.debug("deleting Territory instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Territory merge(Territory detachedInstance) {
		log.debug("merging Territory instance");
		try {
			Territory result = (Territory) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Territory findById(java.lang.Integer id) {
		log.debug("getting Territory instance with id: " + id);
		try {
			Territory instance = (Territory) sessionFactory.getCurrentSession()
					.get("jw.ministry.soft.modules.data.dao.Territory", id);
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

	public List<Territory> findByExample(Territory instance) {
		log.debug("finding Territory instance by example");
		try {
			List<Territory> results = (List<Territory>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"jw.ministry.soft.modules.data.dao.Territory")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<Territory> findByExample(Session session, Territory instance) {
		log.debug("finding Territory instance by example");
		try {
			List<Territory> results = (List<Territory>) session
					.createCriteria(
							"jw.ministry.soft.modules.data.dto.Territory")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<Territory> getAllTerritories(Session session) {
		log.debug("finding all Territory instances");
		try {

			Query query = session.createQuery("from Territory");
			List<Territory> results = query.list();
			log.debug("get All territories successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<Territory> getAllAssignedTerritories(Session session) {
		log.debug("finding all Territory instances");
		try {

			Query query = session.createQuery("from Territory where Territory.territoriesassignmentses NOT");
			List<Territory> results = query.list();
			log.debug("get All territories successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

}
