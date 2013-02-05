package edu.umd.cs.argviz.client;

import java.util.ArrayList;

public class ArgumentComparison {

	private ArrayList<ArgumentCollocation> side1;
	private ArrayList<ArgumentCollocation> side2;
	private String name;
	
	ArgumentComparison(String name, ArrayList<ArgumentCollocation> l1, ArrayList<ArgumentCollocation> l2){
		this.name = name;
		this.side1 = l1;
		this.side2 = l2;
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<ArgumentCollocation> getSide1 (){
		return side1;
	}
	
	public ArrayList<ArgumentCollocation> getSide2 (){
		return side2;
	}
	
	
}