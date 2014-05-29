package org.wjy.test;

import java.util.ArrayList;
import java.util.List;

public class Attribute {
	
	private String category = "";
	private String value = "";
	
	public Attribute(String c, String v) {
		setCategory(c);
		setValue(v);
	}
	public String getCategory() {
		return category;
	}
	public String getValue() {
		return value;
	}
	public void setCategory(String c){
		category = c;
	}
	public void setValue(String v) {
		value = v;
	}
}
