<?php
define ( 'LUCAHOMEPORT', 6677 );

$action = Get ( 'action' );
$user = Get ( 'user' );
$password = Get ( 'password' );
$authentification = "$user:$password";
$authentificationaction = "$authentification:$action";

switch ($action) {
	
	/* ------------------- Birthday -------------------- */
	
	case 'getbirthdays' :
		echo Send ( "$authentificationaction:all" );
		break;
	case 'addbirthday' :
	case 'updatebirthday' :
		$name = Get ( 'name' );
		$day = Get ( 'day' );
		$month = Get ( 'month' );
		$year = Get ( 'year' );
		if ($name != '' && $day != '' && $month != '' && $year != '') {
			echo Send ( "$authentificationaction:$name:$day:$month:$year" );
		}
		break;
	case 'deletebirthday' :
		$name = Get ( 'name' );
		if ($name != '') {
			echo Send ( "$authentificationaction:$name" );
		}
		break;
	
	/* ---------------------- Gpio --------------------- */
	
	case 'getgpios' :
		echo Send ( "$authentificationaction:all" );
		break;
	case 'addgpio' :
		$name = Get ( 'name' );
		$gpio = Get ( 'gpio' );
		if ($name != '' && $gpio != '') {
			echo Send ( "$authentificationaction:$name:$gpio:0" );
		}
		break;
	case 'setgpio' :
		$gpio = Get ( 'gpio' );
		$state = Get ( 'state' );
		if ($gpio != '' && $state != '') {
			echo Send ( "$authentificationaction:$gpio:$state" );
		}
		break;
	case 'deletegpio' :
		$name = Get ( 'name' );
		if ($name != '') {
			echo Send ( "$authentificationaction:$name" );
		}
		break;
	
	/* --------------------- Movie --------------------- */
	
	case 'getmovies' :
		echo Send ( "$authentificationaction:all" );
		break;
	case 'addmovie' :
	case 'updatemovie' :
		$title = Get ( 'title' );
		$genre = Get ( 'genre' );
		$description = Get ( 'description' );
		$rating = Get ( 'rating' );
		$watched = Get ( 'watched' );
		if ($title != '') {
			echo Send ( "$authentificationaction:$title:$genre:$description:$rating:$watched" );
		}
		break;
	case 'deletemovie' :
		$title = Get ( 'title' );
		if ($title != '') {
			echo Send ( "$authentificationaction:$title" );
		}
		break;
	
	/* -------------------- Schedule ------------------- */
	
	case 'getschedules' :
		echo Send ( "$authentificationaction:all" );
		break;
	case 'addschedule' :
		$name = Get ( 'name' );
		$socket = Get ( 'socket' );
		$weekday = Get ( 'weekday' );
		$hour = Get ( 'hour' );
		$minute = Get ( 'minute' );
		$onoff = Get ( 'onoff' );
		$isTimer = Get ( 'isTimer' );
		if ($name != '' && $socket != '' && $weekday != '' && $hour != '' && $minute != '' && $onoff != '' && $isTimer != '') {
			echo Send ( "$authentificationaction:$name:$socket:$weekday:$hour:$minute:$onoff:$isTimer:1" );
		}
		break;
	case 'setschedule' :
	case 'deleteschedule' :
		$schedule = Get ( 'schedule' );
		if ($schedule != '') {
			echo Send ( "$authentificationaction:$schedule" );
		}
		break;
	
	/* ---------------- Wireless Socket ---------------- */
	
	case 'getsockets' :
		echo Send ( "$authentificationaction:all" );
		break;
	case 'addsocket' :
		$name = Get ( 'name' );
		$area = Get ( 'area' );
		$code = Get ( 'code' );
		if ($name != '' && $code != '') {
			echo Send ( "$authentificationaction:$name:$area:$code:0" );
		}
		break;
	case 'setsocket' :
		$socket = Get ( 'socket' );
		$state = Get ( 'state' );
		if ($socket != '' && $state != '') {
			echo Send ( "$authentificationaction:$socket:$state" );
		}
		break;
	case 'deletesocket' :
		$socket = Get ( 'socket' );
		if ($socket != '') {
			echo Send ( "$authentificationaction:$socket" );
		}
		break;
	
	/* --------------------- Sound --------------------- */
	
	case 'stopplaying' :
		echo Send ( "$authentificationaction:all" );
		break;
	
	/* -------------------- Different ------------------ */
	
	case 'activateAllGpios' :
	case 'deactivateAllGpios' :
	
	case 'activateAllSockets' :
	case 'deactivateAllSockets' :
	
	case 'activateAllSchedules' :
	case 'deactivateAllSchedules' :

	case 'getchanges' :
	case 'getchangesrest' :
	case 'getinformations' :
	case 'getinformationsrest' :
		
	case 'getraspberry' :
	case 'getarea' :

	case 'getcurrenttemperature' :
	case 'getcurrenttemperaturerest' :
	case 'gettemperaturegraphurl' :
		echo Send ( "$authentificationaction:data" );
		break;
	
	/* ---------------------- Pages -------------------- */
		
	case 'main' :
		var2console ( "Navigated to $action page!" );
		break;
	
	/* --------------------- Default ------------------- */
	
	default :
		var2console ( "ERROR: " );
		var2console ( $action );
		break;
}

/* ===================== Functions ===================== */
function Get($val) {
	if (isset ( $_GET [$val] ))
		return $_GET [$val];
}
function StartsWith($Haystack, $Needle) {
	return strpos ( $Haystack, $Needle ) === 0;
}
function GetValues($data, $type) {
	$lines = explode ( ';', $data );
	
	$values = array ();
	for($i = 0; $i < count ( $lines ); $i ++) {
		if (StartsWith ( $lines [$i], $type )) {
			$values [] = explode ( ':', $lines [$i] );
		}
	}
	return $values;
}
function Send($data) {
	$socket = fsockopen ( 'udp://127.0.0.1', LUCAHOMEPORT, $errno, $errstr, 10 );
	$out = "";
	if (! $socket) {
		var2console ( "SocketError" );
		echo "$errstr ($errno)";
		exit ();
	} else {
		fwrite ( $socket, "$data" );
		$out = fread ( $socket, 20000 );
		fclose ( $socket );
	}
	return $out;
}

/* ================== Get Informations ================= */
function GetInformations() {
	return Send ( "Website:0000:getinformations:all" );
}
function ParseInformations($data) {
	$values = GetValues ( $data, 'information:' );
	$informations = array ();
	for($i = 0; $i < count ( $values ); $i ++) {
		$informations [] = array (
				'key' => trim ( $values [$i] [1] ),
				'value' => trim ( $values [$i] [2] ) 
		);
	}
	return $informations;
}

/* ===================== Get Changes =================== */
function GetChanges() {
	return Send ( "Website:0000:getchanges:all" );
}
function ParseChanges($data) {
	$values = GetValues ( $data, 'change:' );
	$changes = array ();
	for($i = 0; $i < count ( $values ); $i ++) {
		$changes [] = array (
				'type' => trim ( $values [$i] [1] ),
				'hour' => trim ( $values [$i] [2] ),
				'minute' => trim ( $values [$i] [3] ),
				'day' => trim ( $values [$i] [4] ),
				'month' => trim ( $values [$i] [5] ),
				'year' => trim ( $values [$i] [6] ),
				'user' => trim ( $values [$i] [7] ) 
		);
	}
	return $changes;
}

/* ======================= Get Area ==================== */
function GetArea() {
	return Send ( "Website:0000:getarea:region" );
}

/* =================== Get Temperature ================= */
function GetTemperature() {
	return Send ( "Website:0000:getcurrenttemperature:value" );
}

/* ============== Get Temperature Graph URL ============ */
function GetTemperatureGraphUrl() {
	return Send ( "Website:0000:gettemperaturegraphurl:url" );
}

/* ================== Logger Functions ================= */
function var2console($var, $name = '', $now = false) {
	if ($var === null)
		$type = 'NULL';
	else if (is_bool ( $var ))
		$type = 'BOOL';
	else if (is_string ( $var ))
		$type = 'STRING[' . strlen ( $var ) . ']';
	else if (is_int ( $var ))
		$type = 'INT';
	else if (is_float ( $var ))
		$type = 'FLOAT';
	else if (is_array ( $var ))
		$type = 'ARRAY[' . count ( $var ) . ']';
	else if (is_object ( $var ))
		$type = 'OBJECT';
	else if (is_resource ( $var ))
		$type = 'RESOURCE';
	else
		$type = '???';
	if (strlen ( $name )) {
		str2console ( "$type $name = " . var_export ( $var, true ) . ';', $now );
	} else {
		str2console ( "$type = " . var_export ( $var, true ) . ';', $now );
	}
}
function str2console($str, $now = false) {
	if ($now) {
		echo "<script type='text/javascript'>\n";
		echo "//<![CDATA[\n";
		echo "console.log(", json_encode ( $str ), ");\n";
		echo "//]]>\n";
		echo "</script>";
	} else {
		register_shutdown_function ( 'str2console', $str, true );
	}
}
?>
