package main.java;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "partyfinderdb.friends")
public class Friend {
	
	public Friend(){
		
	}
	
	public Friend(int idFirst, int idSecond){
		this.idFirst = idFirst;
		this.idSecond = idSecond;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idFriendship")
	private int idFriendship;
	
	@Column(name = "idFirst")
	private int idFirst;
	
	@Column(name = "idSecond")
	private int idSecond;
	
	public int getIdFirst(){
		return this.idFirst;
	}
	
	public void setIdFirst(int id){
		this.idFirst = id;
	}
	
	public int getIdSecond(){
		return this.idSecond;
	}
	
	public void setIdSecond(int id){
		this.idSecond = id;
	}
	
	public int getIdFriendship(){
		return this.idFriendship;
	}
	
	public void setIdFriendship(int id){
		this.idFriendship = id;
	}
	
	

}
