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
 * - CalculatorIntf.DISPLAY for numbers and<p>
 * - CalculatorIntf.SIDEAREA for VAT calculations.
 * <p>
 * Method(s):
 * - public void nextToken( Token tok );	;process next token from UI controller
 */
class CalculatorLogic implements CalculatorLogicIntf {

    private StringBuffer dsb = new StringBuffer();

    /**
     * Local constructor.
     */
    CalculatorLogic() {
        nextToken(Token.K_C);        // reset buffers
    }

    /**
     * Process next token from UI controller. Tokens are defined in CalculatorIntf.java
     * <p>
     * Outputs are are passed through display properties:
     * - CalculatorIntf.DISPLAY for numbers and
     * - CalculatorIntf.SIDEAREA for VAT calculations.
     * <p>
     *
     * @param tok the next Token passed from the UI, CalculatorViewController.
     */
    public void nextToken(Token tok) {
        String d = tok == Token.K_DOT ? "." : CalculatorLogicIntf.KeyLabels[tok.ordinal()];
        try {
            switch (tok) {
                case K_0:
                case K_1:
                case K_2:
                case K_3:
                case K_4:
                case K_5:
                case K_6:
                case K_7:
                case K_8:
                case K_9:
                    appendBuffer(d);
                    break;

                case K_1000:
                    nextToken(Token.K_0);
                    nextToken(Token.K_0);
                    nextToken(Token.K_0);
                    break;

                case K_DIV:
                    appendBuffer("/");
                    break;
                    // throw new ArithmeticException("ERR: div by zero");
                case K_MUL:
                    appendBuffer("*");
                    break;
                case K_PLUS:
                    appendBuffer("+");
                    break;
                case K_MIN:
                    appendBuffer("-");
                    break;
                case K_EQ:
                    solve();
                    break;

                case K_VAT:
                    if (!solve()) break;
                    double inputNums = Double.parseDouble(SIDEAREA.get().replace(",", "."));
                    double netto = inputNums / ((VAT_RATE + 100) / 100);
                    double mwst = inputNums - netto;
                    SIDEAREA.set(

                            String.format("Brutto: %.2f\n%.2f%% Mwst: %.2f\nNetto: %.2f", inputNums, VAT_RATE, mwst, netto)

                            //"Brutto:  " + inputNums + "\n" +
                            //        VAT_RATE + "% MwSt: " + mwst + "\n" +
                            //        "Netto:  " +

                    );

                    break;

                case K_DOT:
                    appendBuffer(d);
                    break;

                case K_BACK:
                    dsb.setLength(Math.max(0, dsb.length() - 1));
                    break;

                case K_C:
                    SIDEAREA.set("");
                case K_CE:
                    dsb.delete(0, dsb.length());
                    break;

                case K_BRO:
                    appendBuffer(d);
                    break;
                case K_BRC:
                    appendBuffer(d);

                default:
            }
            String display = dsb.length() == 0 ? "0" : dsb.toString();
            DISPLAY.set(display);

        } catch (ArithmeticException e) {
            DISPLAY.set(e.getMessage());
        }
    }

    /*
     * Private method(s).
     */

    private void appendBuffer(String d) {
        if (dsb.length() <= DISPLAY_MAXDIGITS) {
            dsb.append(d);
        }
    }

    private boolean solve() {
        // Versucht die eingegebene Gleichung zu lösen
        try {
            String result = String.format("%.2f", calculate(DISPLAY.getValue()));
            SIDEAREA.set("");
            SIDEAREA.set(result);
            return true;
        } catch(NumberFormatException | StringIndexOutOfBoundsException e) {
            SIDEAREA.set("Eingabe Fehlerhaft!");
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
