package SingleThreadedServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleThreadedServer implements Runnable{
	
	protected int serverPort = 8080;
	protected ServerSocket serverSocket = null;
	protected boolean stopped = false;
	protected Thread runningThread = null;
	
	public SingleThreadedServer(int port){
		serverPort = port;
	}

	public void run(){
		synchronized(this){
			runningThread = Thread.currentThread();
		}
		initServerSocket();
		while(!isStopped()){
			Socket clientServer;
			try{
				clientServer = serverSocket.accept();
			}catch(IOException e){
				if(isStopped()){
					System.out.println("Server stopped.");
					return;
				}
				throw new RuntimeException("Error accepting client connection ", e);
			}
			
			try{
				processClientRequest(clientServer);
			}catch(Exception e){
				System.out.println("Error: " + e);
			}
		}
		System.out.println("The server is no longer running.");
	}
	
	private void processClientRequest(Socket socket) throws IOException{
		//InputStream input = socket.getInputStream();
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		OutputStream output = socket.getOutputStream();
		long time = System.currentTimeMillis();
		
		output.write(("HTTP/1.1 200 Ok\n\n <html><body>" + 
					"Singlethreaded server: " + time + 
					"</body></html>").getBytes());
		output.flush();
		output.close();
		input.close();
		System.out.println("Request processed: " + time);
	}
	
	public synchronized void stop(){
		stopped = true;
		try{
			//stop the server.
			serverSocket.close();
		}catch(Exception e){
			throw new RuntimeException("Error closing server", e);
		}
	}
	
	private boolean isStopped(){
		return stopped;
	}
	
	public void initServerSocket() {
		try{
			serverSocket = new ServerSocket(8080);
		}catch(Exception e){
			throw new RuntimeException("Cannot open port 8080", e);
		}
	}
	
}
