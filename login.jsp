<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<script type = "text/javascript" src = "http://code.jquery.com/jquery-1.10.0.min.js"></script>
<script>
var userPassword = "";
var adminTableOrder = 1;

//an exitCode of 0 indicates success
//an exitCode of 1 indicates an invalid password
//an exitCode of 2 indicates that the servlet could not be connected to
//an exit code of 3 indicates that the requested operation could not be completed
//if the database query succedded, then functionMessage stores the word(s) representing how to end
//the update message (ex. for checkout products, function message would be "checked out" and the function status line would thus be updated to say
//"product succesfully checked out"). If the query failed, then function message stores the error message to be displayed (ex. "Error: Product was not checked out to begin with"
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

function databaseLogin()
{
		 userPassword = $("#Password").val();
		 
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
					document.getElementById("searchFeature").style = "display: inline;";
					document.getElementById("featureSelect").style = "display: inline";
					checkoutProductFeatureLoad();
					
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
			updateStatus(2, "");
		}
		
	});
	
}

function addProductDatabase()
{
	var myManufacturer = $("#addManufacturer").val();
	var myProduct = $("#addProductName").val();
	
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
         	{
        		updateStatus(0, "added");
         	}
        	 else
        	 {
        		 updateStatus(1, "");
        	}
         },
         failure: function(data) {
            {
            	updateStatus(2, "");
            }
            
         }
     });	
	
}

function deleteProductDatabase()
{
var myQR = $("#deleteCode").val();	
var returnValue = 5;

$.ajax ({
	url: 'DeleteProduct',
	type: 'POST',
	data: {
		'QR_Code': myQR,
		'password': userPassword,
		
	},
	success: function(data)
	{
		returnValue = Number(data);
		
		if(returnValue == 0)
			updateStatus(0, "deleted");
		else if(returnValue == 1)
			updateStatus(3, "Error: Invalid QR Code.");
		else
			updateStatus(1, "");
	},
	failure: function(data)
	{
		updateStatus(2, "");	
	}
	
	
});
	
}

function databaseCheckout()
{
	var QR_Code = $("#checkoutQRCode").val();
	var StudentNumber = $("#checkoutStudentNumber").val();
	var StudentName = $("#checkoutStudentName").val();
	var OrganizationName = $("#checkoutOrganizationName").val();
	var Email = $("#checkoutEmailAddress").val();
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
        	 updateStatus(0, "checked out");
         else if(returnValue == 1)
        	 updateStatus(3, "Error: Invalid QR Code entered");
         else if(returnValue == 2)
        	 updateStatus(3, "Error: Product already checked out by somebody else");
         else
        	 updateStatus(1, "");
         	 
         },
         failure: function(data) {
             updateStatus(2, "");
            
         }
     });		
}

function databaseCheckin()
{
	var QR_Code = $("#checkinQRCode").val();
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
        	 updateStatus(0, "checked back in");
         else if(returnValue == 1)
        	 updateStatus(3, "Error: Invalid QR Code was entered");
         
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

function databaseUpdate()
{
	var QR_Code = $("#updateProductQRCode").val();
	var manufacturer = $("#updateManufacturername").val();
	var product = $("#updateProductName").val();
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
        	 updateStatus(0, "updated");
        
         else if(returnValue == 1)
         {
        	updateStatus(3, "Error: Invalid QR Code was entered.");
         }
         else
        	updateStatus(1, "");
        	 
        	 
         },
         failure: function(data) {
            updateStatus(2, "");         
         }
     });	
	
}

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
		else
			{
			document.getElementById("functionStatus").innerHTML = "Error: The password typed in for the current site password was wrong. Please try again."
			document.getElementById("functionStatus").style = "display: inline; color: red;";
			}
			
	},
	failure: function(data){
		document.getElementById("functionStatus").innerHTML = "Error: Server could not connect to the database. Check for network connection issues."
		document.getElementById("functionStatus").style = "display: inline; color: red;";
	}
	
	
});
	
	
}
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
<body style = "background-color: Ivory;">
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

<h1 style = "position: relative; top: 2vw;">Enter the Site Password Here: </h1><br>
<form>
Site Password: <input type = "text" id = "Password"><br>
<input id = "submitButton" type = "button" onclick = "databaseLogin()" value = "Submit">
</form>
<br>
<p id = "loginConfirmation" style = "display: none;"></p>

<div id = "featureSelect" style = "display: none;">
<form>
<br>
Checkout Product: <input type = "radio" name = "featureOption" onclick = "checkoutProductFeatureLoad()" checked = "checked" style = "margin-right: 11.8vw;">
Check In Product: <input type = "radio" name = "featureOption" onclick = "checkinProductFeatureLoad()"><br>
List All Transactions: <input type = "radio" name = "featureOption" onclick = "productSearchFeatureLoad()" style = "margin-right: 11.8vw;">
 Add a Product to the Database: <input type = "radio" name = "featureOption" onclick = "addFeatureLoad()" ><br>
 Delete a Product from the Database: <input type = "radio" name = "featureOption" onclick = "deleteFeatureLoad()" style = "margin-right: 5vw;">
 Update the Product Name and Manufacturer Name of a Product in the Database: <input type = "radio" name = "featureOption" onclick = "updateFeatureLoad()"><br>
 Change Site Password: <input type = "radio" name = "featureOption" onclick = "changePasswordFeatureLoad()"><br>
</form>
</div>

<div id = "checkoutFeature" style = "display: none;">
<h5>Enter in the product ID number/QR Code of the product to be checked out and the information about the person checking the eqipment out: </h5>
<form>
Product ID Number/QR Code: <input type = "text" id = "checkoutQRCode"> <br>
Student Number: <input type = "text" id = "checkoutStudentNumber" style = "margin-right: 11.8vw;">
Student Name: <input type = "text" id = "checkoutStudentName"><br>
Organization Name (optional): <input type = "text" id = "checkoutOrganizationName" style = "margin-right: 11.8vw;">
Email Address (optional): <input type = "text" id = "checkoutEmailAddress"><br>
<input type = "button" onclick = "databaseCheckout()" value = "Check Out">
</form>
</div>

<div id = "checkinFeature" style = "display: none;">
<h5>Enter in the Product ID number/QR Code of the product to be checked back in: </h5>
<form>
Product ID Number/QR Code: <input type = "text" id = "checkinQRCode"><br>
<input type = "button" onclick = "databaseCheckin()" value = "Check In">
</form>
</div>

<div id = "searchFeature" style = "display: none;">
<h5>Use the search feature to fine-tune your search</h5>
<form>
Transaction Number: <input style = "margin-right: 5vw;" type = "text" id = "adminTransactionNumber">Product Number: <input type = "text" id = "adminProductNumber"><br>
Manufacturer: <input style = "margin-right: 7vw;" type = "text" id = "adminManufacturer">Product: <input type = "text" id = "adminProduct"><br>
Student Name: <input style = "margin-right: 5vw;" type = "text" id = "adminStudentName">Student Number: <input type = "text" id = "adminStudentNumber"><br>
Organization Name: <input style = "margin-right: 5vw;" type = "text" id = "adminOrganization">Email Address: <input type = "text" id = "adminEmail"><br>
Show All Transactions: <input type = "radio" name = "filterStatus" id = "adminAll" checked = "checked">
<br>
Show Only Completed Transactions <input type = "radio" name = "filterStatus" id = "adminCompleted">
<br>
Show Only Transactions That Are Ongoing (where the product is still checked out): <input type = "radio" name = "filterStatus" id = "adminOngoing">
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
<th onclick = "adminDataLoad('J')" style = "cursor: pointer; padding-left: 1vw; padding-right: 1vw;">Checkin Date  </th>
</tr>
</thead>
<tbody id = "adminTableBody">
</tbody>
</table>
</div>

<div id = "addProducts" style = "display: none;">
<h5>Type in the manufacturer name and the product name of the new product: </h5>
<form>
Manufacturer Name: <input style = "margin-right: 3vw;" type = "text" id = "addManufacturer">Product Name: <input id = "addProductName" type = "text"><br>
<input id = "button9" type = "button" onclick = "addProductDatabase()" value = "Submit">
</form>
</div>

<div id = "deleteProducts" style = "display: none;">
<h5>Type in the QR Code/Product ID Number of the product you want to remove from the database</h5>
<p style = "font-size: 1vw;">Note: Doing this will delete any transactions from the administrator's records that contain the item that is being deleted</p>
<form>
QR Code/Product ID Number: <input type = "text" id = "deleteCode"><br>
<input type = "button" onclick = "deleteProductDatabase()" value = "Delete">
</form>
</div>

<div id = "updateProducts" style = "display: none;">
<h5>Type in the QR Code/Product ID Number of the product whose information you would like to update. Then, type in what you would like the new product name and manufacturer name for his product to be</h5>
<form>
QR Code/Product ID Number: <input type = "text" id = "updateProductQRCode"><br>
Manufacturer Name: <input type = "text" id = "updateManufacturerName" style = "margin-right: 5vw;">Product Name: <input type = "text" id = "updateProductName"><br>
<input type = "button" onclick = "databaseUpdate()" value = "Update">
</form>
</div>

<div id = "changePassword" style = "display: none;">
<h5>Please enter in the old site password and the new password you would like the website to have: </h5>
<form>
Old Site Password: <input type = "text" id = "oldPassword" style = "margin-right: 5vw;">
New Site Password: <input type = "text" id = "newPassword"><br>
<input type = "button" onclick = "databaseChangePassword()" value = "Change Password">
</form>
</div>

<p id = "functionStatus" style = "display: none;"></p>
<div class = "footer">Clark University | Little Center Theater Equipment Rentals | &#169; 2019</div>
</body>
</html>
