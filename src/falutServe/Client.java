package falutServe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author Pardeep 
 * 
 */

public class Client {

	private String hostname;
	static int port;
	private static Socket s = null;
	private int i;
	private static BufferedReader res = null;
	private static PrintStream req = null;
	private static boolean runOnce = false, runOnce2 = false;
	public static ArrayList<String> serverHeader = new ArrayList<String>();
	public static ArrayList<String> serverHeader2 = new ArrayList<String>();
	static TypeServer ts;
	public static boolean tryBackupServerOn = false,isSocketChanged=false;
	public static EncryptionUtil eu = EncryptionUtil.getInstanceEncDec();;
	Client(String hostname, String port, TypeServer ts) {
		this.hostname = hostname;
		this.port = Integer.parseInt(port);
		this.ts = ts;
		serverHeader2.add("na");
		serverHeader2.add("na");
		serverHeader2.add("na");
		serverHeader2.add("na");
	}

	public static boolean on = true;
	static String s1 = "";
	public static String checkLife = "";

	static class PrintThread extends Thread {

		public void run() {
			while (!isSocketChanged)
				try {
					
					if (res.ready() && on) {
						System.out.println("ready res");
						s1 = res.readLine();
						while(!s1.contains("data1publickKey") && !runOnce){
							s1 = res.readLine();
						}
						if (s1.contains("data1publickKey") && (runOnce == false)) {
							readHeader(s1);
							runOnce = true;
							on = true;
						} else if (s1.contains("0") && (runOnce == true)) {
							System.out.println("Server is Active");
							on = true;
						} else {
							checkLife = Integer.toString(a[jj] * 3);
							System.out.println("value of check life : "
									+ checkLife+ " value of s1: "+ s1);
							if (s1.contains(checkLife)) {
								System.out.println("Server's respond: " + s1);
								on = true;
							} else {
								System.out.println("Server is corrupted");
								on = false;
							}
						}
						System.out.println("Server is Active=> " + on);
						System.out.println();System.out.println();
					}else if(req.checkError()){
							
						 System.out.println("Trying bakcup server after 5seconds.. ");
						 try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 if(runOnce2 == false){
						
							 s.close();
							
						
						 tryBackupServerOn=true;runOnce2=true;isSocketChanged=true;
						 
						 }
					}
				}
				catch (SocketException e1) {
					System.out.println("connection reset: " + e1);
				} catch (IOException e) {
					System.out.println("waiting for streams" +e);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
		}
	}

	private void tryOriginal() throws UnknownHostException, IOException {
		
		s = new Socket(ts.getIpAddr(), port);

	}

	@SuppressWarnings("deprecation")
	private void tryBackup() throws UnknownHostException, IOException {
		System.out.println("connecting to " + serverHeader2.get(1)+"; "+ serverHeader2.get(2));
	
		s = new Socket(serverHeader2.get(1), Integer.parseInt(serverHeader2.get(2)));
		pt.stop();
//		InetAddress adr = InetAddress.getByName(ts.getIpAddr_bkp());
//		if (adr.isReachable(1000)) {
//			System.out.println("backup server reachable");
//		}
		
		
	}

	public void serve() {
		eu.genKey();
		try {
			if(isSocketChanged){
				System.out.println("changing socket...trying Backup Server");
				tryBackup();
				isSocketChanged=false;
				res.close();
				req.close();
				runOnce = false; runOnce2 = false; on = true;
				serverHeader.clear();
				serverHeader2.clear();
				serverHeader2.add("na");
				serverHeader2.add("na");
				serverHeader2.add("na");
				serverHeader2.add("na");
				initializeStreams();
				
				runServerBody();
			}
			else{
				tryOriginal();
				initializeStreams();
				runServerBody();
				
			}
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			System.out.println("1 1 " + e1);

		} catch (ConnectException e1) {
			// e1.printStackTrace();
			System.out.println("ma ki tait" + e1);

		} catch (IOException e1) {// connection refused or server is not up
			// TODO Auto-generated catch block
			System.out.println("3 3 " + e1);

		}

		

	}

	private void initializeStreams() {
		try {
			res = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (IOException e) {
			System.out.println(e);
		}

		try {
			req = new PrintStream(s.getOutputStream());
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	static PrintThread pt;
	private void runServerBody() {

		System.out.println("Checking Connection...");
		req.println("0");
		 pt= new PrintThread();
		pt.start();

		final Timer timer = new Timer();
		i = -1;
		do {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (runOnce == false);

		timer.schedule(new TimerTask() {
			public void run() {
				// do your work
				// System.out.println("Server status: "+ checkserver(s));
				// System.out.println(a++);
				// {System.out.println(Integer.parseInt(serverHeader.get(2)));}
				jj = (++i) % 9;
				req.println(a[jj]);
				// Scanner in=new Scanner(System.in);

				System.out.println(new Date() + "number" + a[jj] + "value "	+ jj);
				if (tryBackupServerOn == true) {
					serve();
					tryBackupServerOn=false;
					System.out.println("running backup server");
					timer.cancel();
				}
			}
		}, 0, (Integer.parseInt(serverHeader2.get(3))) * 1000);
	}

	public static int jj;
	public static int[] a = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	private static void readHeader(String s) {
		String ServerpubKey = "";
		if(s.contains("data1publickKey:")){
			System.out.println("public key received from server ");
			
			for (String ss : s.split("data1publickKey:")) {
				
				serverHeader.add(ss);
				
			}
			System.out.println("Server public key:"+serverHeader.get(serverHeader.size()-1));
			
			System.out.println("sharing Public key with Server for encryption...");
			String pubKey = eu.getPublicKey();
			//System.out.println(pubKey);
			req.println("data1publicKey:"+pubKey);
		}
		
		

		String s1;
		try {
			s1 = res.readLine();
			System.out.println("reading header data...");
		while(!s1.contains("other1:")){
			System.out.println("waiting for data");
			s1 = res.readLine();
		}
		
		for (String ss : s1.split("other1:")) {
			//System.out.println(ss);
			serverHeader.add(ss);
			System.out.println("header received : " + ss);
			
		}
		
		
		//eu.decodeStringToArrya(serverHeader2.get(serverHeader.size()-1));
		
		
		System.out.println();System.out.println();
		System.out.println("sending header and key to server..");
		req.println("data1:"+ts.getIpAddr_bkp() + "data1:"+ts.getPort_bkp());
System.out.println();System.out.println();
		System.out.println("Decrypting received header to get backup and heartbeat");
		
		for(int ii=1;ii<serverHeader.size();ii++)
		{
			//System.out.println("element at "+ii+" " + serverHeader.get(ii));
		}
		
		System.out.println();System.out.println();
		System.out.println("Decrypting header: ");
		//System.out.println(serverHeader2.get(serverHeader2.size()-1));
		String timerr1 = eu.decText(eu.decodeStringToArrya(serverHeader.get(3)));
		System.out.println(timerr1);
		serverHeader2.set(1,timerr1);
		String timerr2 = eu.decText(eu.decodeStringToArrya(serverHeader.get(4)));
		System.out.println(timerr2);
		serverHeader2.set(2,timerr2);
		String timerr3 = eu.decText(eu.decodeStringToArrya(serverHeader.get(5)));
		System.out.println(timerr3);
		serverHeader2.set(3,timerr3);
		System.out.println("intializing hearbeat to => "+serverHeader2.get(3));
		
		System.out.println("starting client......");
		
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//
	
	}
	
}
