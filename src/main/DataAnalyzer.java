package main;

import java.util.ArrayList;
import java.util.List;

import main.xml.XmlElement;

public class DataAnalyzer {

	private List<XmlElement> list = new ArrayList<XmlElement>();
	
	public DataAnalyzer(List<XmlElement> list) {
		this.list = list;
	}
	/**
	 * Returns a list of XmlElements that are capacitors.
	 * 
	 * @return List of all capacitor Xml Elements.
	 */
	public List<XmlElement> getCapacitors() {
		List<XmlElement> caps = new ArrayList<XmlElement>();
		for(XmlElement e:list) {
			if(e.getName().equals("element")) {
				String name = e.getParam("name");
				if(name==null) continue;
				if(name.charAt(0)=='C') {
					try { 
						Integer.parseInt(String.valueOf(name.charAt(1))); 
						caps.add(e);
					} catch(NumberFormatException nfe) { break; }
				}
			}
		}
		return caps;
	}
	/**
	 * Prints out all capacitors with their name, value, package size, and xy coordinates.
	 * 
	 * @param name name of the signal to calculate.
	 */
	public void printCapacitors() {
		for(XmlElement e:this.getCapacitors()) {
			String name = "-> Name: "+e.getParam("name")+", ";
			String value = "-> Value: "+e.getParam("value")+", ";
			String packageSize = "-> Package: "+e.getParam("package")+", ";
			String xy = "-> X,Y coordinates: ("+e.getParam("x")+","+e.getParam("y")+"), ";
			System.out.println(name+value+packageSize+xy);
		}
	}
	
	/**
	 * Returns the total routed length of a given signal in millimeters.
	 * 
	 * @param name Name of the signal to calculate.
	 * @return Total length of the specified signal rounded to the nearest whole number.
	 */
	public double getLengthOfSignal(String name) {
		double total=0.0;
		for(XmlElement e:list) {
			if(e.getName().equals("signal")) {
				if(e.getParam("name").equals(name)) {
					for(XmlElement child:e.getChildren()) {
						if(child.getName().equals("wire")) {
							if(child.getParam("layer").equals("19")) continue;
							try {
								double distance=0.0;
								double x1 = Double.parseDouble(child.getParam("x1"));
								double y1 = Double.parseDouble(child.getParam("y1"));
								double x2 = Double.parseDouble(child.getParam("x2"));
								double y2 = Double.parseDouble(child.getParam("y2"));
								double chord = Math.sqrt(Math.pow((x2-x1), 2)+Math.pow((y2-y1), 2));
								if(child.hasParam("curve")) {
									double theta = Math.abs(Double.parseDouble(child.getParam("curve")))*(Math.PI/180);
									double a = (chord*theta)/(2*Math.sin(theta/2));
									distance=a;
								} else {
									distance=chord;
								}
								total+=distance;
							//If error, ignore this length...
							} catch(NumberFormatException nfe) { continue; }
						}
					}
				}
			}
		}
		return Math.round(total);
	}
	
	/**
	 * Calculates an area of any linear polygon. All curves will be treated as lines!
	 * @param vertices List of vertices for the polygon
	 * @return Computed polygon area
	 */
	private double computePolygonArea(List<Vertex> vertices) {
		//Adaptation of Green's Theorem...
		double area=0.0;
		for(int i=0;i<vertices.size()-1;i++) {
			int j = (i+1)%vertices.size();
			area += vertices.get(i).getX()*vertices.get(j).getY();
			area -= vertices.get(i).getY()*vertices.get(j).getX();
		}
		return Math.abs(area);
	}
	/**
	 * Takes a copper polygon pour and calculates the area of all instances on all layers of the signal.
	 * @param name Signal name to get area of.
	 * @return Total polygon enclosed area rounded to the nearest whole number.
	 */
	public double getTotalPolygonArea(String name) {
		double area=0.0;
		for(XmlElement e:list) {
			if(e.getName().equals("signal") && e.getParam("name").equals(name)) {
				for(XmlElement polygon:e.getChildren()) {
					if(polygon.getName().equals("polygon")) {
						List<Vertex> vertices = new ArrayList<Vertex>();
						for(XmlElement child:polygon.getChildren()) {
							if(child.getName().equals("vertex")) {
								try {
									double x = Double.parseDouble(child.getParam("x"));
									double y = Double.parseDouble(child.getParam("y"));
									vertices.add(new Vertex(x,y));
								} catch(NumberFormatException nfe) { continue; }
							}
						}
						area += this.computePolygonArea(vertices);
					}
				}
			}
		}
		return Math.round(area);
	}
}
//Wrapper class for a Vertex point...
class Vertex {
	double x,y;
	public Vertex(double x,double y) {
		this.x=x;
		this.y=y;
	}
	public double getX() { return x; }
	public double getY() { return y; }
}