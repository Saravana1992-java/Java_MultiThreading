package com.thehartford.scrumble.libs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import com.thehartford.scrumble.util.Config;
import com.thehartford.scrumble.util.GeneralConstants;
import com.thehartford.scrumble.util.GeneralUtil;

public class ScriptMaster {
	private static HashMap<String, String> configMap = null;

	private enum Lob {
		PR, GL, AUTO
	}

	public static void main(String[] args) {
		String inputFileName = GeneralConstants.INPUT_FILE;
		List<String> inputList = new ArrayList<String>();
		try {
			configMap = Config.getInstance().getConfigMap();
			String consoleMsg = "Required inputs are not available in " + inputFileName
					+ GeneralConstants.LINE_SEPARATOR
					+ "Enter the US/DE numbers(Ex: USXXXXX-PR,USXXXXX-GL,USXXXXX-AUTO): ";
			if (configMap != null) {
				try {
					File inputFile = new File(inputFileName);
					boolean areInputsAvailableInFile = inputFile.exists();
					if (areInputsAvailableInFile) {
						inputList = GeneralUtil.fileReaderByLine(inputFile, configMap.get("encoding"));
						areInputsAvailableInFile = !GeneralUtil.isCollectionEmpty(inputList);
					}
					inputList = (!areInputsAvailableInFile)
							? Arrays.asList(
									GeneralUtil.getInputFromConsole(0, consoleMsg).split(configMap.get("delimitter")))
							: inputList;
					if (!GeneralUtil.isCollectionEmpty(inputList)) {
						processRequest(inputList);
					} else {
						GeneralUtil.exitSystem("Inputs are empty... So exiting...");
					}
				} catch (Exception e) {
					// TODO: handle exception
					GeneralUtil.exitSystem(e.getMessage());
				}
			} else {
				GeneralUtil.exitSystem("Could not find the configuration... So exiting...");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			GeneralUtil.exitSystem(e.getMessage());
		}
	}

	private static void processRequest(List<String> inputList) throws Exception {

		Map<Lob, ArrayList<String>> inputMap = new HashMap<Lob, ArrayList<String>>();
		ArrayList<String> userStoryNosPR = new ArrayList<String>();
		ArrayList<String> userStoryNosGL = new ArrayList<String>();
		ArrayList<String> userStoryNosAUTO = new ArrayList<String>();
		for (String input : inputList) {
			String[] inputArray = input.split(configMap.get("delimitter.hiphen"));
			String userStoryNo = inputArray[0];
			String lob = inputArray[1];
			if (Lob.PR.toString().equals(lob)) {
				userStoryNosPR.add(userStoryNo);
			} else if (Lob.GL.toString().equals(lob)) {
				userStoryNosGL.add(userStoryNo);
			} else if (Lob.AUTO.toString().equals(lob)) {
				userStoryNosAUTO.add(userStoryNo);
			} else {
				// GeneralUtil.exitSystem(input + " is an invalid input.");
				System.out.println(input + " is an invalid input.");
			}
		}
		if (!GeneralUtil.isCollectionEmpty(userStoryNosPR)) {
			inputMap.put(Lob.PR, userStoryNosPR);
		}
		if (!GeneralUtil.isCollectionEmpty(userStoryNosGL)) {
			inputMap.put(Lob.GL, userStoryNosGL);
		}
		if (!GeneralUtil.isCollectionEmpty(userStoryNosAUTO)) {
			inputMap.put(Lob.AUTO, userStoryNosAUTO);
		}
		if (!GeneralUtil.isMapEmpty(inputMap)) {
			System.out.println("user input size =" + inputMap.size());
			multiThreadedProcess(inputMap);
		} else {
			GeneralUtil.exitSystem("There are no valid inputs to the system. So exiting...");
		}
	}

	private static void multiThreadedProcess(Map<Lob, ArrayList<String>> inputMap)
			throws EncryptedDocumentException, IOException {
		ExecutorService executor = Executors.newFixedThreadPool(GeneralConstants.TOTAL_THREADS);
		String prMapFile = configMap.get("pr.filename").trim();
		String glMapFile = configMap.get("gl.filename").trim();
		String autoMapFile = configMap.get("auto.filename").trim();
		String prURL = configMap.get("pr.mapping.url").trim();
		String glURL = configMap.get("gl.mapping.url").trim();
		String autoURL = configMap.get("auto.mapping.url").trim();
		String mappingDirName = GeneralConstants.MAPPING_FILES_DIR;
		File mappingDir = new File(mappingDirName);
		boolean ismappingDirExist = mappingDir.exists();
		if ("y".equalsIgnoreCase(configMap.get("is.refresh.enabled")) || !ismappingDirExist) {
			if (ismappingDirExist) {
				FileUtils.cleanDirectory(mappingDir);
			}
			ismappingDirExist = mappingDir.exists() ? true : mappingDir.mkdirs();
			System.out.println(mappingDirName + " was created " + ismappingDirExist);
		}

		for (Map.Entry<Lob, ArrayList<String>> entry : inputMap.entrySet()) {
			Runnable processor = null;
			Lob lob = entry.getKey();
			ArrayList<String> inputList = entry.getValue();
			System.out.println("input list size = " + inputList.size()+" in "+lob.toString());
			if (!GeneralUtil.isCollectionEmpty(inputList)) {
				switch (lob) {
				case PR:
					processor = new ScriptProcessor(prMapFile, prURL, 0, 0, 0, inputList);
					break;
				case GL:
					processor = new ScriptProcessor(glMapFile, glURL, 0, 0, 0, inputList);
					break;
				case AUTO:
					processor = new ScriptProcessor(autoMapFile, autoURL, 0, 0, 0, inputList);
					break;

				default:
					break;
				}
				executor.execute(processor);
			}
		}
		executor.shutdown();
		// Wait until all threads are finish
		while (!executor.isTerminated()) {
		}
		System.out.println("The system is now exiting. Bye!!");
	}
}
