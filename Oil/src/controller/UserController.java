package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import model.User;
import model.UserDao;

@ViewScoped
@ManagedBean(name = "userController")
public class UserController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<User> users;
	private User user;
	UserDao ud = new UserDao();

	public String add() {
		String ret = "home";

		ud.insertUser(this.user);
		users = ud.getUserList();
		return "";

	}

	public User getUser() {
		if (user == null)
			user = new User();
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<User> getUsers() {
		if (users == null)
			users = ud.getUserList();

		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
