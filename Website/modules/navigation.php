<?php
class Navigation {
	var $app;
	var $class;
	var $pages;
	var $currentpage;
	var $defaultaction;
	function Navigation(&$app) {
		$this->app = $app;
		$this->class = null;
		$this->pages = null;
		$this->currentpage = '';
	}
	function AddPage($page) {
		if ($page != '' && ! isset ( $this->pages [$page] ))
			$this->currentpage = $page;
	}
	function AddAction($action, $function) {
		if ($this->currentpage != '' && $action != '' && ! isset ( $this->pages [$this->currentpage] [$action] ) && $function != '')
			$this->pages [$this->currentpage] [$action] = $function;
	}
	function DefaultAction($action) {
		if ($this->currentpage != '' && ! isset ( $this->defaultaction [$this->currentpage] ) && $action != '')
			$this->defaultaction [$this->currentpage] = $action;
	}
	function CallAction($action) {
		if ($this->class != null && isset ( $this->pages [$this->currentpage] [$action] ))
			call_user_func ( array (
					$this->class,
					$this->pages [$this->currentpage] [$action] 
			) );
	}
	function CallDefaultAction() {
		if ($this->class != null && isset ( $this->defaultaction [$this->currentpage] ) && isset ( $this->pages [$this->currentpage] [$this->defaultaction [$this->currentpage]] ))
			call_user_func ( array (
					$this->class,
					$this->pages [$this->currentpage] [$this->defaultaction [$this->currentpage]] 
			) );
	}
	function LoadPage($page, $action) {
		$page = strtolower ( $page );
		$action = strtolower ( $action );
		
		if ($page != '' && in_array ( $page, $this->app->Conf->NAV_PAGES )) {
			$path = $this->app->Conf->TPL_DIR . $page . '.php';
			if (file_exists ( $path )) {
				// Include Class
				include_once ($path);
				
				// Start Instance
				$this->class = new $page ( $this->app );
				
				if ($action != '')
					$this->CallAction ( $action );
				else
					$this->CallDefaultAction ();
			} else
				$this->Go ( $this->app->Conf->NAV_DEFAULT );
		} else
			$this->Go ( $this->app->Conf->NAV_DEFAULT );
	}
	function Go($link) {
		header ( "Location: $link" );
		exit ();
	}
	function GoDefault() {
		$this->Go ( $this->app->Conf->NAV_DEFAULT );
	}
}
?>