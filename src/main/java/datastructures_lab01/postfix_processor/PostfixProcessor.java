package datastructures_lab01.postfix_processor;

import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;

/**
 * This class executes the main method which takes in 2 arguments: inputFile and
 * outputFile The program will functionally do the following: 
 * 		- read each line of the inputFile (char by char) 
 * 		- determine if line is a valid postfix expression 
 * 		- if line is valid postfix expression, translate to machine lang sequence 
 * 		- if line is valid postfix expression, write machine lang sequence to outputFile
 * 
 * pre-conditions: 
 * 		- inputFile exists 
 * 		- program has read access to inputFile 
 * 		- program has write access to outputFile
 * 
 * post-conditions: 
 * 		- outputFile written with machine lang sequence translation of postfix string
 * 
 * @author zachbialik
 *
 */
public class PostfixProcessor {

	private static Logger LOGGER = LogManager.getLogger(PostfixProcessor.class);
	private static StackLinked operandStackLinked = new StackLinked(); // initialize empty stack
	private static String machineLangString = ""; // initialize empty string
	private static final String TEMP_PREFIX = "TEMP";
	private static int tempVariable = 1;

	private static FileReader inputReader = null;
	private static BufferedWriter outputWriter = null;

	public static void main(String[] args) throws Exception {
		String inputFile = args[0];
		String outputFile = args[1];

		LOGGER.info("reading user inputs");
		LOGGER.debug("input file: " + inputFile);
		LOGGER.debug("output file: " + outputFile);

		try {
			inputReader = new FileReader(inputFile); // input file
			outputWriter = new BufferedWriter(new FileWriter(outputFile)); // output file

			outputWriter.write("");
			convertPostfixToMachineLanguage(inputReader, outputWriter); // execute main process

		} catch (FileNotFoundException e) {
			LOGGER.error("file not found:" + inputFile);
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.error("i/o error thrown: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (inputReader != null) {
					inputReader.close();
				}
				if (outputWriter != null) {
					outputWriter.close();
				}
			} catch (IOException e) {
				LOGGER.error("error closing stream.");
			}
		}
	}

	private static void convertPostfixToMachineLanguage(FileReader inputReader, BufferedWriter outputWriter)
			throws Exception {

		int cInt;
		boolean postfixValid = true;
		boolean postfixConversionComplete = true;
		String postfixString = "";
		
		LOGGER.info("Begin reading input file char by char.");
		while ((cInt = inputReader.read()) != -1) { // read and process one character
			postfixConversionComplete = false; //

			// if (end of line)
			if (String.valueOf((char) cInt).equals("\n") || String.valueOf((char) cInt).equals("\r")) {

				LOGGER.trace("Linebreak character identified");

				// document results
				documentResults(postfixValid, postfixString);

				// reset everything to start as new
				tempVariable = 1;
				postfixString = "";
				machineLangString = "";
				postfixValid = true;
				postfixConversionComplete = true;
				operandStackLinked.clear();

			}
			// if (postfix string is valid) and (character is not white space): process
			// character
			else if (!isWhiteSpace(cInt)) {

				char c = (char) cInt;

				// append character to postfixString
				postfixString += c;

				// process character
				if (postfixValid) {
					LOGGER.trace("Processing character: " + String.valueOf(c));
					postfixValid = processCharacter(cInt);
					LOGGER.trace("operandStackLinked: " + operandStackLinked.toString());
				}

			}
		}

		// document results for last postfix string if no linebreak at EOF
		if (!postfixConversionComplete) {
			documentResults(postfixValid, postfixString);
		}
	}

	private static void documentResults(boolean postfixValid, String postfixString) throws Exception {

		// remove last stored TEMP variable from operand stack
		operandStackLinked.pop();
		
		writeStringToFile(outputWriter, "---------------------------------------------");

		// if (end of line) and (postfix string is valid) and (operand stack is empty):
		// write stack to file
		if (postfixValid && operandStackLinked.isEmpty()) {

			LOGGER.debug("postfix expression (" + postfixString + "):\t VALID.");

			postfixString += " as machine lang sequence:";
			writeStringToFile(outputWriter, postfixString);
			writeStringToFile(outputWriter, machineLangString);
			
		} else {
			// write INVALID to output file for given postfix string
			LOGGER.debug("postfix expression (" + postfixString + "):\t INVALID.");

			postfixString += " is an INVALID postfix expression\n";
			writeStringToFile(outputWriter, postfixString);
//			writeStringToFile(outputWriter, "\tINVALID\n");

		}
	}

	/**
	 * Appends the provided string to file
	 * 
	 * @param outputStream
	 * @param line
	 */
	private static void writeStringToFile(BufferedWriter outputWriter, String line) {

		try {
			outputWriter.append(line);
			outputWriter.newLine();
		} catch (IOException e) {
			LOGGER.fatal("caught i/o error while appending string (" + line + ") to file.");
			e.printStackTrace();
		}

	}

	/**
	 * If character is operand: push to operandStackLinked return postfixValid =
	 * true Else If character is operator: determine machine language steps and
	 * append to machineLangStackLinked return postfixValid = true Else: return
	 * postfixValid = false
	 * 
	 * @param cInt
	 * @return postfixValid (bool) representing whether postfix string is valid
	 * @throws Exception
	 */
	private static boolean processCharacter(int cInt) throws Exception {

		char c = (char) cInt;
		boolean postfixValid = true;

		if (isOperand(c)) {
			operandStackLinked.push(c);
		} else if (isOperator(c)) {

			// First, verify operandStackLinked has at least 2 elements
			if (operandStackLinked.size() >= 2) {

				// Then, convert operator to machine language
				String machineOperator = translateOperator(c);

				// Then, pop from operandStackLinked twice
				String c1 = String.valueOf(operandStackLinked.pop());
				String c2 = String.valueOf(operandStackLinked.pop());

				// append "LD {c2}" to machineLangString
				machineLangString += "\tLD\t" + c2 + "\n";

				// append "{machineOperator} {c1}" to machineLangString
				machineLangString += "\t" + machineOperator + "\t" + c1 + "\n"; 

				// set tempVariableString to TEMPn
				String tempVariableString = TEMP_PREFIX + String.valueOf(tempVariable);
				
				// append "ST {tempVariableString}" to machineLangString
				machineLangString += "\tST\t" + tempVariableString + "\n";
				
				// push tempVariableString to operandStackLinked and increment temp variable
				operandStackLinked.push(tempVariableString);
				tempVariable++;
				
			} else {
				postfixValid = false;
			}

		} else {
			postfixValid = false;
		}

		return postfixValid;

	}

	/**
	 * Returns the string representing the given operator in the machine lang e.g.
	 * '+' returns "AD"
	 * 
	 * @param operator
	 * @return machineOperator
	 * @throws Exception
	 */
	private static String translateOperator(char operator) throws Exception {

		// return translated operators for +, -, *, and /
		if (operator == '+') {
			return "AD";
		} else if (operator == '-') {
			return "SB";
		} else if (operator == '*') {
			return "ML";
		} else if (operator == '/') {
			return "DV";
		} else if (operator == '$') {
			return "PW";
		} else {
			throw new Exception("the provided character is not a valid operator");
		}
	}

	/**
	 * Returns true if character is an operand (if alphabet letter)
	 * 
	 * @param c
	 * @return isOperand
	 */
	private static boolean isOperand(char c) {
		if (Character.isLetter(c)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns true if character is operator
	 * 
	 * @param c
	 * @return isOperator
	 */
	private static boolean isOperator(char c) {
		if (c == '+' || c == '-' || c == '*' || c == '/' || c == '$') {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isWhiteSpace(int cInt) throws Exception {

		char c = (char) cInt;
		if (!String.valueOf(cInt).matches(".") && !String.valueOf(c).equals(" ")) {
			return false;
		} else {
			return true;
		}
	}

}
