package edu.albany.cs.graph;


import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import edu.albany.cs.apdmIO.APDMInputFormat;

public class TransWeatherGraph extends Graph {

	/** each node i has an observation x[i] */
	public final double[] x;
	/** each node i has expectation of observation x[i] */
	public final double[] mu;
	/** each node i has standard deviation of observation x[i] */
	public final double[] sigma;
	
	public TransWeatherGraph(APDMInputFormat apdm) {
		super(apdm);
		this.x = apdm.data.counts;			
		this.mu = apdm.data.mean;
		this.sigma = apdm.data.std;
		
	}
	
	public TransWeatherGraph(APDMInputFormat apdm,String type) {
		super(apdm);
		this.x = apdm.data.attributes[0];
		DescriptiveStatistics stats = new DescriptiveStatistics();
		 for (int i = 0; i < this.x.length; i++) {
	            stats.addValue(this.x[i]);                
	    }
       
        //calculate average value
		 this.mu=new double[this.x.length];
		 this.sigma=new double[this.x.length];
		 
		 for (int i = 0; i < this.x.length; i++) {
	   
			 this.mu[i] = stats.getMean();
			 this.sigma[i] = stats.getStandardDeviation();
		 }
	}
	

	@Override
	public String toString() {
		return "DifferentialPrivacyGraph [x=" + Arrays.toString(x) + ", mu=" + Arrays.toString(mu) + ", sigma="
				+ Arrays.toString(sigma) + "]";
	}
	

}
