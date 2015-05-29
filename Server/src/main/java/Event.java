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

	public Event(String name, Calendar date) {
		this.nameEvent = name;
		this.dateEvent = date;
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
	
	@Column(name = "photoId")
	private int photoId;

	public int getIdEvent() {
		return this.idEvent;
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
