package cn.net.sinodata.cm.hibernate.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

public abstract class GenericDao<T> implements IGenericDao<T> {

	private Class<T> entityClass;

	public GenericDao(Class<T> clazz) {
		this.entityClass = clazz;
	}

	@Resource
	protected SessionFactory sessionFactory;

	@Override
	public void save(T t) {
		sessionFactory.getCurrentSession().saveOrUpdate(t);
	}
	
	@Override
	public void save(List<T> list) {
		for (Object t : list) {
			sessionFactory.getCurrentSession().saveOrUpdate(t);
		}
		
	}
	
	@Override
	public void insert(T t) {
		sessionFactory.getCurrentSession().save(t);
	}

	@Override
	public void delete(T t) {
		sessionFactory.getCurrentSession().delete(t);
	}

	@Override
	public void delete(List<T> list) {
		for (T t : list) {
			sessionFactory.getCurrentSession().delete(t);
		}
	}
	
	@Override
	public void update(T t) {
		sessionFactory.getCurrentSession().update(t);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T queryById(String id) {
		return (T) sessionFactory.getCurrentSession().get(entityClass, id);
	}

	@Override
	public List<T> queryAll() {
		String hql = "from " + entityClass.getSimpleName();
		return queryForList(hql, null);
	}

	@SuppressWarnings("unchecked")
	public T queryForObject(String hql, Object[] params) {
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		setQueryParams(query, params);
		return (T) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public T queryForTopObject(String hql, Object[] params) {
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		setQueryParams(query, params);
		return (T) query.setFirstResult(0).setMaxResults(1).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<T> queryForList(String hql, Object[] params) {
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		setQueryParams(query, params);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> queryForList(final String hql, final Object[] params,
			final int recordNum) {
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		setQueryParams(query, params);
		return query.setFirstResult(0).setMaxResults(recordNum).list();
	}

	protected void setQueryParams(Query query, Object[] params) {
		if (null == params) {
			return;
		}
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
	}
	
	public void evict(Object obj){
		sessionFactory.getCurrentSession().evict(obj);
	}

}
