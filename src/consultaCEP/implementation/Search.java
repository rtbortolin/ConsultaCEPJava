package consultaCEP.implementation;

import java.util.Date;

import org.bson.types.ObjectId;

public class Search {
	private ObjectId id;
	private Date createdIn;
	private String cep;

	public Search(ObjectId id, Date createdIn, String cep) {
		this.id = id;
		this.createdIn = createdIn;
		this.cep = cep;
	}
	
	public Search(String cep) {
		this.createdIn = new Date();
		this.cep = cep;
	}

	public ObjectId getId() {
		return id;
	}

	public Date getCreatedIn() {
		return createdIn;
	}

	public String getCep(){
		return cep;
	}
}
