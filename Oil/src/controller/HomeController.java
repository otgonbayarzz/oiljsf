package controller;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

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

import model.Product;
import model.Arm;
import model.Constant;
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
	private List<DeliveryOrder> sorders;
	private String tempCommand;

	private String density;

	@ManagedProperty(value = "#{appController}")
	private ApplicationController appController;

	public Date dd = new Date();

	public String startDate = "";
	public String endDate = "";
	public int prodId;

	public HomeController() {
		super();
	}

	public void pushOrders() {

		Date today = new Date();
		today.setDate(today.getDate() - 1);
		today.setHours(00);
		today.setMinutes(00);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		StringBuilder sb = new StringBuilder();

		sb.append(" select a.deliveryOrderId from ");
		sb.append(" (select deliveryOrderId, count(1) as cnt ");
		sb.append(" from DeliveryOrder ");
		sb.append(" where (shippedDate is not null OR char_length(shippedDate) > 5) and sentStatus = 0 ");
		sb.append(" group by deliveryOrderId  ) as a ");
		sb.append(" inner join ");
		sb.append(" ( select deliveryOrderId, count(1) as cnt ");
		sb.append(" from DeliveryOrder ");
		sb.append(" group by deliveryOrderId ");
		sb.append(" ) as b ");
		sb.append("  on a.deliveryOrderId = b.deliveryOrderId ");
		sb.append(" and a.cnt = b.cnt ");
		System.out.println(sb.toString());
		List<Object> ol = cursor.getListByCustomQuery(Integer.class, sb.toString());
		System.out.println("---." + ol.size());

		for (Object o : ol) {
			int doId = (int) o;
			StringBuilder sbb = new StringBuilder();
			sbb.append(" select dor ");
			sbb.append(" from DeliveryOrder dor ");
			sbb.append(" where deliveryOrderId  = ");
			sbb.append(doId);
			sbb.append(" and sentStatus  = 0  ");
			List<Object> doObjectList = cursor.getListByQuery(DeliveryOrder.class, sbb.toString());
			List<DeliveryOrder> doList = new ArrayList<DeliveryOrder>();
			JSONArray array = new JSONArray();
			for (Object ob : doObjectList) {
				DeliveryOrder order = (DeliveryOrder) ob;
				doList.add(order);

				JSONObject param = new JSONObject();
				param.put("TrailerNo", order.getTrailerNo());
				param.put("VehicleNo", order.getVehicleNo());
				param.put("Capacity", order.getCapacity());

				param.put("DeliveryOrderID", order.getDeliveryOrderId());
				param.put("DriverName", order.getDriverName());
				if (order.getDeliveryOrderDate() != null)
					param.put("ShippedDate ", order.getDeliveryOrderDate().toString());
				else
					param.put("ShippedDate ", null);
				param.put("DeliveryOrderDate", order.getDeliveryOrderDate().toString());
				param.put("ProductID", order.getProductId());
				param.put("CompartmentSequence", order.getCompartmentSequence());
				if (order.getShippedDate() != null)
					param.put("ShippedDate ", order.getShippedDate().toString());
				else
					param.put("ShippedDate ", null);
				param.put("TankID", order.getTankId());
				param.put("ArmID", order.getArmId());
				param.put("Temperature ", order.getTemprature());
				param.put("Density ", order.getDensity());
				param.put("ArmStartMetr ", order.getArmStartMetr());
				param.put("ArmEndMetr ", order.getArmEndMetr());
				param.put("ShippedAmount", order.getShippedAmount());
				array.add(param);

			}
			System.out.println(array.toString());

			String url = getAppController().getLocationIp() + "/completedShipmentReceiver?ShipmentJSON="
					+ array.toString();

			Document doc = null;
			try {
				doc = Jsoup.connect(url).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Element body = doc.select("body").first();
			String rr = body.text();
			System.out.println("called" + rr);
			if ("1".equals(rr)) {
				for (DeliveryOrder dd : doList) {
					dd.setSentStatus(1);
					cursor.update(dd);
				}

			} else {

				System.out.println("0 irsen retry");
			}

		}

	}

	public void initData() {
		try {

			getCursor();
			getTanks().clear();
			getProducts().clear();
			getArms().clear();
			// getOrderDataFromOilDepot();

			System.out.println("----->" + Float.valueOf(getDensity()));

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
				sb.append("  AND (shippedDate is null OR char_length(shippedDate) < 5 ) ");

				sb.append("  ");
				for (Object o : cursor.getListByQuery(new DeliveryOrder(), sb.toString())) {
					DeliveryOrder od = (DeliveryOrder) o;
					a.getOrders().add(od);
				}

			}

		} catch (Exception ex) {

		}

		PrimeFaces.current().ajax().update("form:baySection");
		PrimeFaces.current().ajax().update("form:prods");

	}

	public void getOrderDataFromOilDepot() {
		List<Object> ll = new ArrayList<Object>();
		ll = cursor.getListByQuery(new Object(), "select a from DeliveryOrder a where a.loadingStatus = 1 ");
		if (ll.size() < 1) {
			try {

				String url = getAppController().getLocationIp() + "/findByDeliveryOrderList?LocationID="
						+ appController.getLocationId();
				System.out.println(url);

				Document doc = Jsoup.connect(url).get();
				System.out.println(doc);
				Element body = doc.select("body").first();

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
					
					sb.append(" and trailerNo =   '");
					sb.append(order.getTrailerNo());

					sb.append("'");

					List<Object> ol = cursor.getListByQuery(new DeliveryOrder(), sb.toString());
					System.out.println("size" + cursor.getListByQuery(new DeliveryOrder(), sb.toString()));
					if (ol != null && ol.size() > 0) {
						System.out.println("Already Have");

					} else {
						System.out.println("inserting");
						cursor.insert(order);
					}

				}

			} catch (Exception ex) {
				ex.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Алдаа гарлаа"));

			}
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Мэдээлэл шинэчиллээ"));

			initData();
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"Ачилт явагдаж байна. Ачилт дууссаны дараа мэдээлэл шинэчилнэ үү!"));
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
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Мэдээлэл шинэчиллээ"));

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

	public String armName(int armId) {
		String ret = "";
		for (Arm a : getArms()) {
			if (armId == a.getArmId()) {
				ret = a.getArmName();
				return ret;
			}
		}

		return ret;

	}

	public void getShippedOrders() {
		Date today = new Date();
		today.setDate(today.getDate() - 1);
		today.setHours(00);
		today.setMinutes(00);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String tdy = df.format(today);

		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT dor ");
		sb.append(" FROM DeliveryOrder dor ");
		sb.append(" WHERE shippedDate BETWEEN '");
		sb.append(getStartDate());
		sb.append("' AND '");
		sb.append(getEndDate());
		sb.append("' ");
		if (prodId != 0) {
			sb.append("AND productId =  ");
			sb.append(getProdId());
			sb.append(" ");
		}

		List<Object> ol = getCursor().getListByQuery(new DeliveryOrder(), sb.toString());
		getSorders().clear();
		if (ol != null && ol.size() > 0) {
			for (Object o : ol) {
				DeliveryOrder dor = (DeliveryOrder) o;
				sorders.add(dor);
			}

		}

		PrimeFaces.current().ajax().update("form:orderSection");

	}
	
	
	
	public void hardReset()
	{
		try {
			cursor.deleteByQuery("update DeliveryOrder set loadingStatus = 0 where id > -1 ");
		}
		catch (Exception ex)
		{
			
		}
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

	public ApplicationController getAppController() {
		if (appController == null)
			appController = new ApplicationController();
		return appController;
	}

	public void setAppController(ApplicationController appController) {
		this.appController = appController;
	}

	public List<DeliveryOrder> getSorders() {
		if (sorders == null)
			sorders = new ArrayList<DeliveryOrder>();
		return sorders;
	}

	public void setSorders(List<DeliveryOrder> sorders) {
		this.sorders = sorders;
	}

	public String getDensity() {

		if (this.density == null) {
			List<Object> ol = cursor.getList(new Constant());

			if (ol != null && ol.size() > 0) {
				if ("density".equals(((Constant) ol.get(0)).getName())) {
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

	@SuppressWarnings("deprecation")
	public String getStartDate() {
		if (startDate == null)
			startDate = new Date().getYear() + "-" + new Date().getMonth() + "-" + new Date().getDate();
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@SuppressWarnings("deprecation")
	public String getEndDate() {
		if (endDate == null)
			endDate = new Date().getYear() + "-" + new Date().getMonth() + "-" + new Date().getDate();
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getProdId() {
		return prodId;
	}

	public void setProdId(int prodId) {
		this.prodId = prodId;
	}

}
