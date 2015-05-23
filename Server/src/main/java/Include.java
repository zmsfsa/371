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
	
	@Column(name = "idUser")
	private int idUser;
	
	public Include(){
		
	}
	
	public Include(int idEvent, int idUser){
		this.idEvent = idEvent;
		this.idUser = idUser;
	}
	
	public int getIdEvent(){
		return this.idEvent;
	}
	
	public int getIdUser(){
		return this.idUser;
	}
	
	public void setIdUser(int id){
		this.idUser = id;
	}
	
	public void setIdEvent(int id){
		this.idEvent = id;
	}
}
