<?php
	function buildErrorJson($error, $errorMsg, $cause) {
		$errorArray = array('error'=>$error,'errorMessage'=>$errorMsg,'cause'=>$cause);
		return json_encode($errorArray);
	}
	
	function dieOnError() {
		die(buildErrorJson("MysqlErrorException", "Error with mysql backend!", ""));
	}
	
	function initMySql() {
		include('config.php');
		$conn = mysql_connect($database_url, $database_user, $database_pass) or dieOnError();
		$db = mysql_select_db($database_name, $conn) or dieOnError();
	}
	
	function querySql($sql) {
		$query = mysql_query($sql) or dieOnError();
		return $query;
	}
	
	function numRows($query) {
		$numrows = mysql_num_rows($query) or dieOnError();
		return $numrows;
	}
	
	function fetchAssoc($query) {
		$assoc = mysql_fetch_assoc($query) or dieOnError();
		return $assoc;
	}
	
	function getTableName() {
		include('config.php');
		return $database_table_name;
	}
	
	function makeProfile($id, $name) {
		return array('id'=>$id, 'name'=>$name);
	}
	
	function getEncodedInput() {
		return file_get_contents("php://input");
	}
	
	function getDecodedInput() {
		return json_decode(file_get_contents("php://input"));
	}
	
	function makeUser($id) {
		return array('id' => $id);
	}
	
	function fetchAllProfiles($assoc) {
		$enc_allprofiles = $assoc['allProfiles'];
		$dec_allprofiles = base64_decode($enc_allprofiles);
		return json_decode($dec_allprofiles);
	}
	
	function generateUUID() {
		return sprintf( '%04x%04x-%04x-%04x-%04x-%04x%04x%04x',
			mt_rand( 0, 0xffff ), mt_rand( 0, 0xffff ),
			mt_rand( 0, 0xffff ),
			mt_rand( 0, 0x0fff ) | 0x4000,
			mt_rand( 0, 0x3fff ) | 0x8000,
			mt_rand( 0, 0xffff ), mt_rand( 0, 0xffff ), mt_rand( 0, 0xffff )
		);
	}
?>