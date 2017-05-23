package multithreadedServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultithreadedServer implements Runnable{
	
	protected int serverPort = 8080;
	protected ServerSocket serverSocket = null;
	protected boolean isStopped = false;
	protected Thread runningThread = null;
	
	public MultithreadedServer(int port){
		serverPort = port;
	}
	
	private void initServerSocket(){
		try{
			serverSocket = new ServerSocket(serverPort);
		}catch(Exception e){
			throw new RuntimeException("Cannot open port 8080", e);
		}
	}
	
	public synchronized void stop(){
		isStopped = true;
		try{
			serverSocket.close();
		}catch(IOException e){
			throw new RuntimeException("Error closing server", e);
		}
	}
	
	public void run(){
		synchronized(this){
			runningThread = Thread.currentThread();
		}
		initServerSocket();
		while(!isStopped()){
			Socket clientSocket = null;
			try{
				clientSocket = serverSocket.accept();
			}catch(Exception e){
				if(isStopped()){
					System.out.println("The Server has stopped");
					return;
				}
				throw new RuntimeException("Error accepting client connection", e);
			}
			new Thread(new WorkerRunnable(clientSocket, "Multithreaded Server")).start();
		}
		System.out.println("Server Stopped.");
	}
	
	public boolean isStopped(){
		return isStopped;
	}
}
