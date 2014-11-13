package org.bitmessagej.model;

import java.util.Date;

import org.codehaus.preon.annotation.BoundNumber;
import org.codehaus.preon.buffer.ByteOrder;

public class Time {
	
	@BoundNumber(byteOrder=ByteOrder.BigEndian, size="64")
	private Long value;
	
	public Time() {
		setDate(new Date());
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}
	
	public void setDate(Date date) {
		this.value = date.getTime();
	}
	
	public Date getDate() {
		return new Date(getValue());
	}
	
}
