package com.thehartford.scrumble.util;

import java.io.File;
import java.text.SimpleDateFormat;

public class GeneralConstants {

	public static final String CONFIG_FILE_NAME = "config.properties";
	public static final String FILE_SEPARATOR = File.separator;
	public static final String LINE_SEPARATOR = System.lineSeparator();
	public static final String REPORT_DIR = "c:" + FILE_SEPARATOR + "project" + FILE_SEPARATOR;
	public static final String CONFIG_FILE_PATH = REPORT_DIR+"config.properties";
	public static final String INPUT_FILE = REPORT_DIR + "US-numbers.txt";
	public static final String MAPPING_FILES_DIR = REPORT_DIR + "mappingDocs";
	public static final String SCRIPTS_DIR = REPORT_DIR + "generated_scripts"+FILE_SEPARATOR;
	public static final String SCRIPTS_FILE_EXTN = ".sql";
	public static final SimpleDateFormat mmddyyyyformatter =new SimpleDateFormat("MM/dd/yyyy");
	public static final int TOTAL_THREADS = 3;

	// script tags
	
	
}
