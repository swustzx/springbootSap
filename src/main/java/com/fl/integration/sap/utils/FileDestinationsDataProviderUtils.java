package com.fl.integration.sap.utils;

import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.ServerDataEventListener;
import com.sap.conn.jco.ext.ServerDataProvider;
import com.sap.conn.jco.util.FastStringBuffer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author david
 * @create 2018-03-05 16:59
 **/
public class FileDestinationsDataProviderUtils implements DestinationDataProvider, ServerDataProvider {

	private static String DESTINATION_FILES_SUFFIX = ".jcoDestination";
	private static String SERVER_CFG_FILES_SUFFIX = ".jcoServer";
	private File destinationDirectory;

	public FileDestinationsDataProviderUtils(String directory) throws FileNotFoundException {
		File destinationDirFile = null;
		FastStringBuffer error = new FastStringBuffer(128);
		destinationDirFile = new File(directory);
		if (this.checkFile(destinationDirFile, error)) {
			this.destinationDirectory = destinationDirFile;
		} else {
			throw new FileNotFoundException(error.toString());
		}
	}

	private boolean checkFile(File file, FastStringBuffer error) {
		if (file.exists()) {
			if (file.canRead()) {
				return true;
			}

			if (error != null) {
				error.append("File ");
				error.append(file.getAbsolutePath());
				error.append(" exists, but cannot be read. ");
			}
		} else if (error != null) {
			error.append("File ");
			error.append(file.getAbsolutePath());
			error.append(" does not exist. ");
		}

		return false;
	}

	public Properties getDestinationProperties(String destinationName) {
		return this.loadProperties(destinationName, DESTINATION_FILES_SUFFIX);
	}

	public boolean supportsEvents() {

		return true;
	}

	public void setDestinationDataEventListener(DestinationDataEventListener eventListener) {
		eventListener.updated("BCE");
	}

	public Properties getServerProperties(String serverName) {
		return this.loadProperties(serverName, SERVER_CFG_FILES_SUFFIX);
	}

	public void setServerDataEventListener(ServerDataEventListener eventListener) {
	}

	private Properties loadProperties(String destinationName, String suffix) {
		File destinationFile = new File(this.destinationDirectory, destinationName + suffix);
		FastStringBuffer buf = new FastStringBuffer(256);
		if (!this.checkFile(destinationFile, buf)) {
			throw new RuntimeException(buf.toString());
		} else {
			FileInputStream fis = null;
			Properties properties = new Properties();

			Properties var7;
			try {
				properties.load(fis = new FileInputStream(destinationFile));
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
