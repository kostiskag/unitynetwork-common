package org.kostiskag.unitynetwork.common.utilities;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import java.util.function.Supplier;

/**
 *
 * @author Konstantinos Kagiampakis
 */
public class ReadPreferencesFile {

	protected @Deprecated final Properties cfg;

	@Deprecated
	protected ReadPreferencesFile(File file) throws IOException {
		cfg = readPreferencesFile(file.toPath());
	}

	protected static Properties readPreferencesFile(Path filePath) throws IOException {
		var prop = new Properties();
		try(var fileIn = Files.newInputStream(filePath, StandardOpenOption.READ)) {
			prop.load(fileIn);
		}
		return prop;
	}

	@Deprecated
	protected static void generateFile(File file, Supplier<String> supp) throws IOException {
		generateFile(file.toPath(), supp);
	}

	protected static void generateFile(Path filePath, Supplier<String> supp) throws IOException {
		try (PrintWriter writer = new PrintWriter(Files.newOutputStream(filePath, StandardOpenOption.WRITE))) {
			writer.print(supp.get());
		};
	}
}
