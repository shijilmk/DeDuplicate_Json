package com.myworkdayjobs.wd5.adobe;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.logging.Level; 
import java.util.logging.Logger;
import org.json.JSONException;
import java.util.Date;
import java.io.FileWriter;
import java.io.File; 

public class JsonDataExtract {
		
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); 
	
	public static void Initialize(String inputjson, String outputjson, String changelog){
		File fl = new File(inputjson);
	
		LOGGER.log(Level.INFO, "\n*** This program is used to de duplicate JSON entries from the file '" + fl.getName() + "' based on keys 'ID' and 'Email'.\n" +
		        "*** Filtered Json results are saved under '"+ outputjson + "'  and logs are written in '" + changelog + "'\n which contains the list of removed entries and corresponding reason for removal.\n" +
		        "*** Please note that the sample input file '" + inputjson + "' will remain unchanged.\n"); 
		LOGGER.log(Level.INFO,"Finding Duplicate Entries....\n");
	}
	
	public static void addheader(String changelog, String inputjson, Date date) throws IOException{
		
		String str = "Removing duplicate JSON entries from " + inputjson + " based on ID and email.\n" + "Process started: " + date + ".\n";
		FileWriter fw=new FileWriter(changelog);
		
		for (int i = 0; i < str.length(); i++) 
            fw.write(str.charAt(i)); 
		 fw.close();
		 
		/*int ch;
		FileReader fr=null;
		try
        { 
            fr = new FileReader(changelog); 
        } 
        catch (FileNotFoundException fe) 
        { 
            System.out.println("File not found"); 
        } 
  
        // read from FileReader till the end of file 
        while ((ch=fr.read())!=-1) 
            System.out.print((char)ch); 
  
        // close the file 
        fr.close(); */
    } 

	public static void emailAndID (String email, String id){
		LOGGER.log(Level.INFO, "Duplicate Email and ID : The Email " + email + " and the id " + id + " are duplicate, hence removed.");
	}
	
	public static void email (String emailid){
		LOGGER.log(Level.INFO, "Duplicate Email '" + emailid + " , hence removed.");
	}
	
	public static void id (String id){

		LOGGER.log(Level.INFO, "Duplicate ID '" + id + " , hence removed.");
	}
	
	public static void add (String changelog, String data) throws JSONException, IOException{
		FileWriter fw=new FileWriter(changelog);
		
		//for (int i = 0; i < data.length(); i++)
		 fw.write(data);
            //fw.write(data.getString(i)); 
		 fw.close();
	}

	public static void count(int countEmailAndID, int countEmail, int countID){
		int cnt_total = countEmailAndID+countEmail+countID;
		LOGGER.log(Level.INFO, "\n---Number of entries with duplicate Email and Id: " + countEmailAndID+
								"\n---Number of entries with duplicate Email: " + countEmail+
								"\n---Number of entries with duplicate Id: " + countID+
								"\n---Total number of duplicate entries found and deleted: " + cnt_total+
								"\nCheck output file for filtered result");
		
	}
	
}
