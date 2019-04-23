<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
.headerBanner{min-width: 100vw; height: 3vw; position: absolute; top: 0vw; left: 0vw; background-color: red;}
.headerLink{margin-right: 4vw; background-color: red; padding-left: 1vw; padding-right: 1vw; padding-top: 0.25vw; padding-bottom: 0.1vw; display: inline-block; font-size: 2.30vw; text-align: center;}
.headerLink:hover{background-color: orange;}
.linkFormat{color: white; text-decoration: none;}
.headingFormat{position: relative; top: 2vw; left: 45vw; color: black; font-family: Heveltica; size: 2vw;}
.footer{margin-left: 30vw; color: navy; font-family: "Times New Roman";}


.boxTitle{font-family: "Heveltica"; font-size: 1.6vw; text-weight: bold; text-align: center; margin-bottom: 1vw; margin-top: 1.5vw;}
.ProductFormatBox{min-width: 18vw; min-height: 18vw; background-color: #EEEEEE; color: #0769AD; border-radius: 1vw; margin-left: 2vw; margin-bottom: 2vw;}
.boxManufacturer{font-family: "Heveltica"; font-size: 1.6vw; text-align: center; color: green; margin-bottom: 2vw;}
.numInStock{font-family: "Heveltica"; font-size: 1.6vw; text-align: center; color: green; margin-bottom: 0.3vw;}
.numAvailable{font-family: "serif"; font-size: 1.6vw; text-align: center; color: red; margin-bottom: 1vw;} 
 

</style>
 <script type = "text/javascript" src = "http://code.jquery.com/jquery-1.10.0.min.js"></script>
<script>

class ProductAggregate
{
	 constructor(ProductName, Manufacturer, numAvailable, numInStock)
	 {
		 this.ProductName = ProductName;
		 this.Manufacturer = Manufacturer;
		 this.numAvailable = numAvailable;
		 this.numInStock = numInStock;
	 } 
}

class Transaction
{
	 constructor(transactionNumber, QR_Code, manufacturerName, productName, studentNumber, studentName, studentEmail, organizationName, checkoutDate, checkinDate)
	 {
		 this.transactionNumber = transactionNumber;
		 this.QR_Code = QR_Code;
		 this.manufacturerName = manufacturerName;
		 this.productName = productName;
		 this.studentNumber = studentNumber;
		 this.studentName = studentName;
		 this.studentEmail = studentEmail;
		 this.organizationName = organizationName;
		 this.checkoutDate = checkoutDate;
		 this.checkinDate = checkinDate;
	 }
	  
}

var tableSortOrder = 1;
$( document ).ready(function()
	{
	populateTable("", "", false, 'A');
	});
	
function populateTable(manufacturer, product, filterOn, sortLetter)
{
	var sortCriteria = sortLetter + tableSortOrder.toString();
	tableSortOrder = tableSortOrder * -1;
	
	$.ajax({
		url: 'ListProducts',
		type: 'POST',
		data: {
			'manufacturer': manufacturer,
			'product': product,
			'filterOn': filterOn,
			'sortOrder': sortCriteria,
		},
		success: function(data)
		{
			var productArray = JSON.parse(data);
			var listLength = productArray.length;
			var TableBody = document.getElementById("tableStart");
			var cellInnerHTMLString = "";
			TableBody.innerHTML = "";
			var tableBodyHTMLString = "<tr>";
			var row, cell1;
			for(var i = 0; i < listLength; ++i)
			{
			if(i % 4 == 0)
			row = TableBody.insertRow();
			cell1 = row.insertCell(0);	
			cellInnerHTMLString = "<div class = 'ProductFormatBox'><div class = 'boxTitle'>" + productArray[i].ProductName + "</div><img style = 'margin-left: 4.5vw;' src = 'images/Camera.jpg' alt = 'Product Image' max-height = '50%' width = '50%'><div class = 'boxManufacturer'>Made by " + productArray[i].Manufacturer + "</div>"
			+ "<div class = 'numInStock'>Number in Inventory: " + productArray[i].numInStock;
			if(productArray[i].numAvailable == 0)
				cellInnerHTMLString += "<div class = 'numAvailable' style = 'color: red'>";
			else
				cellInnerHTMLString += "<div class = 'numAvailable' style = 'color: green'>";
			
			cellInnerHTMLString += ("Number Available: " + productArray[i].numAvailable + "</div></div>");
			cell1.innerHTML = cellInnerHTMLString;
			cellInnerHTMLString = "";	
			}
			
		},
		failure: function(data)
		{
			alert("Failed to connect to servlet");
		}
});
	
}
</script>
<meta charset="UTF-8">
<title>Inventory View</title>
</head>
<body style = "background-color: LightYellow;">
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
<h1 class = "headingFormat">View Products: </h1>
<table style = "border-style: none;">
<tbody id = "tableStart">
</tbody>
</table>
<div class = "footer">Clark University | Little Center Theater Equipment Rentals | &#169; 2019</div>
</body>
</html>
