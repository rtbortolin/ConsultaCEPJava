package main.java.consultaCEP.infra.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

import org.apache.commons.lang3.StringEscapeUtils;

import main.java.consultaCEP.domain.entities.Address;
import main.java.consultaCEP.interfaces.ICorreiosWebAccess;

public class CorreiosWebAccess implements ICorreiosWebAccess {

	@Override
	public Address getResponse(String cep) {
		if (!cep.contains("-"))
			return null;

		String html = getHtml(cep);

		if (!html.contains("DADOS ENCONTRADOS COM SUCESSO."))
			return null;

		String logradouro = getLogradouro(html).trim();
		String bairro = getBairro(html).trim();
		String cidade = getCidade(html).trim();
		String uf = getUf(html).trim();
		String responseCep = getCep(html).trim();

		Address address = new Address(logradouro, bairro, cidade, uf, responseCep);

		return address;
	}

	private String getHtml(String cep) {
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL("http://www.buscacep.correios.com.br/sistemas/buscacep/resultadoBuscaCepEndereco.cfm");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Language", "pt-BR");
			connection.setRequestProperty("Accept-Encoding", "gzip");

			StringBuffer sbuffer = new StringBuffer();
			sbuffer.append("relaxation=" + URLEncoder.encode(cep, "UTF-8"));
			sbuffer.append("&TipoCep=" + URLEncoder.encode("ALL", "UTF-8"));
			sbuffer.append("&semelhante=" + URLEncoder.encode("N", "UTF-8"));

			String urlParameters = sbuffer.toString();
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = new GZIPInputStream(connection.getInputStream());
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("ISO-8859-1")));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();

			String strResponse = response.toString();
			strResponse = strResponse.replace("&nbsp;", "");
			strResponse = StringEscapeUtils.unescapeHtml4(strResponse);
			return strResponse;

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
		int startIndex = html.indexOf("tmptabela");
		startIndex = html.indexOf("<tr", startIndex);
		startIndex = html.indexOf("<tr", startIndex);
		startIndex = html.indexOf("<td", startIndex);
		startIndex = html.indexOf(">", startIndex) + 1;

		int endIndex = html.indexOf("</td>", startIndex);

		return html.substring(startIndex, endIndex);
	}

	private String getBairro(String html) {
		int startIndex = html.indexOf("tmptabela");
		startIndex = html.indexOf("<tr", startIndex);
		startIndex = html.indexOf("<tr", startIndex);
		startIndex = html.indexOf("<td", startIndex) + 1;
		startIndex = html.indexOf("<td", startIndex);
		startIndex = html.indexOf(">", startIndex) + 1;

		int endIndex = html.indexOf("</td>", startIndex);

		return html.substring(startIndex, endIndex);
	}

	private String getCidade(String html) {
		int startIndex = html.indexOf("tmptabela");
		startIndex = html.indexOf("<tr", startIndex);
		startIndex = html.indexOf("<tr", startIndex);
		startIndex = html.indexOf("<td", startIndex) + 1;
		startIndex = html.indexOf("<td", startIndex) + 1;
		startIndex = html.indexOf("<td", startIndex);
		startIndex = html.indexOf(">", startIndex) + 1;

		int endIndex = html.indexOf("</td>", startIndex);

		return html.substring(startIndex, endIndex).split("/")[0];
	}

	private String getUf(String html) {
		int startIndex = html.indexOf("tmptabela");
		startIndex = html.indexOf("<tr", startIndex);
		startIndex = html.indexOf("<tr", startIndex);
		startIndex = html.indexOf("<td", startIndex) + 1;
		startIndex = html.indexOf("<td", startIndex) + 1;
		startIndex = html.indexOf("<td", startIndex);
		startIndex = html.indexOf(">", startIndex) + 1;

		int endIndex = html.indexOf("</td>", startIndex);

		return html.substring(startIndex, endIndex).split("/")[1];
	}

	private String getCep(String html) {
		int startIndex = html.indexOf("tmptabela");
		startIndex = html.indexOf("<tr", startIndex);
		startIndex = html.indexOf("<tr", startIndex);
		startIndex = html.indexOf("<td", startIndex) + 1;
		startIndex = html.indexOf("<td", startIndex) + 1;
		startIndex = html.indexOf("<td", startIndex) + 1;
		startIndex = html.indexOf("<td", startIndex);
		startIndex = html.indexOf(">", startIndex) + 1;

		int endIndex = html.indexOf("</td>", startIndex);

		return html.substring(startIndex, endIndex);
	}

	private void doFirstRequest() {

	}
}
