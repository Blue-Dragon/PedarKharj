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
            $usersCount = getUsersCount1($conn, 'contacts');

            $response['error'] = false;
            $response['message'] = "users num : " . $usersCount-> total_count;
            break;

        case 'add_user':
            $name ='name';
            $img = 'img';
            $created_at = 'created_at';

            if (areTheseParamsAvailable('contact_name', 'contact_img', 'created_at') ) {
                $name= $_REQUEST['contact_name'];
                $img= $_REQUEST['contact_img'];
                $created_at= $_REQUEST['created_at'];

                //add user
                if ($name != null && $created_at != null){
                    $stmt = $conn->prepare("INSERT INTO users( contact_name, contact_img, created_at) VALUES (???)");
                    $msg = addUser($name, $img, $created_at);

                    $response['error'] = false;
                    $response['message'] = $msg ." ->  $name/ created at : $created_at";

                }else {
                    $response['error'] = true;
                    $response['message'] = 'Failed - name or date is null';

                    }


            }else {
                $response['error'] = true;
                $response['message'] = 'some args needed';
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
