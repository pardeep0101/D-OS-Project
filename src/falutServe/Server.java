package falutServe;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;


/**
 * @author Pardeep 
 * 
 */

public class Server {
	public EncryptionUtil eu = null;
	String hostname;
	int port;
	TypeServer ts;
	private ArrayList<Socket> inc = new ArrayList<Socket>();
	private boolean valid = false;
	final static String unauthorized_log = "unauthorized_log.txt";
	private static PrintWriter writer = null;
	FileWriter fw;
	BufferedWriter bw;
	public static int hbt_log = 0;

	// private boolean runOnce = false;
	Server(String hostname, String port, TypeServer ts) {
		this.hostname = hostname;
		this.port = Integer.parseInt(port);
		this.ts = ts;
		this.hbt_log = Integer.parseInt(ts.getHeartBeat());
		try {
			s = new ServerSocket(this.port);

		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private ServerSocket s;
	private Player p[] = new Player[100];
	private static int num = -1;
	public Socket c = null;

	public void serve() {
		eu = EncryptionUtil.getInstanceEncDec();

		System.out.println("running server on " + port);
		fileUtility fu = new fileUtility(unauthorized_log);
		fileUtility fu1 = new fileUtility("client_log.txt");
		fileUtility fu2 = new fileUtility("authorised_client.txt");
		fileUtility fu3 = new fileUtility("client_status.txt");

		fu.createFile();
		fu.openFileStreams();
		fu1.createFile();
		fu1.openFileStreams();
		fu2.createFile();
		fu2.openFileStreams();
		fu3.createFile();
		fu3.openFileStreams();

		while (true) {
			try {
				try {
					System.out.println("Server is Idle and waiting for clients...");
					
					s.setSoTimeout(5000);
					c = s.accept();
					
					inc.add(c);
					System.out.println("Got connection from : "
							+ c.getInetAddress());
					valid = ts.check_vIpAddr(c.getInetAddress().toString());
					if (valid == true) {
						
						p[++num] = new Player(c);
						System.out.println("Client  " + (num + 1)
								+ " entereded");

						String ss = "\n#ClientNumber" + (num) + "Ip#"
								+ c.getRemoteSocketAddress().toString()
								+ "#Port#" + c.getLocalPort()
								+ "#HeartbeatRate#" + ts.getHeartBeat()
								+ "#Timestamp#" + System.currentTimeMillis()
								+ "#status#" + "Active#";
						System.out.println(ss);
						fu2.openFileStreams();
						fu2.appendTofile(ss);

					} else {
						System.out
								.println("Unauthorized attempt--Connection information is recorded ");

						try {

							String ss = "\nIp#"
									+ c.getRemoteSocketAddress().toString()
									+ "#Port#" + c.getLocalPort() + "#Channel#"
									+ c.getChannel() + "#Timestamp#"
									+ System.currentTimeMillis() + "#status#"
									+ " Inactive#";
							System.out.println(ss);
							fu.openFileStreams();
							fu.appendTofile(ss);
							// writer.println(ss);
							
							System.out
									.println("putting unauthorized connection to sleep");
							Thread.sleep(1000);
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// c.close();
						System.out.println("unauthorised connection closed");
						fu.closeFile();
						throw new SocketTimeoutException();
						
						// writer.close();
					}
				} catch (SocketTimeoutException e) {
					if (valid == true) {

						fu1.openFileStreams();
						String ss = "";
						// System.out.println("runnning again");
						for (int i = 0; i <= num; i++) {

							// if(p[i].checkError()){
							// p[i].status = "InActive";
							// ss =
							// "\n#Client#"+i+"#Status#"+p[i].status+"#HeartbeatRate#"+ts.getHeartBeat()+"#LastReplyRecievedAtTimestamp#"+
							// System.currentTimeMillis();
							// System.out.println(ss);
							// fu1.openFileStreams();
							// fu1.appendTofile(ss);
							// }
							if (p[i].ready()) {

								if (!p[i].runOnce) {
									sendHeader(p, i);
									p[i].runOnce = true;
								}
								//System.out.println("requesting data:");
								int r = p[i].request() * 3;

								// System.out.println("Client "+p[i]+" "+p[i].status);

								//System.out.println("responding data:");
								p[i].respond(r);
								// int j = 1;
								// if (i % 2 == 0)
								// j = 0;
								// for (; j <= num; j += 1) {
								// p[j].respond(r);
								// }
								p[i].status = "Active";
								clientStatus(p, i, fu3, 0, true);
								ss = "\n#Client#" + i + "#Status#"
										+ p[i].status + "#HeartbeatRate#"
										+ ts.getHeartBeat()
										+ "#ReplyRecievedAtTimestamp#"
										+ System.currentTimeMillis();// +"#ReplyExpectedIn#"+(System.currentTimeMillis()+Integer.parseInt(ts.getHeartBeat())+(i*10));
								System.out.println(ss);
								fu1.openFileStreams();
								fu1.appendTofile(ss);

								if (!p[i].clientActiveStatus) {
									fu3.openFileStreams();
									fu3.appendTofile(ss);
									p[i].clientActiveStatus = true;

								}
								p[i].missed_hbt = 0;

							} else if (!(p[i].clientFailedStatus)) {

								// System.out.println("inactive in else");

								clientStatus(p, i, fu3, 1, false);
							}

							// System.out.println("loop end");

						}// for loop
					}
				}
			} catch (IOException e) {
				System.out.println(e);
			}

		}

	}

	private void clientStatus(Player p[], int i, fileUtility fu, int hbt,
			boolean active) {
		// TODO Auto-generated method stub
		String ss = "";
	//	System.out.println("status client method");
		if (active) {
			p[i].missed_hbt = 0;
		} else {
			p[i].missed_hbt += hbt;
		}
		if (p[i].missed_hbt > hbt_log * 1) {
			if (!p[i].clientFailedStatus) {
				p[i].status = "InActive";
				System.out.println("InActive");
				ss = "\n#Client#" + i + "#Status#" + p[i].status
						+ "#HeartbeatRate#" + ts.getHeartBeat()
						+ "#ReplyRecievedAtTimestamp#"
						+ System.currentTimeMillis();// +"#ReplyExpectedIn#"+(System.currentTimeMillis()+Integer.parseInt(ts.getHeartBeat())+(i*10));
				System.out.println(ss);
				fu.openFileStreams();
				fu.appendTofile(ss);
				p[i].clientFailedStatus = true;
			}
		}
		System.out.println("Client#"+p[i] + " not responded from last " + p[i].missed_hbt+" heartbeats");

	}

	private void sendHeader(Player a[], int num) {

		//System.out.println("Encrypting header with Public key of client.. ");
		//System.out.println(ts.getIpAddr_bkp() + "&&" + ts.getPort_bkp() + "&&"+ ts.getHeartBeat().toString());
		System.out.println();
		try {
			// if(a[num].ready()){

			System.out.println("sharing server Public key with client to recieve encrypted data...");

			String pubKey = eu.getPublicKey();
			System.out.println(pubKey);

			a[num].respond("data1publickKey:" + pubKey);
			System.out.println();
			System.out.println();
			System.out.println("requesting Public key from client: ");
			a[num].request("data1publicKey");
			System.out.println("Encrypting header with Public key of client..");
			byte[] encText1 = eu.encText(ts.getIpAddr_bkp(),
					eu.decodeStringPubKey(a[num].ClientPubKey));
			String text1 = eu.decodeArrayToString(encText1);

			byte[] encText2 = eu.encText(ts.getPort_bkp(),
					eu.decodeStringPubKey(a[num].ClientPubKey));
			String text2 = eu.decodeArrayToString(encText2);

			byte[] encText3 = eu.encText(ts.getHeartBeat().toString(),
					eu.decodeStringPubKey(a[num].ClientPubKey));
			String text3 = eu.decodeArrayToString(encText3);

			System.out.println("sending header: ");
			a[num].respond("other1:" + text1 + "other1:" + text2 + "other1:"
					+ text3);

			System.out.println("requesting header from client..");
			a[num].request("header");
			System.out.println("header received from client: ");
			for (int i = 0; i < a[num].clientHeader.size(); i++) {
				System.out.println(a[num].clientHeader.get(i));
			}

			// }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}