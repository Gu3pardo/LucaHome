<?php
class Meta {
	var $app;
	function Meta(&$app) {
		$this->app = $app;
		
		$this->title = $app->Conf->META_TITLE;
		$this->description = $app->Conf->META_DESCRIPTION;
		$this->keywords_de = $app->Conf->META_KEYWORDS_DE;
		$this->keywords_en = $app->Conf->META_KEYWORDS_EN;
		$this->author = $app->Conf->META_AUTHOR;
		
		$this->date = date ( 'd-m-Y' );
		$this->index = true;
		$this->follow = true;
	}
	function Title($title) {
		$this->title = $title;
	}
	function Description($description) {
		$this->description = $description;
	}
	function KeywordsDE($keywords) {
		$this->keywords_de = $keywords;
	}
	function KeywordsEN($keywords) {
		$this->keywords_en = $keywords;
	}
	function Author($author) {
		$this->author = $author;
	}
	function NoIndex() {
		$this->index = false;
	}
	function NoFollow() {
		$this->follow = false;
	}
	function Invisible() {
		$this->index = false;
		$this->follow = false;
	}
	function Apply() {
		$this->app->Tpl->Set ( 'METATITLE', $this->title );
		$this->app->Tpl->Set ( 'METADESCRIPTION', $this->description );
		if ($this->keywords_de != '')
			$this->app->Tpl->Set ( 'METAKEYWORDSDE', "\n" . '  <meta name="keywords" lang="de" content="' . $this->keywords_de . '"/>' );
		$this->app->Tpl->Set ( 'METAKEYWORDSEN', $this->keywords_en );
		$this->app->Tpl->Set ( 'METAAUTHOR', $this->author );
		$this->app->Tpl->Set ( 'METADATE', $this->date );
		
		$index_out = (($this->index) ? 'index' : 'noindex');
		$follow_out = (($this->follow) ? 'follow' : 'nofollow');
		$this->app->Tpl->Set ( 'METAINDEX', "$index_out,$follow_out" );
	}
}

?>
