package Rmiserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import Rmiserver.Functions;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import Rmiclient.AdvisorClient;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import DCRSApp.corbaInterface;
import DCRSApp.corbaInterfaceHelper;

public class CompServer {
	//	static Functions fun;
		static String Operation;
		HashMap<String, HashMap<String, Integer>> hashData;
		static HashMap<String, Integer> hashin;
		static HashMap<String, HashMap<String, Integer>> hashComp= new HashMap<String, HashMap<String, Integer>>();
		static HashMap<String, Integer> hash1 = new HashMap<String, Integer>();
		static Logger logger = Logger.getLogger(AdvisorClient.class.getName());
		static private FileHandler fileTxt;
	    static private SimpleFormatter formatterTxt;
		static String DataReceived = null;
		static String DataReceivedI;
		static String DataReceivedS;
		static String Sem;
		static String semReceived;
		static HashMap<String,Integer> CompSem;
		static HashMap<String, Integer> fetchValuesC= new HashMap<String, Integer>();
	   public static void startCompServer() throws SecurityException, IOException {
			try {
				
			//	fileTxt = new FileHandler("C:\\Users\\WEB\\eclipse-workspace\\CORBA\\Log files\\CompServer.log");
				fileTxt = new FileHandler("G:\\workspace\\Logfile\\CompServer.log");
				formatterTxt = new SimpleFormatter();
			    fileTxt.setFormatter(formatterTxt);
			    logger.addHandler(fileTxt);
		        inputValues("FALL");
		        inputValues("SUMMER");
		        inputValues("WINTER");
		        List<HashMap<String,HashMap<String,Integer>>> d = Arrays.asList(hashComp);
				System.out.println(d);
				Runnable task = () -> {
					receive();
				};
				Thread thread = new Thread(task);
				thread.start();
		//	System.out.println("hey bro!");

		//		CommonServer.startRegistry(RMIPort);
			 /*   Properties props = new Properties();
			    props.put("org.omg.CORBA.ORBInitialPort", "1050");
		        props.put("org.omg.CORBA.ORBInitialHost", "MyHost");*/
			    /*ORB orb = ORB.init(args, null); // create and initialize the ORB
			 // get reference to rootpoa & activate the POAManager
			    POA rootpoa = (POA)orb.resolve_initial_references("RootPOA");
			    rootpoa.the_POAManager().activate();
			 // create servant and register it with the ORB
			    Functions DcrsImpl = new Functions();
			    Functions.setORB(orb);
			 // get object reference from the servant
			    org.omg.CORBA.Object ref = rootpoa.servant_to_reference(DcrsImpl);
			 // and cast the reference to a CORBA reference
			    corbaInterface href = corbaInterfaceHelper.narrow(ref);
			 // get the root naming context
			 // NameService invokes the transient name service
			    org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			// Use NamingContextExt, which is part of the
			// Interoperable Naming Service (INS) specification.
			    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			    String name = "CompServer";
			    NameComponent path[] = ncRef.to_name( name );
			    ncRef.rebind(path, href);
			    System.out.println("CompServer ready and waiting ...");
			    orb.run();*/
		//		Functions rmiComp = new Functions();
		//        Naming.rebind("rmi://localhost:" + RMIPort + "/rmiComp", rmiComp);
		//        System.out.println("RmiComp Server registered"); 
		        logger.info("******RmiComp Server registered******");
/*		        inputValues("FALL");
		        inputValues("SUMMER");
		        inputValues("WINTER");*/
			}
			catch(Exception e) {
				System.out.println("Exception in RmiComp: " + e);
			}
		}
	   	   
	   public static String listCourse1(String semester){
		   	logger.info("******listCourse1:COMP SERVER******");
		   	String result = null;
		   	Operation = "L,";
		   	Sem=Operation.concat(semester);	   
			if(hashComp.containsKey(semester)) {
				CompSem = hashComp.get(semester);
			}
			List<HashMap<String,Integer>> g = Arrays.asList(CompSem);
			System.out.println(g);
			result = "COMP" +CompSem.toString();
			logger.info("******listCourse1:Sending message to SOEN SERVER******");
			sendMessage(5555);
			DataReceivedS = DataReceived;
			result =result+" "+"SOEN"+DataReceivedS.trim();
		//	result = result.concat(DataReceivedS);
			System.out.println(DataReceivedS);
			logger.info("******listCourse1:Sending message to INSE SERVER******");
			sendMessage(7777);
			DataReceivedI = DataReceived;
			result =result+" "+"INSE"+DataReceivedI.trim();
	//		result = result.concat(DataReceivedI);
			System.out.println(DataReceivedI);

			return result;
   }
	   
	   public static void inputValues(String term) {
		   
			if(term == "FALL") {
				logger.info("******Adding Fall Courses in Comp Hashmap******");
				hashin = new HashMap<String, Integer>();
				hashin.put("COMP6431", 10);
				hashin.put("COMP6461", 10);
				hashin.put("COMP6451", 10);
				hashin.put("COMP6481", 10);	
				if(!hashComp.containsKey(term)) {
					hashComp.put(term, hashin);
				}
			}

			if(term == "SUMMER") {
				logger.info("******Adding Summer Courses in Comp Hashmap******");
				hashin = new HashMap<String, Integer>();
				hashin.put("COMP6592", 10);
				hashin.put("COMP6593", 10);
				hashin.put("COMP6481", 10);
				hashin.put("COMP6595", 10);
				if(!hashComp.containsKey(term)) {
					hashComp.put(term, hashin);
				}
			}
			if(term == "WINTER") {
				logger.info("******Adding Winter Courses in Comp Hashmap******");
				hashin = new HashMap<String, Integer>();
				hashin.put("COMP6641", 10);
				hashin.put("COMP6651", 10);
				hashin.put("COMP6481", 10);
				hashin.put("COMP6621", 10);
				if(!hashComp.containsKey(term)) {
					hashComp.put(term, hashin);
				}
			}
		}	
	   
	   private static void sendMessage(int serverPort) {
			DatagramSocket aSocket = null;
			try {
				aSocket = new DatagramSocket();
				byte[] message = Sem.getBytes();
				InetAddress aHost = InetAddress.getByName("localhost");
				System.out.println("sem length is:" + Sem.length());
				DatagramPacket request = new DatagramPacket(message, Sem.length(), aHost, serverPort);
				aSocket.send(request);
				System.out.println("Request message sent from the client to server with port number " + serverPort + " is: "
						+ new String(request.getData()));
				byte[] buffer = new byte[1000];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(reply);
				DataReceived = null;
				 DataReceived = new String(reply.getData()).trim();
					System.out.println("Reply received from the server with port number " + serverPort + " to COMP server is: "
								+ DataReceived);
			} catch (SocketException e) {
				System.out.println("Socket: " + e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("IO: " + e.getMessage());
			} finally {
				if (aSocket != null)
					aSocket.close();
			}
		}
	   private static String FetchHashmap(String Semester) {
		   logger.info("FetchHashmap:Fetch Comp Server Data for semester"+" "+Semester);
		   if(hashComp.containsKey(Semester)) {
		   HashMap<String,Integer> Compsem = hashComp.get(Semester);
		   List<HashMap<String,Integer>> i = Arrays.asList(Compsem);
			String	hash = Compsem.toString();
		    return hash;}
		   else {
			   return null;
		   }
	   }
	   
	   private static void receive() {
			DatagramSocket aSocket = null;
			try {
				boolean flag=false;
				String	rep = null;
				aSocket = new DatagramSocket(8888);
				System.out.println("Comp Server 8888 Started............");
				while (true) {
					byte[] buffer = new byte[10000];
					DatagramPacket request = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(request);
					semReceived = new String(request.getData());
					String func=semReceived.split(",")[0];
					func=func.trim();
					if(func.equals("E")) {
						System.out.println("Semester reseived is :" + " " + semReceived);
						String []parm=semReceived.split(",",4);
						String studentid=parm[1];
						studentid=studentid.trim();
						String courseID =parm[2];
						courseID=courseID.trim();
					    String semester =parm[3];
						semester=semester.trim();	
						String d=checkCourse(semester,courseID);
						System.out.println("d is"+d);
						if (d.equals("pass")) {
							System.out.println("rep here is"+rep);
						flag = Functions.Student(studentid,courseID,semester);
						if (flag){
							rep ="pass";
						}else{
							rep ="fail";
							}
						}
						else {
							rep ="fail";
						}
						System.out.println("rep here is--"+rep);
						if(rep.equals("pass")) {
						Capacity(semester,courseID,"ENROLL");}
					}else if(func.equals("L")){
					System.out.println("Semester received is :" + " " + semReceived);
					String []parm=semReceived.split(",",2);
					String Semester=parm[1];
					Semester=Semester.trim();
					rep = FetchHashmap(Semester.trim());
					}else if(func.equals("D")) {
						System.out.println("Semester received is :" + " " + semReceived);
						String []parm=semReceived.split(",",3);
						String courseID =parm[1];
						courseID=courseID.trim();
						System.out.println("courseID received is :" + " " + courseID);
					    String semester =parm[2];
						semester=semester.trim();
						System.out.println("semester received is :" + " " + semester);
						Capacity(semester,courseID,"DROP");
						rep="pass";
						}
					buffer = rep.getBytes();
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length, request.getAddress(),
							request.getPort());
					aSocket.send(reply);
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

	public static void Capacity(String semester,String courseID,String Operation) {
		logger.info("Capacity:Change capacity for"+" "+courseID+" "+"in semester"+" "+semester);
		fetchValuesC = hashComp.get(semester);
		int capacity = fetchValuesC.get(courseID);
		if(Operation.equals("ENROLL")) {
			capacity = capacity-1;
			logger.info("Capacity:Capacity decreased by 1");	
		}else if(Operation.equals("DROP")) {
			capacity = capacity+1;
			logger.info("Capacity:Capacity increased by 1");
		}
		fetchValuesC.put(courseID, capacity);
		hashComp.put(semester, fetchValuesC);
		logger.info("Capacity:Comp Hashmap updated with new capacity of course"+" "+courseID);
		List<HashMap<String,HashMap<String,Integer>>> g = Arrays.asList(hashComp);
		System.out.println(g);
	}
	
	public static String checkCourse(String semester,String courseId) {
		System.out.println("semester"+semester+"---------"+"courseId"+courseId);
		logger.info("checkCourse: Checking availabilty of course"+" "+courseId+"in semester"+" "+semester);
		String result="fail";
		fetchValuesC = hashComp.get(semester);
		if(fetchValuesC.containsKey(courseId)) {
			int capacity = fetchValuesC.get(courseId);
			if(capacity>0) {
				logger.info("checkCourse:course"+" "+courseId+"is available in semester"+" "+semester);
				System.out.println("Course is ok!");
				result="pass";}else {
					System.out.println(courseId +" "+"does not have an availability!");
					logger.info("checkCourse:course"+" "+courseId+"is not available in semester"+" "+semester);
				}
			}else {
				System.out.println(courseId +" "+" is not a valid course!");
				logger.info("checkCourse:course"+" "+courseId+"is not a valid course in semester"+" "+semester);
			}
		return result;
	}

	   
	public static String CourseEnrol(String studentID, String courseID, String semester){
		logger.info("CourseEnrol: Enrolling course"+" "+courseID+"in semester"+" "+semester+" "+"for student"+" "+studentID);
			boolean flag = false;
			String Result=null;
		   String CourseDept = courseID.substring(0,4).toUpperCase();
		   if(CourseDept.equals("COMP")) {   
			   fetchValuesC = hashComp.get(semester);
			   System.out.println(hashComp);
			   System.out.println(fetchValuesC);
			   System.out.println(courseID);
				if(fetchValuesC.containsKey(courseID)) {
					int capacity = fetchValuesC.get(courseID);
					if(capacity>0) {
						flag = Functions.Student(studentID,courseID,semester);
						if(flag) {
							Result="pass";
						}else {
							Result="fail";
						}
						if(Result.equals("pass")) {
							Capacity(semester,courseID,"ENROLL");
							logger.info("CourseEnrol:"+" "+studentID+" "+"enrolled in course"+" "+courseID);
							System.out.println("Student enrolled!");
							return Result;
						}else {
							logger.info("CourseEnrol:"+" "+studentID+" "+"could not be enrolled in course"+" "+courseID);
							System.out.println("Student enrolling failed!");
							return Result;
						}
					}else {
						logger.info("CourseEnrol:"+" "+courseID +" "+"does not have an availability!");
						System.out.println(courseID +" "+"does not have an availability!");
						return "fail";
					}
				}else {
					logger.info("CourseEnrol:"+" "+courseID +" "+"is not a valid course!");
					System.out.println(courseID +" "+" is not a valid course!");
					return "fail";
				}	   
		   }else {
			   Operation = "E,";
			   Sem=Operation.concat(studentID).concat(",").concat(courseID).concat(",").concat(semester);
			   System.out.println(Sem);
			   if(CourseDept.equals("INSE")) {
				   logger.info("CourseEnrol:"+"Sending message to INSE server");
					sendMessage(7777);
					System.out.println("IN Runnable task2");
					System.out.println("DataReceived is :"+DataReceived);
				System.out.println("Below TASK2");
		   }else if(CourseDept.equals("SOEN")) {
			   logger.info("CourseEnrol:"+"Sending message to SOEN server");
					sendMessage(5555);  
		   }
			   if (DataReceived.equals("pass")){
				   logger.info("CourseEnrol:"+" "+studentID+" "+"enrolled in course"+" "+courseID);
				   System.out.println("In Data received!");
					System.out.println("Student enrolled!");
					return "pass";
				}else {
					System.out.println("In else of Data received!");
					return "fail";
				}   
		   }
	} //end of CourseEnroll

	public static String DropCourse(String courseID, String semester) {
		logger.info("DropCourse: Drop course"+" "+courseID+"in semester"+" "+semester);
		String CourseDept = courseID.substring(0,4).toUpperCase();
		if(CourseDept.equals("COMP")) {
			Capacity(semester,courseID,"DROP");
			DataReceived = "pass";
		}else if(CourseDept.equals("INSE")) {
			 Operation = "D,";
			  Sem=Operation.concat(courseID).concat(",").concat(semester);
			  logger.info("DropCourse:"+"Sending message to INSE server");
			  sendMessage(7777);
			  System.out.println("DataReceived is :"+DataReceived);
		}else if(CourseDept.equals("SOEN")) {
			 Operation = "D,";
			  Sem=Operation.concat(courseID).concat(",").concat(semester);
			  logger.info("DropCourse:"+"Sending message to SOEN server");
			  sendMessage(5555);
			  System.out.println("DataReceived is :"+DataReceived);
		}
		if (DataReceived.equals("pass")){
				logger.info("DropCourse:"+" "+courseID+"dropped in semester"+" "+semester);
			   System.out.println("In Data received!");
				return "pass";
			}else {
				logger.info("DropCourse:"+" "+courseID+"could not be dropped in semester"+" "+semester);
				System.out.println("In else of Data received!");
				return "fail";
			}   
	}
	
	} //end of CompServer

   


