package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.PrimeFaces;

import db.CustomDao;
import model.Bay;
import model.User;

@SessionScoped
@ManagedBean(name = "adminController")

public class AdminController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Bay> bays;
	private List<User> users;
	private CustomDao cursor = new CustomDao();
	private User cursorUser = new User();

	public AdminController() {
		super();
	}

	public void initData() {
		try {
			getBays().clear();
			for (Object o : cursor.getList(new Bay())) {
				Bay b = (Bay) o;
				this.bays.add(b);
			}
			getUsers().clear();
			for (Object ob : cursor.getList(new User())) {
				User u = (User) ob;
				this.users.add(u);
			}

			PrimeFaces.current().ajax().update("form:userSection");
			PrimeFaces.current().ajax().update("form:baySection");

		} catch (Exception ex) {
			System.out.println("error occured while getting data initData..");
			ex.printStackTrace();
		}
	}

	public void saveBayConfig(Bay bay) {
		try {
			cursor.update(bay);
		} catch (Exception ex) {
			System.out.println("error occured while getting data saveBay..");
			ex.printStackTrace();
		}

	}

	public void saveUser(User user) {
		if (user.getId() == 0) {
			try {
				cursor.insert(user);
			} catch (Exception ex) {
				System.out.println("error occured while getting data saveUser..");
				ex.printStackTrace();
			}
		} else {
			try {

				cursor.update(user);
			} catch (Exception ex) {
				System.out.println("error occured while getting data saveUser..");
				ex.printStackTrace();
			}
		}
		PrimeFaces.current().ajax().update("form:userSection");
	}

	public void deleteUser(User user) {
		if (user.getId() != 0) {
			try {
				cursor.delete(user);
				initData();
			} catch (Exception ex) {
				System.out.println("error occured while getting data saveUser..");
				ex.printStackTrace();
			}

		}

	}

	public void addUser() {
		getUsers();
		User u = new User();
		users.add(u);
		PrimeFaces.current().ajax().update("form:userSection");
	}

	public List<Bay> getBays() {
		if (bays == null)
			bays = new ArrayList<Bay>();

		return bays;
	}

	public void setBays(List<Bay> bays) {
		this.bays = bays;
	}

	public List<User> getUsers() {
		if (users == null)
			users = new ArrayList<User>();
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public CustomDao getCursor() {
		if (cursor == null)
			cursor = new CustomDao();
		return cursor;
	}

	public void setCursor(CustomDao cursor) {
		this.cursor = cursor;
	}

	public User getCursorUser() {
		if (cursorUser == null)
			cursorUser = new User();
		return cursorUser;
	}

	public void setCursorUser(User cursorUser) {
		this.cursorUser = cursorUser;
	}

}
