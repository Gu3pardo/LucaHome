<?php
session_start ();
include ('./system.php');

// Set Theme
$app->Core->SetTheme ();

// Get Page and Action
$page = $app->Secure->GetGET ( "page" );
$action = $app->Secure->GetGET ( "action" );

// Open Page
$app->Nav->LoadPage ( $page, $action );

// Apply Metatags
$app->Meta->Invisible ();
$app->Meta->Apply ();

// Display Page
$app->Tpl->Set ( 'BASEDIR', _BASEDIR );
$app->Tpl->Display ();
?>
