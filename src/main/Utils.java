package main;

public class Utils {

	/**
	 * Add a string to a primitive string list
	 * @param input The initial String list
	 * @param elem the String to add
	 * @return The new array
	 */
	public static String[] addElementToPSList(String[] input, String elem) {
		String[] np = new String[input.length+1];
		for(int i=0;i<input.length;i++) {
			np[i]=input[i];
		}
		np[input.length] = elem;
		return np;
	}
	/**
	 * Removes a string from a primitive string list
	 * @param input The initial String list
	 * @param index The index to remove
	 * @return The new array
	 */
	public static String[] removedElementFromPSList(String[] input, int index) {
		String[] np = new String[input.length-1];
		for(int i=0;i<np.length;i++) {
			if(i==index) continue;
			else if(i<index) np[i]=input[i];
			else np[i]=input[i+1];
		}
		return np;
	}
	/**
	 * Finds the index of a certain string in a primitive list
	 * @param input The list to seach
	 * @param elem The element to get the index of
	 * @return The index of the string in the list given
	 */
	public static int getElementIndex(String[] input, String elem) {
		for(int i=0;i<input.length;i++) {
			if(input[i].equals(elem)) return i;
		}
		return -1;
	}
}
