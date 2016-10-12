<?php
class Template {
	var $app;
	var $parse;
	var $parse_file;
	function Template(&$app) {
		$this->app = $app;
		$this->template = 'index.html';
	}
	function SetTemplate($template) {
		if (file_exists ( './themes/' . $this->app->Conf->TPL_THEME . '/' . $template ))
			$this->template = $template;
	}
	function Set($tag, $value_de, $value_en = '') {
		if ($tag == '')
			return;
		
		if ($value_en != '')
			$value = (($this->app->Core->GetLanguage () == 'EN') ? $value_en : $value_de);
		else
			$value = $value_de;
		
		if (! (isset ( $this->parse [$tag] ) || isset ( $this->parse_file [$tag] )))
			$this->parse [$tag] = $value;
	}
	function Parse($tag, $template_de, $template_en = '') {
		if ($tag == '' || ! ($template_de != '' || $template_en != ''))
			return;
		
		if ($template_en != '')
			$template = (($this->app->Core->GetLanguage () == 'EN') ? $template_en : $template_de);
		else
			$template = $template_de;
		
		if (! (isset ( $this->parse [$tag] ) || isset ( $this->parse_file [$tag] )))
			$this->parse_file [$tag] = $template;
	}
	function Display() {
		$page = file_get_contents ( './themes/' . $this->app->Conf->TPL_THEME . '/' . $this->template );
		
		if ($page == '') {
			echo 'ERROR: Can\'t open index.html<br>';
			exit ();
		}
		
		// Parse Tpl-Files
		if (is_array ( $this->parse_file )) {
			$parse_file = array_reverse ( $this->parse_file, true );
			foreach ( $parse_file as $key => $value ) {
				$path = $this->app->Conf->TPL_TPLDIR . $value;
				
				if (file_exists ( $path )) {
					$content = file_get_contents ( $path );
					$page = str_replace ( '[' . $key . ']', $content, $page );
				}
			}
		}
		
		// Parse Variables
		if (is_array ( $this->parse )) {
			foreach ( $this->parse as $key => $value )
				$page = str_replace ( '[' . $key . ']', $value, $page );
		}
		
		// Search for unsetted TAGS
		$page = preg_replace ( '/\[[A-Z]+[0-9]*[A-Z]+\]/', '', $page );
		
		echo $page;
	}
}
?>