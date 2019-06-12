package model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.primefaces.PrimeFaces;

import db.CustomDao;

@Entity
@Table(name = "Arm")
public class Arm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "armName")
	private String armName;

	@Column(name = "armId")
	private int armId;

	@Column(name = "armNo")
	private String armNo;

	@Column(name = "ip")
	private String ip;

	@Transient
	private int mapId;

	@Transient
	private List<DeliveryOrder> orders;

	@Transient
	private List<Tank> armTanks;

	@Transient
	private DeliveryOrder selectedOrder;

	@Transient
	private int selectedOrderId;

	@Transient
	private int productId;

	@Transient
	private CustomDao cursor;

	@Transient
	private boolean pollStop = false;

	public Arm() {
		super();
	}

	public Arm(Arm a, String armNo) {
		super();
		this.armId = a.armId;
		this.id = a.id;
		this.armName = a.armName;
		this.armNo = armNo;
	}

	public String giveCommand(String cmd) {
		String ret = "";
		;
		int port = 7734;
		byte[] buff = new byte[10000];
		buff[0] = 0x02;
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
			ret = responseData;

		} catch (UnknownHostException ex) {

			return "error";

		} catch (IOException ex) {

			return "error";

		} catch (InterruptedException e) {

			return "error";

		} finally {
			String kk = "Command:" + cmd + "\t Response :" + ret;

			executeJsCommand("console.log('" + kk + "');");

		}

		return ret;
	}

	public void start(long index) {

		// Zahialga uguh command
		StringBuilder cmd = new StringBuilder();
		cmd.append((this.getArmNo() != null) ? this.getArmNo() : "01");
		cmd.append("SB ");
		cmd.append(getSelectedOrder().getCapacity());
		String response = "error";
		response = giveCommand(cmd.toString());

		if (!"error".equals(response) && response.contains("OK")) {

			StringBuilder cmd2 = new StringBuilder();
			cmd2.append((this.getArmNo() != null) ? this.getArmNo() : "01");
			cmd2.append("VT G 01");
			String resp = giveCommand(cmd2.toString());
			if (resp.split("G 01").length > 1)
				this.getSelectedOrder()
						.setArmStartMetr(Float.valueOf(resp.split("G 01")[1].replaceAll("[^\\d.]+|\\.(?!\\d)", "")));

			if (getSelectedOrder().getId() != 0) {
				try {
					cursor.update(this.getSelectedOrder());
				} catch (Exception ex) {

				}
			}

		}
		setPollStop(false);
		StringBuilder ssbb = new StringBuilder();
		ssbb.append("PF('poll");
		ssbb.append(index);
		ssbb.append("').start();");

		System.out.println("Start command -> " + ssbb.toString());
		executeJsCommand(ssbb.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("form:bb:");
		sb.append(index);
		sb.append(":section");
		PrimeFaces.current().ajax().update(sb.toString());

	}

	public void stop(long index) {
		setPollStop(true);
		StringBuilder ssbb = new StringBuilder();
		ssbb.append("PF('poll");
		ssbb.append(index);
		ssbb.append("').stop();");
		System.out.println("Stop command -> " + ssbb.toString());
		executeJsCommand(ssbb.toString());

		try {

			Thread.sleep(100);

		} catch (Exception ex) {

		}

		StringBuilder cmd = new StringBuilder();
		cmd.append((this.getArmNo() != null) ? this.getArmNo() : "01");
		cmd.append("ST");
		String resp = "error";

		resp = giveCommand(cmd.toString());
		System.out.println(resp);

		StringBuilder sb = new StringBuilder();
		sb.append("form:bb:");
		sb.append(index);
		sb.append(":section");
		PrimeFaces.current().ajax().update(sb.toString());

	}

	public void next(long index) {
		setPollStop(true);
		StringBuilder ssbb = new StringBuilder();
		ssbb.append("PF('poll");
		ssbb.append(index);
		ssbb.append("').stop();");
		System.out.println("Stop command -> " + ssbb.toString());
		executeJsCommand(ssbb.toString());

		giveCommand("01ET");
	}

	public void check(long index) {
		StringBuilder cmd = new StringBuilder();
		cmd.append((this.getArmNo() != null) ? this.getArmNo() : "01");
		cmd.append("RS");
		String resp = "error";
		resp = giveCommand(cmd.toString());
		if (!resp.equals("error") && resp.contains("BD")) {
			// Temprature
			StringBuilder subCmd = new StringBuilder();
			subCmd.append((this.getArmNo() != null) ? this.getArmNo() : "01");
			subCmd.append("DY CB 6");
			String tempResp = giveCommand(subCmd.toString());
			if (tempResp.split("Temp").length > 1) {
				float floatTemprature = Float.valueOf(tempResp.split("Temp")[1].replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
				this.selectedOrder.setTemprature((int) floatTemprature);
			}

			// Density
			StringBuilder subCmd1 = new StringBuilder();
			subCmd1.append((this.getArmNo() != null) ? this.getArmNo() : "01");
			subCmd1.append("DY CB 7");
			String densResp = giveCommand(subCmd1.toString());
			if (densResp.split("Dens").length > 1) {
				float denisity = Float.valueOf(densResp.split("Dens")[1].replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
				this.selectedOrder.setDensity(denisity);
			}

			// end batch
			StringBuilder subCmd2 = new StringBuilder();
			subCmd2.append((this.getArmNo() != null) ? this.getArmNo() : "01");
			subCmd2.append("RE BD");
			giveCommand(subCmd2.toString());
			// end meter
			StringBuilder cmd2 = new StringBuilder();
			cmd2.append((this.getArmNo() != null) ? this.getArmNo() : "01");
			cmd2.append("VT G 01");
			String emResp = giveCommand(cmd2.toString());
			if (emResp.split("G 01").length > 1)
				this.getSelectedOrder()
						.setArmEndMetr(Float.valueOf(emResp.split("G 01")[1].replaceAll("[^\\d.]+|\\.(?!\\d)", "")));

			
			///completedShipmentReceiver ParamenterName->ShipmentJSON
			getSelectedOrder().setShippedDate(new Date());
			if (getSelectedOrder().getId() != 0) {
				
				try {
					cursor.update(this.getSelectedOrder());
				} catch (Exception ex) {

				}
			}
			setPollStop(true);
			StringBuilder ssbb = new StringBuilder();
			ssbb.append("PF('poll");
			ssbb.append(index);
			ssbb.append("').stop();");
			executeJsCommand(ssbb.toString());
			try {

				Thread.sleep(100);

			} catch (Exception ex) {

			}

			System.out.println("STOP command" + ssbb.toString());

		} else if (!resp.equals("error")) {
			// Capacity - Loaded volume
			StringBuilder subCmd = new StringBuilder();
			subCmd.append((this.getArmNo() != null) ? this.getArmNo() : "01");
			subCmd.append("DY CB 2");
			String statusResp = "error";
			statusResp = giveCommand(subCmd.toString());
			if (!statusResp.equals("error") && statusResp.split("Batch").length > 1) {
				float f = Float.valueOf(
						giveCommand(subCmd.toString()).split("Batch")[1].replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
				this.selectedOrder.setFillStatus(String.valueOf(f));
				if (getSelectedOrder().getId() != 0) {
					try {
						System.out.println(this.getSelectedOrder().getId());
						cursor.update(this.getSelectedOrder());
					} catch (Exception ex) {

					}
				}
			}
			StringBuilder sb = new StringBuilder();
			sb.append("form:bb:");
			sb.append(index);
			sb.append(":section");

			PrimeFaces.current().ajax().update(sb.toString());

		}

	}

	public void executeJsCommand(String cmd) {
		PrimeFaces.current().executeScript(cmd);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getArmName() {
		return armName;
	}

	public void setArmName(String armName) {
		this.armName = armName;
	}

	public int getArmId() {
		return armId;
	}

	public void setArmId(int armId) {
		this.armId = armId;
	}

	public String getArmNo() {
		return armNo;
	}

	public void setArmNo(String armNo) {
		this.armNo = armNo;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public List<DeliveryOrder> getOrders() {
		if (orders == null)
			orders = new ArrayList<DeliveryOrder>();
		return orders;
	}

	public void setOrders(List<DeliveryOrder> orders) {
		this.orders = orders;
	}

	public DeliveryOrder getSelectedOrder() {
		if (selectedOrder == null)
			if (getOrders().size() > 0)
				selectedOrder = orders.get(0);
			else
				selectedOrder = new DeliveryOrder();

		return selectedOrder;
	}

	public void setSelectedOrder(DeliveryOrder selectedOrder) {
		this.selectedOrder = selectedOrder;
	}

	public int getSelectedOrderId() {
		return selectedOrderId;
	}

	public void setSelectedOrderId(int selectedOrderId) {
		this.selectedOrderId = selectedOrderId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Tank> getArmTanks() {
		if (armTanks == null)
			armTanks = new ArrayList<Tank>();
		return armTanks;
	}

	public void setArmTanks(List<Tank> armTanks) {
		this.armTanks = armTanks;
	}

	public int getProductId() {
		if (this.armTanks != null && this.armTanks.size() > 0)
			productId = this.armTanks.get(0).getProductId();
		else
			productId = -1;
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public CustomDao getCursor() {
		if (cursor == null)
			cursor = new CustomDao();
		return cursor;
	}

	public void setCursor(CustomDao cursor) {
		this.cursor = cursor;
	}

	public boolean isPollStop() {
		return pollStop;
	}

	public void setPollStop(boolean pollStop) {
		this.pollStop = pollStop;
	}

}
