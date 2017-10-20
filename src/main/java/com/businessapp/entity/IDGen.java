package com.businessapp.entity;

import com.businessapp.entity.EntityStoreIntf.ID_CHARSET;


/**
 * Local implementation class of a random ID-generator that supports a variety
 * of identifier formats as specified by an idFormat String.
 * <p>
 * Instances of class are associated each with an EntityStore and produce String
 * identifiers that are unique within the associated EntityStore.
 * <p>
 * Entity identifier formats are specified by character set, prefix and the number
 * of digits used in an IdFormat String:
 * <p>
 *	idFormat String		- generated identifiers<p>
 *	"ALPHA_NUM_UPPER#6"	- 6-digit, alpha-num identifier, e.g. "X8BA40" (travel booking codes)<p>
 *	"C_DEC#8"			- 8-digit, decimal identifer prefixed by "C", e.g. "C36874215"<p>
 *	"HEX#8"				- 8-digit, hexadecimal numbers, e.g. "8ACD44022"<p>
 */
class IDGen {
	private static final String[] characterSets = new String[] {
		EntityStoreIntf.ID_CHARSET.ALPHA_NUM.name(), "0123456789" + "ABCDEGFHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuwvxyz",
		EntityStoreIntf.ID_CHARSET.ALPHA_NUM_UPPER.name(), "0123456789" + "ABCDEGFHIJKLMNOPQRSTUVWXYZ",	// NumbersUpperCaseLetters
		EntityStoreIntf.ID_CHARSET.DEC.name(),	"0123456789",			// DecNumbers
		EntityStoreIntf.ID_CHARSET.HEX.name(),	"0123456789ABCDEF",		// HexNumbers
		EntityStoreIntf.ID_CHARSET.BIN.name(),	"01"					// BinaryNumbers
	};
	private String alphabet;
	private String prefix;
	private int digits;


	/**
	 * Local constructor.
	 * <p>
	 * @param idFormat format specification according to which identifiers will be generated.
	 */
	IDGen( String idFormat ) {
		this.alphabet = characterSets[ 3 ];
		this.prefix = null;
		this.digits = 8;

		parseIDFormat( idFormat );
	}

	/**
	 * Local method to generate random identifier according to format specification.
	 * <p>
	 * @return generated identifier. The identifier is not yet tested for uniqueness.
	 */
	String generateNextId() {
		final int alphaLen = alphabet.length();
		final int chunkSize = 10;	// generate + append chunks of up to 10 digits (long limit)
		StringBuffer kb = new StringBuffer();
		if( prefix != null ) {
			kb.append( prefix );
		}
		for( int kl = digits; kl > 0; kl -= chunkSize ) {
			int kl2 = Math.min( chunkSize,  kl );
			long rnd = (long)( Math.random() * Math.pow( alphaLen, kl2 ) );
			for( int i=0; i++ < kl2; rnd /= alphaLen ) {
				long k1 = rnd % alphaLen;
				kb.append( alphabet.charAt( (int)k1 ) );
			}
		}
		//int fill = digitLen - kb.length();
		for( int i=kb.length(); i < digits; i++ ) {
			kb.insert( 0, "@" );
			System.out.println( "============" );
		}
		return kb.toString();
	}

	/**
	 * Local static method to produces idFormat String from arguments.
	 * <p>
	 * @param charSet ID_CHARSET used for identifiers (ALPHA_NUM, ALPHA_NUM_UPPER, DEC, HEX, BIN).
	 * @param prefix null or prefix used for identifier.
	 * @param digits number of digits used.
	 * @return idFormat String.
	 */
	static String getIdFormat( ID_CHARSET charSet, String prefix, int digits ) {
		String p = prefix==null? "" : prefix + ".";
		return p + charSet.name() + "#" + digits;
	} 

	/*
	 * Private method(s).
	 */
	private void parseIDFormat( String idFormat ) {
		if( idFormat != null ) {
			String s2 = null;
			String[] split_1 = idFormat.split( "\\." );
			if( split_1.length > 1 ) {
				this.prefix = split_1[0].length() > 0? split_1[0] : null;
				s2 = split_1[1];
			} else {
				s2 = split_1[0];
			}
			String[] split_2 = s2.split( "#" );
			if( split_2.length > 1 ) {
				for( int i=0; i < characterSets.length; i += 2 ) {
					if( characterSets[ i ].equals( split_2[0] ) ) {
						this.alphabet = characterSets[ i + 1 ];
						break;
					}
				}
				try {
					this.digits = Integer.parseInt( split_2[1] );
	
				} catch( NumberFormatException e ) {
				}
			}
		}
	}

}
