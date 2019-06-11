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
			ret = ex.getMessage();

		} catch (IOException ex) {

			System.out.println("I/O error: " + ex.getMessage());
			ret = ex.getMessage();

		} catch (InterruptedException e) {

			// TODO Auto-generated catch block
			ret = e.getMessage();
			e.printStackTrace();

		} finally {
			String kk = "Command:" + cmd + "\t Response :" + ret;
			System.out.println("Command:" + cmd + "\t Response :" + ret);
			PrimeFaces.current().executeScript("console.log('" + kk + "');");

		}

		return ret;
	}

	public void start() {

		StringBuilder cmd = new StringBuilder();
		cmd.append("01");
		cmd.append("SB ");
		cmd.append(getSelectedOrder().getCapacity());
		giveCommand(cmd.toString());
		// todo fix
		this.getSelectedOrder().setArmStartMetr(Float.valueOf(giveCommand("01VT G 01").split("G 01")[1]));

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

	}

	public void stop() {

		StringBuilder cmd = new StringBuilder();
		cmd.append("01");
		cmd.append("ST ");

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

	}

	public void next() {
		giveCommand("01ET");
	}

	public void check() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		StringBuilder cmd = new StringBuilder();
		cmd.append("01");
		cmd.append("RS");
		if (giveCommand(cmd.toString()).contains("BD")) {
			// Temprature
			StringBuilder subCmd = new StringBuilder();
			subCmd.append("01");
			subCmd.append("DY CB 6");
			float floatTemprature = Float
					.valueOf(giveCommand(subCmd.toString()).split("Temp")[1].replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
			this.selectedOrder.setTemprature((int) floatTemprature);

			// Density
			StringBuilder subCmd1 = new StringBuilder();
			subCmd1.append("01");
			subCmd1.append("DY CB 7");

			float denisity = Float
					.valueOf(giveCommand(subCmd1.toString()).split("Dens")[1].replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
			this.selectedOrder.setDensity(denisity);
			giveCommand("01RE BD");

			this.getSelectedOrder().setArmEndMetr(Float.valueOf(giveCommand("01VT G 01").split("G 01")[1]));

			StringBuilder ssbb = new StringBuilder();
			ssbb.append("PF('poll");
			ssbb.append(this.id - 1);
			ssbb.append("').stop();");
			PrimeFaces.current().executeScript(ssbb.toString());

		} else {
			// Capacity - Loaded volume
			StringBuilder subCmd = new StringBuilder();
			subCmd.append("01");
			subCmd.append("DY CB 2");

			float f = Float
					.valueOf(giveCommand(subCmd.toString()).split("Batch")[1].replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
			this.selectedOrder.setFillStatus(String.valueOf(f));

		}

		StringBuilder sb = new StringBuilder();
		sb.append("form:bb:");
		sb.append(this.id - 1);
		sb.append(":section");

		PrimeFaces.current().ajax().update(sb.toString());

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
		if(this.armTanks!= null && this.armTanks.size()>0)
			productId = this.armTanks.get(0).getProductId();
		else
			productId = -1;
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}

}
