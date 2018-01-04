package jw.ministry.soft.modules.data.dao;

// Generated 24 févr. 2015 21:08:02 by Hibernate Tools 4.3.1

import static org.hibernate.criterion.Example.create;

import java.util.List;

import jw.ministry.soft.modules.data.dto.Interested;
import jw.ministry.soft.modules.utils.HibernateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;

/**
 * Home object for domain model class Interested.
 * @see jw.ministry.soft.modules.data.dao2.Interested
 * @author Hibernate Tools
 */
public class InterestedHome {

	private static final Log log = LogFactory.getLog(InterestedHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		return HibernateUtil.getSessionFactory();
	}

	public void persist(Interested transientInstance) {
		log.debug("persisting Interested instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Interested instance) {
		log.debug("attaching dirty Interested instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Interested instance) {
		log.debug("attaching clean Interested instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Interested persistentInstance) {
		log.debug("deleting Interested instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Interested merge(Interested detachedInstance) {
		log.debug("merging Interested instance");
		try {
			Interested result = (Interested) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Interested findById(java.lang.Integer id) {
		log.debug("getting Interested instance with id: " + id);
		try {
			Interested instance = (Interested) sessionFactory
					.getCurrentSession()
					.get("jw.ministry.soft.modules.data.dao2.Interested", id);
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

	public List<Interested> findByExample(Interested instance) {
		log.debug("finding Interested instance by example");
		try {
			List<Interested> results = (List<Interested>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"jw.ministry.soft.modules.data.dao2.Interested")
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
