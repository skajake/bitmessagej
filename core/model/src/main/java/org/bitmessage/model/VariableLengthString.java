package org.bitmessage.model;

import org.codehaus.preon.annotation.Bound;
import org.codehaus.preon.annotation.BoundString;
import org.codehaus.preon.annotation.If;

public class VariableLengthString {
	
	@Bound
	private VariableLengthInteger length;
	
	@If("length.firstByte < 0xfd")
	@BoundString(size="length.firstByte")
	private String value8;
	
	@If("length.firstByte == 0xfd")
	@BoundString(size="length.twoBytes")
	private String value16;
	
	@If("length.firstByte == 0xfe")
	@BoundString(size="length.fourBytes")
	private String value32;
	
	public void setValue(String value) {
		if(value.length() < 0xfd) {
			value8 = value;
			value16 = null;
			value32 = null;
		} else if(value.length() <= 0xffff) {
			value16 = value;
			value8 = null;
			value32 = null;
		} else {
			value32 = value;
			value8 = null;
			value16 = null;
		}
		
		this.length = new VariableLengthInteger();
		this.length.setValue(value.length());
	}
	
	public String getValue() {
		if(length.getValue() < 0xfd) {
			return value8;
		} else if(length.getValue() <= 0xffff) {
			return value16;
		} else {
			return value32;
		}
	}
}
