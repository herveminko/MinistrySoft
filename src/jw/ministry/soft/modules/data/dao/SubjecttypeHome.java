package jw.ministry.soft.modules.data.dao;

// Generated 25 août 2014 22:09:51 by Hibernate Tools 4.3.1

import static org.hibernate.criterion.Example.create;

import java.util.List;

import jw.ministry.soft.modules.data.dto.Subjecttype;
import jw.ministry.soft.modules.utils.HibernateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;

/**
 * Home object for domain model class Subjecttype.
 * @see jw.ministry.soft.modules.data.dao.Subjecttype
 * @author Hibernate Tools
 */
public class SubjecttypeHome {

	private static final Log log = LogFactory.getLog(SubjecttypeHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		return HibernateUtil.getSessionFactory();
	}

	public void persist(Subjecttype transientInstance) {
		log.debug("persisting Subjecttype instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Subjecttype instance) {
		log.debug("attaching dirty Subjecttype instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Subjecttype instance) {
		log.debug("attaching clean Subjecttype instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Subjecttype persistentInstance) {
		log.debug("deleting Subjecttype instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Subjecttype merge(Subjecttype detachedInstance) {
		log.debug("merging Subjecttype instance");
		try {
			Subjecttype result = (Subjecttype) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Subjecttype findById(java.lang.Integer id) {
		log.debug("getting Subjecttype instance with id: " + id);
		try {
			Subjecttype instance = (Subjecttype) sessionFactory
					.getCurrentSession()
					.get("jw.ministry.soft.modules.data.dao.Subjecttype", id);
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

	public List<Subjecttype> findByExample(Subjecttype instance) {
		log.debug("finding Subjecttype instance by example");
		try {
			List<Subjecttype> results = (List<Subjecttype>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"jw.ministry.soft.modules.data.dao.Subjecttype")
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
