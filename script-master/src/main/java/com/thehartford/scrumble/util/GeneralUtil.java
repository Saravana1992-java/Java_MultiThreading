package com.thehartford.scrumble.util;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class GeneralUtil {

	public static Properties getProperties(String fileName) {
		Properties prop = new Properties();
		InputStream is = null;
		try {
			File destFile = new File(fileName);
			if (!destFile.exists()) {
				System.out.println("Required configuration is missing in " + fileName);
				is = new GeneralUtil().getClass().getClassLoader().getResourceAsStream(destFile.getName());
				File reportDir = new File(GeneralConstants.REPORT_DIR);
				boolean isReportDirExists = reportDir.exists() ? true : reportDir.mkdirs();
				System.out.println(reportDir + " was created " + isReportDirExists);
				System.out.println("Copying configuration to " + fileName);
				IOUtils.copy(is, new FileOutputStream(destFile));
			}
			is = new FileInputStream(destFile);
			prop.load(is);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

	public static String getInputFromConsole(int option, String msg) {
		Console console = System.console();
		Scanner scanner = null;
		if (console == null) {
			System.err.println("Couldn't get Console instance");
			System.out.println(msg);
			scanner = new Scanner(System.in);
		}
		return getUserInput(option, msg, console, scanner);
	}

	private static String getUserInput(int option, String msg, Console console, Scanner scanner) {
		try {
			switch (option) {
			case 0:
				return (console != null) ? console.readLine(msg) : scanner.nextLine();
			case 1:
				return (console != null) ? new String(console.readPassword(msg)) : scanner.nextLine();
			default:
				return null;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	public static List<String> fileReaderByLine(File file, String encoding) throws IOException {
		return FileUtils.readLines(file, encoding);
	}

	public static boolean isCollectionEmpty(Collection<?> col) {
		return (col == null || col.isEmpty() || (col.size() < 0)) ? true : false;
	}

	public static boolean isMapEmpty(Map<?, ?> map) {
		return (map == null || map.isEmpty() || (map.size() < 0)) ? true : false;
	}

	public static boolean isStringEmpty(String s) {
		return (s == null || s.isEmpty()) ? true : false;
	}

	public static void exitSystem(String msg) {
		System.out.println(msg);
		System.exit(0);
	}

}
