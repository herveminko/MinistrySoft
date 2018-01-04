package jw.ministry.soft.modules.data.dao;

// Generated 18 mai 2015 18:30:12 by Hibernate Tools 4.3.1

import static org.hibernate.criterion.Example.create;

import java.util.List;

import javax.naming.InitialContext;

import jw.ministry.soft.modules.data.dto.Territoryhistory;
import jw.ministry.soft.modules.utils.HibernateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Home object for domain model class Territoryhistory.
 * 
 * @see jw.ministry.soft.modules.data.dto.Territoryhistory
 * @author Hibernate Tools
 */
public class TerritoryhistoryHome {

	private static final Log log = LogFactory
			.getLog(TerritoryhistoryHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		return HibernateUtil.getSessionFactory();
	}


	public void persist(Territoryhistory transientInstance) {
		log.debug("persisting Territoryhistory instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void persist(Session session,
			List<Territoryhistory> transientInstances) {
		log.debug("persisting Territoryhistory instance");
		Transaction trans = session.beginTransaction();
		try {
			for (Territoryhistory transientInstance : transientInstances) {
				session.persist(transientInstance);
				log.debug("persist successful");
			}
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			trans.rollback();
			throw re;
		} finally {
			trans.commit();
		}
	}

	public void persist(Session session, Territoryhistory transientInstance) {
		log.debug("persisting Territoryhistory instance");
		Transaction trans = session.beginTransaction();
		try {
			session.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			trans.rollback();
			throw re;
		} finally {
			trans.commit();
		}
	}

	public void attachDirty(Territoryhistory instance) {
		log.debug("attaching dirty Territoryhistory instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Territoryhistory instance) {
		log.debug("attaching clean Territoryhistory instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Territoryhistory persistentInstance) {
		log.debug("deleting Territoryhistory instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Territoryhistory merge(Territoryhistory detachedInstance) {
		log.debug("merging Territoryhistory instance");
		try {
			Territoryhistory result = (Territoryhistory) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Territoryhistory findById(int id) {
		log.debug("getting Territoryhistory instance with id: " + id);
		try {
			Territoryhistory instance = (Territoryhistory) sessionFactory
					.getCurrentSession()
					.get("jw.ministry.soft.modules.data.dao2.Territoryhistory",
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

	public List<Territoryhistory> findByExample(Territoryhistory instance) {
		log.debug("finding Territoryhistory instance by example");
		try {
			List<Territoryhistory> results = (List<Territoryhistory>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"jw.ministry.soft.modules.data.dao2.Territoryhistory")
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
