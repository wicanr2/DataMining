package org.wjy.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Category {
	private String name ;
	private Set<String> rangeSet = new HashSet<String>(); 
	private int index;
	private int rangeSize;
	private String [] range = null;
	public Category(String n,int i) {
		name = n;
		index = i;
		rangeSet.clear();
	}
	public int getIndex() {
		return index;
	}
	public String getCategory() {
		return name;
	}
	public Set<String> getRangeSet() {
		return rangeSet;
	}
	public int getRangeSize() {
		return rangeSize;
	}
	public void update() {
		range = rangeSet.toArray(new String[0]);
		rangeSize = rangeSet.size();
	}
	public String getRangeI(int index) {
		return range[index];
	}
	public String[] getRangeStrAry() {
		return range;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<String> iter = rangeSet.iterator();
		sb.append(name).append(" range: ");
		while(iter.hasNext()) {
			sb.append(iter.next()).append(",");
		}
		return sb.toString();
	}
}
