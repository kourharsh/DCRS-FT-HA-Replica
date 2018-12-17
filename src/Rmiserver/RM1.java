package Rmiserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

public class RM1 {
	
	static String studentId="";
	static String advisorId="";
	static String courseId="";
	static String semester="";
	static String sequence="";
	static String capacity="";
	static String newCourseID = "";
	static String replicaId = "";
	static int rm3Count = 0;
	static int[] vector = {0,0,0,0};
	static int[] vectorc = {0,0,0,0};
	static int index11=0;
	static String array[]= new String[70];
	static int lseq =0;
	static boolean flag1 = false;
	static String FaultyRM = null;
	
	public RM1() {
		
	}

	public static void udp(String message) {
		DatagramSocket aSocket = null;
		System.out.println("message--"+message);
		try {
			aSocket = new DatagramSocket();
			byte[] m = message.getBytes();
			System.out.println("m----"+m);
			System.out.println("message----"+m.toString());
			InetAddress aHost = InetAddress.getByName("230.1.1.5");
			DatagramPacket request = new DatagramPacket(m, m.length, aHost, 1314);
			aSocket.send(request);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void rmUdp(String message) {
		DatagramSocket aSocket = null;
		System.out.println("message--"+message);
		try {
			aSocket = new DatagramSocket();
			byte[] m = message.getBytes();
			System.out.println("m----"+m);
			System.out.println("message----"+m.toString());
			InetAddress aHost = InetAddress.getByName("172.168.1.105");
			DatagramPacket request = new DatagramPacket(m, m.length, aHost, 5555);
			aSocket.send(request);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void main(String args[]) throws InvalidName, AdapterInactive, ServantNotActive, WrongPolicy, org.omg.CosNaming.NamingContextPackage.InvalidName, NotFound, CannotProceed, SecurityException, IOException {
		CompServer.startCompServer();				    
		SoenServer.startSoenServer();		    
		InseServer.startInseServer();	
		Functions imp = new Functions();
		
		MulticastSocket aSocket = null;
		try {

			aSocket = new MulticastSocket(1313);

			aSocket.joinGroup(InetAddress.getByName("230.1.1.5"));

			
			System.out.println("Server Started............");

			while (true) {
				byte[] buffer = new byte[1000];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				System.out.println("abcd---"+request.getData().toString());
				String stringdata=new String (request.getData());
				System.out.println(stringdata);
				String indexi[] = stringdata.split(",");
				System.out.println("index length--" + indexi.length);
				
				
				if(indexi[0].trim().equals("faultybug")) {
					replicaId =indexi[1].trim();
					
					if(replicaId.equals("RM1")) {
						vector[0]=vector[0]+1;
					}
					
					else if(replicaId.equals("RM2")) {
						vector[1]=vector[1]+1;
					}
					else if(replicaId.equals("RM3")) {
						vector[2]=vector[2]+1;
					}
					
					else if(replicaId.equals("RM4")) {
						vector[3]=vector[3]+1;
					}
					
					if(vector[0]==3) {
						FaultyRM = "RM1";
					}else if(vector[1]==3) {
						FaultyRM = "RM2";
					}else if(vector[2]==3) {
						FaultyRM = "RM3";
					}else if(vector[3]==3) {
						FaultyRM = "RM4";
					}
					
					if(FaultyRM!=null) {
						System.out.println(FaultyRM+" "+"has a software bug!");
					}
					
				}
				else if(indexi[0].trim().equals("faultycrash")) {
					
					replicaId =indexi[1].trim();
					
					if(replicaId.equals("RM1")) {
						vectorc[0]=vectorc[0]+1;
					}
					
					else if(replicaId.equals("RM2")) {
						vectorc[1]=vectorc[1]+1;
					}
					else if(replicaId.equals("RM3")) {
						vectorc[2]=vectorc[2]+1;
					}
					
					else if(replicaId.equals("RM4")) {
						vectorc[3]=vectorc[3]+1;
					}
					
					if(vectorc[0]==3) {
						FaultyRM = "RM1";
					}else if(vectorc[1]==3) {
						FaultyRM = "RM2";
					}else if(vectorc[2]==3) {
						FaultyRM = "RM3";
					}else if(vectorc[3]==3) {
						FaultyRM = "RM4";
					}
					
					if(FaultyRM!=null) {
						System.out.println(FaultyRM+" "+"is not working and should be replaced!");
						if(FaultyRM=="RM1") {
							System.out.println("Recovery Phase for"+" "+FaultyRM+" "+"is on!");
							//Read array and perform!
						}
					}
					
				}else {
				
				// Handling array		
				String c = indexi[indexi.length-1].trim();
				int cseq = Integer.parseInt(c);
				System.out.println("Current sequence is:"+cseq);
				System.out.println("stringdata:"+stringdata);	
				if(array[cseq]==null) {
				array[cseq] = stringdata; 
				System.out.println("Value for current sequence in array is:" +array[cseq]);
				}else {
					System.out.println("Input request is with duplicate sequence number, thus discarded!");
				}	
				for(int i=lseq;i<=cseq;i++) {
					if(array[i]==null) {
						flag1=false;
						break;
					}else {
						flag1=true;
					}
					System.out.println("flag:"+flag1);
				
				if(flag1) {
					String index[] = array[i].split(",");
					System.out.println("index length--" + index.length);
					
				if(index[0].trim().equals("addCourse")) {
					courseId = index[1].trim();
					semester = index[2].trim();
					capacity = index[3].trim();
					sequence = index[4].trim();
					int seq = Integer.parseInt(sequence);
					int cap = Integer.parseInt(capacity.trim());
					boolean add = imp.addCourse(courseId, semester, cap);
					String str = String.valueOf(add);
					udp(str+":RM1");					
					
				}
				else if(index[0].trim().equals("removeCourse")) {
					courseId = index[1].trim();
					semester = index[2].trim();
					sequence = index[3].trim();
					boolean rem = imp.removeCourse(courseId, semester);
					String str = String.valueOf(rem);
					udp(str+":RM1");
				}
				else if(index[0].trim().equals("listCourseAvailability")) {
					semester = index[1].trim();
					sequence = index[2].trim();
					String str = imp.listCourseAvailability(semester);
					udp(str+":RM1");
				}
				else if(index[0].trim().equals("enrolCourse")) {
					studentId = index[1].trim();
					courseId = index[2].trim();
					semester = index[3].trim();
					sequence = index[4].trim();
					boolean enrol = imp.enrolCourse(studentId, courseId, semester);
					String str = String.valueOf(enrol);
					udp(str+":RM1");
				}
				else if(index[0].trim().equals("dropCourse")) {
					studentId = index[1].trim();
					courseId = index[2].trim();
					sequence = index[3].trim();
					boolean drop = imp.dropCourse(studentId, courseId);
					String str = String.valueOf(drop);
					udp(str+":RM1");
				}
				else if(index[0].trim().equals("getClassSchedule")) {
					studentId = index[1].trim();
					sequence = index[2].trim();
					String str = imp.getClassSchedule(studentId);
					udp(str+":RM1");
				}
				else if(index[0].trim().equals("swapCourse")) {
					studentId = index[1].trim();
					courseId = index[3].trim();
					newCourseID = index[2].trim();
					sequence = index[4].trim();
					boolean swap = imp.swapCourse(studentId, newCourseID, courseId);
					String str = String.valueOf(swap);
					udp(str+":RM1");
				}
				lseq =lseq+1 ;
				}
				}
				}
		}
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
		
	
	}
}