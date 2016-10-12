<?php
class Core {
	var $app;
	function Core(&$app) {
		$this->app = $app;
	}
	function SetTheme() {
		$this->app->Tpl->Set ( 'THEME', $this->app->Conf->TPL_THEME );
	}
	function ReplaceKomma($value) {
		return str_replace ( ',', '.', $value );
	}
	function RemoveKommaAndPoint($value) {
		$value = str_replace ( ',', '', $value );
		return str_replace ( '.', '', $value );
	}
	function MBToByte($mb) {
		return $mb * 1048576;
	}
	function WriteFile($data, $path) {
		$handler = fopen ( $path, 'w' );
		fwrite ( $handler, $data );
		fclose ( $handler );
	}
	function Encode($text) {
		$text = $this->ReplaceUmlaute ( $text );
		return str_replace ( ' ', '-', $text );
	}
	function Decode($text) {
		return str_replace ( '-', ' ', $text );
	}
	function ReplaceUmlaute($text) {
		$text = str_replace ( "\xc4", 'Ae', $text );
		$text = str_replace ( "\xe4", 'ae', $text );
		$text = str_replace ( "\xd6", 'Oe', $text );
		$text = str_replace ( "\xf6", 'oe', $text );
		$text = str_replace ( "\xdc", 'Ue', $text );
		$text = str_replace ( "\xfc", 'ue', $text );
		$text = str_replace ( "\xdf", 'ss', $text );
		
		return $text;
	}
	function ReplaceSpecialChars($text) {
		$text = str_replace ( array (
				"\xc0",
				"\xc1",
				"\xc2",
				"\xc3",
				"\xc4",
				"\xc5",
				"\xc6" 
		), 'A', $text );
		$text = str_replace ( array (
				"\xe0",
				"\xe1",
				"\xe2",
				"\xe3",
				"\xe4",
				"\xe5",
				"\xe6" 
		), 'a', $text );
		$text = str_replace ( array (
				"\xc8",
				"\xc9",
				"\xca",
				"\xcb" 
		), 'E', $text );
		$text = str_replace ( array (
				"\xe8",
				"\xe9",
				"\xea",
				"\xeb" 
		), 'e', $text );
		$text = str_replace ( array (
				"\xcc",
				"\xcd",
				"\xce",
				"\xcf" 
		), 'I', $text );
		$text = str_replace ( array (
				"\xec",
				"\xed",
				"\xee",
				"\xef" 
		), 'i', $text );
		$text = str_replace ( array (
				"\xd2",
				"\xd3",
				"\xd4",
				"\xd5",
				"\xd6" 
		), 'O', $text );
		$text = str_replace ( array (
				"\xf2",
				"\xf3",
				"\xf4",
				"\xf5",
				"\xf6" 
		), 'o', $text );
		$text = str_replace ( array (
				"\xd9",
				"\xda",
				"\xdb",
				"\xdc" 
		), 'U', $text );
		$text = str_replace ( array (
				"\xf9",
				"\xfa",
				"\xfb",
				"\xfc" 
		), 'u', $text );
		$text = str_replace ( "\xd1", 'N', $text );
		$text = str_replace ( "\xf1", 'n', $text );
		$text = str_replace ( "\xdf", 'ss', $text );
		
		$text = preg_replace ( '/[^A-za-z0-9[:space:]]+/', " ", $text );
		return $text;
	}
	function ClientAdress() {
		if (! empty ( $_SERVER ['HTTP_CLIENT_IP'] ))
			$ip = $_SERVER ['HTTP_CLIENT_IP'];
		elseif (! empty ( $_SERVER ['HTTP_X_FORWARDED_FOR'] ))
			$ip = $_SERVER ['HTTP_X_FORWARDED_FOR'];
		else
			$ip = $_SERVER ['REMOTE_ADDR'];
		return $ip;
	}
	function ParseSelect($data, $emptyline = '') {
		$options = (($emptyline != '') ? '<option value="">' . $emptyline . '</option>' : '');
		for($i = 0; $i < count ( $data ); $i ++) {
			$options .= "<option value=\"{$data[$i]['name']}\">{$data[$i]['name']}</option>";
		}
		
		return $options;
	}
	function TimeSelect($max) {
		$time = '';
		for($i = 0; $i <= $max; $i ++) {
			$format = str_pad ( $i, 2, 0, STR_PAD_LEFT );
			$time .= "<option value=\"$format\">$format</option>";
		}
		return $time;
	}
	function ArraySearch($key, $value, $arr) {
		if (is_array ( $arr )) {
			for($i = 0; $i < count ( $arr ); $i ++) {
				if ($arr [$i] [$key] == $value)
					return true;
			}
		}
		return false;
	}
}
?>
