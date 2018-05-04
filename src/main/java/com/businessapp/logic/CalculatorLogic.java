package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.fxgui.CalculatorGUI_Intf;
import com.businessapp.fxgui.CalculatorGUI_Intf.Token;


/**
 * Implementation of CalculatorLogicIntf that only displays Tokens
 * received from the Calculator UI.
 *
 */
class CalculatorLogic implements CalculatorLogicIntf {
	private CalculatorGUI_Intf view;
	private StringBuffer dsb = new StringBuffer();
	private final double VAT_RATE = 19.0;

	CalculatorLogic() {
	}

	@Override
	public void inject( ControllerIntf dep ) {
		this.view = (CalculatorGUI_Intf)dep;
	}

	@Override
	public void inject( Component parent ) {		
	}

	@Override
	public void start() {
		nextToken( Token.K_C );		// reset calculator
	}

	@Override
	public void stop() {
	}


	/**
     * Process next token received from UI controller.
     * <p>
     * Tokens are transformed into output into UI properties:
     * 	- CalculatorIntf.DISPLAY for numbers and
     * 	- CalculatorIntf.SIDEAREA for VAT calculations.
     * <p>
     * @param tok the next Token passed from the UI, CalculatorViewController.
     */
	public void nextToken( Token tok ) {
		try {
			switch( tok ) {
			case K_0:	appendBuffer( "0" ); break;
			case K_1:	appendBuffer( "1" ); break;
			case K_2:	appendBuffer( "2" ); break;
			case K_3:	appendBuffer( "3" ); break;
			case K_4:	appendBuffer( "4" ); break;
			case K_5:	appendBuffer( "5" ); break;
			case K_6:	appendBuffer( "6" ); break;
			case K_7:	appendBuffer( "7" ); break;
			case K_8:	appendBuffer( "8" ); break;
			case K_9:	appendBuffer( "9" );
				break;

			case K_1000:appendBuffer( "000" );
				break;

			case K_DIV:
				throw new ArithmeticException( "ERR: div by zero" );
			case K_MUL:	appendBuffer( "*" ); break;
			case K_PLUS:appendBuffer( "+" ); break;
			case K_MIN:	appendBuffer( "-" ); break;
			case K_EQ:
			    //appendBuffer( "=" );
			    solve();
			    break;

			case K_VAT:
                if (!solve()) break;
                double inputNums = Double.parseDouble(dsb.toString().replace(",", "."));
                double netto = inputNums / ((VAT_RATE + 100) / 100);
                double mwst = inputNums - netto;
                view.writeSideArea(
                        String.format("Brutto: %.2f\n%.2f%% Mwst: %.2f\nNetto: %.2f", inputNums, VAT_RATE, mwst, netto)
                );
				break;

			case K_DOT:	appendBuffer( "." );
				break;

			case K_BACK:
				dsb.setLength( Math.max( 0, dsb.length() - 1 ) );
				break;

			case K_C:
				view.writeSideArea( "" );
			case K_CE:
				dsb.delete( 0,  dsb.length() );
				break;

			default:
			}
			String display = dsb.length()==0? "0" : dsb.toString();
			view.writeTextArea( display );

		} catch( ArithmeticException e ) {
			view.writeTextArea( e.getMessage() );
		}
	}

	/*
	 * Private method(s).
	 */
	private void appendBuffer( String d ) {
		if( dsb.length() <= CalculatorGUI_Intf.DISPLAY_MAXDIGITS ) {
			dsb.append( d );
		}
	}


	private boolean solve() {
		// Versucht die eingegebene Gleichung zu lösen
		try {
			String result = String.format("%.2f", calculate(dsb.toString()));
			view.writeSideArea("");
			view.writeSideArea(result);
			return true;
		} catch(NumberFormatException | StringIndexOutOfBoundsException e) {
			view.writeSideArea("Eingabe Fehlerhaft!");
			return false;
		}
	}

	private static Double calculate(String expression) throws NumberFormatException, StringIndexOutOfBoundsException {
		// Löst die eingegebene Gleichung
		if (expression == null || expression.length() == 0) {
			return null;
		}
		if (expression.startsWith("(") && expression.endsWith(")")) {
			// Eliminiert äußere Klammern
			return calculate(expression.substring(1, expression.length() - 1));
		}
		String[] exprArray = new String[]{expression}; // String array mit expression
		double leftVal = getNextOperand(exprArray); // Der nächste Operand.
		expression = exprArray[0];
		if (expression.length() == 0) {
			return leftVal;
		}
		char operator = expression.charAt(0);
		expression = expression.substring(1);

		while (operator == '*' || operator == '/') {
			exprArray[0] = expression;
			double rightVal = getNextOperand(exprArray);
			expression = exprArray[0];
			if (operator == '*') {
				leftVal = leftVal * rightVal;
			} else {
				leftVal = leftVal / rightVal;
			}
			if (expression.length() > 0) {
				operator = expression.charAt(0);
				expression = expression.substring(1);
			} else {
				return leftVal;
			}
		}
		if (operator == '+') {
			return leftVal + calculate(expression);
		} else {
			return leftVal - calculate(expression);
		}

	}

	private static double getNextOperand(String[] exp){
		// Liefert den nächsten Operanden zurück
		double result;
		if (exp[0].startsWith("(")) {
			int numOpenBrackets = 1;
			int i = 1;
			while (numOpenBrackets != 0) {
				// finde innerste Klammer
				if (exp[0].charAt(i) == '(') {
					numOpenBrackets++;
				} else if (exp[0].charAt(i) == ')') {
					numOpenBrackets--;
				}
				i++;
			}
			result = calculate(exp[0].substring(1, i - 1)); // innerste Klammer ausrechnen
			exp[0] = exp[0].substring(i);
		} else {
			int i = 1;
			if (exp[0].charAt(0) == '-') {
				i++;
			}
			while (exp[0].length() > i && isNumber((int) exp[0].charAt(i))) {
				i++;
			}
			result = Double.parseDouble(exp[0].substring(0, i));
			exp[0] = exp[0].substring(i);
		}
		return result;
	}

	private static boolean isNumber(int c) {
		// char in int umwandeln
		int zero = (int) '0';
		int nine = (int) '9';
		return (c >= zero && c <= nine) || c =='.';

	}
}
