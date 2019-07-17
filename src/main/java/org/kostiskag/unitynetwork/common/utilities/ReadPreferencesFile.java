package org.kostiskag.unitynetwork.common.utilities;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.function.Supplier;

/**
 *
 * @author Konstantinos Kagiampakis
 */
public class ReadPreferencesFile {

	protected final Properties cfg;

	protected ReadPreferencesFile(File file) throws IOException {
		this.cfg = new Properties();
		try(InputStream fileIn = new FileInputStream(file)) {
			cfg.load(fileIn);
		}
	}

	protected static void generateFile(File file, Supplier<String> supp) throws IOException {
		try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
			writer.print(supp.get());
		};
	}
}
