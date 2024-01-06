package com.thymeleaf.demo;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
public class TestService {
	
	@Autowired
	private SpringTemplateEngine templateEngine;
	
	private static final Logger logger = LoggerFactory.getLogger(TestService.class);
	
	
	public byte[] xhtmlToPdf( String html) throws Exception {
		 try ( ByteArrayOutputStream os = new ByteArrayOutputStream(); ) {
			 ITextRenderer renderer = new ITextRenderer();
			 SharedContext sharedContext = renderer.getSharedContext();
			 sharedContext.setInteractive(false);
			 renderer.setDocumentFromString(html);
			    renderer.layout();
			    renderer.createPDF(os);
			    renderer.finishPDF();
			    System.out.println("PDF creation completed"); 
			    return os.toByteArray();
		}
	}
	
	public String toXHTML(String html) {
		Document document = Jsoup.parse(html);
		document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);  
		return document.html();
	}
	
	public void convert() {
		File outputPdf = new File("C:\\Users\\Ritik_Kumar\\Documents\\outputHTML.pdf");
		File inputHTML = new File("C:\\Users\\Ritik_Kumar\\git\\incentivo\\frontend\\user_Interface\\src\\invoice.html");
		Document inputHtml = createWellFormedHtml(inputHTML);
		System.out.println("Starting conversion to PDF...");
		try {
			test();
//			xhtmlToPdf(inputHtml, outputPdf);
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	
	private Document createWellFormedHtml(File html) {
		try {
			Document document = Jsoup.parse(html,"UTF-8");
			document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);	
	        return document;
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}
	
	private void xhtmlToPdf1( String xhtml, File outputPdf ) throws IOException {
		 try ( OutputStream os = new FileOutputStream(outputPdf);) {
			 ITextRenderer renderer = new ITextRenderer();
			 SharedContext sharedContext = renderer.getSharedContext();
			 sharedContext.setInteractive(false);
			 renderer.setDocumentFromString(xhtml);
			    renderer.layout();
			    renderer.createPDF(os);
			    renderer.finishPDF();
			    System.out.println("PDF creation completed"); 
		}
	}
	
	public void test() {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream is = classloader.getResourceAsStream("templates/invoiceTemplate.html");
		System.out.println("hello");
		URL resouce = TestService.class.getClassLoader().getResource("templates/invoiceTemplate.html");
		String html = "" ;
		if(resouce != null) {
			File file = new File(resouce.getFile());
			
			StringBuilder stringBuilder = new StringBuilder();
			try(BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(resouce.openStream()))){
				String val;
				while((val = bufferedReader.readLine()) != null) {
					stringBuilder.append(val);
				}
				html = stringBuilder.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Map<String,Object> mapOfProperties = new HashMap<>();
//	    String[] headerKeys = { "Sr. No.", "Case Id", "Agency Name", "Process Month",
//                "Loan Amount", "Total Amount" };
//	    List<Map<String,Object>> myListData = new ArrayList<>();
//	    for(int i = 0; i < 2; i++) {
//	    	Map<String,Object> body = new HashMap<>();
//	    	body.put("Sr. No." , 1);
//	    	body.put("Case Id", 10000);
//	    	body.put("Agency Name", "Ritik Kumar");
//	    	body.put("Process Month", "January");
//	    	body.put("Loan Amount", "10000000.0");
//	    	body.put("Total Amount", 50000);
//	    	myListData.add(body);
//	    }
//	    List<List<Object>> listData = new ArrayList<>();
//	    for( Map<String,Object> l : myListData ) {
//	    	List<Object> ll = new ArrayList<>();
//	    	for( String h : headerKeys ) {
//	    		ll.add(l.get(h));
//	    	}
//	    	listData.add(ll);
//	    }
//	    
//	    String s = "String \n Hello \n Ritik";
//	    
//	    mapOfProperties.put("spAddDetails",s );
//	    
//	    List<Map<String,Object>> m = new ArrayList();
//	    
//	    List<String> headers = new ArrayList<>();
//	    
//	    headers.add("Name");
//	    headers.add("RollNo");
//	    headers.add("Class");
//	    
//	    for(int i = 0; i < 3; i++) {
//	    	Map<String,Object> o = new HashMap<>();
//	    	o.put("Name","Ritik" + i);
//	    	o.put("RollNo","101" + i);
//	    	o.put("Class","B.Tech" +  i);
//	    	m.add(o);
//	    }
//	    
//	    mapOfProperties.put("fields", headers);
//	    mapOfProperties.put("headers", headers);
//	    mapOfProperties.put("data", m);
	    
	    
	    
//	    mapOfProperties.put("fields",headerKeys);
//	    mapOfProperties.put("list", listData);
//	    Map<String,Object> map = new HashMap<>();
//	    map.put("name", "Ritik");
//	    map.put("list", listData);
//	    map.put("fields", headerKeys);
//	    map.put("spName", "Ritik Kumar");
//	    map.put("spAddDetails", "This is invoice Template . Generated by Act 21 for testing during pdf from html code");
//	    map.put("srName", "Ritik Kumar");
//	    map.put("srAddDetails", "This is invoice Template . Generated by Act 21 for testing during pdf from html code");	    
//		map.put("qalabel1", "Google Com.");
//		map.put("tpalabel1","HTML");
////		map.put("adlabel1", "Ritik");
//		map.put("adleft","MMM");
////		map.put("aaleft", "");
//		map.put("remarks", "SomeRemarks.");
//		map.put("amountChInWords", "Only."); 
	    
	    mapOfProperties.put("id", "1");
	    mapOfProperties.put("Case Id", "7855522");
	    
	    Map<String,Object> data_Map = new HashMap();
	    
	    Map<String,Object> caseStatusWiseReport = new HashMap<>();
	    caseStatusWiseReport.put("caseWiseStatusReport", mapOfProperties);
	    data_Map.put("payload", caseStatusWiseReport);
	    
	    
		String output =templateEngine.process(html, new Context(Locale.getDefault(),data_Map));
		System.out.println(output);
		String xhtml = toXHTML(output);
		File file = new File("C:\\Users\\Ritik_Kumar\\Documents\\outputHTML.pdf");
		try {
			xhtmlToPdf1(xhtml, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(output);
//		return output;
	}

}
