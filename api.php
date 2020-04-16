
<?php
 
// Create connection
$con=mysqli_connect("85.13.145.19","d031be59","Fami03bb!!","d031be59");
mysqli_set_charset($con, "utf8");
 
// Check connection
if (mysqli_connect_errno())
{
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

//get all tablenames
$sql1 = "SHOW TABLES FROM d031be59;";
$sth = mysqli_query($con,$sql1);
$tableNames = array();
while($r = mysqli_fetch_assoc($sth)) {
    $tableNames[] = $r;
}
//print_r($tableNames);
//all tablenames to string
$string="";/*
for ($i=0; $i<sizeof($rows1); $i++){
	$string.=$rows[$i]["Tables_in_d031be59"];
	if($i!=sizeof($rows)-1){
	$string.=",";}	
}*/
$wrapper = array();
$objects = array();

for ($i=0; $i<sizeof($tableNames); $i++){
	$tableName=$tableNames[$i]["Tables_in_d031be59"];
	$sql2 = "SELECT * FROM $tableName";
	$sth = mysqli_query($con,$sql2);
	$rows = array();
	while($r = mysqli_fetch_assoc($sth)) {
	
    	$rows[] = $r;
	}
	

	$objects[$tableName]=$rows;
	
	//$wrapper[]=$objects;
}
echo json_encode( $objects, JSON_UNESCAPED_UNICODE );

//echo $string;
//get content
/*
$sql2 = "SELECT * FROM `Extra Tour Birkenwerder`";
$sth = mysqli_query($con,$sql2);
$rows = array();
while($r = mysqli_fetch_assoc($sth)) {
	print_r($r);
    //$rows[] = $r;
}
echo json_encode( $rows, JSON_UNESCAPED_UNICODE );
*/
//$sql = "SELECT * FROM INFORMATION_SCHEMA.TABLES";

// Close connections
mysqli_close($con);
?>