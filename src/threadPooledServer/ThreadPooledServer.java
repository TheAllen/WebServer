package threadPooledServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPooledServer implements Runnable{

	protected ServerSocket serverSocket;
	protected int serverPort;
	protected boolean stopped = false;
	protected Thread runningThread;
	protected ExecutorService threadPool = Executors.newFixedThreadPool(10);
	
	public ThreadPooledServer(){
		serverPort = 8080;
	}
	
	public  void initServerSocket(){
		try{
			serverSocket = new ServerSocket(serverPort);
		}catch(IOException e){
			throw new RuntimeException("Cannot find server", e);
		}
	}
	
	public synchronized void stop(){
		stopped = true;
		try{
			serverSocket.close();
		}catch(IOException e){
			throw new RuntimeException("Error closing server ", e);
		}
	}
	
	public boolean isStopped(){
		return stopped;
	}
	
	public void run(){
		synchronized(this){
			runningThread = Thread.currentThread();//setting the thread to the main thread.
		}
		initServerSocket();
		while(!isStopped()){
			Socket clientSocket;
			try{
				clientSocket = serverSocket.accept();
			}catch(IOException e){
				if(isStopped()){
					System.out.println("The server has stopped");
					break;
				}
				throw new RuntimeException("Error accepting client connection ", e);
			}
			
			threadPool.execute(new WorkerRunnable(clientSocket, "Thread Pooled Server"));
		}
		threadPool.shutdown();
		System.out.println("Server stopped.");
	}
	
	
}
