package main.java.consultaCEP.domain.entities;

import java.util.Date;

public class Address {
	private String logradouro;
	private String bairro;
	private String cidade;
	private String uf;
	private String cep;
	private Date createdIn;
	private Date updatedIn;
	

	public Address(String logradouro, String bairro, String cidade, String uf,
			String cep) {
		this.logradouro = logradouro;
		this.bairro = bairro;
		this.cidade = cidade;
		this.uf = uf;
		this.cep = cep;
		this.createdIn = new Date();
		this.updatedIn = new Date();
	}

	public String getLogradouro() {
		return logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public String getUf() {
		return uf;
	}

	public String getCep() {
		return cep;
	}

	public Date getCreatedIn() {
		return createdIn;
	}

	public void setCreatedIn(Date createdIn) {
		this.createdIn = createdIn;
	}

	public Date getUpdatedIn() {
		return updatedIn;
	}

	public void setUpdatedIn(Date updatedIn) {
		this.updatedIn = updatedIn;
	}
}
