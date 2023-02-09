package org.maox.wampire;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.maox.wampire.servers.AcidImg;
import org.maox.wampire.servers.CoreImg;
import org.maox.wampire.servers.ImageBam;
import org.maox.wampire.servers.ImageFolks;
import org.maox.wampire.servers.ImagePorter;
import org.maox.wampire.servers.ImageTwist;
import org.maox.wampire.servers.ImageVenue;
import org.maox.wampire.servers.ImgBB;
import org.maox.wampire.servers.ImgBabes;
import org.maox.wampire.servers.ImgBox;
import org.maox.wampire.servers.ImgCandy;
import org.maox.wampire.servers.ImgChili;
import org.maox.wampire.servers.ImgClick;
import org.maox.wampire.servers.ImgFlare;
import org.maox.wampire.servers.ImgSpice;
import org.maox.wampire.servers.ImgStudio;
import org.maox.wampire.servers.ImgTRex;

/**
 * Pool de servidores
 *
 * @author Alex
 *
 */
public class ServerPool {

	// Patron singleton
	private static ServerPool _instance = null;
	private List<ImageServer> serverList;

	/**
	 * Constructor que instancia los posibles servidores
	 */
	private ServerPool() {
		serverList = new ArrayList<ImageServer>();
		serverList.add(new AcidImg());
		serverList.add(new CoreImg());
		serverList.add(new ImageBam());
		serverList.add(new ImageFolks());
		serverList.add(new ImagePorter());
		serverList.add(new ImageTwist());
		serverList.add(new ImageVenue());
		serverList.add(new ImgBabes());
		serverList.add(new ImgBB());
		serverList.add(new ImgBox());
		serverList.add(new ImgCandy());
		serverList.add(new ImgChili());
		serverList.add(new ImgClick());
		serverList.add(new ImgFlare());
		serverList.add(new ImgSpice());
		serverList.add(new ImgStudio());
		serverList.add(new ImgTRex());

	}

	/**
	 * Obtiene el servidor que corresponde a la URL pasada
	 *
	 * @param link
	 * @return
	 */
	public ImageServer getServer(URL link) {
		for (ImageServer server : serverList) {
			if (server.canProcess(link))
				return server;
		}
		return null;
	}

	/**
	 * Devuelve la instancia del Pool de Servidores
	 *
	 * @return
	 */
	public static ServerPool getInstance() {
		if (_instance == null)
			_instance = new ServerPool();

		return _instance;
	}

}
