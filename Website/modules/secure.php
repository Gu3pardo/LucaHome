<?php
class Secure {
	function Secure() {
	}
	function GetGET($name) {
		$value = $_GET [$name];
		
		if ($value != '')
			return $this->doFilter ( $value );
		
		return '';
	}
	function GetPOST($name) {
		$value = '';
		if (isset ( $_POST [$name] ))
			$value = $_POST [$name];
		
		if ($value != '')
			return $this->doFilter ( $value );
		
		return '';
	}
	function Filter($var) {
		return strip_tags ( $var );
	}
	function doFilter($arr) {
		if (is_array ( $arr ) && count ( $arr ) > 0) {
			foreach ( $arr as $key => $value )
				$arr [$key] = $this->doFilter ( $value );
			return $arr;
		} else
			return $this->Filter ( $arr );
	}
	function ValidateEmail($email) {
		$isValid = true;
		$atIndex = strrpos ( $email, '@' );
		if (is_bool ( $atIndex ) && ! $atIndex)
			$isValid = false;
		else {
			$domain = substr ( $email, $atIndex + 1 );
			$local = substr ( $email, 0, $atIndex );
			$localLen = strlen ( $local );
			$domainLen = strlen ( $domain );
			if ($localLen < 1 || $localLen > 64)
				$isValid = false;
			else if ($domainLen < 1 || $domainLen > 255)
				$isValid = false;
			else if ($local [0] == '.' || $local [$localLen - 1] == '.')
				$isValid = false;
			else if (preg_match ( '/\\.\\./', $local ))
				$isValid = false;
			else if (! preg_match ( '/^[A-Za-z0-9\\-\\.]+$/', $domain ))
				$isValid = false;
			else if (preg_match ( '/\\.\\./', $domain ))
				$isValid = false;
			else if (! preg_match ( '/^(\\\\.|[A-Za-z0-9!#%&`_=\\/$\'*+?^{}|~.-])+$/', str_replace ( "\\\\", "", $local ) )) {
				if (! preg_match ( '/^"(\\\\"|[^"])+"$/', str_replace ( "\\\\", "", $local ) ))
					$isValid = false;
			}
			if ($isValid && ! (checkdnsrr ( $domain, "MX" ) || checkdnsrr ( $domain, "A" )))
				$isValid = false;
		}
		return $isValid;
	}
}
?>