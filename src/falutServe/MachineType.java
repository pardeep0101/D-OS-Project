package falutServe;

import java.util.ArrayList;

import java.util.Scanner;

/**
 * @author Pardeep 
 * 
 */

public class MachineType {

	private static String machineType = " ";
	public static Scanner in = new Scanner(System.in);
	public static String a = "";
	public static ArrayList<String> fta = new ArrayList<String>();
	fileUtility ft;
	static TypeServer typeserver;
	MachineType( String file){
		ft = new fileUtility(file);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MachineType mt = new MachineType("config_server.txt");
		//MachineType mt = new MachineType("config_client.txt");
		mt.ft.createFile();
		String confg_txt = mt.ft.readFromFile();
		System.out.println("loading configuration : "+confg_txt);
		int i=0;
		for(String cd : confg_txt.split(" ")){
			fta.add(cd);
			System.out.println(fta.get(i));
			i++;
		}
		
		
		System.out.println("Enter the machine type: \n Enter 1 for Server \n Enter 2 for chunk server ");
		//String input = in.next();
		String input = fta.get(0);
		if(input.contains("1")){
			
			System.out.println("Enter hostaname of your server(without space): ");
			a = fta.get(1);//= in.next();
			System.out.println(a);
			mt.setMachineType(a);
			typeserver = new TypeServer(machineType, mt,0,fta);
		}
		
		else if(input.contains("2")){
			System.out.println("Enter hostname of you ChunkServer (without Space): ");
			 a =  fta.get(1);
			 System.out.println(a);
			mt.setMachineType(a);
			typeserver = new TypeServer(machineType, mt,1,fta);
		}
		
		
		System.out.println("\n more specific details are required...");
		typeserver.runSpecfics();
		
		
	}
	
	private void setMachineType(String mt){
		
		if(mt.contains(" ")){
			System.out.println("is it really a machine ");
		}
		else{
			this.machineType = mt;
		}
	}
	
	private String getMachineType(){
		
		if(this.machineType.contains(" ")){
			return "0";
		}
		else {
			return this.machineType;
		}
		
	}

}
