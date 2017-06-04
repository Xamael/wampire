package org.maox.wampire.web;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Página Web HTML
 *
 * @author Alex
 *
 */
public class WebPage {

	private URL address; /* Dirección de la página */
	private String htmlCode; /* Código HTML de la página */
	List<URL> listLinks; /* links de la pagina */
	boolean bReady = false;

	/**
	 * Constructor desde una cadena con la pagina web
	 *
	 * @param webPage
	 * @throws MalformedURLException
	 */
	public WebPage(String webPage) throws MalformedURLException {
		this(new URL(webPage));
	}

	/**
	 * Constructor desde una URL
	 *
	 * @param webpage
	 */
	public WebPage(URL webpage) {
		address = webpage;
	}

	/**
	 * Devuelve los links scaneados anteriormente por la pagina
	 *
	 * @return
	 */
	public List<URL> getLinks() {
		return listLinks;
	}

	/**
	 * Lee el cógido HTML de la página web
	 *
	 * @throws IOException
	 */
	public void readHTML() throws IOException {
		// Conexión de lectura
		StringBuilder builder = new StringBuilder();
		URLConnection uc = address.openConnection();
		uc.setRequestProperty("User-Agent", "Mozilla/5.0");
		uc.getInputStream();
		BufferedInputStream in = new BufferedInputStream(uc.getInputStream());

		int ch;
		while ((ch = in.read()) != -1) {
			builder.append((char) ch);
		}

		in.close();
		htmlCode = builder.toString();
	}

	/**
	 * Obtiene los enlaces de la página
	 *
	 * @return
	 */
	public void scanLinks() {

		listLinks = new ArrayList<URL>();
		Pattern pattern = Pattern.compile("(?i)HREF\\s*=\\s*\"(.*?)\"");
		Matcher matcher = pattern.matcher(htmlCode);
		while (matcher.find()) {
			try {
				listLinks.add(new URL(matcher.group(1)));
			} catch (MalformedURLException e) {
			}
		}
	}
}
