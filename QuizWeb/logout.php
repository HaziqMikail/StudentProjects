<?php
session_start();
session_destroy();
header("Location: login/main-login.php");
exit();
?>
