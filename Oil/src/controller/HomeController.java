package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import model.Device;

@ViewScoped
@ManagedBean(name = "homeController")
public class HomeController implements Serializable {

	public HomeController() {
		super();
	}

	private List<Device> deviceList;
	private String tempIp;
	private int tempPort;
	private String tempCommand;

	public void addDevice() {
		Device d = new Device(this.tempIp, this.tempPort);
		deviceList.add(d);
		this.tempIp = "";
		this.tempPort = 0;

	}
	
	public void giveCommand(Device d)
	{
		try {
			d.giveCommand(tempCommand);
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public List<Device> getDeviceList() {
		if (deviceList == null)
			deviceList = new ArrayList<Device>();

		return deviceList;
	}

	public void setDeviceList(List<Device> deviceList) {
		this.deviceList = deviceList;
	}

	public String getTempIp() {
		if (tempIp == null)
			tempIp = "";
		return tempIp;
	}

	public void setTempIp(String tempIp) {
		this.tempIp = tempIp;
	}

	public int getTempPort() {
		return tempPort;
	}

	public void setTempPort(int tempPort) {
		this.tempPort = tempPort;
	}

	public String getTempCommand() {
		if(tempCommand == null)
			tempCommand = "";
		return tempCommand;
	}

	public void setTempCommand(String tempCommand) {
		this.tempCommand = tempCommand;
	}
	
	

}
