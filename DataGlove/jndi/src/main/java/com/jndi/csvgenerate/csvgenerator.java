package com.jndi.csvgenerate;

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
import org.springframework.stereotype.Component;
import com.opencsv.CSVWriter;

@Component
public class csvgenerator {
	
//	@Autowired
//	private RestTemplate tfsRestTemplate;

	public void csvZipper(HttpServletResponse httpServletResponse, List<String[]> list) {
		try {
			OutputStream servletOutputStream = httpServletResponse.getOutputStream(); // retrieve OutputStream from
																						// HttpServletResponse
			ZipOutputStream zos = new ZipOutputStream(servletOutputStream); // create a ZipOutputStream from
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

	public void addMultipleTables(HttpServletResponse httpServletResponse, List<ArrayList<String[]>> list,
			List<String> tableName) {
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
					String[] data = row.substring(1, row.length() - 1).split(",");
					writer.writeNext(data);
				}
				writer.flush();
			}
			zos.closeEntry();
			zos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*public void addAttachmentToTicket(Set<String> files, String taskSysId) throws Exception {// this method will need
		// tfsId as the input
		String url = "https://emeatest.tfs.landisgyr.net/tfs/DefaultCollection/MDMS/_apis/wit/workitems/" + taskSysId
				+ "?api-version=5.1";
		for (String file : files) {
			System.out.println(file);
			String attachmentUrl = attachToTfsCloud(file);
			if (!attachmentUrl.equals("notAttached")) {
				String fileName = file.substring(3).replaceAll("\\s", "");
				WorkItem workItem = new WorkItem("add", "/fields/System.History", "Adding The attachment :" + fileName);
				Value value = new Value();
				value.setRel("AttachedFile");
				value.setUrl(attachmentUrl);
				WorkItemPutData workItemPutData = new WorkItemPutData("add", "/relations/-", value);
				HttpHeaders headers = new HttpHeaders();
				headers.set("content-type", "application/json-patch+json");
				List<Object> list = new ArrayList<Object>();
				list.add(workItem);
				list.add(workItemPutData);

				HttpEntity<String> request = new HttpEntity<String>(new ObjectMapper().writeValueAsString(list),
						headers);*/
				//uncomment the below when tfsRestTemplate is configured properly
//				ResponseEntity<String> resp = tfsRestTemplate.exchange(new URI(url), HttpMethod.PATCH, request,
//						String.class);
//				System.out.println(resp);
			/*}
		}
	}*/

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
