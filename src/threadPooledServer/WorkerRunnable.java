package threadPooledServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WorkerRunnable implements Runnable{
	
	protected Socket clientSocket;
	protected String serverText;
	

	public WorkerRunnable(Socket clientSocket, String serverText){
		this.clientSocket = clientSocket;
		this.serverText = serverText;
	}
	
	public void run(){
		try{
			BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter output = new PrintWriter(clientSocket.getOutputStream());
			long time = System.currentTimeMillis();
			
			output.print(("HTTP/1.1 200 OK\n\nWorkerRunnable: " +
						serverText + "-" + time + " ").getBytes());
			output.flush();
			output.close();
			input.close();
			System.out.print("Process request time: " + time + "ms");
			
		}catch(IOException e){
			throw new RuntimeException("Cannot connect to server ", e);
		}
	}
}
