package controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import db.CustomDao;
import model.Constant;
import model.DeliveryOrder;
import model.LocationConfig;

@ApplicationScoped
@ManagedBean(name = "appController")
public class ApplicationController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String alertMessage;
	private String locationId;
	private String locationIp;
	CustomDao cursor = new CustomDao();
	private String density;

	public ApplicationController() {
		super();

	}

	public void showMessage(String msg) {
		this.alertMessage = msg;

	}

	public String getAlertMessage() {
		return alertMessage;
	}

	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;

	}

	public String getLocationId() {

		if (this.locationId == null) {
			List<Object> ol = cursor.getList(new LocationConfig());

			if (ol != null && ol.size() > 0) {

				this.locationId = ((LocationConfig) ol.get(0)).getLocationId();
			}
		}

		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getLocationIp() {
		if (this.locationIp == null) {
			List<Object> ol = cursor.getList(new LocationConfig());

			if (ol != null && ol.size() > 0) {

				this.locationIp = ((LocationConfig) ol.get(0)).getLocationIp();
			}
		}

		return locationIp;
	}

	public void setLocationIp(String locationIp) {
		this.locationIp = locationIp;
	}
	
	public String getDensity() {
		
		if (this.density == null) {
			List<Object> ol = cursor.getList(new Constant());

			if (ol != null && ol.size() > 0) {
				if("density".equals(((Constant) ol.get(0)).getName()))
				{
					this.density = ((Constant) ol.get(0)).getValue();
				}

				else 
					this.density = "0";
			}
		}

		
		return density;
	}
	public void setDensity(String density) {
		this.density = density;
	}

}
