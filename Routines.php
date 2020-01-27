<?php

//our boolean function
function areTheseParamsAvailable($params){
    foreach ($params as $paramKey) {
        if (!isset($_POST[$paramKey])) {
            return false;
        }
    }
    return true;
}

function addUser($name){
    global $stmt;

    if (
        $stmt &&
        $stmt->bind_param("s", $name) &&
        $stmt->execute()
    ){
        $msg = "user added";
    } else{
        $msg = "fuck u looser!";
    };

//    echo $msg ." -> " .$name. "<br>";
    $response['message'] = $msg ." -> " .$name;

    return $msg;
}
function addUser_old($conn){
    $sql =" INSERT INTO users( name ) VALUES ('".$_REQUEST["name"]."')";
    if (mysqli_query($conn, $sql)){
        $msg = "user added";
    }else{
        $msg = "fuck u looser!";
    }
}

//function getUsersCount($conn, $table){
//    $result = mysqli_query($conn, "SELECT count(*) FROM $table");
//    $row = mysqli_fetch_array($result);
//
//    return  $row[0];
//}
function getUsersCount1($conn, $table)
{
    $result = mysqli_query($conn, "SELECT count(*) AS total_count FROM $table") or exit(mysqli_error());
    $row = mysqli_fetch_object($result);
//    return $row -> total_count;
    return $row ;
}
//function getUsersCount2($conn, $table)
//{
//    $result = mysqli_query($conn, "SELECT * FROM $table");
//    $row = mysqli_num_rows($result);
//    return $row;
//}
//function getUsersCount($conn){
//    $num_result = mysqli_query($conn, "SELECT count(*) AS total_count FROM users;") or exit(mysqli_error());
//    $usersCount = mysqli_fetch_object($num_result);
//    return $usersCount;
//}

function getUser($conn, $id){
    $stmt = $conn -> prepare("SELECT id, name FROM users WHERE id = $id");
    $stmt -> execute();

    $result = $stmt -> get_result();
    $row = $result -> fetch_assoc();

    printf("id. %s  = %s \n", $row['id'], $row['name'] );

    $stmt -> close();
}
function getAllUsers($conn){
    $stmt = $conn -> prepare("SELECT id, name FROM users ");
    $stmt -> execute();

    $result = $stmt -> get_result();
    while ($row = $result -> fetch_assoc() ){
        printf("id. %s  = %s  %s", $row['id'], $row['name'], "<br>" );
    }

    $stmt -> close();
}
function getAllUsersArray($conn){
    $users = array();
    $new  = array();

    $stmt = $conn -> prepare("SELECT id, name FROM users ");
    $stmt -> execute();

    $result = $stmt -> get_result();
    while ($row = $result -> fetch_assoc() ){
//        printf("id. %s  = %s  %s", $row['id'], $row['name'], "<br>" );
        $id = $row['id'];
        $name = $row['name'];
        $new = array_push($users, ['id' => $id , 'name' => $name]);
    }
//    print_r($users);
//    echo json_encode($users);
//    echo $new."\n";
    $stmt -> close();

    return $users;
}



