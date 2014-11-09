package org.bitmessage.model;

import org.codehaus.preon.annotation.BoundNumber;
import org.codehaus.preon.annotation.If;
import org.codehaus.preon.buffer.ByteOrder;

public class VariableLengthInteger {

	@BoundNumber(byteOrder=ByteOrder.BigEndian, size="8")
	private int firstByte;
	
	@If("firstByte == 0xfd")
	@BoundNumber(byteOrder=ByteOrder.BigEndian, size="16")
	private int twoBytes;
	
	@If("firstByte == 0xfe")
	@BoundNumber(byteOrder=ByteOrder.BigEndian, size="32")
	private long fourBytes;
	
	public void setValue(long value) {
		if(value < 0xfd) {
			firstByte = (int) value;
		} else if(value <= 0xffff) {
			firstByte = 0xfd;
			twoBytes = (int) value;
		} else {
			firstByte = 0xfe;
			fourBytes = value;
		}
	}
	
	public long getValue() {
		if(firstByte < 0xfd)
			return firstByte;
		else if(firstByte == 0xfd) {
			return twoBytes;
		} else {
			return fourBytes;
		}
	}
	
}
