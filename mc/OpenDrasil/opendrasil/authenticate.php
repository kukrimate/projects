<?php
	//include functions, decode input
	include("functions.php");
	$input = getDecodedInput();
	
	//get agent
	$agent = $input -> {'agent'};
	$agentName = $agent -> {'name'};
	$agentVersion = $agent -> {'version'};
	
	//get user data
	$username = $input -> {'username'};
	$password = $input -> {'password'};
	$clientToken = $input -> {'clientToken'};
	
	//check inputs
	if (!$username) {
		die(buildErrorJson("ForbiddenOperationException", "Invalid credentials. Invalid username or password.", ""));
	}
	
	if (!$password) {
		die(buildErrorJson("ForbiddenOperationException", "Invalid credentials. Invalid username or password.", ""));
	}
	
	if (!$clientToken) {
		die(buildErrorJson("ForbiddenOperationException", "Invalid token.", ""));
	}
	
	//init mysql
	initMySql();
	
	//make password md_5
	$md5Password = md5($password);
	
	//check the username and the password
	$query = querySql("SELECT * FROM ".getTableName()." WHERE username='$username' AND password='$md5Password'");
	$num_rows = numRows($query);
	
	//Die if they not match database
	if ($num_rows < 1) {
		die(buildErrorJson("ForbiddenOperationException", "Invalid credentials. Invalid username or password.", ""));
	}
	
	//Generate new accessToken
	$newAccessToken = generateUUID();
	
	//Write new accessToken to databases
	$uuidQuery = querySql("UPDATE ".getTableName()." SET accessToken='$newAccessToken' WHERE password='$md5Password'");
	
	//Query again and write clientToken if changed
	$tokenQueryGet = querySql("SELECT * FROM ".getTableName()." WHERE password='$md5Password'");
	$tokenAssoc = fetchAssoc($tokenQueryGet);
	if ($tokenAssoc['clientToken'] != $clientToken) {
		//Not indentical update clientToken
		$tokenQuerySet = querySql("UPDATE ".getTableName()." SET clientToken='$clientToken' WHERE password='$md5Password'");
	}
	
	//Get final data from mysql and echo result
	$finalQuery = querySql("SELECT * FROM ".getTableName()." WHERE password='$md5Password'");
	$finalAssoc = fetchAssoc($finalQuery);
	
	$finalAccessToken = $finalAssoc['accessToken'];
	$finalClientToken = $finalAssoc['clientToken'];
	
	$finalSelectedProfile = makeProfile($finalAssoc['selectedProfile'], $username);
	$allProfiles = fetchAllProfiles($finalAssoc);
	
	$userid = $finalAssoc['id'];
	$finalUser = makeUser($userid);
	
	$result = array('accessToken' => $finalAccessToken, 'clientToken' => $finalClientToken, 'selectedProfile' => $finalSelectedProfile, 'availableProfiles' => $allProfiles, 'user' => $finalUser);
	
	$jsonResult = json_encode($result);
	
	echo($jsonResult);
?>