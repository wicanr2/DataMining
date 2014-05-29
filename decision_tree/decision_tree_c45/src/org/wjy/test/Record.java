package org.wjy.test;

import java.util.ArrayList;
import java.util.List;

public class Record {
	private List<Attribute> list = new ArrayList<Attribute>();
	
	public Record() {
		list.clear();
	}
	public List<Attribute> getList() {
		return list;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.setLength(0);
		for ( Attribute attr : list ) {
			sb.append(attr.getValue()).append(" ");
		}
		sb.append("\n");
		return sb.toString();
	}
}
