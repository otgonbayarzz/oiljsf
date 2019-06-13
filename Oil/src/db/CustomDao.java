package db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import db.HibernateUtil;
import model.User;

public class CustomDao {

	public List<Object> getList(Object o) {

		Session session = null;

		String tableName = o.getClass().getName().replace("model.", "");

		List<Object> retList = null;

		try {
			session = HibernateUtil.getSession();
			StringBuilder sb = new StringBuilder();
			sb.append("select obj from ");
			sb.append(tableName);
			sb.append(" obj ");

			Query query = session.createQuery(sb.toString());
			retList = query.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			// handle exception here
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception ex) {
			}
		}
		return retList;
	}

	public List<Object> getListByQuery(Object o, String qry) {

		Session session = null;

		List<Object> retList = null;

		try {
			session = HibernateUtil.getSession();

			Query query = session.createQuery(qry);

			retList = query.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			// handle exception here
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception ex) {
			}
		}
		return retList;
	}

	public List<Object> getListByCustomQuery(Object o, String qry) {

		Session session = null;

		List<Object> retList = null;

		try {
			session = HibernateUtil.getSession();

			Query query = session.createSQLQuery(qry);

			retList = query.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			// handle exception here
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception ex) {
			}
		}
		return retList;
	}

	public Object getByById(int id, Object o) {
		Session session = null;
		Object retObj = null;
		try {
			session = HibernateUtil.getSession();
			retObj = session.get(o.getClass(), id);

		} catch (Exception ex) {
			ex.printStackTrace();
			// handle exception here
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception ex) {
			}
		}
		return retObj;

	}

	public void insert(Object o) {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();

			session.save(o);

			transaction.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			// handle exception here
			if (transaction != null)
				transaction.rollback();
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception ex) {
			}
		}
	}

	public void update(Object o) {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();

			session.update(o);

			transaction.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			// handle exception here
			if (transaction != null)
				transaction.rollback();
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception ex) {
			}
		}
	}

	public void delete(Object o) {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			session.delete(o);
			transaction.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			// handle exception here
			if (transaction != null)
				transaction.rollback();
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception ex) {
			}
		}
	}

}
