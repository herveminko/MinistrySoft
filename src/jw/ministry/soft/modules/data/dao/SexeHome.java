package jw.ministry.soft.modules.data.dao;

// Generated 25 août 2014 22:09:51 by Hibernate Tools 4.3.1

import static org.hibernate.criterion.Example.create;

import java.util.List;

import jw.ministry.soft.modules.data.dto.Sexe;
import jw.ministry.soft.modules.utils.HibernateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Home object for domain model class Sexe.
 * @see jw.ministry.soft.modules.data.dto.Sexe
 * @author Hibernate Tools
 */
public class SexeHome {

	private static final Log log = LogFactory.getLog(SexeHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		return HibernateUtil.getSessionFactory();
	}

	public void persist(Sexe transientInstance) {
		log.debug("persisting Sexe instance");
		Session session = sessionFactory.openSession();
		Transaction trans = session.beginTransaction();
		try {			
			session.persist(transientInstance);
			trans.commit();
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		} finally {
			session.close();
		}
		
	}
	
	public void persist(Session session, Sexe transientInstance) {
		log.debug("persisting Sexe instance");
		Transaction trans = session.beginTransaction();
		try {
			session.persist(transientInstance);
			trans.commit();
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Sexe instance) {
		log.debug("attaching dirty Sexe instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Sexe instance) {
		log.debug("attaching clean Sexe instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Sexe persistentInstance) {
		log.debug("deleting Sexe instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Sexe merge(Sexe detachedInstance) {
		log.debug("merging Sexe instance");
		try {
			Sexe result = (Sexe) sessionFactory.getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Sexe findById(java.lang.Integer id) {
		log.debug("getting Sexe instance with id: " + id);
		try {
			Sexe instance = (Sexe) sessionFactory.getCurrentSession().get(
					"jw.ministry.soft.modules.data.dto.Sexe", id);
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

	public List<Sexe> findByExample(Sexe instance) {
		log.debug("finding Sexe instance by example");
		try {
			List<Sexe> results = (List<Sexe>) sessionFactory
					.getCurrentSession()
					.createCriteria("jw.ministry.soft.modules.data.dto.Sexe")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	public List<Sexe> findByExample(Session session, Sexe instance) {
		log.debug("finding Sexe instance by example");
		Transaction trans = session.beginTransaction();
		try {
			
			List<Sexe> results = (List<Sexe>) session
					.createCriteria("jw.ministry.soft.modules.data.dto.Sexe")
					.add(create(instance)).list();
			trans.commit();

			log.debug("find by example successful, result size: "
					+ results.size());
			
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		} 
	}
}
