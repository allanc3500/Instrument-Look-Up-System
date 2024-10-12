/***************************************************************
Student Name: Allan Chung
File Name: MusicClient.java
Assignment number: Project 5

Description: MusicClient class. User chooses choices from the list of options
			This gets sent to the server. Server sends this information to the 
			database. The database will return information to the server, which
			will return information back to the client to display in a dialog box.
***************************************************************/
package uwf.project5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane; 
import javafx.event.ActionEvent;
import javafx.event.EventHandler;




/* 
 * MusicClient class. User is to enter information of the type of instrument, brand, maximum cost,
 * and location that they desire. This gets sent to the server and the database to process the data.
 * Data is returned to the client in a dialog box format.
*/
public class MusicClient extends Application {
	ObservableList<String>instrumentArray;
	ComboBox<String> instrumentComboBox;
	ObservableList<String> defaultBrandArray;
	ComboBox<String> brandComboBox;
	TextField maxCost;
	ObservableList<String>warehouseLocation;
	ComboBox<String> warehouseLocationComboBox;
	Socket s = null;
	InputStream instream = null;
	OutputStream outstream = null;
	Scanner in;
	PrintWriter out;
	Button submitButton;
	Label instrumentLabel;
	Label brandLabel;
	Label maxCostLabel;
	Label warehouseLocationLabel;
    long startTime;


	/* 
	 Sets up the lookup GUI containing the necessary features like the parameters and parameter
	 options. There is also a submit button. Pressing this button results in client connecting to the server,
	 allowing data to sent to be processed and retrieved.  
	*/
    @Override
    public void start(Stage stage) {
        GridPane root = new GridPane();
    	choiceSetup();
		sendRequest();
		
        root.add(instrumentLabel, 0, 0);
        root.add(instrumentComboBox, 1, 0);
        
        root.add(brandLabel, 0, 1);
        root.add(brandComboBox, 1, 1);
        
        root.add(maxCostLabel, 0, 2);
        root.add(maxCost, 1, 2);
        
        root.add(warehouseLocationLabel, 0, 3);
        root.add(warehouseLocationComboBox, 1, 3);
       
        root.add(submitButton, 1, 4);
        
        Scene scene = new Scene(root, 300, 250); 
        scene.getRoot();
        scene.getRoot().setStyle("-fx-font-family: 'serif'");         
        stage.setScene(scene);        
        stage.setTitle("Musical Instrument Lookup");
        stage.setScene(scene);
        stage.show();
    }
    
	/* 
		Function to set up the labels and ComboBoxes for each parameter in the lookup program.
		Once an option is chosen in the instrument type option, the brand ComboBox is adjusted to
		fit the parameters for what type of instrument the user chosen.
	*/
	public void choiceSetup() {
		
        instrumentLabel = new Label("Instrument: ");
    	instrumentArray = FXCollections.observableArrayList("all", "guitar", "bass", "keyboard", "drums");
    	instrumentComboBox = new ComboBox<>();
        instrumentComboBox.setItems(instrumentArray);
        instrumentComboBox.getSelectionModel().select(0);
        
        brandLabel = new Label("Brand: ");
        defaultBrandArray = FXCollections.observableArrayList("all", "yamaha", "gibson", "fender", "roland", "alesis", "ludwig");
        brandComboBox = new ComboBox<>();
        brandComboBox.setItems(defaultBrandArray);
        brandComboBox.getSelectionModel().select(0);
        
        instrumentComboBox.setOnAction(event -> {
        	ObservableList<String> updatedBrandArray;
			if ("guitar".equals(instrumentComboBox.getValue())) {
                updatedBrandArray = FXCollections.observableArrayList("all", "yamaha", "gibson");
            } else if ("bass".equals(instrumentComboBox.getValue())) {
            	updatedBrandArray = FXCollections.observableArrayList("all", "fender");
            } else if ("keyboard".equals(instrumentComboBox.getValue())) {
            	updatedBrandArray = FXCollections.observableArrayList("all", "roland", "alesis");
            } else if ("drums".equals(instrumentComboBox.getValue())) {
            	updatedBrandArray = FXCollections.observableArrayList("all", "ludwig", "yamaha");
            } else {
            	updatedBrandArray = FXCollections.observableArrayList("all", "yamaha", "gibson", "fender", "roland", "alesis", "ludwig");
            }
            
			brandComboBox.setItems(updatedBrandArray);
			brandComboBox.getSelectionModel().select(0);

        });

        maxCostLabel = new Label("Max Cost: ");
        maxCost = new TextField();

        warehouseLocationLabel = new Label("Warehouse Location: ");
        warehouseLocation = FXCollections.observableArrayList("all", "PNS", "CLT", "DFW");
        warehouseLocationComboBox = new ComboBox<>(warehouseLocation);
        warehouseLocationComboBox.getSelectionModel().select(0);
    }

	/* 
		Function to connect the client to the server. Includes a Scanner and
		PrintWriter to communicate with the server.
	*/
    public void connectToServer() {
		final int SBAP_PORT = 8888;
        
		try {
			s = new Socket("localhost", SBAP_PORT);
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
        
		try {
			instream = s.getInputStream();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
        
		try {
			outstream = s.getOutputStream();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
        
		 in = new Scanner(instream);
         out = new PrintWriter(outstream);
       
    }

	/* 
		Function to connect the client to the server. Includes a Scanner and
		PrintWriter to communicate.
	 */
    public void sendRequest() {
    		submitButton = new Button("Submit Request");
    		submitButton.setOnAction(new EventHandler<ActionEvent>()  {
        	@Override
        	public void handle(ActionEvent event) {
        		connectToServer();
        		
                String maxCostValue; 
                if (maxCost.getText() == "") {
                	maxCostValue = "none";
                }
                else {
                	maxCostValue = maxCost.getText();
                }
                String instrument = instrumentComboBox.getValue();
                String brand = brandComboBox.getValue();
                String location = warehouseLocationComboBox.getValue();
                String command = instrument + " " + brand + " " + maxCostValue + " " + location; 
                System.out.println("Client is initialized");
                System.out.println("This will be sent to the Server: " + command + "\n");
                startTime = System.nanoTime();
                out.println(command);
                out.flush();
                displayServerResponse();

                
        	}
        });
      
	}
    
	/* 
		Function to display server response in the Client view. Information is displayed in a
		dialog box.
	 */
    public void displayServerResponse() {
        String response = "";
        String responseFormatted = "";
        while(in.hasNextLine()) {
        	response = in.nextLine();
        	if(response.isEmpty()) {
        		break;
        	}
        	responseFormatted = responseFormatted +response + "\n";
        }
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        System.out.println("Elapsed time in nanoseconds: " + elapsedTime);

        System.out.println("Results from the Server: " + "\n" + responseFormatted);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Musical Instrument Lookup");
        alert.setHeaderText("Results");
        alert.setContentText(responseFormatted);
        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch();
    }

}