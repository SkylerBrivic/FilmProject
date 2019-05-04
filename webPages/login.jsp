<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- This page is used by an administrator of the website in order to perform features such as checking products out, checking products in,
viewing a list of transactions, adding products to the database, deleting products from the database, updating product information, and changing
the site's password -->
<title>Login</title>
<script type = "text/javascript" src = "http://code.jquery.com/jquery-1.10.0.min.js"></script>
<script>
//userPassword stores the last thing the user typed in when trying to enter in the site password. Initially, it is the empty String
var userPassword = "";
//adminTableOrder is either 1 or -1, with its sign indicating which direction the admin transaction list should be sorted in. Its sign is flipped
//whenever the admin transaction table is loaded
var adminTableOrder = 1;

//the Transaction class represents the characteristics of a specific transaction. An array of Transactions is what is sent to the client when an admin uses the 
//list transactions feature of the website.
class Transaction
{
	 constructor(transactionNumber, Product_ID, manufacturerName, productName, studentNumber, studentName, studentEmail, organizationName, checkoutDate, actualCheckinDate, expectedCheckinDate)
	 {
		 this.transactionNumber = transactionNumber;
		 this.Product_ID = Product_ID;
		 this.manufacturerName = manufacturerName;
		 this.productName = productName;
		 this.studentNumber = studentNumber;
		 this.studentName = studentName;
		 this.studentEmail = studentEmail;
		 this.organizationName = organizationName;
		 this.checkoutDate = checkoutDate;
		 this.actualCheckinDate = actualCheckinDate;
		 this.expectedCheckinDate = expectedCheckinDate;
	 }
	  
}


//updateStatus is a function that updates the color and message of a line of text after the client has finished a servlet call
//the resulting message can either signify success (shown in green) or failure (shown in red)
//an exitCode of 0 indicates success
//an exitCode of 1 indicates an invalid password
//an exitCode of 2 indicates that the servlet could not be connected to
//an exit code of 3 indicates that the requested operation could not be completed
//if the database query succedded, then functionMessage stores the word(s) representing how to end
//the update message (ex. for checkout products, function message would be "checked out" and the function status line would thus be updated to say
//"product succesfully checked out"). If the query failed because the requested operation could not be completed, then function message stores the error message to be displayed (ex. "Error: Product was not checked out to begin with"
//would be the value of functionMessage if the user tried to checkin a product that wasn't checked out).
function updateStatus(exitCode, functionMessage)
{
	if(exitCode == 0)
		{
			 document.getElementById("functionStatus").innerHTML = ("Product Succesfully " + functionMessage + ".");
			 document.getElementById("functionStatus").style = "display: inline; color: green;";
		}
	else if(exitCode == 1)
		{
 		 document.getElementById("functionStatus").innerHTML = "Password could not be authenticated. Please re-enter the site password above.";
		 document.getElementById("functionStatus").style = "display: inline; color: red;";
		}
	
	else if(exitCode == 2)
		{
 		 document.getElementById("functionStatus").innerHTML = "Error: Server could not connect to the database. Check for network connection issues.";
		 document.getElementById("functionStatus").style = "display: inline; color: red;";
		}
	
	else
		{
		 document.getElementById("functionStatus").innerHTML = functionMessage;
		 document.getElementById("functionStatus").style = "display: inline; color: red;";
		}
}

//databaseLogin passes what the user types in for the site password to the PasswordCheck servlet to see
//if this is really the site password. If it was correct, then the list of admin features on the page loads.
//Otherwise, an error message is displayed
function databaseLogin()
{
		 userPassword = $("#Password").val();
		 document.getElementById("Password").value = "";
		 
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
					document.getElementById("loginConfirmation").style = "display: inline; color: green;";
					document.getElementById("featureSelect").style = "display: inline";
					checkoutProductFeatureLoad();
					document.getElementById("checkoutRadio").checked = "checked";
					
					}
				else
					{
					document.getElementById("loginConfirmation").innerHTML = "Incorrect Password - Please try again.";	
					document.getElementById("loginConfirmation").style = "display: inline; color: red;"
					}
			},
			
			failure: function(data)
			{
				document.getElementById("loginConfirmation").innerHTML = "Error: Server could not connect to the database. Check for network connection issues."
				document.getElementById("loginConfirmation").style = "display: inline; color: red;";
			}
			 
			 
		 });
		 
}

//adminDataLoad takes the sortCriteria from the user (what column to sort by) and uses this to call the servlet AdministratorDataView, which returns a list
//of Transactions that match the user's search criteria. adminDataLoad then displays the contents of this list in a table.
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
			updateStatus(1, "");
			return;
			}

			document.getElementById("adminTable").style = "display: inline; background-color: white;";
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
				var cell11 = row.insertCell(10);
				
				cell1.innerHTML = adminArray[index].transactionNumber;
				cell2.innerHTML = adminArray[index].Product_ID;
				cell3.innerHTML = adminArray[index].manufacturerName;
				cell4.innerHTML = adminArray[index].productName;
				
				
				cell5.innerHTML = adminArray[index].studentName;
				cell6.innerHTML = adminArray[index].studentNumber;
				cell7.innerHTML = adminArray[index].studentEmail;
				cell8.innerHTML = adminArray[index].organizationName;
				cell9.innerHTML = adminArray[index].checkoutDate;
				cell10.innerHTML = adminArray[index].actualCheckinDate;
				cell11.innerHTML = adminArray[index].expectedCheckinDate;
			}
			
			
		},
		
		failure: function(data)
		{
			updateStatus(2, "");
		}
		
	});
	
}

//addProductDatabase calls the AddProduct servlet to add a product to the MySql database
function addProductDatabase()
{
	var myManufacturer = $("#addManufacturer").val();
	var myProduct = $("#addProductName").val();
	var quantity = $("#addQuantity").val();
	
	 $.ajax({
         url: 'AddProduct',
         type: 'POST',
         data: {
             'manufacturer': myManufacturer,
             'productName': myProduct,
             'password': userPassword,
             'quantity': quantity,
          
         },
         success: function(data) {
        	 if(Number(data) == 0)
         	{
        		updateStatus(0, "added");
         	}
        	 else if(Number(data) == 1)
        	 {
        		 updateStatus(1, "");
        	}
        	 else if(Number(data) == 3)
        		 {
        		 updateStatus(3, "Error: Both Manufacturer Name and Product Name must be filled in to add a product to the database");
        		 }
        	 else
        		 updateStatus(3, "Error: Quantity must be a whole number greater than zero.");
         },
         failure: function(data) {
            {
            	updateStatus(2, "");
            }
            
         }
     });	
	
}

//deleteProductDatabase calls the servlet DeleteProduct, which deletes the product specified by the user from the database.
function deleteProductDatabase()
{
var myProdID = $("#deleteCode").val();	
var returnValue = 5;

$.ajax ({
	url: 'DeleteProduct',
	type: 'POST',
	data: {
		'Product_ID': myProdID,
		'password': userPassword,
		
	},
	success: function(data)
	{
		returnValue = Number(data);
		
		if(returnValue == 0)
			updateStatus(0, "deleted");
		else if(returnValue == 1)
			updateStatus(3, "Error: Invalid Product ID Number.");
		else
			updateStatus(1, "");
	},
	failure: function(data)
	{
		updateStatus(2, "");	
	}
	
	
});
	
}

//the function databaseCheckout calls the servlet CheckoutProduct to check out a product for the user
function databaseCheckout()
{
	var Product_ID = $("#checkoutProductIDNumber").val();
	var StudentNumber = $("#checkoutStudentNumber").val();
	var StudentName = $("#checkoutStudentName").val();
	var OrganizationName = $("#checkoutOrganizationName").val();
	var Email = $("#checkoutEmailAddress").val();
	var expectedReturnDate = $("#checkoutReturnDate").val();
	var returnValue = 5;
	
	 $.ajax({
         url: 'CheckoutProduct',
         type: 'POST',
         data: {
             'Product_ID': Product_ID,
             'StudentNumber': StudentNumber,
             'StudentName': StudentName,
             'OrganizationName': OrganizationName,
             'Email': Email,
             'password': userPassword,
             'ExpectedReturnDate': expectedReturnDate,
          
         },
         success: function(data) 
         {
       returnValue = Number(data);
         if(returnValue == 0)
        	 updateStatus(0, "checked out");
         else if(returnValue == 1)
        	 updateStatus(3, "Error: Invalid Product ID Number entered");
         else if(returnValue == 2)
        	 updateStatus(3, "Error: Product already checked out by somebody else");
         else if(returnValue == 4)
        	 updateStatus(3, "Error: Student Name may not be left blank");
         else if(returnValue == 5)
        	 updateStatus(3, "Error: Organization Name may not be left blank");
         else if(returnValue == 6 || returnValue == 7)
        	 updateStatus(3, "Error: Expected Return Date must be filled in with yyyy-MM-dd format date (ex. 2019-06-10)");
         else
        	 updateStatus(1, "");
         	 
         },
         failure: function(data) {
             updateStatus(2, "");
            
         }
     });		
}

//the databaseCheckin() function calls the CheckinProduct servlet to check a product back in for a user
function databaseCheckin()
{
	var Product_ID = $("#checkinProductIDNumber").val();
	var returnValue = 5;
	
	 $.ajax({
         url: 'CheckinProduct',
         type: 'POST',
         data: {
             'Product_ID': Product_ID,
             'password': userPassword,
          
         },
         
         success: function(data) 
         {
       returnValue = Number(data);
         if(returnValue == 0)
        	 updateStatus(0, "checked back in");
         else if(returnValue == 1)
        	 updateStatus(3, "Error: Invalid Product ID Number was entered");
         
         else if(returnValue == 2)
         {
        	 updateStatus(3, "Error: Product was not checked out to begin with.");
         }
         else
        	 updateStatus(1, "");
        	 
        	 
         },
         failure: function(data) {
             updateStatus(2, "");
            
         }
     });	
	
	
}

//databaseUpdate() updates the information of the product in the database specified by the user via the UpdateProduct servlet
function databaseUpdate()
{
	var Product_ID = $("#updateProductIDNumber").val();
	var manufacturer = $("#updateManufacturerName").val();
	var product = $("#updateProductName").val();
	var returnValue = 5;
	
	 $.ajax({
         url: 'UpdateProduct',
         type: 'POST',
         data: {
             'Product_ID': Product_ID,
             'manufacturer': manufacturer,
             'product': product,
             'password': userPassword,
          
         },
         
         success: function(data) 
         {
       returnValue = Number(data);
         if(returnValue == 0)
        	 updateStatus(0, "updated");
        
         else if(returnValue == 1)
         {
        	updateStatus(3, "Error: Invalid Product ID Number was entered.");
         }
         else
        	updateStatus(1, "");
        	 
        	 
         },
         failure: function(data) {
            updateStatus(2, "");         
         }
     });	
	
}

//databaseChangePassword() calls the ChangePassword servlet, which will change the user's password if they typed in the site password correctly and entered in some new password.
function databaseChangePassword()
{
var oldPassword = $("#oldPassword").val();
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
			{
			document.getElementById("functionStatus").innerHTML = ("Password succesfully changed to " + newPassword + " be sure to login with the new password to continue performing administrator-only functions on the site");
			document.getElementById("functionStatus").style = "display: inline; color: green;";
			}
		else if(Number(data) == 1)
			{
			document.getElementById("functionStatus").innerHTML = "Error: The password typed in for the current site password was wrong. Please try again."
			document.getElementById("functionStatus").style = "display: inline; color: red;";
			}
		else
			{
			document.getElementById("functionStatus").innerHTML = "Error: New Password must be typed in to change password."
			document.getElementById("functionStatus").style = "display: inline; color: red;";
			}
			
	},
	failure: function(data){
		document.getElementById("functionStatus").innerHTML = "Error: Server could not connect to the database. Check for network connection issues."
		document.getElementById("functionStatus").style = "display: inline; color: red;";
	}
	
	
});
	
	
}

//the following functions are used to hide all features but one, and then load that particular feature
//(ex. checkotuProductFeatureLoad makes the checkoutFeature div visible and all the other feature tabs invisible).
//Only one feature tab can be visible at a time.
//These functions are loaded when the user clicks on one of the radio buttons that lists various admin functions, and
//the checkout feature is loaded by default when the user first logs into the website.

function checkoutProductFeatureLoad()
{
	document.getElementById("searchFeature").style = "display: none;";
	document.getElementById("adminTable").style = "display: none;";
	document.getElementById("deleteProducts").style = "display: none;";
	document.getElementById("addProducts").style = "display: none;";
	document.getElementById("updateProducts").style = "display: none;";
	document.getElementById("checkinFeature").style = "display: none;";
	document.getElementById("checkoutFeature").style = "display: inline;";
	document.getElementById("functionStatus").style = "display: none;";
	document.getElementById("changePassword").style = "display: none;";

}
function checkinProductFeatureLoad()
{
	document.getElementById("searchFeature").style = "display: none;";
	document.getElementById("adminTable").style = "display: none;";
	document.getElementById("deleteProducts").style = "display: none;";
	document.getElementById("addProducts").style = "display: none;";
	document.getElementById("updateProducts").style = "display: none;";
	document.getElementById("checkinFeature").style = "display: inline;";
	document.getElementById("checkoutFeature").style = "display: none;";
	document.getElementById("functionStatus").style = "display: none;";
	document.getElementById("changePassword").style = "display: none;";
}

function addFeatureLoad()
{
	document.getElementById("searchFeature").style = "display: none;";
	document.getElementById("adminTable").style = "display: none;";
	document.getElementById("deleteProducts").style = "display: none;";
	document.getElementById("addProducts").style = "display: inline;";
	document.getElementById("updateProducts").style = "display: none;";
	document.getElementById("checkinFeature").style = "display: none;";
	document.getElementById("checkoutFeature").style = "display: none;";
	document.getElementById("functionStatus").style = "display: none;";
	document.getElementById("changePassword").style = "display: none;";

}

function productSearchFeatureLoad()
{
	document.getElementById("searchFeature").style = "display: inline;";
	document.getElementById("adminTable").style = "display: inline;";
	document.getElementById("deleteProducts").style = "display: none;";
	document.getElementById("addProducts").style = "display: none;";
	document.getElementById("updateProducts").style = "display: none;";
	document.getElementById("checkinFeature").style = "display: none;";
	document.getElementById("checkoutFeature").style = "display: none;";
	document.getElementById("functionStatus").style = "display: none;";
	document.getElementById("changePassword").style = "display: none;";

}

function deleteFeatureLoad()
{
	document.getElementById("searchFeature").style = "display: none;";
	document.getElementById("adminTable").style = "display: none;";
	document.getElementById("deleteProducts").style = "display: inline;";
	document.getElementById("addProducts").style = "display: none;";
	document.getElementById("updateProducts").style = "display: none;";
	document.getElementById("checkinFeature").style = "display: none;";
	document.getElementById("checkoutFeature").style = "display: none;";
	document.getElementById("functionStatus").style = "display: none;";
	document.getElementById("changePassword").style = "display: none;";

}

function updateFeatureLoad()
{
	document.getElementById("searchFeature").style = "display: none;";
	document.getElementById("adminTable").style = "display: none;";
	document.getElementById("deleteProducts").style = "display: none;";
	document.getElementById("addProducts").style = "display: none;";
	document.getElementById("updateProducts").style = "display: inline;";
	document.getElementById("checkinFeature").style = "display: none;";
	document.getElementById("checkoutFeature").style = "display: none;";
	document.getElementById("functionStatus").style = "display: none;";
	document.getElementById("changePassword").style = "display: none;";

}

function changePasswordFeatureLoad()
{
	document.getElementById("searchFeature").style = "display: none;";
	document.getElementById("adminTable").style = "display: none;";
	document.getElementById("deleteProducts").style = "display: none;";
	document.getElementById("addProducts").style = "display: none;";
	document.getElementById("updateProducts").style = "display: none;";
	document.getElementById("checkinFeature").style = "display: none;";
	document.getElementById("checkoutFeature").style = "display: none;";
	document.getElementById("functionStatus").style = "display: none;";
	document.getElementById("changePassword").style = "display: inline;";
}

</script>
<style>
.headerBanner{min-width: 100vw; height: 3vw; position: absolute; top: 0vw; left: 0vw; background-color: red;}
.headerLink{margin-right: 4vw; background-color: red; padding-left: 1vw; padding-right: 1vw; padding-top: 0.25vw; padding-bottom: 0.1vw; display: inline-block; font-size: 2.30vw; text-align: center;}
.headerLink:hover{background-color: orange;}
.linkFormat{color: white; text-decoration: none;}
.headingFormat{position: relative; top: 2vw; left: 45vw; color: black; font-family: Heveltica; size: 2vw;}
.footer{margin-left: 30vw; color: navy; font-family: "Times New Roman";}
p{font-family: Heveltica;}
table, tr, th{background-color: white;}


</style>
</head>
<body style = "background-color: Ivory; font-size: 1.20vw; font-family: 'Heveltica'">
<header>
<!-- This section contains the main navigation bar for the website, with links to the other pages of the website here -->
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

<h1 style = "position: relative; top: 2vw;">Enter the Site Password Here: </h1><br>
<form>
Site Password: <input type = "password" id = "Password" style = "margin-left: 0.2vw;"><br>
<input id = "submitButton" type = "button" onclick = "databaseLogin()" value = "Submit">
</form>
<br>
<p id = "loginConfirmation" style = "display: none;"></p>

<div id = "featureSelect" style = "display: none;">
<form>
<br>
<input type = "radio" name = "featureOption" id = "checkoutRadio" onclick = "checkoutProductFeatureLoad()" checked = "checked" style = "margin-right: 0.70vw;">Checkout Product
<input type = "radio" name = "featureOption" onclick = "checkinProductFeatureLoad()" style = "margin-left: 12vw; margin-right: 0.70vw;" >Check In Product<br>
<input type = "radio" name = "featureOption" onclick = "productSearchFeatureLoad()" style = "margin-right: 0.70vw;">List All Transactions
<input type = "radio" name = "featureOption" onclick = "addFeatureLoad()" style = "margin-right: 0.70vw; margin-left: 10.6vw;">Add a Product to the Database<br>
<input type = "radio" name = "featureOption" onclick = "updateFeatureLoad()" style = "margin-right: 0.70vw;">Update Product Information
<input type = "radio" name = "featureOption" onclick = "deleteFeatureLoad()" style = "margin-right: 0.70vw; margin-left: 7.27vw;">Delete a Product from the Database<br>
<input type = "radio" name = "featureOption" onclick = "changePasswordFeatureLoad()" style = "margin-right: 0.70vw;">Change Site Password<br>
</form>
</div>

<div id = "checkoutFeature" style = "display: none;">
<h5>Enter in the product ID number of the product to be checked out and the information about the person checking the equipment out: </h5>
<form>
<span style = "color: red;">*</span> Product ID Number: <input type = "text" id = "checkoutProductIDNumber" style = "margin-left: 5.03vw;"> <br>
<span style = "color: red;">*</span> Student Name: <input type = "text" id = "checkoutStudentName" style = "margin-left: 7.66vw; margin-right: 11vw;">
<span style = "color: red;">*</span> Expected Return Date: <input type = "date" style = "margin-left: 1.5vw;" id = "checkoutReturnDate"><br>
<span style = "color: red;">*</span> Organization Name: <input type = "text" id = "checkoutOrganizationName" style = "margin-left: 5.1vw; margin-right: 11.9vw;">
Student Number: <input type = "text" id = "checkoutStudentNumber" style = "margin-left: 4.16vw;"> <br>
&nbsp;&nbsp;&nbsp;Email Address (optional): <input type = "text" id = "checkoutEmailAddress" style = "margin-left: 2.5vw;"><br>
<input type = "button" onclick = "databaseCheckout()" value = "Check Out">
</form>
<p> <span style = "color: red;">*</span> = required field </p>
</div>

<div id = "checkinFeature" style = "display: none;">
<h5>Enter in the Product ID number of the product to be checked back in: </h5>
<form>
<span style = "color: red;">*</span> Product ID Number/Product ID Number: <input type = "text" id = "checkinProductIDNumber"><br>
<input type = "button" onclick = "databaseCheckin()" style = "margin-left: 0.2vw;" value = "Check In">
</form>
<p> <span style = "color: red;">*</span> = required field </p>
</div>

<div id = "searchFeature" style = "display: none;">
<h5>Use the search feature to fine-tune your search</h5>
<form>
Transaction Number: <input style = "margin-left: 0.2vw; margin-right: 9vw;" type = "text" id = "adminTransactionNumber">Product Number: <input type = "text" style = "margin-left: 0.2vw;"id = "adminProductNumber"><br>
Manufacturer: <input style = "margin-left: 3.5vw; margin-right: 8.9vw;" type = "text" id = "adminManufacturer">Product Name: <input type = "text" style = "margin-left: 1.25vw;" id = "adminProduct"><br>
Student Name: <input style = "margin-right: 8.9vw; margin-left: 3.2vw;" type = "text" id = "adminStudentName">Student Number: <input type = "text" style = "margin-left: 0.35vw" id = "adminStudentNumber"><br>
Organization Name: <input style = "margin-right: 8.85vw; margin-left: 0.7vw;" type = "text" id = "adminOrganization">Email Address: <input type = "text" style = "margin-left: 1.1vw;" id = "adminEmail"><br>
<input type = "radio" name = "filterStatus" id = "adminAll" style = "margin-right: 0.70vw" checked = "checked">Show All Transactions<input type = "radio" style = "margin-left: 17.3vw; margin-right: 0.70vw;" name = "filterStatus" id = "adminCompleted"> Show Only Completed Transactions
<br><input type = "radio" style = "margin-right: 0.70vw;"  name = "filterStatus" id = "adminOngoing">Show Only Ongoing Transactions
<br>

<input id = "button8" type = "button" onclick = "adminDataLoad('A')" value = "Submit">
</form>
<br><br>
<p id = "searchStatus" style = "display: none;"></p>
<table id = "adminTable" style = "background-color: white;" border = "1">
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
<th onclick = "adminDataLoad('J')" style = "cursor: pointer; padding-left: 1vw; padding-right: 1vw;">Actual Checkin Date  </th>
<th onclick = "adminDataLoad('K')" style = "cursor: pointer; padding-left: 1vw; padding-right: 1vw;">Expected Checkin Date </th>
</tr>
</thead>
<tbody id = "adminTableBody">
</tbody>
</table>
</div>

<div id = "addProducts" style = "display: none;">
<h5>Type in the manufacturer name and the product name of the new product: </h5>
<form>
<span style = "color: red;">*</span> Manufacturer Name: <input style = "margin-right: 3vw; margin-left: 0.2vw;" type = "text" id = "addManufacturer"><span style = "color: red;">*</span> Product Name: <input id = "addProductName" style = "margin-left: 0.2vw;" type = "text"><br>
<span style = "color: red;">*</span> Quantity: <input style = "margin-left: 5.5vw;" type = "number" id = "addQuantity"><br>
<input id = "button9" type = "button" onclick = "addProductDatabase()" value = "Submit">
</form>
<p> <span style = "color: red;">*</span> = required field </p>
</div>

<div id = "deleteProducts" style = "display: none;">
<h5>Type in the Product ID Number of the product you want to remove from the database</h5>
<p style = "font-size: 1vw;">Note: Doing this will delete any transactions from the administrator's records that contain the item that is being deleted</p>
<form>
<span style = "color: red;">*</span> Product ID Number/Product ID Number: <input type = "text" id = "deleteCode"><br>
<input type = "button" onclick = "deleteProductDatabase()" value = "Delete">
</form>
<p> <span style = "color: red;">*</span> = required field </p>
</div>

<div id = "updateProducts" style = "display: none;">
<h5>Type in the Product ID Number of the product whose information you would like to update. Then, type in what you would like the new product name and manufacturer name for his product to be</h5>
<form>
<span style = "color: red;">*</span> Product ID Number: <input type = "text" id = "updateProductIDNumber" style = "margin-left: 1vw;"><br>
&nbsp;&nbsp;&nbsp;Manufacturer Name: <input type = "text" id = "updateManufacturerName" style = "margin-left: 0.8vw; margin-right: 5vw;">Product Name: <input type = "text" id = "updateProductName" style = "margin-left: 0.2vw;"><br>
<input type = "button" onclick = "databaseUpdate()" value = "Update">
</form>
<p> <span style = "color: red;">*</span> = required field </p>
</div>

<div id = "changePassword" style = "display: none;">
<h5>Please enter in the old site password and the new password you would like the website to have: </h5>
<form>
<span style = "color: red;">*</span> Old Site Password: <input type = "text" id = "oldPassword" style = "margin-right: 5vw; margin-left: 0.2vw;" >
<span style = "color: red;">*</span> New Site Password: <input type = "text" id = "newPassword" style = "margin-left: 0.2vw;"><br>
<input type = "button" onclick = "databaseChangePassword()" value = "Change Password">
</form>
<p> <span style = "color: red;">*</span> = required field </p>
</div>

<p id = "functionStatus" style = "display: none;"></p>
<div  class = "footer">Clark University | Little Center Theater Equipment Rentals | &#169; 2019</div>
</body>
</html>

