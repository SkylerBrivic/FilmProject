<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
.headerBanner{min-width: 100vw; height: 3vw; position: absolute; top: 0vw; left: 0vw; background-color: red;}
.headerLink{margin-right: 4vw; background-color: red; position: relative; padding-left: 1vw; padding-right: 1vw; padding-top: 0.10vw; padding-bottom: 0.1vw; display: inline-block; font-size: 2.40vw; text-align: center;}
.headerLink:hover{background-color: orange;}
.linkFormat{color: white; text-decoration: none;}
.headingFormat{position: relative; top: 2vw; left: 45vw; color: black; font-family: Heveltica; size: 2vw;}
.footer{margin-left: 30vw; position: fixed; bottom: 0vw; color: navy; font-family: "Times New Roman";}
p{font-family: Heveltica}
</style>
<title>Welcome</title>
</head>
<body style = "background-color: Ivory; margin: 1vw;">
<header>
<div class = "headerBanner">
		<div class = "headerLink">
			<a class = "linkFormat" href = "userWelcomePage.jsp">Home</a>
		</div>
		<div class = "headerLink">
			<a class = "linkFormat" href = "login.jsp">Login</a>
		</div>
		<div class = "headerLink">
			<a class = "linkFormat" href = "viewProducts.jsp">View Products</a>
		</div>
</div>
</header>
<h1 class = "headingFormat">Welcome!</h1>
<br><p>Welcome to the Little Center Inventory Page! Using this website, you can check what equipment is available. Additionally, administrators can login to this
site in order to check products in, check products out, and perform other administrative functions</p>
<p>In order to view the status of the equipement in the Little Center, click on the View Products link. To login to the site to perform administrative functions,
please click on the Login tab</p>
<img style = "margin-left: 40vw; max-height: 30vw; max-width: 25vw;" src = "images/Clark_Theater_Picture.jpg" alt = "Main picture">
<div class = "footer">Clark University | Little Center Theater Equipment Rentals | &#169; 2019</div>
</body>
</html>
