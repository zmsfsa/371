package main.java;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mysql.jdbc.Blob;
@Entity
@Table(name = "partyfinderdb.photos")
public class Photo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idPhoto")
	private int idPhoto;
	
	@Column(name = "photoFile")
	Blob photoFile;
	
	@Column(name = "idEvent")
	private int idEvent;
	
	public Photo(){
		
	}
	
	public Photo(Blob photo, int idEvent){
		this.photoFile = photo;
		this.idEvent = idEvent;
	}
	
	public Blob getPhotoFile(){
		return this.photoFile;
	}
	
	public int getIdEvent(){
		return this.idEvent;
	}
	
	public void setPhotoFile(Blob photo){
		this.photoFile = photo;
	}
	
	public void setIdEvent(int id){
		this.idEvent = id;
	}
}
