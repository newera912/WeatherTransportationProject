package edu.albany.cs.transWeather;

public class MultiVarTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MultiVarTransWeatherTest test=null;
		for(String mon:new String[]{"201604","201605","201606","201607","201608","201609"}){
			test=new MultiVarTransWeatherTest();
			test.CaseStudy(18, 2, mon);
		}
	}

}
