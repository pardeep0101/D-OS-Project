package falutServe;

import java.io.BufferedReader;
//import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;


/**
 * @author Pardeep 
 * 
 */

public class Player {
	private BufferedReader req;
	private PrintStream res;
	public boolean runOnce = false,clientFailedStatus=false, clientActiveStatus=false;
	public String status ="";
	public ArrayList<String> clientHeader = new ArrayList<String> ();
	String ClientPubKey = null;
	public int missed_hbt=0;
	String details = "";
	Socket pc = null;
	public Player(Socket c) throws IOException {
		req= new BufferedReader(new InputStreamReader(c.getInputStream()));
        res = new PrintStream(c.getOutputStream());
        this.pc = c;
	}
	
	public String toString(){
		
		details = this.pc.getRemoteSocketAddress().toString();
		
		
		return details;
		
	}
	
	public boolean ready() throws IOException{
		
			return req.ready();
		
	}
	
	public boolean checkError() throws IOException{
		
		return this.res.checkError();
	}
	
	public int request() throws IOException
 {
		
			return Integer.parseInt(req.readLine());
		
	}
	
	public void respond(String r) throws IOException
	{	
		res.println(r);
		this.status = "Active";
	}
	
	public void respond(int r) throws IOException
 {
		
			res.println(r);
		
		//res.println(r);
		
	}
	public void request(String s) throws IOException
	{	String s1 = req.readLine();
		if(s.equals("data1publicKey")){
			while(!s1.contains("data1publicKey")){
				System.out.println("waiting for public key of client ");
				s1 = req.readLine();			}
			for (String ss : s1.split("data1publicKey:")) {
				System.out.println(ss);
				ClientPubKey=ss;
		}
		
		}
		if(s.contains("header")){
		while(!s1.contains("data1:")){
			s1 = req.readLine();
		}
		for (String ss : s1.split("data1:")) {
			System.out.println(ss);
			clientHeader.add(ss);
		}
	}
		this.status = "Active";
	}
	}
