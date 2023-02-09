package org.maox.wampire.servers;

import java.net.MalformedURLException;
import java.net.URL;

import org.maox.wampire.ImageServer;

public class ImgStudio extends ImageServer {

	@Override
	public boolean canProcess(URL link) {
		if (link.getHost().equals("imgstudio.org"))
			return true;
		return false;
	}

	@Override
	public URL decodeURL(URL link) {
		// TODO Auto-generated method stub
		URL result = null;

		try {
			result = new URL("");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

}
