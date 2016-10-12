<?php
// Config
include ('./conf/config.php');

// Modules
include ('./modules/core.php');
include ('./modules/secure.php');
include ('./modules/template.php');
include ('./modules/navigation.php');
include ('./modules/meta.php');

$app = new stdClass ();
$app->Conf = new Config ();
$app->Core = new Core ( $app );
$app->Secure = new Secure ();
$app->Tpl = new Template ( $app );
$app->Nav = new Navigation ( $app );
$app->Meta = new Meta ( $app );
?>