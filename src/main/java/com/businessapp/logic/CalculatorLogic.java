package com.businessapp.logic;


/**
 * ********************************************************************************
 * Local implementation class (unfinished) of calculator logic.
 * <p>
 * Instance is invoked with public interface method nextToken( Token tok ) passing
 * an input token that is created at the UI from a key event. Input tokens are defined
 * in CalculatorIntf and comprised of digits [0-9,.], numeric operators [+,-,*,/,VAT]
 * and control tokens [\backspace,=,C,CE,K_1000].
 * <p>
 * Outputs are are passed through display properties:<p>
 * 	- CalculatorIntf.DISPLAY for numbers and<p>
 * 	- CalculatorIntf.SIDEAREA for VAT calculations.
 * <p>
 * Method(s):
 *	- public void nextToken( Token tok );	;process next token from UI controller
 * 
 */
class CalculatorLogic implements CalculatorLogicIntf {

	private StringBuffer dsb = new StringBuffer();

	/**
	 * Local constructor.
	 */
	CalculatorLogic() {
		nextToken( Token.K_C );		// reset buffers
	}

	/**
     * Process next token from UI controller. Tokens are defined in CalculatorIntf.java
     * <p>
     * Outputs are are passed through display properties:
     * 	- CalculatorIntf.DISPLAY for numbers and
     * 	- CalculatorIntf.SIDEAREA for VAT calculations.
     * <p>
     * @param tok the next Token passed from the UI, CalculatorViewController.
     */
	public void nextToken( Token tok ) {
		String d = tok==Token.K_DOT? "." : CalculatorLogicIntf.KeyLabels[ tok.ordinal() ];
		try {
			switch( tok ) {
			case K_0: case K_1: case K_2: case K_3: case K_4:
			case K_5: case K_6: case K_7: case K_8: case K_9:
				appendBuffer( d );
				break;

			case K_1000:
				nextToken( Token.K_0 );
				nextToken( Token.K_0 );
				nextToken( Token.K_0 );
				break;

			case K_DIV:
				throw new ArithmeticException( "ERR: div by zero" );
			case K_MUL:
			case K_PLUS:
			case K_MIN:
			case K_EQ:
				appendBuffer( d );
				break;

			case K_VAT:
				CalculatorLogicIntf.SIDEAREA.set(
					"Brutto:  1,000.00\n" +
					VAT_RATE + "% MwSt:  159.66\n" +
					"Netto:  840.34"
				);
				break;

			case K_DOT:
				appendBuffer( d );
				break;

			case K_BACK:
				dsb.setLength( Math.max( 0, dsb.length() - 1 ) );
				break;

			case K_C:
				CalculatorLogicIntf.SIDEAREA.set( "" );
			case K_CE:
				dsb.delete( 0,  dsb.length() );
				break;

			default:
			}
			String display = dsb.length()==0? "0" : dsb.toString();
			CalculatorLogicIntf.DISPLAY.set( display );

		} catch( ArithmeticException e ) {
			CalculatorLogicIntf.DISPLAY.set( e.getMessage() );
		}
	}


	/*
	 * Private method(s).
	 */
	private void appendBuffer( String d ) {
		if( dsb.length() <= DISPLAY_MAXDIGITS ) {
			dsb.append( d );
		}
	}

}
