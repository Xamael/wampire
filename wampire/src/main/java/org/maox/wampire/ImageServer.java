package org.maox.wampire;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Servidor de Imagenes
 *
 * @author Alex
 *
 */
public abstract class ImageServer {

	/**
	 * Determina si el servidor puede tratar una URL
	 *
	 * @param link
	 * @return
	 */
	public abstract boolean canProcess(URL link);

	/**
	 * Obtiene una URL de imagen desde la URL codificada
	 *
	 * @param link
	 * @return
	 */
	public abstract URL decodeURL(URL link);

	/**
	 * Graba una imagen a disco desde una URL
	 *
	 * @param fileOut
	 * @param link
	 * @throws IOException
	 */
	public static void saveImage(File fileOut, URL link) throws IOException {
		InputStream in = new BufferedInputStream(link.openStream());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while (-1 != (n = in.read(buf))) {
			out.write(buf, 0, n);
		}
		out.close();
		in.close();
		byte[] response = out.toByteArray();

		FileOutputStream fos = new FileOutputStream(fileOut);
		fos.write(response);
		fos.close();
	}
}
