/***************************************************************
Student Name: Allan Chung
File Name: DBCreator.java. 
Assignment number: Project 5

Description: Creates databases of instrument, locations, and inventory.
***************************************************************/
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBCreator {

	/* Creates database of the instruments Returns the set */
	public ResultSet createInstruments(Statement stat) throws Exception
	  {
	         stat.execute("CREATE TABLE Instruments (instName CHAR(12),instNumber INTEGER,cost DOUBLE,descrip CHAR(20))");
	         stat.execute("INSERT INTO Instruments VALUES ('guitar',1,100.0,'yamaha')");
	         stat.execute("INSERT INTO Instruments VALUES ('guitar',2,500.0,'gibson')");
	         stat.execute("INSERT INTO Instruments VALUES ('bass',3,250.0,'fender')");
	         stat.execute("INSERT INTO Instruments VALUES ('keyboard',4,600.0,'roland')");
	         stat.execute("INSERT INTO Instruments VALUES ('keyboard',5,500.0,'alesis')");
	         stat.execute("INSERT INTO Instruments VALUES ('drums',6,1500.0,'ludwig')");
	         stat.execute("INSERT INTO Instruments VALUES ('drums',7,400.0,'yamaha')");
	         ResultSet result = stat.executeQuery("SELECT * FROM Instruments");
	         return result;
	  }

	  /* Creates database of the locations. Returns the set */
	  public ResultSet createLocations(Statement stat) throws Exception
	  {
	         stat.execute("CREATE TABLE Locations (locName CHAR(12),locNumber INTEGER,address CHAR(50))");
	         stat.execute("INSERT INTO Locations VALUES ('PNS',1,'Pensacola Florida')");
	         stat.execute("INSERT INTO Locations VALUES ('CLT',2,'Charlotte North Carolina')");
	         stat.execute("INSERT INTO Locations VALUES ('DFW',3,'Dallas Fort Worth Texas')");
	         ResultSet result = stat.executeQuery("SELECT * FROM Locations");
	         return result;
	  }

	  /* Creates database of the inventories. Returns the set */
	  public ResultSet createInventory(Statement stat) throws Exception
	  {
	         stat.execute("CREATE TABLE Inventory (iNumber INTEGER,lNumber INTEGER,quantity INTEGER)");
	         stat.execute("INSERT INTO Inventory VALUES (1,1,15)");
	         stat.execute("INSERT INTO Inventory VALUES (1,2,27)");
	         stat.execute("INSERT INTO Inventory VALUES (1,3,20)");
	         stat.execute("INSERT INTO Inventory VALUES (2,1,10)");
	         stat.execute("INSERT INTO Inventory VALUES (2,2,10)");
	         stat.execute("INSERT INTO Inventory VALUES (2,3,35)");
	         stat.execute("INSERT INTO Inventory VALUES (3,1,45)");
	         stat.execute("INSERT INTO Inventory VALUES (3,2,10)");
	         stat.execute("INSERT INTO Inventory VALUES (3,3,17)");
	         stat.execute("INSERT INTO Inventory VALUES (4,1,28)");
	         stat.execute("INSERT INTO Inventory VALUES (4,2,10)");
	         stat.execute("INSERT INTO Inventory VALUES (4,3,16)");
	         stat.execute("INSERT INTO Inventory VALUES (5,1,28)");
	         stat.execute("INSERT INTO Inventory VALUES (5,2,10)");
	         stat.execute("INSERT INTO Inventory VALUES (5,3,1)");
	         stat.execute("INSERT INTO Inventory VALUES (6,1,2)");
	         stat.execute("INSERT INTO Inventory VALUES (6,2,10)");
	         stat.execute("INSERT INTO Inventory VALUES (6,3,16)");
	         stat.execute("INSERT INTO Inventory VALUES (7,1,16)");
	         stat.execute("INSERT INTO Inventory VALUES (7,2,4)");
	         stat.execute("INSERT INTO Inventory VALUES (7,3,12)");     
	         ResultSet result = stat.executeQuery("SELECT * FROM Inventory");
	         return result;
	  }

	/* Gets the query data from the server and executes the SQL instruction. Returns result to the server. */
	public ResultSet getDataForServer(Statement stat, String instType, String instBrand, String maxCost, String warehouseLocation) throws SQLException {
		String query = "SELECT Instruments.instName, Instruments.descrip, Instruments.cost, Inventory.quantity, Locations.address "
		        + "FROM Instruments "
		        + "JOIN Inventory ON Instruments.instNumber = Inventory.iNumber "
		        + "JOIN Locations ON Locations.locNumber = Inventory.lNumber "
		        + "WHERE Instruments.instName " + instType + " "
		        + "AND Instruments.descrip " + instBrand + " "
		        + "AND Instruments.cost " + maxCost + " "
		        + "AND Locations.locName " + warehouseLocation;
		
		System.out.println("Query is: " + query + "\n");
		ResultSet result = stat.executeQuery(query);	
			return result;
	}


}

