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
//ProductAggregate is what the server returns to the web page. Information from an array of these objects is what enables the 
//creation of the list of products on the viewProducts.jsp page
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


var tableSortOrder = 1;
//Loading the list of products when the page is finished loading. The products will initially be sorted by Product Name
$( document ).ready(function()
	{
	populateTable("", "", 'A');
	});
	
//populateTable takes as input the Strings manufacturer, product, and sortLetter
//populateTable passes these parameters to the ListProducts Servlet, which returns an array of ProductAggregate objects
//the contents of this array are then used to populate a table, which generates the list of products seen on this page.
function populateTable(manufacturer, product, sortLetter)
{
	var sortCriteria = sortLetter + tableSortOrder.toString();
	tableSortOrder = tableSortOrder * -1;
	
	$.ajax({
		url: 'ListProducts',
		type: 'POST',
		data: {
			'manufacturer': manufacturer,
			'product': product,
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
			cell1 = row.insertCell(i % 4);	
			cellInnerHTMLString = "<div class = 'ProductFormatBox'><div class = 'boxTitle'>" + productArray[i].ProductName + "</div><img style = 'margin-left: 3vw; margin-right: 3vw;' src = 'images/" + productArray[i].Manufacturer + "_" + productArray[i].ProductName + ".jpg' alt = 'Product Image Not Available' height = '200px' width = '200px'><div class = 'boxManufacturer'>Made by " + productArray[i].Manufacturer + "</div>"
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

//populateTableHelper is the helper method that the submit button for the product search feature calls, which collects
//the manufacturer name and product name strings that the user has requested and passes these on to the function populateTable.
function populateTableHelper()
{
	var manufacturerName = $("#manufacturerText").val();
	var productName = $("#productNameText").val();
	populateTable(manufacturerName, productName, 'A');
}

</script>
<meta charset="UTF-8">
<!-- This page provides a list of all the products in the Little Center, with a list of how many of each product is currently available,
how many total products of that type the Little Center has, the Product Name and Manufacturer name of the product, and a picture of the product.
Additionally, there is a search bar where the user can search for products that contain a specific String within their product name or manufacturer name -->
<title>Inventory View</title>
</head>
<body style = "background-color: LightYellow;">
<header>
<!-- This section contains the top navigation bar for the website, which contains links to all the other pages of the website. -->
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
<h5>Type in a product name or manufacturer name to narrow your search for products.</h5>
<form>
Manufacturer Name: <input type = "text" id = "manufacturerText" style = "margin-right: 3vw;">
Product Name: <input type = "text" id = "productNameText"><br>
<input type = "button" onclick = "populateTableHelper()" value = "Search"><br>
</form>
<table style = "border-style: none;">
<tbody id = "tableStart">
</tbody>
</table>
<div class = "footer">Clark University | Little Center Theater Equipment Rentals | &#169; 2019</div>
</body>
</html>
