/***************************************************************
Student Name: Allan Chung
File Name: MusicServer.java
Assignment number: Project 5

Description: MusicServer class. Sets up a socket to connect to the 
			 client and a thread to run the operation of an Instrument Lookup database.
***************************************************************/
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/* MusicServer class. Sets up a port and socket to get communication from the client. 
 * Signals with a print statement if connected and starts the Instrument lookup operation
 * until the user quits.
*/
public class MusicServer {
    public static void main(String[] args) throws IOException{
    	DBCreator sql = new DBCreator();
    	final int SBAP_PORT = 8888;
        ServerSocket server = new ServerSocket(SBAP_PORT);
        System.out.println("Waiting for clients to connect.");

        /* Waiting for clients to connect. When a client submits a request, they will be connected to a server, 
         * and an instance of Runnable called requestOperation that will be created and assigned to the thread.
         * Operation begins until the user quits.
         */
        while(true)
        {
            Socket s = server.accept();
            System.out.println("Client connected.");
            System.out.println("Getting request from Client" + "\n");
            MusicRequest requestOperation = new MusicRequest(s, sql);
            Thread t = new Thread(requestOperation);
            t.start();
        }
    }
}
