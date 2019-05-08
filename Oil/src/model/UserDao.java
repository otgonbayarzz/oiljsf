package model;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import db.HibernateUtil;

public class UserDao {

	public List<User> getUserList() {

		Session session = null;
		List<User> ulist = null;
		try {
			session = HibernateUtil.getSession();
			String queryStr = "select user from User user";
			Query query = session.createQuery(queryStr);
			ulist = query.list();
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
		return ulist;
	}

	public User getUserById(long id) {
		Session session = null;
		User user = null;
		try {
			session = HibernateUtil.getSession();
			user = (User) session.get(User.class, id);

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
		return user;

	}

	public void insertUser(User user) {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			session.save(user);
			System.out.println("inserted employee: " + user.getUserName());
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

	public void deleteUser(User user) {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			session.delete(user);
			transaction.commit();
			System.out.println("deleted employee: " + user.getUserName());
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
