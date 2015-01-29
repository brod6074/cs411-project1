
import java.io.FileReader;
import java.io.PushbackReader;


public class Client {

	public static void main(String[] args) throws Exception {
		// open up a file stream for the Toy source code and pass to
		// lexical analyzer
		String filename = "words1.txt";
		PushbackReader source = new PushbackReader(new FileReader(filename));
		ToyLexer toyLexer = new ToyLexer(source);
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();
		toyLexer.scanNextToken();

		
		//toyLexer.dumpTokens() prints tokens to console
		//toyLexer.dumpSymTab() prints the symbol table to the console
		
	}

}
