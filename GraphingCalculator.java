import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

// This is still in progress and therefore includes subroutines that are not yet implemented.
// (Namely, matrices are not yet usable.)

public class GraphingCalculator extends JFrame {

	// Set up constants for button names and functionalities.
	// (Button names should be no longer than about 8 characters for them to fit on the buttons.)
	private static final String ERROR_MESSAGE = "Error!";
	private static final String DOMAIN_ERROR_MESSAGE = "Domain Error!";
	private static final String DIMENSION_ERROR_MESSAGE = "Dimension Error!";

	private static final String ONE = "1";
	private static final String TWO = "2";
	private static final String THREE = "3";
	private static final String FOUR = "4";
	private static final String FIVE = "5";
	private static final String SIX = "6";
	private static final String SEVEN = "7";
	private static final String EIGHT = "8";
	private static final String NINE = "9";
	private static final String ZERO = "0";
	private static final String DECIMAL_POINT = ".";

	private static final String COMMA = ",";

	private static final String ADDITION = "+";
	private static final String SUBTRACTION = "-";
	private static final String MULTIPLICATION = "\u00D7";
	private static final String ASTERISK = "*";
	private static final String DIVISION = "\u00F7";
	private static final String SLASH = "/";
	private static final String OPEN_PARENTHESIS = "(";
	private static final String CLOSE_PARENTHESIS = ")";

	private static final String ENTER = "Enter";
	private static final String ENTRY = "Entry";
	private static final String ANSWER = "Ans";
	private static final String NEGATIVE_TEXT = "(-)";

	// NEGATIVE is used for encoding negative signs to distinguish them from subtraction.
	private static final String NEGATIVE = "\u207B";

	private static final String END = "$";

	private static final String SINE = "sin";
	private static final String COSINE = "cos";
	private static final String TANGENT = "tan";
	private static final String INVERSE_SINE = "sin\u207B\u00B9";
	private static final String INVERSE_COSINE = "cos\u207B\u00B9";
	private static final String INVERSE_TANGENT = "tan\u207B\u00B9";
	private static final String COSECANT = "csc";
	private static final String SECANT = "sec";
	private static final String COTANGENT = "cot";
	private static final String INVERSE_COSECANT = "csc\u207B\u00B9";
	private static final String INVERSE_SECANT = "sec\u207B\u00B9";
	private static final String INVERSE_COTANGENT = "cot\u207B\u00B9";
	private static final String COMMON_LOG = "log";
	private static final String NATURAL_LOG = "ln";
	private static final String SQUARE_ROOT = "\u221A";
	private static final String POWER = "^";

	private static final String IDENTITY = "identity";
	private static final String DETERMINANT_TEXT = "determinant";
	private static final String DETERMINANT = "det";
	private static final String TRANSPOSE_TEXT = "transpose";
	private static final String TRANSPOSE = "\u1D40";
	private static final String ADJOINT_TEXT = "adjoint";
	private static final String ADJOINT = " \u20F0";
	private static final String TRACE_TEXT = "trace";
	private static final String TRACE = "tr";
	private static final String REF_TEXT = "row echelon form";
	private static final String REF = "ref";
	private static final String RREF_TEXT = "row reduced echelon form";
	private static final String RREF = "rref";
	private static final String SUBMATRIX = "submatrix";
	private static final String DELETE_ROW_OR_COLUMN_TEXT = "delete row or column";
	private static final String DELETE_ROW_OR_COLUMN = "delete";
	private static final String AUGMENT = "augment";
	private static final String DIRECT_SUM_TEXT = "direct sum";
	private static final String DIRECT_SUM = "\u2295";
	private static final String DOT_PRODUCT_TEXT = "dot product";
	private static final String DOT_PRODUCT = "\u2022";
	private static final String NORM = "norm";

	private static final String SQUARED = "\u00B2";
	private static final String SQUARED_TEXT = "X\u00B2";
	private static final String INVERSE = "\u207B\u00B9";
	private static final String INVERSE_TEXT = "X\u207B\u00B9";

	private static final String PI = "\u03C0";
	private static final String E = "e";

	private static final String STORE_TEXT = "store";
	private static final String STORE = "\u2192";

	private static final String VARIABLE = "X"; // only used for categorizing an arbitrary variable in addFunction()
	private static final String X = "X";
	private static final String THETA = "\u03b8";
	private static final String T = "t";
	private static final String I = "i";

	private static final String UP_ARROW = "\u25B2";
	private static final String LEFT_ARROW = "\u25C0";
	private static final String DOWN_ARROW = "\u25BC";
	private static final String RIGHT_ARROW = "\u25B6";

	private static final String INSERT = "insert";
	private static final String DELETE = "delete";
	private static final String CLEAR = "clear";

	private static final String FUNCTIONS = "Y=";
	private static final String WINDOW = "WINDOW";
	private static final String QUIT = "QUIT";
	private static final String MATRIX = "MATRIX";
	private static final String GRAPH = "GRAPH";

	private static final String SECOND = "2\u207F\u1D48";
	private static final String ALPHA = "ALPHA";
	private static final String CHANGE_MODE = "MODE";

	// There can only be MAX_LINES lines visible on the display.
	// (Should be an odd number since knowledge of whether a particular
	//  string is a command or an output is based on the index's parity.)
	private static final int MAX_LINES = 5;

	// There can only be MAX_COMMANDS commands in the history,
	// excluding the active one.
	private static final int MAX_COMMANDS = 15;

	// There can only be MAX_COMMANDS_VISIBLE commands visible on the display,
	// excluding the active one.
	private static final int MAX_COMMANDS_VISIBLE = (MAX_LINES - 1) / 2;

	// Only MAX_FUNCTIONS functions can be stored at a time.
	// (This must be between 1 and 10 so that a function can be written as "Y#=",
	//  where 0 <= # <= 9.)
	private static final int MAX_FUNCTIONS = 10;

	// EPSILON is our "close enough" difference for rounding floating-point numbers.
	// It should always be a negative power of 10 for decimal rounding purposes.
	private static final double EPSILON = 0.0000000001;

	// Set up a default space between different components where necessary.
	private static final int SPACE_BETWEEN = 5;

	// Create aliases for good colors for functions.
	private static final Color RED = Color.RED;
	private static final Color ORANGE = new Color(255, 153, 0);
	private static final Color YELLOW = new Color(212, 203, 25);
	private static final Color GREEN = new Color(51, 204, 51);
	private static final Color CYAN = new Color(0, 230, 230);
	private static final Color BLUE = new Color(0, 102, 255);
	private static final Color PURPLE = new Color(153, 51, 255);
	private static final Color PINK = new Color(255, 51, 255);
	private static final Color GRAY = new Color(163, 163, 194);
	private static final Color BLACK = Color.BLACK;

	// Set up an array of possible graph colors.
	private static final Color[] COLORS = {RED,
										   ORANGE,
										   YELLOW,
										   GREEN,
										   CYAN,
									       BLUE,
									       PURPLE,
									       PINK,
							        	   GRAY,
							        	   BLACK};

	private static final String[] RECTANGULAR_PARAMETER_NAMES = {"xMin",
												    "xOrigin",
											        "xMax",
												    "xScale",
												    "yMin",
												    "yOrigin",
												    "yMax",
												    "yScale",
												    "step"};

	private static final String[] POLAR_PARAMETER_NAMES = concatenate(new String[] {THETA + "Min", THETA + "Max"}, RECTANGULAR_PARAMETER_NAMES);

	private static final String[] PARAMETRIC_PARAMETER_NAMES = concatenate(new String[] {T + "Min", T + "Max"}, RECTANGULAR_PARAMETER_NAMES);

	private static final String[] BUTTONS_USING_ANSWER = {ADDITION,
														  SUBTRACTION,
														  MULTIPLICATION,
														  DIVISION,
														  POWER,
														  SQUARED,
														  INVERSE,
														  STORE_TEXT};

	private static final String[] VARIABLES = {ANSWER, "A", "B", "C", "D", "E",
												   		   "F", "G", "H", "I", "J",
										   		   		   "K", "L", "M", "N", "O",
										   		   		   "P", "Q", "R", "S", "T",
										   		   		   "U", "V", "W", "X", "Y", "Z",
										   		   THETA, T};

	private static final String[] MATRICES = {"[A]", "[B]", "[C]", "[D]", "[E]",
											  "[F]", "[G]", "[H]", "[I]", "[J]"};

	private static final String[] VECTORS = {"[u]", "[v]", "[w]", "[x]", "[y]", "[z]"};

	private static final String[] ALL_VARIABLES = concatenate(VARIABLES, MATRICES, VECTORS);

	private static final String[] MATRIX_OPERATIONS = {DETERMINANT_TEXT,
													   TRANSPOSE_TEXT,
													   RREF_TEXT,
													   IDENTITY,
													   DOT_PRODUCT_TEXT,
													   NORM,
													   ADJOINT_TEXT,
													   REF_TEXT,
													   TRACE_TEXT,
													   AUGMENT,
													   SUBMATRIX,
													   DELETE_ROW_OR_COLUMN_TEXT,
													   DIRECT_SUM_TEXT};

	private static final String[] MATRIX_MENUS = {"MATRICES",
												  "VECTORS",
												  "OPERATIONS",
												  "EDIT"};

	private static final int MATRICES_INDEX = 0;
	private static final int VECTORS_INDEX = 1;
	private static final int OPERATIONS_INDEX = 2;
	private static final int EDIT_INDEX = 3;

	private static final String[][] MATRIX_MENU = {MATRICES,
												   VECTORS,
												   MATRIX_OPERATIONS,
												   concatenate(MATRICES, VECTORS)};

	private static final int STEPS = 1000;

	private static final double INITIAL_X_MIN = -10;
	private static final double INITIAL_X_ORIGIN = 0;
	private static final double INITIAL_X_MAX = 10;
	private static final double INITIAL_X_SCALE = 1;
	private static final double INITIAL_Y_MIN = -10;
	private static final double INITIAL_Y_ORIGIN = 0;
	private static final double INITIAL_Y_MAX = 10;
	private static final double INITIAL_Y_SCALE = 1;
	private static final double INITIAL_THETA_MIN = 0;
	private static final double INITIAL_THETA_MAX = 13;
	private static final double INITIAL_T_MIN = 0;
	private static final double INITIAL_T_MAX = 13;
	private static final double INITIAL_STEP = (INITIAL_X_MAX - INITIAL_X_MIN) / STEPS;

	private JPanel display;
	private JPanel buttonPanel;

	private String command;

	private LinkedList<String> commands;
	private LinkedList<String> outputs;
	private ArrayList<String> functions;
	private ArrayList<String> parameters;
	private ArrayList<String> rectangularParameters;
	private ArrayList<String> polarParameters;
	private ArrayList<String> parametricParameters;

	private int maxParameters;
	private String[] parameterNames;

	private ArrayList<Color> functionColors;

	private int cursorPosition;
	private int visibleCursorPosition;
	private int visibleStartIndex;
	private int visibleEndIndex;

	private int maxCharacters;

	private int firstVisible; // For calculation, function, and window modes
	private int lastVisible;
	private int firstItemVisible; // For menu system
	private int lastItemVisible;

	private int activeIndex;
	private int activeItemIndex; // Must be in [0, 35] range to use alphanumeric shortcuts
	private int activeMenuIndex;
	private int maxItemsVisible;

	private int firstRowVisible; // For matrix editing mode
	private int lastRowVisible;
	private int firstColumnVisible;
	private int lastColumnVisible;
	private int activeRowIndex;
	private int activeColumnIndex;

	// Organize all functions (e.g., sin()) in an array from greatest to smallest length
	// and combine them in one larger array, still sorted by length.
	private final String[] allFunctions;
	private final String[] multicharacterNonfunctionButtons;

	private final HashMap<String, Complex> variables;
	private final HashMap<String, Matrix> matrices;
	private final HashMap<String, Vector> vectors;
	private final HashMap<String, String> buttonMap;

	private Mode mode;
	private ButtonMode buttonMode;
	private FunctionMode functionMode;

	private String activeVariable = X;

	public GraphingCalculator() {
		super("Graphing Calculator");

		// Set the location of the window in relation to the desktop.
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screensize.width / 3;
		int height = 2 * screensize.height / 3;
		setSize(width, height);
		setLocation(0, 0);

		// Create the display where calculations and graphs show up.
		display = new Display();
		display.setPreferredSize(new Dimension(width, height / 3));

		// Create the button panel.
		buttonPanel = new ButtonPanel();
		buttonPanel.setPreferredSize(new Dimension(width, 2 * height / 3));

		// Initialize the command the user enters to the empty string.
		command = "";

		// Keep a history of the most recent commands, outputs, and functions.
		commands = new LinkedList<String>();
		outputs = new LinkedList<String>();
		functions = new ArrayList<String>(MAX_FUNCTIONS);

		// Initialize the elements of the function list to empty strings.
		for (int i = 0; i < MAX_FUNCTIONS; i++) {
			functions.add("");
		}

		// The cursor represents where the user is currently at within the active text.
		cursorPosition = 0;

		// Keep track of what the user has selected.
		activeIndex = 0;
		activeItemIndex = 0;
		activeMenuIndex = 0;

		maxItemsVisible = Math.min(MATRIX_MENU[0].length, MAX_LINES - 1);

		// Only a portion of the active text may be visible,
		// so we also record where in the visible text the cursor is
		// and what indices mark the start and end of the visible text.
		visibleCursorPosition = 0;
		visibleStartIndex = 0;
		visibleEndIndex = 0;

		// Keep track of which commands, outputs, functions, etc. are currently visible
		// since these can change when the user arrows up or down.
		firstVisible = 0; // In calculation mode, this index, together with the next one,
						  // is taken wholistically from all commands and outputs. For example,
						  // if the user has typed 1 command and hit "Enter," firstVisible will be 0,
						  // and lastVisible will be 1.
						  // When in other modes, the index is as one would expect.
		lastVisible = -1;
		firstItemVisible = 0;
		lastItemVisible = maxItemsVisible - 1;


		// Keep track of colors chosen for functions.
		List<Color> colorsList = Arrays.asList(COLORS);
		//Collections.shuffle(colorsList); // Possibly randomize color order.
		functionColors = new ArrayList<Color>(MAX_FUNCTIONS);
		for (int i = 0; i < MAX_FUNCTIONS; i++) {
			functionColors.add(colorsList.get(i));
		}

		// Keep track of the max number of typable characters in a piece of text.
		maxCharacters = 0;

		// Set up variables necessary for graphing.
		double[] rectangularParameterArray = {INITIAL_X_MIN,    // xMin:    Left border of graph
										      INITIAL_X_ORIGIN, // xOrigin: x-coordinate of intersection of axes
										      INITIAL_X_MAX,    // xMax:    Right border of graph
										      INITIAL_X_SCALE,	// xScale:  Tick mark intervals from xOrigin
										      INITIAL_Y_MIN,	// yMin:    Bottom border of graph
										      INITIAL_Y_ORIGIN, // yOrigin: y-coordinate of intersection of axes
										      INITIAL_Y_MAX,	// yMax:    Top border of graph
										      INITIAL_Y_SCALE,	// yScale:  Tick mark intervals from yOrigin
										      INITIAL_STEP};    // step:    Step size when computing function values

		double[] polarParameterArray = concatenate(new double[] {INITIAL_THETA_MIN, // thetaMin: Start angle in radians
																 INITIAL_THETA_MAX}, // thetaMax: End angle in radians
												   rectangularParameterArray);

		double[] parametricParameterArray = concatenate(new double[] {INITIAL_T_MIN, // tMin: Start value
											 						  INITIAL_T_MAX}, // tMax: End value
											 			rectangularParameterArray);

		rectangularParameters = new ArrayList<String>(rectangularParameterArray.length);
		polarParameters = new ArrayList<String>(polarParameterArray.length);
		parametricParameters = new ArrayList<String>(parametricParameterArray.length);
		for (int i = 0; i < rectangularParameterArray.length; i++) {
			rectangularParameters.add(formatNegative(roundDoubleToString(rectangularParameterArray[i])));
		}
		for (int i = 0; i < polarParameterArray.length; i++) {
			polarParameters.add(formatNegative(roundDoubleToString(polarParameterArray[i])));
		}
		for (int i = 0; i < parametricParameterArray.length; i++) {
			parametricParameters.add(formatNegative(roundDoubleToString(parametricParameterArray[i])));
		}
		parameters = rectangularParameters;
		maxParameters = parameters.size();
		parameterNames = RECTANGULAR_PARAMETER_NAMES;

		// Sort all functions and variables into arrays in descending order of length.
		allFunctions = new String[] {SUBMATRIX,
									 IDENTITY,
									 AUGMENT,
									 DELETE_ROW_OR_COLUMN,
									 INVERSE_SINE,
								     INVERSE_COSINE,
								     INVERSE_TANGENT,
								     INVERSE_COSECANT,
								     INVERSE_SECANT,
								     INVERSE_COTANGENT,
								     NORM,
								     RREF,
								     REF,
								     DETERMINANT,
								     SINE,
								     COSINE,
							    	 TANGENT,
							    	 COSECANT,
							    	 SECANT,
							    	 COTANGENT,
							    	 COMMON_LOG,
							    	 TRACE,
								     NATURAL_LOG,
								     SQUARE_ROOT};

		String[] multicharacterNonfunctionButtons0 = new String[] {INVERSE,
														 		   ANSWER,
														 		   ADJOINT};
		multicharacterNonfunctionButtons = concatenate(multicharacterNonfunctionButtons0, MATRICES, VECTORS);

		Comparator<String> descendingLengthComparator = new DescendingLengthComparator();
		Arrays.sort(allFunctions, descendingLengthComparator);
		Arrays.sort(multicharacterNonfunctionButtons, descendingLengthComparator);

		// This will hold a record of our variables for translation purposes.
		variables = new HashMap<String, Complex>();
		matrices = new HashMap<String, Matrix>();
		vectors = new HashMap<String, Vector>();
		for (String v : VARIABLES) {
			variables.put(v, Complex.ZERO); // Initialize to 0.
		}
		for (String m : MATRICES) {
			matrices.put(m, new Matrix()); // Initialize to a 1 x 1 zero matrix.
		}
		for (String v : VECTORS) {
			vectors.put(v, new Vector()); // Initialize to a 1 x 1 zero vector.
		}

		// This will hold a record of what strings should change form
		// from initial appearance to what appears in a command.
		buttonMap = new HashMap<String, String>();
		buttonMap.put(SQUARED_TEXT, SQUARED);
		buttonMap.put(INVERSE_TEXT, INVERSE);
		buttonMap.put(DETERMINANT_TEXT, DETERMINANT);
		buttonMap.put(TRANSPOSE_TEXT, TRANSPOSE);
		buttonMap.put(TRACE_TEXT, TRACE);
		buttonMap.put(REF_TEXT, REF);
		buttonMap.put(RREF_TEXT, RREF);
		buttonMap.put(ADJOINT_TEXT, ADJOINT);
		buttonMap.put(DELETE_ROW_OR_COLUMN_TEXT, DELETE_ROW_OR_COLUMN);
		buttonMap.put(DOT_PRODUCT_TEXT, DOT_PRODUCT);
		buttonMap.put(DIRECT_SUM_TEXT, DIRECT_SUM);

		// Initialize the calculator modes.
		mode = Mode.CALCULATION;
		buttonMode = ButtonMode.REGULAR;
		functionMode = FunctionMode.RECTANGULAR;

		// Create a box to hold the display and button panel with some space in between and around.
		Box box = Box.createVerticalBox();
		box.add(display);
		box.add(Box.createVerticalStrut(10));
		box.add(buttonPanel);
		box.setBorder(BorderFactory.createEmptyBorder(SPACE_BETWEEN, SPACE_BETWEEN,
												 	  SPACE_BETWEEN, SPACE_BETWEEN));
		
		// Add the box to the window.
		add(box);

		// Set the default close operation to quitting the program.
		// (Argument can be changed to JFrame.DISPOSE_ON_CLOSE if desired.)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}

	private static enum Mode {
		FUNCTION,
		WINDOW,
		CALCULATION,
		MATRIX_MENUS,
		MATRIX_EDIT,
		GRAPH
	}

	private static enum ButtonMode {
		REGULAR,
		SECONDARY,
		ALPHA
	}

	private static enum FunctionMode {
		RECTANGULAR,
		POLAR,
		PARAMETRIC
	}

	private static enum Number {
		SCALAR,
		VECTOR,
		MATRIX
	}

	private static class DomainException extends Exception {
		public DomainException() {
			super();
		}
	}

	private class DescendingLengthComparator implements Comparator<String> {
		
		// Sorts strings in descending order of length.
		public int compare(String s1, String s2) {
			return s2.length() - s1.length();
		}
	}

	private String getActiveText() {
		switch (mode) {
			case CALCULATION:
				return command;
			case FUNCTION:
				return functions.get(activeIndex);
			case WINDOW:
				return parameters.get(activeIndex);
		}
		return "";
	}

	private void setActiveText(String s) {
		switch (mode) {
			case CALCULATION:
				command = s;
				break;
			case FUNCTION:
				functions.set(activeIndex, s);
				break;
			case WINDOW:
				parameters.set(activeIndex, s);
		}
	}

	private List<String> getList() {
		switch (mode) {
			case CALCULATION:
				return commands;
			case FUNCTION:
				return functions;
			case WINDOW:
				return parameters;
		}
		return null;
	}

	private class Display extends JPanel {

		// Create an offset for text vertical location so that nothing is offscreen.
		private final int OFFSET = 5;

		private final Color BACKGROUND_COLOR = new Color(213, 226, 213);
		private final Color TEXT_COLOR = BLACK;
		private final String FONT_NAME = "Monospaced";

		// TIMER_DELAY measures how often the cursor flashes in milliseconds.
		private final int TIMER_DELAY = 500;

		// TICK_MARK_SCALE measures how many tick marks fit into
		// the smaller of the width and height of the display.
		private final int TICK_MARK_SCALE = 20;

		// MIN_THICKNESS measures the minimum thickness of a pixel when graphing functions.
		private final int MIN_THICKNESS = 2;

		private int fontHeight;
		private boolean cursorVisible = true;
		private double tickMarkScale;
		private int tickMarkLength;

		public Display() {
			super();
			setBackground(BACKGROUND_COLOR);

			// Create a timer for the cursor and start it.
			Timer cursorTimer = new Timer(TIMER_DELAY, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					switch (mode) {
						case CALCULATION:
						case FUNCTION:
						case WINDOW:
						case MATRIX_EDIT:

							// Alternate the visibility of the cursor every TIMER_DELAY milliseconds.
							cursorVisible = !cursorVisible;
							repaint();
					}
				}
			});
			cursorTimer.start();
		}

		private void paintText(Graphics g, int width, int height, FontMetrics metrics) {

			// Let x and y represent coordinates for our first piece of text.
			// (They'll be used for the other text as well.)
			int x = 0;
			int y = height / MAX_LINES - OFFSET;

			// Set specifics for the font.
			int ascent = metrics.getAscent();
			int descent = metrics.getDescent();

			String s = getActiveText();
			int length = s.length();
			List<String> list = getList();
			ListIterator<String> iterator = list.listIterator();
			ListIterator<String> outputIterator = outputs.listIterator();

			// Set up variables to record the active piece of text.
			int activeY = 0;
			String activeDependentVariable = ""; // e.g., "Y", "r", or "X"
			Color activeColor = null;

			// Draw each piece of text that is visible except the active text.

			// Go to the first visible piece of text if we're in calculation mode.
			if (mode == Mode.CALCULATION) {
				for (int i = 0; i < firstVisible; i++) {
					if (i % 2 == 0) {
						iterator.next();
					}
					else {
						outputIterator.next();
					}
				}
			}
			for (int i = firstVisible; i <= lastVisible; i++) {
				if (mode == Mode.CALCULATION) {
					if (i % 2 == 0) {
						s = iterator.next();
						x = 0;
					}
					else {
						s = outputIterator.next();
						x = width - metrics.stringWidth(s);
					}
				}
				else {
					s = list.get(i);
				}
				Color color = TEXT_COLOR;
				if (i == activeIndex) {
					activeY = y;
					switch (mode) {
						case CALCULATION:
							g.fillRect(x, y - ascent, metrics.stringWidth(s), ascent + descent);
							color = Color.WHITE;
							break;
						case FUNCTION:
							switch (functionMode) {
								case RECTANGULAR:
									activeColor = functionColors.get(i);
									activeDependentVariable = "Y";
									break;
								case POLAR:
									activeColor = functionColors.get(i);
									activeDependentVariable = "r";
									break;
								case PARAMETRIC:
									activeColor = functionColors.get(i / 2);
									if (i % 2 == 0) {
										activeDependentVariable = "X";
									}
									else {
										activeDependentVariable = "Y";
									}
							}
					}				
				}
				switch (mode) {
					case FUNCTION:
						switch (functionMode) {
							case RECTANGULAR:
								color = functionColors.get(i);
								s = "Y" + i + "=" + s;
								break;
							case POLAR:
								color = functionColors.get(i);
								s = "r" + i + "=" + s;
								break;
							case PARAMETRIC:
								color = functionColors.get(i / 2);
								if (i % 2 == 0) {
									s = "X" + i / 2 + "=" + s;
								}
								else {
									s = "Y" + i / 2 + "=" + s;
								}
						}
						break;
					case WINDOW:
						s = parameterNames[i] + "=" + s;
				}
				g.setColor(color);
				g.drawString(s, x, y);
				y += height / MAX_LINES;
			}
			
			// If the active text is visible,
			if (mode != Mode.CALCULATION || 2 * commands.size() - firstVisible <= 2 * MAX_COMMANDS_VISIBLE) {

				// Add arrows on the left and/or right of the text to indicate that some parts are invisible.
				String activeText = getActiveText();
				String visibleText = "";
				if (visibleStartIndex > 0) {
					if (visibleEndIndex < length) {
						System.out.println("F");
						visibleText = LEFT_ARROW
										 + activeText.substring(visibleStartIndex + 1, visibleEndIndex)
										 + RIGHT_ARROW;
					}
					else {
						System.out.println("G");
						visibleText = LEFT_ARROW
										 + activeText.substring(visibleStartIndex + 1);
					}
				}
				else {
					if (visibleEndIndex < length) {
						System.out.println("H");
						visibleText = activeText.substring(0, visibleEndIndex)
										 + RIGHT_ARROW;
					}
					else {
						//System.out.println("I");
						visibleText = activeText;
					}
				}
				String prefix = "";
				switch (mode) {
					case CALCULATION:
						activeY = y;
						break;
					case FUNCTION:
						switch (functionMode) {
							case RECTANGULAR:
							case POLAR:
								prefix = activeDependentVariable + activeIndex + "=";
								break;
							case PARAMETRIC:
								prefix = activeDependentVariable + activeIndex / 2 + "=";
						}
						break;
					case WINDOW:
						prefix = parameterNames[activeIndex] + "=";
				}
				g.setColor(activeColor);
				g.drawString(prefix + visibleText, 0, activeY);

				// If the cursor is currently visible,
				if (cursorVisible) {

					// If the last output is selected and the command isn't visible, return.
					if (mode == Mode.CALCULATION && activeIndex != 2 * commands.size()) {
						return;
					}

					// Get its location on the display using the visible command.
					int locationOfCursor = metrics.stringWidth(prefix + visibleText.substring(0, visibleCursorPosition));

					// Let characterCoveredUp be the character covered up by the cursor or an arbitrary character if none is.
					String characterCoveredUp;
					if (visibleCursorPosition < visibleText.length()) {
						characterCoveredUp = "" + visibleText.charAt(visibleCursorPosition);
					}
					else {
						characterCoveredUp = ONE;
					}

					// Draw the cursor (a rectangle) on the display so that it covers up the appropriate character.
					g.setColor(TEXT_COLOR);
					g.fillRect(locationOfCursor, activeY - ascent, metrics.stringWidth(characterCoveredUp), ascent + descent);
				}
			}
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			int width = getWidth();
			int height = getHeight();

			// Get the max number of characters that can be displayed in a line.
			int fontHeight = height / MAX_LINES - OFFSET;
			Font font = new Font(FONT_NAME, Font.BOLD, fontHeight);
			FontMetrics metrics = g.getFontMetrics(font);
			g.setFont(font);
			g.setColor(TEXT_COLOR);

			String s = "";
			int characterWidth = metrics.stringWidth(" ");
			for (int tempWidth = characterWidth; tempWidth <= width; tempWidth += characterWidth) {
				s += " ";
			}
			maxCharacters = s.length();
			//updateVisibleIndices();
			//System.out.println("New max characters = " + maxCharacters);
			//statusUpdate();

			switch (mode) {
				case CALCULATION:
				case FUNCTION:
				case WINDOW:
					paintText(g, width, height, metrics);
					break;
				case MATRIX_MENUS:
					paintMatrixMenus(g, width, height, metrics);
					break;
				case MATRIX_EDIT:
					paintMatrixEdit(g, width, height, metrics);
					break;
				case GRAPH:
					paintGraphs(g, width, height);
			}
		}

		private void paintMatrixMenus(Graphics g, int width, int height, FontMetrics metrics) {
			
			// Let y represent the y-coordinate for the headers.
			// (It'll be used for everything else as well.)
			int y = height / MAX_LINES - OFFSET;

			// Set specifics for the font for everything.
			int ascent = metrics.getAscent();
			int descent = metrics.getDescent();
			int characterWidth = metrics.stringWidth(" ");

			// Draw the categories.
			int x = 0;
			String s = "";
			for (int i = 0; i < MATRIX_MENUS.length; i++) {
				s = MATRIX_MENUS[i];
				int sWidth = metrics.stringWidth(s);
				if (i == activeMenuIndex) {
					g.fillRect(x, y - ascent, sWidth, ascent + descent);
					g.setColor(Color.WHITE);
					g.drawString(s, x, y);
					g.setColor(TEXT_COLOR);
				}
				else {
					g.drawString(s, x, y);
				}
				x += sWidth + characterWidth;
			}

			// Draw each item.
			for (int i = firstItemVisible; i <= lastItemVisible; i++) {
				y += height / MAX_LINES;
				String item = MATRIX_MENU[activeMenuIndex][i];
				if (i < 10) {
					
					// Prepend digit.
					s = "" + i;
				}
				else {
					
					// Prepend letter.
					s = VARIABLES[i - 9];
				}
				if (i == activeItemIndex) {
					g.fillRect(0, y - ascent, 2 * characterWidth, ascent + descent);
					g.setColor(Color.WHITE);
					g.drawString(s + ":", 0, y);
					g.setColor(TEXT_COLOR);
					g.drawString(item, 2 * characterWidth, y);
				}
				else {
					g.drawString(s + ":" + item, 0, y);
				}
			}
		}

		private void paintMatrixEdit(Graphics g, int width, int height, FontMetrics metrics) {
			
			// Let x and y represent coordinates for our first piece of text.
			// (They'll be used for the other text as well.)
			int x = 0;
			int y = height / MAX_LINES - OFFSET;

			// Set specifics for the font.
			int ascent = metrics.getAscent();
			int descent = metrics.getDescent();

			String s = getActiveText();
			int length = s.length();
			List<String> list = getList();
			ListIterator<String> iterator = list.listIterator();
			ListIterator<String> outputIterator = outputs.listIterator();

			// Set up variables to record the active piece of text.
			int activeY = 0;
			String activeDependentVariable = ""; // e.g., "Y", "r", or "X"
			Color activeColor = null;
		}

		private void paintGraphs(Graphics g, int width, int height) {
			double[] parsedParameters = new double[maxParameters];
			try {
				for (int i = 0; i < maxParameters; i++) {
					parsedParameters[i] = Double.parseDouble(parseNegative(parameters.get(i)));
				}
			}
			catch (NumberFormatException ex) {
				printGraphError(g, width, height);
				return;
			}
			int start = (functionMode == FunctionMode.RECTANGULAR ? 0 : 2);
			double xMin    = parsedParameters[start + 0];
			double xOrigin = parsedParameters[start + 1];
			double xMax    = parsedParameters[start + 2];
			double xScale  = parsedParameters[start + 3];
			double yMin    = parsedParameters[start + 4];
			double yOrigin = parsedParameters[start + 5];
			double yMax    = parsedParameters[start + 6];
			double yScale  = parsedParameters[start + 7];
			double step    = parsedParameters[start + 8];

			double digitalWidth = xMax - xMin;
			double digitalHeight = yMax - yMin;
			double digitalXOrigin = xOrigin - xMin;
			double digitalYOrigin = yMax - yOrigin;
			int actualXOrigin = (int) (width * digitalXOrigin / digitalWidth);
			int actualYOrigin = (int) (height * digitalYOrigin / digitalHeight);
			double actualStep = width * step / digitalWidth;

			tickMarkScale = (xMax - xMin) / xScale;
			tickMarkLength = (int) Math.min(width / tickMarkScale, height / tickMarkScale);

			// Paint axes.
			g.setColor(TEXT_COLOR);
			g.drawLine(0, actualYOrigin, width, actualYOrigin);  // x-axis
			g.drawLine(actualXOrigin, 0, actualXOrigin, height); // y-axis

			// Paint the tick marks
			for (double x = actualXOrigin; x - width <= EPSILON; x += width * xScale / digitalWidth) {
				
				// Paint tick marks on right half of x-axis
				if (x > actualXOrigin) {
					g.drawLine((int) x, actualYOrigin - tickMarkLength / 2, (int) x, actualYOrigin + tickMarkLength / 2);
				}
			}
			for (double x = actualXOrigin; -x <= EPSILON; x -= width * xScale / digitalWidth) {
				
				// Paint tick marks on left half of x-axis
				if (x < actualXOrigin) {
					g.drawLine((int) x, actualYOrigin - tickMarkLength / 2, (int) x, actualYOrigin + tickMarkLength / 2);
				}
			}
			for (double y = actualYOrigin; -y <= EPSILON; y -= height * yScale / digitalHeight) {
				
				// Paint tick marks on top half of y-axis
				if (y < actualYOrigin) {
					g.drawLine(actualXOrigin - tickMarkLength / 2, (int) y, actualXOrigin + tickMarkLength / 2, (int) y);
				}
			}
			for (double y = actualYOrigin; y - height <= EPSILON; y += height * yScale / digitalHeight) {
				
				// Paint tick marks on bottom half of y-axis
				if (y > actualYOrigin) {
					g.drawLine(actualXOrigin - tickMarkLength / 2, (int) y, actualXOrigin + tickMarkLength / 2, (int) y);
				}
			}

			Complex oldVariable = variables.get(activeVariable);

			switch (functionMode) {
				case RECTANGULAR:
					for (int i = 0; i < functions.size(); i++) {
						String f = functions.get(i);
						if (!f.isEmpty()) {
							System.out.println(f);
							g.setColor(functionColors.get(i));
							double previousActualX = 0.0;
							double previousActualY = 0.0;
							boolean startGraphing = false;
							for (double x = xMin; x < xMax; x += step) {
								variables.put(activeVariable, roundComplex(x));
								String yString = evaluate(f);
								switch (yString) {
									case ERROR_MESSAGE:
									case DIMENSION_ERROR_MESSAGE:
										printGraphError(g, width, height);
										return;
									case DOMAIN_ERROR_MESSAGE:
										previousActualX += actualStep;
										startGraphing = false;
										continue;
								}
								Complex yComplex = Complex.parseComplex(yString);
								if (!yComplex.isReal()) {
									previousActualX += actualStep;
									startGraphing = false;
									continue;
								}
								double y = Double.parseDouble(yString);
								double actualY = height * (yMax - y) / digitalHeight;
								if (startGraphing) {
									if (actualY > 0 && actualY < height || previousActualY > 0 && previousActualY < height) {
										double dy = previousActualY - actualY; // Opposite of expected since vertical orientation is flipped.
										if (dy > 0) { // Increasing
											System.out.println("A");
											g.fillRect((int) previousActualX, (int) actualY,
													   Math.max(MIN_THICKNESS, (int) actualStep), Math.max(MIN_THICKNESS, (int) dy));
											System.out.println("Just drew a line from ("
															   + (int) previousActualX + ", "
															   + (int) actualY + ") to ("
															   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
															   + ((int) actualY + Math.max(MIN_THICKNESS, (int) dy)) + ")");
										}
										else if (dy < 0) { // Decreasing
											System.out.println("B");
											g.fillRect((int) previousActualX, (int) previousActualY,
													   Math.max(MIN_THICKNESS, (int) actualStep), Math.max(MIN_THICKNESS, (int) -dy));
											System.out.println("Just drew a line from ("
															   + (int) previousActualX + ", "
															   + (int) previousActualY + ") to ("
															   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
															   + ((int) previousActualY + Math.max(MIN_THICKNESS, (int) -dy)) + ")");
										}
										else { // Constant
											System.out.println("C");
											g.fillRect((int) previousActualX, (int) actualY - MIN_THICKNESS / 2,
													   Math.max(MIN_THICKNESS, (int) actualStep), MIN_THICKNESS);
											System.out.println("Just drew a line from ("
															   + (int) previousActualX + ", "
															   + ((int) actualY - MIN_THICKNESS / 2) + ") to ("
															   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
															   + ((int) actualY + MIN_THICKNESS / 2) + ")");
										}
									}
									previousActualX += actualStep;
								}
								previousActualY = actualY;
								startGraphing = true;
							}
						}
					}
					break;
				case POLAR:
					double thetaMin = parsedParameters[0];
					double thetaMax = parsedParameters[1];
					for (int i = 0; i < functions.size(); i++) {
						String f = functions.get(i);
						if (!f.isEmpty()) {
							System.out.println(f);
							g.setColor(functionColors.get(i));
							double previousActualX = 0.0;
							double previousActualY = 0.0;
							boolean startGraphing = false;
							for (double theta = thetaMin; theta < thetaMax; theta += step) {
								variables.put(activeVariable, roundComplex(theta));
								String rString = evaluate(f);
								switch (rString) {
									case ERROR_MESSAGE:
									case DIMENSION_ERROR_MESSAGE:
										printGraphError(g, width, height);
										return;
									case DOMAIN_ERROR_MESSAGE:
										startGraphing = false;
										continue;
								}
								Complex rComplex = Complex.parseComplex(rString);
								if (!rComplex.isReal()) {
									startGraphing = false;
									continue;
								}
								double r = Double.parseDouble(rString);
								double x = r * Math.cos(theta);
								double y = r * Math.sin(theta);
								double actualX = width * (x - xMin) / digitalWidth;
								double actualY = height * (yMax - y) / digitalHeight;
								if (startGraphing) {
									if ((actualY > 0 && actualY < height || previousActualY > 0 && previousActualY < height) &&
										(actualX > 0 && actualX < width || previousActualX > 0 && previousActualX < width)) {
										double dx = actualX - previousActualX;
										double dy = previousActualY - actualY;
										if (dx > 0) {
											if (dy > 0) {
												g.fillRect((int) previousActualX, (int) actualY,
														   Math.max(MIN_THICKNESS, (int) dx), Math.max(MIN_THICKNESS, (int) dy));
												//System.out.println("Just drew a line from ("
												//				   + (int) previousActualX + ", "
												//				   + (int) actualY + ") to ("
												//				   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
												//				   + ((int) actualY + Math.max(MIN_THICKNESS, (int) dy)) + ")");
											}
											else if (dy < 0) {
												g.fillRect((int) previousActualX, (int) previousActualY,
														   Math.max(MIN_THICKNESS, (int) dx), Math.max(MIN_THICKNESS, (int) -dy));
												//System.out.println("Just drew a line from ("
												//				   + (int) previousActualX + ", "
												//				   + (int) previousActualY + ") to ("
												//				   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
												//				   + ((int) previousActualY + Math.max(MIN_THICKNESS, (int) -dy)) + ")");
											}
											else {
												g.fillRect((int) previousActualX, (int) actualY - MIN_THICKNESS / 2,
														   Math.max(MIN_THICKNESS, (int) dx), MIN_THICKNESS);
												//System.out.println("Just drew a line from ("
												//				   + (int) previousActualX + ", "
												//				   + ((int) actualY - MIN_THICKNESS / 2) + ") to ("
												//				   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
												//				   + ((int) actualY + MIN_THICKNESS / 2) + ")");
											}
										}
										else if (dx < 0) {
											if (dy > 0) {
												g.fillRect((int) actualX, (int) actualY,
														   Math.max(MIN_THICKNESS, (int) -dx), Math.max(MIN_THICKNESS, (int) dy));
												//System.out.println("Just drew a line from ("
												//				   + (int) previousActualX + ", "
												//				   + (int) actualY + ") to ("
												//				   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
												//				   + ((int) actualY + Math.max(MIN_THICKNESS, (int) dy)) + ")");
											}
											else if (dy < 0) {
												g.fillRect((int) actualX, (int) previousActualY,
														   Math.max(MIN_THICKNESS, (int) -dx), Math.max(MIN_THICKNESS, (int) -dy));
												//System.out.println("Just drew a line from ("
												//				   + (int) previousActualX + ", "
												//				   + (int) previousActualY + ") to ("
												//				   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
												//				   + ((int) previousActualY + Math.max(MIN_THICKNESS, (int) -dy)) + ")");
											}
											else {
												g.fillRect((int) actualX, (int) actualY - MIN_THICKNESS / 2,
														   Math.max(MIN_THICKNESS, (int) -dx), MIN_THICKNESS);
												//System.out.println("Just drew a line from ("
												//				   + (int) previousActualX + ", "
												//				   + ((int) actualY - MIN_THICKNESS / 2) + ") to ("
												//				   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
												//				   + ((int) actualY + MIN_THICKNESS / 2) + ")");
											}
										}
										else {
											if (dy > 0) {
												g.fillRect((int) actualX - MIN_THICKNESS / 2, (int) actualY,
														   MIN_THICKNESS, Math.max(MIN_THICKNESS, (int) dy));
												//System.out.println("Just drew a line from ("
												//				   + (int) previousActualX + ", "
												//				   + (int) actualY + ") to ("
												//				   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
												//				   + ((int) actualY + Math.max(MIN_THICKNESS, (int) dy)) + ")");
											}
											else if (dy < 0) {
												g.fillRect((int) actualX - MIN_THICKNESS / 2, (int) previousActualY,
														   MIN_THICKNESS, Math.max(MIN_THICKNESS, (int) -dy));
												//System.out.println("Just drew a line from ("
												//				   + (int) previousActualX + ", "
												//				   + (int) previousActualY + ") to ("
												//				   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
												//				   + ((int) previousActualY + Math.max(MIN_THICKNESS, (int) -dy)) + ")");
											}
											else {
												g.fillRect((int) actualX - MIN_THICKNESS / 2, (int) actualY - MIN_THICKNESS / 2,
														   MIN_THICKNESS, MIN_THICKNESS);
												//System.out.println("Just drew a line from ("
												//				   + (int) previousActualX + ", "
												//				   + ((int) actualY - MIN_THICKNESS / 2) + ") to ("
												//				   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
												//				   + ((int) actualY + MIN_THICKNESS / 2) + ")");
											}
										}
									}
								}
								previousActualX = actualX;
								previousActualY = actualY;
								startGraphing = true;
							}
						}
					}
					break;
				case PARAMETRIC:
					double tMin = parsedParameters[0];
					double tMax = parsedParameters[1];
					for (int i = 0; i < functions.size(); i += 2) {
						String fx = functions.get(i);
						String fy = functions.get(i + 1);
						if (!fx.isEmpty() && !fy.isEmpty()) {
							System.out.println("X(" + T + ") = " + fx);
							System.out.println("Y(" + T + ") = " + fy);
							g.setColor(functionColors.get(i / 2));
							double previousActualX = 0.0;
							double previousActualY = 0.0;
							boolean startGraphing = false;
							for (double t = tMin; t < tMax; t += step) {
								variables.put(activeVariable, roundComplex(t));
								String xString = evaluate(fx);
								String yString = evaluate(fy);
								switch (xString) {
									case ERROR_MESSAGE:
									case DIMENSION_ERROR_MESSAGE:
										printGraphError(g, width, height);
										return;
									case DOMAIN_ERROR_MESSAGE:
										startGraphing = false;
										continue;
								}
								switch (yString) {
									case ERROR_MESSAGE:
									case DIMENSION_ERROR_MESSAGE:
										printGraphError(g, width, height);
										return;
									case DOMAIN_ERROR_MESSAGE:
										startGraphing = false;
										continue;
								}
								Complex xComplex = Complex.parseComplex(xString);
								Complex yComplex = Complex.parseComplex(yString);
								if (!xComplex.isReal() || !yComplex.isReal()) {
									startGraphing = false;
									continue;
								}
								double x = Double.parseDouble(xString);
								double y = Double.parseDouble(yString);
								double actualX = width * (x - xMin) / digitalWidth;
								double actualY = height * (yMax - y) / digitalHeight;
								if (startGraphing) {
									if ((actualY > 0 && actualY < height || previousActualY > 0 && previousActualY < height) &&
										(actualX > 0 && actualX < width || previousActualX > 0 && previousActualX < width)) {
										double dx = actualX - previousActualX;
										double dy = previousActualY - actualY;
										if (dx > 0) {
											if (dy > 0) {
												g.fillRect((int) previousActualX, (int) actualY,
														   Math.max(MIN_THICKNESS, (int) dx), Math.max(MIN_THICKNESS, (int) dy));
												//System.out.println("Just drew a line from ("
												//				   + (int) previousActualX + ", "
												//				   + (int) actualY + ") to ("
												//				   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
												//				   + ((int) actualY + Math.max(MIN_THICKNESS, (int) dy)) + ")");
											}
											else if (dy < 0) {
												g.fillRect((int) previousActualX, (int) previousActualY,
														   Math.max(MIN_THICKNESS, (int) dx), Math.max(MIN_THICKNESS, (int) -dy));
												//System.out.println("Just drew a line from ("
												//				   + (int) previousActualX + ", "
												//				   + (int) previousActualY + ") to ("
												//				   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
												//				   + ((int) previousActualY + Math.max(MIN_THICKNESS, (int) -dy)) + ")");
											}
											else {
												g.fillRect((int) previousActualX, (int) actualY - MIN_THICKNESS / 2,
														   Math.max(MIN_THICKNESS, (int) dx), MIN_THICKNESS);
												//System.out.println("Just drew a line from ("
												//				   + (int) previousActualX + ", "
												//				   + ((int) actualY - MIN_THICKNESS / 2) + ") to ("
												//				   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
												//				   + ((int) actualY + MIN_THICKNESS / 2) + ")");
											}
										}
										else if (dx < 0) {
											if (dy > 0) {
												g.fillRect((int) actualX, (int) actualY,
														   Math.max(MIN_THICKNESS, (int) -dx), Math.max(MIN_THICKNESS, (int) dy));
												//System.out.println("Just drew a line from ("
												//				   + (int) previousActualX + ", "
												//				   + (int) actualY + ") to ("
												//				   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
												//				   + ((int) actualY + Math.max(MIN_THICKNESS, (int) dy)) + ")");
											}
											else if (dy < 0) {
												g.fillRect((int) actualX, (int) previousActualY,
														   Math.max(MIN_THICKNESS, (int) -dx), Math.max(MIN_THICKNESS, (int) -dy));
												//System.out.println("Just drew a line from ("
												//				   + (int) previousActualX + ", "
												//				   + (int) previousActualY + ") to ("
												//				   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
												//				   + ((int) previousActualY + Math.max(MIN_THICKNESS, (int) -dy)) + ")");
											}
											else {
												g.fillRect((int) actualX, (int) actualY - MIN_THICKNESS / 2,
														   Math.max(MIN_THICKNESS, (int) -dx), MIN_THICKNESS);
												//System.out.println("Just drew a line from ("
												//				   + (int) previousActualX + ", "
												//				   + ((int) actualY - MIN_THICKNESS / 2) + ") to ("
												//				   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
												//				   + ((int) actualY + MIN_THICKNESS / 2) + ")");
											}
										}
										else {
											if (dy > 0) {
												g.fillRect((int) actualX - MIN_THICKNESS / 2, (int) actualY,
														   MIN_THICKNESS, Math.max(MIN_THICKNESS, (int) dy));
												//System.out.println("Just drew a line from ("
												//				   + (int) previousActualX + ", "
												//				   + (int) actualY + ") to ("
												//				   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
												//				   + ((int) actualY + Math.max(MIN_THICKNESS, (int) dy)) + ")");
											}
											else if (dy < 0) {
												g.fillRect((int) actualX - MIN_THICKNESS / 2, (int) previousActualY,
														   MIN_THICKNESS, Math.max(MIN_THICKNESS, (int) -dy));
												//System.out.println("Just drew a line from ("
												//				   + (int) previousActualX + ", "
												//				   + (int) previousActualY + ") to ("
												//				   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
												//				   + ((int) previousActualY + Math.max(MIN_THICKNESS, (int) -dy)) + ")");
											}
											else {
												g.fillRect((int) actualX - MIN_THICKNESS / 2, (int) actualY - MIN_THICKNESS / 2,
														   MIN_THICKNESS, MIN_THICKNESS);
												//System.out.println("Just drew a line from ("
												//				   + (int) previousActualX + ", "
												//				   + ((int) actualY - MIN_THICKNESS / 2) + ") to ("
												//				   + ((int) previousActualX + Math.max(MIN_THICKNESS, (int) actualStep)) + ", "
												//				   + ((int) actualY + MIN_THICKNESS / 2) + ")");
											}
										}
									}
								}
								previousActualX = actualX;
								previousActualY = actualY;
								startGraphing = true;
							}
						}
					}
			}

			variables.put(activeVariable, oldVariable);
		}

		private void printGraphError(Graphics g, int width, int height) {
			super.paintComponent(g);
			g.setColor(TEXT_COLOR);
			g.setFont(new Font(FONT_NAME, Font.BOLD, height / 2));
			printStringInCenter(g, ERROR_MESSAGE, width / 2, height / 2);
		}

		public void printStringInCenter(Graphics g, String s, int x, int y) {
			FontMetrics metrics = g.getFontMetrics(g.getFont());
			int stringWidth = metrics.stringWidth(s);
			int ascent = metrics.getAscent();
			g.drawString(s, x - stringWidth / 2, y + ascent / 2);
		}
	}

	private int maxCharacters() {
		switch (mode) {
			case CALCULATION:

				// Exclude space for cursor.
				return maxCharacters - 1;
			case FUNCTION:

				// Exclude space for cursor and "[DEPENDENT_VARIABLE]#=".
				return maxCharacters - 4;
			case WINDOW:

				// Exclude space for cursor and "[parameter]=".
				return maxCharacters - parameterNames[activeIndex].length() - 2;
			case MATRIX_MENUS:

				// Exclude space for cursor and "#:".
				return maxCharacters - 3;
		}
		return 0;
	}

	private void statusUpdate() {
		String activeText = getActiveText();
		int length = activeText.length();
		System.out.println("    Mode: " + mode);
		System.out.println("    Length: " + length);
		System.out.println("    Visible Cursor Position: " + visibleCursorPosition);
		System.out.println("    Visible Start Index: " + visibleStartIndex);
		System.out.println("    Visible End Index: " + visibleEndIndex);
		System.out.println("    First Visible: " + firstVisible);
		System.out.println("    Last Visible: " + lastVisible);
		System.out.println("    Active Index: " + activeIndex);
		System.out.println("    Active Text: " + activeText + "\n");
	}

	private void setVariables() {
		cursorPosition = getActiveText().length();
		visibleCursorPosition = Math.min(cursorPosition, maxCharacters());
		visibleStartIndex = Math.max(0, cursorPosition - maxCharacters());
		visibleEndIndex = cursorPosition;
	}

	private void updateVisibleIndices() {
		int extraRoom = visibleEndIndex - visibleStartIndex - maxCharacters();
		if (extraRoom == 0) {
			return;
		}
		if (extraRoom > 0) {
			
		}
		visibleStartIndex = Math.max(0, cursorPosition - maxCharacters());
		visibleEndIndex = Math.min(getActiveText().length(), maxCharacters());
		Math.min(visibleStartIndex + maxCharacters(), cursorPosition);
		visibleEndIndex = cursorPosition;
		visibleCursorPosition = cursorPosition - visibleStartIndex;
	}

	private class ButtonPanel extends JPanel {
		private static final int BUTTONS_WIDE = 5;
		private static final int BUTTONS_TALL = 9;
		private static final String BUTTON_FONT_NAME = "SansSerif";

		// This is the color used for when a button is pressed and active, like insert.
		private final Color PRESSED_COLOR = Color.GREEN;

		// Set particular colors for special buttons.
		private final Color MODE_COLOR = PURPLE;
		private final Color SECONDARY_COLOR = new Color(63, 111, 166);
		private final Color ALPHA_COLOR = new Color(75, 189, 83);

		private final int ARROW_PANEL_ROW = 1;
		private final int ARROW_PANEL_COLUMN = 4;
		private final int INSERT_ROW = 1;
		private final int INSERT_COLUMN = 3;
		private final int SECONDARY_ROW = 1;
		private final int SECONDARY_COLUMN = 0;
		private final int ALPHA_ROW = 2;
		private final int ALPHA_COLUMN = 0;
		private final int VARIABLE_ROW = 1;
		private final int VARIABLE_COLUMN = 1;

		// NOTE: Matrix functionality not yet fully implemented.
		//       When complete, the button will be added in Row 1, Column 4.

		private String[][] buttonNames = {
			{FUNCTIONS,    WINDOW,         QUIT,             "",            GRAPH},
			{SECOND,       activeVariable, CHANGE_MODE,      DELETE,            ""},
			{ALPHA,        COSECANT,       SECANT,           COTANGENT,         CLEAR},
			{PI,           SINE,           COSINE,           TANGENT,           POWER},
			{INVERSE_TEXT, I,              OPEN_PARENTHESIS, CLOSE_PARENTHESIS, DIVISION},
			{SQUARED_TEXT, SEVEN,          EIGHT,            NINE,              MULTIPLICATION},
			{NATURAL_LOG,  FOUR,           FIVE,             SIX,               SUBTRACTION},
			{COMMON_LOG,   ONE,            TWO,              THREE,             ADDITION},
			{STORE_TEXT,   ZERO,           DECIMAL_POINT,    NEGATIVE_TEXT,          ENTER}
		};
		private String[][] secondaryButtonNames = {
			{FUNCTIONS,    WINDOW,           QUIT,             "",            GRAPH},
			{SECOND,       activeVariable,   CHANGE_MODE,      INSERT,            ""},
			{ALPHA,        INVERSE_COSECANT, INVERSE_SECANT,   INVERSE_COTANGENT, CLEAR},
			{E,            INVERSE_SINE,     INVERSE_COSINE,   INVERSE_TANGENT,   POWER},
			{INVERSE_TEXT, I,                OPEN_PARENTHESIS, CLOSE_PARENTHESIS, DIVISION},
			{SQUARE_ROOT,  SEVEN,            EIGHT,            NINE,              MULTIPLICATION},
			{NATURAL_LOG,  FOUR,             FIVE,             SIX,               SUBTRACTION},
			{COMMON_LOG,   ONE,              TWO,              THREE,             ADDITION},
			{STORE_TEXT,   ZERO,             DECIMAL_POINT,    ANSWER,            ENTRY}
		};
		private String[][] alphaButtonNames = {
			{FUNCTIONS,     	WINDOW,         	QUIT,             	"",             GRAPH},
			{SECOND,        	activeVariable, 	CHANGE_MODE,      	DELETE,             ""},
			{ALPHA,         	COSECANT,       	SECANT,       	  	COTANGENT,          CLEAR},
			{VARIABLES[1],  	VARIABLES[2],   	VARIABLES[3],	  	VARIABLES[4],  		VARIABLES[5]},
			{VARIABLES[6],  	VARIABLES[7],   	VARIABLES[8],	  	VARIABLES[9],  		VARIABLES[10]},
			{VARIABLES[11], 	VARIABLES[12],  	VARIABLES[13], 	  	VARIABLES[14], 		VARIABLES[15]},
			{VARIABLES[16], 	VARIABLES[17],  	VARIABLES[18], 		VARIABLES[19], 		VARIABLES[20]},
			{VARIABLES[21], 	VARIABLES[22],  	VARIABLES[23], 		VARIABLES[24], 		VARIABLES[25]},
			{VARIABLES[26], 	ZERO,           	DECIMAL_POINT,    	ANSWER,             ENTRY}
		};
		private JButton[][] buttons;
		private ArrowPanel arrowPanel;
		private ActionListener listener;

		// This is our font for regular buttons.
		private Font regularFont;

		// This is our font for the arrow panel.
		private Font arrowFont;

		// This measures whether we are currently inserting or not.
		private boolean insertMode;

		// This button is included in buttons[][], but we provide a more explicit reference
		// to it to refer to it for color changes when we are inserting.
		private JButton insertButton;

		// This button is included in buttons[][], but we provide a more explicit reference
		// to it to refer to it for color changes when we are inserting.
		private JButton secondaryButton;

		// This button is included in buttons[][], but we provide a more explicit reference
		// to it to refer to it for color changes when we are inserting.
		private JButton alphaButton;

		public ButtonPanel() {
			super();

			// Create the buttons array and a listener to handle when buttons are pressed
			buttons = new JButton[BUTTONS_TALL][BUTTONS_WIDE];
			listener = new ButtonHandler();

			// Create the separate arrow panel since it is not just one button.
			arrowPanel = new ArrowPanel();

			// We will start in normal mode.
			insertMode = false;

			// Set up a grid layout for the button panel.
			setLayout(new GridLayout(BUTTONS_TALL, BUTTONS_WIDE, SPACE_BETWEEN, SPACE_BETWEEN));

			// Set the fonts for the buttons.
			int regularFontHeight = getHeight() / (3 * BUTTONS_TALL);
			int arrowFontHeight = regularFontHeight / 2;
			regularFont = new Font(BUTTON_FONT_NAME, Font.PLAIN, regularFontHeight);
			arrowFont = new Font(BUTTON_FONT_NAME, Font.BOLD, arrowFontHeight);

			// Add the buttons to the panel.
			for (int i = 0; i < BUTTONS_TALL; i++) {
				for (int j = 0; j < BUTTONS_WIDE; j++) {

					// Add the arrow panel separately.
					if (i == ARROW_PANEL_ROW && j == ARROW_PANEL_COLUMN) {
						add(arrowPanel);
					}
					else {
						buttons[i][j] = new JButton(buttonNames[i][j]);
						buttons[i][j].setFont(regularFont);
						if (i == 0) {
							buttons[i][j].setForeground(MODE_COLOR);
						}
						buttons[i][j].setBackground(PRESSED_COLOR);
						buttons[i][j].addActionListener(listener);
						add(buttons[i][j]);
					}
				}
			}

			// Initialize the insert and secondary buttons as what they should be.
			insertButton = buttons[INSERT_ROW][INSERT_COLUMN];
			secondaryButton = buttons[SECONDARY_ROW][SECONDARY_COLUMN];
			secondaryButton.setForeground(SECONDARY_COLOR);
			alphaButton = buttons[ALPHA_ROW][ALPHA_COLUMN];
			alphaButton.setForeground(ALPHA_COLOR);
		}

		private class ArrowPanel extends JPanel {
			private JButton[] arrowButtons;
			private final int ARROWS = 4;
			private final String[] arrowNames = {
				LEFT_ARROW,
				UP_ARROW,
				DOWN_ARROW,
				RIGHT_ARROW
			};

			public ArrowPanel() {
				super();

				// Set up layout as LEFT_ARROW | [panel] | RIGHT_ARROW,
				// where [panel] has the up and down arrows.
				setLayout(new GridLayout(1, 3, 0, 0));
				arrowButtons = new JButton[ARROWS];

				// Add each arrow button to the arrowButtons[] array.
				for (int i = 0; i < ARROWS; i++) {
					arrowButtons[i] = new JButton(arrowNames[i]);
					arrowButtons[i].setFont(arrowFont);
					arrowButtons[i].addActionListener(listener);
				}

				// Add the left arrow to the container.
				add(arrowButtons[0]);

				// Create and add the [panel] as specified before that will
				// contain the up and down arrows.
				JPanel middlePanel = new JPanel();
				middlePanel.setLayout(new GridLayout(2, 1, 0, 0));
				middlePanel.add(arrowButtons[1]);
				middlePanel.add(arrowButtons[2]);
				add(middlePanel);

				// Add the right arrow.
				add(arrowButtons[3]);
			}

			// Concisely set the font of all arrow buttons when necessary.
			public void setTheFont(Font f) {
				for (JButton button : arrowButtons) {
					button.setFont(f);
				}
			}
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			// Set the fonts of all buttons (since they should change when the window size changes).
			int regularFontHeight = getHeight() / (3 * BUTTONS_TALL);
			int arrowFontHeight = regularFontHeight / 2;
			regularFont = new Font(BUTTON_FONT_NAME, Font.PLAIN, regularFontHeight);
			arrowFont = new Font(BUTTON_FONT_NAME, Font.BOLD, arrowFontHeight);

			for (int i = 0; i < BUTTONS_TALL; i++) {
				for (int j = 0; j < BUTTONS_WIDE; j++) {
					if (i == ARROW_PANEL_ROW && j == ARROW_PANEL_COLUMN) {
						arrowPanel.setTheFont(arrowFont);
					}
					else {
						buttons[i][j].setFont(regularFont);
					}
				}
			}
		}

		private void insertSet(boolean insert) {
			insertMode = insert;
			insertButton.setOpaque(insert);
		}

		private void buttonReset() {
			buttonSet(ButtonMode.REGULAR);
		}

		private void buttonSet(ButtonMode newMode) {
			if (buttonMode == newMode) {
				return;
			}
			forceButtonSet(newMode);
		}

		private void forceButtonSet(ButtonMode newMode) {
			buttonMode = newMode;
			secondaryButton.setOpaque(buttonMode == ButtonMode.SECONDARY);
			alphaButton.setOpaque(buttonMode == ButtonMode.ALPHA);
			for (int i = 0; i < BUTTONS_TALL; i++) {
				for (int j = 0; j < BUTTONS_WIDE; j++) {
					if (i == ARROW_PANEL_ROW && j == ARROW_PANEL_COLUMN) {
						continue;
					}
					String newText = "";
					switch (buttonMode) {
						case REGULAR:
							newText = buttonNames[i][j];
							break;
						case SECONDARY:
							newText = secondaryButtonNames[i][j];
							break;
						case ALPHA:
							newText = alphaButtonNames[i][j];
					}
					buttons[i][j].setText(newText);
				}
			}
		}

		private class ButtonHandler implements ActionListener {
			
			// Keep track of which command should appear when the user presses ENTRY.
			private int entryIndex;

			// Keep track of which mode the user just exited.
			private Mode previousMode;

			public void actionPerformed(ActionEvent e) {
				String buttonName = e.getActionCommand();
				switch (buttonName) {
					case SECOND:
						if (buttonMode == ButtonMode.SECONDARY) {
							buttonReset();
						}
						else {
							buttonSet(ButtonMode.SECONDARY);
						}
						return;
					case ALPHA:
						if (buttonMode == ButtonMode.ALPHA) {
							buttonReset();
						}
						else {
							buttonSet(ButtonMode.ALPHA);
						}
						return;
					case CHANGE_MODE:
						switch (functionMode) {
							case RECTANGULAR:
								functionMode = FunctionMode.POLAR;
								break;
							case POLAR:
								functionMode = FunctionMode.PARAMETRIC;
								break;
							case PARAMETRIC:
								functionMode = FunctionMode.RECTANGULAR;
								break;
						}
						updateFunctionMode();
						statusUpdate();
						display.repaint();
						return;
				}
				buttonReset();
				Mode newMode = null;
				switch (buttonName) {
					case FUNCTIONS:
						newMode = Mode.FUNCTION;
						break;
					case WINDOW:
						newMode = Mode.WINDOW;
						break;
					case QUIT:
						newMode = Mode.CALCULATION;
						break;
					case MATRIX:
						newMode = Mode.MATRIX_MENUS;
						break;
					case GRAPH:
						newMode = Mode.GRAPH;
						break;
				}
				if (newMode != null) {
					previousMode = mode;
					mode = newMode;
					if (previousMode == Mode.WINDOW) {
						evaluateImmediately();
					}
					entryIndex = commands.size() - 1;
					if (mode != Mode.MATRIX_MENUS) {
						insertSet(false);
					}
					resetVariables();
					statusUpdate();
					display.repaint();
					return;
				}
				String text = "";
				boolean checkPrevious = false;
				boolean delete = false;
				boolean shift = false;
				switch (mode) {
					case MATRIX_MENUS:
						matrixMenuAction(buttonName);
						statusUpdate();
						display.repaint();
						return;
					case CALCULATION:
					case FUNCTION:
					case WINDOW:
						String answer;
						switch (buttonName) {
							case ENTER:
								switch (mode) {
									case CALCULATION:
										if (activeIndex != 2 * commands.size()) {
											if (activeIndex % 2 == 1) {
												command = outputs.get(activeIndex / 2);
												if (isError(command)) {
													command = "";
												}
											}
											else {
												command = commands.get(activeIndex / 2);
											}
											resetVariables();
											insertSet(false);
											statusUpdate();
											display.repaint();
											return;
										}
										if (command.equals("")) {
											return;
										}
										addCommand(command);
										entryIndex = commands.size() - 1;
										answer = evaluate();
										addOutput(formatNegative(answer));
										if (!isError(answer)) {
											variables.put(ANSWER, Complex.parseComplex(answer));
										}
										command = "";
										resetVariables();
										break;
									default:
										goDown(true);
								}
								insertSet(false);
								statusUpdate();
								display.repaint();
								return;
							case CLEAR:
								setActiveText("");
								setVariables();
								insertSet(false);
								statusUpdate();
								display.repaint();
								return;
							case INSERT:
								insertSet(!insertMode);
								return;
							case ENTRY:
								if (!commands.isEmpty()) {
									String entry = commands.get(entryIndex);
									switch (mode) {
										case CALCULATION:
											command = entry;
											activeIndex = 2 * commands.size();
											break;
										case FUNCTION:
											functions.set(activeIndex, entry);
											break;
										case WINDOW:
											parameters.set(activeIndex, entry);
									}
									setVariables();
									if (entryIndex > 0) {
										entryIndex--;
									}
								}
								insertSet(false);
								display.repaint();
								return;
							case UP_ARROW:
								goDown(false);
								insertSet(false);
								statusUpdate();
								display.repaint();
								return;
							case DOWN_ARROW:
								goDown(true);
								insertSet(false);
								statusUpdate();
								display.repaint();
								return;
							case LEFT_ARROW:
								checkPrevious = true;
							case RIGHT_ARROW:
								shift = true;
								break;
							case DELETE:
								checkPrevious = true;
								delete = true;
								shift = true;
								break;
							case NEGATIVE_TEXT:
								text = NEGATIVE;
								break;
							case SQUARED_TEXT:
								text = SQUARED;
								break;
							case INVERSE_TEXT:
								text = INVERSE;
								break;
							case STORE_TEXT:
								text = STORE;
								break;
							default:
								String prefix = "";
								String suffix = "";
								for (String f : allFunctions) {
									if (f.equals(buttonName)) {
										suffix = OPEN_PARENTHESIS;
										break;
									}
								}
								if (mode == Mode.CALCULATION && command.isEmpty()) {
									for (String b : BUTTONS_USING_ANSWER) {
										if (b.equals(buttonName)) {
											prefix = ANSWER;
											break;
										}
									}
								}
								switch (buttonName) {
									case MULTIPLICATION:
										text = prefix + ASTERISK + suffix;
										break;
									case DIVISION:
										text = prefix + SLASH + suffix;
										break;
									default:
										text = prefix + buttonName + suffix;
								}
						}
						if (shift) {
							shiftText(checkPrevious, delete);
							insertSet(false);
						}
						else {
							addText(text);
						}
				}
				statusUpdate();
				display.repaint();
			}

			private void evaluateImmediately() {
				String answer = evaluate(getActiveText());
				if (!answer.isEmpty()) {
			 		if (isError(answer) || !Complex.parseComplex(answer).isReal()) {
			 			answer = "";
			 		}
			 		setActiveText(formatNegative(answer));
			 	}
			}

			private void goDown(boolean down) {
				int maxIndex = 0;
				switch (mode) {
					case CALCULATION:
						maxIndex = 2 * commands.size();
						break;
					case FUNCTION:
						maxIndex = MAX_FUNCTIONS - 1;
						break;
					case WINDOW:
						maxIndex = maxParameters - 1;
						evaluateImmediately();
				}
				if (activeIndex == (down ? maxIndex : 0)) {
					setVariables();
					return;
				}
				activeIndex += (down ? 1 : -1);
				setVariables();
				switch (mode) {
					case CALCULATION:
						if (down) {
							if (activeIndex > lastVisible) {
								if (activeIndex == 2 * commands.size()) {
									if (lastVisible - firstVisible == 2 * MAX_COMMANDS_VISIBLE) {
										firstVisible++;
									}
								}
								else {
									lastVisible++;
									if (lastVisible - firstVisible > 2 * MAX_COMMANDS_VISIBLE) {
										firstVisible++;
									}
								}
							}
						}
						else {
							if (activeIndex < firstVisible) {
								firstVisible--;
								if (lastVisible - firstVisible > 2 * MAX_COMMANDS_VISIBLE) {
									lastVisible--;
								}
							}
						}
						break;
					default:
						if (down) {
							if (activeIndex > lastVisible) {
								firstVisible++;
								lastVisible++;
							}
						}
						else {
							if (activeIndex < firstVisible) {
								firstVisible--;
								lastVisible--;
							}
						}
				}
			}

			private void resetVariables() {
				switch (mode) {
					case CALCULATION:
						activeIndex = 2 * commands.size();
						firstVisible = 2 * Math.max(0, commands.size() - MAX_COMMANDS_VISIBLE);
						lastVisible = 2 * commands.size() - 1;
						break;
					case MATRIX_MENUS:
					case MATRIX_EDIT:
						return;
					default:
						activeIndex = 0;
						firstVisible = 0;
						lastVisible = MAX_LINES - 1;
				}
				setVariables();
			}

			private String transformToCorrectVariables(String s) {
				String output = "";
				for (int i = 0; i < s.length(); i++) {
					String character = s.charAt(i) + "";
					String newCharacter = "";
					switch (character) {
						case X:
						case THETA:
						case T:
							newCharacter = activeVariable;
							break;
						default:
							newCharacter = character;
					}
					output += newCharacter;
				}
				return output;
			}

			private void updateFunctionMode() {
				switch (functionMode) {
					case RECTANGULAR:
						activeVariable = X;
						break;
					case POLAR:
						activeVariable = THETA;
						break;
					case PARAMETRIC:
						activeVariable = T;
				}
				parameters.set(activeIndex, transformToCorrectVariables(parameters.get(activeIndex)));
				switch (functionMode) {
					case RECTANGULAR:
						parameters = rectangularParameters;
						parameterNames = RECTANGULAR_PARAMETER_NAMES;
						break;
					case POLAR:
						parameters = polarParameters;
						parameterNames = POLAR_PARAMETER_NAMES;
						break;
					case PARAMETRIC:
						parameters = parametricParameters;
						parameterNames = PARAMETRIC_PARAMETER_NAMES;
				}
				maxParameters = parameters.size();
				resetVariables();
				for (int i = 0; i < commands.size(); i++) {
					commands.set(i, transformToCorrectVariables(commands.get(i)));
				}
				for (int i = 0; i < functions.size(); i++) {
					functions.set(i, transformToCorrectVariables(functions.get(i)));
				}
				command = transformToCorrectVariables(command);
				buttonNames[VARIABLE_ROW][VARIABLE_COLUMN] = activeVariable;
				secondaryButtonNames[VARIABLE_ROW][VARIABLE_COLUMN] = activeVariable;
				alphaButtonNames[VARIABLE_ROW][VARIABLE_COLUMN] = activeVariable;
				forceButtonSet(buttonMode);
			}

			private void matrixMenuAction(String buttonName) {
				boolean reset = false;
				switch (buttonName) {
					case ENTER:
						reset = enterSelectedText(activeItemIndex);
						break;
					case UP_ARROW:
						if (activeItemIndex > 0) {
							activeItemIndex--;
							if (activeItemIndex < firstItemVisible) {
								firstItemVisible--;
								lastItemVisible--;
							}
						}
						break;
					case DOWN_ARROW:
						if (activeItemIndex < MATRIX_MENU[activeMenuIndex].length - 1) {
							activeItemIndex++;
							if (activeItemIndex > lastItemVisible) {
								firstItemVisible++;
								lastItemVisible++;
							}
						}
						break;
					case LEFT_ARROW:
						reset = true;
						activeMenuIndex--;
						if (activeMenuIndex == -1) {
							activeMenuIndex = MATRIX_MENUS.length - 1;
						}
						break;
					case RIGHT_ARROW:
						reset = true;
						activeMenuIndex++;
						if (activeMenuIndex == MATRIX_MENUS.length) {
							activeMenuIndex = 0;
						}
						break;
					default:
						boolean automaticSelection = false;
						int i;
						for (i = 0; i < MATRIX_MENU[activeMenuIndex].length; i++) {
							String shortcut = "";
							if (i < 10) {
								shortcut = "" + i;
							}
							else {
								shortcut = VARIABLES[i - 9];
							}
							if (buttonName.equals(shortcut)) {
								automaticSelection = true;
								break;
							}
						}
						if (automaticSelection) {
							enterSelectedText(i);
						}
				}
				if (reset) {
					activeItemIndex = 0;
					maxItemsVisible = Math.min(MAX_LINES - 1, MATRIX_MENU[activeMenuIndex].length);
					firstItemVisible = 0;
					lastItemVisible = maxItemsVisible - 1;
				}
			}

			/*
			 * @return whether we should reset the menu system.
			 */

			private boolean enterSelectedText(int itemIndex) {
				String text = "";
				switch (activeMenuIndex) {
					case OPERATIONS_INDEX: // OPERATIONS
						text = buttonMap.get(MATRIX_MENU[activeMenuIndex][itemIndex]);
						for (String f : allFunctions) {
							if (f.equals(text)) {
								text += OPEN_PARENTHESIS;
								break;
							}
						}
					case MATRICES_INDEX: // MATRICES
					case VECTORS_INDEX: // VECTORS
						if (text.isEmpty()) {
							text = MATRIX_MENU[activeMenuIndex][itemIndex];
						}
						mode = previousMode;
						addText(text);
						break;
					case EDIT_INDEX: // EDIT
						mode = Mode.MATRIX_EDIT;
						activeMenuIndex = 0;
						return true;
				}
				return false;
			}

			private void adjustVisibleIndices() {
				if (visibleCursorPosition <= 0) {
					int difference = Math.min(maxCharacters(), visibleStartIndex - cursorPosition);
					visibleCursorPosition += difference;
					visibleStartIndex -= difference;
					visibleEndIndex -= difference;
				}
				else if (visibleCursorPosition > maxCharacters()) {
					visibleStartIndex += visibleCursorPosition - maxCharacters();
					visibleEndIndex += visibleCursorPosition - maxCharacters();
					visibleCursorPosition = maxCharacters();
				}
			}

			private void shiftText(boolean checkPrevious, boolean delete) {
				String text = OPEN_PARENTHESIS;
				String string = getActiveText();
				int length = string.length();
				if (checkPrevious && cursorPosition == 0 ||
					!checkPrevious && cursorPosition == length) {
					return;
				}
				for (String f : allFunctions) {
					int offset = (checkPrevious ? f.length() + 1 : 0);
					int start = cursorPosition - offset;
					int end = start + f.length();
					if (start >= 0 && end < length && string.substring(start, end).equals(f)) {
						text = f + OPEN_PARENTHESIS;
						break;
					}
				}
				for (String m : multicharacterNonfunctionButtons) {
					int offset = (checkPrevious ? m.length() : 0);
					int start = cursorPosition - offset;
					int end = start + m.length();
					if (start >= 0 && end <= length && string.substring(start, end).equals(m)) {
						text = m;
						break;
					}
				}
				int textLength = text.length();
				if (delete) {
					String newString = string.substring(0, cursorPosition - textLength)
							  	  	   + string.substring(cursorPosition);
					setActiveText(newString);
					cursorPosition -= textLength;
					visibleCursorPosition += Math.min(0, visibleStartIndex - textLength);
					visibleStartIndex = Math.max(0, visibleStartIndex - textLength);
					visibleEndIndex = Math.min(maxCharacters(), length - textLength);
				}
				else if (checkPrevious) { // left arrow
					cursorPosition -= textLength;
					visibleCursorPosition -= textLength;
				}
				else { // right arrow
					cursorPosition += textLength;
					visibleCursorPosition += textLength;
				}
				adjustVisibleIndices();
			}

			private void addText(String text) {
				int textLength = text.length();
				String string = getActiveText();
				int length = string.length();
				String newString = string.substring(0, cursorPosition) + text;
				if (insertMode) {
					newString += string.substring(cursorPosition);
				}
				else if (cursorPosition < length) {
					
					// We are replacing old text with what has just been pressed.
					String oldText = OPEN_PARENTHESIS;
					int start = cursorPosition;
					for (String f : allFunctions) {
						int end = start + f.length();
						if (end < length && string.substring(start, end).equals(f)) {
							oldText = f + OPEN_PARENTHESIS;
							break;
						}
					}
					for (String m : multicharacterNonfunctionButtons) {
						int end = start + m.length();
						if (end < length && string.substring(start, end).equals(m)) {
							oldText = m;
							break;
						}
					}
					newString += string.substring(cursorPosition + oldText.length());
				}
				int newLength = newString.length();
				setActiveText(newString);
				cursorPosition += textLength;
				visibleCursorPosition += textLength;
				if (cursorPosition < length) {
					if (insertMode) {
						if (visibleCursorPosition > maxCharacters() || newLength < maxCharacters()) {
							visibleEndIndex += textLength;
						}
					}
					else {
						if (length <= maxCharacters()) {
							if (newLength <= maxCharacters()) {
								visibleEndIndex += newLength - length;
							}
							else {
								visibleCursorPosition = maxCharacters();
								visibleStartIndex = newLength - maxCharacters();
								visibleEndIndex = newLength;
							}
						}
						else if (newLength <= maxCharacters()) {
							visibleCursorPosition += visibleStartIndex;
							visibleStartIndex = 0;
							visibleEndIndex = newLength;
						}
						else if (visibleCursorPosition > maxCharacters()) {
							visibleEndIndex = visibleStartIndex + visibleCursorPosition;
							visibleStartIndex = visibleEndIndex - maxCharacters();
							visibleCursorPosition = maxCharacters();
						}
					}
				}
				else {
					visibleEndIndex += textLength;
				}
				int tooFar = visibleEndIndex - (visibleStartIndex + maxCharacters());
				if (tooFar > 0) {
					visibleStartIndex += tooFar;
					visibleCursorPosition = maxCharacters();
				}
			}

			private void addCommand(String s) {
				if (commands.size() == MAX_COMMANDS) {
					commands.removeFirst();
				}
				commands.addLast(s);
			}

			private void addOutput(String s) {
				if (outputs.size() == MAX_COMMANDS) {
					outputs.removeFirst();
				}
				outputs.addLast(s);
			}
		}
	}

	private String parseNegative(String s) {
		if (s.isEmpty()) {
			return s;
		}
		if (s.substring(0, 1).equals(NEGATIVE)) {
			return "-" + s.substring(1);
		}
		return s;
	}

	private String formatNegative(String s) {
		if (s.isEmpty()) {
			return s;
		}
		if (s.substring(0, 1).equals("-")) {
			return NEGATIVE + s.substring(1);
		}
		return s;
	}

	private class Node {
		private String value;
		private Complex number;
		private Vector vector;
		private Matrix matrix;
		private boolean isNumerical;
		private Node previous;
		private Node next;
		private String function;

		public Node(String value) {
			this(value, null, null, null, false, null, null, "");
		}

		public Node(String value, String function) {
			this(value, null, null, null, false, null, null, function);
		}

		public Node(String value, Node previous, Node next) {
			this(value, null, null, null, false, previous, next, "");
		}

		public Node(Complex number) {
			this("" + number, number, null, null, true, null, null, "");
		}

		public Node(Complex number, Node previous, Node next) {
			this("" + number, number, null, null, true, previous, next, "");
		}

		public Node(Vector vector) {
			this("" + vector, null, vector, null, true, null, null, "");
		}

		public Node(Vector vector, Node previous, Node next) {
			this("" + vector, null, vector, null, true, previous, next, "");
		}

		public Node(Matrix matrix) {
			this("" + matrix, null, null, matrix, true, null, null, "");
		}

		public Node(Matrix matrix, Node previous, Node next) {
			this("" + matrix, null, null, matrix, true, previous, next, "");
		}

		public Node(String value, Complex number, Vector vector, Matrix matrix, boolean isNumerical, Node previous, Node next, String function) {
			this.value = value;
			this.number = number;
			this.vector = vector;
			this.matrix = matrix;
			this.isNumerical = isNumerical;
			this.previous = previous;
			if (previous != null) {
				previous.setNext(this);
			}
			this.next = next;
			if (next != null) {
				next.setPrevious(this);
			}
			this.function = function;
		}

		public String getValue() {
			return value;
		}

		public Complex getNumber() {
			return number;
		}

		public boolean isNumerical() {
			return isNumerical;
		}

		public boolean isScalar() {
			return number != null;
		}

		public boolean isVector() {
			return vector != null;
		}

		public boolean isMatrix() {
			return matrix != null;
		}

		public boolean isNumericalOrNegative() {
			return isNumerical || NEGATIVE.equals(value);
		}

		public boolean isNegative() {
			return NEGATIVE.equals(value);
		}

		public Node getNext() {
			return next;
		}

		public Node getPrevious() {
			return previous;
		}

		public void setValue(String value) {
			this.value = value;
			this.number = null;
			this.isNumerical = false;
		}

		public void setNumerical(boolean isNumerical) {
			this.isNumerical = isNumerical;
		}

		public void setNext(Node next) {
			this.next = next;
		}

		public void setPrevious(Node previous) {
			this.previous = previous;
		}

		public void setNumber(Complex number) {
			this.value = "" + number;
			this.number = number;
			this.isNumerical = true;
		}

		public String getFunction() {
			return function;
		}

		public String toString() {
			return function + value;
		}
	}

	private class Expression {
		private Node head;
		private Node tail;
		private int size;

		public Expression() {
			this(null, null, 0);
		}

		public Expression(Node node) {
			this(node, node, 1);
		}

		public Expression(Node head, Node tail, int size) {
			this.head = head;
			this.tail = tail;
			this.size = size;
		}

		public Node getHead() {
			return head;
		}

		public Node getTail() {
			return tail;
		}

		public int size() {
			return size;
		}

		public void initialize(Node node) {
			head = node;
			head.setPrevious(null);
			head.setNext(null);
			tail = node;
			tail.setPrevious(null);
			tail.setNext(null);
			size++;
		}

		public void addFirst(Node node) {
			if (isEmpty()) {
				initialize(node);
			}
			else {
				node.setNext(head);
				head.setPrevious(node);
				head = node;
				size++;
			}
		}

		public void addFirst(String value) {
			addFirst(new Node(value));
		}

		public void addFirst(double number) {
			addFirst(new Node(new Complex(number)));
		}

		public void addFirst(Complex number) {
			addFirst(new Node(number));
		}

		public void addFirst(Vector vector) {
			addFirst(new Node(vector));
		}

		public void addFirst(Matrix matrix) {
			addFirst(new Node(matrix));
		}

		public void addLast(Node node) {
			if (isEmpty()) {
				initialize(node);
			}
			else {
				node.setPrevious(tail);
				tail.setNext(node);
				tail = node;
				size++;
			}
		}

		public void addLast(String value) {
			addLast(new Node(value));
		}

		public void addLast(String value, String function) {
			addLast(new Node(value, function));
		}

		public void addLast(double number) {
			addLast(new Node(new Complex(number)));
		}

		public void addLast(Complex number) {
			addLast(new Node(number));
		}

		public void addLast(Vector vector) {
			addLast(new Node(vector));
		}

		public void addLast(Matrix matrix) {
			addLast(new Node(matrix));
		}

		public Node removeFirst() {
			Node node = head;
			head = node.getNext();
			head.setPrevious(null);
			node.setPrevious(null);
			node.setNext(null);
			size--;
			return node;
		}

		public Node removeLast() {
			Node node = tail;
			tail = node.getPrevious();
			tail.setNext(null);
			node.setPrevious(null);
			node.setNext(null);
			size--;
			return node;
		}

		// Here node1 is in the list.
		public void addBefore(Node node1, Node node2) {
			if (head == node1) {
				head = node2;
				node2.setNext(node1);
				node1.setPrevious(node2);
			}
			else {
				Node previousNode = node1.getPrevious();
				previousNode.setNext(node2);
				node2.setNext(node1);
				node1.setPrevious(node2);
				node2.setPrevious(previousNode);
			}
			size++;
		}

		// Here node1 is in the list.
		public void addAfter(Node node1, Node node2) {
			if (tail == node1) {
				tail = node2;
				node1.setNext(node2);
				node2.setPrevious(node1);
			}
			else {
				Node nextNode = node1.getPrevious();
				node1.setNext(node2);
				node2.setNext(nextNode);
				nextNode.setPrevious(node2);
				node2.setPrevious(node1);
			}
			size++;
		}

		public boolean isEmpty() {
			return size == 0;
		}

		public String toString() {
			String output = "";
			for (Node node = head; node != null; node = node.getNext()) {
				output += "  " + node + "\n";
			}
			return output;
		}
	}

	public String evaluate() {
		return evaluate(command);
	}

	public String evaluate(String s) {
		System.out.println(s);
		s += END;
		int start = -1;
		int end = -1;
		boolean isNumerical = false;
		boolean storing = false;
		String variable = "";
		String characterBefore = "";
		Expression expression = new Expression();
		for (int i = 0; i < s.length(); i++) {
			String character = s.charAt(i) + "";
			String function = "";
			switch (character) {
				case ONE:
				case TWO:
				case THREE:
				case FOUR:
				case FIVE:
				case SIX:
				case SEVEN:
				case EIGHT:
				case NINE:
				case ZERO:
				case DECIMAL_POINT:
					if (isNumerical) {
						end++;
					}
					else {
						start = i;
						end = i + 1;
					}
					isNumerical = true;
					function = null;
					break;
				case NEGATIVE:
					if (s.length() > i + 1 && s.substring(i, i + 2).equals(INVERSE)) {
						function = INVERSE;
						i++;
						break;
					}
					switch (characterBefore) {
						case ZERO:
						case ONE:
						case TWO:
						case THREE:
						case FOUR:
						case FIVE:
						case SIX:
						case SEVEN:
						case EIGHT:
						case NINE:
						case DECIMAL_POINT:
							return ERROR_MESSAGE;
						case CLOSE_PARENTHESIS:
							expression.addLast(MULTIPLICATION);
					}
					expression.addLast(NEGATIVE);
					function = null;
					break;
				case ASTERISK:
					function = MULTIPLICATION;
					break;
				case SLASH:
					function = DIVISION;
					break;
				case STORE:
					if (mode == Mode.GRAPH) {
						return ERROR_MESSAGE;
					}
					for (String v : ALL_VARIABLES) {
						int startIndex = i + 1;
						int endIndex = startIndex + v.length();
						if (s.length() == endIndex + 1 && s.substring(startIndex, endIndex).equals(v)) {
							if (v.equals(ANSWER)) {
								return ERROR_MESSAGE;
							}
							function = null;
							storing = true;
							variable = v;
							i += v.length();
							break;
						}
					}

					// If command does not end in "[STORE][VARIABLE][END]",
					if (variable == "") { 
						return ERROR_MESSAGE;
					}
					break;
				default:
					for (String f : allFunctions) {
						int startIndex = i;
						int endIndex = startIndex + f.length();
						if (endIndex < s.length() && s.substring(startIndex, endIndex).equals(f)) {
							function = f;
							i += f.length();
							break;
						}
					}
					if (function.isEmpty()) {
						for (String m : multicharacterNonfunctionButtons) {
							int startIndex = i;
							int endIndex = startIndex + m.length();
							if (endIndex <= s.length() && s.substring(startIndex, endIndex).equals(m)) {
								function = m;
								i += m.length() - 1;
								break;
							}
						}
					}
					if (function.isEmpty()) {
						function = character;
					}
			}
			if (function != null) {
				try {
					addFunction(s, function, expression, start, end, isNumerical);
				}
				catch (NumberFormatException ex) {
					return ERROR_MESSAGE;
				}
				isNumerical = false;
			}
			characterBefore = character;
		}
		System.out.print("Expression:\n" + expression);
		if (expression.isEmpty()) {
			return "";
		}
		try {

			// Close all parentheses if the user did not.
			int parenthesesToAdd = 0;
			for (Node node = expression.getHead(); node != null; node = node.getNext()) {
				switch (node.getValue()) {
					case OPEN_PARENTHESIS:
						parenthesesToAdd++;
						break;
					case CLOSE_PARENTHESIS:
						parenthesesToAdd--;
				}
			}
			for (int i = 0; i < parenthesesToAdd; i++) {
				expression.addLast(CLOSE_PARENTHESIS);
			}

			Complex answer = evaluate(expression);
			print("Answer: " + answer);
			String roundedAnswer = roundComplexToString(answer);
			print("Rounded Answer: " + roundedAnswer);
			if (storing) {
				variables.put(variable, roundComplex(answer));
			}
			return roundedAnswer;
		}
		catch (ArithmeticException ex) {
			return ERROR_MESSAGE;
		}
		catch (DomainException ex) {
			return DOMAIN_ERROR_MESSAGE;
		}
	}

	public static double roundDouble(double d) {
		if (d == (int) d) {
			if (d == -0.0) {
				d = 0.0;
			}
			return (int) d;
		}
		int decimalPlacesToCheck = 0;
		double epsilon = EPSILON;
		while (epsilon < 1) {
			decimalPlacesToCheck++;
			epsilon *= 10;
		}
		double epsilonInverse = 1;
		for (int i = 1; i <= decimalPlacesToCheck; i++) {
			epsilon /= 10;
			epsilonInverse *= 10;
			double scaledNumber = epsilonInverse * d;
			double roundedScaledNumber = Math.rint(scaledNumber);
			if (Math.abs(roundedScaledNumber - scaledNumber) < EPSILON) {
				double answer = roundedScaledNumber * epsilon;
				if ((int) answer == answer) {
					return (int) answer;
				}
				return Double.parseDouble(String.format("%." + i + "f", answer));
			}
		}
		return d;
	}

	public static Complex roundComplex(double d) {
		return new Complex(roundDouble(d));
	}

	public static Complex roundComplex(Complex z) {
		return new Complex(roundDouble(z.getRealPart()), roundDouble(z.getImaginaryPart()));
	}

	private String roundDoubleToString(double d) {
		double roundedD = roundDouble(d);
		if (roundedD == (int) roundedD) {
			return "" + (int) roundedD;
		}
		return "" + roundedD;
	}

	private String roundComplexToString(Complex z) {
		return roundComplex(z).toString();
	}

	private void addFunction(String function, Expression expression, int start, int end, boolean isNumerical) throws NumberFormatException {
		addFunction(command, function, expression, start, end, isNumerical);
	}

	private void addFunction(String s, String function, Expression expression, int start, int end, boolean isNumerical) throws NumberFormatException {
		if (isNumerical) {
			print(s.substring(start, end));
			double number = Double.parseDouble(s.substring(start, end));
			if (!expression.isEmpty()) {
				if (expression.getTail().getValue().equals(CLOSE_PARENTHESIS) ||
					expression.getTail().isNumerical()) {
					
					// For expressions like ")#"  and "[VARIABLE]#"
					// so that they become  ")*#" and "[VARIABLE]*#".
					expression.addLast(MULTIPLICATION);
				}
			}
			expression.addLast(number);
		}
		boolean addMultiplication = false;
		boolean extraFunction = false;
		boolean isVariable = false;
		String variable = "";
		Number number = isVariable(function);
		if (number != null) {
			variable = function;
			isVariable = true;
			function = VARIABLE;
		}
		switch (function) {
			case END:
				return;
			case CLOSE_PARENTHESIS:
				break;
			case SQUARED:
			case INVERSE:
				expression.addLast(POWER);
				expression.addLast(OPEN_PARENTHESIS);
				expression.addLast(function.equals(SQUARED) ? 2.0 : -1.0);
				expression.addLast(CLOSE_PARENTHESIS);
				return;
			case OPEN_PARENTHESIS:
			case PI:
			case E:
			case I:
			case VARIABLE:

				// For expressions like "#(",  "#[VARIABLE]",  ")(",  and ")[VARIABLE]"
				// so that they become  "#*(", "#*[VARIABLE]", ")*(", and ")*[VARIABLE]".
				if (expression.isEmpty()) {
					break;
				}
				addMultiplication = expression.getTail().getValue().equals(CLOSE_PARENTHESIS) ||
									expression.getTail().isNumerical();
				break;
			default:

				// For expressions like "#f(x)"  and ")f(x)"
				// so that they become  "#*f(x)" and ")*f(x)".
				for (String f : allFunctions) {
					if (function.equals(f)) {
						extraFunction = true;
						if (expression.isEmpty()) {
							break;
						}
						addMultiplication = expression.getTail().getValue().equals(CLOSE_PARENTHESIS) ||
									 		expression.getTail().isNumerical();
						break;
					}
				}
				if (extraFunction || expression.isEmpty()) {
					break;
				}

				// For expressions like ")f(x)"
				// so that they become  ")*f(x)".
				addMultiplication = expression.getTail().getValue().equals(CLOSE_PARENTHESIS) &&
									!isAnOperator(function);
		}
		if (addMultiplication) {
			expression.addLast(MULTIPLICATION);
		}
		if (extraFunction) {
			expression.addLast(OPEN_PARENTHESIS, function);
		}
		else if (!function.equals(END)) {
			switch (function) {
				case PI:
					expression.addLast(Math.PI);
					break;
				case E:
					expression.addLast(Math.E);
					break;
				case I:
					expression.addLast(Complex.I);
					break;
				case VARIABLE:
					switch (number) {
						case SCALAR:
							expression.addLast(variables.get(variable));
							break;
						case VECTOR:
							expression.addLast(vectors.get(variable));
							break;
						case MATRIX:
							expression.addLast(matrices.get(variable));
					}
					break;
				default:
					expression.addLast(function);
			}
		}
	}

	private boolean isAnOperator(String s) {
		switch (s) {
			case ADDITION:
			case SUBTRACTION:
			case MULTIPLICATION:
			case DIVISION:
			case POWER:
				return true;
		}
		return false;
	}

	private Number isVariable(String s) {
		for (String variable : variables.keySet()) {
			if (s.equals(variable)) {
				return Number.SCALAR;
			}
		}
		for (String vector : vectors.keySet()) {
			if (s.equals(vector)) {
				return Number.VECTOR;
			}
		}
		for (String matrix : matrices.keySet()) {
			if (s.equals(matrix)) {
				return Number.MATRIX;
			}
		}
		return null;
	}

	private boolean isError(String s) {
		switch (s) {
			case ERROR_MESSAGE:
			case DOMAIN_ERROR_MESSAGE:
			case DIMENSION_ERROR_MESSAGE:
				return true;
		}
		return false;
	}

	public void print(String s) {
		System.out.println(s);
	}

	public static String[] concatenate(String[] ... arrays) {
		switch (arrays.length) {
			case 0:
				return null;
			case 1:
				return arrays[0];
			default:
				int length = 0;
				for (int i = 0; i < arrays.length; i++) {
					length += arrays[i].length;
				}
				String[] array = new String[length];
				int arrayIndex = 0;
				for (int i = 0; i < arrays.length; i++) {
					for (int j = 0; j < arrays[i].length; j++) {
						array[arrayIndex] = arrays[i][j];
						arrayIndex++;
					}
				}
				return array;
		}
	}

	public static double[] concatenate(double[] ... arrays) {
		switch (arrays.length) {
			case 0:
				return null;
			case 1:
				return arrays[0];
			default:
				int length = 0;
				for (int i = 0; i < arrays.length; i++) {
					length += arrays[i].length;
				}
				double[] array = new double[length];
				int arrayIndex = 0;
				for (int i = 0; i < arrays.length; i++) {
					for (int j = 0; j < arrays[i].length; j++) {
						array[arrayIndex] = arrays[i][j];
						arrayIndex++;
					}
				}
				return array;
		}
	}	

	private Complex evaluateFunction(String function, Complex number) throws DomainException {
		if (function.isEmpty()) {
			return number;
		}
		System.out.println("Evaluating " + function + "...");
		switch (function) {
			case SINE:
				number = number.sin();
				break;
			case COSINE:
				number = number.cos();
				break;
			case TANGENT:
				number = number.tan();
				break;
			case COSECANT:
				number = number.csc();
				break;
			case SECANT:
				number = number.sec();
				break;
			case COTANGENT:
				number = number.cot();
				break;
			case INVERSE_SINE:
				number = number.arcsin();
				break;
			case INVERSE_COSINE:
				number = number.arccos();
				break;
			case INVERSE_TANGENT:
				number = number.arctan();
				break;
			case INVERSE_COSECANT:
				number = number.arccsc();
				break;
			case INVERSE_SECANT:
				number = number.arcsec();
				break;
			case INVERSE_COTANGENT:
				number = number.arccot();
				break;
			case COMMON_LOG:
				number = number.log();
				break;
			case NATURAL_LOG:
				number = number.ln();
				break;
			case SQUARE_ROOT:
				number = number.toThe(0.5);
				break;
			default:
				return null;
		}
		if (number == null) {
			throw new DomainException();
		}
		return number;
	}

	private Complex evaluate(Expression e) throws ArithmeticException, DomainException {
		return evaluate(e.getHead(), e.getTail(), true);
	}

	private Complex evaluate(Node fromNode, Node toNode, boolean checkForParentheses) throws ArithmeticException, DomainException {
		System.out.println("from " + fromNode + " to " + toNode);

		// If the expression is just a number, return it.
		if (fromNode == toNode) {
			if (!fromNode.isNumerical()) {

				// If it's just an operation, throw an exception.
				throw new ArithmeticException();
			}
			return fromNode.getNumber();
		}
		Node node = fromNode;
		Node next, previous;
		if (checkForParentheses) {

			// Evaluate expressions inside parentheses recursively using a stack.
			LinkedList<Node> parentheses = new LinkedList<Node>();
			while (node != null) {

				// If current node is an open parenthesis, push it onto the stack.
				if (node.getValue().equals(OPEN_PARENTHESIS)) {
					parentheses.addLast(node);
				}

				// If it's a close parenthesis,
				else if (node.getValue().equals(CLOSE_PARENTHESIS)) {

					// and there is no open parenthesis already in the stack, throw an exception.
					if (parentheses.isEmpty()) {
						throw new ArithmeticException();
					}
					Node start = parentheses.removeLast();

					// If "()" appears, throw an exception.
					if (start.getNext() == node) {
						throw new ArithmeticException();
					}

					// Evaluate what's inside the parentheses.
					Complex newNumber = evaluate(start.getNext(), node.getPrevious(), false);
					newNumber = evaluateFunction(start.getFunction(), newNumber);

					// Mend the list by replacing the parenthesized expression with its value in the overall expression.
					next = node.getNext();
					previous = start.getPrevious();
					if (previous == null) {
						if (next == null) {
							return newNumber;
						}
						else {
							fromNode = new Node(newNumber, null, next);
						}
					}
					else {
						if (next == null) {
							toNode = new Node(newNumber, previous, null);
						}
						else {
							new Node(newNumber, previous, next);
						}
					}
				}

				// If we made it to the last node we're suppposed to look at, break.
				if (node == toNode) {
					break;
				}

				// Go to the next node.
				node = node.getNext();
			}

			// Reset node.
			node = fromNode;
		}

		// Make lists for beginning and ending nodes for each term.
		ArrayList<Node> termStarts = new ArrayList<Node>();
		ArrayList<Node> termEnds = new ArrayList<Node>();

		// Record whether operations between terms are addition or subtraction.
		ArrayList<Boolean> addition = new ArrayList<Boolean>();

		// Make lists for beginning and ending nodes for each factor for each term.
		ArrayList<ArrayList<Node>> factorStarts = new ArrayList<ArrayList<Node>>();
		ArrayList<ArrayList<Node>> factorEnds = new ArrayList<ArrayList<Node>>();

		// Record whether operations between factors are multiplication or division for each term.
		ArrayList<ArrayList<Boolean>> multiplication = new ArrayList<ArrayList<Boolean>>();

		// Set up a helper variable.
		String nextValue;

		// Find where the terms are by looping through the expression.
		if (true) {
			print("PRINT OUT THE EXPRESSION:");
			while (true) {
				print("  " + node);
				if (node == toNode) {
					node = fromNode;
					break;
				}
				node = node.getNext();
			}
		}
		while (true) {
			System.out.println("Adding to termStarts: " + node);
			if (!node.isNumericalOrNegative()) {
				throw new ArithmeticException();
			}
			termStarts.add(node);

			// Keep looping till we get to a plus or minus sign.
			while (node != toNode) {
				next = node.getNext();
				if (next == null) {
					break;
				}
				nextValue = next.getValue();
				if (nextValue.equals(ADDITION) || nextValue.equals(SUBTRACTION)) {
					break;
				}
				node = next;
			}
			if (!node.isNumericalOrNegative()) {
				throw new ArithmeticException();
			}
			termEnds.add(node);
			if (node == toNode) {
				break;
			}
			next = node.getNext();
			if (next == null) {
				break;
			}
			nextValue = next.getValue();
			addition.add(nextValue.equals(ADDITION));
			node = next.getNext();
			if (node == null) {
				throw new ArithmeticException();
			}
		}
		for (int i = 0; i < termStarts.size(); i++) {
			node = termStarts.get(i);
			Node endNode = termEnds.get(i);
			factorStarts.add(new ArrayList<Node>());
			factorEnds.add(new ArrayList<Node>());
			multiplication.add(new ArrayList<Boolean>());
			while (true) {
				System.out.println("Adding to factorStarts: " + node);
				if (!node.isNumericalOrNegative()) {
					throw new ArithmeticException();
				}
				factorStarts.get(i).add(node);

				// Keep looping till we get to a multiplication or division sign.
				while (node != endNode) {
					next = node.getNext();
					if (next == null) {
						break;
					}
					nextValue = next.getValue();
					if (nextValue.equals(MULTIPLICATION) || nextValue.equals(DIVISION)) {
						break;
					}
					node = next;
				}
				if (!node.isNumericalOrNegative()) {
					throw new ArithmeticException();
				}
				factorEnds.get(i).add(node);
				if (node == endNode) {
					break;
				}
				next = node.getNext();
				if (next == null) {
					break;
				}
				nextValue = next.getValue();
				multiplication.get(i).add(nextValue.equals(MULTIPLICATION));
				node = next.getNext();
				if (node == null) {
					throw new ArithmeticException();
				}
			}
		}

		// Evaluate each term and add/subtract it from a cumulative total.
		Complex total = new Complex();
		for (int i = 0; i < termStarts.size(); i++) {
			node = termStarts.get(i);

			// Let number be the first number in this term.
			Complex termTotal = node.getNumber();

			// If we haven't reached the end of the term yet,
			if (node != termEnds.get(i)) {

				// Loop through and evaluate the next multiplication or division till we get to the end.
				for (int j = 0; j < factorEnds.get(i).size(); j++) {
					node = factorEnds.get(i).get(j);

					// Let number be the last number in this factor.
					Complex number = node.getNumber();

					// Negate the number as many times as there are negative signs before it.
					while (node.getPrevious() != null && node.getPrevious().isNegative()) {
						number = number.negative();
						node = node.getPrevious();
					}

					// If we haven't reached the start of the factor yet,
					if (node != factorStarts.get(i).get(j)) {

						// Loop through and evaluate the previous power till we get to the start.
						while (true) {
							node = node.getPrevious().getPrevious();
							if (node == null || !node.isNumericalOrNegative()) {
								throw new ArithmeticException();
							}
							number = node.getNumber().toThe(number);
							if (number == null) { // Disallow computing 0 to any negative or unreal power.
								throw new DomainException();
							}
							if (node.getPrevious() != null && node.getPrevious().isNegative()) {
								number = number.negative();
								node = node.getPrevious();
							}
							if (node == factorStarts.get(i).get(j)) {
								break;
							}
						}
					}

					// Initialize term total to the number if this is our first factor.
					if (j == 0) {
						termTotal = number;
					}

					// Otherwise, multiply or divide the total by it.
					else {
						if (multiplication.get(i).get(j - 1)) {
							termTotal = termTotal.times(number);
						}
						else {

							// Disallow division by 0.
							if (number.isZero()) {
								throw new DomainException();
							}
							termTotal = termTotal.dividedBy(number);
						}
					}
				}
			}

			// Initialize total to the term total if this is our first term.
			if (i == 0) {
				total = termTotal;
			}

			// Otherwise, add it to or subtract it from the total.
			else {
				if (addition.get(i - 1)) {
					total = total.plus(termTotal);
				}
				else {
					total = total.minus(termTotal);
				}
			}
		}
		System.out.println(termStarts + "\n" + termEnds + "\n" + addition);
		System.out.println("Total: " + total);
		return total;
	}
}