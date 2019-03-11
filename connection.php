<?php

const HOST_NAME = "localhost";
const USERNAME = "root";
const PASSWORD = "";
const DB_NAME = "registerlogin";

$conn = new mysqli(HOST_NAME, USERNAME, PASSWORD, DB_NAME);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>