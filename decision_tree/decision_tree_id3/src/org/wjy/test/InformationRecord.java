package org.wjy.test;

import java.util.ArrayList;
import java.util.List;

public class InformationRecord {
	private ArrayList<ArrayList<Integer>> piList = null;
	private int [] piListTotal = null;
	private int piListSize = 0;
	//private List<Integer> piList = new ArrayList<Integer>();
	//private List<Integer> niList = new ArrayList<Integer>();
	private List<Double> information = new ArrayList<Double>();
	private List<Integer> subSample = new ArrayList<Integer>();
	private Category category;
	private int length = 0;
	private double entropy =0.0f;
	private double total_info = 0.0f;
	private double gain = 0.0f;
    public InformationRecord(int rangeSize) {
		//piList.clear();
		//niList.clear();
		information.clear();
		subSample.clear();
		piListSize = rangeSize;
		piList = new ArrayList<ArrayList<Integer>>();
		piListTotal = new int[piListSize];
		for(int i = 0 ; i < piListSize ; i++ ) {
			piList.add(new ArrayList<Integer>());
		}
    }
    public void setCategory(Category c) {
    	category = c;
    	setLength(category.getRangeSet().size());
    	reset();
    }
    public Category getCategory() {
    	return category;
    }
    public void setLength(int length) {
    	//piList.clear();
		//niList.clear();
    	for(int i = 0 ; i < piListSize ; i++ ) {
    		piList.get(i).clear();
    	}
		information.clear();
		subSample.clear();
		this.length = length;
		
		for(int i = 0 ; i < piListSize ; i++ ) {
    		for(int j = 0 ; j < length ; j++ ) {
    			piList.get(i).add(0);
    		}
    	}
		
        for (int i=0; i<length; i++) {
            //piList.add(0);
            //niList.add(0);
            information.add(0.0);
            subSample.add(0);
        }
    } 
    public int getPi(int range, int index) {
    	return piList.get(range).get(index);
    }
    public void setPi(int range,int index,int value){
    	piList.get(range).set(index, value);
    }
    /*
    public void setPi(int index, int value) {
        piList.set(index,value);
    }
    public int getPi(int index) {
        return piList.get(index);
    }
    public void setNi(int index, int value) {
        niList.set(index,value);
    }
    public int getNi(int index) {
        return niList.get(index);
    }
    */
    public void setInformation(int index,double value){
        information.set(index,value);
    }
    public double getInformation(int index) {
        return information.get(index);
    }
    
    public void countInfoGainNew() {
    	double tmp1 = 0;
    	double tmp2 = 0;
    	double tmp = 0.0;
    	int total_sample = 0;
    	for ( int i = 0 ; i < piListSize ; i++ ) {
    		piListTotal[i] = 0;
    		for ( int x : piList.get(i) ) {
    			piListTotal[i]+=x;
    			//System.out.println("i " + i + " = x = " + x);
    		}
    		total_sample += piListTotal[i];
    	}
    	//System.out.println("total_sample = " + total_sample);
    	// total info
    	tmp = 0 ;
    	for ( int i = 0 ; i < piListSize ; i++ ) {
			tmp2 = countInfoGeneric(piListTotal[i],total_sample);
			tmp = tmp + tmp2;
    	}
    	total_info = tmp ;
    	//System.out.println("total_info = " + total_info);
    	
    	int sub_sample = 0;
    	for ( int i = 0 ; i < length ; i++ ) {
    		sub_sample = 0;
    		for ( int j = 0 ; j < piListSize ; j++ ) {
    			sub_sample += piList.get(j).get(i);
    		}
    		subSample.set(i, sub_sample);
    		//System.out.println("i " + i + " = sample = " + sub_sample);
    	}
    	tmp = 0.0;
    	for( int i = 0 ; i < length ; i++ ) {
    		tmp1 = 0;
    		for ( int j = 0 ; j < piListSize ; j++ ) {
    			tmp2 = (double)(subSample.get(i))/(double)(total_sample);
    			tmp2 = tmp2 * countInfoGeneric(piList.get(j).get(i),subSample.get(i));
    			tmp1 += tmp2;;
    		}
    		setInformation(i,tmp1);
    		tmp += tmp1;
    	}
    	entropy = tmp;
    	gain = total_info - entropy;
    }
    /*
    public void countInfoGain() {
    	int p = 0;
    	int n = 0;
    	for ( int i = 0 ; i < length ; i++ ) {
    		setInformation(i, countInfo(getPi(i),getNi(i)));
    		p += getPi(i);
    		n += getNi(i);
    	}
    	total_info = countInfo(p,n); //I(p,n)
    	double tmp1 = 0.0;
    	double tmp2 = 0.0;
    	double tmp = 0.0;
    	int tmpI = 0;
    	for ( int i = 0 ; i < length ; i++ ) {
    		tmpI = getPi(i) + getNi(i);
    		tmp1 = (double)tmpI/(double)(p+n);
    		tmp2 = getInformation(i);
    		tmp += ((tmp1)*tmp2);
    	}
    	entropy = tmp;
    	gain = total_info - entropy;
    }
    */
    public double getEntroy() {
    	return entropy;
    }
    public double getGain() {
    	return gain;
    }
    public double getTotalInfo() {
    	return total_info;
    }
    public void reset() {
    	entropy = 0.0f;
    	gain = 0.0f;
    	total_info = 0.0f;
    	for (int i=0; i<length; i++) {
    		//piList.set(i,0);
    		//niList.set(i,0);
    		information.set(i,0.0);
    	}
    }
    //-------------------------------------------
    @Override 
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(category.getCategory()).append(" ");
    	sb.append("gain=").append(gain).append(" ");
    	sb.append("total_info=").append(total_info).append(" ");
    	sb.append("entropy=").append(entropy).append(" ");
    	return  sb.toString() ;
    }
    //-------------------------------------------
    private double countInfoGeneric(int pi, int p) {
    	double tmp1 = 0;
    	if ( pi == 0 || p == 0 ) return tmp1;
    	tmp1 = (double)pi/(double)p;
    	//System.out.println("tmp1(before) = " + tmp1);
    	if ( tmp1 != 0) {
    		tmp1 = (-tmp1) * (Math.log(tmp1)/Math.log(2));
    	}
    	//System.out.println("tmp1(after) = " + tmp1);
    	return tmp1;
    }
    private double countInfo(int p, int n) {
		double tmp = 0;
		double tmp1 = 0;
		double tmp2 = 0;
		tmp1 = (double)p/(double)(p+n);
		//System.out.println("tmp1 = " + tmp1);
		if ( tmp1 != 0 ) {
			tmp1 = (-tmp1) * (Math.log(tmp1)/Math.log(2));
		}
		//System.out.println("tmp1 = " + tmp1);
		tmp2 = (double)n/(double)(p+n);
		if ( tmp2 != 0 ) {
			tmp2 = (-tmp2) * (Math.log(tmp2)/Math.log(2));
		}
		tmp = tmp1+tmp2;
		//System.out.println("tmp = " + tmp);
		return tmp;
	}
    
}
