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
			this.value8 = value;
		} else if(value.length() <= 0xffff) {
			this.value16 = value;
		} else {
			this.value32 = value;
		}
		
		this.length = new VariableLengthInteger();
		this.length.setValue(value.length());
	}
	
	public String getValue() {
		if(length.getValue() < 0xfd) {
			return this.value8;
		} else if(length.getValue() <= 0xffff) {
			return this.value16;
		} else {
			return this.value32;
		}
	}
}
