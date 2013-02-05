package edu.umd.cs.argviz.client;

import java.util.ArrayList;

public class ArgumentTurn {
	
	private int turnID;
	private int speakerID;
	private ArrayList<ArgumentWord> words;
	private int previewSize;
	private boolean expanded;
	
	public ArgumentTurn(int tid, int sid, int psize, boolean exp){
		this.setTurnID(tid);
		this.setSpeakerID(sid);
		this.setPreviewSize(psize);
		this.setExpanded(exp);
		
		this.words = new ArrayList<ArgumentWord>();
	}

	public void setWords(String[] words) {
		this.words.clear();
		for(String word: words){
			this.words.add(new ArgumentWord(word));
		}
	}
	
	public String getFullText() {
		String full = new String();
		boolean skip = true;
		
		for (ArgumentWord w : this.words) {
			if (skip) {
				skip = false;
			} else
				full += " ";
				
			full += w.getText();
		}
		
		return full;
	}
	
	public String getPreview() {
		String preview = new String();
		int i;
		
		if (this.previewSize > this.words.size())
			return this.getFullText();
		
		for (i=0; i < this.previewSize; i++) {
			if (i > 0) {
				preview += " ";
			}
				
			preview += this.words.get(i).getText();
		}
		
		return preview;
	}
	
	public String getBar() {
		return "" + this.speakerID;
	}
	
	public String getMap() {
		return "HeatMap";
	}
	
	public ArrayList<ArgumentWord> getWords() {
		return words;
	}

	public void setTurnID(int turnID) {
		this.turnID = turnID;
	}

	public int getTurnID() {
		return turnID;
	}

	public void setSpeakerID(int speakerID) {
		this.speakerID = speakerID;
	}

	public int getSpeakerID() {
		return speakerID;
	}

	public void setPreviewSize(int previewSize) {
		this.previewSize = previewSize;
	}

	public int getPreviewSize() {
		return previewSize;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public boolean isExpanded() {
		return expanded;
	}
}
