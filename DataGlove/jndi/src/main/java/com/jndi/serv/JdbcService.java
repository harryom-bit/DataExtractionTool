package com.jndi.serv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.jndi.csvgenerate.csvgenerator;
import com.jndi.model.ApplicationLogs;
import com.jndi.model.Login;
import com.jndi.model.ProcessRegistry;
import com.jndi.model.Qresult;
import com.jndi.model.TableFilters;

@Service
public class JdbcService {
	Logger logger = LoggerFactory.getLogger(JdbcService.class);
	@Autowired
	EntityManager em;

	@Value("${spring.datasource.url}")
	private String dataSourceUrl;
	@Value("${spring.datasource.username}")
	private String dataSourceUserName;
	@Value("${spring.datasource.password}")
	private String dataSourceUserPassword;

	// duplicate latest test method to fetch the data from the database
	public List<String[]> searchTestDuplicate(String tables, TableFilters tf) {
		List<String[]> list = new ArrayList<String[]>();
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(this.dataSourceUrl, this.dataSourceUserName,
					this.dataSourceUserPassword);
			// CallableStatement stmt = con.prepareCall(
			// "{call PKG_RETURN_TABLES.FUNC_RET_ROWS(?,?,?,?,?)}");
			java.sql.CallableStatement stmt = con
					.prepareCall("select * from (PKG_RETURN_TABLES.FUNC_RET_ROWS(:1 ,:2 ,:3 ,:4 ,:5 ))");
			// String q="select * from
			// table(PKG_RETURN_TABLES.FUNC_RET_ROWS("+tf.getMeterId()+","+tf.getSdpId()+","+tf.getFrom()+","+tf.getTo()+","+tables+")";
			// PreparedStatement p=con.prepareStatement(q);
			System.out.println("trimmed string value to be passed is : " + tables.substring(1, tables.length() - 1));
			String str2 = tables;

			stmt.setString(1, tf.getMeterId());
			stmt.setString(2, tf.getSdpId());
			stmt.setDate(3, (java.sql.Date) tf.getFrom());
			stmt.setDate(4, (java.sql.Date) tf.getTo());
			System.out.println("passing the tables with it's values " + str2);
			stmt.setString(5, str2);
			System.out.println("query is " + stmt);
			ResultSet res = stmt.executeQuery();
			list = new ArrayList<String[]>();
			boolean firstRow = true;
			while (res.next()) {
				if (firstRow) {
					String[] arr = res.getString(2).split(",");
					list.add(arr);
					firstRow = false;
				} else {
					// System.out.println(res.getString(2));
					String[] arr = res.getString(2).substring(1, res.getString(2).length() - 1).split(",");
					list.add(arr);
				}

			}
			for (String[] s : list) {
				System.out.println("stored list is " + Arrays.toString(s));
			}
			/*
			 * int mid=0; while(res.next()) { mid=res.getInt(1); System.out.println(mid); }
			 * 
			 * String query = ""; for (String table : tables) { query = "select * from " +
			 * table + " where mdvc_id=? "; PreparedStatement ps =
			 * con.prepareStatement(query); ps.setInt(1, mid); ResultSet rs =
			 * ps.executeQuery(); java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			 * List<String[]> list = new ArrayList<String[]>(); String columnName[] = new
			 * String[rsmd.getColumnCount()]; for (int i = 1; i <= rsmd.getColumnCount();
			 * i++) { columnName[i - 1] = rsmd.getColumnName(i); } list.add(columnName);
			 * System.out.println(Arrays.toString(columnName)); while (rs.next()) { String
			 * data[] = new String[rsmd.getColumnCount()]; for (int i = 1; i <=
			 * rsmd.getColumnCount(); i++) { data[i - 1] = rs.getString(i); }
			 * 
			 * list.add(data); System.out.println(Arrays.toString(data)); }
			 */
			// return (List<String[]>) res;
			// return new ArrayList<String[]>();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// for process registry date extraction
	public List<String[]> processRegistryServiceImpl(ProcessRegistry tf) {
		System.out.println("------------inside process registry service impl method----------------");
		List<String[]> list = new ArrayList<String[]>();
		java.sql.Timestamp sqlFromTimestamp = null;
		java.sql.Timestamp sqlToTimestamp = null;
		try {
//			System.out.println("------------inside process registry service impl method try block----------------");
//			Class.forName("oracle.jdbc.OracleDriver");
//			Connection con = DriverManager.getConnection(this.dataSourceUrl, this.dataSourceUserName,
//					this.dataSourceUserPassword);
//			java.sql.CallableStatement stmt = con
//					.prepareCall("select * from (PKG_RETURN_TABLES_LOGS.FUNC_RET_ROWS(:1 ,:2 ,:3 ,:4 ,:5,:6,:7 ))");
//			
//            stmt.setString(1, null); //module name
//            stmt.setString(2,null); //submodule name
//            stmt.setTimestamp(3,null);
//			stmt.setTimestamp(4,null);
//			stmt.setObject(5, null);
//			stmt.setString(6,"PROCESS_REGISTRY"); 
//			stmt.setInt(7, 4776);
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(this.dataSourceUrl, this.dataSourceUserName,
					this.dataSourceUserPassword);
			java.sql.CallableStatement stmt = con
					.prepareCall("select * from (PKG_RETURN_TABLES_LOGS.FUNC_RET_ROWS(:1 ,:2 ,:3 ,:4 ,:5,:6,:7 ))");
			if (tf.getFromDate() != null && tf.getToDate() != null) {
				String convertedFromDate[] = tf.getFromDate().split("T");
				String convertedToDate[] = tf.getToDate().split("T");
				String fromTimeStamp = "";
				String toTimeStamp = "";
				for (int i = 0; i < convertedFromDate.length; i++) {
					fromTimeStamp += convertedFromDate[i];
					if (i == 0)
						fromTimeStamp += " ";
				}
				for (int i = 0; i < convertedToDate.length; i++) {
					toTimeStamp += convertedToDate[i];
					if (i == 0)
						toTimeStamp += " ";
				}
				fromTimeStamp += ":00";
				toTimeStamp += ":00";
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				java.util.Date utilFromDate = dateFormat.parse(fromTimeStamp);
				sqlFromTimestamp = new java.sql.Timestamp(utilFromDate.getTime());
				java.util.Date utilToDate = dateFormat.parse(toTimeStamp);
				sqlToTimestamp = new java.sql.Timestamp(utilToDate.getTime());
			}

			System.out.println("from and to time stamps are " + sqlFromTimestamp + " to " + sqlToTimestamp);
			System.out.println("process module name is " + tf.getProcessModule() + "sub module name is "
					+ tf.getProcessSubModule());

			// stmt.setString(1, "PKG_MANAGE_OPERATOR");
			stmt.setString(1, tf.getProcessModule());
			stmt.setString(2, tf.getProcessSubModule());
			stmt.setTimestamp(3, sqlFromTimestamp);
			stmt.setTimestamp(4, sqlToTimestamp);
			// validate the log level before sending the data

			stmt.setObject(5, null);

			stmt.setString(6, "PROCESS_REGISTRY");
			stmt.setInt(7, tf.getProcessId());
			ResultSet res = stmt.executeQuery();
			list = new ArrayList<String[]>();
			boolean firstRow = true;
			while (res.next()) {
				if (firstRow) {
					String[] arr = res.getString(2).split(",");
					System.out.println("metadata is " + Arrays.toString(arr));
					list.add(arr);
					firstRow = false;
				} else {
					String[] arr = res.getString(2).substring(1, res.getString(2).length() - 1).split(",");
					list.add(arr);
				}
			}// Serv.exceptionFlag=true;
			// Serv.exception= new FileNotFoundException("you have got an exception - your
			// meter id is not present: ");
		} catch (Exception e) {
			Serv.exceptionFlag = true;
			Serv.exception = e;
			e.printStackTrace();
		}
		return list;
	}

	// for application logs data extraction
	public List<String[]> applicationLogsServiceImpl(ApplicationLogs tf) {
		List<String[]> list = new ArrayList<String[]>();
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(this.dataSourceUrl, this.dataSourceUserName,
					this.dataSourceUserPassword);
			java.sql.CallableStatement stmt = con
					.prepareCall("select * from (PKG_RETURN_TABLES_LOGS.FUNC_RET_ROWS(:1 ,:2 ,:3 ,:4 ,:5,:6,:7 ))");
			String convertedFromDate[] = tf.getFromDate().split("T");
			String convertedToDate[] = tf.getToDate().split("T");
			String fromTimeStamp = "";
			String toTimeStamp = "";
			for (int i = 0; i < convertedFromDate.length; i++) {
				fromTimeStamp += convertedFromDate[i];
				if (i == 0)
					fromTimeStamp += " ";
			}
			for (int i = 0; i < convertedToDate.length; i++) {
				toTimeStamp += convertedToDate[i];
				if (i == 0)
					toTimeStamp += " ";
			}
			fromTimeStamp += ":00";
			toTimeStamp += ":00";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Date utilFromDate = dateFormat.parse(fromTimeStamp);
			java.sql.Timestamp sqlFromTimestamp = new java.sql.Timestamp(utilFromDate.getTime());
			java.util.Date utilToDate = dateFormat.parse(toTimeStamp);
			java.sql.Timestamp sqlToTimestamp = new java.sql.Timestamp(utilToDate.getTime());

			System.out.println("from and to time stamps are " + sqlFromTimestamp + " to " + sqlToTimestamp);
			System.out.println("process module name is " + tf.getProcessModule() + "sub module name is "
					+ tf.getProcessSubModule());
			System.out.println("log level is " + tf.getLogLevel());

			// stmt.setString(1, "PKG_MANAGE_OPERATOR");
			stmt.setString(1, tf.getProcessModule());
			stmt.setString(2, tf.getProcessSubModule());
			stmt.setTimestamp(3, sqlFromTimestamp);
			stmt.setTimestamp(4, sqlToTimestamp);
			// validate the log level before sending the data
			if (tf.getLogLevel() == 0) {
				stmt.setObject(5, null);
			} else {
				stmt.setInt(5, tf.getLogLevel());
			}

			stmt.setString(6, "APPLICATION_LOG");
			stmt.setInt(7, 0);
			ResultSet res = stmt.executeQuery();
			list = new ArrayList<String[]>();
			boolean firstRow = true;
			while (res.next()) {
				if (firstRow) {
					String[] arr = res.getString(2).split(",");
					System.out.println("metadata is " + Arrays.toString(arr));
					list.add(arr);
					firstRow = false;
				} else {
					System.out.println("inside else part ---------");
					String[] arr = res.getString(2).substring(1, res.getString(2).length() - 1).split(",");
					System.out.println("date is " + Arrays.toString(arr));

					list.add(arr);
				}
			}// Serv.exceptionFlag=true;
			// Serv.exception= new FileNotFoundException("you have got an exception - your
			// meter id is not present: ");
		} catch (Exception e) {
			Serv.exceptionFlag = true;
			Serv.exception = e;
			e.printStackTrace();
		}
		return list;
	}

	// for to date
	public List<String[]> searchTestDuplicateForSpecificDate(Map<String, Integer> bufferDays,
			Map<String, java.sql.Date> fromDateMap, Map<String, java.sql.Date> toDateMap, String tableName,
			TableFilters tf, Date fromDate, Date toDate) {

		List<String[]> list = new ArrayList<String[]>();
		Serv.exceptionFlag = false;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(this.dataSourceUrl, this.dataSourceUserName,
					this.dataSourceUserPassword);
			java.sql.CallableStatement stmt = con
					.prepareCall("select * from (PKG_RETURN_TABLES.FUNC_RET_ROWS(:1 ,:2 ,:3 ,:4 ,:5,:6,:7 ))");
			stmt.setString(1, "EXCEPTION_MANAGEMENT");
			stmt.setString(2, tf.getMeterId());
			stmt.setString(3, tf.getSdpId());
			System.out.println("setting from date ");
			if (fromDateMap.containsKey(tableName)) {
				System.out.println("setting the custom from date :" + fromDateMap.get(tableName));
				System.out.println("from date map has following keys " + fromDateMap);
				stmt.setDate(4, fromDateMap.get(tableName));
			} else {
				System.out
						.println("inside else block and tableName is " + tableName + " fromDateMap is " + fromDateMap);
				stmt.setDate(4, tf.getAffectedDate());
			}
			System.out.println("setting to date ");
			// commenting this part as we have changed the to date from date to number on
			// frontend.
			/*
			 * if(toDateMap.containsKey(tableName)) { stmt.setDate(5,
			 * toDateMap.get(tableName)); }else { stmt.setDate(5, tf.getAffectedDate()); }
			 */
			if (bufferDays.containsKey(tableName) && bufferDays.get(tableName) != 0) { // if bufferdays are given
																						// externally
				Date dt = tf.getAffectedDate();
				long inc = dt.getTime() + bufferDays.get(tableName) * 24 * 60 * 60 * 1000;
				Date toDt = new Date(inc);
				stmt.setDate(5, toDt);
				System.out.println("setting the custom bufferdays value");

			} else { // if default buffer days needs to be passed
				if (tf.getBufferDays() == 0) {// if tablefilterd buffer days are passed
					System.out.println("setting the default affected date in the to date field");
					stmt.setDate(5, tf.getAffectedDate());

				} else {// if no buffer is given
					System.out.println("setting the passed universal buffer days value");
					Date dt = tf.getAffectedDate();
					long inc = dt.getTime() + tf.getBufferDays() * 24 * 60 * 60 * 1000;
					Date toDt = new Date(inc);
					stmt.setDate(5, toDt);
				}
			}
			System.out.println("setting the table name " + tableName);
			stmt.setString(6, tableName);
			System.out.println("setting affected DAte " + tf.getAffectedDate());
			stmt.setDate(7, tf.getAffectedDate());
			System.out.println("table arguments are ---------- " + "EXCEPTION_MANAGEMENT " + tf.getMeterId() + "  "
					+ tf.getSdpId() + "  " + tf.getAffectedDate() + " " + tf.getAffectedDate() + " " + tableName + "  "
					+ tf.getAffectedDate());
			ResultSet res = stmt.executeQuery();
			list = new ArrayList<String[]>();
			boolean firstRow = true;
			while (res.next()) {
				if (firstRow) {
					String[] arr = res.getString(2).split(",");
					System.out.println("metadata is " + Arrays.toString(arr));
					list.add(arr);
					firstRow = false;
				} else {
					String[] arr = res.getString(2).substring(1, res.getString(2).length() - 1).split(",");
					list.add(arr);
				}
			}

//			 Serv.exceptionFlag=true;
//			 Serv.exception= new FileNotFoundException("you have got an exception - your meter id is not present: ");
		} catch (Exception e) {
			Serv.exceptionFlag = true;
			Serv.exception = e;
			e.printStackTrace();
		}
		return list;
	}

	// for vee module
	public List<String[]> veeModuleService(Map<String, Integer> bufferDays, Map<String, java.sql.Date> fromDateMap,
			Map<String, java.sql.Date> toDateMap, String tableName, TableFilters tf, Date fromDate, Date toDate) {
		List<String[]> list = new ArrayList<String[]>();
		Serv.exceptionFlag = false;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(this.dataSourceUrl, this.dataSourceUserName,
					this.dataSourceUserPassword);
			java.sql.CallableStatement stmt = con
					.prepareCall("select * from (PKG_RETURN_TABLES_VEE.FUNC_RET_ROWS(:1 ,:2 ,:3 ,:4 ,:5,:6,:7 ))");
			stmt.setString(1, "EXCEPTION_MANAGEMENT");
			stmt.setString(2, tf.getMeterId());
			stmt.setString(3, tf.getSdpId());
			System.out.println("setting from date ");
			if (fromDateMap.containsKey(tableName)) {
				System.out.println("setting the custom from date :" + fromDateMap.get(tableName));
				System.out.println("from date map has following keys " + fromDateMap);

				stmt.setDate(4, fromDateMap.get(tableName));
			} else {
				System.out
						.println("inside else block and tableName is " + tableName + " fromDateMap is " + fromDateMap);
				// experimenting
				// Create a Calendar instance and set it to the date you want to manipulate
				// Calendar calendar = Calendar.getInstance();
				// calendar.setTime(tf.getAffectedDate());
				// Subtract 5 days
				// calendar.add(Calendar.DAY_OF_MONTH, -tf.getBufferDays());
				// Get the modified date
				// Date fromDt = (Date) calendar.getTime();
				// remove till here to revert it to last stage

				stmt.setDate(4, tf.getAffectedDate());
				// stmt.setDate(4, fromDt);
			}
			// stmt.setDate(4, (java.sql.Date) fromDate);
			if (bufferDays.containsKey(tableName) && bufferDays.get(tableName) != 0) { // if bufferdays are given
																						// externally
				Date dt = tf.getAffectedDate();
				long inc = dt.getTime() + bufferDays.get(tableName) * 24 * 60 * 60 * 1000;
				Date toDt = new Date(inc);
				stmt.setDate(5, toDt);
				System.out.println("setting the custom bufferdays value");

			} else { // if default buffer days needs to be passed
				if (tf.getBufferDays() == 0) {// if tablefilterd buffer days are passed
					System.out.println("setting the default affected date in the to date field");
					stmt.setDate(5, tf.getAffectedDate());

				} else {// if no buffer is given
					System.out.println("setting the passed universal buffer days value");
					Date dt = tf.getAffectedDate();
					long inc = dt.getTime() + tf.getBufferDays() * 24 * 60 * 60 * 1000;
					Date toDt = new Date(inc);
					stmt.setDate(5, toDt);
				}
			}
			// stmt.setDate(5, (java.sql.Date) toDate);
			stmt.setString(6, tableName.toUpperCase());
			stmt.setDate(7, tf.getAffectedDate());
			System.out.println("table arguments are ---------- " + "EXCEPTION_MANAGEMENT " + tf.getMeterId() + "  "
					+ tf.getSdpId() + "  " + fromDate + toDate + tableName + "  " + tf.getAffectedDate());
			ResultSet res = stmt.executeQuery();
			list = new ArrayList<String[]>();
			boolean firstRow = true;
			while (res.next()) {
				if (firstRow) {
					String[] arr = res.getString(2).split(",");
					System.out.println("metadata is " + Arrays.toString(arr));
					list.add(arr);
					firstRow = false;
				} else {
					String[] arr = res.getString(2).substring(1, res.getString(2).length() - 1).split(",");
					list.add(arr);
				}
			}
			// Serv.exceptionFlag=true;
			// Serv.exception= new FileNotFoundException("you have got an exception - your
			// meter id is not present: ");
		} catch (Exception e) {
			Serv.exceptionFlag = true;
			Serv.exception = e;
			e.printStackTrace();
		}
		return list;
	}

	// for iec extracts & billing window module
	public List<String[]> iecExtractBillingWindowModuleService(Map<String, Integer> bufferDays,
			Map<String, java.sql.Date> fromDateMap, Map<String, java.sql.Date> toDateMap, String tableName,
			TableFilters tf, Date fromDate, Date toDate) {
		List<String[]> list = new ArrayList<String[]>();
		Serv.exceptionFlag = false;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(this.dataSourceUrl, this.dataSourceUserName,
					this.dataSourceUserPassword);
			java.sql.CallableStatement stmt = con.prepareCall(
					"select * from (PKG_RETURN_TABLES_IEC_EXTRACT.FUNC_RET_ROWS(:1 ,:2 ,:3 ,:4 ,:5,:6,:7 ))");
			stmt.setString(1, "BILLING_EXTRACT");
			stmt.setString(2, tf.getMeterId());
			stmt.setString(3, tf.getSdpId());
			System.out.println("setting from date ");
			if (fromDateMap.containsKey(tableName)) {
				System.out.println("setting the custom from date :" + fromDateMap.get(tableName));
				System.out.println("from date map has following keys " + fromDateMap);
				stmt.setDate(4, fromDateMap.get(tableName));
			} else {
				System.out
						.println("inside else block and tableName is " + tableName + " fromDateMap is " + fromDateMap);
				stmt.setDate(4, tf.getAffectedDate());
			}
			// stmt.setDate(4, (java.sql.Date) fromDate);
			if (bufferDays.containsKey(tableName) && bufferDays.get(tableName) != 0) { // if bufferdays are given
																						// externally
				Date dt = tf.getAffectedDate();
				long inc = dt.getTime() + bufferDays.get(tableName) * 24 * 60 * 60 * 1000;
				Date toDt = new Date(inc);
				stmt.setDate(5, toDt);
				System.out.println("setting the custom bufferdays value");

			} else { // if default buffer days needs to be passed
				if (tf.getBufferDays() == 0) {// if tablefilterd buffer days are passed
					System.out.println("setting the default affected date in the to date field");
					stmt.setDate(5, tf.getAffectedDate());

				} else {// if no buffer is given
					System.out.println("setting the passed universal buffer days value");
					Date dt = tf.getAffectedDate();
					long inc = dt.getTime() + tf.getBufferDays() * 24 * 60 * 60 * 1000;
					Date toDt = new Date(inc);
					stmt.setDate(5, toDt);
				}
			}
			// stmt.setDate(5, (java.sql.Date) toDate);
			stmt.setString(6, tableName.toUpperCase());
			stmt.setDate(7, tf.getAffectedDate());
			System.out.println("table arguments are ---------- " + "EXCEPTION_MANAGEMENT " + tf.getMeterId() + "  "
					+ tf.getSdpId() + "  " + fromDate + toDate + tableName + "  " + tf.getAffectedDate());
			ResultSet res = stmt.executeQuery();
			list = new ArrayList<String[]>();
			boolean firstRow = true;
			while (res.next()) {
				if (firstRow) {
					String[] arr = res.getString(2).split(",");
					System.out.println("metadata is " + Arrays.toString(arr));
					list.add(arr);
					firstRow = false;
				} else {
					String[] arr = res.getString(2).substring(1, res.getString(2).length() - 1).split(",");
					list.add(arr);
				}
			}// Serv.exceptionFlag=true;
			// Serv.exception= new FileNotFoundException("you have got an exception - your
			// meter id is not present: ");
		} catch (Exception e) {
			Serv.exceptionFlag = true;
			Serv.exception = e;
			e.printStackTrace();
		}
		return list;
	}

	// for dse realtime module
	public List<String[]> dseRealtimeModuleService(Map<String, Integer> bufferDays,
			Map<String, java.sql.Date> fromDateMap, Map<String, java.sql.Date> toDateMap, String tableName,
			TableFilters tf, Date fromDate, Date toDate) {
		List<String[]> list = new ArrayList<String[]>();
		Serv.exceptionFlag = false;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(this.dataSourceUrl, this.dataSourceUserName,
					this.dataSourceUserPassword);
			java.sql.CallableStatement stmt = con.prepareCall(
					"select * from (PKG_RETURN_TABLES_DSE_REALTIME.FUNC_RET_ROWS(:1 ,:2 ,:3 ,:4 ,:5,:6,:7 ))");
			stmt.setString(1, "PKG_CIM_DSE_RT");
			stmt.setString(2, tf.getMeterId());
			stmt.setString(3, tf.getSdpId());
			System.out.println("setting from date ");
			if (fromDateMap.containsKey(tableName)) {
				System.out.println("setting the custom from date :" + fromDateMap.get(tableName));
				System.out.println("from date map has following keys " + fromDateMap);
				stmt.setDate(4, fromDateMap.get(tableName));
			} else {
				System.out
						.println("inside else block and tableName is " + tableName + " fromDateMap is " + fromDateMap);
				stmt.setDate(4, tf.getAffectedDate());
			}
			// stmt.setDate(4, (java.sql.Date) fromDate);
			if (bufferDays.containsKey(tableName) && bufferDays.get(tableName) != 0) { // if bufferdays are given
																						// externally
				Date dt = tf.getAffectedDate();
				long inc = dt.getTime() + bufferDays.get(tableName) * 24 * 60 * 60 * 1000;
				Date toDt = new Date(inc);
				stmt.setDate(5, toDt);
				System.out.println("setting the custom bufferdays value");

			} else { // if default buffer days needs to be passed
				if (tf.getBufferDays() == 0) {// if tablefilterd buffer days are passed
					System.out.println("setting the default affected date in the to date field");
					stmt.setDate(5, tf.getAffectedDate());

				} else {// if no buffer is given
					System.out.println("setting the passed universal buffer days value");
					Date dt = tf.getAffectedDate();
					long inc = dt.getTime() + tf.getBufferDays() * 24 * 60 * 60 * 1000;
					Date toDt = new Date(inc);
					stmt.setDate(5, toDt);
				}
			}
			// stmt.setDate(5, (java.sql.Date) toDate);
			stmt.setString(6, tableName.toUpperCase());
			stmt.setDate(7, tf.getAffectedDate());
			System.out.println("table arguments are ---------- " + "EXCEPTION_MANAGEMENT " + tf.getMeterId() + "  "
					+ tf.getSdpId() + "  " + fromDate + toDate + tableName + "  " + tf.getAffectedDate());
			ResultSet res = stmt.executeQuery();
			list = new ArrayList<String[]>();
			boolean firstRow = true;
			while (res.next()) {
				if (firstRow) {
					String[] arr = res.getString(2).split(",");
					System.out.println("metadata is " + Arrays.toString(arr));
					list.add(arr);
					firstRow = false;
				} else {
					String[] arr = res.getString(2).substring(1, res.getString(2).length() - 1).split(",");
					list.add(arr);
				}
			}// Serv.exceptionFlag=true;
			// Serv.exception= new FileNotFoundException("you have got an exception - your
			// meter id is not present: ");
		} catch (Exception e) {
			Serv.exceptionFlag = true;
			Serv.exception = e;
			e.printStackTrace();
		}
		return list;
	}

	// for data hub module
	public List<String[]> dataHubQuery(Map<String, Integer> bufferDays, Map<String, java.sql.Date> fromDateMap,
			Map<String, java.sql.Date> toDateMap, String tableName, TableFilters tf, Date fromDate, Date toDate) {
		List<String[]> list = new ArrayList<String[]>();
		Serv.exceptionFlag = false;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(this.dataSourceUrl, this.dataSourceUserName,
					this.dataSourceUserPassword);
			java.sql.CallableStatement stmt = con
					.prepareCall("select * from (PKG_RETURN_TABLES_DATAHUB.FUNC_RET_ROWS(:1 ,:2 ,:3 ,:4 ,:5,:6,:7 ))");

			// stmt.setString(1, "EXCEPTION_MANAGEMENT");
			stmt.setString(1, "PKG_DATAHUB_PROCESS");
			stmt.setString(2, tf.getMeterId());
			stmt.setString(3, tf.getSdpId());
			System.out.println("setting from date ");
			if (fromDateMap.containsKey(tableName)) {
				System.out.println("setting the custom from date :" + fromDateMap.get(tableName));
				System.out.println("from date map has following keys " + fromDateMap);
				stmt.setDate(4, fromDateMap.get(tableName));
			} else {
				System.out
						.println("inside else block and tableName is " + tableName + " fromDateMap is " + fromDateMap);
				stmt.setDate(4, tf.getAffectedDate());
			}
			// stmt.setDate(4, (java.sql.Date) fromDate);
			if (bufferDays.containsKey(tableName) && bufferDays.get(tableName) != 0) { // if bufferdays are given
																						// externally
				Date dt = tf.getAffectedDate();
				long inc = dt.getTime() + bufferDays.get(tableName) * 24 * 60 * 60 * 1000;
				Date toDt = new Date(inc);
				stmt.setDate(5, toDt);
				System.out.println("setting the custom bufferdays value");

			} else { // if default buffer days needs to be passed
				if (tf.getBufferDays() == 0) {// if tablefilterd buffer days are passed
					System.out.println("setting the default affected date in the to date field");
					stmt.setDate(5, tf.getAffectedDate());

				} else {// if no buffer is given
					System.out.println("setting the passed universal buffer days value");
					Date dt = tf.getAffectedDate();
					long inc = dt.getTime() + tf.getBufferDays() * 24 * 60 * 60 * 1000;
					Date toDt = new Date(inc);
					stmt.setDate(5, toDt);
				}
			}
			// stmt.setDate(5, (java.sql.Date) toDate);
			stmt.setString(6, tableName.toUpperCase());
			stmt.setDate(7, tf.getAffectedDate());
			System.out.println("table arguments are ---------- " + "EXCEPTION_MANAGEMENT " + tf.getMeterId() + "  "
					+ tf.getSdpId() + "  " + fromDate + toDate + tableName + "  " + tf.getAffectedDate());
			ResultSet res = stmt.executeQuery();
			list = new ArrayList<String[]>();
			boolean firstRow = true;
			while (res.next()) {
				if (firstRow) {
					String[] arr = res.getString(2).split(",");
					System.out.println("metadata is " + Arrays.toString(arr));
					list.add(arr);
					firstRow = false;
				} else {
					String[] arr = res.getString(2).substring(1, res.getString(2).length() - 1).split(",");
					list.add(arr);
				}
			}// Serv.exceptionFlag=true;
			// Serv.exception= new FileNotFoundException("you have got an exception - your
			// meter id is not present: ");
		} catch (Exception e) {
			Serv.exceptionFlag = true;
			Serv.exception = e;
			e.printStackTrace();
		}
		return list;
	}

	// for vee module
	public List<String[]> odeModuleService(Map<String, Integer> bufferDays, Map<String, java.sql.Date> fromDateMap,
			Map<String, java.sql.Date> toDateMap, String tableName, TableFilters tf, Date fromDate, Date toDate) {
		List<String[]> list = new ArrayList<String[]>();
		Serv.exceptionFlag = false;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(this.dataSourceUrl, this.dataSourceUserName,
					this.dataSourceUserPassword);
			java.sql.CallableStatement stmt = con
					.prepareCall("select * from (PKG_RETURN_TABLES_ODE.FUNC_RET_ROWS(:1 ,:2 ,:3 ,:4 ,:5,:6,:7 ))");
			stmt.setString(1, "EXCEPTION_MANAGEMENT");
			stmt.setString(2, tf.getMeterId());
			stmt.setString(3, tf.getSdpId());
			System.out.println("setting from date ");
			if (fromDateMap.containsKey(tableName)) {
				System.out.println("setting the custom from date :" + fromDateMap.get(tableName));
				System.out.println("from date map has following keys " + fromDateMap);
				stmt.setDate(4, fromDateMap.get(tableName));
			} else {
				System.out
						.println("inside else block and tableName is " + tableName + " fromDateMap is " + fromDateMap);
				stmt.setDate(4, tf.getAffectedDate());
			}
			// stmt.setDate(4, (java.sql.Date) fromDate);
			if (bufferDays.containsKey(tableName) && bufferDays.get(tableName) != 0) { // if bufferdays are given
																						// externally
				Date dt = tf.getAffectedDate();
				long inc = dt.getTime() + bufferDays.get(tableName) * 24 * 60 * 60 * 1000;
				Date toDt = new Date(inc);
				stmt.setDate(5, toDt);
				System.out.println("setting the custom bufferdays value");

			} else { // if default buffer days needs to be passed
				if (tf.getBufferDays() == 0) {// if tablefilterd buffer days are passed
					System.out.println("setting the default affected date in the to date field");
					stmt.setDate(5, tf.getAffectedDate());

				} else {// if no buffer is given
					System.out.println("setting the passed universal buffer days value");
					Date dt = tf.getAffectedDate();
					long inc = dt.getTime() + tf.getBufferDays() * 24 * 60 * 60 * 1000;
					Date toDt = new Date(inc);
					stmt.setDate(5, toDt);
				}
			}
			// stmt.setDate(5, (java.sql.Date) toDate);
			stmt.setString(6, tableName.toUpperCase());
			stmt.setDate(7, tf.getAffectedDate());
			System.out.println("table arguments are ---------- " + "EXCEPTION_MANAGEMENT " + tf.getMeterId() + "  "
					+ tf.getSdpId() + "  " + fromDate + toDate + tableName + "  " + tf.getAffectedDate());
			ResultSet res = stmt.executeQuery();
			list = new ArrayList<String[]>();
			boolean firstRow = true;
			while (res.next()) {
				if (firstRow) {
					String[] arr = res.getString(2).split(",");
					System.out.println("metadata is " + Arrays.toString(arr));
					list.add(arr);
					firstRow = false;
				} else {
					String[] arr = res.getString(2).substring(1, res.getString(2).length() - 1).split(",");
					list.add(arr);
				}
			}// Serv.exceptionFlag=true;
			// Serv.exception= new FileNotFoundException("you have got an exception - your
			// meter id is not present: ");
		} catch (Exception e) {
			Serv.exceptionFlag = true;
			Serv.exception = e;
			e.printStackTrace();
		}
		return list;
	}

	// for emed read processing batch module
	public List<String[]> emedReadProBatchQuery(Map<String, Integer> bufferDays, Map<String, java.sql.Date> fromDateMap,
			Map<String, java.sql.Date> toDateMap, String tableName, TableFilters tf, Date fromDate, Date toDate) {
		List<String[]> list = new ArrayList<String[]>();
		Serv.exceptionFlag = false;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(this.dataSourceUrl, this.dataSourceUserName,
					this.dataSourceUserPassword);
			java.sql.CallableStatement stmt = con
					.prepareCall("select * from (PKG_RETURN_TABLES_EMED.FUNC_RET_ROWS(:1 ,:2 ,:3 ,:4 ,:5,:6,:7 ))");

			// stmt.setString(1, "EXCEPTION_MANAGEMENT");
			stmt.setString(1, "PKG_EMED");
			stmt.setString(2, tf.getMeterId());
			stmt.setString(3, tf.getSdpId());
			System.out.println("setting from date ");
			if (fromDateMap.containsKey(tableName)) {
				System.out.println("setting the custom from date :" + fromDateMap.get(tableName));
				System.out.println("from date map has following keys " + fromDateMap);
				stmt.setDate(4, fromDateMap.get(tableName));
			} else {
				System.out
						.println("inside else block and tableName is " + tableName + " fromDateMap is " + fromDateMap);
				stmt.setDate(4, tf.getAffectedDate());
			}
			// stmt.setDate(4, (java.sql.Date) fromDate);
			if (bufferDays.containsKey(tableName) && bufferDays.get(tableName) != 0) { // if bufferdays are given
																						// externally
				Date dt = tf.getAffectedDate();
				long inc = dt.getTime() + bufferDays.get(tableName) * 24 * 60 * 60 * 1000;
				Date toDt = new Date(inc);
				stmt.setDate(5, toDt);
				System.out.println("setting the custom bufferdays value");

			} else { // if default buffer days needs to be passed
				if (tf.getBufferDays() == 0) {// if tablefilterd buffer days are passed
					System.out.println("setting the default affected date in the to date field");
					stmt.setDate(5, tf.getAffectedDate());

				} else {// if no buffer is given
					System.out.println("setting the passed universal buffer days value");
					Date dt = tf.getAffectedDate();
					long inc = dt.getTime() + tf.getBufferDays() * 24 * 60 * 60 * 1000;
					Date toDt = new Date(inc);
					stmt.setDate(5, toDt);
				}
			}
			// stmt.setDate(5, (java.sql.Date) toDate);
			stmt.setString(6, tableName.toUpperCase());
			stmt.setDate(7, tf.getAffectedDate());
			System.out.println("table arguments are ---------- " + "EXCEPTION_MANAGEMENT " + tf.getMeterId() + "  "
					+ tf.getSdpId() + "  " + fromDate + toDate + tableName + "  " + tf.getAffectedDate());
			ResultSet res = stmt.executeQuery();
			list = new ArrayList<String[]>();
			boolean firstRow = true;
			while (res.next()) {
				if (firstRow) {
					String[] arr = res.getString(2).split(",");
					System.out.println("metadata is " + Arrays.toString(arr));
					list.add(arr);
					firstRow = false;
				} else {
					String[] arr = res.getString(2).substring(1, res.getString(2).length() - 1).split(",");
					list.add(arr);
				}
			}// Serv.exceptionFlag=true;
			// Serv.exception= new FileNotFoundException("you have got an exception - your
			// meter id is not present: ");
		} catch (Exception e) {
			Serv.exceptionFlag = true;
			Serv.exception = e;
			e.printStackTrace();
		}
		return list;
	}

	// for dgm materializer module
	public List<String[]> dgmMaterializerQuery(Map<String, Integer> bufferDays, Map<String, java.sql.Date> fromDateMap,
			Map<String, java.sql.Date> toDateMap, String tableName, TableFilters tf, Date fromDate, Date toDate) {
		List<String[]> list = new ArrayList<String[]>();
		Serv.exceptionFlag = false;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(this.dataSourceUrl, this.dataSourceUserName,
					this.dataSourceUserPassword);
			java.sql.CallableStatement stmt = con
					.prepareCall("select * from (PKG_RETURN_TABLES_DGM.FUNC_RET_ROWS(:1 ,:2 ,:3 ,:4 ,:5,:6,:7 ))");

			stmt.setString(1, "PKG_GROUPING_MANAGEMENT");
			// stmt.setString(1, "EXCEPTION_MANAGEMENT");
			// stmt.setString(2, tf.getMeterId());
			stmt.setString(2, "CELLIRN1512");
			// stmt.setString(3, tf.getSdpId());
			stmt.setString(3, "CLOCIRN1512");
			System.out.println("setting from date ");
			if (fromDateMap.containsKey(tableName)) {
				stmt.setDate(4, fromDateMap.get(tableName));
			} else {
				stmt.setDate(4, tf.getAffectedDate());
			}
			// stmt.setDate(4, (java.sql.Date) fromDate);
			if (bufferDays.containsKey(tableName) && bufferDays.get(tableName) != 0) { // if bufferdays are given
																						// externally
				Date dt = tf.getAffectedDate();
				long inc = dt.getTime() + bufferDays.get(tableName) * 24 * 60 * 60 * 1000;
				Date toDt = new Date(inc);
				stmt.setDate(5, toDt);

			} else { // if default buffer days needs to be passed
				if (tf.getBufferDays() == 0) {// if tablefilterd buffer days are passed
					stmt.setDate(5, tf.getAffectedDate());

				} else {// if no buffer is given
					Date dt = tf.getAffectedDate();
					long inc = dt.getTime() + tf.getBufferDays() * 24 * 60 * 60 * 1000;
					Date toDt = new Date(inc);
					stmt.setDate(5, toDt);
				}
			}
			// stmt.setDate(5, (java.sql.Date) toDate);
			stmt.setString(6, tableName.toUpperCase());
			stmt.setDate(7, tf.getAffectedDate());
			ResultSet res = stmt.executeQuery();
			if (res == null) {
				System.out.println("result is null");
			}
			list = new ArrayList<String[]>();
			boolean firstRow = true;
			while (res.next()) {
				if (firstRow) {
					String[] arr = res.getString(2).split(",");
					list.add(arr);
					firstRow = false;
				} else {
					String[] arr = res.getString(2).substring(1, res.getString(2).length() - 1).split(",");
					list.add(arr);
				}
			}// Serv.exceptionFlag=true;
			// Serv.exception= new FileNotFoundException("you have got an exception - your
			// meter id is not present: ");
		} catch (Exception e) {
			Serv.exceptionFlag = true;
			Serv.exception = e;
			e.printStackTrace();
		}
		return list;
	}

	// latest test method to fetch the data from the database
	public List<String[]> searchTest(String[] tables, TableFilters tf) {
		System.out.println("passed array value is " + Arrays.toString(tables));
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(this.dataSourceUrl, this.dataSourceUserName,
					this.dataSourceUserPassword);
			// CallableStatement stmt = con.prepareCall(
			// "{call PKG_RETURN_TABLES.FUNC_RET_ROWS(?,?,?,?,?)}");
			java.sql.CallableStatement stmt = con
					.prepareCall("select * from (PKG_RETURN_TABLES.FUNC_RET_ROWS(:1 ,:2 ,:3 ,:4 ,:5 ))");
			// String q="select * from
			// table(PKG_RETURN_TABLES.FUNC_RET_ROWS("+tf.getMeterId()+","+tf.getSdpId()+","+tf.getFrom()+","+tf.getTo()+","+tables+")";
			// PreparedStatement p=con.prepareStatement(q);
			String[] str = tables;
			System.out.println("trimmed string value to be passed is : "
					+ Arrays.toString(tables).substring(1, Arrays.toString(tables).length() - 1));
			String str2 = Arrays.toString(tables).substring(1, Arrays.toString(tables).length() - 1);

			stmt.setString(1, tf.getMeterId());
			stmt.setString(2, tf.getSdpId());
			stmt.setDate(3, (java.sql.Date) tf.getFrom());
			stmt.setDate(4, (java.sql.Date) tf.getTo());
			stmt.setString(5, str2);
			System.out.println("query is " + stmt);
			ResultSet res = stmt.executeQuery();
			List<String[]> list = new ArrayList<String[]>();
			boolean firstRow = true;
			while (res.next()) {
				if (firstRow) {
					String[] arr = res.getString(2).split(",");
					list.add(arr);
					firstRow = false;
				} else {
					System.out.println(res.getString(2));
					String[] arr = res.getString(2).substring(1, res.getString(2).length() - 1).split(",");
					list.add(arr);
				}

			}
			for (String[] s : list) {
				System.out.println("stored list is " + Arrays.toString(s));
			}
			/*
			 * int mid=0; while(res.next()) { mid=res.getInt(1); System.out.println(mid); }
			 * 
			 * String query = ""; for (String table : tables) { query = "select * from " +
			 * table + " where mdvc_id=? "; PreparedStatement ps =
			 * con.prepareStatement(query); ps.setInt(1, mid); ResultSet rs =
			 * ps.executeQuery(); java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			 * List<String[]> list = new ArrayList<String[]>(); String columnName[] = new
			 * String[rsmd.getColumnCount()]; for (int i = 1; i <= rsmd.getColumnCount();
			 * i++) { columnName[i - 1] = rsmd.getColumnName(i); } list.add(columnName);
			 * System.out.println(Arrays.toString(columnName)); while (rs.next()) { String
			 * data[] = new String[rsmd.getColumnCount()]; for (int i = 1; i <=
			 * rsmd.getColumnCount(); i++) { data[i - 1] = rs.getString(i); }
			 * 
			 * list.add(data); System.out.println(Arrays.toString(data)); }
			 */
			// return (List<String[]>) res;
			return new ArrayList<String[]>();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean saveTable(String tableName) {
		try {
			File f = new File("/jndi/src/main/resources/static/tables.txt");
			BufferedWriter fw = new BufferedWriter(new FileWriter(f, true));
			fw.write("," + tableName);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public String getTableNames() {
		String tableName = "";
		try {
			File f = new File("/jndi/src/main/resources/static/tables.txt");
			Scanner reader = new Scanner(f);
			while (reader.hasNextLine()) {
				tableName += reader.nextLine();
				System.out.println("table from the text file is " + tableName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tableName;
	}

	public String getConnectionName() {
		String connectionName = "";

		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(this.dataSourceUrl, this.dataSourceUserName,
					this.dataSourceUserPassword);
			PreparedStatement p1 = con.prepareStatement("select * from global_name");
			ResultSet res1 = p1.executeQuery();
			if (res1.next()) {
				connectionName = res1.getString(1);
			} else {
				connectionName = "invalid db connection";
			}
			PreparedStatement p2 = con
					.prepareStatement("SELECT sys_context ('USERENV','SERVER_HOST') server_host FROM dual");
			ResultSet res2 = p2.executeQuery();
			if (res2.next()) {
				connectionName += " || Host Name : " + res2.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connectionName;
	}

	public List<String[]> search(String[] tables, String tf) {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(this.dataSourceUrl, this.dataSourceUserName,
					this.dataSourceUserPassword);

			String q = "select * from metering_device where CLIENT_MDVC_NUMBER=?";
			PreparedStatement p = con.prepareStatement(q);
			p.setString(1, tf);
			ResultSet res = p.executeQuery();
			int mid = 0;
			while (res.next()) {
				mid = res.getInt(1);
				System.out.println(mid);
			}

			String query = "";
			for (String table : tables) {
				query = "select * from " + table + " where mdvc_id=? ";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setInt(1, mid);
				ResultSet rs = ps.executeQuery();
				java.sql.ResultSetMetaData rsmd = rs.getMetaData();
				List<String[]> list = new ArrayList<String[]>();
				String columnName[] = new String[rsmd.getColumnCount()];
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					columnName[i - 1] = rsmd.getColumnName(i);
				}
				list.add(columnName);
				System.out.println(Arrays.toString(columnName));
				while (rs.next()) {
					String data[] = new String[rsmd.getColumnCount()];
					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						data[i - 1] = rs.getString(i);
					}

					list.add(data);
					System.out.println(Arrays.toString(data));
				}
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// using the entitymanager object here instead of jdbc
	public void searchUsingEntityManager(String[] tables, int tf) {
		// List<List>> list=new ArrayList<>();
		try {
			String query = "";
			for (String table : tables) {
				query = "select * from " + table + " where mdvc_id=?";
				List<List<List<String>>> q = em.createNativeQuery(query).setParameter(1, tf).getResultList();
				// System.out.println(q);

				for (List<List<String>> result : q) {
					for (List<String> res : result) {
						for (String str : res) {
							System.out.print(str + " ");
						}
						System.out.println();
					}
				}
				// q.setParameter(1, tf);

				// List<String> rs=(List<String>)q.getResultList();
				// list.add(rs);
				// ObjectMapper om=new ObjectMapper();
				// List<Object> list=(List<Object>)
				// q.map(x->x.toString()).collect(Collectors.toList());
				/*
				 * for(Object s:list) { String value=String.valueOf(s); //List<Object>
				 * l=Arrays.asList(s); System.out.println(value); }
				 */
				// System.out.println(rs.toString());
				/*
				 * java.sql.ResultSetMetaData rsmd = rs.getMetaData(); List<String[]> list = new
				 * ArrayList<String[]>(); String columnName[] = new
				 * String[rsmd.getColumnCount()]; for (int i = 1; i <= rsmd.getColumnCount();
				 * i++) { columnName[i - 1] = rsmd.getColumnName(i); } list.add(columnName);
				 * System.out.println(Arrays.toString(columnName)); while (rs.next()) { String
				 * data[] = new String[rsmd.getColumnCount()]; for (int i = 1; i <=
				 * rsmd.getColumnCount(); i++) { data[i - 1] = rs.getString(i); }
				 * 
				 * list.add(data); System.out.println(Arrays.toString(data)); }
				 */
				// return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return null;
	}

	public boolean searchSingleResult(String table, int tf) {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(this.dataSourceUrl, this.dataSourceUserName,
					this.dataSourceUserPassword);
			String query = "select * from " + table + " where mdvc_id=?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, tf);
			Qresult qr = new Qresult();
			ResultSet rs = ps.executeQuery();
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			List<String[]> list = new ArrayList<String[]>();
			String columnName[] = new String[rsmd.getColumnCount()];
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				columnName[i - 1] = rsmd.getColumnName(i);
			}
			list.add(columnName);
			System.out.println(Arrays.toString(columnName));
			while (rs.next()) {
				String data[] = new String[rsmd.getColumnCount()];
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					data[i - 1] = rs.getString(i);
				}

				list.add(data);
				System.out.println(Arrays.toString(data));
			}
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<String[]> runRawQuery(String query) {
		List<String[]> list = new ArrayList<String[]>();
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(this.dataSourceUrl, this.dataSourceUserName,
					this.dataSourceUserPassword);
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			java.sql.ResultSetMetaData rsmd = resultSet.getMetaData();

			String columnName[] = new String[rsmd.getColumnCount()];
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				columnName[i - 1] = rsmd.getColumnName(i);
			}
			list.add(columnName);
			System.out.println(Arrays.toString(columnName));
			while (resultSet.next()) {
				String data[] = new String[rsmd.getColumnCount()];
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					data[i - 1] = resultSet.getString(i);
				}
				list.add(data);
				System.out.println(Arrays.toString(data));
			}
		} catch (Exception e) {
			String str[] = new String[1];
			str[0] = e.getLocalizedMessage();
			list.add(str);
			csvgenerator.exception = true;
			e.printStackTrace();
		}
		return list;
	}
}
