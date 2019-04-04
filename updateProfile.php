<?php
require_once 'connection.php';

$profile_pic = $_POST["profile_pic"];


//our boolean function
function isTheseParametersAvailable($params){
    foreach($params as $paramKey){
        if(!isset($_POST[$paramKey])){
            return false;
        }
    }
    return true;
}