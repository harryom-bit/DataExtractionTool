package com.jndi.csvgenerate;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jndi.ctrl.mainCtrl;
import com.jndi.serv.Serv;
import com.opencsv.CSVWriter;

@Component
public class csvgenerator {

//	@Autowired
//	private RestTemplate tfsRestTemplate;

	public static boolean exception=false;

	public void csvZipper(HttpServletResponse httpServletResponse, List<String[]> list) {
		try {
			// OutputStream servletOutputStream = httpServletResponse.getOutputStream(); //
			// retrieve OutputStream from
			String fileName = "example.zip";
			FileOutputStream fos = new FileOutputStream(fileName); // HttpServletResponse
			ZipOutputStream zos = new ZipOutputStream(fos); // create a ZipOutputStream from
															// servletOutputStream

			int count = 0;
			String filename = "file-" + ++count + ".csv";
			ZipEntry entry = new ZipEntry(filename); // create a zip entry and add it to ZipOutputStream
			CSVWriter writer = new CSVWriter(new OutputStreamWriter(zos));
			zos.putNextEntry(entry);
			for (String[] entries : list) {
				System.out.println(Arrays.toString(entries));
				writer.writeNext(entries[1].split(","));
			}

			writer.flush();
			zos.closeEntry();

			zos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addMultipleFiles(int loop, HttpServletResponse httpServletResponse, List<String[]> list) {
		try {
			OutputStream servletOutputStream = httpServletResponse.getOutputStream(); // retrieve OutputStream from
																						// HttpServletResponse
			ZipOutputStream zos = new ZipOutputStream(servletOutputStream); // create a ZipOutputStream from
																			// servletOutputStream

			int count = 0;

			for (int i = 0; i < loop; i++) {
				String filename = "file-" + ++count + ".csv";
				ZipEntry entry = new ZipEntry(filename); // create a zip entry and add it to ZipOutputStream
				CSVWriter writer = new CSVWriter(new OutputStreamWriter(zos));
				zos.putNextEntry(entry);
				for (String[] entries : list) {
					System.out.println(Arrays.toString(entries));
					writer.writeNext(entries[1].split(","));
				}
				writer.flush();
			}

			zos.closeEntry();

			zos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
//	private static List<String> removeQuotes(List<String> record) {
//        // Remove double quotes from each field
//        List<String> cleanedRecord = List.copyOf(record);
//        for (int i = 0; i < cleanedRecord.size(); i++) {
//            cleanedRecord.set(i, cleanedRecord.get(i).replace("\"", ""));
//        }
//        return cleanedRecord;
//    }
	public String[] removeDoubleQuotes(String[] rowWithQuotes) {
		String[] rowWithoutQuotes=Arrays.stream(rowWithQuotes).map(value->value.replace("\"","")).toArray(String[]::new);
		return rowWithoutQuotes;
	}
	//currently used by all the tables to download the data of the selected tables
	public void addMultipleTables(HttpServletResponse httpServletResponse, List<ArrayList<String[]>> list,
			List<String> tableName) {
		Serv.noDataTables=new String[tableName.size()];
		try {
			OutputStream servletOutputStream = httpServletResponse.getOutputStream(); // retrieve OutputStream from
			// HttpServletResponse
			ZipOutputStream zos = new ZipOutputStream(servletOutputStream); // create a ZipOutputStream from
																			// servletOutputStream
			int fileCount = 0;
			int count = 0;
			for (ArrayList<String[]> tab : list) {
				String filename = tableName.get(count++) + "_" + String.valueOf(fileCount++) + ".csv";
				ZipEntry entry = new ZipEntry(filename);// creates a zip entry and add it to ZipOutputStream
				zos.putNextEntry(entry);
				CSVWriter writer = new CSVWriter(new OutputStreamWriter(zos));
				for (String[] rows : tab) {
					String row = Arrays.toString(rows);
					String[] data = removeDoubleQuotes(row.substring(1, row.length() - 1).split(","));
					
					writer.writeNext(data);
				}
				writer.flush();
			}
			mainCtrl.loaderStatus = false;
			zos.closeEntry();
			zos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void storeTablesToTxt(HttpServletResponse httpServletResponse, List<ArrayList<String[]>> list,
			List<String> tableName) {
		System.out.println("calling the storeTablesToTxt");
		int noDataTablesCounter=0;
		Serv.noDataTables=new String[tableName.size()];
		try {
			OutputStream servletOutputStream = httpServletResponse.getOutputStream(); // retrieve OutputStream from
			// HttpServletResponse
			ZipOutputStream zos = new ZipOutputStream(servletOutputStream); // create a ZipOutputStream from
																			// servletOutputStream
			int fileCount = 0;
			int count = 0;
			for (ArrayList<String[]> tab : list) {
				if(tab.size()==1) {
					System.out.println("---------->setting the table with no data");
					Serv.noDataTables[noDataTablesCounter++]=tableName.get(count);
				}
				String filename = tableName.get(count++) + "_" + String.valueOf(fileCount++) + ".txt";
				ZipEntry entry = new ZipEntry(filename);// creates a zip entry and add it to ZipOutputStream
				zos.putNextEntry(entry);
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zos));
				for (String[] rows : tab) {
					String[] rowWithoutQuotes=removeDoubleQuotes(rows);
					String row = String.join(",",rowWithoutQuotes);
					writer.write(row);
					writer.newLine();
				}
				System.out.println("file name is "+entry.getName());
				writer.flush();
			}
			mainCtrl.loaderStatus = false;
			zos.closeEntry();
			zos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void storeExceptionToTxt(HttpServletResponse httpServletResponse, Exception message,
			List<String> tableName) {
		System.out.println("calling the storeTablesToTxt");
		try {
			OutputStream servletOutputStream = httpServletResponse.getOutputStream(); // retrieve OutputStream from
			// HttpServletResponse
			ZipOutputStream zos = new ZipOutputStream(servletOutputStream); // create a ZipOutputStream from
																			// servletOutputStream
			int fileCount = 0;
			int count = 0;
				String filename = tableName.get(count++) + "_" + String.valueOf(fileCount++) + ".txt";
				ZipEntry entry = new ZipEntry(filename);// creates a zip entry and add it to ZipOutputStream
				zos.putNextEntry(entry);
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zos));
				writer.write(message.getMessage());
				writer.flush();
			
			mainCtrl.loaderStatus = false;
			zos.closeEntry();
			zos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void storeTablesToXLSX(HttpServletResponse httpServletResponse, List<ArrayList<String[]>> list,
			List<String> tableName) {
		System.out.println("calling the storeTablesToXLSX");
		Serv.noDataTables=new String[tableName.size()];
		try {
			OutputStream servletOutputStream = httpServletResponse.getOutputStream(); // retrieve OutputStream from
			// HttpServletResponse
			ZipOutputStream zos = new ZipOutputStream(servletOutputStream); // create a ZipOutputStream from
																			// servletOutputStream
			int fileCount = 0;
			int count = 0;
			for (ArrayList<String[]> tab : list) {
				String filename = tableName.get(count++) + "_" + String.valueOf(fileCount++) + ".xlsx";
				ZipEntry entry = new ZipEntry(filename);// creates a zip entry and add it to ZipOutputStream
				zos.putNextEntry(entry);
				int rowNum=0;
				Workbook workbook=new XSSFWorkbook();
				Sheet sheet=workbook.createSheet("Sheet1");
				for (String[] rows : tab) {
					String[] rowWithoutQuotes=removeDoubleQuotes(rows);
					Row row=sheet.createRow(rowNum++);
					int cellNum=0;
					for(String cellData:rowWithoutQuotes) {
						Cell cell=row.createCell(cellNum++);
						cell.setCellValue(cellData);
					}
				}
				try {
					workbook.write(zos);
					workbook.close();
				}catch(Exception e) {
					e.getStackTrace();
				}
				System.out.println("file name is "+entry.getName());
			}
			
			mainCtrl.loaderStatus = false;
			zos.closeEntry();
			zos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void downloadRawQueryData(HttpServletResponse httpServletResponse, List<String[]> list) throws IOException {
		try {
			System.out.println("exception status is "+this.exception);
			OutputStream servletOutputStream = httpServletResponse.getOutputStream();
			ZipOutputStream zos = new ZipOutputStream(servletOutputStream);
			int fileCount = 0;
			if (list.size() > 0) {
				if (this.exception == false) {
					String filename = "rawQueryData" + "_" + String.valueOf(fileCount++) + ".csv";
					ZipEntry entry = new ZipEntry(filename);// creates a zip entry and add it to ZipOutputStream
					zos.putNextEntry(entry);
					CSVWriter writer = new CSVWriter(new OutputStreamWriter(zos));
					for (String[] rows : list) {
						String row = Arrays.toString(rows);
						String[] data = row.substring(1, row.length() - 1).split(",");
						writer.writeNext(data);
					}
					writer.flush();
				} else {
					String filename = "exception" + "_" + String.valueOf(fileCount++) + ".txt";
					ZipEntry entry = new ZipEntry(filename);// creates a zip entry and add it to ZipOutputStream
					zos.putNextEntry(entry);
					CSVWriter writer = new CSVWriter(new OutputStreamWriter(zos));
					for (String[] rows : list) {
						String row = Arrays.toString(rows);
						String[] data = row.substring(1, row.length() - 1).split(",");
						writer.writeNext(data);
					}
					writer.flush();
				}
			}
			mainCtrl.loaderStatus = false;
			zos.closeEntry();
			zos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * public void addAttachmentToTicket(Set<String> files, String taskSysId) throws
	 * Exception {// this method will need // tfsId as the input String url =
	 * "https://emeatest.tfs.landisgyr.net/tfs/DefaultCollection/MDMS/_apis/wit/workitems/"
	 * + taskSysId + "?api-version=5.1"; for (String file : files) {
	 * System.out.println(file); String attachmentUrl = attachToTfsCloud(file); if
	 * (!attachmentUrl.equals("notAttached")) { String fileName =
	 * file.substring(3).replaceAll("\\s", ""); WorkItem workItem = new
	 * WorkItem("add", "/fields/System.History", "Adding The attachment :" +
	 * fileName); Value value = new Value(); value.setRel("AttachedFile");
	 * value.setUrl(attachmentUrl); WorkItemPutData workItemPutData = new
	 * WorkItemPutData("add", "/relations/-", value); HttpHeaders headers = new
	 * HttpHeaders(); headers.set("content-type", "application/json-patch+json");
	 * List<Object> list = new ArrayList<Object>(); list.add(workItem);
	 * list.add(workItemPutData);
	 * 
	 * HttpEntity<String> request = new HttpEntity<String>(new
	 * ObjectMapper().writeValueAsString(list), headers);
	 */
	// uncomment the below when tfsRestTemplate is configured properly
//				ResponseEntity<String> resp = tfsRestTemplate.exchange(new URI(url), HttpMethod.PATCH, request,
//						String.class);
//				System.out.println(resp);
	/*
	 * } } }
	 */

	// to download csv file in csv format
	/*
	 * public void writeToCsv(List<List<Object>> list,Writer writer) { int i=0;
	 * String tableName=""; try { CSVPrinter printer=new
	 * CSVPrinter(writer,CSVFormat.DEFAULT); for(List<Object> r:list) {
	 * tableName=(String) r.get(0); if(r.size()>0) { r.remove(0); } String
	 * row=(String)r.get(0); String[] objects=row.split(",");
	 * System.out.println(Arrays.toString(objects)); printer.printRecord(objects); }
	 * }catch(Exception e) { e.printStackTrace(); } }
	 */

	/*
	 * try { OutputStream servletOutputStream =
	 * httpServletResponse.getOutputStream(); // retrieve OutputStream from
	 * HttpServletResponse ZipOutputStream zos = new
	 * ZipOutputStream(httpServletResponse.getOutputStream()); // create a
	 * ZipOutputStream from servletOutputStream
	 * 
	 * int count = 0; String filename = "file-" + ++count + ".csv"; ZipEntry entry =
	 * new ZipEntry(filename); // create a zip entry and add it to ZipOutputStream
	 * zos.putNextEntry(entry);
	 * 
	 * try { CSVPrinter printer=new
	 * CSVPrinter(httpServletResponse.getWriter(),CSVFormat.DEFAULT); for(String[]
	 * r:list) {
	 * 
	 * System.out.println(Arrays.toString(r)); printer.printRecord(r); }
	 * zos.closeEntry();
	 * 
	 * }catch(Exception e) { e.printStackTrace(); }
	 */

	public void writeToCsvLocal(List<List<Object>> list, Writer writer) {
		try {
			CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
			for (List<Object> r : list) {
				printer.printRecord(r);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * public void writeToCsvSingle(List<List<String>> list,Writer writer) { try {
	 * CSVPrinter printer=new CSVPrinter(writer,CSVFormat.DEFAULT); for(List<String>
	 * r:list) { System.out.println(r); printer.printRecord(); } }
	 */

}
