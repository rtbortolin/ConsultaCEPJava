package consultaCEP.domain.entities;

public class Address {
	private String logradouro;
	private String bairro;
	private String cidade;
	private String uf;
	private String cep;

	public Address(String logradouro, String bairro, String cidade, String uf,
			String cep) {
		this.logradouro = logradouro;
		this.bairro = bairro;
		this.cidade = cidade;
		this.uf = uf;
		this.cep = cep;
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
}
