package jw.ministry.soft.modules.data.dao;

// Generated 25 août 2014 22:09:51 by Hibernate Tools 4.3.1

import static org.hibernate.criterion.Example.create;

import java.util.List;

import jw.ministry.soft.modules.data.dto.Ministryweek;
import jw.ministry.soft.modules.utils.HibernateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;

/**
 * Home object for domain model class Ministryweek.
 * @see jw.ministry.soft.modules.data.dao.Ministryweek
 * @author Hibernate Tools
 */
public class MinistryweekHome {

	private static final Log log = LogFactory.getLog(MinistryweekHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		return HibernateUtil.getSessionFactory();
	}

	public void persist(Ministryweek transientInstance) {
		log.debug("persisting Ministryweek instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Ministryweek instance) {
		log.debug("attaching dirty Ministryweek instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Ministryweek instance) {
		log.debug("attaching clean Ministryweek instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Ministryweek persistentInstance) {
		log.debug("deleting Ministryweek instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Ministryweek merge(Ministryweek detachedInstance) {
		log.debug("merging Ministryweek instance");
		try {
			Ministryweek result = (Ministryweek) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Ministryweek findById(java.lang.Integer id) {
		log.debug("getting Ministryweek instance with id: " + id);
		try {
			Ministryweek instance = (Ministryweek) sessionFactory
					.getCurrentSession().get(
							"jw.ministry.soft.modules.data.dao.Ministryweek",
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

	public List<Ministryweek> findByExample(Ministryweek instance) {
		log.debug("finding Ministryweek instance by example");
		try {
			List<Ministryweek> results = (List<Ministryweek>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"jw.ministry.soft.modules.data.dao.Ministryweek")
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
