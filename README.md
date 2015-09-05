How to document for fault tolerance module for distributed operating system project.
//Developed by : Pardeep Kumar for ICOM6006 UPRM
// website: http://ece.uprm.edu/~s142535/#/
Fault tolerance:

for live video demonstration of the project:

https://drive.google.com/file/d/0B6841f9RPpuSUnlkVzkxblF5QVE/view?usp=sharing
https://drive.google.com/file/d/0B6841f9RPpuSWC16cjh2OVFDVU0/view?usp=sharing


Fault tolerance is the property that enables a system to continue operating properly in the event of the failure of (or one or more faults within) some of its components. If its operating quality decreases at all, the decrease is proportional to the severity of the failure, as compared to a naively designed system in which even a small failure can cause total breakdown. Fault tolerance is particularly sought after in high-availability or life-critical systems.
A fault-tolerant design enables a system to continue its intended operation, possibly at a reduced level, rather than failing completely, when some part of the system fails. The term is most commonly used to describe computer systems designed to continue more or less fully operational with, perhaps, a reduction in throughput or an increase in response time in the event of some partial failure. That is, the system as a whole is not stopped due to problems either in the hardware or the software. An example in another field is a motor vehicle designed so it will continue to be drivable if one of the tires is punctured. A structure is able to retain its integrity in the presence of damage due to causes such as fatigue, corrosion, manufacturing flaws, or impact.
Within the scope of an individual system, fault tolerance can be achieved by anticipating exceptional conditions and building the system to cope with them, and, in general, aiming for self-stabilization so that the system converges towards an error-free state. However, if the consequences of a system failure are catastrophic, or the cost of making it sufficiently reliable is very high, a better solution may be to use some form of duplication. In any case, if the consequence of a system failure is so catastrophic, the system must be able to use reversion to fall back to a safe mode. This is similar to roll-back recovery but can be a human action if humans are present in the loop.
Model: Heartbeat Algorithm
In computer clusters, heartbeat network is a private network which is shared only by the cluster nodes, and is not accessible from outside the cluster. It is used by cluster nodes in order to monitor each node's status and communicate with each other.
The heartbeat method uses the FIFO nature of the signals sent across the network. By making sure that all messages have been received, the system ensures that events can be properly ordered.
In this communications protocol every node sends back a message in a given interval, say delta, in effect confirming that it is alive and has a heartbeat. These messages are viewed as control messages that help determine that the network includes no delayed messages. A receiver node called a "sink", maintains an ordered list of the received messages. Once a message with a timestamp later than the given marked time is received from every node, the system determines that all messages have been received, since the FIFO property ensures that the messages are ordered.
In general, it is difficult to select a delta (heartbeat) that is optimal for all applications. If delta is too small, it requires too much overhead and if it is large it results in performance degradation as everything waits for the next heartbeat signal.

Process Explanation:
We have developed a fault tolerance module which will provide a Server and Client based interaction where clients will send a message to their respective servers at fixed heartbeat specified by their servers.
The overall idea of the module is very simple but complexity lies in the foundation i.e. developing basic communication modules for client and server and synchronizing the client with different heartbeat. Many other challenges we have faced while developing this module such as providing secure communication using Public and Private key encryption, authenticating server and checking status of every client in a given heartbeat at the server side and updating the logs.
Implementing reliable client-server communication: 
Implementing heartbeat algorithm: This algorithm will provide server and its clients a way to notify each other of its liveliness.

Server backup utility: If server goes down then dynamically transferring the load to the backup server with minimum latency.

Configurable: Provided server the ability to select the time of heartbeat for its clients. Provided each node to add its backup information.

Security: Secure message passing between client and server. Generating Public and Private keys for each node 

 Server will respond all the ChunkServers and backup ChunkServers at the same time.
More features:
Server will write logs for all the activities:
-for all the authorized and unauthorized clients connect to the server (authorised_client.txt and unauthorized_log.txt)
-for all the connected clients their status will be kept in client_status.txt. 
-for all the activities happening on server side will be kept in client_log.txt. 
*One can use these logs in any high level language or shell script to take decision in other programs.


How to run:
Install java JDK:
on CentOS:
yum install java-1.7.0-openjdk
on Ubuntu
apt-get install openjdk-7-jre

Connect to your machine using putty or if you have graphical access then simpl run below command on your command line:

1. Copy the fault-tolerance folder in your computer.
	-this folder has two sub folders named “Server” & “Client”.

2.  Open terminal in your machine or run the below commands from putty terminal:
- First if you are running server on this machine, then point to the server folder from your terminal using cd command.

- For running server, type “ java –jar  server-fault-tol-auto-config-final5.0.jar”  & press Enter, It will ask you to configure your server i.e. providing the details of your client IP and the port    number to use to run the socket server.

- For running client, point to the client folder using cd command and type ““ java –jar  client-fault-tol-auto-config-final3.0.jar” & press Enter.

-It will ask you to configure your client i.e providing the IP address and port of your server to connect to.
-The configuration needs to be done only the first time and the same configuration will be used for running the server/client next time. Options will be provided on the terminal by the java program.



-The server will ask you details about:
	Ip address of your server
	listening port for fault policy
	backup server ip address
	listening port for backup server fault policy
	heartbeat for client

-The Client will ask you details about:
	Ip address of your Server
	port to listen from server
	backup Chunk server ip address
	port for backup chunk server to listen from server
Note: The backup utility for Chunk Server have not been added but one can achieve the same result by creating another server on the client itself and connecting the backup chunk server to its main chunk server by running the same server client interaction between them.
* For server, you are allowed to add more clients IP directly into the config_server.txt file before “-1” as server uploads the client list, for validation of client, dynamically every time a client tries to connect to the server. If you have to change the port number or details of the backup server you have to restart your server again after making changes in the config_server.txt file.
* For client, if you make any changes in client_config.txt you have to run your client again using command shown above.
Note: Always start your server before running client.
Once running successfully you will be able to see below files in you server/client directory:
authorised_client.txt: It will have the details of the authorized clients.
client_log.txt: This file write the log of activities of all the clients connected on the server.
client_status.txt: This file provides the status of clients i.e. either they are Active or InActive and there timestamp.
config_client.txt: This file will get created if you are running client and will have details of your running client.
config_server.txt: This file will get generated if you are running server and will have the details of you running server.
 Public and Private keys: This will be generated by the program and will be saved in the same directory for every client or server.

Technical Details:
We took ten machines and classified them as follow:
Nameserver01 => running server module with backup at Nameserver02
Chunkserver011 => client module pointing to Nameserver01
Chunkserver012 => client module pointing to Name+server01
ChunkServer013=> client module pointing to Nameserver01
ChunkServer014=> client module pointing to Nameserver01

Nameserver02=> running server module with backup at Nameserver01
Chunkserver021=> client module pointing to Nameserver02
Chunkserver022=> client module pointing to Nameserver02
ChunkServer023=> client module pointing to Nameserver02
ChunkServer024=> client module pointing to Nameserver02

Nameserver01 and all its Chunkservers i.e. Chunkserver011, Chunkserver012, Chunkserver013 and Chunkserver014 are running Ubuntu OS and have java installed.
Whereas Nameserver02 and all its Chunkservers i.e. . Chunkserver021, Chunkserver022, Chunkserver023 and Chunkserver024 are running Cent OS and have java installed.
The backup configuration has been used as follow:
Nameserver01 has back on Nameserver02 i.e. if it goes down then traffic or client connected to Nameserver01 will be redirected to Nameserver02.
Nameserver02 has back on Nameserver01 i.e. if it goes down then traffic or client connected to Nameserver02 will be redirected to Nameserver01, just in case if Nameserver01 come back up again.
Both the servers have specified different heartbeat to their client, if any of the server goes down the redirection of client to another server will take the new heartbeat of the server to which it is being redirected and will share and receive its Public Key again with new server.
Nameservers will receive and use the Public Key of client to encrypt their header information and will send the client encrypted header along with its own Public Key. Client responsibility is to receive the header decrypt it and take Public Key of the server to encrypt messages before sending it to the server.



