/**
 * Programmer: 	Roberto Rodriguez
 * Date:		2/3/2015
 * Class:		CS 411 (TTh 3:00pm)
 * Assignment:	Project #1
 * 
 * INSTRUCTIONS: Compile both Client.java and ToyLexer.java files and make sure
 * to include both "input1.txt" and "input2.txt" in the same directory.
 */

import java.io.FileReader;
import java.io.PushbackReader;


/**
 * 
 * The client class creates a ToyLexer object for each input file, reads in the
 * tokens from each file, and outputs the results to System.out
 */
public class Client {

	public static void main(String[] args) throws Exception {
		/////////////////////////////////////////////////////
		// TEST 1
		////////////////////////////////////////////////////
		
		// open up stream for source file and pass to lexer
		String filename = "input1.txt";
		PushbackReader source = new PushbackReader(new FileReader(filename), 3);
		ToyLexer toyLexer = new ToyLexer(source);
		
		// scan tokens until end of file
		while (!toyLexer.isEOF())
			toyLexer.scanNextToken();	
		
		System.out.println("--------------------------------------------");
		System.out.println("|         TEST 1 (input1.txt)              |");
		System.out.println("--------------------------------------------");
		System.out.println(("\n------------- TOKEN OUTPUT -----------------"));
		toyLexer.dumpTokens();
		System.out.println();
		System.out.println(("\n------------- SYMBOL TABLE OUTPUT -----------"));
		toyLexer.dumpSymbolTable();	
		System.out.println();
		
		/////////////////////////////////////////////////////
		// TEST 2
		////////////////////////////////////////////////////

		filename = "input2.txt";
		source = new PushbackReader(new FileReader(filename), 3);
		toyLexer = new ToyLexer(source);

		// scan tokens until end of file
		while (!toyLexer.isEOF())
			toyLexer.scanNextToken();	

		System.out.println("--------------------------------------------");
		System.out.println("|         TEST 2 (input2.txt)              |");
		System.out.println("--------------------------------------------");
		System.out.println(("\n------------- TOKEN OUTPUT -----------------"));
		toyLexer.dumpTokens();
		System.out.println();
		System.out.println(("\n------------- SYMBOL TABLE OUTPUT -----------"));
		toyLexer.dumpSymbolTable();	
		System.out.println();
	}
}