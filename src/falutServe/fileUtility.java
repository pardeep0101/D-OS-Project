package falutServe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;


/**
 * @author Pardeep 
 * 
 */

public class fileUtility {

	public File file;
	public FileWriter fw;
	public BufferedWriter bw;
	public PrintWriter pw;
	public Scanner sr;
	public String content = "";

	fileUtility(String filename) {
		file = new File(filename);
	}
	
	public void createFile() {
		content = "";
		try {
			sr = new Scanner(System.in);
			if (!file.exists()) {
				if (file.getName().equals("config_server.txt")) {

					System.out
							.println("Enter the machine type: \n Enter 1 for Server \n Enter 2 for chunk server ");
					content = "1 ";
					System.out.println("Lets write config file for server:");

					System.out
							.println("Enter hostaname of your server(without space): ");
					content += sr.next() + " ";

					System.out.println("Enter the Ip address of your server ");
					content += sr.next() + " ";
					System.out
							.println("Enter the listening port for fault ploicy");
					content += sr.next() + " ";
					System.out
							.println("Enter the backup server ip address, if any other wise enter zero ");
					content += sr.next() + " ";
					System.out
							.println("Enter the listening port for fault ploicy , if any other wise enter zero ");
					content += sr.next() + " ";

					System.out.println("Enter the heartbeat for client ");
					content += sr.next() + " ";

					System.out.println();
					System.out.println();
					boolean needClient = true;
					int i = 1;
					System.out
							.println("Enter the validated client ip addresses(enter -1 to finish)");
					do {
						System.out.println("Enter Ip for Client " + i);
						String inp = sr.next();

						if (inp.contains("-1")) {
							content += inp + " ";
							needClient = false;
						} else {
							content += inp + " ";
							i++;
						}
					} while (needClient);

					System.out.println("check data: ");

					System.out.println(content);
					this.openFileStreams();
					this.appendTofile(content);

				}
				if (file.getName().equals("config_client.txt")) {

					System.out.println("Lets write config file for client:");
					// System.out.println("Enter the machine type: \n Enter 1 for Server \n Enter 2 for chunk server ");
					content = "2 ";

					System.out
							.println("Enter hostaname of your client(without space): ");
					content += sr.next() + " ";

					System.out.println("Enter the Ip address of your Server");
					System.out
							.println("Enter the Ip address of your server to connect ");
					content += sr.next() + " ";

					System.out.println("Enter the port to listen from server");
					content += sr.next() + " ";

					System.out
							.println("Enter the backup Chunk server ip address, if any other wise enter zero ");
					content += sr.next() + " ";

					System.out
							.println("Enter the port for backup chunk server to listen from server , if any other wise enter zero ");
					content += sr.next() + " ";

					System.out.println("check data: ");

					System.out.println(content);
					this.openFileStreams();
					this.appendTofile(content);

				}
				file.createNewFile();
			}
		} catch (IOException ioe) {
			System.out.println("Exception occurred:");
			ioe.printStackTrace();
		}
	}

	public void openFileStreams() {
		try {
			fw = new FileWriter(file, true);

			bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void appendTofile(String data) {

		pw.println(data);
		pw.close();

	}

	public String readFromFile() {
		content = "";
		try {

			sr = new Scanner(file);

			content = sr.nextLine();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sr.close();

		return content;

	}

	public void closeFile() {
		pw.close();

	}

}
