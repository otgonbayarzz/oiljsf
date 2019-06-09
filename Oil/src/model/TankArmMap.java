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
@Table(name = "TankArmMap")
public class TankArmMap implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "armId")
	private int armId;

	@Column(name = "tankId")
	private int tankId;

	@Column(name = "armNo")
	private String armNo;

	public TankArmMap() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public void setTankId(int tankId) {
		this.tankId = tankId;
	}

	public int getTankId() {
		return tankId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
