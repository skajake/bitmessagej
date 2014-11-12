package org.bitmessagej.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.preon.annotation.Bound;
import org.codehaus.preon.annotation.BoundList;
import org.codehaus.preon.annotation.If;

public class VariableLengthIntegerList {

	@Bound
	private VariableLengthInteger length;
	
	@If("length.firstByte < 0xfd")
	@BoundList(size="length.firstByte", type=VariableLengthInteger.class)
	private VariableLengthInteger[] value8;
	
	@If("length.firstByte == 0xfd")
	@BoundList(size="length.twoBytes", type=VariableLengthInteger.class)
	private VariableLengthInteger[] value16;
	
	@If("length.firstByte == 0xfe")
	@BoundList(size="length.fourBytes", type=VariableLengthInteger.class)
	private VariableLengthInteger[] value32;

	public VariableLengthIntegerList() {
		length = new VariableLengthInteger();
		length.setValue(0);
		value8 = new VariableLengthInteger[0];
	}
	
	private VariableLengthInteger[] getVariableLengthIntegerList() {
		if(length.getValue() < 0xfd) {
			return value8;
		} else if(length.getValue() <= 0xffff) {
			return value16;
		} else {
			return value32;
		}
	}
	
	private VariableLengthInteger[] variableLengthIntegerArrayFromLongList(List<Long> longList) {
		VariableLengthInteger[] variableLengthIntegers = new VariableLengthInteger[longList.size()];
		int i = 0;
		for(Long longValue : longList) {
			VariableLengthInteger variableLengthInteger = new VariableLengthInteger();
			variableLengthInteger.setValue(longValue);
			variableLengthIntegers[i] = variableLengthInteger;
			i++;
		}
		return variableLengthIntegers;
	}
	
	 /**
     *  
     * @return A list of longs copied from the variable length integers..  Warning: Any mutations made to this list will not be reflected in the variable length integers. 
     */
	public List<Long> getList() {
		List<Long> longList = new ArrayList<Long>();
		for(VariableLengthInteger variableLengthInteger : getVariableLengthIntegerList()) {
			longList.add(variableLengthInteger.getValue());
		}
		return longList;
	}
	
	public void setList(List<Long> value) {
		if(value.size() < 0xfd) {
			value8 = variableLengthIntegerArrayFromLongList(value);
			value16 = null;
			value32 = null;
		} else if(value.size() <= 0xffff) {
			value16 = variableLengthIntegerArrayFromLongList(value);
			value8 = null;
			value32 = null;
		} else {
			value32 = variableLengthIntegerArrayFromLongList(value);
			value8 = null;
			value16 = null;
		}
		
		this.length = new VariableLengthInteger();
		this.length.setValue(value.size());
	}
}
