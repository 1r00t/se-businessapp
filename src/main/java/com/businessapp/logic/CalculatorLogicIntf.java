package com.businessapp.logic;

import javafx.beans.property.SimpleStringProperty;


/**
 * Public interface of Calculator logic. Reference to anonymous implementation is
 * injected into the associated CalculatorViewController to connect with the UI.
 */
public interface CalculatorLogicIntf extends LogicIntf {
	final SimpleStringProperty DISPLAY = new SimpleStringProperty();
	final SimpleStringProperty SIDEAREA = new SimpleStringProperty();
	final int DISPLAY_MAXDIGITS = 16;
	final double VAT_RATE = 19.0;		// 19% VAT in Germany

	enum Token {
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

	final Object[][] ShortCutKeys = new Object[][] {
		{ "\r",	Token.K_EQ		},
		{ "\b",	Token.K_BACK	},
		{ "c",	Token.K_C		},
		{ "e",	Token.K_CE		},
		{ ".",	Token.K_DOT		},
		{ "m",	Token.K_VAT		},
		{ "t",	Token.K_1000	},
	};


	/**
     * Process next token from UI controller. Tokens are defined in CalculatorIntf.java
     * <p>
     * @param tok the next Token passed from the UI, CalculatorViewController.
     * 
     */
	public void nextToken( Token tok );

}
