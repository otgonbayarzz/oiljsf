package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Bay")
public class Bay {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "bayId")
	private int bayId;

	@Column(name = "armId")
	private int armId;

	/**
	 * 1 - Active 0 - Inactive
	 */
	@Column(name = "status")
	private int status;

	@Column(name = "productId")
	private int productId;

	/**
	 * 1 - Automatic 0 - manual
	 */
	@Column(name = "controlType")
	private int controlType;

	@Column(name = "ip")
	private String ip;

	@Transient
	private boolean active;

	@Transient
	private boolean automatic;

	@Transient
	private List<OrderDtl> orders;

	@Transient
	private OrderDtl selectedOrder;

	@Transient
	private int selectedOrderId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getControlType() {
		return controlType;
	}

	public void setControlType(int controlType) {
		this.controlType = controlType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getArmId() {
		return armId;
	}

	public void setArmId(int armId) {
		this.armId = armId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setActive(boolean active) {
		if (active)
			this.status = 1;
		else
			this.status = 0;
		this.active = active;
	}

	public boolean isActive() {
		if (this.status == 1)
			this.active = true;
		else
			this.active = false;
		return active;
	}

	public boolean isAutomatic() {
		if (this.controlType == 1)
			this.automatic = true;
		else
			this.automatic = false;
		return automatic;
	}

	public void setAutomatic(boolean automatic) {
		if (automatic)
			controlType = 1;
		else
			controlType = 0;
		this.automatic = automatic;
	}

	public List<OrderDtl> getOrders() {
		if (orders == null)
			orders = new ArrayList<OrderDtl>();
		return orders;
	}

	public void setOrders(List<OrderDtl> orders) {
		this.orders = orders;
	}

	public OrderDtl getSelectedOrder() {
		if (selectedOrder == null)
			if (getOrders().size() > 0)
				selectedOrder = orders.get(0);
			else
				selectedOrder = new OrderDtl();

		return selectedOrder;
	}

	public void setSelectedOrder(OrderDtl selectedOrder) {
		this.selectedOrder = selectedOrder;
	}

	public int getBayId() {
		return bayId;
	}

	public void setBayId(int bayId) {
		this.bayId = bayId;
	}

	public int getSelectedOrderId() {
		return selectedOrderId;
	}

	public void setSelectedOrderId(int selectedOrderId) {
		this.selectedOrderId = selectedOrderId;
	}

}
