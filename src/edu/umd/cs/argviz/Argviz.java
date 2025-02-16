package edu.umd.cs.argviz.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import edu.umd.cs.argviz.shared.FieldVerifier;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
//import com.smartgwt.client.widgets.Label;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Argviz implements EntryPoint {

	private RootPanel rootPanel;
	private HorizontalPanel topPanel = new HorizontalPanel();
	private VerticalPanel fullPanel = new VerticalPanel();
	private ScrollPanel makeItRoll;
	private FlexTable turnTable = new FlexTable();
	private HorizontalPanel comparisonPanel = new HorizontalPanel();
	private HorizontalPanel topicPanel = new HorizontalPanel();
	private Grid grid = new Grid(numRows, numCols);
	private FlowPanel[][] gridPanels = new FlowPanel[numRows][numCols];
	private FlowPanel zoomTopicPanel = new FlowPanel();
	private HorizontalPanel sidesPanel = new HorizontalPanel();
	private HorizontalPanel fightPanel = new HorizontalPanel();
	private VerticalPanel speakerPanel = new VerticalPanel();
	private ListBox speakerList = new ListBox();
	private ArrayList<Integer> highlightedSet = new ArrayList<Integer>();
	private ListBox sideBox1 = new ListBox();
	private ListBox sideBox2 = new ListBox();
	private ArrayList<ArgumentCollocation> Side1topNCollocs = new ArrayList<ArgumentCollocation>();		
	private ArrayList<ArgumentCollocation> Side2topNCollocs = new ArrayList<ArgumentCollocation>();		

	public static final int STATUS_CODE_OK = 200;
	
	public static final int MIN_FONT_SIZE = 8;
	public static final int MAX_FONT_SIZE = 20;
	private static final int NUM_SIDE_COLLOCS = 10;
	
	public static int numWordPerTopicSmall = 10;
	public static int numWordPerTopicLarge = 20;
	public static int numRows = 1;
	public static int numCols = 25;

	// all vocabularies
	public static ArrayList<String> vocab; // word vocabulary	
	public static ArrayList<String> speakerVocab; // contains distinct speaker names
	//public static HashMap<Integer, ArrayList<String>> collocVocab; 
	
	// distributions
	public static double[][] topic_word_distributions; // topic-word distributions (for word cloud)
	public static ArrayList<float[]> turn_topic_distributions; // turn-topic distributions (for heatmap)
		
	// information about turns 
	public static double[] turnTopicShiftScores; // better to access this score through ArgumentTurn  	
	public static int[] turnSpeakerIDs; // better to access the speaker through ArgumentTurn
	public static ArrayList<ArgumentTurn> allTurns; // store all turns
	public static HashMap<Integer, String> speakerMap = new HashMap<Integer, String>();
	
	
	// inverted indexing
	/*Map contains all the ArgumentWord (those appear in the vocabulary)*/
	public static HashMap<String, ArgumentWord> wordTable;
	
	/*2 Maps contains the ArgumentCollocation corresponding to 2 sides
	 * To get all ArgumentCollocation of side 1, use argCollocTables.get(1)
	 * To get all ArgumentCollocation of side 2, use argCollocTables.get(2)
	 * To sort, use the features in each ArgumentCollocation object*/
	public static HashMap<Integer, HashMap<String, ArgumentCollocation>> argCollocTables;
	
	private static boolean verbose = true;
	private static int previewSize = 6; // changed from 20 to 6
	
	public static ArrayList<ArgumentComparison> comparisons;
	
	/**
	 * This is the entry point method.
	 */
	
	public void onModuleLoad() {
		getData();
	}
	
	private void showVisualization(){
		rootPanel = RootPanel.get();
		
		turnTable.setText(0, 0, "Topic Shift");
		turnTable.setText(0, 1, "Turn");
		turnTable.setText(0, 2, "Topic Distribution");

		/* Load data into the turnTable */

		int i;
		for(i = 0; i < allTurns.size(); i++){
			ArgumentTurn turn = allTurns.get(i);
			DisclosurePanel textCell = new DisclosurePanel(); 
			/* Add the bar graph */
			
			double score = turnTopicShiftScores[i];
			int speaker = turnSpeakerIDs[i];
			ShiftHtml phase = new ShiftHtml(speaker,score);			
			turnTable.setWidget(i+1,0,phase.getHtml() );
			
			/* Add the primary text */
			textCell.setHeader(new Label(turn.getPreview()));
			textCell.setContent(new Label(turn.getFullText()));
			
			turnTable.setWidget(i+1, 1, textCell);
			
			turnTable.addStyleName("turnTable");
		    turnTable.getCellFormatter().addStyleName(i, 0, "turnTableGraphColumn");
		    turnTable.getCellFormatter().addStyleName(i, 1, "turnTableMainColumn");
	
			/* Add the heat map */

			turnTable.setWidget(i+1, 2, getViewPanel(turn_topic_distributions.get(i)));
		}

	    // Add styles to elements in the turn table
	    turnTable.getRowFormatter().addStyleName(0, "turnTableHeader");
	    turnTable.getCellFormatter().addStyleName(0, 2, "turnTableHeatmapColumn");	    

	    /* Code for the topic panel */		
		// grid: clouds of word-clouds
		for (int row = 0; row < numRows; ++row) {
	    	for (int col = 0; col < numCols; ++col){
	    		gridPanels[row][col] = new FlowPanel();
//	    		gridPanels[row][col].setStylePrimaryName("cloudWrap");
	    		grid.setWidget(row, col, gridPanels[row][col]);
	    	}
		}
		
		// click handler for 1 word-cloud cell
		grid.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				HTMLTable.Cell cell = grid.getCellForEvent(event);
				int row = cell.getRowIndex();
				int col = cell.getCellIndex();
				int topicIndex = row * numRows + col;				
				showSelectedWordCloud(topicIndex, numWordPerTopicLarge, zoomTopicPanel);
			}
		});   	    
	    	    
	    // zoom topic panel
//	    zoomTopicPanel.setHeight("200px");//change this Sopan
		zoomTopicPanel.addStyleName("zoomTopicPanel");
	    topicPanel.add(zoomTopicPanel);

//		grid.getCellFormatter().setWidth(0, 2, "256px");//can change this Sopan
	    grid.addStyleName("cloudWrap");
	    topicPanel.add(grid);
		topicPanel.setBorderWidth(1);
		
		
		/*Comparison panel*/
		HashMap<String,ArgumentCollocation> sideCollocTable;
		
		/* Set up side 1 */
		sideCollocTable = argCollocTables.get(1);
		Side1topNCollocs = getTopN(sideCollocTable, NUM_SIDE_COLLOCS, "frequency");
		sideBox1.setVisibleItemCount(5);
			
		sideBox1.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent event)
			{
				int selectedIndex = sideBox1.getSelectedIndex();
				highlightCollocTurns(Side1topNCollocs.get(selectedIndex));
			}
		});
			
		comparisonPanel.add(sideBox1);
		
		/* Set up side 2 */
		sideCollocTable = argCollocTables.get(2);
		Side2topNCollocs = getTopN(sideCollocTable, NUM_SIDE_COLLOCS, "frequency");
			
		sideBox2.setVisibleItemCount(5);
			
		sideBox2.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent event)
			{
				int selectedIndex = sideBox2.getSelectedIndex();
				highlightCollocTurns(Side2topNCollocs.get(selectedIndex));
			}
		});
			
		comparisonPanel.add(sideBox2);

		updateSides();

		/* Build radio buttons for comparison panel */
		for (final String speaker : speakerVocab){
			final RadioButton speakerButton = new RadioButton("speakers", speaker);
			speakerButton.addClickHandler(new ClickHandler()
			{
			public void onClick(ClickEvent event)
			{				
				if (speakerButton.getValue()){
					Side1topNCollocs = getTopN(argCollocTables.get(1), NUM_SIDE_COLLOCS, speaker);
					Side2topNCollocs = getTopN(argCollocTables.get(2), NUM_SIDE_COLLOCS, speaker);
					updateSides();
				}
			}
			});
		
			speakerPanel.add(speakerButton);
		}
		
		final RadioButton freqButton = new RadioButton("speakers", "frequency");
		freqButton.addClickHandler(new ClickHandler()
			{
			public void onClick(ClickEvent event)
			{
				if (freqButton.getValue()){
					Side1topNCollocs = getTopN(argCollocTables.get(1), NUM_SIDE_COLLOCS, "frequency");
					Side2topNCollocs = getTopN(argCollocTables.get(2), NUM_SIDE_COLLOCS, "frequency");
					updateSides();
				}
			}
			});
		
		speakerPanel.add(freqButton);

		comparisonPanel.add(speakerPanel);
		comparisonPanel.addStyleName("comparisonPanel");
		
		turnTable.setWidth("100%");
		turnTable.setHeight("100%");

		makeItRoll = new ScrollPanel(turnTable);
		makeItRoll.setSize("800px", "600px");
		topPanel.add(makeItRoll);
		topPanel.add(comparisonPanel);
	    
		fullPanel.add(topPanel);
		fullPanel.add(topicPanel);
		rootPanel.add(fullPanel, 0, 0);
		
	}
	
	private void updateSides(){
		sideBox1.clear();
		sideBox2.clear();
		
		for (ArgumentCollocation ac : Side1topNCollocs){
			sideBox1.addItem(ac.getCollocText());
		}
		for (ArgumentCollocation ac : Side2topNCollocs){
			sideBox2.addItem(ac.getCollocText());
		}
	}
	
	private void highlightCollocTurns(ArgumentCollocation ac) {
		ArrayList<ArgumentWord> aws = ac.getCollocArgumentWords();
		int i;
		clearHighlights();
		Set<Integer> unionSet = aws.get(0).getTurnIDs();

		for(i=1;i<aws.size();i++){
			unionSet.retainAll(aws.get(i).getTurnIDs());
		}

		
		
		for(Integer tid : unionSet){
			highlightTurn(tid);
		}
	}

	public void highlightTurn(int turn){
		turnTable.getCellFormatter().setStyleName(turn+1, 1, "turnTableMainColumnHighlighted");
		highlightedSet.add(turn);
	}
	
	public void clearHighlights(){
		for (Integer i : highlightedSet){
			turnTable.getCellFormatter().setStyleName(i+1, 1, "turnTableMainColumn");
		}
		highlightedSet.clear();
	}
	
	public void highlightWordTurns(ArgumentWord aw){
		
		clearHighlights();
		
		for(Integer tid : aw.getTurnIDs()){
			highlightTurn(tid);
		}
	}
	
	public void highlightStringTurns(String word){
		clearHighlights();
		highlightWordTurns(wordTable.get(word));
	}
	
	/**Display the "cloud" of word clouds*/
	public void displayWordClouds(){		
		int topicCount = 0;
	    for (int row = 0; row < numRows; ++row) {
	    	for (int col = 0; col < numCols; ++col){
	    		if(topicCount == topic_word_distributions.length)
	    			break;
	    			 
	    		HashMap<Integer, Integer> topWordLabelSize = 
	    			getTopWordLabelSize(topic_word_distributions[topicCount], numWordPerTopicSmall);	    			    		
	    		gridPanels[row][col].clear();
	    		for(int wordIndex : topWordLabelSize.keySet()){
					//Hyperlink tagLink = new Hyperlink(vocab.get(wordIndex),vocab.get(wordIndex));
					//tagLink.setStylePrimaryName("cloudTags");
					Label wordLabel = new Label(vocab.get(wordIndex));
					Style linkStyle = wordLabel.getElement().getStyle();
					String labelSize = Integer.toString(topWordLabelSize.get(wordIndex)) + "pt";
		            linkStyle.setProperty("fontSize", labelSize);
		            
		            gridPanels[row][col].add(wordLabel);
				}
	    		topicCount ++;
	    	}	        
	    }
	}
	
	public Canvas getViewPanel(float[] topicDistributions) {
		final ListGrid heatmapGrid = new ListGrid() {
			protected String getCellStyle( ListGridRecord record, int rowNum,int colNum) {
				HeatmapRecord HeatmapRecord = (HeatmapRecord) record;
				float arr[]=HeatmapRecord.getValues();
				int  intensity = map(1.0f,0.0f,arr[colNum]);
				String intense = "intensity-"+intensity;				
				return intense;
			}
		};
//		heatmapGrid.setWidth("22.5%");
		heatmapGrid.setHeight("25px");
		heatmapGrid.setBorder("none");
		heatmapGrid.setCanSort(false);
		heatmapGrid.setShowAllRecords(true);
		heatmapGrid.setShowHeaderContextMenu(false);
		heatmapGrid.setShowHeader(false);		
		
		ListGridField[] listGridFields = new ListGridField[ topicDistributions.length];
		
		for(int i=0; i<listGridFields.length; i++){
			 ListGridField f = new ListGridField("T" + (i+1), "Topic " + (i+1));
			 f.setType(ListGridFieldType.FLOAT);
			 listGridFields[i] = f;
		}
		heatmapGrid.setFields(listGridFields);
		heatmapGrid.setData( new HeatmapRecord[]{ HeatmapData.getRecord(topicDistributions)});
		
		heatmapGrid.addCellClickHandler(new CellClickHandler() {
			
			@Override
			public void onCellClick(CellClickEvent event) {
				// TODO Auto-generated method stub
				int row = event.getRowNum();
				int col = event.getColNum(); // now equals to the topic index
				showSelectedWordCloud(col, numWordPerTopicLarge, zoomTopicPanel);
				/*HTMLTable.Cell cell = grid.getCellForEvent(event);
				int row = cell.getRowIndex();
				int col = cell.getCellIndex();
				int topicIndex = row * numRows + col;				
				showWordCloud(topicIndex, numWordPerTopicLarge, zoomTopicPanel);*/
			}
		});
		
		return heatmapGrid;
	}
	
	/**Display a single word cloud at the bottom of the Topic Panel once 
	 * a topic is selected
	 * @param topicIndex The index of the selected topic
	 * @param numTopWord The number of words to be shown in the word cloud
	 * @param container The panel to contain the word cloud*/
	public void showSelectedWordCloud(int topicIndex, int numTopWord, FlowPanel container){		
		HashMap<Integer, Integer> topWordLabelSize = 
			getTopWordLabelSize(topic_word_distributions[topicIndex], numTopWord);
		container.clear();
		for(int wordIndex : topWordLabelSize.keySet()){
			final Label clickableWordLabel = new Label(vocab.get(wordIndex));
			clickableWordLabel.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					ArgumentWord argWord = wordTable.get(clickableWordLabel.getText());
					highlightWordTurns(argWord);
				}
			});
			
			Style linkStyle = clickableWordLabel.getElement().getStyle();
			String labelSize = Integer.toString(topWordLabelSize.get(wordIndex)) + "pt";
            linkStyle.setProperty("fontSize", labelSize);
            
            container.add(clickableWordLabel);
		}
	}

	/**Call the php file on the server*/
	public void getData(){
	    String protocol = com.google.gwt.user.client.Window.Location.getProtocol();
		String host = com.google.gwt.user.client.Window.Location.getHostName();
		String path = com.google.gwt.user.client.Window.Location.getPath();
		
		String base = protocol + "//" + host + "" + path;
		
		base = base.substring(0,base.length()-11);
		String url = base + "debateio.php";
		
		String convo = com.google.gwt.user.client.Window.Location.getParameter("convo");
	    
	    RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST,url);
	    requestBuilder.setHeader("Content-Type", "application/x-www-form-urlencoded");
	    try{
	    	StringBuffer postData = new StringBuffer();
	    	postData.append(URL.encode("conversation")).append("=").append(URL.encode(convo));
	    		    	
	        requestBuilder.sendRequest(postData.toString(), new RequestCallback() {
	            public void onResponseReceived(Request request, Response response){
	                if (response.getStatusCode() == STATUS_CODE_OK){ 
	                	handleResponse(response.getText());
	                	showVisualization();	                	
	                	displayWordClouds();
	                }
	                else{
	                	Window.alert("Response error "+ response.getText());
	                }
	            }   
	  
	            public void onError(Request request, Throwable exception){
	            	Window.alert("On Error");
	                throw new UnsupportedOperationException("Not supported yet.");  
	            }	            
	        });
	    } catch (Exception e){
	    	Window.alert("Exception: " + e.getMessage());
	        e.printStackTrace();  
	    }  
	}
	/**Handle the response from the server, parse the response text into JSON objects
	 * @param response The response JSON text from php*/
	public void handleResponse(String response){
		JSONValue jsonValue = JSONParser.parse(response);
		JSONObject jsonObj = jsonValue.isObject();
		
		// vocabulary array
		JSONArray jsonVocabArray = jsonObj.get("vocabulary").isArray();
		createVocabulary(jsonVocabArray);
		
		// list of speakers in each turn
		JSONArray jsonSpeaker = jsonObj.get("speaker").isArray();
		createSpeakers(jsonSpeaker);
		
		// topic shift scores
		JSONArray jsonTurnTopicShiftScores = jsonObj.get("topic_shift_score").isArray();
		createTurnTopicShiftScore(jsonTurnTopicShiftScores);
		
		// get turn texts and inverted index
		JSONArray jsonText = jsonObj.get("text").isArray();
		createTurns(jsonText);
				
		
		// topic-word distributions (for word clouds)
		JSONArray jsonTopic = jsonObj.get("topic_word_distribution").isArray();
		createTopicWordDistribution(jsonTopic);
		
		// turn-topic distributions (for heat map)
		JSONArray jsonTurnTopicDistr = jsonObj.get("turn_topic_distribution").isArray();
		createTurnTopicDistribution(jsonTurnTopicDistr);
		
		// crreate list of collocations with their frequencies
		JSONArray jsonColloc = jsonObj.get("collocation").isArray();
		createCollocations(jsonColloc);
		
		// load collocations for each speaker with their framing (fighting phrase) scores
		JSONObject jsonFramingScores = jsonObj.get("framing_score").isObject();
		createSpeakerFramingScore(jsonFramingScores);
	}
		
	
	/**Create the vocabulary from JSON*/
	public void createVocabulary(JSONArray jsonVocabArray){
		vocab = new ArrayList<String>();//new String[jsonVocabArray.size()];
		for(int i=0; i<jsonVocabArray.size(); i++){
			vocab.add(jsonVocabArray.get(i).toString().replace("\"", ""));
		}
		
		if(verbose)
			Window.alert("Word vocabulary created: " + vocab.size());
	}
	
	/**Read and index the speakers*/
	public void createSpeakers(JSONArray jsonSpeaker){
		// read all speakers
		ArrayList<String> speakers = new ArrayList<String>();
		Set<String> speakerSet = new HashSet<String>();
		for(int i=0; i<jsonSpeaker.size(); i++){
			String speaker = jsonSpeaker.get(i).toString().replace("\"", "");
			speakers.add(speaker);
			speakerSet.add(speaker);
		}
		
		// create speaker vocab
		speakerVocab = new ArrayList<String>();
		for(String speaker : speakerSet)
			speakerVocab.add(speaker);
		
		// create turn speaker list
		turnSpeakerIDs = new int[speakers.size()];
		for(int i=0; i<turnSpeakerIDs.length; i++){
			turnSpeakerIDs[i] = getSpeakerIndex(speakers.get(i));
		}
		
		if(verbose){
			Window.alert("Speaker vocab size: " + speakerVocab.size() + ". Turn speaker ID size: " + turnSpeakerIDs.length);
		}
	}
	
	/**Load topic shift scores*/
	public void createTurnTopicShiftScore(JSONArray jsonScoreArray){
		turnTopicShiftScores = new double[jsonScoreArray.size()];
		for(int i=0; i<jsonScoreArray.size(); i++)
			turnTopicShiftScores[i] = Double.parseDouble(jsonScoreArray.get(i).toString());
		
		if(verbose)
			Window.alert("Number of turns in turnTopicShiftScores: " + turnTopicShiftScores.length);
	}
	
	/**Load the content of all the turns*/
	public void createTurns(JSONArray jsonText){
		allTurns = new ArrayList<ArgumentTurn>();
		
		for(int i=0; i<jsonText.size(); i++){
			int sid = turnSpeakerIDs[i];
			String singleTurnText = jsonText.get(i).toString().replace("\"", "").replace("\\", "");
			ArgumentTurn singleTurn = new ArgumentTurn(i, sid, singleTurnText, previewSize, false);
			double topicShiftScore = turnTopicShiftScores[i];
			singleTurn.setTopicShiftScore(topicShiftScore);			
			allTurns.add(singleTurn);
		}
				
		if(verbose)
			Window.alert("Number of turns: " + allTurns.size());
		
		// inverted index words
		wordTable = new HashMap<String, ArgumentWord>();
		for(int i=0; i<vocab.size(); i++){
			String wordText = vocab.get(i);
			ArgumentWord argWord = new ArgumentWord(i, wordText);
			wordTable.put(wordText, argWord);
		}
		for(int i=0; i<allTurns.size(); i++){
			//TODO: find a better way to do the tokenization or preprocess the data
			String[] splitTurnTexts = allTurns.get(i).getTurnText().toLowerCase().split(" "); 
			for(String word : splitTurnTexts){
				ArgumentWord argWord = wordTable.get(word);
				if(argWord == null)
					continue;
				argWord.addTurnID(i);
			}
		}		
	}
	
	/**Create topic-word distribution from JSON*/
	public void createTopicWordDistribution(JSONArray jsonTopicWord){
		ArrayList<ArrayList<Double>> temp = new ArrayList<ArrayList<Double>>();
		for(int i=0; i<jsonTopicWord.size(); i++){
			ArrayList<Double> aWordDist = new ArrayList<Double>();
			JSONArray jsonSingleWord = jsonTopicWord.get(i).isArray();
			for(int j=0; j<jsonSingleWord.size(); j++)
				aWordDist.add(Double.parseDouble(jsonSingleWord.get(j).toString()));
			temp.add(aWordDist);
		}
		
		topic_word_distributions = new double[temp.get(0).size()][temp.size()];
		for(int i=0; i<temp.size(); i++){
			for(int j=0; j<temp.get(0).size(); j++)
				topic_word_distributions[j][i] = temp.get(i).get(j);
		}
		
		if(verbose)
			Window.alert("Topic-word distribution loaded: " + topic_word_distributions.length + "x" + topic_word_distributions[0].length);
	}
	
	/**Load the turn-topic distributions*/
	public void createTurnTopicDistribution(JSONArray jsonTurnTopicDistr){
		turn_topic_distributions = new ArrayList<float[]>();
		for(int i=0; i<jsonTurnTopicDistr.size(); i++){
			JSONArray topicDistr = jsonTurnTopicDistr.get(i).isArray();
			float[] singleTurnTopicDistr = new float[topicDistr.size()];
			for(int j=0; j<singleTurnTopicDistr.length; j++)
				singleTurnTopicDistr[j] = Float.parseFloat(topicDistr.get(j).toString());
			turn_topic_distributions.add(singleTurnTopicDistr);
		}
		
		//debug
		if(verbose){
			Window.alert("Number of turns from turn_topic_distributions: " + turn_topic_distributions.size());
			Window.alert("Number of topics: " + turn_topic_distributions.get(0).length);
		}
	}	
	
	/**Load collocation*/
	public void createCollocations(JSONArray jsonColloc){
		argCollocTables = new HashMap<Integer, HashMap<String,ArgumentCollocation>>();
		argCollocTables.put(1, new HashMap<String, ArgumentCollocation>());
		argCollocTables.put(2, new HashMap<String, ArgumentCollocation>());
		
		for(int i=0; i<jsonColloc.size(); i++){
			JSONObject colloc = jsonColloc.get(i).isObject();
			int side = Integer.parseInt(colloc.get("side").toString());
			
			HashMap<String, ArgumentCollocation> sidedArgCollocTable = argCollocTables.get(side);
			String collocText = colloc.get("colloc").toString().replace("\"", "");
			ArgumentCollocation argColloc = sidedArgCollocTable.get(collocText);
			if(argColloc == null)
				argColloc = new ArgumentCollocation(collocText, side);

			// add ArgumentWord to the ArgumentCollocation
			String[] tokens = collocText.split(" ");			
			for(int x=0; x<tokens.length; x++){
				ArgumentWord argWord = wordTable.get(tokens[x]);
				if(argWord == null)
					continue;
				argColloc.addArgumentWord(argWord);
			}
			
			int frequency = Integer.parseInt(colloc.get("frequency").toString());
			argColloc.addFeature("frequency", (double)frequency);
			sidedArgCollocTable.put(collocText, argColloc);			
		}
		
		if(verbose){
			Window.alert("#collocs side 1: " + argCollocTables.get(1).size());
			Window.alert("#collocs side 2: " + argCollocTables.get(2).size());
			//ArrayList<ArgumentCollocation> rankCollocs = getTopN(argCollocTables.get(1), 5, "frequency");
			//for(int i=0; i<5; i++)
			//	Window.alert("Colloc " + rankCollocs.get(i).getCollocText() + "\t" + rankCollocs.get(i).getFeature("frequency"));
		}		
	}
	
	/**Load the framing scores of each speaker*/
	public void createSpeakerFramingScore(JSONObject jsonSpeakerColloc){
		for(String speaker : jsonSpeakerColloc.keySet()){
			JSONArray speakerColloc = jsonSpeakerColloc.get(speaker).isArray();
			
			for(int i=0; i<speakerColloc.size(); i++){
				JSONObject colloc = speakerColloc.get(i).isObject();
				String collocText = colloc.get("colloc").toString().replace("\"", "").replace("(", "").replace(")", "");
				int side = Integer.parseInt(colloc.get("side").toString());
				double score = Double.parseDouble(colloc.get("score").toString());
				
				HashMap<String, ArgumentCollocation> sidedArgCollocTable = argCollocTables.get(side);
				ArgumentCollocation argColloc = sidedArgCollocTable.get(collocText);
				if(argColloc == null)
					argColloc = new ArgumentCollocation(collocText, side);
				
				argColloc.addFeature(speaker, score);
				sidedArgCollocTable.put(collocText, argColloc);
			}
			
			if(verbose)
				Window.alert(speaker + " - # collocs: " + speakerColloc.size());
		}
	}
	
	/**Get top-N collocations sorted by a given criterion
	 * @param sideCollocTable The table containing collocation of 1 side
	 * @param numSideCollocs The number of collocations to be returned
	 * @param feat The feature to be used for sorting*/
	private ArrayList<ArgumentCollocation> getTopN(
			HashMap<String, ArgumentCollocation> sideCollocTable,
			int numSideCollocs,
			String feat){
		
		ArrayList<RankingItem<ArgumentCollocation>> fullRankList = new ArrayList<RankingItem<ArgumentCollocation>>();
		
		for(ArgumentCollocation colloc : sideCollocTable.values()){
			Double value = colloc.getFeature(feat);
			if(value == null)
				continue;
			RankingItem<ArgumentCollocation> item = new RankingItem<ArgumentCollocation>(colloc, value);
			fullRankList.add(item);
		}
		Collections.sort(fullRankList);
		ArrayList<ArgumentCollocation> rankCollocs = new ArrayList<ArgumentCollocation>();
		for(int i=0; i<Math.min(numSideCollocs, fullRankList.size()); i++)
			rankCollocs.add(fullRankList.get(i).getObject());
		return rankCollocs;
	}

	private int getSpeakerIndex(String speaker){
		for(int i=0; i<speakerVocab.size(); i++){
			if(speaker.equals(speakerVocab.get(i)))
				return i;
		}
		return -1;
	}
	
	private int getWordIndex(String word){
		for(int i=0; i<vocab.size(); i++){
			if(vocab.get(i).equals(word))
				return i;
		}
		return -1;
	}
	
	/**Map color for heatmap*/
	private int map(float max, float min, float value) {
       float x = (value - min) * 7 /( max- min);
       // avoid value == max leading to off-by-one values
       x = Math.min(x, 7 - 1);
       return Math.round(x);
    }
	
	/**Get the font size of each word in the word cloud according to the probabilities*/
	private HashMap<Integer, Integer> getTopWordLabelSize(double[] probabilities, int numWordPerTopic){		
		ArrayList<RankingItem<Integer>> rankingItems = new ArrayList<RankingItem<Integer>>();
		for(int i=0; i<probabilities.length; i++)
			rankingItems.add(new RankingItem<Integer>(i, probabilities[i]));
		Collections.sort(rankingItems);
		
		HashMap<Integer, Integer> topWordLabelSize = new HashMap<Integer, Integer>();
		double maxProb = rankingItems.get(0).getRankValue();
		double minProb = rankingItems.get(numWordPerTopic-1).getRankValue();
		for(int i=0; i<numWordPerTopic; i++){
			double weight = (Math.log(rankingItems.get(i).getRankValue()) - Math.log(minProb)) / (Math.log(maxProb) - Math.log(minProb));
			int fontSize = MIN_FONT_SIZE + (int)Math.round((MAX_FONT_SIZE - MIN_FONT_SIZE) * weight);  
			topWordLabelSize.put(rankingItems.get(i).getObject(), fontSize);
		}
		return topWordLabelSize;
	}
}

class HeatmapRecord extends ListGridRecord {
	
	private float[] dist;

	public HeatmapRecord(float[] dist) {
		this.dist = dist;
	}
	
	public float[] getValues( ){
		return dist;		
	}
}

class HeatmapData {

	private static HeatmapRecord record;
	
	public static HeatmapRecord getRecord(float[] turnTopicDistr){
		record = new HeatmapRecord(turnTopicDistr);
		return record;
	}
}
