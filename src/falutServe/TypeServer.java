package falutServe;

import java.util.ArrayList;


/**
 * @author Pardeep 
 * 
 */

public class TypeServer  {
	
	Server s ;
	Client c ;
	ArrayList<String> fta = new ArrayList<String>();
	ArrayList<String> cfta = new ArrayList<String>();
	MachineType mt;
	private String MachineType = "";
	private int machineValue;
	private String ipAddr, port, heartBeat;
	private String ipAddr_bkp, port_bkp;
	private ArrayList<String> vIpAddr = new ArrayList<String>();
	boolean needClient = true;
	TypeServer(String MachineType, MachineType obj,int machineValue,ArrayList<String> fta ){
		this.MachineType = MachineType; 
		this.mt = obj;
		this.machineValue=machineValue;
		this.fta = fta;
		
	}
	protected void runSpecfics(){
		
		if(this.machineValue == 0){
			System.out.println("Enter the Ip address of your server ");
			//ipAddr = mt.in.next();
			ipAddr = fta.get(2);
			System.out.println(ipAddr);
			System.out.println("Enter the listening port for fault ploicy");
			//port = mt.in.next();
			port = fta.get(3);
			System.out.println(port);
			System.out.println("Enter the backup server ip address, if any other wise enter zero ");
			//ipAddr_bkp = mt.in.next();
			ipAddr_bkp = fta.get(4);
			System.out.println(ipAddr_bkp);
			System.out.println("Enter the listening port for fault ploicy , if any other wise enter zero ");
			//port_bkp = mt.in.next();
			port_bkp = fta.get(5);
			System.out.println(port_bkp);
			
			System.out.println("Enter the heartbeat for client ");
			this.setHeartBeat(fta.get(6));//mt.in.next());
			System.out.println(fta.get(6));
			
			System.out.println();System.out.println();
			int i = 1;
			System.out.println("please check if everything is good "+ ipAddr + " " + port+ " backup at " + ipAddr_bkp + " " + port_bkp );
			do{
				//System.out.println("Enter the validated client ip addresses(enter -1 to finish)");
				//System.out.println("Enter Ip for Client "+ i);
				String inp = fta.get(6+i);//mt.in.next();
					if(inp.contains("-1")){
						needClient=false;
					}
					else{
					//inp = mt.in.next();
					
					vIpAddr.add(inp);
					i++;
					}
			}while(needClient);
			
			System.out.println("List of validated clients are : ");
			for(i=0;i< vIpAddr.size();i++){
				System.out.println(vIpAddr.get(i));
			}
			
			
		}
		
		
		if(this.machineValue == 1){
			System.out.println("Enter the Ip address of your Server");
			ipAddr =fta.get(2);// mt.in.next();
			System.out.println(ipAddr);
			
			System.out.println("Enter the port to listen from server");
			port  =fta.get(3);//= mt.in.next();
			System.out.println(port);
			
			System.out.println("Enter the backup Chunk server ip address, if any other wise enter zero ");
			ipAddr_bkp  =fta.get(4);// = mt.in.next();
			System.out.println(ipAddr_bkp);
			
			System.out.println("Enter the port for backup chunk server to listen from server , if any other wise enter zero ");
			port_bkp=  fta.get(5);// = mt.in.next();

			
		}		
		System.out.println("please check if everything is good "+ ipAddr + " " + port+ " backup at " + ipAddr_bkp + " " + port_bkp );
		runMachineProcess(this.machineValue);
	}
	private void runMachineProcess(int mv){
		
		System.out.println("\n \n lets run some process speciic to your machine");
		if(mv == 0){
			
			s = new Server(ipAddr, port, this);
			s.serve();
			
		}
		if(mv == 1 ){
			c = new Client(ipAddr, port, this);
			c.serve();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
	}
	


	public String getMachineType() {
		return MachineType;
	}

	public void setMachineType(String machineType) {
		MachineType = machineType;
	}

	public int getMachineValue() {
		return machineValue;
	}

	public void setMachineValue(int machineValue) {
		this.machineValue = machineValue;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getIpAddr_bkp() {
		return ipAddr_bkp;
	}

	public void setIpAddr_bkp(String ipAddr_bkp) {
		this.ipAddr_bkp = ipAddr_bkp;
	}

	public String getPort_bkp() {
		return port_bkp;
	}

	public void setPort_bkp(String port_bkp) {
		this.port_bkp = port_bkp;
	}
	public ArrayList<String> get_vIpAddr() {
		return vIpAddr;
	}
	
	protected boolean check_vIpAddr(String cIp){
		cIp = cIp.replaceAll("/", "");
		
	
			//System.out.println("Enter the validated client ip addresses(enter -1 to finish)");
			//System.out.println("Enter Ip for Client "+ i);
			String confg_txt= mt.ft.readFromFile();
			System.out.println("refreshing client list : "+confg_txt);
			int i=0;
			for(String cd : confg_txt.split(" ")){
				cfta.add(cd);
				//System.out.println("valid clients:"+ cfta.get(i));
				i++;
			}
					//fta.get(6+i1);//mt.in.next();
			for(i=0;i< cfta.size();i++){
				//System.out.println(cfta.get(i));
				
				if(i>6 && i<(cfta.size()-1)){
				//	System.out.println("Adding clients: ");
					vIpAddr.add(cfta.get(i));
				}
				
			}
		for(int j=0;j< vIpAddr.size();j++){
			if(cIp.equals(vIpAddr.get(j))){
				System.out.println("client is valid");
				return true;
			}
		}
		System.out.println("validating client " +cIp);
		
		return false;
		
		
	}
	public String getHeartBeat() {
		return heartBeat;
	}
	public void setHeartBeat(String heartBeat) {
		this.heartBeat = heartBeat;
	}


}
