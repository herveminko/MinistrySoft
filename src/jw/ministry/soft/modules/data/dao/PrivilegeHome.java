package jw.ministry.soft.modules.data.dao;

// Generated 25 août 2014 22:09:51 by Hibernate Tools 4.3.1

import static org.hibernate.criterion.Example.create;

import java.util.List;

import jw.ministry.soft.modules.data.dto.Privilege;
import jw.ministry.soft.modules.utils.HibernateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Home object for domain model class Privilege.
 * @see jw.ministry.soft.modules.data.dao.Privilege
 * @author Hibernate Tools
 */
public class PrivilegeHome {

	private static final Log log = LogFactory.getLog(PrivilegeHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		return HibernateUtil.getSessionFactory();
	}

	public void persist(Privilege transientInstance) {
		log.debug("persisting Privilege instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void persist(Session session, Privilege transientInstance) {
		log.debug("persisting Privilege instance");
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

	public void attachDirty(Privilege instance) {
		log.debug("attaching dirty Privilege instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Privilege instance) {
		log.debug("attaching clean Privilege instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Privilege persistentInstance) {
		log.debug("deleting Privilege instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	
	
	public Privilege merge(Privilege detachedInstance) {
		log.debug("merging Privilege instance");
		try {
			Privilege result = (Privilege) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Privilege findById(java.lang.Integer id) {
		log.debug("getting Privilege instance with id: " + id);
		try {
			Privilege instance = (Privilege) sessionFactory.getCurrentSession()
					.get("jw.ministry.soft.modules.data.dao.Privilege", id);
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

	public List<Privilege> findByExample(Privilege instance) {
		log.debug("finding Privilege instance by example");
		try {
			List<Privilege> results = (List<Privilege>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"jw.ministry.soft.modules.data.dao.Privilege")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	public List<Privilege> findByExample(Session session, Privilege instance) {
		log.debug("finding Privilege instance by example");
		try {
			List<Privilege> results = (List<Privilege>) session
					.createCriteria(
							"jw.ministry.soft.modules.data.dto.Privilege")
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
