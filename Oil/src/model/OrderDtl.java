package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "OrderDtl")

public class OrderDtl {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "armId")
	private int armId;

	@Column(name = "orderId")
	private int orderId;

	@Column(name = "productId")
	private int productId;

	@Column(name = "vehicleId")
	private String vehicleId;

	@Column(name = "orderVolume")
	private float orderVolume;

	@Column(name = "compartmentNo")
	private int compartmentNo;

	@Column(name = "fillStatus")
	private int fillStatus;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getArmId() {
		return armId;
	}

	public void setArmId(int armId) {
		this.armId = armId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public float getOrderVolume() {
		return orderVolume;
	}

	public void setOrderVolume(float orderVolume) {
		this.orderVolume = orderVolume;
	}

	public int getCompartmentNo() {
		return compartmentNo;
	}

	public void setCompartmentNo(int compartmentNo) {
		this.compartmentNo = compartmentNo;
	}

	public int getFillStatus() {
		return fillStatus;
	}

	public void setFillStatus(int fillStatus) {
		this.fillStatus = fillStatus;
	}

}
