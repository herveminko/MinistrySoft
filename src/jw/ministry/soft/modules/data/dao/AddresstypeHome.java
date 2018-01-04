package jw.ministry.soft.modules.data.dao;

// Generated 18 mai 2015 18:30:12 by Hibernate Tools 4.3.1

import static org.hibernate.criterion.Example.create;

import java.util.List;

import javax.naming.InitialContext;

import jw.ministry.soft.modules.data.dto.Addresstype;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;

/**
 * Home object for domain model class Addresstype.
 * @see jw.ministry.soft.modules.data.dao2.Addresstype
 * @author Hibernate Tools
 */
public class AddresstypeHome {

	private static final Log log = LogFactory.getLog(AddresstypeHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext()
					.lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Addresstype transientInstance) {
		log.debug("persisting Addresstype instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Addresstype instance) {
		log.debug("attaching dirty Addresstype instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Addresstype instance) {
		log.debug("attaching clean Addresstype instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Addresstype persistentInstance) {
		log.debug("deleting Addresstype instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Addresstype merge(Addresstype detachedInstance) {
		log.debug("merging Addresstype instance");
		try {
			Addresstype result = (Addresstype) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Addresstype findById(java.lang.Integer id) {
		log.debug("getting Addresstype instance with id: " + id);
		try {
			Addresstype instance = (Addresstype) sessionFactory
					.getCurrentSession().get(
							"jw.ministry.soft.modules.data.dao2.Addresstype",
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

	public List<Addresstype> findByExample(Addresstype instance) {
		log.debug("finding Addresstype instance by example");
		try {
			List<Addresstype> results = (List<Addresstype>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"jw.ministry.soft.modules.data.dao2.Addresstype")
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
