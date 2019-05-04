<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href = "NavigationBar.css" rel = "stylesheet" type = "text/css">
<style>
.headerBanner{min-width: 100vw; height: 3vw; position: absolute; top: 0vw; left: 0vw; background-color: red;}
.headerLink{margin-right: 4vw; background-color: red; position: relative; padding-left: 1vw; padding-right: 1vw; padding-top: 0.10vw; padding-bottom: 0.1vw; display: inline-block; font-size: 2.40vw; text-align: center;}
.headerLink:hover{background-color: orange;}
.linkFormat{color: white; text-decoration: none;}
.headingFormat{position: relative; top: 2vw; left: 45vw; color: black; font-family: Heveltica; size: 2vw;}
.footer{text-align: center; color: navy; font-family: "Times New Roman"; margin-top: 1.3vw;}
p{font-family: Heveltica}
body{margin: 0;}
</style>
<!-- This acts as the home page for the website -->
<title>Welcome</title>
</head>
<body style = "background-color: Ivory;">
<header>
<!-- This section contains the main navigation bar for the website, with links to the other pages of the website here -->
<div class = "w3-bar w3-red" style = "margin-bottom: 0vw;">
	<a href="userWelcomePage.jsp" class="w3-bar-item w3-button w3-mobile w3-hover-orange w3-hover-text-white" style = "font-size: 2.3vw; margin-right: 4vw; padding-top: 0.2vw; padding-bottom: 0.2vw;">Home</a>
	<a href = "login.jsp" class = "w3-bar-item w3-button w3-mobile w3-hover-orange w3-hover-text-white" style = "font-size: 2.3vw; margin-right: 4vw; padding-top: 0.2vw; padding-bottom: 0.2vw;">Login</a>
	<a href = "viewProducts.jsp" class = "w3-bar-item w3-button w3-mobile w3-hover-orange w3-hover-text-white" style = "font-size: 2.3vw; margin-right: 4vw; padding-top: 0.2vw; padding-bottom: 0.2vw;">View Products</a>
</div>
</header>
<h1 style = "margin-top: 0.4vw; margin-left: 0.2vw; margin-bottom: 0.1vw; padding: 0; text-align: center;">Welcome!</h1>
<br><p>Welcome to the Little Center Inventory Page! Using this website, you can check what equipment is available. Additionally, administrators can login to this
site in order to check products in, check products out, and perform other administrative functions</p>
<p>In order to view the status of the equipment in the Little Center, click on the View Products link. To login to the site to perform administrative functions,
please click on the Login tab</p>
<img style = "margin-left: 40vw; max-height: 30vw; max-width: 25vw;" src = "images/Clark_Theater_Picture.jpg" alt = "Main picture">
<div class = "footer">Clark University | Little Center Theater Equipment Rentals | &#169; 2019</div>
</body>
</html>
