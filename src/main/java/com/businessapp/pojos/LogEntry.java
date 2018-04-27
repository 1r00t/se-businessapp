package com.businessapp.pojos;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * LogEntry represents an entry in a log with timestamp, a separator and text.
 * Example: "2018-04-02 10:16:24:868, This is a text entry."
 */
public class LogEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	/*
	 * DateFormats for Date-part ex-/internalization.
	 *	"dd. MMM yyyy, HH:mm"	- 02. Apr 2018, 10:16
	 *	"yyyy-MM-dd HH:mm a z"	- 2018-04-02 10:16:24:868
	 */
	public static final SimpleDateFormat df			= new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS" );
	public static final SimpleDateFormat df_simple	= new SimpleDateFormat( "dd. MMM yyyy, HH:mm" );

	private static final String Separator = ", ";
	private static long lastTimeStamp = 0L;
	static {
		df.setTimeZone( TimeZone.getTimeZone( "GMT+01" ) );
		df_simple.setTimeZone( df.getTimeZone() );
	}


	/*
	 * Properties.
	 */
	private Date timeStamp = null;

	private String logLine = null;


	/**
	 * Public default constructor (required by JSON deserialization).
	 */
	public LogEntry() {
		this( "New." );
	}

	/**
	 * Public constructor from externalized String that can be split into a
	 * timestamp and a logLine part.
	 */
	public LogEntry( String logStr ) {
		Object[] parts = parselogStr( logStr );
		this.timeStamp = parts[0]==null? null : (Date)parts[0];
		this.logLine = (String)parts[1];
		if( this.timeStamp == null ) {
			this.timeStamp = nextUniqueTimeStamp();
		}
	}

	/**
	 * Get timestamp part .
	 */
	public String getSimpleTimestamp() {
		return df_simple.format( timeStamp );
	}

	/**
	 * Get/set logLine part.
	 */
	public String getLog() {
		return logLine;
	}

	public void setLog( String log ) {
		this.logLine = log;
	}

	/**
	 * Externalize as String: "2018-03-15 20:10:27.730, Customer 1234 created".
	 * @return externalized String.
	 */
	@Override
	public String toString() {
		return df.format( timeStamp ) + Separator + logLine;
	}


	/*
	 * Private helper function to split a "timestamp;log"-string into parts.
	 * @return split parts of a parsed logLine as Date for timestamp and text for logLine.
	 */
	private Object[] parselogStr( String logStr ) {
		Object[] res = new Object[] { null, null };
		String[] spl = logStr.split( Separator );
		if( spl.length > 1 ) {
			// two parts, try to parse date
			try {
				res[0] = df.parse( spl[ 0 ] );
				res[1] = spl[ 1 ];

			} catch( ParseException e ) {
			}
		}
		if( res[1]==null ) {
			res[1] = logStr;
		}
		return res;
	}

	/*
	 * Private helper method that generates a unique timestamp that differs at least by 1 msec
	 * from a prior call to nextUniqueTimeStamp().
	 * @return next unique timestamp.
	 */
	private Date nextUniqueTimeStamp() {
		Date now = new Date();
		long nowL = now.getTime();
		if( nowL <= lastTimeStamp ) {
			now = new Date( ++lastTimeStamp );
		} else {
			lastTimeStamp = nowL;
		}
		return now;
	}

}
