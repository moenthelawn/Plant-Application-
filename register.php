
<?php
if ($_SERVER['REQUEST_METHOD'] == 'POST'){
    $id = $_POST['id']
    
    $email = $_POST['email']
    $password = $_POST['password']
    $password = password_hash($password,PASSWORD_DEFAULT); 
    require_once 'connect.php'; 
    $sql = "INSERT INTO login(id,email,password) VALUES('$id','$email','$password')"; 
    if (mysqli_query($conn,$sql)){
        $result["success"] = "1"; 
        $result["message"] = "success"; 
        echo json_encode($result); 
        mysqli_close($conn); 
    }else{
        $result["success"] = "0"; 
        $result["message"] = "error"; 
        echo json_encode($result); 
        mysqli_close($conn); 
    }
}

?>
