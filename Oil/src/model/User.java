package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "User")
public class User {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(name = "userName")
	private String userName;

	@Column(name = "password")
	private String password;

	@Column(name = "adminStatus")
	private int adminStatus;

	@Transient
	private boolean admin;

	public User() {
		super();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAdminStatus() {
		return adminStatus;
	}

	public void setAdminStatus(int adminStatus) {
		this.adminStatus = adminStatus;
	}

	public boolean isAdmin() {
		if (this.adminStatus == 1)
			this.admin = true;
		else
			this.admin = false;
		return admin;
	}

	public void setAdmin(boolean admin) {
		if (admin)
			this.adminStatus = 1;
		else
			this.adminStatus = 0;
		this.admin = admin;
	}

}
