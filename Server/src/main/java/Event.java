package main.java;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "partyfinderdb.events")
public class Event {

	public Event() {

	}

	public Event(String name, Calendar date, String addres, String width, String height) {
		this.nameEvent = name;
		this.dateEvent = date;
		this.apHeight = height;
		this.apWidth = width;
		this.addres = addres;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idEvent")
	private int idEvent;

	@Column(name = "nameEvent", unique = true)
	private String nameEvent;
	
	@Temporal(value=TemporalType.DATE)
	@Column(name = "dateEvent")
	private Calendar dateEvent;
	
	@Column(name = "apWidth")
	private String apWidth;

	@Column(name = "apHeight")
	private String apHeight;
	
	@Column(name = "addres")
	private String addres;
	
	@Column(name = "photoId")
	private int photoId;

	public int getIdEvent() {
		return this.idEvent;
	}
	
	public String getAddres(){
		return this.addres;
	}
	
	public String getHeight() {
		return this.apHeight;
	}

	public String getWidth() {
		return this.apWidth;
	}
	
	public int getPhotoId() {
		return this.photoId;
	}

	public String getNameEvent() {
		return this.nameEvent;
	}

	public Calendar getDateEvent() {
		return this.dateEvent;
	}
	
	public void setHeight(String height) {
		this.apHeight = height;
	}
	
	public void setAddres(String addres){
		this.addres = addres;
	}

	public void setWidth(String width) {
		this.apWidth = width;
	}

	public void setDateEvent(Calendar date) {
		this.dateEvent = date;
	}
	
	public void setPhotoId(int id){
		this.photoId = id;
	}

	public void setNameEvent(String name) {
		this.nameEvent = name;
	}

}
