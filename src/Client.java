import java.io.BufferedReader;
import java.io.FileReader;


public class Client {

	public static void main(String[] args) throws Exception {
		// open up a file stream for the Toy source code and pass to
		// lexical analyzer
		String filename = "input1.txt";
		BufferedReader in = new BufferedReader(new FileReader(filename));
		ToyLexicalAnalyzer scanner = new ToyLexicalAnalyzer(in);
		
		// print out the tokens of the source file
		// for (iter = scanner.getTokenIterator(); iter != null; iter++)
		//		System.out.print(iter.getTokenString);
		
		// print out contents of trie table
		// code to print table
		
	}

}
