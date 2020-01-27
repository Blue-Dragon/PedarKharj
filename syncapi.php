<?php
require_once 'connection.php';
include 'Routines.php';
$response= array();


//if request is 'apicall'
/**
 * @param $conn
 * @return mixed
 */


if (isset($_REQUEST['apicall'])) {
    switch ($_REQUEST['apicall']) {

        //if clicked on sign up
        case 'count':
            //now send user info back to client
            $usersCount = getUsersCount1($conn, 'users');

            $response['error'] = false;
            $response['message'] = "users num : " . $usersCount-> total_count;
            break;

        case 'add_user':
            if (isset( $_REQUEST['name'])){
                $name= $_REQUEST['name'];
                //add user
                $stmt = $conn->prepare("INSERT INTO users( name ) VALUES (?)");

                $msg = addUser(  $name);
                $response['error'] = false;
                $response['message'] = $msg ." -> " .$name;

            }else {
                $response['error'] = true;
                $response['message'] = 'name needed';
            }

            break;

        case 'get_user':
            if (isset($_REQUEST['user_id'])){
                $userId = $_REQUEST['user_id'];
                getUser($conn, $userId);
            }
            break;

        case 'get_all_users_array':
            $users = getAllUsersArray($conn);

            $response['error'] = false;
            $response['message'] = "users retrieved successfully";
            $response['users'] = $users;
            break;

        //if clicked on something else
        default:
            $response['error'] = true;
            $response['message'] = 'Invalid Operation Called';
    }

} //if request is NOT 'apicall'
else {
    $response['error'] = true;
    $response['message'] = 'Invalid API Call';
}


echo json_encode($response);


////////////////


/*****************************      Functions       *******************************/
