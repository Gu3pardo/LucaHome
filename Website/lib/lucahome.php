<?php
define ( 'LUCAHOMEPORT', 6677 );

$user = Get ( 'user' );
$password = Get ( 'password' );
$login = "$user:$password";

$action = Get ( 'action' );

switch ($action) {
	
	/* ------------------- Birthday -------------------- */
	case 'getbirthdays' :
		echo Send ( "$login:BIRTHDAY:GET:ALL" );
		break;
	case 'addbirthday' :
		$id = Get ( 'id' );
		$name = Get ( 'name' );
		$day = Get ( 'day' );
		$month = Get ( 'month' );
		$year = Get ( 'year' );
		if ($id != '' && $name != '' && $day != '' && $month != '' && $year != '') {
			echo Send ( "$login:BIRTHDAY:ADD:$id:$name:$day:$month:$year" );
		} else {
			echo "Error 35:Parameter not found for birthday";
		}
		break;
	case 'updatebirthday' :
		$id = Get ( 'id' );
		$name = Get ( 'name' );
		$day = Get ( 'day' );
		$month = Get ( 'month' );
		$year = Get ( 'year' );
		if ($id != '' && $name != '' && $day != '' && $month != '' && $year != '') {
			echo Send ( "$login:BIRTHDAY:UPDATE:$id:$name:$day:$month:$year" );
		} else {
			echo "Error 35:Parameter not found for birthday";
		}
		break;
	case 'deletebirthday' :
		$id = Get ( 'id' );
		if ($id != '') {
			echo Send ( "$login:BIRTHDAY:DELETE:$id" );
		} else {
			echo "Error 35:Parameter not found for birthday";
		}
		break;
	
	/* -------------------- Changes -------------------- */
	case 'getchanges' :
		echo Send ( "$login:CHANGE:GET:WEBSITE" );
		break;
	case 'getchangesrest' :
		echo Send ( "$login:CHANGE:GET:REST" );
		break;
	
	/* ------------------ Informations ------------------ */
	case 'getinformations' :
		echo Send ( "$login:INFORMATION:GET:WEBSITE" );
		break;
	case 'getinformationsrest' :
		echo Send ( "$login:INFORMATION:GET:REST" );
		break;
	
	/* ------------------- MapContent ------------------- */
	case 'getmapcontents' :
		echo Send ( "$login:MAPCONTENT:GET:ALL" );
		break;
	case 'addmapcontent' :
		$id = Get ( 'id' );
		$position = Get ( 'position' );
		$type = Get ( 'type' );
		$schedules= Get ( 'schedules' );
		$sockets = Get ( 'sockets' );
		$temperatureArea = Get ( 'temperatureArea' );
		if ($id != '' & $position != '' & $type != '' & $schedules != '' & $sockets != '' & $temperatureArea != '') {
			echo Send ( "$login:MAPCONTENT:ADD:$id:$position:$type:$schedules:$sockets:$temperatureArea" );
		} else {
			echo "Error 145:Parameter not found for mapcontent";
		}
		break;
	case 'updatemapcontent' :
		$id = Get ( 'id' );
		$position = Get ( 'position' );
		$type = Get ( 'type' );
		$schedules= Get ( 'schedules' );
		$sockets = Get ( 'sockets' );
		$temperatureArea = Get ( 'temperatureArea' );
		if ($id != '' & $position != '' & $type != '' & $schedules != '' & $sockets != '' & $temperatureArea != '') {
			echo Send ( "$login:MAPCONTENT:UPDATE:$id:$position:$type:$schedules:$sockets:$temperatureArea" );
		} else {
			echo "Error 145:Parameter not found for mapcontent";
		}
		break;
	case 'deletemapcontent' :
		$id = Get ( 'id' );
		if ($id != '') {
			echo Send ( "$login:MAPCONTENT:DELETE:$id" );
		} else {
			echo "Error 145:Parameter not found for mapcontent";
		}
		break;
	
	/* --------------------- Movie --------------------- */
	case 'getmovies' :
		$movieCount = Send ( "$login:MOVIE:GET:COUNT" );
		if (strpos ( $movieCount, 'Error' ) !== false) {
			echo $movieCount;
		} else {
			$movies = "";
			$startIndex = 0;
			$endIndex = 25;
			while ( $startIndex < $movieCount - 1 ) {
				$response = Send ( "$login:MOVIE:GET:INDEX:$startIndex:$endIndex" );
				if (strpos ( $response, 'Error' ) !== false) {
					echo $response;
					break;
				} else {
					$movies .= $response;
					$startIndex += 25;
					$endIndex += 25;
				}
			}
			echo $movies;
		}
		break;
	case 'addmovie' :
		$title = Get ( 'title' );
		$genre = Get ( 'genre' );
		$description = Get ( 'description' );
		$rating = Get ( 'rating' );
		$watched = Get ( 'watched' );
		$sockets = Get ( 'sockets' );
		if ($title != '') {
			echo Send ( "$login:MOVIE:ADD:$title:$genre:$description:$rating:$watched:$sockets" );
		} else {
			echo "Error 400:Parameter not found for movie";
		}
		break;
	case 'updatemovie' :
		$title = Get ( 'title' );
		$genre = Get ( 'genre' );
		$description = Get ( 'description' );
		$rating = Get ( 'rating' );
		$watched = Get ( 'watched' );
		$sockets = Get ( 'sockets' );
		if ($title != '') {
			echo Send ( "$login:MOVIE:UPDATE:$title:$genre:$description:$rating:$watched:$sockets" );
		} else {
			echo "Error 400:Parameter not found for movie";
		}
		break;
	case 'deletemovie' :
		$title = Get ( 'title' );
		if ($title != '') {
			echo Send ( "$login:MOVIE:DELETE:$title" );
		} else {
			echo "Error 400:Parameter not found for movie";
		}
		break;
	case 'startmovie' :
		$title = Get ( 'title' );
		if ($title != '') {
			echo Send ( "$login:MOVIE:START:$title" );
		} else {
			echo "Error 400:Parameter not found for movie";
		}
		break;
	
	/* ---------------------- Gpio --------------------- */
	case 'getgpios' :
		echo Send ( "$login:REMOTE:GET:GPIO:ALL" );
		break;
	case 'addgpio' :
		$name = Get ( 'name' );
		$gpio = Get ( 'gpio' );
		if ($name != '' && $gpio != '') {
			echo Send ( "$login:REMOTE:ADD:GPIO:$name:$gpio:0" );
		} else {
			echo "Error 121:Parameter not found for remote";
		}
		break;
	case 'deletegpio' :
		$name = Get ( 'name' );
		if ($name != '') {
			echo Send ( "$login:REMOTE:DELETE:GPIO:$name" );
		} else {
			echo "Error 121:Parameter not found for remote";
		}
		break;
	case 'setgpio' :
		$gpio = Get ( 'gpio' );
		$state = Get ( 'state' );
		if ($gpio != '' && $state != '') {
			echo Send ( "$login:REMOTE:SET:GPIO:$gpio:$state" );
		} else {
			echo "Error 121:Parameter not found for remote";
		}
		break;
	case 'activateAllGpios' :
		echo Send ( "$login:REMOTE:SET:GPIO:ALL:1" );
		break;
	case 'deactivateAllGpios' :
		echo Send ( "$login:REMOTE:SET:GPIO:ALL:0" );
		break;
	
	/* -------------------- Schedule ------------------- */
	case 'getschedules' :
		echo Send ( "$login:REMOTE:GET:SCHEDULE:ALL" );
		break;
	case 'addschedule' :
		$name = Get ( 'name' );
		$socket = Get ( 'socket' );
		$gpio = Get ( 'gpio' );
		$weekday = Get ( 'weekday' );
		$hour = Get ( 'hour' );
		$minute = Get ( 'minute' );
		$onoff = Get ( 'onoff' );
		$isTimer = Get ( 'isTimer' );
		$playSound = Get ( 'playSound' );
		$playRaspberry = Get ( 'playRaspberry' );
		if ($name != '' && ($socket != '' || $gpio != '') && $weekday != '' && $hour != '' && $minute != '' && $onoff != '' && $isTimer != '' && $playSound != '' && $playRaspberry != '') {
			echo Send ( "$login:REMOTE:ADD:SCHEDULE:$name:$socket:$gpio:$weekday:$hour:$minute:$onoff:$isTimer:$playSound:$playRaspberry:1" );
		} else {
			echo "Error 121:Parameter not found for remote";
		}
		break;
	case 'updateschedule' :
		$name = Get ( 'name' );
		$socket = Get ( 'socket' );
		$gpio = Get ( 'gpio' );
		$weekday = Get ( 'weekday' );
		$hour = Get ( 'hour' );
		$minute = Get ( 'minute' );
		$onoff = Get ( 'onoff' );
		$isTimer = Get ( 'isTimer' );
		$playSound = Get ( 'playSound' );
		$playRaspberry = Get ( 'playRaspberry' );
		$isActive = Get( 'isactive' );
		if ($name != '' && ($socket != '' || $gpio != '') && $weekday != '' && $hour != '' && $minute != '' && $onoff != '' && $isTimer != '' && $playSound != '' && $playRaspberry != '' && $isActive != '') {
			echo Send ( "$login:REMOTE:UPDATE:SCHEDULE:$name:$socket:$gpio:$weekday:$hour:$minute:$onoff:$isTimer:$playSound:$playRaspberry:$isActive" );
		} else {
			echo "Error 121:Parameter not found for remote";
		}
		break;
	case 'deleteschedule' :
		$schedule = Get ( 'schedule' );
		if ($schedule != '') {
			echo Send ( "$login:REMOTE:DELETE:SCHEDULE:$schedule" );
		} else {
			echo "Error 121:Parameter not found for remote";
		}
		break;
	case 'setschedule' :
		$schedule = Get ( 'schedule' );
		$state = Get ( 'state' );
		if ($schedule != '' && $state != '') {
			echo Send ( "$login:REMOTE:SET:SCHEDULE:$schedule:$state" );
		} else {
			echo "Error 121:Parameter not found for remote";
		}
		break;
	case 'activateAllSchedules' :
		echo Send ( "$login:REMOTE:SET:SCHEDULE:ALL:1" );
		break;
	case 'deactivateAllSchedules' :
		echo Send ( "$login:REMOTE:SET:SCHEDULE:ALL:0" );
		break;
	
	/* ---------------- Wireless Socket ---------------- */
	case 'getsockets' :
		echo Send ( "$login:REMOTE:GET:SOCKET:ALL" );
		break;
	case 'addsocket' :
		$name = Get ( 'name' );
		$area = Get ( 'area' );
		$code = Get ( 'code' );
		if ($name != '' && $area != '' && $code != '') {
			echo Send ( "$login:REMOTE:ADD:SOCKET:$name:$area:$code:0" );
		} else {
			echo "Error 121:Parameter not found for remote";
		}
		break;
	case 'updatesocket' :
		$name = Get ( 'name' );
		$area = Get ( 'area' );
		$code = Get ( 'code' );
		$isactivated = Get ( 'isactivated' );
		if ($name != '' && $area != '' && $code != '' && $isactivated != '') {
			echo Send ( "$login:REMOTE:UPDATE:SOCKET:$name:$area:$code:$isactivated" );
		} else {
			echo "Error 121:Parameter not found for remote";
		}
		break;
	case 'deletesocket' :
		$socket = Get ( 'socket' );
		if ($socket != '') {
			echo Send ( "$login:REMOTE:DELETE:SOCKET:$socket" );
		} else {
			echo "Error 121:Parameter not found for remote";
		}
		break;
	case 'setsocket' :
		$socket = Get ( 'socket' );
		$state = Get ( 'state' );
		if ($socket != '' && $state != '') {
			echo Send ( "$login:REMOTE:SET:SOCKET:$socket:$state" );
		} else {
			echo "Error 121:Parameter not found for remote";
		}
		break;
	case 'activateAllSockets' :
		echo Send ( "$login:REMOTE:SET:SOCKET:ALL:1" );
		break;
	case 'deactivateAllSockets' :
		echo Send ( "$login:REMOTE:SET:SOCKET:ALL:0" );
		break;
	
	/* --------------------- Sound --------------------- */
	case 'startplaying' :
		$song = Get ( 'song' );
		if ($song != '') {
			echo Send ( "$login:SOUND:PLAY:$song" );
		} else {
			echo "Error 94:Parameter not found for sound";
		}
		break;
	case 'stopplaying' :
		echo Send ( "$login:SOUND:STOP:ALL" );
		break;
	case 'increasevolume' :
		echo Send ( "$login:SOUND:SET:VOLUME:INCREASE" );
		break;
	case 'decreasevolume' :
		echo Send ( "$login:SOUND:SET:VOLUME:DECREASE" );
		break;
	case 'getvolume' :
		echo Send ( "$login:SOUND:GET:VOLUME" );
		break;
	case 'getsounds' :
		echo Send ( "$login:SOUND:GET:FILES" );
		break;
	case 'issoundplaying' :
		echo Send ( "$login:SOUND:GET:PLAYING" );
		break;
	case 'getplayingfile' :
		echo Send ( "$login:SOUND:GET:PLAYINGFILE" );
		break;
	
	/* ------------------ Temperature ------------------ */
	case 'getcurrenttemperature' :
		echo Send ( "$login:TEMPERATURE:GET:WEBSITE" );
		break;
	case 'getcurrenttemperaturerest' :
		echo Send ( "$login:TEMPERATURE:GET:REST" );
		break;
	case 'gettemperaturegraphurl' :
		echo Send ( "$login:REMOTE:GET:URL:TEMPERATURE" );
		break;
	
	/* --------------------- Remote -------------------- */
	case 'getraspberry' :
		echo Send ( "$login:REMOTE:GET:RASPBERRY" );
		break;
	case 'getarea' :
		echo Send ( "$login:REMOTE:GET:AREA" );
		break;
	
	/* ---------------------- User --------------------- */
	case 'validateuser' :
		echo Send ( "$login:USER:VALIDATE:NOW" );
		break;
	
	/* ----------------- Access Control ---------------- */
	case 'activatealarm' :
		echo Send ( "$login:ACCESS:ACTIVATE:ALARM" );
		break;
	case 'sendcode' :
		$code = Get ( 'code' );
		if ($code != '') {
			echo Send ( "$login:ACCESS:CHECK:CODE:$code" );
		} else {
			echo "Error 200:Parameter not found for access control";
		}
	
	/* ---------------------- Other -------------------- */
	case 'main' :
		echo Send ( "$login:SERVER:AVAILABILITY:CHECK" );
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
		$out = fread ( $socket, 65536 );
		fclose ( $socket );
	}
	return $out;
}

/* ================== Get Informations ================= */
function GetInformations() {
	return Send ( "Website:234524:INFORMATION:GET:WEBSITE" );
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
	return Send ( "Website:234524:CHANGE:GET:WEBSITE" );
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
	return Send ( "Website:234524:REMOTE:GET:AREA" );
}

/* =================== Get Temperature ================= */
function GetTemperature() {
	return Send ( "Website:234524:TEMPERATURE:GET:WEBSITE" );
}

/* ============== Get Temperature Graph URL ============ */
function GetTemperatureGraphUrl() {
	return Send ( "Website:234524:REMOTE:GET:URL:TEMPERATURE" );
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
