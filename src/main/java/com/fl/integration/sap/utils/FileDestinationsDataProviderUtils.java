package com.fl.integration.sap.utils;

import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.ServerDataEventListener;
import com.sap.conn.jco.ext.ServerDataProvider;
import com.sap.conn.jco.util.FastStringBuffer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author david
 * @create 2018-03-05 16:59
 **/
public class FileDestinationsDataProviderUtils implements DestinationDataProvider, ServerDataProvider {

	private static String DESTINATION_FILES_SUFFIX = ".jcoDestination";
	private static String SERVER_CFG_FILES_SUFFIX = ".jcoServer";
	private File destinationDirectory;

	public FileDestinationsDataProviderUtils() throws FileNotFoundException {
		File destinationDirFile = null;
		FastStringBuffer error = new FastStringBuffer(128);
		//	destinationDirFile = new File(directory);
//		if (this.checkFile(destinationDirFile, error)) {
//			this.destinationDirectory = destinationDirFile;
//		} else {
////			throw new FileNotFoundException(error.toString());
//		}
	}

	public void setServerDataEventListener(ServerDataEventListener eventListener) {
		eventListener.updated("MYSERVER");
	}

	public Properties getDestinationProperties(String destinationName) {
		return this.loadProperties(destinationName, DESTINATION_FILES_SUFFIX);
	}

	private boolean checkFile(Resource resource, FastStringBuffer error) {
		try {
			if (resource.exists()) {
				if (resource.isReadable()) {
					return true;
				}

				if (error != null) {
					error.append("File ");

					error.append(resource.getFile().getAbsolutePath());

					error.append(" exists, but cannot be read. ");
				}
			} else if (error != null) {
				error.append("File ");
				error.append(resource.getFile().getAbsolutePath());
				error.append(" does not exist. ");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public void setDestinationDataEventListener(DestinationDataEventListener eventListener) {
		eventListener.updated("BCE");
	}

	public Properties getServerProperties(String serverName) {
		return this.loadProperties(serverName, SERVER_CFG_FILES_SUFFIX);
	}

	public boolean supportsEvents() {

		return false;
	}

	private Properties loadProperties(String destinationName, String suffix) {

		Resource resource = new ClassPathResource(destinationName + suffix);

		FastStringBuffer buf = new FastStringBuffer(256);
		if (!this.checkFile(resource, buf)) {
			throw new RuntimeException(buf.toString());
		} else {
			InputStreamReader fis = null;
			Properties properties = new Properties();

			Properties var7;
			try {
				properties.load(fis = new InputStreamReader(resource.getInputStream()));
				var7 = properties;
			} catch (IOException var16) {
				throw new RuntimeException("Unable to load the destination properties", var16);
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (Exception var15) {
						;
					}
				}

			}

			return var7;
		}
	}
}
