package com.jndi.serv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.jndi.csvgenerate.csvgenerator;
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

	private List<String> selectedTables;

	public static final String delimiter = ",";

	public void queryforData(Map<String,Integer> customBufferDays ,Map<String, java.sql.Date> fromDate,Map<String, java.sql.Date> toDate,TableFilters tf, HttpServletResponse hsr) {
		List<ArrayList<String[]>> allData = new ArrayList<ArrayList<String[]>>();
		for (String tblName : selectedTables) {
				allData.add((ArrayList<String[]>) js.searchTestDuplicateForSpecificDate(customBufferDays,fromDate,toDate,tblName.toUpperCase(), tf,fromDate.get(tblName),toDate.get(tblName)));
		}
		//here we will be passing ado id so that we can attach the data in the ado ticket
		cg.addMultipleTables(hsr, allData, getSelectedTables());

	}
	public void queryVeeData(Map<String,Integer> customBufferDays ,Map<String, java.sql.Date> fromDate,Map<String, java.sql.Date> toDate,TableFilters tf, HttpServletResponse hsr) {
		List<ArrayList<String[]>> allData = new ArrayList<ArrayList<String[]>>();
		for (String tblName : selectedTables) {
				allData.add((ArrayList<String[]>) js.veeModuleService(customBufferDays,fromDate,toDate,tblName, tf,fromDate.get(tblName),toDate.get(tblName)));
		}
		System.out.println("selected tables are " + getSelectedTables());
		//here we will be passing ado id so that we can attach the data in the ado ticket
		cg.addMultipleTables(hsr, allData, getSelectedTables());

	}

	public List<String> getSelectedTables() {
		return this.selectedTables;
	}

	public void updateTables(String[] tables) {
		selectedTables = new ArrayList<String>();
		for (String table : tables) {
			this.selectedTables.add(table.toUpperCase() );
		}
		System.out.println("updated tables are "+this.selectedTables);
	}

	public List<String> getModuleRelatedTables(String owner, String moduleAcronym) {
		String tableName = "";
		switch (moduleAcronym) {
		case "EXCEPTION":
			tableName = "";
			try {
				File f = new File(this.textFilePath+"exceptionManagement.txt");
				Scanner reader = new Scanner(f);
				while (reader.hasNextLine()) {
					tableName += reader.nextLine();
				}
				System.out.println("------------------------after addition tables names are "+tableName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Arrays.asList(tableName.split(","));
		case "CE":
			tableName = "";
			try {
				File f = new File(this.textFilePath+"calculationEngine.txt");
				Scanner reader = new Scanner(f);
				while (reader.hasNextLine()) {
					tableName += reader.nextLine();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Arrays.asList(tableName.split(","));
		case "EXTRACTS":
			tableName = "";
			try {
				File f = new File(this.textFilePath+"extracts.txt");
				Scanner reader = new Scanner(f);
				while (reader.hasNext()) {
					tableName += reader.nextLine();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Arrays.asList(tableName.split(","));
		case "DATA HUB":
			tableName="";
			try {
				File f = new File(this.textFilePath+"dataHub.txt");
				Scanner reader = new Scanner(f);
				while (reader.hasNext()) {
					tableName += reader.nextLine();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Arrays.asList(tableName.split(","));
		case "VEE":
			tableName="";
			try {
				File f = new File(this.textFilePath+"vee.txt");
				Scanner reader = new Scanner(f);
				while (reader.hasNext()) {
					tableName += reader.nextLine();
				}
				System.out.println("------------------------after addition tables names are "+tableName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Arrays.asList(tableName.split(","));
		case "DGM MATERIALIZER":
			tableName="";
			try {
				File f = new File(this.textFilePath+"dgmMaterializer.txt");
				Scanner reader = new Scanner(f);
				while (reader.hasNext()) {
					tableName += reader.nextLine();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Arrays.asList(tableName.split(","));
		case "IEC EXTRACT & BILLING WINDOW":
			tableName="";
			try {
				File f = new File(this.textFilePath+"iecExtractAndBillingWindow.txt");
				Scanner reader = new Scanner(f);
				while (reader.hasNext()) {
					tableName += reader.nextLine();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Arrays.asList(tableName.split(","));
		case "ON DEMAND ENGINE":
			tableName="";
			try {
				File f = new File(this.textFilePath+"onDemandEngine.txt");
				Scanner reader = new Scanner(f);
				while (reader.hasNext()) {
					tableName += reader.nextLine();
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

	/*
	 * public List<List<Object>> singleRow(){ return repo.singleRow(); }
	 */

}
