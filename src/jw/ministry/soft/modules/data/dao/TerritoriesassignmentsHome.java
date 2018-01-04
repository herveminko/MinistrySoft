package jw.ministry.soft.modules.data.dao;

// Generated 18 mai 2015 18:30:12 by Hibernate Tools 4.3.1

import static org.hibernate.criterion.Example.create;

import java.util.List;

import jw.ministry.soft.modules.data.dto.Congregation;
import jw.ministry.soft.modules.data.dto.Territoriesassignments;
import jw.ministry.soft.modules.utils.HibernateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Home object for domain model class Territoriesassignments.
 * @see jw.ministry.soft.modules.data.dto.Territoriesassignments
 * @author Hibernate Tools
 */
public class TerritoriesassignmentsHome {

	private static final Log log = LogFactory
			.getLog(TerritoriesassignmentsHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	private static SessionFactory getSessionFactory() {
		return HibernateUtil.getSessionFactory();
	}

	public void persist(Territoriesassignments transientInstance) {
		log.debug("persisting Territoriesassignments instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void persist(Session session, Territoriesassignments transientInstance) {
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

	public void attachDirty(Territoriesassignments instance) {
		log.debug("attaching dirty Territoriesassignments instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Territoriesassignments instance) {
		log.debug("attaching clean Territoriesassignments instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Territoriesassignments persistentInstance) {
		log.debug("deleting Territoriesassignments instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	public void delete(Session session, Territoriesassignments persistentInstance) {
		log.debug("deleting Territoriesassignments instance");
		Transaction trans = null;
		try {
			
			trans = session.beginTransaction();
			session.delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		} finally {
			trans.commit();
		}
	}

	public Territoriesassignments merge(Territoriesassignments detachedInstance) {
		log.debug("merging Territoriesassignments instance");
		try {
			Territoriesassignments result = (Territoriesassignments) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Territoriesassignments findById(java.lang.Integer id) {
		log.debug("getting Territoriesassignments instance with id: " + id);
		try {
			Territoriesassignments instance = (Territoriesassignments) sessionFactory
					.getCurrentSession()
					.get("jw.ministry.soft.modules.data.dao.Territoriesassignments",
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

	public List<Territoriesassignments> findByExample(
			Territoriesassignments instance) {
		log.debug("finding Territoriesassignments instance by example");
		try {
			List<Territoriesassignments> results = (List<Territoriesassignments>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"jw.ministry.soft.modules.data.dto.Territoriesassignments")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	public List<Territoriesassignments> findByExample(Session session, Territoriesassignments instance) {
		log.debug("finding Territoriesassignments instance by example");
		try {
			List<Territoriesassignments> results = (List<Territoriesassignments>) session
					.createCriteria(
							"jw.ministry.soft.modules.data.dto.Territoriesassignments")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	public List<Territoriesassignments> getAllTerritoriesAssignments(Session session) {
		log.debug("finding all Territory Assignments instances");
		try {

			Query query = session.createQuery("from Territoriesassignments");
			List<Territoriesassignments> results = query.list();
			log.debug("getAll Territoriesassignments successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
