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

}
