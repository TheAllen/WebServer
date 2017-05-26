package threadPooledServer;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class Client extends Thread{
	
	private final String USER_AGENT = "Mozilla/5.0";
	private final int THREAD = 1;
	private Thread[] thread = new Thread[THREAD];
	private int threadID;
	
	public Client(int threadID){
		this.threadID = threadID;
	}
	
	public void sendGet() throws Exception{
		
		String url = "http://localhost:8080";
		URL myUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection)myUrl.openConnection();
		
		//call GET method to get requests
		conn.setRequestMethod("GET");
		//Add request header
		conn.setRequestProperty("User-Agent", USER_AGENT);
		
		Map<String, List<String>> responseCode = conn.getHeaderFields();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while((inputLine = in.readLine()) != null){ //setting the inputLine with input stream
			response.append(inputLine);
		}
		
		in.close();
		
		System.out.println(response.toString() + "\n");
	}
	
	public void run(){
		try{
			sendGet();
		}catch(Exception e){
			System.out.println("SendGet() overload...");
		}
	}
}
