package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

import db.CustomDao;
import model.Tank;
import model.Device;
import model.OrderDtl;

@ViewScoped
@ManagedBean(name = "homeController")
public class HomeController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CustomDao cursor = new CustomDao();
	private List<Tank> bays;
	private List<Device> deviceList;

	private String tempIp;
	private int tempPort;
	private String tempCommand;
	public Date dd = new Date();

	public HomeController() {
		super();
	}

	public void initData() {
		try {

			getBays();
			getBays().clear();
			for (Object o : cursor.getList(new Tank())) {
				Tank b = (Tank) o;
				this.bays.add(b);
			}

			for (Tank b : bays) {
				b.getOrders().clear();
				StringBuilder sb = new StringBuilder();
				sb.append("SELECT od ");
				sb.append("from OrderDtl od ");
				sb.append("WHERE od.productId =  ");
				sb.append(b.getProductId());
				
				sb.append("  ");
				for (Object o : cursor.getListByQuery(new OrderDtl(), sb.toString())) {
					OrderDtl od = (OrderDtl) o;
					b.getOrders().add(od);
				}

			}

		} catch (Exception ex) {

		}

		PrimeFaces.current().ajax().update("form:baySection");

	}
	
	public void changeOrder(Tank b, int id, int index) {
		for (OrderDtl od :b.getOrders())
		{
			if(od.getOrderId() == id)
			{
				b.setSelectedOrder(od);
			}
		}
				
		StringBuilder sb = new StringBuilder();
		
		sb.append("form:bb:");
		sb.append(index);
		sb.append(":section");
		
		
		PrimeFaces.current().ajax().update(sb.toString());
		
	}

	public void addDevice() {
		Device d = new Device(this.tempIp, this.tempPort);
		deviceList.add(d);
		this.tempIp = "";
		this.tempPort = 0;

	}

	public void giveCommand(Device d) {
		try {
			d.giveCommand(tempCommand);

		} catch (Exception ex) {
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
		if (tempCommand == null)
			tempCommand = "";
		return tempCommand;
	}

	public void setTempCommand(String tempCommand) {
		this.tempCommand = tempCommand;
	}

	public Date getDd() {
		if (dd == null)
			dd = new Date();
		return dd;
	}

	public void setDd(Date dd) {
		this.dd = dd;
	}

	public List<Tank> getBays() {
		if (bays == null)
			bays = new ArrayList<Tank>();

		return bays;
	}

	public void setBays(List<Tank> bays) {
		this.bays = bays;
	}

}
