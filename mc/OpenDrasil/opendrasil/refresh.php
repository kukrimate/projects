<?php
	//include functions, decode input
	include("functions.php");
	$input = getDecodedInput();
	
	//get tokens
	$clientToken = $input -> {'clientToken'};
	$accessToken = $input -> {'accessToken'};
	
	//get profile
	$selectedProfile = $input -> {'selectedProfile'};
	$profileId = $selectedProfile -> {'id'};
	$profileName = $selectedProfile -> {'name'};
	
	//check inputs
	if (!$clientToken) {
		die(buildErrorJson("ForbiddenOperationException", "Invalid token.", ""));
	}
	
	if (!$accessToken) {
		die(buildErrorJson("ForbiddenOperationException", "Invalid token.", ""));
	}
	
	//init mysql
	initMySql();
	
	//check the accessToken and the clientToken
	$query = querySql("SELECT * FROM ".getTableName()." WHERE accessToken='$accessToken' AND clientToken='$clientToken'");
	$num_rows = numRows($query);
	
	//Die if they not match database
	if ($num_rows < 1) {
		die(buildErrorJson("ForbiddenOperationException", "Invalid token.", ""));
	}
		
	//Generate new accessToken
	$newAccessToken = generateUUID();
	
	//Write new accessToken to databases
	$uuidQuery = querySql("UPDATE ".getTableName()." SET accessToken='$newAccessToken' WHERE clientToken='$clientToken'");
	
	//Query again and select profile if changed
	$profileQueryGet = querySql("SELECT * FROM ".getTableName()." WHERE clientToken='$clientToken'");
	$profileAssoc = fetchAssoc($profileQueryGet);
	if ($profileAssoc['selectedProfile'] != $profileId) {
		//Not indentical update selected profile
		$profileQuerySet = querySql("UPDATE ".getTableName()." SET selectedProfile='$profileId' WHERE clientToken='$clientToken'");
	}
	
	//Get final data from mysql and echo result
	$finalQuery = querySql("SELECT * FROM ".getTableName()." WHERE clientToken='$clientToken'");
	$finalAssoc = fetchAssoc($finalQuery);
	
	$finalAccessToken = $finalAssoc['accessToken'];
	$finalClientToken = $finalAssoc['clientToken'];
	
	$finalSelectedProfile = makeProfile($profileId, $profileName);
	$allProfiles = fetchAllProfiles($finalAssoc);
	
	$userid = $finalAssoc['id'];
	$finalUser = makeUser($userid);
	
	$result = array('accessToken' => $finalAccessToken, 'clientToken' => $finalClientToken, 'selectedProfile' => $finalSelectedProfile, 'availableProfiles' => $allProfiles, 'user' => $finalUser);
	
	$jsonResult = json_encode($result);
	
	echo($jsonResult);
?>