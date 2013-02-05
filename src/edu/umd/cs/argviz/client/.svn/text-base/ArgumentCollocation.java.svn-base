package edu.umd.cs.argviz.client;

import java.util.ArrayList;
import java.util.HashMap;

public class ArgumentCollocation {
	private final String collocText;
	private final int side;
	
	private ArrayList<ArgumentWord> collocArgWords;
	
	private HashMap<String, Double> features;
	
	public ArgumentCollocation(String text, int side){
		this.collocText = text;
		this.side = side;
		this.collocArgWords = new ArrayList<ArgumentWord>();
		this.features = new HashMap<String, Double>();
	}
		
	public String getCollocText(){
		return this.collocText;
	}
	
	public int getSide(){
		return this.side;
	}
	
	public void addArgumentWord(ArgumentWord word){
		this.collocArgWords.add(word);
	}
	
	public ArrayList<ArgumentWord> getCollocArgumentWords(){
		return this.collocArgWords;
	}
	
	public void setCollocArgumentWords(ArrayList<ArgumentWord> words){
		this.collocArgWords = words;
	}
	
	public void addFeature(String key, Double value){
		this.features.put(key, value);
	}
	
	public Double getFeature(String key){
		return this.features.get(key);
	}
}
