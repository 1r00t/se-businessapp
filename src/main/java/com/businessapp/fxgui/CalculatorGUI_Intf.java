package com.businessapp.fxgui;


public interface CalculatorGUI_Intf extends FXMLControllerIntf {

	public static final int DISPLAY_MAXDIGITS = 16;

	public enum Token {
		K_VAT,	K_CE,	K_C,	K_BACK,
		K_MPLUS,K_MR,	K_MC,	K_DIV,
		K_7,	K_8,	K_9,	K_MUL,
		K_4,	K_5,	K_6,	K_MIN,
		K_1,	K_2,	K_3,	K_PLUS,
		K_0,	K_1000,	K_DOT,	K_EQ,
	};

	final String[] KeyLabels = new String[] {
		"MwSt",	"CE",	"C",	"<-",
		"M+",	"MR",	"MC",	"/",
		"7",	"8",	"9",	"*",
		"4",	"5",	"6",	"-",
		"1",	"2",	"3",	"+",
		"0",	"1000",	",",	"=",
	};

	public void writeTextArea( String text );

	public void writeSideArea( String text );

}
