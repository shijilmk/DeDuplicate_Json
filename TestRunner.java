package com.myworkdayjobs.wd5.adobe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class TestRunner {
	
	static String outputjson = "output.json";
	static String inputjson = "input.json";
	static String changelog = "changelog.txt";
//	InputStream in = getClass().getResourceAsStream(".\\Files\\leads.json");
	
	/*	Function Name 	: removeducplicates
		Parameters 		: inputjson file, outputjson file and changelog file
		Description 	: Uses standard Json library files to create Json object and array to read Json data and
		looping through the input file to remove duplicate entries. Results file - Output.json 
		*/
public void deduplicatejson(String inputjson, String outputjson, String changelog) throws IOException, ParseException{
			
		// initialize counters and variables
		int countEmailAndID = 0;
		int countEmail = 0;
		int countID = 0;
		String formattedData = "{ leads: []	 }";
		Date date = new Date(); 
		File out = new File("output.json");
//		File in = new File(".\\Files\\leads.json");
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream in = classLoader.getResourceAsStream("leads.json");
//		InputStream in = new File(".\\Files\\leads.json");
//		InputStream in = new FileInputStream(".\\Files\\leads.json");
//		OutputStream out = new FileOutputStream(".\\Files\\output.json");
//		OutputStream out1 = getClass().getOutputStream(out);
//		InputStream in = getClass().getResourceAsStream(".\\Files\\leads.json");
		
		// Print Program purpose
		JsonDataExtract.Initialize(inputjson,outputjson,changelog);
		//logger
		JsonDataExtract.addheader(changelog, inputjson, date);
		//output file path
//		BufferedWriter fw = new BufferedWriter(new InputStreamWriter(out));
//		FileWriter fw=new FileWriter(out);
		StringBuilder contentBuilder = new StringBuilder();
		@SuppressWarnings("resource")
		//input file path, Buffer Reader to read input data
//		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		FileWriter fw=new FileWriter(out);
	    String sCurrentLine;
	        while ((sCurrentLine = br.readLine()) != null)
	        {
	            contentBuilder.append(sCurrentLine).append("\n");
	        }
		
		
		 // Read and filter duplicates, logging reasons for removal to changeFile.
		
		 	JSONObject jsonObj = new JSONObject(contentBuilder.toString());
		 	Iterator<String> itr = jsonObj.keys();
		    JSONArray outputlist = new JSONArray();
		    JSONObject outputjsonObj = new JSONObject(formattedData);
		    JSONArray jsInner = (JSONArray) jsonObj.getJSONArray((String) itr.next());
		    
		    //outer loop iterating through the length of json array
		    for (int i=0;i<jsInner.length();i++) {
		    	
		    	boolean write = true;
		        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
		        //Read Id and Email data from Json Array
		        JSONObject jo1 = jsInner.getJSONObject(i);
		        String id1 = jo1.getString("_id");
		        String emailid1 = jo1.getString("email");
		        String dte1 = jo1.getString("entryDate");
		        Date date1 = parser.parse(dte1);
		        
		        	 //inner loop iterating through the length of json array and find matching string with id and email, email and id
		        	for (int j = 0; j < i; j++){
		        		
		        		//Read Id and Email data from Json Array
		        		JSONObject jo2 = jsInner.getJSONObject(j);
		        		String id2 = jo2.getString("_id");
			        	String emailid2 = jo2.getString("email");
			        	String dte2 = jo2.getString("entryDate");
			        	Date date2 = parser.parse(dte2);
			        	int res = date1.compareTo(date2);// result of date comparison
			        	
			        	
		   		     // Check and remove if both email and id match found and log changes
		        		if((i!=j) &&(id1.equals(id2)) && (emailid1.equals(emailid2))){
		                    countEmailAndID++;
		                    write = false;
		        			// Checking for newer record and log changes
		                    if (res <= 0) {
		                    	JsonDataExtract.emailAndID(emailid2, id2);
		                    	JsonDataExtract.add(changelog, emailid2 +" and "+ id2 +" , was removed because the Email and ID matches an existing record.\n");
			                    break;
		                    }
		                    else {
		                    	JsonDataExtract.emailAndID(emailid1, id1);
		                    	JsonDataExtract.add(changelog, emailid1 +" and "+ id1 +" , was removed because the Email and ID matches an existing record.\n");
			                    break;
		        			}
		        		}
		        		
		        		 // Check and remove if email match found and log changes
		        		if((i!=j) && emailid1.equals(emailid2)){
		                    countEmail++;
		                    write = false;
		        			// Checking for newer record and log changes
		        			if (res <= 0) {
		        				JsonDataExtract.email(emailid2);
		        				JsonDataExtract.add(changelog, emailid2 +", was removed because the ID matches an existing record.\n");
			                    break;
		        			}
		        			else {
		        				JsonDataExtract.email(emailid1);
		        				JsonDataExtract.add(changelog, emailid1 +", was removed because the ID matches an existing record.\n");
			                    break;
		        			}
		        		}		        		

		        		
		        		// Check and remove if Id match found and log changes
		        		if((i!=j) && id1.equals(id2)){
		        			countID++;
		        			write = false;
		        			// Prefer newer records over older ones. If equal, prefer later in record file.
		        			if (res <= 0) {
		        				JsonDataExtract.id(id2);
		        				JsonDataExtract.add(changelog, id2 +", was removed because the ID matches an existing record.\n");
		        				break;
		        			}
		        			else {
		        			// Remove older records
		        				JsonDataExtract.id(id1);
		        				JsonDataExtract.add(changelog, id1 +", was removed because the ID matches an existing record.\n");
		        				break;
		        			}
		        		}
		        		
		        }
		        // Write filtered data to output json array
 		       if(write==true){
 		    	   outputlist.put(jsInner.get(i));
 		       }
		    }
		        
		        // Print number of  Duplicate ID and Emails
		        JsonDataExtract.count(countEmailAndID, countEmail, countID);
		        
		        // write output file
		        outputjsonObj.put("leads",outputlist);
		       // System.out.println("\nJSON Object: "+ outputjsonObj);
		        try{
		        	fw.write(outputjsonObj.toString(4));
		        	fw.flush();
		        	fw.close();
		        } catch (Exception e) {
                    throw new RuntimeException(e);
		        }

 	}

//Main Program

public static void main(String...args) throws IOException, ParseException{
//	 Result result = JUnitCore.runClasses(TestRunner.class);
	//start timer
	final long startTime = System.currentTimeMillis();
	TestRunner runner = new TestRunner();
	runner.deduplicatejson(inputjson,outputjson,changelog);
	//end timer
	final long endTime = System.currentTimeMillis();
	System.out.println("Total execution time in ms: " + (endTime - startTime));
}

//@Test
//public void runner() throws IOException, ParseException{
//	//start timer
//		final long startTime = System.currentTimeMillis();
//		deduplicatejson(inputjson,outputjson,changelog);
//		//end timer
//		final long endTime = System.currentTimeMillis();
//		System.out.println("Total execution time in ms: " + (endTime - startTime));
//}

}
