package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import db.CustomDao;
import model.User;

@SessionScoped
@ManagedBean(name = "userController")
public class UserController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<User> users;
	private User user;
	CustomDao cursor = new CustomDao();

	public UserController() {
		super();
	}

	public boolean isLoggedIn() {
		if (this.getUser().getId() != 0)

			return true;

		else
			return false;
	}

	public String login() {
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT user  ");
		sb.append("FROM User user ");
		sb.append("WHERE user.userName = '");
		sb.append(getUser().getUserName());
		sb.append("' AND user.password = '");
		sb.append(getUser().getPassword());
		sb.append("' ");
		List<Object> ol = cursor.getListByQuery(new User(), sb.toString());
		if (ol != null && ol.size() > 0)
			for (Object o : cursor.getListByQuery(new User(), sb.toString())) {
				getUser();
				this.user = (User) o;
				  
				        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Logged in"));
				  
				     
				return "home";
			}
		return "";
	}

	public String logout() {
		this.user = new User();
		return "login";
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
			users = new ArrayList<User>();

		return users;

	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
