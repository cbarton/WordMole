<?
	define("TOTAL_QUESTIONS", 10.0);

	$conn = mysql_connect('localhost', 'lab3', 'lab3password');
	if ( !$conn ) {
		die('Could not connect to the database: ' . mysql_error());
	}
	mysql_select_db('quiz', $conn);

	function getQuestion($question_number){
		$query = sprintf("SELECT question, question_number, choice, choice_num FROM questions INNER JOIN choices ON questions.id=choices.question_id WHERE questions.id=%d",
				(int)mysql_real_escape_string($question_number));
		
		$result = mysql_query($query);
		if ( !$result )
			die(mysql_error());

		$row = mysql_fetch_array($result);

		$choices = array();
		
		$choices[$row['choice_num']] = $row['choice'];
		for ( $i = 0; $choice = mysql_fetch_array($result); $i++ ){
			$choices[$choice['choice_num']] = $choice['choice'];
		}

		$question = array(
				'question' => $row['question'],
				'choices' => $choices
		);
		return $question;
	}

	function checkAnswer($question_number, $answer){
		$answers = sprintf("SELECT answers.id, answer, sources, choice_num, choice, question FROM answers INNER JOIN choices ON answers.choice_id=choices.id INNER JOIN questions on answers.question_id=questions.id WHERE answers.question_id=%d", 
			(int)mysql_real_escape_string($question_number));

		$answer_result = mysql_query($answers);
		if ( !$answer_result )	
			die(mysql_error());
	
		$answer_row = mysql_fetch_array($answer_result);

		$pictures = sprintf("SELECT picture_path, width, height FROM pictures WHERE answer_id=%d",	
			(int)$answer_row['id']);

		$picture_result = mysql_query($pictures);
		if ( !$picture_result )
			die(mysql_error());
		
		$pictures = array();
	
		for ( $i = 0; $row = mysql_fetch_array($picture_result); $i++ ){
			$pictures[$i]['picture'] = $row['picture_path'];
			$pictures[$i]['height'] = $row['height'];
			$pictures[$i]['width'] = $row['width'];
		}			
		$correct = ( $answer == $answer_row['choice_num'] );
		$answer_ret = array(
			'answer' => $answer_row['answer'],
			'sources' => $answer_row['sources'],
			'choice_num' => $answer_row['choice_num'],
			'choice' => $answer_row['choice'],
			'pictures' => $pictures,
			'correct' => $correct,
			'question' => $answer_row['question']
		);
		return $answer_ret;	
	}
	
	function boolToString($bool){
		return $bool ? 'true' : 'false';
	}

	function getHighScores(){
		$query = "SELECT player, score FROM scores LIMIT 0, 10";
		
		$result = mysql_query($query);
		if ( !$result )
			die(mysql_error());

		$high_scores = array();

		for ( $i = 0; $row = mysql_fetch_array($result); $i++ ){
			$high_scores[$i]['player'] = $row['player'];
			$high_scores[$i]['score'] = $row['score'];
		}
		return $high_scores;
	}
	
	function addPlayer($player, $score){
		$query = "INSERT INTO scores (player, score) VALUES ('$player', '$score')";
		mysql_query($query);
	}
?>
