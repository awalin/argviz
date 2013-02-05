<?php
	// Inputs
	$conversation = $_POST["conversation"];	
	//$conversation = "biden_palin.v2.txt";
	
	// Filenames in the folder /data/
	$topic_word_dist_file = "topic_word_distr.txt";
	$doc_topic_dist_file = "topic_distributions.txt";
	$topic_shift_score_file = "topic_shift_scores.txt";
	$doc_text_file = "show_text.txt";
	$influence_ranks_file = "influence_ranks.txt";
	$collocation_file = "collocations.txt";
	
	// object to store all the JSON objects
	$objs = array();
	getTopicWordDistribution($conversation);
	getCollocations();
	getTurnTopicDistribution();
	getTurnTexts();
	getTopicShiftScores();
	getSpeakerCollocationScores();
	
	// output JSON object
	$json = 
	"{" .
		"collocation:" . $objs["collocation"] . "," .
		"vocabulary:" . $objs["vocabulary"] . "," .
		"topic_word_distribution:" . $objs["topic_word_distribution"] . "," .
		"turn_index:" . $objs["turn_index"] . "," .
		"turn_topic_distribution:" . $objs["turn_topic_distribution"] . "," .
		"speaker:" . $objs["speaker"] . "," .
		"text:" . $objs["text"] . "," .
		"topic_shift_score:" . $objs["topic_shift_score"] . "," .
		"framing_score:" . $objs["framing_score"] .
	"}";
	
	//echo $objs["collocation"];
	//echo $objs["vocabulary"];
	//echo $objs["topic_word_distribution"];
	//echo $objs["turn_index"];
	//echo $objs["turn_topic_distribution"];
	//echo $objs["speaker"];
	//echo $objs["text"];
	//echo $objs["topic_shift_score"];
	//echo $objs["framing_score"];
	echo $json;
	
	// read framing scores
	function getSpeakerCollocationScores()
	{
		global $objs;
		global $conversation;
		global $influence_ranks_file;
		
		$framing_score = array();
		
		$filepath = "data/" . $conversation . "/" . $influence_ranks_file;
		$lines = file($filepath);
		foreach($lines as $line_num => $line)
		{
			$split_cells = explode("\t", trim($line));
			if(count($split_cells) == 1)
				continue;			
			$speaker = $split_cells[0];
			
			$collocs = array();			
			$split_cells = explode(") ", trim($split_cells[2]));
			for($x=0; $x<count($split_cells); $x++){
				$split = explode(":(", $split_cells[$x]);
				$score = (double)$split[0];
				$split = explode(" ", $split[1]);
				if(strcmp($split[0], "COLLOC1") == 0)
					$side = 1;
				else 
					$side = 2;
				$colloc = "";
				for($y=1; $y<count($split); $y++)
					$colloc .= $split[$y] . " ";
					
				$collocs[$x] = array("colloc"=>trim($colloc), "side"=>$side, "score"=>$score);
			}
			$framing_score[$speaker] = $collocs;
		}
		$objs["framing_score"] = json_encode($framing_score);
	}
	
	// read topic shift scores
	function getTopicShiftScores()
	{
		global $objs;
		global $conversation;
		global $topic_shift_score_file;
		
		$topic_shift_scores = array();
		
		$filepath = "data/" . $conversation . "/" . $topic_shift_score_file;
		$lines = file($filepath);
		foreach($lines as $line_num => $line)
		{
			$split_cells = explode("\t", trim($line));
			$topic_shift_scores[$line_num] = (double)$split_cells[1];
		}
		$objs["topic_shift_score"] = json_encode($topic_shift_scores);
	}
	
	// read texts
	function getTurnTexts()
	{
		global $objs;
		global $conversation;
		global $doc_text_file;
		
		$speakers = array();
		$turn_texts = array();
		
		$filepath = "data/" . $conversation . "/" . $doc_text_file;
		$lines = file($filepath);
		foreach($lines as $line_num => $line)
		{
			$split_cells = explode("\t", trim($line));
			$speakers[$line_num] = $split_cells[1];
			$turn_texts[$line_num] = $split_cells[2];				
		}				
		$objs["speaker"] = json_encode($speakers);
		$objs["text"] = json_encode($turn_texts); 
	}
	
	// read collocations
	function getCollocations()
	{
		global $objs;
		global $conversation;
		global $collocation_file;
		$filepath = "data/" . $conversation . "/" . $collocation_file;
				
		$ignore = array("(", ")"); // remove "(" and ")"			
		$colloc_objs = array();		
		$lines = file($filepath);
		foreach($lines as $line_num => $line)
		{
			$split_cells = explode(" ", str_replace($ignore, "", trim($line)));
			$colloc = "";
			for($y=2; $y<count($split_cells); $y++)
			{
				$colloc .= $split_cells[$y] . " ";
			}
			$frequency = (int)$split_cells[0];
			if(strcmp($split_cells[1], "COLLOC1") == 0)
				$side = 1;
			else 
				$side = 2;
			$colloc_objs[$line_num] = array("colloc"=>trim($colloc), "side"=>$side, "frequency"=>$frequency);
		}
		$objs["collocation"] = json_encode($colloc_objs);
	}
	
	// read turn topic distribution
	function getTurnTopicDistribution()
	{
		global $objs;
		global $conversation;
		global $doc_topic_dist_file;
		
		$filepath = "data/" . $conversation . "/" . $doc_topic_dist_file;
		
		$turn_topic_dist = array();
		$turn_indices = array();
		
		$lines = file($filepath);
		foreach($lines as $x => $line)
		{
			$split_cells = explode("\t", $line);
			$topic_dist = array();
			for($y=1; $y<count($split_cells); $y++)
			{					 
				$topic_dist[$y-1] = (double)$split_cells[$y];
			}
			$turn_indices[$x] = $split_cells[0]; 
			$turn_topic_dist[$x] = $topic_dist;
		}		
		$objs["turn_index"] = json_encode($turn_indices);		
		$objs["turn_topic_distribution"] = json_encode($turn_topic_dist);
	}
		
	// read the topic-word distribution
	function getTopicWordDistribution()
	{
		global $objs;	
		global $topic_word_dist_file;
		global $conversation;
		$filepath = "data/" . $conversation . "/" . $topic_word_dist_file;		
							
		$vocab = array();
		$probabilities = array();
		
		$lines = file($filepath);
		foreach($lines as $x => $line)
		{
			$split_cells = explode("\t", $line);
			$vocab[$x] = $split_cells[0];	
			
			$topic_prob = array();
			for($y=1; $y<count($split_cells); $y++)
			{					 
				$topic_prob[$y-1] = (double)$split_cells[$y];
			}
			$probabilities[$x] = $topic_prob;
		}
		$objs["vocabulary"] = json_encode($vocab);
		$objs["topic_word_distribution"] = json_encode($probabilities);
	}
?>