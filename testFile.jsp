<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
 <script type = "text/javascript" src = "http://code.jquery.com/jquery-1.10.0.min.js"></script>
 <script>

 class Product 
 {
	 constructor(QR_Code, manufacturerName, productName, checkoutDate, checkinDate)
	 {
		 this.QR_Code = QR_Code;
		 this.manufacturerName = manufacturerName;
		 this.productName = productName;
		 this.checkoutDate = checkoutDate;
		 this.checkinDate = checkinDate;
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
 
 
 var publicTableOrder = 1;
 var adminTableOrder = -1;
 var userPassword = "";
 var productArray = [];
 
 function databaseLogin()
 {
	 userPassword = $("#login").val();
	 
	 $.ajax({
		url: 'PasswordCheck',
		type: 'POST',
		data: {
			'password': userPassword
		},
		success: function(data)
		{
			if(Number(data) == 1)
				{
				document.getElementById("loginConfirmation").innerHTML = "Login Successfull!";
				}
			else
				document.getElementById("loginConfirmation").innerHTML = "Incorrect Password - Please try again.";	
		},
		
		failure: function(data)
		{
			alert('Failed to connect to servlet');
		}
		 
		 
	 });
	 
 }
function databaseAdd()
{
	var myManufacturer = $("#manufacturerAdd").val();
	var myProduct = $("#productAdd").val();
	
	 $.ajax({
         url: 'AddProduct',
         type: 'POST',
         data: {
             'manufacturer': myManufacturer,
             'productName': myProduct,
             'password': userPassword,
          
         },
         success: function(data) {
        	 if(Number(data) == 0)
         	alert('Success');
        	 else
        		 alert('Invalid password used. Please login again');
         },
         failure: function(data) {
             alert('Update Failed');
            
         }
     });	
	
}

function databaseDelete()
{
var QR_Code = $("#QR_Delete").val();
var returnValue = 5;

$.ajax ({
	url: 'DeleteProduct',
	type: 'POST',
	data: {
		'QR_Code': QR_Code,
		'password': userPassword,
		
	},
	success: function(data)
	{
		returnValue = Number(data);
		
		if(returnValue == 0)
			alert('Product succesfully deleted');
		else if(returnValue == 1)
			alert('Invalid QR Code');
		else
			alert('Invalid password. Please login again');
	},
	failure: function(data)
	{
		alert('Failed to connect to servlet');
		
	}
	
	
});

	
}


function databaseCheckout()
{
	var QR_Code = $("#QR_Checkout").val();
	var StudentNumber = $("#Student_Checkout").val();
	var StudentName = $("#Student_Name").val();
	var OrganizationName = $("#Organization_Name").val();
	var Email = $("#Email").val();
	var returnValue = 5;
	
	 $.ajax({
         url: 'CheckoutProduct',
         type: 'POST',
         data: {
             'QR_Code': QR_Code,
             'StudentNumber': StudentNumber,
             'StudentName': StudentName,
             'OrganizationName': OrganizationName,
             'Email': Email,
             'password': userPassword,
          
         },
         success: function(data) 
         {
       returnValue = Number(data);
         if(returnValue == 0)
        	 alert('Product succesfully checked out');
         else if(returnValue == 1)
        	 alert('Invalid QR Code');
         else if(returnValue == 2)
        	 alert('Product already checked out by somebody else');
         else
        	 alert('Invalid password. Please log in with the correct password before trying to check out a piece of equipment');
         	 
         },
         failure: function(data) {
             alert('Failed to connect to servlet');
            
         }
     });		
}

function databaseCheckin()
{
	var QR_Code = $("#QR_Checkin").val();
	var returnValue = 5;
	
	 $.ajax({
         url: 'CheckinProduct',
         type: 'POST',
         data: {
             'QR_Code': QR_Code,
             'password': userPassword,
          
         },
         
         success: function(data) 
         {
       returnValue = Number(data);
         if(returnValue == 0)
        	 alert('Product succesfully checked back in');
         else if(returnValue == 1)
        	 alert('Invalid QR Code');
         
         else if(returnValue == 2)
         {
        	 alert('Product was not checked out to begin with');
         }
         else
        	 alert('Invalid password used. Please log in again');
        	 
        	 
         },
         failure: function(data) {
             alert('Failed to connect to servlet');
            
         }
     });	
	
	
	
}

function databaseUpdate()
{
	var QR_Code = $("#QR_Update").val();
	var manufacturer = $("#manufacturerUpdate").val();
	var product = $("#productUpdate").val();
	var returnValue = 5;
	
	 $.ajax({
         url: 'UpdateProduct',
         type: 'POST',
         data: {
             'QR_Code': QR_Code,
             'manufacturer': manufacturer,
             'product': product,
             'password': userPassword,
          
         },
         
         success: function(data) 
         {
       returnValue = Number(data);
         if(returnValue == 0)
        	 alert('Product succesfully updated');
        
         else if(returnValue == 1)
         {
        	 alert('Invalid QR Code');
         }
         else
        	 alert('Invalid password used. Please login again');
        	 
        	 
         },
         failure: function(data) {
             alert('Failed to connect to servlet');
            
         }
     });	
	
}

function passwordUpdate()
{
var oldPassword = $("#originalPassword").val();
var newPassword = $("#newPassword").val();

$.ajax({
	url: 'ChangePassword',
	type: 'POST',
	data: {
		'oldPassword': oldPassword,
		'newPassword': newPassword,
	},
	
	success: function(data)
	{
		if(Number(data) == 0)
			alert('Password succesfully changed to ' + newPassword + " Be sure to login with the new password to continue using the system");
		else
			alert('The password typed in for the original password was wrong. Please try again.');
	},
	failure: function(data){
		alert('Failed to connect to servlet');
	
	}
	
	
});
	
	
}
function DataLoad(manufacturer, product, filterOn, sortOrder)
{
	var finalSort = sortOrder + publicTableOrder.toString();
	publicTableOrder = publicTableOrder * -1;
	
	$.ajax({
		url: 'ListProducts',
		type: 'POST',
		data: {
			'manufacturer': manufacturer,
			'product': product,
			'filterOn': filterOn,
			'sortOrder': finalSort,
		},
		success: function(data)
		{
			productArray = JSON.parse(data);
			var listLength = productArray.length;
			var TableBody = document.getElementById("TableBody");
			TableBody.innerHTML = "";
			
			for(var index = 0; index < listLength; ++index)
			{
			
				var row = TableBody.insertRow();
				var cell1 = row.insertCell(0);
				var cell2 = row.insertCell(1);
				var cell3 = row.insertCell(2);
				var cell4 = row.insertCell(3);		
				var cell5 = row.insertCell(4);
				
				cell1.innerHTML = productArray[index].QR_Code;
				cell2.innerHTML = productArray[index].ManufacturerName;
				cell3.innerHTML = productArray[index].ProductName;
				if(productArray[index].checkoutDate == "N/A")
					cell4.innerHTML = "True";
				else
					cell4.innerHTML = "False";
				
				
				cell5.innerHTML = productArray[index].checkoutDate;
			}
			
			
		},
		failure: function(data)
		{
			alert('Failed to connect to servlet');
		}
		
		
		});
		

}


function filterData(sortCriteria)
{
var manufactureString = $("#manufactureSearch").val();
var productString = $("#productSearch").val();
var filterOn = true;

if(document.getElementById("All").checked == true)
	filterOn = false;
	
	DataLoad(manufactureString, productString, filterOn, sortCriteria);
}

$(document).ready(function(){
	
	DataLoad("", "", "false", 'A');
});

function adminDataLoad(sortCriteria)
{
	var finalSort = sortCriteria + adminTableOrder.toString();
	adminTableOrder = adminTableOrder * -1;
	var manufactureString = $("#adminManufacturer").val();
	var productString = $("#adminProduct").val();
	var studentName = $("#adminStudentName").val();
	var studentNumber = $("#adminStudentNumber").val();
	var organizationName = $("#adminOrganization").val();
	var email = $("#adminEmail").val();
	var productNumber = $("#adminProductNumber").val();
	var transactionNumber = $("#adminTransactionNumber").val();
	var filterStatus = 2;
	
	if(document.getElementById("adminAll").checked == true)
		filterStatus = 0;
	else if(document.getElementById("adminCompleted").checked == true)
		filterStatus = 1;
	
	$.ajax({
		url: 'AdministratorDataView',
		type: 'POST',
		data:
		{
		'TransactionNumber': transactionNumber,
		'ProductNumber': productNumber,
		'manufacturer': manufactureString,
		'product': productString,
		'studentName' : studentName,
		'studentNumber': studentNumber,
		'organizationName' : organizationName,
		'email' : email,
		'filterStatus': filterStatus,
		'password': userPassword,
		'sortOrder': finalSort,
		},
		
		success: function(data)
		{
			if(String(data).trim().valueOf() == "2")
			{
			alert('Invalid login credentials. Please login again.');
			return;
			}

			
			var adminArray = JSON.parse(data);
			var listLength = adminArray.length;
			var TableBody = document.getElementById("adminTableBody");
			TableBody.innerHTML = "";
			
			for(var index = 0; index < listLength; ++index)
			{
			
				var row = TableBody.insertRow();
				var cell1 = row.insertCell(0);
				var cell2 = row.insertCell(1);
				var cell3 = row.insertCell(2);
				var cell4 = row.insertCell(3);		
				var cell5 = row.insertCell(4);
				var cell6 = row.insertCell(5);
				var cell7 = row.insertCell(6);
				var cell8 = row.insertCell(7);
				var cell9 = row.insertCell(8);
				var cell10 = row.insertCell(9);
				
				cell1.innerHTML = adminArray[index].transactionNumber;
				cell2.innerHTML = adminArray[index].QR_Code;
				cell3.innerHTML = adminArray[index].manufacturerName;
				cell4.innerHTML = adminArray[index].productName;
				
				
				cell5.innerHTML = adminArray[index].studentName;
				cell6.innerHTML = adminArray[index].studentNumber;
				cell7.innerHTML = adminArray[index].studentEmail;
				cell8.innerHTML = adminArray[index].organizationName;
				cell9.innerHTML = adminArray[index].checkoutDate;
				cell10.innerHTML = adminArray[index].checkinDate;
			}
			
			
		},
		
		failure: function(data)
		{
			alert('Failed to Connect to Servlet');
		}
		
	});
	
}

</script>
<style>
/* sidebar navigation */
 ul {
	list-style-type: none;
	margin: 0;
	padding: 0;
	width: 200px;
	background-color: white;
	height: 100%;
	position: fixed;
	overflow: auto;
 }

 /* buttons on side bar */
 li a {
	display: block;
	color: black;
	padding: 8px 16px;
	text-decoration: none;
 }
 
 /* sidebar active button */
 li a.active {
	background-color: crimson;
	color: white;
 }

 /* sidebar hover over non-active button */
 li a:hover:not(.active) {
	background-color: dimgrey;
	color: white;
 }

 body{
 	background-color: #f0f0f0;
 	
 }

 h1, h2, h3, h4, h5{
 	color: Tomato;
 }
 

</style>


<meta charset="UTF-8">
<title>Little Center Inventory System</title>
</head>
<body>

<ul>
 <li><a href="#admin">Admin</a></li>
 <li><a href="#checkinout">Check In/Out</a>
 <li><a href="#equipment">Equipment</a></li>
</ul>

<div id = "admin"></div>

<h3>Log in here to add or remove products from the database or to check products in and out: </h3>
<form>
Site Password: <input type = "text" id = "login"><br>
<input id = "submitButton" type = "button" onclick = "databaseLogin()" value = "Submit">
</form>
<br><br>

<div id = "loginConfirmation"></div>

<h3>Change the password for logging in here: </h3>
<form>
Current Password: <input type = "text" id = "originalPassword"><br>
New Password: <input type = "text" id = "newPassword"><br>
<input id = "submitButton" type = "button" onclick = "passwordUpdate()" value = "Submit">
</form>
<br><br>

<h3>Add Products Here: </h3>
<form>
Manufacturer: <input type = "text" id = "manufacturerAdd"><br>
Product: <input type = "text" id = "productAdd"><br>
<input id = "button1" type = "button" onclick = "databaseAdd()" value = "Submit">
</form>
<br><br>

<h3>Delete Products from the Database Here (Administrators Only)</h3>
<form>
QR Code: <input type = "text" id = "QR_Delete"><br>
<input id = "button7" type = "button" onclick = "databaseDelete()" value = "Submit">
</form>
<br><br>

<div id = "checkinout"></div>

<h3>Checkout Products Here: </h3>
<form>
QR Code: <input type = "text" id = "QR_Checkout"><br>
Student Number: <input type = "text" id = "Student_Checkout"><br>
Student Name: <input type = "text" id = "Student_Name"><br>
Organization Name (optional): <input type = "text" id = "Organization_Name"><br>
Email (optional): <input type = "text" id = "Email"><br>
<input id = "button2" type = "button" onclick = "databaseCheckout()" value = "Submit">
</form>
<br>
<br>
<h3>Checkin Products Here: </h3>
<form>
QR Code: <input type = "text" id = "QR_Checkin"><br>
<input id = "button3" type = "button" onclick = "databaseCheckin()" value = "Submit">
</form>
<br><br>
<h3>Update Product Information Here: </h3>
<form>
QR Code: <input type = "text" id = "QR_Update"><br>
Manufacturer: <input type = "text" id = "manufacturerUpdate"><br>
Product: <input type = "text" id = "productUpdate"><br>
<input id = "button3" type = "button" onclick = "databaseUpdate()" value = "Submit">
</form>
<br><br>

<div id = "Equipment"></div>

<h3>Here is a view of the products for students and non-administrators: </h3>
<h5>Use the search feature below to fine-tune your search for products</h5>

<form>
Manufacturer: <input type = "text" id = "manufactureSearch"><br>
Product: <input type = "text" id = "productSearch"><br>
Show All Matching Products <input type = "radio" name = "FilterStatus" id = "All" value = "All" checked = "checked">
<br>
Show Only Available Products <input type = "radio" name = "FilterStatus" value = "Filtered"><br>
<input id = "button7" type = "button" onclick = "filterData('A')" value = "Submit">
</form>
<br>
<table border = "1">
<thead>
<tr>
<th onclick = "filterData('A')" style = "cursor: pointer; padding-left: 1vw; padding-right: 1vw;">ID Code:   </th>
<th onclick = "filterData('B')" style = "cursor: pointer; padding-left: 1vw; padding-right: 1vw;">Manufacturer:   </th>
<th onclick = "filterData('C')" style = "cursor: pointer; padding-left: 1vw; padding-right: 1vw;">Product Name:   </th>
<th onclick = "filterData('D')" style = "cursor: pointer; padding-left: 1vw; padding-right: 1vw;">Available?   </th>
<th onclick = "filterData('E')" style = "cursor: pointer; padding-left: 1vw; padding-right: 1vw;">Date Rented by Student:   </th>
</tr>
</thead>
<tbody id = "TableBody">
</tbody>
</table>
<br><br>

<h3>Here is a view of the checkout records for all the products (for administrators only)</h3>
<h5>Use the search feature to fine-tune your search</h5>
<form>
Transaction Number: <input type = "text" id = "adminTransactionNumber"><br>
Product Number: <input type = "text" id = "adminProductNumber"><br>
Manufacturer: <input type = "text" id = "adminManufacturer"><br>
Product: <input type = "text" id = "adminProduct"><br>
Student Name: <input type = "text" id = "adminStudentName"><br>
Student Number: <input type = "text" id = "adminStudentNumber"><br>
Organization Name: <input type = "text" id = "adminOrganization"><br>
Email Address: <input type = "text" id = "adminEmail"><br>
Show All Transactions: <input type = "radio" name = "filterStatus" id = "adminAll" checked = "checked">
<br>
Show Only Completed Transactions <input type = "radio" name = "filterStatus" id = "adminCompleted">
<br>
Show Only Transactions That Are Ongoing (where the product is still checked out): <input type = "radio" name = "filterStatus" id = "adminOngoing">
<br>

<input id = "button8" type = "button" onclick = "adminDataLoad('A')" value = "Submit">
</form>
<br><br>

<table border = "1">
<thead>
<tr>
<th onclick = "adminDataLoad('A')" style = "cursor: pointer; padding-left: 1vw; padding-right: 1vw;">Transaction Number   </th>
<th onclick = "adminDataLoad('B')" style = "cursor: pointer; padding-left: 1vw; padding-right: 1vw;">Product Number:   </th>
<th onclick = "adminDataLoad('C')" style = "cursor: pointer; padding-left: 1vw; padding-right: 1vw;">Manufacturer Name</th>
<th onclick = "adminDataLoad('D')" style = "cursor: pointer; padding-left: 1vw; padding-right: 1vw;">Product Name </th>
<th onclick = "adminDataLoad('E')" style = "cursor: pointer; padding-left: 1vw; padding-right: 1vw;">Student Name  </th>
<th onclick = "adminDataLoad('F')" style = "cursor: pointer; padding-left: 1vw; padding-right: 1vw;">Student Number  </th>
<th onclick = "adminDataLoad('G')" style = "cursor: pointer; padding-left: 1vw; padding-right: 1vw;">Email Address </th>
<th onclick = "adminDataLoad('H')" style = "cursor: pointer; padding-left: 1vw; padding-right: 1vw;">Organization Name </th>
<th onclick = "adminDataLoad('I')" style = "cursor: pointer; padding-left: 1vw; padding-right: 1vw;">Checkout Date </th>
<th onclick = "adminDataLoad('J')" style = "cursor: pointer; padding-left: 1vw; padding-right: 1vw;">Checkin Date  </th>
</tr>
</thead>
<tbody id = "adminTableBody">
</tbody>
</table>


</body>
</html>