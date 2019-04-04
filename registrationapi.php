<?php
require_once 'connection.php';
$response = array();

//if request is 'apicall'
if(isset($_GET['apicall'])){
    switch($_GET['apicall']){

        //if clicked on sign up
        case 'signup':
            if(isTheseParametersAvailable(array('username','email','password','gender'))){
                $username = $_POST['username'];
                $email = $_POST['email'];
                $password = md5($_POST['password']);
                $gender = $_POST['gender'];

                $stmt = $conn->prepare("SELECT id FROM users WHERE username = ? OR email = ?"); //get id if this user exists
                $stmt->bind_param("ss", $username, $email);
                $stmt->execute();
                $stmt->store_result();

                //if id != 0 i.e. user exists
                if($stmt->num_rows > 0){
                    $response['error'] = true;
                    $response['message'] = 'User already registered';
                    $stmt->close();
                }
                else{
                    $stmt = $conn->prepare("INSERT INTO users (username, email, password, gender) VALUES (?, ?, ?, ?)");
                    $stmt->bind_param("ssss", $username, $email, $password, $gender);

                    if($stmt->execute()){
                        $stmt = $conn->prepare("SELECT id, username, email, gender FROM users WHERE username = ?");
                        $stmt->bind_param("s",$username);
                        $stmt->execute();
                        //get info
                        $stmt->bind_result($id, $username, $email, $gender);
                        $stmt->fetch();

                        $user = array(
                            'id'=>$id,
                            'username'=>$username,
                            'email'=>$email,
                            'gender'=>$gender
                        );

                        $stmt->close();

                        $response['error'] = false;
                        $response['message'] = 'User registered successfully';
                        $response['user'] = $user;
                    }
                }

            }
            else{ //if parameters are not given
                $response['error'] = true;
                $response['message'] = 'required parameters are not available';
            }
            break;

        //if clicked on Login
        case 'login':
            if(isTheseParametersAvailable(array('username', 'password'))){
                $username = $_POST['username'];
                $password = md5($_POST['password']);

                $stmt = $conn->prepare("SELECT id, username, email, gender FROM users WHERE username = ? AND password = ?");
                $stmt->bind_param("ss",$username, $password);
                $stmt->execute();
                $stmt->store_result();

                //if user exists
                if($stmt->num_rows > 0){
                    //get info
                    $stmt->bind_result($id, $username, $email, $gender);
                    $stmt->fetch();

                    $user = array(
                        'id'=>$id,
                        'username'=>$username,
                        'email'=>$email,
                        'gender'=>$gender
                    );

                    $response['error'] = false;
                    $response['message'] = 'Login successful';
                    $response['user'] = $user;
                }
                else{
                    $response['error'] = false;
                    $response['message'] = 'Invalid username or password';
                }
            }
            break;

        //if clicked on update
        case 'update':
            if(isTheseParametersAvailable(array('id','username','email','password','gender'))){
                $id = $_POST['id'];
                $username = $_POST['username'];
                $email = $_POST['email'];
                $password = md5($_POST['password']);
                $gender = $_POST['gender'];

                //if this id exists
                if ($id > 0){
                    $stmt = $conn->prepare("UPDATE users SET username=?, email=?, password=?, gender=? WHERE id = '$id'");
                    $stmt->bind_param("ssss", $username, $email, $password, $gender);
//                    $stmt->execute();
//                    $stmt->store_result();


                    //now send user info back to client
                    if($stmt->execute()){
                        $stmt = $conn->prepare("SELECT id, username, email, gender FROM users WHERE username = ?");
                        $stmt->bind_param("s",$username);
                        $stmt->execute();
                        //get info
                        $stmt->bind_result($id, $username, $email, $gender);
                        $stmt->fetch();

                        $user = array(
                            'id'=>$id,
                            'username'=>$username,
                            'email'=>$email,
                            'gender'=>$gender
                        );

                        $stmt->close();


                        $response['error'] = false;
                        $response['message'] = 'User info updated successfully';
                        $response['user'] = $user;

                        //get profile pic
                        $profile_pic = $_POST['profilePic'];
                        if ( isset($profile_pic) ){
                            $profilePic_path = "profile_pics/$username.jpg";
                            file_put_contents($profilePic_path, base64_decode($profile_pic));
                        }

                    }
                }
            } else{
                    //this id doesn't exist
                    $response['error'] = true;
                    $response['message'] = 'Invalid ID';
            }

            break;

        //if clicked on something else
        default:
            $response['error'] = true;
            $response['message'] = 'Invalid Operation Called';
    }
}

//if request is NOT 'apicall'
else{
    $response['error'] = true;
    $response['message'] = 'Invalid API Call';
}



//give out result in format of JSON
echo json_encode($response);

//our boolean function
function isTheseParametersAvailable($params){
    foreach($params as $paramKey){
        if(!isset($_POST[$paramKey])){
            return false;
        }
    }
    return true;
}
?>