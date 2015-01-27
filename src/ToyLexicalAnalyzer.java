//import java.io.BufferedReader;
import java.io.IOException;
import java.io.PushbackReader;
//import java.util.Collection;

/**
 * 
 * ToyLexicalAnalyzer
 *
 */
public class ToyLexicalAnalyzer {
	//private static final char EOF = (char) -1;
	
	private PushbackReader source;
	private Trie symTab;
	//private Collection<ToyToken> tokens;
	
	public ToyLexicalAnalyzer(PushbackReader source) {
		this.source = source;
		symTab = new Trie();
	}
	
	
	/**
	 * Scans the input stream and constructs the next recognized token.
	 * 
	 * @return int representing the token class 
	 */
	public int scanNextToken() throws IOException {
		char currentChar, peekChar;
		currentChar = (char)source.read();
		
		// skip whitespace
		while (isWhiteSpace(currentChar))
			currentChar = (char)source.read();
		
		// code for handling identifiers/keywords/booleans
		if (Character.isLetter(currentChar)) {
			StringBuilder sb = new StringBuilder();
			sb.append(currentChar);
			peekChar = (char)source.read();
			
			while (Character.isLetterOrDigit(peekChar) || peekChar == '_') {
				sb.append(peekChar);
				peekChar = (char)source.read();
			}
			
			String s = sb.toString();
			
			// determine if token should be id, keyword, or boolean
			
			// if id, add to symbol table
			symTab.insert(s);
		}
		if (Character.isDigit(currentChar)) {
			// code to handle integers (decimal or hex)
			// code to handle floating point
		}
		
		return -1;
	}
	
	private boolean isWhiteSpace(char c) {
		return (c == ' ') || (c == '\t') || (c == '\n') || (c == '\r');
	}	
	
	/**
	 * 
	 * Trie
	 *
	 */
	private static class Trie {
		private static final int MAX_TRANSITION = 63;
		private static final int ALPHABETIC_CHARS = 52;
		private static final int EMPTY = -1;
		private static final char KEYWORD_TERMINAL = '!';
		private static final char ID_TERMINAL = '@';
		
		private int 	nextFreeSpot;
		private int[] 	trieSwitch;
		private char[] 	trieSymbol;
		private int[] 	trieNext;
		
		public Trie() {
			nextFreeSpot = 0;
			
			trieSwitch = new int [ALPHABETIC_CHARS];
			for (int i = 0; i < trieSwitch.length; i++)
				trieSwitch[i] = EMPTY;
			
			trieSymbol = new char [MAX_TRANSITION];
			trieNext = new int[MAX_TRANSITION];
			for (int i = 0; i < trieSymbol.length; i++) {
				trieSymbol[i] = ' ';
				trieNext[i] = EMPTY;
			}
		}
		
		void insert(String s) {
			// use first character to determine index in switch array
			char c = s.charAt(0);
			int switchIndex = getSwitchIndex(c);
			
			if (trieSwitch[switchIndex] == EMPTY)
				trieSwitch[switchIndex] = nextFreeSpot;
			
			int currPos = trieSwitch[switchIndex];
			for (int i = 1; i < s.length(); i++) {
				c = s.charAt(i);
				if (trieSymbol[currPos] == ' ' || trieSymbol[currPos] == c) {
					trieSymbol[currPos++] = c;
				} else {
					if (trieNext[currPos] == EMPTY) {
						trieNext[currPos] = nextFreeSpot;
						currPos = nextFreeSpot;
						trieSymbol[currPos++] = c;
					} else {
						//
					}
				}
			}
			
			// if keyword, trieSymbol[position++] = KEYWORD_TERMINAL;
			// else , trieSymbol[position++] = ID_TERMINAL;
			// nextFreeSpot = position;
			trieSymbol[currPos++] = '@';
			nextFreeSpot = currPos;
			
		}
		
		boolean isReserved(String s) {
			return true;
		}
		
		/**
		 * Returns an appropriate number (between 0 and ALPHABETIC_CHARS - 1) 
		 * for indexing into the switch array depending on the char parameter.
		 *  
		 * @param c
		 * @return
		 */
		private int getSwitchIndex(char c) {
			return ((int) c) - 65;
		}
	}
	
	/*private enum ToyToken {
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
		_or(31, "or"),
		_not(32, "not"),
		_assignop(33, "assignop"),
		_semicolon(34, "semicolon"),
		_comma(35, "comma"),
		_period(36, "period"),
		_leftparen(37, "leftparen"),
		_rightparen(38, "rightparen"),
		_leftbracket(39, "leftbracket"),
		_rightbracket(40, "rightbracket"),
		_leftbrace(41, "leftbrace"),
		_rightbrace(42, "rightbrace"),
		_intconstant(43, "intconstant"),
		_doubleconstant(44, "doubleconstant"),
		_stringconstant(45, "stringconstant"),
		_booleanconstant(46, "booleanconstant"),
		_id(47, "id"),
		// special tokens for recognizing/ignoring whitespace
		_space(-1, "space"),
		_tab(-1, "tab"),
		_newline(-2, "newline"),
		_eof(-3, "eof");
		
		private final int tokenNum;
		private final String tokenString;
		
		ToyToken(int num) { 
			this(num, null); 
		}
		
		ToyToken(int num, String keyword) { 
			tokenNum = num; tokenString = keyword;
		}
		
		public int getTokenNumber() { return tokenNum; }
		
	}*/

} // end of class ToyLexicalAnalyzer
