package main.xml;

import java.util.ArrayList;
import java.util.List;

import main.Utils;

public class XmlElement {

	String name;
	String[] params;
	
	public XmlElement(String name, String[] params) {
		this.name = name;
		this.params = params;
	}
	/**
	 * Get the name of this Xml Element
	 * @return This element's name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Get a list of all parameters
	 * @return The list of parameters
	 */
	public String[] getParams() {
		return params;
	}
	/**
	 * Get a parameter by list index
	 * @param index The index of the parameter
	 * @return The parameter
	 */
	public String getParam(int index) {
		if(index<params.length) {
			return params[index];
		} else {
			return null;
		}
	}
	/**
	 * Add a parameter in whole string format.
	 * @param p A string in the format 'name="value"', where name is the parameter name and value is the parameter value. Do not forget quotes.
	 */
	public void addParam(String p) {
		params = Utils.addElementToPSList(params,p);
	}
	/**
	 * Remove a parameter by index
	 * @param index The index of the parameter to remove
	 */
	public void removeParam(int index) {
		params = Utils.removedElementFromPSList(params, index);
	}
	/**
	 * Get a parameter by its name
	 * @param node The name of the parameter node
	 * @return The parameter's value
	 */
	public String getParam(String node) {
		for(String param:this.getParams()) {
			param = param.replace("\"", "");
			String[] split = param.split("=");
			try {
				if(split[0].equals(node)) return split[1];
			} catch(Exception e) {}
		}
		return null;
	}
	/**
	 * Check if this Xml Element has a parameter by the name
	 * @param name The name of the paramers
	 * @return True if the parameter exists, false otherwise
	 */
	public boolean hasParam(String name) {
		String ret = this.getParam(name);
		return ret!=null;
	}
	
	List<XmlElement> parents = new ArrayList<XmlElement>();
	List<XmlElement> children = new ArrayList<XmlElement>();
	
	/**
	 * Add a parent Xml Element.
	 * @param e The parent to add
	 */
	public void addParent(XmlElement e) {
		parents.add(e);
	}
	/**
	 * Remove a parent Xml Element.
	 * @param e The parent to remove
	 */
	public void removeParent(XmlElement e) {
		parents.remove(e);
	}
	/**
	 * Get the parents of this Xml Element.
	 * @return A list with the parent elements in order from most immediate to least.
	 */
	public List<XmlElement> getParents() {
		return parents;
	}
	/**
	 * Add a child Xml Element
	 * @param e The Xml  Element to add to children
	 */
	public void addChild(XmlElement e) {
		children.add(e);
	}
	/**
	 * Remove a child Xml Element
	 * @param e The Xml Element to remove from children
	 */
	public void removeChild(XmlElement e) {
		children.remove(e);
	}
	/**
	 * Get all children Xml Elements
	 * @return A list of all children.
	 */
	public List<XmlElement> getChildren() {
		return children;
	}
	/**
	 * Get a human-readable string of an Xml Element.
	 * @return A user-friendly string.
	 */
	public String toString() {
		String s1 = "Name="+this.getName()+
				", Parameters=";
		String params="[ ";
		for(String p:this.getParams()) params += p + ", ";
		params += "]";
		return s1+params;
	}
}
