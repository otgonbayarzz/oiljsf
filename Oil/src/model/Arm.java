package model;

import java.io.Serializable;

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

	@Transient
	private String armNo;

	@Transient
	private int mapId;

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
