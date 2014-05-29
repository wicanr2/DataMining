package org.wjy.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DecisionTreeDriver {
	private List<Category> mCategoryList = new ArrayList<Category>();
	private Category decisionClass = null;
	private List<Record> mRecordList = new ArrayList<Record>();
	private List<Record> testRecordList = new ArrayList<Record>();
	private String [] decisionRange = null;
	private int decisionRangeSize = 0;
	// information for accounting the pi ni and information values.
	DecisionNode root = new DecisionNode();
	public DecisionTreeDriver() {
		
	}
	//----------------------------------------------------------------------
	public void parseInputFile(String file) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(file));
		mCategoryList.clear();
		mRecordList.clear();
		
		//parse category
		String line = br.readLine();
		String tokens[] = line.split("\\s");
		Record record = null;
		List<Attribute> attrList = null;
		for ( int i = 0 ; i < tokens.length-1 ; i++ ) {
			mCategoryList.add(new Category(tokens[i],i));
		}
		decisionClass = new Category(tokens[tokens.length-1],tokens.length-1); //decision class
		
		//parse record attribute
		while ((line = br.readLine()) != null) {
			// process the line.
			//System.out.println(line);
			tokens = line.split("\\s");
			record = new Record();
			attrList = record.getList();
			for ( int i = 0 ; i < tokens.length ; i++ ) {
				if ( i == tokens.length-1 ) {
					attrList.add(new Attribute(decisionClass.getCategory(),tokens[i]));
					decisionClass.getRangeSet().add(tokens[i]);
				} else {
					attrList.add(new Attribute(mCategoryList.get(i).getCategory(),tokens[i]));
					mCategoryList.get(i).getRangeSet().add(tokens[i]);
				}
			}
			mRecordList.add(record);
		}
		br.close();
		
		// update decisionClass and category list
		decisionClass.update();
		for ( Category cat : mCategoryList ) {
			cat.update();
		}
		decisionRange = decisionClass.getRangeStrAry();
		decisionRangeSize = decisionRange.length;
		
	}
	//----------------------------------------------------------------------
	public void makeDecisionNode(DecisionNode node) {
		//-----------------------------------
		Attribute tmpAttr, tmpAttr2;
		Category tmpC = null;
		InformationRecord tmpR = null;
		int tmpV = 0;
		//-----------------------------------
		
		StringBuilder sb1 = new StringBuilder();
		
		if ( node.getRecordList().size() <= 0) {
			System.out.println("record size == 0");
			return;
		}
		if ( node.getCategoryList().size() <= 0 ) {
			System.out.println("category size == 0");
			
			Set<String> rangeSet = new HashSet<String>();
			for ( Record r : node.getRecordList() ) {
				rangeSet.add(r.getList().get(decisionClass.getIndex()).getValue());
			}
			String tmpStr[] = rangeSet.toArray(new String[0]);
			for ( String s : tmpStr ) {
				sb1.append(s).append("/");
			}
			sb1.setLength(sb1.length()-1);
			node.setAttribute(sb1.toString());
			return;
		}
		System.out.println("+++++++++++Record List+++++++++++++++");
		System.out.println(node.getRecordList());
		System.out.println("+++++++++++End Record +++++++++++++++");
		List<InformationRecord> infoList = new ArrayList<InformationRecord>();
		for ( int i = 0 ; i < node.getCategoryList().size(); i++ ) {
			infoList.add(new InformationRecord(decisionRangeSize));
			infoList.get(i).setCategory(node.getCategoryList().get(i));
		}
		
		for ( int k = 0 ; k < node.getCategoryList().size() ; k++ ) {
			tmpC = node.getCategoryList().get(k);
			tmpR = infoList.get(k);
			for ( Record r : node.getRecordList() ) {
				tmpAttr = r.getList().get(tmpC.getIndex());
				tmpAttr2 = r.getList().get(decisionClass.getIndex());
				for(int i = 0 ; i < tmpC.getRangeSize() ;i++ ) {
					if ( tmpAttr.getValue().equals(tmpC.getRangeI(i)) ) {
						// range match
						for (int j = 0 ; j < decisionRangeSize; j++ ) {
							if ( tmpAttr2.getValue().equals(decisionRange[j])){
								tmpV = tmpR.getPi(j,i);
								tmpV++;
								tmpR.setPi(j,i, tmpV);
								break;
							}
						}
					}
				}
			}
			tmpR.countInfoGainNew();
			System.out.println("-----------information entropy----------");
			sb1.setLength(0);
			sb1.append(tmpC.getCategory()).append(" ");
			for(int i = 0; i < decisionRangeSize ; i++) {
				sb1.append("p"+i).append(" ");
			}
			sb1.append("info");
			
			System.out.println("\n"+sb1.toString());
			for(int i = 0 ; i < tmpC.getRangeSize() ;i++ ) {
				
				sb1.setLength(0);
				sb1.append(tmpC.getRangeI(i)).append(" ");
				for(int j = 0; j < decisionRangeSize ; j++) {
					sb1.append(tmpR.getPi(j, i)).append(" ");
				}
				sb1.append(tmpR.getInformation(i));
				System.out.println(sb1.toString());
			}
			System.out.println("--------------end-----------------------");
		}
		System.out.println("-----------information detail----------");
		for ( int i = 0 ; i < infoList.size() ; i++ ) {
			System.out.println( infoList.get(i));
		}
		System.out.println("--------------end----------------------");
		// find max info gain
		InformationRecord maxInfo = null;
		for ( int i = 0 ; i <  infoList.size() ; i++ ) {
			if ( maxInfo == null ) {
				maxInfo = infoList.get(i);
			} else {
				if ( infoList.get(i).getGain() > maxInfo.getGain() ) {
					maxInfo = infoList.get(i);
				}
			}
		}
		//System.out.println("max information gain in root = " + maxInfo);
	    //consturct tree
        List<DecisionBranch> branchList = node.getDecisionBranch();
        tmpC = maxInfo.getCategory();
        node.setEntroy(maxInfo.getEntroy());
        node.setGain(maxInfo.getGain());
        node.setTotalInfo(maxInfo.getTotalInfo());
        node.setAttribute(tmpC.getCategory());
        Set<String> maxInfoRangeSet = new HashSet<String>();
        for ( Record r : node.getRecordList() ) {
        	maxInfoRangeSet.add(r.getList().get(tmpC.getIndex()).getValue());
        }
        String maxInfoRangeString[] = maxInfoRangeSet.toArray(new String[0]);
        for ( int i = 0 ; i < maxInfoRangeString.length ; i++ ) {
        	branchList.add(new DecisionBranch(maxInfoRangeString[i]));
        	for ( Category c : node.getCategoryList() ) {
        		if ( c.getCategory().equals(maxInfo.getCategory().getCategory()) == false ) {
        			//System.out.println("add category : " + c.getCategory() );
        			branchList.get(i).getNode().getCategoryList().add(c);
        		}
        	}
        }
        /*
        for (DecisionBranch b : branchList) {

        	//System.out.println("branch " + b.node);
        	System.out.println("cond : " + b.getBranchCondition());
        	for ( Category c : b.getNode().getCategoryList() ) {
        		System.out.println("category : " + c.getCategory() );
        	}
        	//System.out.println("size :" + b.node.getRecordList().size() );
        }
        */
        // split record
        for ( Record r : node.getRecordList() ) {
        	tmpAttr = r.getList().get(tmpC.getIndex());
        	// compute pi and ni
        	for( int i = 0 ; i < maxInfoRangeString.length ; i++ ) {
        		if ( tmpAttr.getValue().equals(maxInfoRangeString[i]) ) {
        			branchList.get(i).getNode().getRecordList().add(r);
        			break;
        		}
        	}
        }
        // print branch list
        for ( int i = 0 ; i < maxInfoRangeString.length ; i++ ) {
        	System.out.println(branchList.get(i).getNode().getRecordList());
        }
        if ( node.getGain() > 0.0 ) {
        	for (DecisionBranch b : branchList) {
        		//System.out.println("-------------branch--analysis-------");
        		if ( b.getNode().getRecordList().size() > 0 ) {
        			makeDecisionNode(b.getNode());
        		} 
        		//System.out.println("-------------branch--end-------");
        	}
        } else {
        	String result = node.getRecordList().get(0).getList().get(decisionClass.getIndex()).getValue();
        	branchList.clear();
        	node.setAttribute(result);
        }
	}
	
	//----------------------------------------------------------------------
	public void makeDecisionTree(String file) throws Exception {
		parseInputFile(file);
		
		System.out.println("Decison Category");
		System.out.println(decisionClass);
		
		System.out.println("category");
		for ( Category cat : mCategoryList ) {
			System.out.println(cat);
		}
		System.out.println("record");
		for ( Record rec : mRecordList) {
			System.out.println(rec);
		}
		List<Record> recordList = root.getRecordList();
		for ( Record rec : mRecordList) {
			recordList.add(rec);
		}
		List<Category> catList = root.getCategoryList();
		for ( Category c : mCategoryList ) {
			catList.add(c);
		}
		makeDecisionNode(root);
		System.out.println("------------------------------");
		System.out.println("Decision Tree \n" + root);
		System.out.println("------------------------------");
	}
	//----------------------------------------------------------------------
	public void parseTestFile(String file) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		String tokens[] = null;
		Record record = null;
		testRecordList.clear();
		List<Attribute> attrList = null;
		//parse record attribute
		while ((line = br.readLine()) != null) {
			// process the line.
			//System.out.println(line);
			tokens = line.split("\\s");
			record = new Record();
			attrList = record.getList();
			for ( int i = 0 ; i < mCategoryList.size() ; i++ ) {
				attrList.add(new Attribute(mCategoryList.get(i).getCategory(),tokens[i]));
				mCategoryList.get(i).getRangeSet().add(tokens[i]);
			}
			testRecordList.add(record);
		}
		br.close();


	}
	public void doDecision(String file) throws Exception {
		parseTestFile(file);
		for ( Record r : testRecordList ) {
			System.out.println(r + "---> " + root.makeDecision(r) );
		}
	}
	public DecisionNode getDecisionTree() {
		return root;
	}
	//----------------------------------------------------------------------
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WJY decision tree ID3 algorithm");
		DecisionTreeDriver dt = new DecisionTreeDriver();
		dt.makeDecisionTree(args[0]);
		if ( args.length >= 2 ) {
			dt.doDecision(args[1]);
		}
	}

}
