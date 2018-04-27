package com.businessapp.fxgui;

/*
 * Local helper class to trim white spaces off a String property when
 * altered in a UI by a user. It also carries information whether the
 * Property value is editable.
 */
class StringTestUpdateProperty {
	private final String name;
	private String value;
	private boolean editable;

	StringTestUpdateProperty( String name, String value, boolean editable ) {
		this.name = name;
		this.value = value==null? null : value.trim();
		this.editable = editable;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public String getValueEmptyIfNull() {
		return value != null? value : "";
	}

	public boolean isEditable() {
		return editable;
	}

	public boolean testAndUpdate( String update ) {
		String u = update==null? "" : update.trim();
		String v = value==null? "" : value;
		if( ! v.equals( u ) ) {
			value = update==null? null : u;
			return true;
		}
		return false;
	}

}
