package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.primefaces.PrimeFaces;
import java.text.SimpleDateFormat;

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
	private List<Arm> arms;
	private String tempCommand;

	public Date dd = new Date();

	public HomeController() {
		super();
	}

	public void initData() {
		try {
			getCursor();
			getTanks().clear();
			getProducts().clear();
			getArms().clear();
			//getOrderDataFromOilDepot();

			for (Object o : cursor.getList(new Tank())) {
				Tank b = (Tank) o;
				this.tanks.add(b);
			}

			for (Object o : cursor.getList(new Arm())) {
				Arm a = (Arm) o;
				arms.add(a);

			}

			for (Object o : cursor.getList(new Product())) {
				Product p = (Product) o;
				products.add(p);
			}
			for (Arm a : arms) {
				a.getArmTanks().clear();
				StringBuilder sb = new StringBuilder();
				sb.append("SELECT  tam  ");
				sb.append("FROM TankArmMap  tam ");
				sb.append("WHERE tam.armId =  ");
				sb.append(a.getArmId());
				sb.append(" ");

				List<Object> ol = new ArrayList<Object>();

				ol = cursor.getListByQuery(new TankArmMap(), sb.toString());

				if (ol != null && ol.size() > 0) {
					for (Object o : ol) {
						TankArmMap tam = (TankArmMap) o;
						Tank tt = new Tank();
						tt.setTankId(tam.getTankId());
						StringBuilder qry = new StringBuilder();
						qry.append("select t from Tank t where t.tankId = ");
						qry.append(tam.getTankId());
						tt = (Tank) cursor.getListByQuery(new Tank(), qry.toString()).get(0);
						a.getArmTanks().add(tt);
					}
				}
			}

			for (Arm a : arms) {
				a.getOrders().clear();
				StringBuilder sb = new StringBuilder();
				sb.append("SELECT od ");
				sb.append("from DeliveryOrder od ");
				sb.append("WHERE od.productId =  ");
				sb.append(a.getProductId());
				sb.append("  AND shippedDate is null ");

				sb.append("  ");
				for (Object o : cursor.getListByQuery(new DeliveryOrder(), sb.toString())) {
					DeliveryOrder od = (DeliveryOrder) o;
					a.getOrders().add(od);
				}

			}

		} catch (Exception ex) {

		}

		PrimeFaces.current().ajax().update("form:baySection");

	}

	public void getOrderDataFromOilDepot() {

		try {
			String url = "http://oildepot.petrovis.mn/findByDeliveryOrderList?LocationID=3";
			Document doc = Jsoup.connect(url).get();
			Element body = doc.select("body").first();
			System.out.println("Text: " + body.text());

			Object obj = new JSONParser().parse(body.text());

			JSONArray ja = (JSONArray) obj;

			Iterator<JSONObject> orderIterator = ja.iterator();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while (orderIterator.hasNext()) {
				DeliveryOrder order = new DeliveryOrder();
				JSONObject jjo = orderIterator.next();
				order.setVehicleNo((String) jjo.get("VehicleNo"));
				order.setTrailerNo((String) jjo.get("TrailerNo"));
				order.setProductId((int) (long) jjo.get("ProductID"));
				order.setCapacity((int) (long) jjo.get("Capacity"));
				order.setDeliveryOrderId((int) (long) jjo.get("DeliveryOrderID"));
				order.setDriverName((String) jjo.get("DriverName"));
				order.setDeliveryOrderDate(df.parse((String) jjo.get("DeliveryOrderDate")));
				order.setCompartmentSequence((int) (long) jjo.get("CompartmentSequence"));
				System.out.println(order.getVehicleNo());
				StringBuilder sb = new StringBuilder();
				sb.append(" select order ");
				sb.append(" from DeliveryOrder order ");
				sb.append(" where compartmentSequence =  ");
				sb.append(order.getCompartmentSequence());
				sb.append(" and productId =  ");
				sb.append(order.getProductId());
				sb.append(" and deliveryOrderId =  ");
				sb.append(order.getDeliveryOrderId());
				sb.append(" and vehicleNo =   '");
				sb.append(order.getVehicleNo());
				sb.append("' and shippedDate is null ");

				List<Object> ol = cursor.getListByQuery(new DeliveryOrder(), sb.toString());
				if (ol != null && ol.size() > 0) {
					order.setId(((DeliveryOrder) ol.get(0)).getId());
					cursor.update(order);

				} else
					cursor.insert(order);

				// System.out.println("--" + p.getProductName());
			}

		} catch (Exception ex) {

			ex.printStackTrace();
		}

	}

	public void changeOrder(Arm a, int id, int index) {
		for (DeliveryOrder od : a.getOrders()) {
			if (od.getId() == id) {
				a.setSelectedOrder(od);
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

	public List<Arm> getArms() {
		if (arms == null) {
			arms = new ArrayList<Arm>();

		}

		return arms;
	}

	public void setArms(List<Arm> arms) {
		this.arms = arms;
	}

	public CustomDao getCursor() {
		if (cursor == null)
			cursor = new CustomDao();
		return cursor;
	}

	public void setCursor(CustomDao cursor) {
		this.cursor = cursor;
	}

}
