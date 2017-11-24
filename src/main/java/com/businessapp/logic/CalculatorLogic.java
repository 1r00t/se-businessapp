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
                    solve();
                    double inputNums = Double.parseDouble(SIDEAREA.get());
                    double mwst = inputNums * VAT_RATE / 100;
                    double netto = inputNums - mwst;
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

    public void solve() {
        SIDEAREA.set("");
        try {
            SIDEAREA.set((calculate(DISPLAY.getValue()).toString()));
        } catch(NumberFormatException | StringIndexOutOfBoundsException e) {
            SIDEAREA.set("Eingabe Fehlerhaft!");
        }
    }

    public static Double calculate(String expression) throws NumberFormatException, StringIndexOutOfBoundsException {
        if (expression == null || expression.length() == 0) {
            return null;
        }
        return calc(expression.replace(" ", ""));
    }

    public static Double calc(String expression) {

        if (expression.startsWith("(") && expression.endsWith(")")) {
            return calc(expression.substring(1, expression.length() - 1));
        }
        String[] containerArr = new String[]{expression};
        double leftVal = getNextOperand(containerArr);
        expression = containerArr[0];
        if (expression.length() == 0) {
            return leftVal;
        }
        char operator = expression.charAt(0);
        expression = expression.substring(1);

        while (operator == '*' || operator == '/') {
            containerArr[0] = expression;
            double rightVal = getNextOperand(containerArr);
            expression = containerArr[0];
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
            return leftVal + calc(expression);
        } else {
            return leftVal - calc(expression);
        }

    }

    private static double getNextOperand(String[] exp){
        double res;
        if (exp[0].startsWith("(")) {
            int open = 1;
            int i = 1;
            while (open != 0) {
                if (exp[0].charAt(i) == '(') {
                    open++;
                } else if (exp[0].charAt(i) == ')') {
                    open--;
                }
                i++;
            }
            res = calc(exp[0].substring(1, i - 1));
            exp[0] = exp[0].substring(i);
        } else {
            int i = 1;
            if (exp[0].charAt(0) == '-') {
                i++;
            }
            while (exp[0].length() > i && isNumber((int) exp[0].charAt(i))) {
                i++;
            }
            res = Double.parseDouble(exp[0].substring(0, i));
            exp[0] = exp[0].substring(i);
        }
        return res;
    }


    private static boolean isNumber(int c) {
        int zero = (int) '0';
        int nine = (int) '9';
        return (c >= zero && c <= nine) || c =='.';
    }


    /*
     * Private method(s).
     */
    private void appendBuffer(String d) {
        if (dsb.length() <= DISPLAY_MAXDIGITS) {
            dsb.append(d);
        }
    }

}
