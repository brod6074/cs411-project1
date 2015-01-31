import java.io.IOException;
import java.io.PushbackReader;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

/**
 * 
 * @author Roberto Rodriguez
 * 
 * This class implements a lexical analyzer for the Toy language
 *
 */
public class ToyLexer {
	private static final char EOF_CHAR = (char) -1;
	
	private PushbackReader 	source;
	private List<ToyToken> 	tokens;
	private Trie 			symTab;
	private boolean 		eofReached;
	
	
	/**
	 * Constructor
	 * 
	 * @param source - a PushbackReader containing a stream to the input file
	 */
	public ToyLexer(PushbackReader source) {
		this.source = source;
		tokens = new LinkedList<ToyToken>();
		symTab = new Trie();
		eofReached = false;	
		insertKeywords(); // initialize the symbol table with the keywords
	}
	
	
	/**
	 * Scans the input stream and constructs the next recognized token.
	 * Whitespace and comments encountered are stripped out.
	 * 
	 * The next token scanned is added to the tokens list.
	 *  
	 */
	public void scanNextToken() throws IOException {
		char curr, peek;
		curr = nextUsefulChar();
		
		// HANDLE OPERATORS
		switch (curr) {
		
			// EOF
			case EOF_CHAR:
			tokens.add(ToyToken._eof);
			eofReached = true;
			break;
		
			// STRING CONSTANTS
			case '"':
				while ((curr = readChar()) != '"') {}
				tokens.add(ToyToken._stringconstant);
				break;
			
			// SINGLE CHAR SYMBOLS/OPERATORS
			case '+': tokens.add(ToyToken._plus); 			break;
			case '-': tokens.add(ToyToken._minus); 			break;
			case '*': tokens.add(ToyToken._multiplication); break;
			case '/': tokens.add(ToyToken._division); 		break;
			case '%': tokens.add(ToyToken._mod); 			break;
			case ';': tokens.add(ToyToken._semicolon); 		break;
			case ',': tokens.add(ToyToken._comma); 			break;
			case '.': tokens.add(ToyToken._period); 		break;
			case '(': tokens.add(ToyToken._leftparen); 		break;
			case ')': tokens.add(ToyToken._rightparen); 	break;
			case '[': tokens.add(ToyToken._leftbracket); 	break;
			case ']': tokens.add(ToyToken._rightbracket); 	break;
			case '{': tokens.add(ToyToken._leftbrace); 		break;
			case '}': tokens.add(ToyToken._rightbrace); 	break;
				
			// MULTI-CHAR OPERATORS
			case '<':
				peek = readChar();
				if (peek == '=')
					tokens.add(ToyToken._lessequal);
				else {
					pushback( peek);
					tokens.add(ToyToken._less);
				}
				break;
			case '>':
				peek = readChar();
				if (peek == '=')
					tokens.add(ToyToken._greaterequal);
				else {
					pushback( peek);
					tokens.add(ToyToken._greater);
				}
				break;
			case '=':
				peek = readChar();
				if (peek == '=')
					tokens.add(ToyToken._equal);
				else {
					pushback( peek);
					tokens.add(ToyToken._assignop);
				}
				break;
			case '!':
				peek = readChar();
				if (peek == '=')
					tokens.add(ToyToken._notequal);
				else {
					pushback( peek);
					tokens.add(ToyToken._not);
				}
				break;		
			case '&':
				peek = readChar();
				if (peek == '&')
					tokens.add(ToyToken._and);
				else {
					pushback( peek);
					tokens.add(ToyToken._ERROR);
				}
				break;		
			case '|':
				peek = readChar();
				if (peek == '|')
					tokens.add(ToyToken._or);
				else {
					pushback( peek);
					tokens.add(ToyToken._ERROR);
				}
				break;		
		}
		
		// IDENTIFIERS/KEYWORDS/BOOLEAN CONSTANTS
		if (Character.isLetter(curr)) {
			StringBuilder sb = new StringBuilder();
			sb.append(curr);
			peek = readChar();
			
			while (Character.isLetterOrDigit(peek) || peek == '_') {
				sb.append(peek);
				peek = readChar();
			}
			pushback(peek);
			String s = sb.toString();
			
			// determine if token should be id, keyword, or boolean
			switch (s) {
			case "true":
			case "false":
				tokens.add(ToyToken._booleanconstant); 	break;
			case "boolean":
				tokens.add(ToyToken._boolean); 			break;
			case "break":
				tokens.add(ToyToken._break); 			break;
			case "class":
				tokens.add(ToyToken._class); 			break;
			case "double":
				tokens.add(ToyToken._double); 			break;
			case "else":
				tokens.add(ToyToken._else); 			break;
			case "extends":
				tokens.add(ToyToken._extends); 			break;
			case "for":
				tokens.add(ToyToken._for); 				break;
			case "if":
				tokens.add(ToyToken._if); 				break;
			case "implements":
				tokens.add(ToyToken._implements); 		break;
			case "int":
				tokens.add(ToyToken._int); 				break;
			case "interface":
				tokens.add(ToyToken._interface); 		break;
			case "newarray":
				tokens.add(ToyToken._newarray); 		break;
			case "println":
				tokens.add(ToyToken._println); 			break;
			case "readln":
				tokens.add(ToyToken._readln); 			break;
			case "return":
				tokens.add(ToyToken._return); 			break;
			case "string":
				tokens.add(ToyToken._string); 			break;
			case "void":
				tokens.add(ToyToken._void); 			break;
			case "while":
				tokens.add(ToyToken._while); 			break;
			default:
				tokens.add(ToyToken._id);
				//if (!symTab.search(s))
				//	symTab.insert(s, ID_TERMINAL);
			}
			
		}
		// DIGITS
		else if (Character.isDigit(curr)) {
			peek = readChar();
			
			// HEX INT
			if (curr == '0' && Character.toUpperCase(peek) == 'X') {
				curr = readChar();
				if (isHexDigit(curr)) {
					tokens.add(ToyToken._intconstant);
					while (isHexDigit(curr = readChar())) {}
					pushback(curr);
				}
				else
					tokens.add(ToyToken._ERROR);			
			}
			// DOUBLES AND DECIMAL INTS
			else {
				pushback(peek);
				while (Character.isDigit(curr))
					curr = readChar();
				
				// DOUBLE
				if (curr == '.') {
					tokens.add(ToyToken._doubleconstant);
					handleDouble();
				}
				// DECIMAL INT
				else {
					if (Character.toUpperCase(curr) == 'E')
						handleExponent(curr);
					tokens.add(ToyToken._intconstant);
				}
			}
		}
	}
	
	/**
	 * Handles double constants. Method is called ONLY after a '.' has been
	 * read in from the input stream. 
	 */
	private void handleDouble() throws IOException {
		char curr, peek1, peek2;
		
		while (Character.isDigit(curr = readChar())) {}
		
		// DOUBLE WITH EXPONENT
		if (Character.toUpperCase(curr) == 'E') {
			handleExponent(curr);
		// DOUBLE WITHOUT EXPONENT	
		} else {
			pushback(curr);
		}
	}
	
	/**
	 * Method consumes valid characters for exponent.
	 * @param curr - character with value of 'E' or 'e'
	 * @throws IOException
	 */
	private void handleExponent(char curr) throws IOException{
		char peek1, peek2;
		
		peek1 = readChar();
		// E####
		if (Character.isDigit(peek1)) {
			while (Character.isDigit(peek1 = readChar())) {}
			pushback(peek1);
		// E+ or E-
		} else if (peek1 == '-' || peek1 == '+') {
			peek2 = readChar();
			// at least 1 char after +/- to be valid
			if (Character.isDigit(peek2)) {
				while (Character.isDigit(peek2 = readChar())) {}
				pushback(peek2);
			}
			// invalid exponential form, push back chars that will used
			// for other tokens
			else {
				pushback(peek2); 	// pushback char after +/-
				pushback(peek1); 	// pushback + or -
				pushback(curr); 	// pushback e or E
			}
		}
	}
	
	/**
	 * Checks if character is a valid hex digit
	 * 
	 * @param c - char to be checked
	 * @return - true if c is a hex digit, false otherwise
	 */
	private boolean isHexDigit(char c) {
		c = Character.toUpperCase(c);
		return Character.isDigit(c) || (c == 'A') || (c == 'B') || (c == 'C') ||
				(c == 'D') || (c == 'E') || (c == 'F');
	}
	
	public boolean isEOF() {
		return eofReached;
	}
	
	public void dumpTokens() {
		Iterator<ToyToken> iter = tokens.iterator();
		while (iter.hasNext()) {
			ToyToken t = iter.next();
			if (t.toString().equals("carriage"))
				System.out.println();
			else if (!t.toString().equals("EOF"))
				System.out.print(t.toString() + " ");
		}
	}
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	private boolean isWhiteSpace(char c) {
		if (c == '\r')
			tokens.add(ToyToken._carriageReturn);
		return (c == ' ') || (c == '\t') || (c == '\n') || (c == '\r');
	}
	
	private char nextUsefulChar() throws IOException {
		char curr = readChar();
		char peek;
		boolean usefulChar = false;
		
		do {
			while (isWhiteSpace(curr)) {
				curr = readChar();
			}
			
			if (curr == '/') {
				peek = readChar();
				switch (peek) {
				case '/':
					while ((curr = readChar()) != '\r') {}
					curr = readChar();
					break;
				case '*':
					curr = readChar();
					peek = readChar();
					while (curr != '*' || peek != '/') {
						if (curr == '\r')
							tokens.add(ToyToken._carriageReturn);
						curr = peek;
						peek = readChar();
					}
					curr = readChar();
					break;
				default:
					pushback(peek);
					usefulChar = true;
				}
			} else {
				usefulChar = true;
			}
			
		} while (usefulChar == false);
		
		
		return curr;
	}
	
	
	/**
	 * Read next character from input stream
	 * @return
	 * @throws IOException
	 */
	private char readChar() throws IOException { return (char)source.read(); }
	
	/**
	 * Push back a character into the input stream
	 * @param c
	 * @throws IOException
	 */
	private void pushback(char c) throws IOException { source.unread((int)c); }
	
	private void insertKeywords() {
		symTab.insert("boolean");
		symTab.insert("break");
		symTab.insert("class");
		symTab.insert("double");
		symTab.insert("else");
		symTab.insert("extends");
		symTab.insert("false");
		symTab.insert("for");
		symTab.insert("if");
		symTab.insert("implements");
		symTab.insert("int");
		symTab.insert("interface");
		symTab.insert("newarray");
		symTab.insert("println");
		symTab.insert("readln");
		symTab.insert("return");
		symTab.insert("string");
		symTab.insert("true");
		symTab.insert("void");
		symTab.insert("while");
	}
	
	public void dumpSymbolTable() {
		symTab.dumpSymbolTable();
	}
	
	/**
	 * 
	 * Trie
	 *
	 */
	private static class Trie {
		private static final int MAX_TRANSITION = 300;
		private static final int ALPHABETIC_CHARS = 52;
		private static final int EMPTY = -1;
		
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
		
		/**
		 * Prints out contents of symbol table to System.out
		 */
		public void dumpSymbolTable() {
			System.out.print("\t");
			for (char c = 'A'; c <= 'Z'; c++)
				System.out.print(c + "   ");
			for (char c = 'a'; c <= 'z'; c++)
				System.out.print(c + " ");
			System.out.println();
			System.out.print("switch: ");
			for (int i = 0; i < ALPHABETIC_CHARS; i++)
				System.out.print(trieNext[i] + "  ");
		}
		
		/**
		 * Searches the symbol table for an existing entry in the symbol table.
		 * Returns true if exists, false otherwise
		 * 
		 * @param s - the string to be searched for
		 * @return - boolean representing successful search
		 */
		boolean search(String s) {
			// use first character to determine index in switch array
			char c = s.charAt(0);
			int switchIndex = getSwitchIndex(c);
			
			int currPos = trieSwitch[switchIndex];
			for (int i = 1; i < s.length(); i++) {
				c = s.charAt(i);
				while (c != trieSymbol[currPos]) {
					currPos = trieNext[currPos];
				}
				currPos++;
			}
			if (trieSymbol[currPos] == '@')
				return true;
			else
				return false;
			
		}
		
		/**
		 * Insert a string into the symbol table
		 * @param s - string to be inserted
		 */
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
						
					} else {
						while (trieSymbol[currPos] != ' ' && trieSymbol[currPos] != c) {
							currPos = trieNext[currPos];
							if (trieNext[currPos] == EMPTY)
								trieNext[currPos] = nextFreeSpot;
						}
					}
					trieSymbol[currPos++] = c;
				}
			}
			
			// word is smaller than similar, previously inserted words
			// e.g. inserting abs after absolute/absolutely
			while (trieSymbol[currPos] != ' ') {
				if (trieNext[currPos] == EMPTY) 
					trieNext[currPos] = nextFreeSpot;
					
				currPos = trieNext[currPos];
			}		
			
			trieSymbol[currPos++] = '@';						
			nextFreeSpot = currPos;		
		}
		
		/**
		 * Returns an index number for the switch array in the symbol table.
		 *  
		 * @param c - character of first element in the string
		 * @return index corresponding to the character
		 */
		private int getSwitchIndex(char c) {
			if (Character.isUpperCase(c))
				return ((int) c) - 65;
			else
				return ((int) c) - 71;
		}
	} // end of class Trie
	
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
		_println(13, "println"),
		_readln(14, "readln"), 
		_return(15, "return"),
		_string(16, "string"),
		_void(17, "void"),
		_while(18, "while"),
		_plus(19, "plus"),
		_minus(20, "minus"),
		_multiplication(21, "multiplication"),
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
		_carriageReturn(48, "carriage"),
		_eof(49, "EOF"),
		_ERROR(50, "ERROR_TOKEN");
		
		private final int tokenNum;
		private final String tokenString;
		
		
		ToyToken(int num, String keyword) { 
			tokenNum = num; tokenString = keyword;
		}
		
		public int getTokenNumber() { return tokenNum; }		
		public String toString() { return tokenString; }
		
	} // end of enum ToyToken

} // end of class ToyLexer
