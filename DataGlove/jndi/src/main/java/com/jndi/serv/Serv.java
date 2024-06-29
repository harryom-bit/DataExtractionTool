package com.jndi.serv;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.jndi.csvgenerate.csvgenerator;
import com.jndi.ctrl.mainCtrl;
import com.jndi.model.ApplicationLogs;
import com.jndi.model.Login;
import com.jndi.model.ProcessRegistry;
import com.jndi.model.TableFilters;
import com.jndi.repository.Repository1;

@Service
public class Serv {

	@Autowired
	private Environment env;

	@Autowired
	private csvgenerator cg;

	@Autowired
	public Repository1 repo;
	@Autowired
	public JdbcService js;

	@Value("${textFilePath}")
	private String textFilePath;

	@Value("${downloadPath}")
	private String downloadPath;

	@Value("${serviceNow.caseUrl.prefix}")
	private String caseUrlPrefix;

	@Value("${serviceNow.caseUrl.suffix}")
	private String caseUrlSuffix;

	@Value("${serviceNow.attachmentUrl.prefix}")
	private String attachmentUrlPrefix;

	@Value("${serviceNow.attachmentUrl.suffix}")
	private String attachmentUrlSuffix;

	private List<String> selectedTables;

	public static final String delimiter = ",";
	public String companyName;

	// variables for exception propagation
	public static boolean exceptionFlag = false;
	public static String[] noDataTables;
	public static boolean noDataFlag = false;
	public static Exception exception;

	public void setCompanyName() {

	}

	public int attachmentsToCase(String taskSysId) {
		int responseCode = 0;
		try {
			String sysId = fetchCaseId(taskSysId);
			System.out.println("sysId is " + sysId);
			String fileName = fetchTableName();
			String dnPath = env.getProperty("USERPROFILE") + downloadPath;
			System.out.println(
					"------------------------------downloads path from attachmentsToCase method is----------------------------------" + dnPath);
			String filePath = dnPath + fileName;
			//File file = new File(filePath);
			File file =new File(downloadPath+fileName);
			System.out.println("file name is " + filePath.toString());
			String[] downloadedFileName = fileName.split(" ");
			System.out.println("download file name in array in " + Arrays.toString(downloadedFileName));
			System.out.println("latest file from the downloads folder is " + fileName);
			if (downloadedFileName.length > 1) {
				fileName = downloadedFileName[0] + downloadedFileName[1];
				System.out.println("downloaded file nam eis tableNm" + fileName);
			}
			System.out.println("downloaded table name is " + fileName);

			FileInputStream inputStream = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			inputStream.read(bytes);
			inputStream.close();
			String url = attachmentUrlPrefix + sysId + attachmentUrlSuffix + fileName;
			System.out.println("attachment url is =======================================================" + url);

			java.net.URL obj = new java.net.URL(url);
			System.out.println("url is " + obj + "");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Content-Type", "application/zip");
			con.setRequestProperty("Authorization", "Basic " + Base64.getEncoder()
					.encodeToString(("SnowIntegration.Agent@landisgyr.com" + ":" + "AB-65snprocc4").getBytes()));
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(bytes);
			System.out.println("after writing the bytes size is " + wr.size());
			wr.flush();
			wr.close();

			responseCode = con.getResponseCode();
			System.out.println("response code from serv class is " + responseCode);

		} catch (Exception e) {
			System.out.println(e);
		}
		return responseCode;
	}

	public String fetchCaseId(String caseNumber) throws Exception {
		String result = null;
		String caseUrl = caseUrlPrefix + caseNumber + caseUrlSuffix;
		System.out.println("Case Url is ================= " + caseUrl);
		java.net.URL url = new java.net.URL(caseUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Authorization", "Basic " + Base64.getEncoder()
				.encodeToString(("SnowIntegration.Agent@landisgyr.com" + ":" + "AB-65snprocc4").getBytes()));

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		String output;
		while ((output = br.readLine()) != null) {
			String[] outputArray = output.split(":");
			String str = outputArray[2].split("}")[0].substring(1, outputArray[2].split("}")[0].length() - 1);
			System.out.println("str is " + str);
			result = str;
		}
		conn.disconnect();
		return result;
	}

	public String fetchTableName() {
		File[] downloadFiles = null;
		try {
			Thread.sleep(10000);
			System.out.println("download path set is "+downloadPath);
			//File uploadDirectory = new File("C:\\Users\\mauryayo\\Download\\");
			File uploadDirectory = new File(downloadPath);
			downloadFiles = uploadDirectory.listFiles();
			for (File downloadfiles : downloadFiles) {
				System.out.println("file name is " + downloadfiles);
			}
			Arrays.sort(downloadFiles, new Comparator<File>() {
				@Override
				public int compare(File fileOne, File fileTwo) {
					return Long.valueOf(fileTwo.lastModified()).compareTo(fileOne.lastModified());
				}
			});

		} catch (Exception e) {
			System.out.println(e);
		}
		String downloadedFile = downloadFiles[0].getName();

		return downloadedFile;
	}

	public String runRawQuery(String query, HttpServletResponse hsr) throws IOException {
		List<String[]> response = js.runRawQuery(query);
		if (response.size() > 0) {
			cg.downloadRawQueryData(hsr, response);
		}
		return null;
	}

//	private static List<String> removeQuotes(List<String> record) {
//        // Remove double quotes from each field
//        List<String> cleanedRecord = List.copyOf(record);
//        for (int i = 0; i < cleanedRecord.size(); i++) {
//            cleanedRecord.set(i, cleanedRecord.get(i).replace("\"", ""));
//        }
//        return cleanedRecord;
//    }
	public void queryProcessRegistryData(ProcessRegistry tf, HttpServletResponse hsr) {
		List<ArrayList<String[]>> allData = new ArrayList<ArrayList<String[]>>();

		allData.add((ArrayList<String[]>) js.processRegistryServiceImpl(tf));
		List<String> table = new ArrayList<String>();
		table.add("ProcessRegistry");

		// here we will be passing ado id so that we can attach the data in the ado
		// ticket

		switch (mainCtrl.downloadFormat) {
		case "csv":
			System.out.println("downloading the data in " + mainCtrl.downloadFormat);
			cg.addMultipleTables(hsr, allData, table);
			break;
		case "txt":
			System.out.println("downloading the data in " + mainCtrl.downloadFormat);
			cg.storeTablesToTxt(hsr, allData, table);
			break;
		case "xlsx":
			System.out.println("downloading the data in " + mainCtrl.downloadFormat);
			cg.storeTablesToXLSX(hsr, allData, table);
		}
	}

	public void queryApplicationLogData(ApplicationLogs tf, HttpServletResponse hsr) {
		List<ArrayList<String[]>> allData = new ArrayList<ArrayList<String[]>>();

		allData.add((ArrayList<String[]>) js.applicationLogsServiceImpl(tf));
		List<String> table = new ArrayList<String>();
		table.add("ApplicationLog");

		// here we will be passing ado id so that we can attach the data in the ado
		// ticket
		switch (mainCtrl.downloadFormat) {
		case "csv":
			System.out.println("downloading the data in " + mainCtrl.downloadFormat);
			cg.addMultipleTables(hsr, allData, table);
			break;
		case "txt":
			System.out.println("downloading the data in " + mainCtrl.downloadFormat);
			cg.storeTablesToTxt(hsr, allData, table);
			break;
		case "xlsx":
			System.out.println("downloading the data in " + mainCtrl.downloadFormat);
			cg.storeTablesToXLSX(hsr, allData, table);
		}
	}

	public void queryExceptionManagementData(Map<String, Integer> customBufferDays, Map<String, java.sql.Date> fromDate,
			Map<String, java.sql.Date> toDate, TableFilters tf, HttpServletResponse hsr) {
		List<ArrayList<String[]>> allData = new ArrayList<ArrayList<String[]>>();
		for (String tblName : selectedTables) {
			allData.add((ArrayList<String[]>) js.searchTestDuplicateForSpecificDate(customBufferDays, fromDate, toDate,
					tblName.toUpperCase(), tf, fromDate.get(tblName), toDate.get(tblName)));
		}

		// here we will be passing ado id so that we can attach the data in the ado
		// ticket
		if (Serv.exceptionFlag == false) {
			switch (mainCtrl.downloadFormat) {
			case "csv":
				System.out.println("downloading the data in " + mainCtrl.downloadFormat);
				cg.addMultipleTables(hsr, allData, getSelectedTables());
				break;
			case "txt":
				System.out.println("downloading the data in " + mainCtrl.downloadFormat);
				cg.storeTablesToTxt(hsr, allData, getSelectedTables());
				break;
			case "xlsx":
				System.out.println("downloading the data in " + mainCtrl.downloadFormat);
				cg.storeTablesToXLSX(hsr, allData, getSelectedTables());
			}
		}else {
			cg.storeExceptionToTxt(hsr,Serv.exception, selectedTables);
		}
	}

	public void queryOdeData(Map<String, Integer> customBufferDays, Map<String, java.sql.Date> fromDate,
			Map<String, java.sql.Date> toDate, TableFilters tf, HttpServletResponse hsr) {
		List<ArrayList<String[]>> allData = new ArrayList<ArrayList<String[]>>();
		for (String tblName : selectedTables) {
			allData.add((ArrayList<String[]>) js.odeModuleService(customBufferDays, fromDate, toDate,
					tblName.toUpperCase(), tf, fromDate.get(tblName), toDate.get(tblName)));
		}
		// here we will be passing ado id so that we can attach the data in the ado
		// ticket
		cg.addMultipleTables(hsr, allData, getSelectedTables());

	}

	public void queryVeeData(Map<String, Integer> customBufferDays, Map<String, java.sql.Date> fromDate,
			Map<String, java.sql.Date> toDate, TableFilters tf, HttpServletResponse hsr) {
		List<ArrayList<String[]>> allData = new ArrayList<ArrayList<String[]>>();
		for (String tblName : selectedTables) {
			allData.add((ArrayList<String[]>) js.veeModuleService(customBufferDays, fromDate, toDate, tblName, tf,
					fromDate.get(tblName), toDate.get(tblName)));
		}
		System.out.println("selected tables are " + getSelectedTables());
		// here we will be passing ado id so that we can attach the data in the ado
		// ticket
		cg.addMultipleTables(hsr, allData, getSelectedTables());

	}

	public void queryIecExtractBillingWindowData(Map<String, Integer> customBufferDays,
			Map<String, java.sql.Date> fromDate, Map<String, java.sql.Date> toDate, TableFilters tf,
			HttpServletResponse hsr) {
		List<ArrayList<String[]>> allData = new ArrayList<ArrayList<String[]>>();
		for (String tblName : selectedTables) {
			allData.add((ArrayList<String[]>) js.iecExtractBillingWindowModuleService(customBufferDays, fromDate,
					toDate, tblName, tf, fromDate.get(tblName), toDate.get(tblName)));
		}
		System.out.println("selected tables are " + getSelectedTables());
		// here we will be passing ado id so that we can attach the data in the ado
		// ticket
		cg.addMultipleTables(hsr, allData, getSelectedTables());

	}

	public void queryDseRealtimeData(Map<String, Integer> customBufferDays, Map<String, java.sql.Date> fromDate,
			Map<String, java.sql.Date> toDate, TableFilters tf, HttpServletResponse hsr) {
		List<ArrayList<String[]>> allData = new ArrayList<ArrayList<String[]>>();
		for (String tblName : selectedTables) {
			allData.add((ArrayList<String[]>) js.dseRealtimeModuleService(customBufferDays, fromDate, toDate, tblName,
					tf, fromDate.get(tblName), toDate.get(tblName)));
		}
		System.out.println("selected tables are " + getSelectedTables());
		// here we will be passing ado id so that we can attach the data in the ado
		// ticket
		cg.addMultipleTables(hsr, allData, getSelectedTables());

	}

	public void dataHubServiceImpl(Map<String, Integer> customBufferDays, Map<String, java.sql.Date> fromDate,
			Map<String, java.sql.Date> toDate, TableFilters tf, HttpServletResponse hsr) {
		List<ArrayList<String[]>> allData = new ArrayList<ArrayList<String[]>>();
		for (String tblName : selectedTables) {
			allData.add((ArrayList<String[]>) js.dataHubQuery(customBufferDays, fromDate, toDate, tblName, tf,
					fromDate.get(tblName), toDate.get(tblName)));
		}
		System.out.println("selected tables are " + getSelectedTables());
		// here we will be passing ado id so that we can attach the data in the ado
		// ticket
		cg.addMultipleTables(hsr, allData, getSelectedTables());
	}

	public void emedReadProBatchServiceImpl(Map<String, Integer> customBufferDays, Map<String, java.sql.Date> fromDate,
			Map<String, java.sql.Date> toDate, TableFilters tf, HttpServletResponse hsr) {
		List<ArrayList<String[]>> allData = new ArrayList<ArrayList<String[]>>();
		for (String tblName : selectedTables) {
			allData.add((ArrayList<String[]>) js.emedReadProBatchQuery(customBufferDays, fromDate, toDate, tblName, tf,
					fromDate.get(tblName), toDate.get(tblName)));
		}
		System.out.println("selected tables are " + getSelectedTables());
		// here we will be passing ado id so that we can attach the data in the ado
		// ticket
		cg.addMultipleTables(hsr, allData, getSelectedTables());

	}

	public void dgmMaterializerServiceImpl(Map<String, Integer> customBufferDays, Map<String, java.sql.Date> fromDate,
			Map<String, java.sql.Date> toDate, TableFilters tf, HttpServletResponse hsr) {
		List<ArrayList<String[]>> allData = new ArrayList<ArrayList<String[]>>();
		for (String tblName : selectedTables) {
			allData.add((ArrayList<String[]>) js.dgmMaterializerQuery(customBufferDays, fromDate, toDate, tblName, tf,
					fromDate.get(tblName), toDate.get(tblName)));
		}
		System.out.println("selected tables are " + getSelectedTables());
		// here we will be passing ado id so that we can attach the data in the ado
		// ticket
		cg.addMultipleTables(hsr, allData, getSelectedTables());

	}

	public List<String> getSelectedTables() {
		return this.selectedTables;
	}

	public void updateTables(String[] tables) {
		selectedTables = new ArrayList<String>();
		for (String table : tables) {
			this.selectedTables.add(table.toUpperCase());
		}
		System.out.println("updated tables are " + this.selectedTables);
	}

	public List<String> getModuleRelatedTables(String owner, String moduleAcronym) {
		System.out.println("inside getModulesRelatedTables method from dgm -> owner is " + owner
				+ " and moduleAcronym is " + moduleAcronym);
		String tableName = "";
		switch (moduleAcronym) {
		case "Exception Management":
			tableName = "";
			try {
				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream("tables/exceptionManagement.txt");
				Scanner scanner = new Scanner(inputStream);

				while (scanner.hasNext()) {
					tableName += scanner.nextLine();
				}
				System.out.println("------------------------after addition tables names are " + tableName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Arrays.asList(tableName.split(","));
		case "Calculation Engine":
			tableName = "";
			try {
				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream("tables/calculationEngine.txt");
				Scanner scanner = new Scanner(inputStream);

				while (scanner.hasNext()) {
					tableName += scanner.nextLine();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return Arrays.asList(tableName.split(","));
		case "Emed Reads Processing Batch":
			tableName = "";
			try {
				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream("tables/emedReadProcessingBatch.txt");
				Scanner scanner = new Scanner(inputStream);

				while (scanner.hasNext()) {
					tableName += scanner.nextLine();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return Arrays.asList(tableName.split(","));
		case "EXTRACTS":
			tableName = "";
			try {
				InputStream inputStream = getClass().getClassLoader().getResourceAsStream("tables/extracts.txt");
				Scanner scanner = new Scanner(inputStream);

				while (scanner.hasNext()) {
					tableName += scanner.nextLine();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Arrays.asList(tableName.split(","));
		case "Data Hub":
			System.out.println("inside the data hub case and text file path is " + this.textFilePath);
			tableName = "";
			try {
				InputStream inputStream = getClass().getClassLoader().getResourceAsStream("tables/dataHub.txt");
				Scanner scanner = new Scanner(inputStream);

				while (scanner.hasNext()) {
					tableName += scanner.nextLine();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Arrays.asList(tableName.split(","));
		case "VEE":
			tableName = "";
			try {
				InputStream inputStream = getClass().getClassLoader().getResourceAsStream("tables/vee.txt");
				Scanner scanner = new Scanner(inputStream);

				while (scanner.hasNext()) {
					tableName += scanner.nextLine();
				}
				System.out.println("------------------------after addition tables names are " + tableName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Arrays.asList(tableName.split(","));
		case "DGM Materializer":
			tableName = "";
			try {
				InputStream inputStream = getClass().getClassLoader().getResourceAsStream("tables/dgmMaterializer.txt");
				Scanner scanner = new Scanner(inputStream);

				while (scanner.hasNext()) {
					tableName += scanner.nextLine();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Arrays.asList(tableName.split(","));
		case "IEC Extract & Billing Window":
			tableName = "";
			try {
				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream("tables/iecExtractAndBillingWindow.txt");
				Scanner scanner = new Scanner(inputStream);

				while (scanner.hasNext()) {
					tableName += scanner.nextLine();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Arrays.asList(tableName.split(","));
		case "DSE Real Time":
			System.out.println("inside dse real time switch case");
			tableName = "";
			try {
				InputStream inputStream = getClass().getClassLoader().getResourceAsStream("tables/dseRealTime.txt");
				Scanner scanner = new Scanner(inputStream);

				while (scanner.hasNext()) {
					tableName += scanner.nextLine();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Arrays.asList(tableName.split(","));
		case "On Demand Engine":
			tableName = "";
			try {
				InputStream inputStream = getClass().getClassLoader().getResourceAsStream("tables/onDemandEngine.txt");
				Scanner scanner = new Scanner(inputStream);

				while (scanner.hasNext()) {
					tableName += scanner.nextLine();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Arrays.asList(tableName.split(","));
		default:
			return new ArrayList<String>();

		}
		// return repo.getModuleRelatedTables(owner,moduleAcronym);
		// return new ArrayList<String>();
	}

	public List<String> getTables() {
		System.out.println(repo.tables());
		return repo.tables();
	}

	public List<String[]> getMeteringDevice() {
		System.out.println(repo.meteringDeviceAll());
		return repo.meteringDeviceAll();
	}

	public List<List<Object>> getMdvcCumReads() {
		return repo.mdvcCumReads();
	}

	public List<String[]> singleRow() {
		return repo.singleRow();
	}

	public List<String[]> filteredTable(String table) {
		return repo.singleRow();
	}

	public List<List<Object>> singleRow2() {
		return repo.singleRow2();
	}

	public List<List<String>> read() {
		String csvFile = "D://assets/test_data.csv";
		List<List<String>> ans = new ArrayList<>();
		try {
			File file = new File(csvFile);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = " ";
			String[] tempArr;

			while ((line = br.readLine()) != null) {
				tempArr = line.split(delimiter);
				List<String> list = new ArrayList<String>();
				for (String tempStr : tempArr) {
					System.out.print(tempStr + " ");
					list.add(tempStr);
				}
				ans.add(list);
				System.out.println();
			}
			br.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return ans;
	}
}
