package main.java.consultaCEP.infra.services;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;

import main.java.consultaCEP.domain.entities.Address;
import main.java.consultaCEP.interfaces.*;

public class CorreiosWebAccess implements ICorreiosWebAccess {

	@Override
	public Address getResponse(String cep) {
		if (!cep.contains("-"))
			return null;

		String html = getHtml(cep);

		if (!html.contains("Logradouro(s)"))
			return null;

		String logradouro = getLogradouro(html);
		String bairro = getBairro(html);
		String cidade = getCidade(html);
		String uf = getUf(html);
		String responseCep = getCep(html);

		Address address = new Address(logradouro, bairro, cidade, uf,
				responseCep);

		return address;
	}

	private String getHtml(String cep) {
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(
					"http://www.buscacep.correios.com.br/servicos/dnec/consultaEnderecoAction.do");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Language", "pt-BR");

			StringBuffer sbuffer = new StringBuffer();
			sbuffer.append("relaxation=" + URLEncoder.encode(cep, "UTF-8"));
			sbuffer.append("&TipoCep=" + URLEncoder.encode("ALL", "UTF-8"));
			sbuffer.append("&semelhante=" + URLEncoder.encode("N", "UTF-8"));
			sbuffer.append("&cfm=" + URLEncoder.encode("1", "UTF-8"));
			sbuffer.append("&Metodo="
					+ URLEncoder.encode("listaLogradouro", "UTF-8"));
			sbuffer.append("&TipoConsulta="
					+ URLEncoder.encode("relaxation", "UTF-8"));
			sbuffer.append("&StartRow=" + URLEncoder.encode("1", "UTF-8"));
			sbuffer.append("&EndRow=" + URLEncoder.encode("10", "UTF-8"));

			String urlParameters = sbuffer.toString();

			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("ISO-8859-1")));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();

		} catch (Exception e) {

			e.printStackTrace();
			return null;

		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private String getLogradouro(String html) {
		int startIndex = html.indexOf("<!-- Fim cabecalho da tabela -->");
		startIndex = html.indexOf("<table", startIndex);
		startIndex = html.indexOf("<tr", startIndex);
		startIndex = html.indexOf("<td", startIndex);
		startIndex = html.indexOf(">", startIndex) + 1;

		int endIndex = html.indexOf("</td>", startIndex);

		return html.substring(startIndex, endIndex);
	}

	private String getBairro(String html) {
		int startIndex = html.indexOf("<!-- Fim cabecalho da tabela -->");
		startIndex = html.indexOf("<table", startIndex);
		startIndex = html.indexOf("<tr", startIndex);
		startIndex = html.indexOf("<td", startIndex) + 1;
		startIndex = html.indexOf("<td", startIndex);
		startIndex = html.indexOf(">", startIndex) + 1;

		int endIndex = html.indexOf("</td>", startIndex);

		return html.substring(startIndex, endIndex);
	}

	private String getCidade(String html) {
		int startIndex = html.indexOf("<!-- Fim cabecalho da tabela -->");
		startIndex = html.indexOf("<table", startIndex);
		startIndex = html.indexOf("<tr", startIndex);
		startIndex = html.indexOf("<td", startIndex) + 1;
		startIndex = html.indexOf("<td", startIndex) + 1;
		startIndex = html.indexOf("<td", startIndex);
		startIndex = html.indexOf(">", startIndex) + 1;

		int endIndex = html.indexOf("</td>", startIndex);

		return html.substring(startIndex, endIndex);
	}

	private String getUf(String html) {
		int startIndex = html.indexOf("<!-- Fim cabecalho da tabela -->");
		startIndex = html.indexOf("<table", startIndex);
		startIndex = html.indexOf("<tr", startIndex);
		startIndex = html.indexOf("<td", startIndex) + 1;
		startIndex = html.indexOf("<td", startIndex) + 1;
		startIndex = html.indexOf("<td", startIndex) + 1;
		startIndex = html.indexOf("<td", startIndex);
		startIndex = html.indexOf(">", startIndex) + 1;

		int endIndex = html.indexOf("</td>", startIndex);

		return html.substring(startIndex, endIndex);
	}

	private String getCep(String html) {
		int startIndex = html.indexOf("<!-- Fim cabecalho da tabela -->");
		startIndex = html.indexOf("<table", startIndex);
		startIndex = html.indexOf("<tr", startIndex);
		startIndex = html.indexOf("<td", startIndex) + 1;
		startIndex = html.indexOf("<td", startIndex) + 1;
		startIndex = html.indexOf("<td", startIndex) + 1;
		startIndex = html.indexOf("<td", startIndex) + 1;
		startIndex = html.indexOf("<td", startIndex);
		startIndex = html.indexOf(">", startIndex) + 1;

		int endIndex = html.indexOf("</td>", startIndex);

		return html.substring(startIndex, endIndex);
	}

}
