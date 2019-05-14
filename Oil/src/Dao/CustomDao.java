package Dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import db.HibernateUtil;


public class CustomDao {

	public List<Object> getList(Object object) {

		Session session = null;
		
		String tableName = object.getClass().getName().replace("model.", "");
		
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

}
