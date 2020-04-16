<?php

$con=mysqli_connect("85.13.145.19","d031f64c","Fami03bb!!","d031f64c");
mysqli_set_charset($con, "utf8");
 
// Check connection
if (mysqli_connect_errno())
{
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
else{
    //echo "successful";
}

$name=$_POST["Name"];

$tour=$_POST["Tour"];
$date=$_POST["Date"];

$sql="INSERT INTO Benachrichtigungen(`Name`,Tour,Zeit) VALUES('$name','$tour','$date')";

$result=mysqli_query($con, $sql);

if($result){
    echo("Successfully Saved");
}else{
    echo("Not saved Successfully");
}
mysqli_close($con);


?>

