<?php
$conn = mysqli_connect("plantdbinstance.cgmktlzdibsz.us-west-2.rds.amazonaws.com","root","","test"); 
if ($conn){
    echo "Connection Success" 
}
else{
    echo "Connection not a success 
}
?>