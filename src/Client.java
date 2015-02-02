
import java.io.FileReader;
import java.io.PushbackReader;


public class Client {

	public static void main(String[] args) throws Exception {
		// open up stream for source file and pass to lexer
		String filename = "input1.txt";
		PushbackReader source = new PushbackReader(new FileReader(filename), 3);
		ToyLexer toyLexer = new ToyLexer(source);
		
		// scan tokens until end of file
		while (!toyLexer.isEOF())
			toyLexer.scanNextToken();	
		
		toyLexer.dumpTokens();
		System.out.println();
		System.out.println();
		toyLexer.dumpSymbolTable();	
	}
}
