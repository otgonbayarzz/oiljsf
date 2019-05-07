package controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import model.User;


@ManagedBean(name = "userController")
public class UserController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;

	public String login() {
		String ret = "";
		if (user.getUserName().equals("admin") && user.getPassword().equals("admin")) {
			System.out.print("SUCCESS");
			ret =  "home";
		}
		return ret;

	}

	public User getUser() {
		if (user == null)
			user = new User();
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
