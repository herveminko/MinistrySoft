package jw.ministry.soft.modules.data.dao;

// Generated 25 août 2014 22:09:51 by Hibernate Tools 4.3.1

import static org.hibernate.criterion.Example.create;

import java.util.List;

import jw.ministry.soft.modules.data.dto.PublisherPrivilege;
import jw.ministry.soft.modules.data.dto.PublisherPrivilegeId;
import jw.ministry.soft.modules.utils.HibernateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Home object for domain model class PublisherPrivilege.
 * @see jw.ministry.soft.modules.data.dao.PublisherPrivilege
 * @author Hibernate Tools
 */
public class PublisherPrivilegeHome {

	private static final Log log = LogFactory
			.getLog(PublisherPrivilegeHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		return HibernateUtil.getSessionFactory();
	}

	public void persist(PublisherPrivilege transientInstance) {
		log.debug("persisting PublisherPrivilege instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void persist(Session session, PublisherPrivilege transientInstance) {
		log.debug("persisting PublisherPrivilege instance");
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
	
	public void attachDirty(PublisherPrivilege instance) {
		log.debug("attaching dirty PublisherPrivilege instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PublisherPrivilege instance) {
		log.debug("attaching clean PublisherPrivilege instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void deleteOld(PublisherPrivilege persistentInstance) {
		log.debug("deleting PublisherPrivilege instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	
	public void delete(PublisherPrivilege persistentInstance) {
		log.debug("deleting PublisherPrivilege instance");
		Session session = null;
		Transaction trans = null;
		try {
			
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			session.delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		} finally {
			trans.commit();
			session.close();
		}
	}
	
	public void delete(Session session, PublisherPrivilege persistentInstance) {
		log.debug("deleting PublisherPrivilege instance");
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

	public PublisherPrivilege merge(PublisherPrivilege detachedInstance) {
		log.debug("merging PublisherPrivilege instance");
		try {
			PublisherPrivilege result = (PublisherPrivilege) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PublisherPrivilege findById(
			PublisherPrivilegeId id) {
		log.debug("getting PublisherPrivilege instance with id: " + id);
		try {
			PublisherPrivilege instance = (PublisherPrivilege) sessionFactory
					.getCurrentSession()
					.get("jw.ministry.soft.modules.data.dao.PublisherPrivilege",
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

	public List<PublisherPrivilege> findByExample(PublisherPrivilege instance) {
		log.debug("finding PublisherPrivilege instance by example");
		try {
			List<PublisherPrivilege> results = (List<PublisherPrivilege>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"jw.ministry.soft.modules.data.dao.PublisherPrivilege")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	public List<PublisherPrivilege> findByExample(Session session, PublisherPrivilege instance) {
		log.debug("finding PublisherPrivilege instance by example");
		try {
			List<PublisherPrivilege> results = (List<PublisherPrivilege>) session
					.createCriteria(
							"jw.ministry.soft.modules.data.dto.PublisherPrivilege")
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
