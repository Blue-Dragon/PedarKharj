<!DOCTYPE html>
<html>
<head>
    <title>Add new User 0</title>
</head>

<body>
<form action="temp.php" method="get">
    <input type="text" size="10" name="name">
    <input type="submit" name="apicall" value="count">  <br><br>
</form>

<form action="temp.php" method="get">
    Add new user:<br>
    name <input type="text" size="10" name="contact_name">
    image <input type="text" size="10" name="contact_img">
    created at <input type="text" size="10" name="created_at">

    <input type="submit" name="apicall" value="add_user"> <br><br>
</form>


<form action="temp.php" method="get">
    Enter User's id:<br>
    <input type="text" size="10" name="user_id">
    <input type="submit" name="apicall" value="get_user">
    <input type="submit" name="apicall" value="get_all_users">
    <input type="submit" name="apicall" value="get_all_users_array"> <br>
</form>


</body>
</html>

<?php
require_once 'connection.php';
include 'Routines.php';


if (isset($_REQUEST['apicall'])) {
    switch ($_REQUEST['apicall']) {

        //if clicked on sign up
        case 'count':
            $table = "contacts";
            if (isset ($_REQUEST['name']) && $_REQUEST['name']  ){
                $table = $_REQUEST['name'];
            }

            echo "row num is : ". getUsersCount1($conn, $table) -> total_count;
            break;

        case 'get_user':
            $userId = 30;
            if (isset($_REQUEST['user_id']) && $_REQUEST['user_id'] > 0 ){
                $userId = $_REQUEST['user_id'];
            }
            getUser($conn, $userId);
            break;

        case 'add_user':
            $name ='name';
            $img = 'img';
            $created_at = 'created_at';

            if (areTheseParamsAvailable(array('contact_name', 'contact_img', 'created_at')) ) {
                $name= $_REQUEST['contact_name'];
                $img= $_REQUEST['contact_img'];
                $created_at= $_REQUEST['created_at'];

                //add user
                if ($name != null && $created_at != null){
                    $stmt = $conn->prepare("INSERT INTO contacts( contact_name, contact_img, created_at) VALUES (?, ?, ?)");
                    $msg = addUser($conn, $name, $img, $created_at);
                    $response['error'] = false;
                    $response['message'] = $msg ." ->  $name/ created at : $created_at";
                }else {
                    $response['error'] = true;
                    $response['message'] = 'Failed - name or date is null';
                }

            }else {
                $response['error'] = true;
                $response['message'] = 'Failed - some args needed'; 
            }

            echo $response['message'];
            break;

        case 'get_all_users':;
            getAllUsers($conn);
            break;

        case 'get_all_users_array':;
            getAllUsersArray($conn);
            break;
    }


} else{
    echo 'wrong apicall !';
}

/*****************************      Functions       *******************************/
