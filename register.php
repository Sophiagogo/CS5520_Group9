<?php
$con = mysqli_connect("localhost", "id19749080_user_db", "Fb309747486.0", "id19749080_user");

if(isset($_POST["name"])){
    $name = $_POST["name"];
}
if(isset($_POST["username"])){
    $name = $_POST["username"];
}
if(isset($_POST["password"])){
    $name = $_POST["password"];
}

$statement = mysqli_prepare($con, "INSERT INTO user (name, username, password) VALUES (?, ?, ?)");
mysqli_stmt_bind_param($statement, "sss",  $name, $username, $password);
mysqli_stmt_execute($statement);

$response = array();
$response["success"] = true;

echo json_encode($response);
?>
