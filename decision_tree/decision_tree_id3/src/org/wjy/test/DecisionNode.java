package org.wjy.test;

import java.util.ArrayList;
import java.util.List;


public class DecisionNode {
	private double entropy = 0.0f;
	private double gain = 0.0f;
	private double total_info = 0.0f;
	private String attribute = "";
	private List<Category> mCategoryList = new ArrayList<Category>();
	private List<Record> recordList = new ArrayList<Record>();
	private List<DecisionBranch> childList = new ArrayList<DecisionBranch>(); 
	public DecisionNode() {
		childList.clear();
		recordList.clear();
	}
	public List<DecisionBranch> getDecisionBranch() {
		return childList;
	}
	public List<Category> getCategoryList() {
		return mCategoryList;
	}
	public List<Record> getRecordList() {
		return recordList;
	}
	public void setEntroy(double e) {
		entropy = e;
	}
	public void setGain(double g){
		gain = g;
	}
	public void setTotalInfo(double t) {
		total_info = t;
	}
	public void setAttribute(String attr) {
		attribute = attr;
	}
	public double getEntroy() {
		return entropy;
	}
	public double getGain() {
		return gain;
	}
	public double getTotalInfo() {
		return total_info;
	}
	public String getAttribute() {
		return attribute;
	}
	public String makeDecision(Record r) {
		if ( childList.size() == 0 ) {
			return attribute;
		}
		Attribute target_attr = null;
		for ( Attribute attr : r.getList() ) {
			if ( attr.getCategory().equals(attribute)) {
				target_attr = attr;
				break;
			}
		}
		if ( target_attr == null ) 
			return "n/a";
		DecisionBranch target_branch = null;
		for ( DecisionBranch b : childList ) {
			if ( b.getBranchCondition().equals(target_attr.getValue())) {
				target_branch = b;
			}
		}
		if ( target_branch == null ) 
			return "n/a";
		return target_branch.getNode().makeDecision(r);
	}
	//---------------------------------------------
	public String convertToString(int level) {
		StringBuilder sb = new StringBuilder ();
		if ( childList.size() <= 0 ) {
			for( int i = 0 ; i < level ;i++) {
				sb.append("   ");
			}
			sb.append(attribute).append("\n");
			return sb.toString();
		}
		for( int i = 0 ; i < level ;i++) {
			sb.append("   ");
		}
		sb.append(attribute).append(" --> ").append("\n");
		for ( DecisionBranch b : childList ) {
			if ( b.getNode().getAttribute().equals("n/a") ) continue;
			for( int i = 0 ; i < level+1 ;i++) {
				sb.append("   ");
			}
			sb.append(b. getBranchCondition() ).append(" ==> ").append("\n");
			sb.append(b.getNode().convertToString(level+2));	
		}
		
		//sb.append("\n");
		return sb.toString();
	}
	//---------------------------------------------
	@Override
	public String toString() {
		return convertToString(0);
	}
	
}
