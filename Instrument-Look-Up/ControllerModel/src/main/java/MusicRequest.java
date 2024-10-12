/***************************************************************
Student Name: Allan Chung
File Name: MusicRequest.java
Assignment number: Project 5

Description: A runnable class. Contains a run method that calls the doAction class. 
			 The doAction class gets the user's choices and sends them to the database.
			 Contains functions to translate client's info to data that can be entered in 
			 the database and functions to send the database results back to the Client. 
***************************************************************/
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;


import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;



/**
   Tests a database installation by creating and querying
   a sample table.
*/
public class MusicRequest implements Runnable{
	private Socket s;
    private Scanner in;
    private PrintWriter out;
    public Scanner scan;
    public DBCreator sql;
    public String instrument = "";
    public String brand = "";
    public String maxPrice = "";
    public String location = "";
    public String maxPriceString = "";


    /**
		Parameterized constructor to initialize an instance of a Socket and the Database Creator.
    */
   public MusicRequest(Socket aSocket, DBCreator aSql) {
	   s = aSocket;
	   sql = aSql;
	}

   /**
		Run method that includes Scanner and PrintWriter for communication with the MusicClient.
		Contains a doAction that runs the action to communicate with the database and back to the client.
   */
   @Override
   public void run() {
	     try{
	        try{
	            in = new Scanner(s.getInputStream());
	            out = new PrintWriter(s.getOutputStream());
	            try {
					doAction();
				} 
	            catch (Exception e) {
					e.printStackTrace();
				}
	        }
	        finally{
	            s.close();
	        }
	     }
	       catch(IOException exception){
	            exception.printStackTrace();
	       }
   }
   
   /**
	   Translates and sends the data to the server and contains action to retrieve
	   the data from the server to send back to the client.
   */   
   public void doAction() throws Exception {
       while (true)
       {  
          if (!in.hasNext())
            return;
   	   
       String command = in.nextLine();
   	   scan = new Scanner(command);
   	   instrument = scan.next();
   	   brand = scan.next();
   	   maxPrice = scan.next();
   	   location = scan.next();
   	   
   	   System.out.println("Server will execute this request: ");
   	   System.out.println("Instrument: " + instrument);
   	   System.out.println("Brand: " + brand);
   	   System.out.println("Max Price: " + maxPrice);
   	   System.out.println("Location: " + location);
   	   
   	   translateRequestToDB(); 
   	   sendBackToClient(command);
       }
   }
   
   /**
		Translate client requests to syntax that can executed in the database, which is in SQL form.
   */ 
   public void translateRequestToDB() {   	   
   	   if(instrument.contains("all")) {
   		   instrument = " IS NOT NULL ";
   	   }
   	   else {
   		   instrument = " = '" + instrument + "'";
   	   }
   	   if(brand.contains("all")) {
   		   brand = " IS NOT NULL ";
   	   }
   	   else {
   		   brand = " = '" + brand + "'";
   	   }

   	   if(maxPrice.contains("none")) {
   	   	   maxPriceString = "<= 100000";
   	   }
   	   else {
   	   	   maxPriceString = "<= " + maxPrice + "";
   	   }

   	   if(location.contains("all")) {
   		   location = " IS NOT NULL ";
   	   }
   	   else {
   		   location = " = '" + location + "'";
   	   }
   }
 
   /**
		Sends database data back to the client to display the information to the user.
   */ 
   public void sendBackToClient (String command) throws Exception {
	   SimpleDataSource.init("database.properties");
	   Connection conn = SimpleDataSource.getConnection();
	   Statement stat = conn.createStatement();
	   
	   dropPreviousTables();
	   
	   sql.createInstruments(stat);
	   sql.createLocations(stat);
	   sql.createInventory(stat);
	   
	  
	   ResultSet result = sql.getDataForServer(stat, instrument, brand, maxPriceString, location);
	   
	   ResultSetMetaData rsm = result.getMetaData();
	   int cols = rsm.getColumnCount();
	   System.out.println("Sending to Client: "); 
	   String resultsString = "";
	   while(result.next()){
		   	for(int i = 1; i <= cols; i++) {
		   		resultsString += result.getString(i) + " ";
		   	}
	   		resultsString += "\n";
	   }
  		System.out.println(resultsString);  
  		out.println(resultsString);
  		out.println();
  		out.flush();
   }
   
   /**
		Drops previous tables in case they already exist.
   */ 
   public void dropPreviousTables() throws Exception{
	   SimpleDataSource.init("database.properties");
	   Connection conn = SimpleDataSource.getConnection();
	   Statement stat = conn.createStatement();
	   
	   try {  
		  stat.execute("DROP TABLE Instruments"); 
	   }
	   catch (Exception e){ 
		   System.out.println("drop failed"); 
	   }
	   
	   try {  
		  stat.execute("DROP TABLE Locations"); 
	   }
	   catch (Exception e){ 
		   System.out.println("drop failed"); 
	   }
	   	  
	   try {  
		  stat.execute("DROP TABLE Inventory"); 
	   }
	   catch (Exception e){ 
		   System.out.println("drop failed"); 
	   }
   }
}
  

