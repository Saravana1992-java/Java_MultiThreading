package com.thehartford.scrumble.libs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import com.thehartford.scrumble.util.Config;
import com.thehartford.scrumble.util.GeneralConstants;
import com.thehartford.scrumble.util.GeneralUtil;
import com.thehartford.scrumble.util.HigNTCredentials;

public class ScriptProcessor implements Runnable {

	private Workbook workbook;
	private String mappingDir = GeneralConstants.MAPPING_FILES_DIR;
	private int sheetAt;
	private int rowStart;
	private int rowEnd;
	private static HashMap<String, String> configMap = Config.getInstance().getConfigMap();
	private ArrayList<String> inputList = new ArrayList<String>();
	//private String username;
	//private String password;
	private String mapfileName;
	private String url;

	private ScriptProcessor() {

	}

	public ScriptProcessor(String mapfileName, String url, int sheetAt, int rowStart,
			int rowEnd, ArrayList<String> inputList) throws EncryptedDocumentException, IOException {
		this.sheetAt = sheetAt;
		this.inputList = inputList;
		//this.username = username;
		//this.password = password;
		this.mapfileName = mapfileName;
		this.url = url;
		System.out.println("In ScriptProcessor mapfileName = " + mapfileName);
	}

	public Map<String, ArrayList<String>> process(String mappingFileName)
			throws EncryptedDocumentException, IOException, InvalidFormatException {
		Map<String, ArrayList<String>> scriptMap = new HashMap<String, ArrayList<String>>();
		workbook = WorkbookFactory.create(new File(mappingFileName));
		Sheet sheet = workbook.getSheet("NewCO Auto ");
		System.out.println("sheet.getSheetName() = " + sheet.getSheetName());
		String higcodemapScript = "";

		sheet.forEach(row -> {
			int rowNum = row.getRowNum();
			if (rowNum > 0) {
				int cellnum = 4;
				String userStoryNo = row.getCell(cellnum).getStringCellValue();
				if (inputList.contains(userStoryNo)) {
					// insertScript
					for (int i = 1; i <= 5; i++) {
						prepareStatement(i, row, scriptMap);
					}
				}
			}
		});
		if (workbook != null) {
			workbook.close();
		}
		return scriptMap;
	}

	private Map<String, ArrayList<String>> prepareStatement(int option, Row row,
			Map<String, ArrayList<String>> scriptMap) {
		String script = "";
		String userStoryNo = row.getCell(4).getStringCellValue();
		String key1 = userStoryNo + "-" + GeneralConstants.HIG_CODE_MAPPING_TABLE_NAME;
		String key2 = userStoryNo + "-" + GeneralConstants.HIG_COMML_POL_COV_MAP_TABLE_NAME;
		String key3 = userStoryNo + "-" + GeneralConstants.HIG_POL_COVG_CONFIG_TABLE_NAME;

		switch (option) {

		case 1:
			script = String.format(GeneralConstants.HIG_CODE_MAPPING_INSERT_STMT_1, userStoryNo, userStoryNo);
			if (scriptMap.containsKey(key1)) {
				ArrayList<String> scriptList1 = scriptMap.get(key1);
				scriptList1.add(script);
				scriptMap.put(key1, scriptList1);
			} else {
				ArrayList<String> scriptList1 = new ArrayList<String>();
				scriptList1.add(script);
				scriptMap.put(key1, scriptList1);
			}
			break;
		case 2:
			script = String.format(GeneralConstants.HIG_CODE_MAPPING_INSERT_STMT_2, userStoryNo, userStoryNo);
			if (scriptMap.containsKey(key1)) {
				ArrayList<String> scriptList1 = scriptMap.get(key1);
				scriptList1.add(script);
				scriptMap.put(key1, scriptList1);
			} else {
				ArrayList<String> scriptList1 = new ArrayList<String>();
				scriptList1.add(script);
				scriptMap.put(key1, scriptList1);
			}
			break;
		case 3:
			script = String.format(GeneralConstants.HIG_CODE_MAPPING_INSERT_STMT_3, userStoryNo, userStoryNo);
			if (scriptMap.containsKey(key1)) {
				ArrayList<String> scriptList1 = scriptMap.get(key1);
				scriptList1.add(script);
				scriptMap.put(key1, scriptList1);
			} else {
				ArrayList<String> scriptList1 = new ArrayList<String>();
				scriptList1.add(script);
				scriptMap.put(key1, scriptList1);
			}
			break;
		case 4:
			script = String.format(GeneralConstants.HIG_COMML_POL_COV_MAP_INSERT_STMT, userStoryNo, userStoryNo,
					userStoryNo, userStoryNo, userStoryNo);
			if (scriptMap.containsKey(key2)) {
				ArrayList<String> scriptList2 = scriptMap.get(key2);
				scriptList2.add(script);
				scriptMap.put(key2, scriptList2);
			} else {
				ArrayList<String> scriptList2 = new ArrayList<String>();
				scriptList2.add(script);
				scriptMap.put(key2, scriptList2);
			}
			break;
		case 5:
			script = String.format(GeneralConstants.HIG_POL_COVG_CONFIG_INSERT_STMT, userStoryNo, userStoryNo,
					userStoryNo, userStoryNo, userStoryNo, userStoryNo, userStoryNo, userStoryNo);
			if (scriptMap.containsKey(key3)) {
				ArrayList<String> scriptList3 = scriptMap.get(key3);
				scriptList3.add(script);
				scriptMap.put(key3, scriptList3);
			} else {
				ArrayList<String> scriptList3 = new ArrayList<String>();
				scriptList3.add(script);
				scriptMap.put(key3, scriptList3);
			}
			break;
		default:
			break;
		}
		return scriptMap;
	}

	private String downloadMappingDoc(String mappingURL, String file) throws Exception {
		URL url = new URL(mappingURL);
		URLConnection connection = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection) connection;
		httpConn.setRequestProperty("Content-Type",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=utf-8");
		httpConn.setRequestMethod("GET");

		// Read the response.
		httpConn.connect();
		// for endpoint = " + wsURL);
		int respCode = httpConn.getResponseCode();
		System.out.println("respCode = "+respCode);
		try {
			if (respCode == HttpURLConnection.HTTP_OK) {
				System.out.println("Writing " + file + " to " + mappingDir);
				File f = new File(file);
				File ff = new File(mappingDir, f.getName()); // target
				// writing the byte array into a file using Apache Commons IO
				synchronized (ff) {
					FileUtils.writeByteArrayToFile(ff, IOUtils.toByteArray(httpConn.getInputStream()));
				}
			} else {
				throw new Exception("Problem while receiving " + file + " httpcode : " + respCode);
			}
		} finally {
			if (httpConn != null)
				httpConn.disconnect();
		}
		return mappingDir + GeneralConstants.FILE_SEPARATOR + file;

	}

	private boolean write(Map<String, ArrayList<String>> scriptMap) {
		boolean result = false;
		try {
			System.out.println("================ scriptMap size = " + scriptMap.size() + " ================");
			for (Map.Entry<String, ArrayList<String>> mapEntry : scriptMap.entrySet()) {
				String[] key = mapEntry.getKey().split(configMap.get("delimitter.hiphen"));
				String userStoryNo = key[0];
				String tableName = key[1];
				String today = GeneralConstants.mmddyyyyformatter.format(new Date());
				StringBuffer path = new StringBuffer(GeneralConstants.SCRIPTS_DIR);
				path.append(userStoryNo);
				String destDirName = path.toString();
				File destDir = new File(destDirName);
				File scriptFile = null;
				StringBuffer scriptFileName = new StringBuffer();
				String scriptHeader;
				boolean isDestDirExists = destDir.exists() ? true : destDir.mkdirs();
				System.out.println(destDirName + " was created " + isDestDirExists);
				StringBuffer sb = new StringBuffer();
				switch (tableName) {
				case GeneralConstants.HIG_CODE_MAPPING_TABLE_NAME:
					scriptFileName.append(String.format(GeneralConstants.SCRIPT_FILE_NAME, tableName));
					scriptFileName.append(configMap.get("delimitter.underscore"));
					scriptFileName.append(userStoryNo);
					scriptFileName.append(GeneralConstants.SCRIPTS_FILE_EXTN);
					scriptFile = new File(destDir, scriptFileName.toString());
					scriptHeader = String.format(GeneralConstants.HIG_CODE_MAPPING_HEADER_MSG, tableName, "sample-fillup",
							today, "");
					sb.append(scriptHeader);
					sb.append(mapEntry.getValue().stream().map(String::valueOf).collect(Collectors.joining()));
					result = true;
					break;
				case GeneralConstants.HIG_COMML_POL_COV_MAP_TABLE_NAME:
					scriptFileName.append(String.format(GeneralConstants.SCRIPT_FILE_NAME, tableName));
					scriptFileName.append(configMap.get("delimitter.underscore"));
					scriptFileName.append(userStoryNo);
					scriptFileName.append(GeneralConstants.SCRIPTS_FILE_EXTN);
					scriptFile = new File(destDir, scriptFileName.toString());
					scriptHeader = String.format(GeneralConstants.HIG_CODE_MAPPING_HEADER_MSG, tableName, "sample-fillup",
							today, "");
					sb.append(scriptHeader);
					sb.append(mapEntry.getValue().stream().map(String::valueOf).collect(Collectors.joining()));
					result = true;
					break;
				case GeneralConstants.HIG_POL_COVG_CONFIG_TABLE_NAME:
					scriptFileName.append(String.format(GeneralConstants.SCRIPT_FILE_NAME, tableName));
					scriptFileName.append(configMap.get("delimitter.underscore"));
					scriptFileName.append(userStoryNo);
					scriptFileName.append(GeneralConstants.SCRIPTS_FILE_EXTN);
					scriptFile = new File(destDir, scriptFileName.toString());
					scriptHeader = String.format(GeneralConstants.HIG_CODE_MAPPING_HEADER_MSG, tableName, "sample-fillup",
							today, "");
					sb.append(scriptHeader);
					sb.append(mapEntry.getValue().stream().map(String::valueOf).collect(Collectors.joining()));
					result = true;
					break;

				default:
					break;
				}
				if (result) {
					synchronized (scriptFile) {
						boolean isDeleted = Files.deleteIfExists(scriptFile.toPath());
						System.out.println(scriptFile.toPath() + " will be overwriten " + isDeleted);
						FileUtils.writeByteArrayToFile(scriptFile, sb.toString().getBytes());
					}
				} else {
					result = false;
				}
				// System.out.println("================== END " +
				// mapEntry.getKey() +
				// "==================");

			}
		} catch (Exception e) {
			// TODO: handle exception
			result = false;
		}
		return result;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String mappingDirName = GeneralConstants.MAPPING_FILES_DIR;
		String mappingFileName = mappingDirName + GeneralConstants.FILE_SEPARATOR + mapfileName;
		boolean isRefreshEnabled = "y".equalsIgnoreCase(configMap.get("is.refresh.enabled"));
		File mapDir = new File(mappingFileName);
		boolean isMapDocExists = mapDir.exists();
		try {
			if (isRefreshEnabled) {
					mappingFileName = downloadMappingDoc(url, mapfileName);
			} else {
				if (isMapDocExists) {
					mappingFileName = mappingDirName + GeneralConstants.FILE_SEPARATOR + mapfileName;
				} else if (!isMapDocExists) {
					System.out.println("File: " + mappingFileName + " does not exists. So downloading...");
					mappingFileName = downloadMappingDoc(url, mapfileName);
				} else {
					GeneralUtil.exitSystem("May be " + mappingFileName
							+ " is not available and system is not configured for refresh... So exiting");
				}
			}
			System.out.println(write(process(mappingFileName)) ? "Scripts are generated successfully!!"
					: "Scripts generation failed!!");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
