package org.bitmessagej.model;

import org.codehaus.preon.annotation.BoundList;
import org.codehaus.preon.annotation.BoundNumber;

public class Ipv4Prefix {
	
	@BoundList(size="10")
	private byte[] zeros = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

	@BoundNumber(size="8")
	private Short firstFF = 0xFF;
	
	@BoundNumber(size="8")
	private Short secondFF = 0xFF;

	public byte[] getZeros() {
		return zeros;
	}

	public void setZeros(byte[] zeros) {
		this.zeros = zeros;
	}

	public Short getFirstFF() {
		return firstFF;
	}

	public void setFirstFF(Short firstFF) {
		this.firstFF = firstFF;
	}

	public Short getSecondFF() {
		return secondFF;
	}

	public void setSecondFF(Short secondFF) {
		this.secondFF = secondFF;
	}

}
