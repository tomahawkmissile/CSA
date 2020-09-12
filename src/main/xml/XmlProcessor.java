package main.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlProcessor {

	File file;
	
	public XmlProcessor(File file) {
		this.file=file;
	}
	/**
	 * Read all lines of the Xml file
	 * @return A line-by-line list of the file
	 */
	public List<String> readLines() {
		List<String> lines = new ArrayList<String>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			while((line=reader.readLine())!=null) {
				lines.add(line);
			}
			reader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return lines;
	}
	/**
	 * Convert a list of strings to one string.
	 * @param input The list of strings
	 * @return A string of the entire list
	 */
	public String convertListToString(List<String> input) {
		StringBuilder sb = new StringBuilder();
		for(String s:input) {
			sb.append(s);
		}
		return sb.toString();
	}
	/**
	 * Split a parameter string into its name and values
	 * @param line The entire XML line
	 * @return A list with the first element being the name and the rest being the values
	 */
	public List<String> smartSpaceSplit(String line) {
		boolean inQuotes=false;
		List<String> arr = new ArrayList<String>();
		int last=0;
		if(line.charAt(line.length()-1)=='/') line=line.substring(0,line.length()-1);
		for(int i=0;i<line.length();i++) {
			char c = line.charAt(i);
			switch(c) {
			case '"':
				inQuotes=!inQuotes;
				break;
			case ' ':
				if(!inQuotes && i>0) {
					arr.add(line.substring(last,i));
					last=i+1;
					break;
				}
			default: 
				if(i==line.length()-2) {
					arr.add(line.substring(last,i+2));
					return arr;
				}
				break;
			}
		}
		return arr;
	}
	/**
	 * Generates an XmlElement from a string line
	 * @param line The input string
	 * @return A new XmlElement with the correct parameters and name as specified by the line
	 */
	public XmlElement generateElementFromLine(String line) {
		List<String> stuff = this.smartSpaceSplit(line);
		String name = stuff.get(0);
		String[] params = new String[stuff.size()-1];
		for(int i=0;i<params.length;i++) {
			params[i]=stuff.get(i+1);
		}
		return new XmlElement(name,params);
	}
	/**
	 * Run the Xml Processor and parse the file into a heirarchy of Xml Elements.
	 * @return A list of all XmlElements in the XML file
	 */
	public List<XmlElement> run() {
		String file = this.convertListToString(this.readLines());
		List<XmlElement> elements = new ArrayList<XmlElement>();
		
		String[] split = file.split("");
		
		List<XmlElement> stack = new ArrayList<XmlElement>();
		int start=-1,end=-1;
		boolean init=false;
		for(int i=0;i<split.length;i++) {
			char c = split[i].charAt(0);
			if(c=='<' && i<split.length-1) {
				if(split[i+1].charAt(0)=='?' || split[i+1].charAt(0)=='!') {
					init=true;
					continue;
				}
			}
			switch(c) {
			case '"': break;
			case '<':
				start=i;
				break;
			case '>':
				if(init) {
					init=false;
					break;
				}
				end=i;
				if(i<split.length-1 && split[start+1].charAt(0)=='/') {
					stack.remove(stack.size()-1);
					break;
				}
				XmlElement e = this.generateElementFromLine(file.substring(start+1,end));
				elements.add(e);
				if(stack.size()==0) {
					stack.add(e);
				} else {
					stack.get(stack.size()-1).addChild(e);
				}
				if(i>0 && split[i-1].charAt(0)!='/') stack.add(e);
				start=-1;
				end=-1;
				break;
			default: break;
			}
		}
		return elements;
	}
}
