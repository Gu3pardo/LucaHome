<?php
class Config {
	function Config() {
		// General
		$this->LANG = 'EN';
		$this->DOMAIN = '';
		$this->BASEDIR = '.';
		define ( '_BASEDIR', $this->BASEDIR );
		// Template-Engine
		$this->TPL_DIR = './pages/';
		$this->TPL_TPLDIR = './pages/tpl/';
		$this->TPL_THEME = 'default';
		// Navigation
		$this->NAV_DEFAULT = _BASEDIR . '/index.php?page=home&action=informations';
		$this->NAV_PAGES = array (
				'home' 
		);
		// Meta
		$this->META_TITLE = 'LucaHome 0.1.0.161012';
		$this->META_DESCRIPTION = 'Control your tv, sound and more. Display temperature and show your movies via your android smartphone.';
		$this->META_KEYWORDS_EN = 'SMART, HOME, RASPBERRY, MOVIE, SOCKET, BIRTHDAY';
		$this->META_KEYWORDS_DE = 'SMART, HOME, RASPBERRY, FILM, STECKDOSE, GEBURTSTAG';
		$this->META_AUTHOR = 'Jonas Schubert';
	}
}
?>