package controller;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ApplicationScoped
@ManagedBean(name = "appController")
public class ApplicationController  implements Serializable {
	private String alertMessage;


	public ApplicationController() {
		super();

	}
	
	public void showMessage(String msg)
	{
		this.alertMessage = msg;
		
	}
	public String getAlertMessage() {
		return alertMessage;
	}

	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;
	}

}
