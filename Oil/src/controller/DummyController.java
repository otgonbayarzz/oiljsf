package controller;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

@SessionScoped
@ManagedBean(name = "dmController")
public class DummyController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int i = 0;

	public DummyController() {
		super();
	}

	public void start() {
		PrimeFaces.current().executeScript("PF('poll').start();");
		PrimeFaces.current().executeScript("PF('spinner').getJQ().show();");

	}

	public void stop() {
		PrimeFaces.current().executeScript("PF('poll').stop();");
		PrimeFaces.current().executeScript("PF('spinner').getJQ().hide();");
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

}
