package org.maox.wampire;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.maox.wampire.gui.AppFrame;
import org.maox.wampire.web.WebPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Emulador de 8 Bits
 *
 * @author Alex Orgaz
 *
 */
public class Wampire {

	/* Log */
	final private static Logger logger = LoggerFactory.getLogger(Wampire.class);

	/* Marco visual de ejecución */
	private AppFrame app;

	/* Directorio temporal */
	final String dirTemp = "D:\\Alex\\Temp\\";

	/* Servidores Imagenes */
	private ServerPool pool;

	/* Tareas pendientes */
	private final int MAX_THREAD = 2;
	private int threadBussy = 0;

	/* Listas de Links */
	/* Lista de URLs scaneadas */
	List<URL> listDetected = null;
	/* Lista de URL detectadas */
	List<URL> listImages = null;
	/* Cola de peticiones pendientes */
	Queue<URL> queueWorks = null;

	/**
	 * Constructor
	 *
	 * @throws IOException
	 */
	public Wampire() {
		init();
		/* Inicialización del entorno gráfico */
		initGUI();
	}

	/**
	 * Salida de la aplicación
	 */
	public void exit() {
		System.exit(0);
	}

	/**
	 * Inicialización básica
	 */
	private void init() {
		pool = ServerPool.getInstance();
	}

	/**
	 * Inialización del entorno gráfico
	 */
	private void initGUI() {
		final Wampire wampire = this;
		/* Inicializa el entorno gráfico */
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				app = new AppFrame(wampire);
				app.pack();
				// Centrado de ventana
				app.setLocationRelativeTo(null);
				app.setVisible(true);
			}
		});

		// Esperar hasta que se cree el GUI visual
		while (app == null) {
		}
	}

	/**
	 * Empieza el escaneo de la URL objetivo
	 *
	 * @param initAddress
	 * @param rootAddress
	 */
	public void startScan(String initAddress, String rootAddress) {

		logger.info("Scan: {}", initAddress);

		/* Lista de URLs scaneadas */
		listDetected = new ArrayList<URL>();
		/* Lista de URL detectadas */
		listImages = new ArrayList<URL>();
		/* Cola de peticiones pendientes */
		queueWorks = new ConcurrentLinkedQueue<URL>();

		try {
			// Inicializar rutas
			URL initURL = new URL(initAddress);
			URL rootURL = null;

			if (rootAddress == null)
				rootURL = initURL;
			else
				rootURL = new URL(rootAddress);

			// Se introduce la petición en la pila de peticiones
			queueWorks.add(initURL);

			// TODO
			for (int idx = 0; idx <= 8; idx = idx + 1) {
				// initAddress

				URL link = new URL("https://" + idx);
				queueWorks.add(link);
			}

			// Mientras haya peticiones o threads activos
			while (!queueWorks.isEmpty() || threadBussy != 0) {
				// Si no hya trabajos aún se duerme
				if (queueWorks.isEmpty()) {
					Thread.yield();
					continue;
				}

				// Se extrae la siguiente petición
				URL work = queueWorks.peek();
				// logger.debug("Scaning: {}", work);

				// Si es un servidor de imagenes se almacena (si no se ha procesado ya)
				boolean bDetec = listDetected.contains(work);
				boolean bImage = listImages.contains(work);

				if (!bImage && pool.getServer(work) != null) {
					// Tratamiento link imagen
					// logger.debug("Detected: {}", work);
					listImages.add(work);
					queueWorks.remove();
				}
				// Si es una pagina descendiente del root se procesa
				else if (!bDetec && work.toString().startsWith(rootURL.toString())) {
					// Si ya hay demasiados threads abiertos no se procesa
					if (MAX_THREAD >= threadBussy) {
						logger.debug("Launch Scan: {}", work);
						ScanWeb thScan = new ScanWeb(work);
						(new Thread(thScan)).start();
						threadBussy++;
						queueWorks.remove();
						listDetected.add(work);
					}
				}
				// links repetidos
				else if (bDetec || bImage) {
					queueWorks.remove();
				}
				// Link invalido
				else {
					logger.debug("Fail: {}", work);
					queueWorks.remove();
				}

			} // Bucle principal

			// Se imprimen todas las imagenes encontradas
			PrintWriter pw = new PrintWriter(new File(dirTemp + "links.txt"));

			System.out.println("Imagenes encontradas");
			for (URL image : listImages) {
				pw.println(image);
				System.out.println(image);
			}

			pw.close();
			// URL decodeURL = server.decodeURL(link);
			// String path = decodeURL.getPath();
			// logger.debug("Decoded: {}", path);

			// String fileName = dirTemp +
			// path.substring(path.lastIndexOf("/"));
			// logger.debug("Saved in : {}", fileName);

			// ImageServer.saveImage(new File(fileName), decodeURL);

		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * Obtiene el tiempo actual en milisegundos a partir del reloj de sistema de alta resolución
	 *
	 * @return El tiempo en milisegundos
	 */
	static public long getTimeMilis() {
		return (long) (System.nanoTime() / 1e6);
	}

	/**
	 * Manejador de Excepciones
	 *
	 * @param e Excepcion a controlar
	 */
	public static void handleException(Exception e) {
		logger.error(e.toString());

		StackTraceElement[] arrayOfStackTraceElement = e.getStackTrace();
		for (StackTraceElement element : arrayOfStackTraceElement) {
			logger.error("\tat {}", element);
		}
	}

	/**
	 * Programa Lanzador del wampire
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new Wampire();
		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * Thread scaneador de la pagina
	 *
	 * @author Alex
	 *
	 */
	private class ScanWeb implements Runnable {

		/* Pagina que escanea */
		private WebPage page;

		public ScanWeb(URL link) {
			page = new WebPage(link);
		}

		public void run() {

			try {
				logger.debug("Scan: {}", page);
				page.readHTML();
				page.scanLinks();
				for (URL link : page.getLinks()) {
					if (!listDetected.contains(link) && !listImages.contains(link))
						queueWorks.add(link);
				}

				threadBussy--;
			} catch (IOException e) {
				threadBussy--;
				handleException(e);
			}

		}
	}
}
