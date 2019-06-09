package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.PrimeFaces;

import db.CustomDao;
import model.Tank;
import model.TankArmMap;
import model.Device;
import model.Product;
import model.Arm;
import model.DeliveryOrder;

@SessionScoped
@ManagedBean(name = "homeController")
public class HomeController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CustomDao cursor = new CustomDao();
	private List<Tank> tanks;
	private List<Product> products;

	private String tempCommand;
	public Date dd = new Date();

	public HomeController() {
		super();
	}

	public void initData() {
		try {

			getTanks().clear();
			getProducts().clear();

			for (Object o : cursor.getList(new Tank())) {
				Tank b = (Tank) o;
				this.tanks.add(b);
			}

			for (Object o : cursor.getList(new Product())) {
				Product p = (Product) o;
				products.add(p);
			}

			for (Tank b : tanks) {
				b.getOrders().clear();
				StringBuilder sb = new StringBuilder();
				sb.append("SELECT od ");
				sb.append("from DeliveryOrder od ");
				sb.append("WHERE od.productId =  ");
				sb.append(b.getProductId());

				sb.append("  ");
				for (Object o : cursor.getListByQuery(new DeliveryOrder(), sb.toString())) {
					DeliveryOrder od = (DeliveryOrder) o;
					b.getOrders().add(od);
				}

			}
			for (Tank t : tanks) {
				t.setArms(new ArrayList<Arm>());
				t.getArms().clear();

				StringBuilder sb = new StringBuilder();
				sb.append("SELECT  tam  ");
				sb.append("FROM TankArmMap  tam ");
				sb.append("WHERE tam.tankId =  ");
				sb.append(t.getTankId());
				sb.append(" ");

				List<Object> ol = new ArrayList<Object>();

				ol = cursor.getListByQuery(new Arm(), sb.toString());

				if (ol != null && ol.size() > 0) {
					for (Object o : cursor.getListByQuery(new TankArmMap(), sb.toString())) {
						TankArmMap tam = (TankArmMap) o;
						Arm a = new Arm();
						a.setMapId(tam.getId());
						a.setArmId(tam.getArmId());
						a.setArmNo(tam.getArmNo());
						t.getArms().add(a);
					}
				} else {

					System.out.println("ADDING DUMMY");
					Arm a = new Arm();
					a.setMapId(0);
					t.getArms().add(a);

				}

			}

		} catch (Exception ex) {

		}

		PrimeFaces.current().ajax().update("form:baySection");

	}

	public void changeOrder(Tank b, int id, int index) {
		for (DeliveryOrder od : b.getOrders()) {
			if (od.getId() == id) {
				b.setSelectedOrder(od);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append("form:bb:");
		sb.append(index);
		sb.append(":section");

		PrimeFaces.current().ajax().update(sb.toString());

	}

	public void giveCommand(Device d) {
		try {
			d.giveCommand(tempCommand);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String productName(int productId) {
		String ret = "";
		for (Product p : getProducts()) {
			if (productId == p.getProductId()) {
				ret = p.getProductName();
				return ret;
			}
		}

		return ret;

	}

	public List<Tank> getTanks() {
		if (tanks == null)
			tanks = new ArrayList<Tank>();
		return tanks;
	}

	public void setTanks(List<Tank> tanks) {
		this.tanks = tanks;
	}

	public List<Product> getProducts() {
		if (products == null)
			products = new ArrayList<Product>();
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

}
