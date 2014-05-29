package org.wjy.test;

public class DecisionBranch {
	private String branchCondition;
	private DecisionNode node;
	public DecisionBranch(String cond) {
		branchCondition = cond;
		node = new DecisionNode();
	}
	public String getBranchCondition() {
		return branchCondition;
	}
	public DecisionNode getNode() {
		return node;
	}
}
