<?php
class Home {
	var $app;
	function Home(&$app) {
		$this->app = $app;
		$this->app->Nav->AddPage ( 'main' );
		$this->app->Nav->AddAction ( 'main', 'Main' );
		$this->app->Nav->DefaultAction ( 'main' );
	}
	function Main() {
		include ('./lib/lucahome.php');
		
		// Informations
		
		$dataInformations = GetInformations ();
		$informations = ParseInformations ( $dataInformations );
		
		$information_table = '';
		for($i = 0; $i < count ( $informations ); $i ++) {
			$information_table .= "
						<tr>
							<td class=\"information_type\">{$informations[$i]['key']}</td>
                        	<td class=\"information_value\">{$informations[$i]['value']}</td>
                        </tr>";
		}
		$this->app->Tpl->Set ( 'INFORMATIONTABLE', $information_table );
		
		// Changes
		
		$dataChanges = GetChanges ();
		$changes = ParseChanges ( $dataChanges );
		
		$change_table = '';
		for($i = 0; $i < count ( $changes ); $i ++) {
			$change_table .= "
						<tr>
							<td class=\"change_type\">{$changes[$i]['type']}</td>
                        	<td class=\"change_time\">{$changes[$i]['hour']}:{$changes[$i]['minute']} / {$changes[$i]['day']}.{$changes[$i]['month']}.{$changes[$i]['year']}</td>
							<td class=\"change_user\">{$changes[$i]['user']}</td>
                        </tr>";
		}
		$this->app->Tpl->Set ( 'CHANGETABLE', $change_table );
		
		// Temperature
		
		$area = GetArea ();
		$temperature = GetTemperature ();
		$graphUrl = GetTemperatureGraphUrl ();
		
		$link_text = "Temperature Log ";
		$link_text .= $area;
		
		$link_value = "http://";
		$link_value .= $graphUrl;
		
		$temp_out = '';
		$temp_out .= "<div class=\"button socket\">
		<div class=\"button_text temperature_value\">{$area}:   {$temperature} &#176;C</div>
		</div>";
		
		$this->app->Tpl->Set ( 'TEMPERATURE', $temp_out );
		$this->app->Tpl->Set ( 'LINK_TEXT', $link_text );
		$this->app->Tpl->Set ( 'LINK_VALUE', $link_value );
		$this->app->Tpl->Set ( 'MENUTEMPERATURE', 'class="active"' );
		
		$this->app->Tpl->Set ( 'MENUINFORMATION', 'class="active"' );
		$this->app->Tpl->Parse ( 'PAGE', 'page_main.tpl' );
	}
}
?>