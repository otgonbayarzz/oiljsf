package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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

	public AdminController() {
		super();
	}

	public void initData() {

		try {

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

	public void saveTankConfig(Tank tank) {
		try {
			cursor.update(tank);
			for (Arm a : tank.getArms()) {
				TankArmMap tam = new TankArmMap();
				tam.setTankId(tank.getTankId());
				tam.setArmId(a.getArmId());
				tam.setArmNo(a.getArmNo());
				tam.setId(a.getMapId());
				if (tam.getId() == 0)
					cursor.insert(tam);
				else
					cursor.update(tam);

			}

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
