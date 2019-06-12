package controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.primefaces.PrimeFaces;

import db.CustomDao;
import model.*;

@SessionScoped
@ManagedBean(name = "adminController")

public class AdminController implements Serializable {

	/**
	 * Admin view-n datag udirdah heseg
	 */
	private static final long serialVersionUID = 1L;
	private List<Tank> tanks;
	private List<User> users;

	private List<Product> products;
	private List<Arm> arms;
	private CustomDao cursor = new CustomDao();
	private User cursorUser = new User();
	///

	public AdminController() {
		super();
	}

	public void initData() {

		try {
			getCursor();
			getTanks().clear();
			getArms().clear();
			getProducts().clear();

			for (Object o : cursor.getList(new Arm())) {
				Arm a = (Arm) o;
				arms.add(a);

			}
			for (Object o : cursor.getList(new Product())) {
				Product p = (Product) o;
				products.add(p);
			}

			for (Object o : cursor.getList(new Tank())) {
				Tank t = (Tank) o;
				this.tanks.add(t);

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
			getUsers().clear();
			for (Object ob : cursor.getList(new User())) {
				User u = (User) ob;
				this.users.add(u);
			}

			PrimeFaces.current().ajax().update("form:userSection");
			PrimeFaces.current().ajax().update("form:baySection");

		} catch (Exception ex) {
			System.out.println("error occured while getting data initData..");
			ex.printStackTrace();
		}
	}

	public void loadFromOilDepot() {
		try {
			String url = "http://oildepot.petrovis.mn/findByLocationConfig?LocationID=3";
			Document doc = Jsoup.connect(url).get();
			Element body = doc.select("body").first();
			System.out.println("Text: " + body.text());

			Object obj = new JSONParser().parse(body.text());

			JSONObject jo = (JSONObject) obj;
			JSONArray tankArmMapList = (JSONArray) jo.get("TankAndArmConfigDetailListJSON");
			Iterator<JSONObject> tamIterator = tankArmMapList.iterator();
			JSONArray tankList = (JSONArray) jo.get("TankDetailListJSON");
			Iterator<JSONObject> tankIterator = tankList.iterator();
			JSONArray productList = (JSONArray) jo.get("ProductDetailListJSON");
			Iterator<JSONObject> productIterator = productList.iterator();
			JSONArray armList = (JSONArray) jo.get("ArmDetailListJSON");
			Iterator<JSONObject> armIterator = armList.iterator();

			while (productIterator.hasNext()) {
				Product p = new Product();
				JSONObject jjo = productIterator.next();
				p.setProductName((String) jjo.get("ProductName"));
				p.setProductId((int) (long) jjo.get("ProductID"));
				StringBuilder sb = new StringBuilder();
				sb.append("select p from Product p where productId = ");
				sb.append(p.getProductId());
				List<Object> ol = cursor.getListByQuery(new Product(), sb.toString());
				if (ol != null && ol.size() > 0) {
					p.setId(((Product) ol.get(0)).getId());
					cursor.update(p);
				} else {
					cursor.insert(p);
				}

			}

			while (armIterator.hasNext()) {
				Arm a = new Arm();
				JSONObject jjo = armIterator.next();
				a.setArmName((String) jjo.get("ArmName"));
				a.setArmId((int) (long) jjo.get("ArmID"));
				StringBuilder sb = new StringBuilder();
				sb.append("select a from Arm a where armId = ");
				sb.append(a.getArmId());

				List<Object> ol = cursor.getListByQuery(new Arm(), sb.toString());
				if (ol != null && ol.size() > 0) {
					a.setId(((Arm) ol.get(0)).getId());
					cursor.update(a);
				} else {
					cursor.insert(a);
				}
			}

			while (tankIterator.hasNext()) {
				Tank t = new Tank();
				JSONObject jjo = tankIterator.next();
				t.setTankName((String) jjo.get("TankName"));
				t.setTankId((int) (long) jjo.get("TankID"));
				t.setProductId((int) (long) jjo.get("ProductID"));
				StringBuilder sb = new StringBuilder();
				sb.append("select t from Tank t where tankId = ");
				sb.append(t.getTankId());

				List<Object> ol = cursor.getListByQuery(new Tank(), sb.toString());
				if (ol != null && ol.size() > 0) {
					t.setId(((Tank) ol.get(0)).getId());
					cursor.update(t);
				} else {
					cursor.insert(t);
				}
			}

			while (tamIterator.hasNext()) {
				TankArmMap tam = new TankArmMap();
				JSONObject jjo = tamIterator.next();
				tam.setTankId((int) (long) jjo.get("TankID"));
				tam.setArmId((int) (long) jjo.get("ArmID"));
				StringBuilder sb = new StringBuilder();
				sb.append("select tam from TankArmMap tam where tankId = ");
				sb.append(tam.getTankId());

				List<Object> ol = cursor.getListByQuery(new TankArmMap(), sb.toString());
				if (ol != null && ol.size() > 0) {
					tam.setId(((TankArmMap) ol.get(0)).getId());
					cursor.update(tam);
				} else {
					cursor.insert(tam);
				}
			}
			initData();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void saveTankConfig(Tank tank) {
		try {
			cursor.update(tank);

		} catch (Exception ex) {
			System.out.println("error occured while getting data saveBay..");
			ex.printStackTrace();
		}

	}

	public void saveArmConfig(Arm arm) {
		try {
			cursor.update(arm);

		} catch (Exception ex) {
			System.out.println("error occured while getting data saveBay..");
			ex.printStackTrace();
		}

	}

	public void saveUser(User user) {
		if (user.getId() == 0) {
			try {
				cursor.insert(user);
			} catch (Exception ex) {
				System.out.println("error occured while getting data saveUser..");
				ex.printStackTrace();
			}
		} else {
			try {

				cursor.update(user);
			} catch (Exception ex) {
				System.out.println("error occured while getting data saveUser..");
				ex.printStackTrace();
			}
		}
		PrimeFaces.current().ajax().update("form:userSection");
	}

	public void deleteUser(User user) {
		if (user.getId() != 0) {
			try {
				cursor.delete(user);
				initData();
			} catch (Exception ex) {
				System.out.println("error occured while getting data saveUser..");
				ex.printStackTrace();
			}

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

	public void addUser() {
		getUsers();
		User u = new User();
		users.add(u);
		PrimeFaces.current().ajax().update("form:userSection");
	}

	public List<Tank> getTanks() {
		if (tanks == null)
			tanks = new ArrayList<Tank>();
		return tanks;
	}

	public void setTanks(List<Tank> tanks) {
		this.tanks = tanks;
	}

	public List<User> getUsers() {
		if (users == null)
			users = new ArrayList<User>();
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public CustomDao getCursor() {
		if (cursor == null)
			cursor = new CustomDao();
		return cursor;
	}

	public void setCursor(CustomDao cursor) {
		this.cursor = cursor;
	}

	public User getCursorUser() {
		if (cursorUser == null)
			cursorUser = new User();
		return cursorUser;
	}

	public void setCursorUser(User cursorUser) {
		this.cursorUser = cursorUser;
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

	public List<Product> getProducts() {

		if (products == null) {
			products = new ArrayList<Product>();

		}

		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

}
