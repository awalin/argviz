package edu.umd.cs.argviz.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import edu.umd.cs.argviz.shared.FieldVerifier;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Argviz implements EntryPoint {

	private RootPanel rootPanel;
	private HorizontalPanel topPanel;
	private VerticalPanel conversationPanel;
	private FlexTable turnTable;
	
	private String[][] rawData = {
			{"0" , "0" , "302" , "Good" , "evening" , "<eos>" , "" , "And" , "welcome" , "to" , "the" , "third" , "and" , "last" , "presidential" , "debate" , "of" , "2008" , "<sep>" , "sponsored" , "by" , "the" , "Commission" , "on" , "Presidential" , "Debates" , "<eos>" , "" , "I" , "<quote>" , "m" , "Bob" , "Schieffer" , "of" , "CBS" , "News" , "<eos>" , "" , "" , "The" , "rules" , "tonight" , "are" , "simple" , "<eos>" , "" , "The" , "subject" , "is" , "domestic" , "policy" , "<eos>" , "" , "I" , "will" , "divide" , "the" , "next" , "hour-and-a-half" , "into" , "nine-minute" , "segments" , "<eos>" , "" , "" , "I" , "will" , "ask" , "a" , "question" , "at" , "the" , "beginning" , "of" , "each" , "segment" , "<eos>" , "" , "Each" , "candidate" , "will" , "then" , "have" , "two" , "minutes" , "to" , "respond" , "<sep>" , "and" , "then" , "we" , "<quote>" , "ll" , "have" , "a" , "discussion" , "<eos>" , "" , "" , "I" , "<quote>" , "ll" , "encourage" , "them" , "to" , "ask" , "follow-up" , "questions" , "of" , "each" , "other" , "<eos>" , "" , "If" , "they" , "do" , "not" , "<sep>" , "I" , "will" , "<eos>" , "" , "" , "The" , "audience" , "behind" , "me" , "has" , "promised" , "to" , "be" , "quiet" , "<sep>" , "except" , "at" , "this" , "moment" , "<sep>" , "when" , "we" , "welcome" , "Barack" , "Obama" , "and" , "John" , "McCain" , "<eos>" , "" , "" , "Gentlemen" , "<sep>" , "welcome" , "<eos>" , "" , "" , "By" , "now" , "<sep>" , "we" , "<quote>" , "ve" , "heard" , "all" , "the" , "talking" , "points" , "<sep>" , "so" , "let" , "<quote>" , "s" , "try" , "to" , "tell" , "the" , "people" , "tonight" , "some" , "things" , "that" , "they" , "--" , "they" , "haven" , "<quote>" , "t" , "heard" , "<eos>" , "" , "Let" , "<quote>" , "s" , "get" , "to" , "it" , "<eos>" , "" , "" , "Another" , "very" , "bad" , "day" , "on" , "Wall" , "Street" , "<sep>" , "as" , "both" , "of" , "you" , "know" , "<eos>" , "" , "Both" , "of" , "you" , "proposed" , "new" , "plans" , "this" , "week" , "to" , "address" , "the" , "economic" , "crisis" , "<eos>" , "" , "" , "Senator" , "McCain" , "<sep>" , "you" , "proposed" , "a" , "$52" , "billion" , "plan" , "that" , "includes" , "new" , "tax" , "cuts" , "on" , "capital" , "gains" , "<sep>" , "tax" , "breaks" , "for" , "seniors" , "<sep>" , "write-offs" , "for" , "stock" , "losses" , "<sep>" , "among" , "other" , "things" , "<eos>" , "" , "" , "Senator" , "Obama" , "<sep>" , "you" , "proposed" , "$60" , "billion" , "in" , "tax" , "cuts" , "for" , "middle-" , "income" , "and" , "lower-income" , "people" , "<sep>" , "more" , "tax" , "breaks" , "to" , "create" , "jobs" , "<sep>" , "new" , "spending" , "for" , "public" , "works" , "projects" , "to" , "create" , "jobs" , "<eos>" , "" , "" , "I" , "will" , "ask" , "both" , "of" , "you"}
			,{"1" , "1" , "436" , "Well" , "<sep>" , "let" , "--" , "let" , "me" , "say" , "<sep>" , "Bob" , "<sep>" , "thank" , "you" , "<eos>" , "" , "And" , "thanks" , "to" , "Hofstra" , "<eos>" , "" , "And" , "<sep>" , "by" , "the" , "way" , "<sep>" , "our" , "beloved" , "Nancy" , "Reagan" , "is" , "in" , "the" , "hospital" , "tonight" , "<sep>" , "so" , "our" , "thoughts" , "and" , "prayers" , "are" , "going" , "with" , "you" , "<eos>" , "" , "It" , "<quote>" , "s" , "good" , "to" , "see" , "you" , "again" , "<sep>" , "Senator" , "Obama" , "<eos>" , "" , "" , "Americans" , "are" , "hurting" , "right" , "now" , "<sep>" , "and" , "they" , "<quote>" , "re" , "angry" , "<eos>" , "" , "They" , "<quote>" , "re" , "hurting" , "<sep>" , "and" , "they" , "<quote>" , "re" , "angry" , "<eos>" , "" , "They" , "<quote>" , "re" , "innocent" , "victims" , "of" , "greed" , "and" , "excess" , "on" , "Wall" , "Street" , "and" , "as" , "well" , "as" , "Washington" , "<sep>" , "D" , "<eos>" , "C" , "<eos>" , "" , "And" , "they" , "<quote>" , "re" , "angry" , "<sep>" , "and" , "they" , "have" , "every" , "reason" , "to" , "be" , "angry" , "<eos>" , "" , "" , "And" , "they" , "want" , "this" , "country" , "to" , "go" , "in" , "a" , "new" , "direction" , "<eos>" , "" , "And" , "there" , "are" , "elements" , "of" , "my" , "proposal" , "that" , "you" , "just" , "outlined" , "which" , "I" , "won" , "<quote>" , "t" , "repeat" , "<eos>" , "" , "" , "But" , "we" , "also" , "have" , "to" , "have" , "a" , "short-term" , "fix" , "<sep>" , "in" , "my" , "view" , "<sep>" , "and" , "long-" , "term" , "fixes" , "<eos>" , "" , "Let" , "me" , "just" , "talk" , "to" , "you" , "about" , "one" , "of" , "the" , "short-term" , "fixes" , "<eos>" , "" , "" , "The" , "catalyst" , "for" , "this" , "housing" , "crisis" , "was" , "the" , "Fannie" , "and" , "Freddie" , "Mae" , "that" , "caused" , "subprime" , "lending" , "situation" , "that" , "now" , "caused" , "the" , "housing" , "market" , "in" , "America" , "to" , "collapse" , "<eos>" , "" , "" , "I" , "am" , "convinced" , "that" , "<sep>" , "until" , "we" , "reverse" , "this" , "continued" , "decline" , "in" , "home" , "ownership" , "and" , "put" , "<sep>" , "<eos>" , "" , "" , "" , "<sep>a" , "floor" , "under" , "it" , "<sep>" , "and" , "so" , "that" , "people" , "have" , "not" , "only" , "the" , "hope" , "and" , "belief" , "they" , "can" , "stay" , "in" , "their" , "homes" , "and" , "realize" , "the" , "American" , "dream" , "<sep>" , "but" , "that" , "value" , "will" , "come" , "up" , "<eos>" , "" , "" , "Now" , "<sep>" , "we" , "have" , "allocated" , "$750" , "billion" , "<eos>" , "" , "Let" , "<quote>" , "s" , "take" , "$300" , "billion" , "of" , "that" , "$750" , "billion" , "and" , "go" , "in" , "and" , "buy" , "those" , "home" , "loan" , "mortgages" , "and" , "negotiate" , "with" , "those" , "people" , "in" , "their" , "homes" , "<sep>" , "11" , "million" , "homes" , "or" , "more" , "<sep>" , "so" , "that" , "they" , "can" , "afford" , "to" , "pay" , "the" , "mortgage" , "<sep>" , "stay" , "in" , "their" , "home" , "<eos>" , "" , "" , "Now" , "<sep>" , "I" , "know" , "the" , "criticism" , "of" , "this" , "<eos>" , "" , "" , "" , "<quote>" , "Well" , "<sep>" , "what" , "about" , "the" , "citizen" , "that" , "stayed" , "in" , "their" , "homes" , "<eos>" , "" , "That" , "paid" , "their" , "mortgage" , "payments" , "<eos>" , "" , "<quote>" , "" , "It" , "doesn" , "<quote>" , "t" , "help" , "that" , "person" , "in" , "their" , "home" , "if" , "the" , "next" , "door" , "neighbor" , "<quote>" , "s" , "house" , "is" , "abandoned" , "<eos>" , "" , "And" , "so" , "we" , "<quote>" , "ve" , "got" , "to" , "reverse" , "this" , "<eos>" , "" , "We" , "ought" , "to" , "put" , "the" , "homeowners" , "first" , "<eos>" , "" , "And" , "I" , "am" , "disappointed" , "that" , "Secretary" , "Paulson" , "and" , "others" , "have" , "not" , "made" , "that" , "their" , "first" , "priority" , "<eos>"}
			,{"2" , "2" , "87" , "Well" , "<sep>" , "first" , "of" , "all" , "<sep>" , "I" , "want" , "to" , "thank" , "Hofstra" , "University" , "and" , "the" , "people" , "of" , "New" , "York" , "for" , "hosting" , "us" , "tonight" , "and" , "it" , "<quote>" , "s" , "wonderful" , "to" , "join" , "Senator" , "McCain" , "again" , "<sep>" , "and" , "thank" , "you" , "<sep>" , "Bob" , "<eos>" , "" , "" , "I" , "think" , "everybody" , "understands" , "at" , "this" , "point" , "that" , "we" , "are" , "experiencing" , "the" , "worst" , "financial" , "crisis" , "since" , "the" , "Great" , "Depression" , "<eos>" , "" , "And" , "the" , "financial" , "rescue" , "plan" , "that" , "Senator" , "McCain" , "and" , "I" , "supported" , "is" , "an" , "important" , "first" , "step" , "<eos>" , "" , "And" , "I" , "pushed" , "for" , "some" , "core" , "principles"}
			,{"3" , "0" , "13" , "All" , "right" , "<eos>" , "" , "Would" , "you" , "like" , "to" , "ask" , "him" , "a" , "question" , "<eos>"}
			,{"4" , "1" , "287" , "No" , "<eos>" , "" , "I" , "would" , "like" , "to" , "mention" , "that" , "a" , "couple" , "days" , "ago" , "Senator" , "Obama" , "was" , "out" , "in" , "Ohio" , "and" , "he" , "had" , "an" , "encounter" , "with" , "a" , "guy" , "who" , "<quote>" , "s" , "a" , "plumber" , "<sep>" , "his" , "name" , "is" , "Joe" , "Wurzelbacher" , "<eos>" , "" , "" , "Joe" , "wants" , "to" , "buy" , "the" , "business" , "that" , "he" , "has" , "been" , "in" , "for" , "all" , "of" , "these" , "years" , "<sep>" , "worked" , "10" , "<sep>" , "12" , "hours" , "a" , "day" , "<eos>" , "" , "And" , "he" , "wanted" , "to" , "buy" , "the" , "business" , "but" , "he" , "looked" , "at" , "your" , "tax" , "plan" , "and" , "he" , "saw" , "that" , "he" , "was" , "going" , "to" , "pay" , "much" , "higher" , "taxes" , "<eos>" , "" , "" , "You" , "were" , "going" , "to" , "put" , "him" , "in" , "a" , "higher" , "tax" , "bracket" , "which" , "was" , "going" , "to" , "increase" , "his" , "taxes" , "<sep>" , "which" , "was" , "going" , "to" , "cause" , "him" , "not" , "to" , "be" , "able" , "to" , "employ" , "people" , "<sep>" , "which" , "Joe" , "was" , "trying" , "to" , "realize" , "the" , "American" , "dream" , "<eos>" , "" , "" , "Now" , "Senator" , "Obama" , "talks" , "about" , "the" , "very" , "<sep>" , "very" , "rich" , "<eos>" , "" , "Joe" , "<sep>" , "I" , "want" , "to" , "tell" , "you" , "<sep>" , "I" , "<quote>" , "ll" , "not" , "only" , "help" , "you" , "buy" , "that" , "business" , "that" , "you" , "worked" , "your" , "whole" , "life" , "for" , "and" , "be" , "able" , "--" , "and" , "I" , "<quote>" , "ll" , "keep" , "your" , "taxes" , "low" , "and" , "I" , "<quote>" , "ll" , "provide" , "available" , "and" , "affordable" , "health" , "care" , "for" , "you" , "and" , "your" , "employees" , "<eos>" , "" , "" , "And" , "I" , "will" , "not" , "have" , "--" , "I" , "will" , "not" , "stand" , "for" , "a" , "tax" , "increase" , "on" , "small" , "business" , "income" , "<eos>" , "" , "Fifty" , "percent" , "of" , "small" , "business" , "income" , "taxes" , "are" , "paid" , "by" , "small" , "businesses" , "<eos>" , "" , "That" , "<quote>" , "s" , "16" , "million" , "jobs" , "in" , "America" , "<eos>" , "" , "And" , "what" , "you" , "want" , "to" , "do" , "to" , "Joe" , "the" , "plumber" , "and" , "millions" , "more" , "like" , "him" , "is" , "have" , "their" , "taxes" , "increased" , "and" , "not" , "be" , "able" , "to" , "realize" , "the" , "American" , "dream" , "of" , "owning" , "their" , "own" , "business" , "<eos>"}
			,{"5" , "2" , "237" , "He" , "has" , "been" , "watching" , "ads" , "of" , "Senator" , "McCain" , "<quote>" , "s" , "<eos>" , "" , "Let" , "me" , "tell" , "you" , "what" , "I" , "<quote>" , "m" , "actually" , "going" , "to" , "do" , "<eos>" , "" , "I" , "think" , "tax" , "policy" , "is" , "a" , "major" , "difference" , "between" , "Senator" , "McCain" , "and" , "myself" , "<eos>" , "" , "And" , "we" , "both" , "want" , "to" , "cut" , "taxes" , "<sep>" , "the" , "difference" , "is" , "who" , "we" , "want" , "to" , "cut" , "taxes" , "for" , "<eos>" , "" , "" , "Now" , "<sep>" , "Senator" , "McCain" , "<sep>" , "the" , "centerpiece" , "of" , "his" , "economic" , "proposal" , "is" , "to" , "provide" , "$200" , "billion" , "in" , "additional" , "tax" , "breaks" , "to" , "some" , "of" , "the" , "wealthiest" , "corporations" , "in" , "America" , "<eos>" , "" , "Exxon" , "Mobil" , "<sep>" , "and" , "other" , "oil" , "companies" , "<sep>" , "for" , "example" , "<sep>" , "would" , "get" , "an" , "additional" , "$4" , "billion" , "in" , "tax" , "breaks" , "<eos>" , "" , "" , "What" , "I" , "<quote>" , "ve" , "said" , "is" , "I" , "want" , "to" , "provide" , "a" , "tax" , "cut" , "for" , "95" , "percent" , "of" , "working" , "Americans" , "<sep>" , "95" , "percent" , "<eos>" , "" , "If" , "you" , "make" , "more" , "--" , "if" , "you" , "make" , "less" , "than" , "a" , "quarter" , "million" , "dollars" , "a" , "year" , "<sep>" , "then" , "you" , "will" , "not" , "see" , "your" , "income" , "tax" , "go" , "up" , "<sep>" , "your" , "capital" , "gains" , "tax" , "go" , "up" , "<sep>" , "your" , "payroll" , "tax" , "<eos>" , "" , "Not" , "one" , "dime" , "<eos>" , "" , "And" , "95" , "percent" , "of" , "working" , "families" , "<sep>" , "95" , "percent" , "of" , "you" , "out" , "there" , "<sep>" , "will" , "get" , "a" , "tax" , "cut" , "<eos>" , "" , "In" , "fact" , "<sep>" , "independent" , "studies" , "have" , "looked" , "at" , "our" , "respective" , "plans" , "and" , "have" , "concluded" , "that" , "I" , "provide" , "three" , "times" , "the" , "amount" , "of" , "tax" , "relief" , "to" , "middle-class" , "families" , "than" , "Senator" , "McCain" , "does" , "<eos>"}
			,{"6" , "2" , "157" , "Now" , "<sep>" , "the" , "conversation" , "I" , "had" , "with" , "Joe" , "the" , "plumber" , "<sep>" , "what" , "I" , "essentially" , "said" , "to" , "him" , "was" , "<sep>" , "" , "<quote>" , "Five" , "years" , "ago" , "<sep>" , "when" , "you" , "were" , "in" , "a" , "position" , "to" , "buy" , "your" , "business" , "<sep>" , "you" , "needed" , "a" , "tax" , "cut" , "then" , "<eos>" , "" , "<quote>" , "" , "" , "And" , "what" , "I" , "want" , "to" , "do" , "is" , "to" , "make" , "sure" , "that" , "the" , "plumber" , "<sep>" , "the" , "nurse" , "<sep>" , "the" , "firefighter" , "<sep>" , "the" , "teacher" , "<sep>" , "the" , "young" , "entrepreneur" , "who" , "doesn" , "<quote>" , "t" , "yet" , "have" , "money" , "<sep>" , "I" , "want" , "to" , "give" , "them" , "a" , "tax" , "break" , "now" , "<eos>" , "" , "And" , "that" , "requires" , "us" , "to" , "make" , "some" , "important" , "choices" , "<eos>" , "" , "" , "The" , "last" , "point" , "I" , "<quote>" , "ll" , "make" , "about" , "small" , "businesses" , "<eos>" , "" , "Not" , "only" , "do" , "98" , "percent" , "of" , "small" , "businesses" , "make" , "less" , "than" , "$250" , "<sep>000" , "<sep>" , "but" , "I" , "also" , "want" , "to" , "give" , "them" , "additional" , "tax" , "breaks" , "<sep>" , "because" , "they" , "are" , "the" , "drivers" , "of" , "the" , "economy" , "<eos>" , "" , "They" , "produce" , "the" , "most" , "jobs" , "<eos>"}
			,{"7" , "1" , "210" , "You" , "know" , "<sep>" , "when" , "Senator" , "Obama" , "ended" , "up" , "his" , "conversation" , "with" , "Joe" , "the" , "plumber" , "--" , "we" , "need" , "to" , "spread" , "the" , "wealth" , "around" , "<eos>" , "" , "In" , "other" , "words" , "<sep>" , "we" , "<quote>" , "re" , "going" , "to" , "take" , "Joe" , "<quote>" , "s" , "money" , "<sep>" , "give" , "it" , "to" , "Senator" , "Obama" , "<sep>" , "and" , "let" , "him" , "spread" , "the" , "wealth" , "around" , "<eos>" , "" , "" , "I" , "want" , "Joe" , "the" , "plumber" , "to" , "spread" , "that" , "wealth" , "around" , "<eos>" , "" , "You" , "told" , "him" , "you" , "wanted" , "to" , "spread" , "the" , "wealth" , "around" , "<eos>" , "" , "" , "The" , "whole" , "premise" , "behind" , "Senator" , "Obama" , "<quote>" , "s" , "plans" , "are" , "class" , "warfare" , "<sep>" , "let" , "<quote>" , "s" , "spread" , "the" , "wealth" , "around" , "<eos>" , "" , "I" , "want" , "small" , "businesses" , "--" , "and" , "by" , "the" , "way" , "<sep>" , "the" , "small" , "businesses" , "that" , "we" , "<quote>" , "re" , "talking" , "about" , "would" , "receive" , "an" , "increase" , "in" , "their" , "taxes" , "right" , "now" , "<eos>" , "" , "" , "Who" , "--" , "why" , "would" , "you" , "want" , "to" , "increase" , "anybody" , "<quote>" , "s" , "taxes" , "right" , "now" , "<eos>" , "" , "Why" , "would" , "you" , "want" , "to" , "do" , "that" , "<sep>" , "anyone" , "<sep>" , "anyone" , "in" , "America" , "<sep>" , "when" , "we" , "have" , "such" , "a" , "tough" , "time" , "<sep>" , "when" , "these" , "small" , "business" , "people" , "<sep>" , "like" , "Joe" , "the" , "plumber" , "<sep>" , "are" , "going" , "to" , "create" , "jobs" , "<sep>" , "unless" , "you" , "take" , "that" , "money" , "from" , "him" , "and" , "spread" , "the" , "wealth" , "around" , "<eos>" , "" , "" , "I" , "<quote>" , "m" , "not" , "going" , "to" , "<sep>"}
			,{"8" , "1" , "12" , "We" , "<quote>" , "re" , "not" , "going" , "to" , "do" , "that" , "in" , "my" , "administration" , "<eos>"}
			,{"9" , "2" , "52" , "If" , "I" , "can" , "answer" , "the" , "question" , "<eos>" , "" , "Number" , "one" , "<sep>" , "I" , "want" , "to" , "cut" , "taxes" , "for" , "95" , "percent" , "of" , "Americans" , "<eos>" , "" , "Now" , "<sep>" , "it" , "is" , "true" , "that" , "my" , "friend" , "and" , "supporter" , "<sep>" , "Warren" , "Buffett" , "<sep>" , "for" , "example" , "<sep>" , "could" , "afford" , "to" , "pay" , "a" , "little" , "more" , "in" , "taxes" , "in" , "order" , "<sep>"}
			};
	
	public static final int STATUS_CODE_OK = 200;
	
	public static final int MIN_FONT_SIZE = 8;
	public static final int MAX_FONT_SIZE = 20;
	
	public static int numWordPerTopicSmall = 10;
	public static int numWordPerTopicLarge = 20;
	public static int numRows = 5;
	public static int numCols = 5;
	
	private VerticalPanel topicPanel = new VerticalPanel();
	private Button startButton = new Button("Start");
	private Grid grid = new Grid(numRows, numCols);
	private FlowPanel[][] gridPanels = new FlowPanel[numRows][numCols];
	private FlowPanel zoomTopicPanel = new FlowPanel();
	
	public static String[] vocab;
	public static double[][] topic_word_distributions;
	
	/**
	 * This is the entry point method.
	 */
	
	public void onModuleLoad() {
		rootPanel = RootPanel.get();
		turnTable = new FlexTable();
		conversationPanel = new VerticalPanel();
		turnTable.setText(0, 0, "Topic Shift");
		turnTable.setText(0, 1, "Turn");
		turnTable.setText(0, 2, "Topic Distribution");

		conversationPanel.add(turnTable);

		/* Load data into the turnTable */
		ArrayList<ArgumentTurn> allTurns = this.getTurns();
		int i;
		for(i = 0; i < allTurns.size(); i++){
			ArgumentTurn turn = allTurns.get(i);
			Label graphCell = new Label("bar graph goes here");
			DisclosurePanel textCell = new DisclosurePanel(); 
			Label mapCell = new Label("heat map goes here");

			/* Add the bar graph */
			turnTable.setWidget(i+1, 0, graphCell);
			
			/* Add the primary text */
			textCell.setHeader(new Label(turn.getPreview()));
			textCell.setContent(new Label(turn.getFullText()));
			turnTable.setWidget(i+1, 1, textCell);
			
			/* Add the heat map */
			turnTable.setWidget(i+1, 2, mapCell);
		}

	    // Add styles to elements in the turn table
	    turnTable.getRowFormatter().addStyleName(0, "turnTableHeader");
	    turnTable.getCellFormatter().addStyleName(0, 0, "turnTableGraphColumn");
	    turnTable.getCellFormatter().addStyleName(0, 1, "turnTableMainColumn");
	    turnTable.getCellFormatter().addStyleName(0, 2, "turnTableGraphColumn");

	    /* Code for the topic panel */
		// button		
		startButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				getData();
			}			
		});
		
		topicPanel.add(startButton);
		
		// grid
		for (int row = 0; row < numRows; ++row) {
	    	for (int col = 0; col < numCols; ++col){
	    		gridPanels[row][col] = new FlowPanel();
	    		gridPanels[row][col].setStylePrimaryName("cloudWrap");
	    		grid.setWidget(row, col, gridPanels[row][col]);
	    	}
		}
		grid.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				HTMLTable.Cell cell = grid.getCellForEvent(event);
				int row = cell.getRowIndex();
				int col = cell.getCellIndex();
				//gridPanels[row][col].clear();
				showWordCloud(row, col, numWordPerTopicLarge, zoomTopicPanel);
			}
		});
		grid.getCellFormatter().setWidth(0, 2, "256px");		
	    topicPanel.add(grid);	    
	    	    
	    // zoom panel
	    zoomTopicPanel.setHeight("200px");
	    topicPanel.add(zoomTopicPanel);
    		
	    //mainPanel.setStylePrimaryName("topicPanel");
		topicPanel.setBorderWidth(1);
	    
		topPanel = new HorizontalPanel();
		topPanel.add(conversationPanel);
		topPanel.add(topicPanel);

		rootPanel.add(topPanel, 0, 0);
	}

	private ArrayList<ArgumentTurn> getTurns(){
	    ArrayList<ArgumentTurn> allTurns = new ArrayList<ArgumentTurn>();

		/*
	     * In String[i][]rawData:
	     * Entry 0 = turn ID of i
	     * Entry 1 = speaker ID of i
	     * Entry 2 = word count of i
	     * Entry 3-n = words of i, individually
	     */
				
		int numTurns = rawData.length;
		int i, j;
		int numWords = 0;
		
		for(i = 0; i < numTurns; i++){
			String[] turnString = rawData[i];

			numWords = Integer.parseInt(turnString[2]);
			String[] words = new String[numWords];

			ArgumentTurn turn = new ArgumentTurn(Integer.parseInt(turnString[0]),Integer.parseInt(turnString[1]),20,false);
			
			for(j = 0; j<numWords; j++){
				words[j] = turnString[j + 3];
			}
			turn.setWords(words);
			allTurns.add(turn);			
		}

		return allTurns;
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
					Hyperlink tagLink = new Hyperlink(vocab[wordIndex],vocab[wordIndex]);
					tagLink.setStylePrimaryName("cloudTags");
					
					Style linkStyle = tagLink.getElement().getStyle();
					String labelSize = Integer.toString(topWordLabelSize.get(wordIndex)) + "pt";
		            linkStyle.setProperty("fontSize", labelSize);
		            
		            gridPanels[row][col].add(tagLink);
				}
	    		topicCount ++;
	    	}	        
	    }
	}
	
	/**Display a single word cloud at the bottom of the Topic Panel once 
	 * a topic is selected
	 * @param row The row index of the selected word cloud
	 * @param col The column index of the selected word cloud
	 * @param numTopWord The number of words to be shown in the word cloud
	 * @param container The panel to contain the word cloud*/
	public void showWordCloud(int row, int col, int numTopWord, FlowPanel container){
		int topicIndex = row * numRows + col;
		HashMap<Integer, Integer> topWordLabelSize = 
			getTopWordLabelSize(topic_word_distributions[topicIndex], numTopWord);
		container.clear();
		for(int wordIndex : topWordLabelSize.keySet()){
			Hyperlink tagLink = new Hyperlink(vocab[wordIndex],vocab[wordIndex]);
			tagLink.setStylePrimaryName("cloudTags");
			
			Style linkStyle = tagLink.getElement().getStyle();
			String labelSize = Integer.toString(topWordLabelSize.get(wordIndex)) + "pt";
            linkStyle.setProperty("fontSize", labelSize);
            
            container.add(tagLink);
		}
	}
	
	/**Call the php file on the server*/
	public void getData(){
	    String url = "http://bryan.btr3.com/test/debateio.php";
	    
	    RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST,url);
	    requestBuilder.setHeader("Content-Type", "application/x-www-form-urlencoded");
	    try{
	    	StringBuffer postData = new StringBuffer();
	    	postData.append(URL.encode("conversation")).append("=").append(URL.encode("biden_palin.v2.txt"));
	    		    	
	        requestBuilder.sendRequest(postData.toString(), new RequestCallback() {
	            public void onResponseReceived(Request request, Response response){
	                if (response.getStatusCode() == STATUS_CODE_OK){ 
	                	handleResponse(response.getText());
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
		
		// topic word distribution
		JSONArray jsonTopic = jsonObj.get("topic_word_distribution").isArray();
		createTopicWordDistribution(jsonTopic);
		
		// collocation
		//JSONArray jsonColloc = jsonObj.get("collocation").isArray();
		//createCollocations(jsonColloc);
	}
	
	/**Create the vocabulary from JSON*/
	public void createVocabulary(JSONArray jsonVocabArray){
		vocab = new String[jsonVocabArray.size()];
		for(int i=0; i<vocab.length; i++){
			vocab[i] = jsonVocabArray.get(i).toString().replace("\"", "");
		}
		Window.alert("Vocabulary created: " + vocab.length);
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
		Window.alert("Topic-word distribution loaded: " + topic_word_distributions.length + "x" + topic_word_distributions[0].length);
	}
	/*
	public void createCollocations(JSONArray jsonColloc){
		// TODO: get collocations
		Window.alert("Colloc size: " + jsonColloc.size());
	}
	*/
	
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
