package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "DeliveryOrder")

public class DeliveryOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "deliveryOrderId")
	private int deliveryOrderId;

	@Column(name = "vehicleNo")
	private String vehicleNo;

	@Column(name = "driverName")
	private String driverName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deliveryOrderDate")
	private Date deliveryOrderDate;

	@Column(name = "trailerNo")
	private String trailerNo;

	@Column(name = "compartmentSequence")
	private int compartmentSequence;

	@Column(name = "capacity")
	private int capacity;

	@Column(name = "productId")
	private int productId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "shippedDate")
	private Date shippedDate;

	@Column(name = "tankId")
	private int tankId;

	@Column(name = "armId")
	private int armId;

	@Column(name = "temprature")
	private int temprature;

	@Column(name = "density")
	private float density;

	@Column(name = "armStartMetr")
	private float armStartMetr;

	@Column(name = "armEndMetr")
	private float armEndMetr;

	@Column(name = "shippedAmount")
	private float shippedAmount;

	@Column(name = "sentStatus")
	private int sentStatus;

	public DeliveryOrder() {
		super();
	}

	public DeliveryOrder(int doId) {
		super();
		this.deliveryOrderId = doId;
	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDeliveryOrderId() {
		return deliveryOrderId;
	}

	public void setDeliveryOrderId(int deliveryOrderId) {
		this.deliveryOrderId = deliveryOrderId;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public Date getDeliveryOrderDate() {
		return deliveryOrderDate;
	}

	public void setDeliveryOrderDate(Date deliveryOrderDate) {
		this.deliveryOrderDate = deliveryOrderDate;
	}

	public String getTrailerNo() {
		return trailerNo;
	}

	public void setTrailerNo(String trailerNo) {
		this.trailerNo = trailerNo;
	}

	public int getCompartmentSequence() {
		return compartmentSequence;
	}

	public void setCompartmentSequence(int compartmentSequence) {
		this.compartmentSequence = compartmentSequence;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public Date getShippedDate() {
		return shippedDate;
	}

	public void setShippedDate(Date shippedDate) {
		this.shippedDate = shippedDate;
	}

	public int getTankId() {
		return tankId;
	}

	public void setTankId(int tankId) {
		this.tankId = tankId;
	}

	public int getArmId() {
		return armId;
	}

	public void setArmId(int armId) {
		this.armId = armId;
	}

	public int getTemprature() {
		return temprature;
	}

	public void setTemprature(int temprature) {
		this.temprature = temprature;
	}

	public float getDensity() {
		return density;
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public float getArmStartMetr() {
		return armStartMetr;
	}

	public void setArmStartMetr(float armStartMetr) {
		this.armStartMetr = armStartMetr;
	}

	public float getArmEndMetr() {
		return armEndMetr;
	}

	public void setArmEndMetr(float armEndMetr) {
		this.armEndMetr = armEndMetr;
	}

	public float getShippedAmount() {
		return shippedAmount;
	}

	public void setShippedAmount(float shippedAmount) {
		this.shippedAmount = shippedAmount;
	}

	public int getSentStatus() {
		return sentStatus;
	}

	public void setSentStatus(int sentStatus) {
		this.sentStatus = sentStatus;
	}

}
