package edu.umd.cs.argviz.client;

import com.google.gwt.user.client.ui.HTML;

public class ShiftHtml  {
	
	private int speaker ;
	private double score ;
	
	private String colorMapper( int id){
		String[] colors = {"orange","red", "blue","black"};
		if(id<colors.length) 
			return colors[id]; 
		return "yellow";		
	}
	
	ShiftHtml( int speaker, double score ) {
		this.speaker = speaker;
		this.score = score;			
	}
	
	public HTML getHtml(){
		
		String color = colorMapper(speaker);
		score = score*100/2;
		double w[]=new double[3];
		
		
		if(score<0){
			score = -score;
			w[0]=50-score;
			w[1]=score;
			w[2]=50;			
		}
		else{
			w[0]=50;
			w[1]=score;
			w[2]=50-score;			
		}	

		
		HTML htmlString = new HTML(				
				"<div style='background-color:#E0E0E0;width:"+ w[0]+"%;float:left;height:25px;'>&nbsp;</div>" +
				"<div style='background-color:"+color+";width:"+w[1]+"%;float:left;height:25px'>&nbsp;</div>" +
				"<div style='background-color:#E0E0E0;width:"+w[2]+"%;float:left;height:25px'>&nbsp;</div>"				
				);		
		
		System.err.println(" after converted to html "+htmlString.toString());
		
		return htmlString;
	}
	
	
	

}
