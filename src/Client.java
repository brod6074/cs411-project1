//import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PushbackReader;


public class Client {

	public static void main(String[] args) throws Exception {
		// open up a file stream for the Toy source code and pass to
		// lexical analyzer
		String filename = "words1.txt";
		//BufferedReader in = new BufferedReader(new FileReader(filename));
		PushbackReader source = new PushbackReader(new FileReader(filename));
		ToyLexicalAnalyzer toyLexer = new ToyLexicalAnalyzer(source);
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		
		//toyLexer.dumpTokens() prints tokens to console
		//toyLexer.dumpSymTab() prints the symbol table to the console
		
	}

}
