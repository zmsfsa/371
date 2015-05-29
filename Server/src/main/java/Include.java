package main.java;

import javax.persistence.*;

@Entity
@Table(name = "partyfinderdb.includes")
public class Include {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idInclude")
	private int idInclude;

	@Column(name = "idEvent")
	private int idEvent;

	@Column(name = "checkWidth")
	private String checkWidth;

	@Column(name = "checkHeight")
	private String checkHeight;

	@Column(name = "idUser")
	private int idUser;

	public Include() {

	}

	public Include(int idEvent, int idUser, String width, String height) {
		this.idEvent = idEvent;
		this.checkWidth = width;
		this.checkHeight = height;
		this.idUser = idUser;
	}

	public int getIdEvent() {
		return this.idEvent;
	}

	public String getHeight() {
		return this.checkHeight;
	}

	public String getWidth() {
		return this.checkWidth;
	}

	public int getIdUser() {
		return this.idUser;
	}

	public void setHeight(String height) {
		this.checkHeight = height;
	}

	public void setWidth(String width) {
		this.checkWidth = width;
	}

	public void setIdUser(int id) {
		this.idUser = id;
	}

	public void setIdEvent(int id) {
		this.idEvent = id;
	}
}
