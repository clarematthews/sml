package sml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Scanner;

/*
 * The translator of a <b>S</b><b>M</b>al<b>L</b> program.
 */
public class Translator {

	// word + line is the part of the current line that's not yet processed
	// word has no whitespace
	// If word and line are not empty, line begins with whitespace
	private String line = "";
	private Labels labels; // The labels of the program being translated
	private ArrayList<Instruction> program; // The program to be created
	private String fileName; // source file of SML code
	private Properties props;

	private static final String SRC = "src";

	public Translator(String fileName) {
		this.fileName = SRC + "/" + fileName;
		// load the properties file
		try {
			props = new Properties();
			InputStream in = getClass().getResourceAsStream("props.properties");
			props.load(in);
			in.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	// translate the small program in the file into lab (the labels) and
	// prog (the program)
	// return "no errors were detected"
	public boolean readAndTranslate(Labels lab, ArrayList<Instruction> prog) {
		Scanner sc; // Scanner attached to the file chosen by the user
		try {
			sc = new Scanner(new File(fileName));
		} catch (IOException ioE) {
			System.out.println("File: IO error to start " + ioE.getMessage());
			return false;
		}
		labels = lab;
		labels.reset();
		program = prog;
		program.clear();

		try {
			line = sc.nextLine();
		} catch (NoSuchElementException ioE) {
			return false;
		}

		// Each iteration processes line and reads the next line into line
		while (line != null) {
			// Store the label in label
			String label = scan();

			if (label.length() > 0) {
				Instruction ins = getInstruction(label);
				if (ins != null) {
					labels.addLabel(label);
					program.add(ins);
				}
			}

			try {
				line = sc.nextLine();
			} catch (NoSuchElementException ioE) {
				return false;
			}
		}
		return true;
	}

	// line should consist of an MML instruction, with its label already
	// removed. Translate line into an instruction with label label
	// and return the instruction
	public Instruction getInstruction(String label) {
		if (line.equals(""))
			return null;

		String ins = scan();
		String className = props.getProperty(ins);

		try {
			Class c = Class.forName(className);
			Object[] params = getParams(label);
			Class[] paramClasses = toClass(params);
			
			Constructor cons = c.getConstructor(paramClasses);
			
			return (Instruction)cons.newInstance(params);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/*
	 * Return the first word of line and remove it from line. If there is no
	 * word, return ""
	 */
	public String scan() {
		line = line.trim();
		if (line.length() == 0)
			return "";

		int i = 0;
		while (i < line.length() && line.charAt(i) != ' '
				&& line.charAt(i) != '\t') {
			i = i + 1;
		}
		String word = line.substring(0, i);
		line = line.substring(i);
		return word;
	}

	// Return the first word of line as an integer. If there is
	// any error, return the maximum int
	public int scanInt() {
		String word = scan();
		if (word.length() == 0) {
			return Integer.MAX_VALUE;
		}

		try {
			return Integer.parseInt(word);
		} catch (NumberFormatException e) {
			return Integer.MAX_VALUE;
		}
	}

	/*
	 * Return an array of Objects representing each element on
	 * the line. Elements are parsed as Integers where possible,
	 * or otherwise left as Strings
	 */
	private Object[] getParams(String label) {
		List<Object> params = new ArrayList<Object>();
		params.add(label);
		String nextParam = scan();
		while(nextParam.length()!=0) {
			try{
				Integer nextParamInt = Integer.parseInt(nextParam);
				params.add(nextParamInt);
			} catch (NumberFormatException e) {
				params.add(nextParam);
			} finally {
				nextParam = scan();
			}
		}
		return params.toArray();
	}

	/*
	 *  Convert an Object array into a Class array,
	 *  with each element being the classtype of the corresponding
	 *  objects. Integer objects give primitive classtype int.class
	 */
	private Class[] toClass(Object[] params) {
		Class[] result = new Class[params.length];
		for(int i=0 ; i<params.length ; i++) {
			result[i] = params[i].getClass();
			if(result[i] == Integer.class){
				result[i] = int.class;
			}
		}
		return result;
	}
}