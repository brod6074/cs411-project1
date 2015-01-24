import java.io.BufferedReader;
import java.io.IOException;

/**
 * 
 * ToyLexicalAnalyzer
 *
 */
public class ToyLexicalAnalyzer {
	
	private SourceStream SourceStream;
	
	/**
	 * TODO
	 */
	public ToyLexicalAnalyzer(BufferedReader in) throws IOException {
		
	}
	
	
	/**
	 * TODO
	 */
	public int getNextToken() {
		
		
		return -1;
	}
	
	
	
	/**
	 * 
	 * SourceStream
	 *
	 */
	private static class SourceStream {
		private BufferedReader in;
		
		private SourceStream(BufferedReader in) {
			this.in = in;
		}
		
		private void ignoreWhitespace() {
			
		}
		
		public char getNextChar() throws IOException {
			return (char)in.read();
		}
		
		
	}
	
	
	/**
	 * 
	 * Trie
	 *
	 */
	private static class Trie {
		private static final int MAX_TRANSITION = 63;
		private static final int ALPHABETIC_CHARS = 52;
		
		private int[] 	trieSwitch = new int [ALPHABETIC_CHARS];
		private char[] 	trieSymbol = new char [MAX_TRANSITION];
		private int[] 	trieNext = new int[MAX_TRANSITION];
	}
	
	private enum ToyToken {
		_boolean(1, "boolean"),
		_break(2, "break"),
		_class(3, "class"),
		_double(4, "double"),
		_else(5, "else"),
		_extends(6, "extends"),
		_for(7, "for"),
		_if(8, "if"),
		_implements(9, "implements"),
		_int(10, "int"),
		_interface(11, "interface"),
		_newarray(12, "newarray"),
		_printl(13, "println"),
		_readln(14, "readln"), 
		_return(15, "return"),
		_string(16, "string"),
		_void(17, "void"),
		_while(18, "while"),
		_plus(19, "plus"),
		_minus(20, "minus"),
		_multiplication(21, "multiplicaiton"),
		_division(22, "division"),
		_mod(23, "mod"),
		_less(24, "less"),
		_lessequal(25, "lessequal"),
		_greater(26, "greater"),
		_greaterequal(27, "greaterequal"),
		_equal(28, "equal"),
		_notequal(29, "notequal"),
		_and(30, "and"),
		_or(31), 
		_not(32), 
		_assignop(33), 
		_semicolon(34), 
		_comma(35),
		_period(36), 
		_leftparen(37, "leftparen"), 
		_rightparen(38, "rightparen"), 
		_leftbracket(39),
		_rightbracket(40), 
		_leftbrace(41), _rightbrace(42), 
		_intconstant(43, "intconstant"),
		_doubleconstant(44), 
		_stringconstant(45), 
		_booleanconstant(46), 
		_id(47),
		// special tokens for recognizing/ignoring whitespace
		_space(-1, "space"),
		_tab(-1, "tab"),
		_newline(-2, "newline");
		
		private final int tokenNum;
		private final String tokenString;
		//private boolean bKeyword;
		
		ToyToken(int num) { 
			this(num, null); 
			//bKeyword = false;
		}
		
		ToyToken(int num, String keyword) { 
			tokenNum = num; tokenString = keyword;
			//bKeyword = true;
		}
		
		public int getTokenNumber() { return tokenNum; }
		
		public boolean isKeyword() { return bKeyword; }
		
	}
	
	

}
