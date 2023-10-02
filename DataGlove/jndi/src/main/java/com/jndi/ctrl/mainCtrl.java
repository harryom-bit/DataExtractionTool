package com.jndi.ctrl;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jndi.csvgenerate.csvgenerator;
import com.jndi.model.TableFilters;
import com.jndi.model.tableFromDate;
import com.jndi.serv.JdbcService;
import com.jndi.serv.Serv;

import oracle.sql.DATE;

@CrossOrigin(origins="*",allowedHeaders="*")
@RestController
public class mainCtrl {
	
	TableFilters tbfl;
	
		
	@Autowired
	private Serv s;
	
	@Autowired
	private csvgenerator cg;
	
	@Autowired
    private ServletContext servletContext;
	
	@Autowired
	private JdbcService js;
	
	private String tbl[];
	
	Map<String,Date> fromDate=new HashMap<String,Date>();
	Map<String,Date> toDate=new HashMap<String,Date>();
	Map<String,Integer> bufferDays=new HashMap<String,Integer>();
	
	
	/*@RequestMapping("MDMSDataTables") 
	public List<String[]> resOneByOne1(HttpServletResponse hsr) {
		return s.queryforData(tbfl.getMeterId(), tbfl.getSdpId(), tbfl.getFrom(), tbfl.getTo());
		//return js.searchTest(this.tbl, tbfl);
		
		
		/*List<ArrayList<String[]>> ans=new ArrayList<ArrayList<String[]>>();
		for(String s:this.tbl) {
			List<String[]> toAdd=js.search(new String[] {s},this.tbfl.getMeterId());
			ans.add((ArrayList<String[]>) toAdd);
		}
		System.out.println(ans.size());
		this.cg.addMultipleTables(hsr, ans,this.tbl);
		return "done";*/
	//}
	@RequestMapping("updateFromDate")
	public int updateTableFromDate(@RequestBody tableFromDate tfd) {
		System.out.println("table name is "+tfd.getTableName()+" and from date is "+tfd.getFromDate());
		fromDate.put(tfd.getTableName().toUpperCase().trim(), tfd.getFromDate());
		return 200;
	}
	@RequestMapping("updateBufferDays")
	public int updateTableBufferDays(@RequestBody tableFromDate tfd) throws SQLException {
		bufferDays.put(tfd.getTableName().toUpperCase(), tfd.getBufferDays());
		System.out.println("buffer days are "+bufferDays);
//		if(bufferDays.containsKey(tfd.getTableName())){
//			DATE dt=DATE.getCurrentDate();
//			Calendar c =Calendar.getInstance();
//		}
		return 200;
	}
	@RequestMapping("updateToDate")
	public int updateTableToDate(@RequestBody tableFromDate tfd) {
		System.out.println("table name is "+tfd.getTableName()+" and TO date is "+tfd.getFromDate());
		toDate.put(tfd.getTableName().toUpperCase(), tfd.getFromDate());
		return 200;
	}
	@RequestMapping("MDMSDataTables") 
	public void resOneByOne1(HttpServletResponse hsr) {
		 s.queryforData(this.bufferDays,this.fromDate,this.toDate,tbfl,hsr);
		
	}
	@RequestMapping("VEETables") 
	public void resOneByOne2(HttpServletResponse hsr) {
		 s.queryVeeData(this.bufferDays,this.fromDate,this.toDate,tbfl,hsr);
		
	}
	
	
	//to fetch module related tables
	@RequestMapping("fetchModuleRelatedTables")
	public List<String> fetchModuleRelatedTables(@RequestBody String tableAcronym) {
		System.out.println("tablename is "+tableAcronym);
		System.out.println("printing the tables "+s.getModuleRelatedTables("wacsmdms", "EXCEPTION%"));
		//return s.getModuleRelatedTables("WACSMDMS",tableAcronym+"%");it is used if table is fetched from the DB
		return s.getModuleRelatedTables("WACSMDMS",tableAcronym);
	}
	
	//using jdbcService service class
	@RequestMapping("saveTable")
	public boolean saveTable(@RequestBody String tableName) {
		return this.js.saveTable(tableName);
	}
	
	@RequestMapping("getTables")
	public String[] getTableNames() {
		return this.js.getTableNames().split(",");
	}
	
	@RequestMapping("downloadStatus")
	public String checkStatus(HttpServletResponse hsr) {
		//return this.resOneByOne1(hsr);
		return "uncomment the previous and remove this one";
	}
	
	@RequestMapping("findResult")
	public void getDataBasedOnMeterId() {
		//return js.search(this.tbl, 1);
	}
	
	@RequestMapping("getConnectionName")
	public String getConnectionName() {
		return this.js.getConnectionName();
	}
	
	@RequestMapping("updateTables")
	public boolean updataTables(@RequestBody String[] tables) {
		System.out.println("updated tables are "+Arrays.toString(tables));
			this.tbl=tables;
			this.s.updateTables(tables);
		return true;
	}
	
	@RequestMapping("DataTables")
	public List<ArrayList<String[]>> resOneByOne(HttpServletResponse hsr,@RequestBody String[] tables) {
		System.out.println(Arrays.toString(tables));
		this.tbl=tables;
		List<ArrayList<String[]>> ans=new ArrayList<ArrayList<String[]>>();
		List<String> tab=new ArrayList<String>();
		for(String s:this.tbl) {
			tab.add(s);
			List<String[]> toAdd=js.search(new String[] {s},new String().concat("1"));
			ans.add((ArrayList<String[]>) toAdd);
		}
		System.out.println(ans.size());
		//this.cg.addMultipleTables(hsr, ans,tab);
		return ans;
	}
	/*@RequestMapping("MDMSDataTables") THIS IS THE OLDER METHOD TO DOWNLOAD THE DATA
	public String resOneByOne1(HttpServletResponse hsr) {
		List<ArrayList<String[]>> ans=new ArrayList<ArrayList<String[]>>();
		for(String s:this.tbl) {
			List<String[]> toAdd=js.search(new String[] {s},this.tbfl.getMeterId());
			ans.add((ArrayList<String[]>) toAdd);
		}
		System.out.println(ans.size());
		this.cg.addMultipleTables(hsr, ans,this.tbl);
		return "done";
	}*/
	
	
	@RequestMapping("DataTables12")
	public String resOneByOne12(HttpServletResponse hsr) {
		//List<ArrayList<String[]>> ans=new ArrayList<ArrayList<String[]>>();
		for(String s:this.tbl) {
			js.searchUsingEntityManager(new String[] {s},Integer.parseInt(this.tbfl.getMeterId()));
			//ans.add((ArrayList<String[]>) toAdd);
		}
		//System.out.println(ans.size());
		//this.cg.addMultipleTables(hsr, ans,this.tbl);
		return "done";
	}
	 
	@RequestMapping("findOneTableResult")
	public boolean getSingleTableData(String table,int mdvc_id) {
		return js.searchSingleResult("mdvc_cum_reads", 1);
	}
	
	//using serv service class from here
	@RequestMapping("tables")
	public List<String> getTables() {
		System.out.println(s.getTables());
		return s.getTables();
	}
	
	@GetMapping("meteringDevice")
	public void exportAsCsvMeteringDevice(HttpServletResponse hsr)throws Exception {
		//hsr.setContentType("text/csv");
		//hsr.addHeader("Content-Disposition", "attachment; filename=\"EXCEPTIONMANAGEMENT.csv\"");
		cg.csvZipper(hsr, s.singleRow());
		//cg.writeToCsv(s.getMeteringDevice(), hsr.getWriter());
	}
	
	@GetMapping("addMultipleFiles")
	public void addMultiple(HttpServletResponse hsr)throws Exception {
		cg.addMultipleFiles(2, hsr, s.singleRow());
	}
	
	/*@GetMapping("meteringDeviceFilteredData")
	public void singleRowController(HttpServletResponse hsr)throws Exception {
		hsr.setContentType("text/csv");
		hsr.addHeader("Content-Disposition", "attachment; filename=\"EXCEPTIONMANAGEMENT.csv\"");
		cg.writeToCsv(s.singleRow2(), hsr.getWriter());
	}*/
	
	//to download the table data according to selected table without any filter
	@GetMapping("getFilteredTableData")
	public Map<String,List<List<Object>>> filteredTableData(Object[] tables) {
		Map<String,List<List<Object>>> data=new HashMap<>();
		for(Object obj:tables) {
			String table=obj.toString();
			//data.put(table, s.filteredTable(table));
		}
		System.out.println(data);
		return data;
	}
	
	@RequestMapping(path="tableData")
	public boolean test(@RequestBody String[] tablename)throws Exception {
		System.out.println("table filters are :"+this.tbfl.toString());
		System.out.println(Arrays.toString(tablename));
		return true;
	}
	@RequestMapping(path="tablefilters")
	public int tableFilters(@RequestBody TableFilters filters) {
		System.out.println("received the table filters and they are "+filters.getBufferDays());
		this.fromDate=new HashMap<String,Date>();
		this.toDate=new HashMap<String,Date>();
		this.bufferDays=new HashMap<String,Integer>();
		this.tbfl=filters;
		//TableFilters tf=filters;
		return 200;
	}
	
	
	/*@GetMapping("datesearch")
	public List<List<Object>> test() {
		System.out.println(this.tbfl);
		return s.filteredTable("METERING_DEVICE");
	}*/
	
	/*@GetMapping("mdvcCumReads")
	public void exportAsCsvMdvcCumReads() {
		hsr.setContentType("text/csv");
		hsr.addHeader("Content-Disposition", "attachment; filename=\"EXCEPTIONMANAGEMENT.csv\"");
		cg.writeToCsv(s.getMeteringDevice(), hsr.getWriter());
	}*/
	
	/*@GetMapping("downloadlocalcsv")
	public void exportLocalAsCsv(HttpServletResponse hsr) throws Exception{
		hsr.setContentType("text/csv");
		hsr.setHeader("Content-Disposition", "attachment; filename=\"EXCEPTIONMANAGEMENT.csv\"");
		cg.writeToCsvLocal(s.read(), hsr.getWriter());
	}*/
}
