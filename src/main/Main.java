package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import main.xml.XmlElement;
import main.xml.XmlProcessor;

public class Main {

	/**
	 * Defines main entry point
	 * @param args Which action to perform on the data
	 */
	private static void writeLines(List<String> lines) {
		try {
			System.out.println("[INFO] Saving to results.txt...");
			File out = new File("results.txt");
			FileOutputStream fos = new FileOutputStream(out);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			for(String line:lines) {
				bw.write(line);
				bw.newLine();
			}
			bw.close();
			System.out.println("[INFO] Done.");
		} catch(Exception e) {
			System.out.print("[ERROR] A system error has occured. Printing stack trace and exiting...");
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		if(args.length==0) {
			System.out.println("----+ Begin XmlProcessor Help Page +----");
			System.out.println("----> Usage: java -jar XML.jar <file> [args]");
			System.out.println("----> Arguments Available:");
			System.out.println("----> demo               ==== run demonstration program...");
			System.out.println("----> listCapacitors     ==== list capacitors in document...");
			System.out.println("----> getLength <signal> ==== get total signal length given a signal name...");
			System.out.println("----> getArea <signal>   ==== get total signal polygon area given a signal name...");
			System.out.println("----> Note: output may be very large, and you will have the option to save it to a text file!");
			System.out.println("----+ End XmlProcessor Help Page +----");
		} else {
			File f = new File(args[0]);
			if(!f.exists()) {
				System.out.println("[ERROR] Invalid file path!");
				return;
			}
			DataAnalyzer da = new DataAnalyzer(new XmlProcessor(f).run());
			switch(args.length) {
			case 2:
				if(args[1].equals("listCapacitors")) {
					System.out.println("[INFO] Outputting all capacitors...");
					da.printCapacitors();
					System.out.println("[INFO] Would you like to save the output to a text file? The file will be in the same directory as this JAR and will have the name"
							+ "results.txt <yes/no>");
					Scanner sc = new Scanner(System.in);
					if(sc.nextLine().equals("yes")) {
						List<String> lines = new ArrayList<String>();
						for(XmlElement e:da.getCapacitors()) {
							String name = "-> Name: "+e.getParam("name")+", ";
							String value = "-> Value: "+e.getParam("value")+", ";
							String packageSize = "-> Package: "+e.getParam("package")+", ";
							String xy = "-> X,Y coordinates: ("+e.getParam("x")+","+e.getParam("y")+"), ";
							lines.add(name+value+packageSize+xy);
						}
						writeLines(lines);
					} else {
						System.out.println("[INFO] Not saving output. Exiting program...");
					}
					sc.close();
					return;
				} else if(args[1].equals("demo")) {
					System.out.println("[INFO] Running demo program.");
					
					String demoSignal1 = "DDR3_A1";
					String demoSignal2 = "VSS";
					
					System.out.println("[INFO] Outputting all capacitors...");
					da.printCapacitors();
					System.out.println("[INFO] Done.");
					System.out.println("[INFO] Outputting length of signal "+demoSignal1+"...");
					System.out.println(da.getLengthOfSignal(demoSignal1)+"mm");
					System.out.println("[INFO] Done.");
					System.out.println("[INFO] Outputting area of signal "+demoSignal2+" polygon...");
					System.out.println(da.getTotalPolygonArea(demoSignal2)+" square mm");
					System.out.println("[INFO] Done.");
					return;
				}
			case 3:
				if(args[1].equals("getLength")) {
					System.out.println("[INFO] Outputting length of signal "+args[2]+"...");
					System.out.println(da.getLengthOfSignal(args[2])+"mm");
					System.out.println("[INFO] Done.");
					return;
				} else if(args[1].equals("getArea")) {
					System.out.println("[INFO] Outputting area of signal "+args[2]+" polygon...");
					System.out.println(da.getTotalPolygonArea(args[2])+" square mm");
					System.out.println("[INFO] Done.");
					return;
				}
			default:
				System.out.println("[ERROR] Invalid arguments! Pass 0 arguments to view help page!");
				break;
			}
		}
	}
}
