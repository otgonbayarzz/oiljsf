package model;

import java.io.DataOutputStream;
import java.io.IOException;

import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.primefaces.PrimeFaces;

@Entity
@Table(name = "Tank")
public class Tank implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "tankId")
	private int tankId;

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

	@Transient
	private String command;

	@Transient
	private String response;

	public void start() {

		String lastResponse = "";
		int port = 7734;
		byte[] buff = new byte[10000];
		buff[0] = 0x02;
		StringBuilder cmd = new StringBuilder();
		cmd.append("0");
		cmd.append(this.armId);
		cmd.append("SB ");
		cmd.append(getSelectedOrder().getOrderVolume());

		try (Socket socket = new Socket(this.ip, port)) {

			OutputStream output = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(output);
			byte[] b = cmd.toString().getBytes(StandardCharsets.UTF_8);

			for (int i = 0; i < b.length; i++) {
				buff[i + 1] = b[i];
			}
			buff[b.length + 1] = 0x03;

			dos.write(buff, 0, b.length + 2);
			Thread.sleep(1000);

			byte[] data = new byte[socket.getInputStream().available()];
			int bytes = socket.getInputStream().read(data, 0, data.length);
			String responseData = new String(data, 0, bytes, "ASCII");
			this.response = responseData;

		} catch (UnknownHostException ex) {

			lastResponse = ex.getMessage();
			this.response = lastResponse;

		} catch (IOException ex) {

			System.out.println("I/O error: " + ex.getMessage());
			lastResponse = ex.getMessage();
			this.response = lastResponse;

		} catch (InterruptedException e) {

			// TODO Auto-generated catch block
			lastResponse = e.getMessage();
			e.printStackTrace();
			this.response = lastResponse;
		} finally {
			StringBuilder sb = new StringBuilder();
			sb.append("form:bb:");
			sb.append(this.id - 1);
			sb.append(":section");

			StringBuilder ssb = new StringBuilder();
			ssb.append("PF('pb");
			ssb.append(this.id - 1);
			ssb.append("').getJQ().show();");

			StringBuilder ssbb = new StringBuilder();
			ssbb.append("PF('poll");
			ssbb.append(this.id - 1);
			ssbb.append("').start();");

			PrimeFaces.current().ajax().update(sb.toString());
			PrimeFaces.current().executeScript(ssb.toString());
			PrimeFaces.current().executeScript(ssbb.toString());
			PrimeFaces.current().executeScript("alert('" + cmd.toString() + ":" + lastResponse + "');");
			PrimeFaces.current().executeScript("alert('" + cmd.toString() + ":" + lastResponse + "');");

		}

	}

	public void stop() {
		String lastResponse = "";
		int port = 7734;
		byte[] buff = new byte[10000];
		buff[0] = 0x02;
		StringBuilder cmd = new StringBuilder();
		cmd.append("0");
		cmd.append(this.armId);
		cmd.append("ST ");

		try (Socket socket = new Socket(this.ip, port)) {

			OutputStream output = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(output);
			byte[] b = cmd.toString().getBytes(StandardCharsets.UTF_8);

			for (int i = 0; i < b.length; i++) {
				buff[i + 1] = b[i];
			}
			buff[b.length + 1] = 0x03;

			dos.write(buff, 0, b.length + 2);
			Thread.sleep(1000);

			byte[] data = new byte[socket.getInputStream().available()];
			int bytes = socket.getInputStream().read(data, 0, data.length);
			String responseData = new String(data, 0, bytes, "ASCII");
			lastResponse = responseData;

		} catch (UnknownHostException ex) {

			lastResponse = ex.getMessage();

		} catch (IOException ex) {

			System.out.println("I/O error: " + ex.getMessage());
			lastResponse = ex.getMessage();

		} catch (InterruptedException e) {

			// TODO Auto-generated catch block
			lastResponse = e.getMessage();
			e.printStackTrace();

		} finally {
			StringBuilder sb = new StringBuilder();
			sb.append("form:bb:");
			sb.append(this.id - 1);
			sb.append(":section");

			StringBuilder ssb = new StringBuilder();
			ssb.append("PF('pb");
			ssb.append(this.id - 1);
			ssb.append("').getJQ().hide();");

			StringBuilder ssbb = new StringBuilder();
			ssbb.append("PF('poll");
			ssbb.append(this.id - 1);
			ssbb.append("').stop();");

			PrimeFaces.current().ajax().update(sb.toString());
			PrimeFaces.current().executeScript(ssb.toString());
			PrimeFaces.current().executeScript(ssbb.toString());
			PrimeFaces.current().executeScript("alert('" + cmd.toString() + ":" + lastResponse + "');");

		}

	}

	public void check() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("i am here in function ");
		String lastResponse = "";
		int port = 7734;
		byte[] buff = new byte[10000];
		buff[0] = 0x02;
		StringBuilder cmd = new StringBuilder();
		cmd.append("0");
		cmd.append(this.armId);
		cmd.append("EB ");
		
		try (Socket socket = new Socket(this.ip, port)) {

			OutputStream output = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(output);
			byte[] b = cmd.toString().getBytes(StandardCharsets.UTF_8);

			for (int i = 0; i < b.length; i++) {
				buff[i + 1] = b[i];
			}
			buff[b.length + 1] = 0x03;

			dos.write(buff, 0, b.length + 2);
			Thread.sleep(1000);

			byte[] data = new byte[socket.getInputStream().available()];
			int bytes = socket.getInputStream().read(data, 0, data.length);
			String responseData = new String(data, 0, bytes, "ASCII");
			lastResponse = responseData;

		} catch (UnknownHostException ex) {

			lastResponse = ex.getMessage();

		} catch (IOException ex) {

			System.out.println("I/O error: " + ex.getMessage());
			lastResponse = ex.getMessage();

		} catch (InterruptedException e) {

			// TODO Auto-generated catch block
			lastResponse = e.getMessage();
			e.printStackTrace();

		} finally {
			
			this.response = lastResponse;
			StringBuilder sb = new StringBuilder();
			sb.append("form:bb:");
			sb.append(this.id - 1);
			sb.append(":section");

			PrimeFaces.current().executeScript("alert('" + this.response + "');");


			PrimeFaces.current().ajax().update(sb.toString());

		}

	}

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


	public int getSelectedOrderId() {
		return selectedOrderId;
	}

	public void setSelectedOrderId(int selectedOrderId) {
		this.selectedOrderId = selectedOrderId;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
