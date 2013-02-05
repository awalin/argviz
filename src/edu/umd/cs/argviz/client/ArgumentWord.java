package edu.umd.cs.argviz.client;

public class ArgumentWord {

	private String text;
	private String color;
	
	public ArgumentWord(String w){
		this.setText(w);
		this.setColor("");
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
	
	
	
}
