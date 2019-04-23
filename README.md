# FilmProject

The folder "textFiles" contains two files. One is a text document containing the mySql commands needed to create the database and tables used for the project.
The other file ("information.txt") contains the database credentials that the file DatabaseInterface.java reads in order to connect to the database

The folder "webPages" contains 3 files containing  HTML, CSS and JavaScript. More specifically, each file is a jsp document (JavaServer Pages)
The file userWelcomePage.jsp is the homepage for the site. The file login.jsp contains the login screen (for administrators) and all the administrative functions
of the site, such as checking products in and out.
The file viewProducts.jsp is the page where regular users can view the inventory of all products and see which products are available.
Note that viewProducts.jsp is currently set to output a picture of a camera for each product. More specifically, it will try to output a 
picture with the relative pathname "images/Camera.jpg". Of course, since this will be replaced later on by the actual pictures, you can just ignore this for now,
or you can download a stock photo of a camera and give it the appropriate name so that you can see what the boxes look like with a picture in them.

The folder "Src" contains two folders, which are the names of the two java packages that hold all the java files used for the backend of the website.
The first package in "Src" is called filmObjects, and the second package in "Src" is called filmProjectServlets

the filmObjects package contains 8 java files which contain objects that are used by the servlets (however, none of the java files in this
package are servlets - they are all regular object files). The first file is DatabaseInterface.java. This function allows a user to connect
to the mySql database using the credentials in the file information.txt. It is the file that all the servlets depend on to be able
to access the database. 
KeywordMatcher.java creates an object of type KeywordMatcher, which can check if a String is empty using the method isEmpty,
or can use the method matchDataStrings to compare two Strings to see if the second string appears in its entirety anywhere in the first string
Product is an object with 5 String variables - QR Code, Manufacturer, Product Name, Checkout Date, and checkinDate. It is used
soley on the backend as a means of creating ProductAggregate objects, which are what is actually sent back to the client from the server.
ProductAggregate is an object containing 2 String variables: Product Name and Manufacturer Name. It also contains 2 int variables: 
number available and number in stock. One product Aggregate object is created for each unique combination of manufacturer name and product name 
(by this I mean that two products with the same manufacturer name and product name will be represetned by one product aggregate object, which
will have a number in stock value of 2, and a number available value which could be either 0, 1 or 2 depending on whether either of the 2 products
is currently checked out). This is what is sent back to the client when they request to see all products.
ProductAggregateComparator is a class containing methods that can sort a list of ProductAggregates by either manufacturer name or product name, in either alphabetical order or reverse alphabetical order.
The ProductComparator class is a class containing methods that can sort a list of Products by either manufacturer name or product name, in either alphabetical order or reverse alphabetical order.
The file Transaction.java creates an object of type Transaction, which has 10 String variables: Transaction Number, QR Code, Manufacturer Name,
Product Name, Student Number, Student Name, Student Email, Organization Name, Checkout Date, and Checkin Date. A list of transactions is what is sent back to the client from the server
when an administrator requests to see all transactions.
TransactionComparator is a class containing methods that can sort a list of transactions by each of its 10 fields (every field except for Checkout Date and Checkin Date
can be sorted by that field's value in alphabetical order or reverse alphabetical order. Checkout Date and Checkin Date can be sorted by earlierst dates first or most recent dates first).

The filmProjectServlets package contains 9 java files which represent servlets that the client can connect to. 
AddProduct.java contains a servlet that adds a product to the database, provided that the user entered in the site password
AdministratorDataView.java contains a servlet that returns a list of transactions that represent all the transactions that match the criteria
that the administrator has searched for. This file also requires a correct site password to be used.
ChangePassword.java contains a servlet that changes the site's password to a new password, provided that the user entered in the correct
current password for the website.
CheckinProduct.java contains a servlet that checks in the product specified by a QR Code. This servlet can also only be accessed if the user
provides the correct password for the website
CheckoutProduct.java contains a servlet that checks out the product specified by a QR Code, and creates a record of the student who is checking the product out.
This servlet can also only be accessed if the user provides the corrext password for the website.
DeleteProduct.java contains a servlet that deletes a specific product from the database of products. This servlet can only be accessed if the user
provides the correct password for the website.
ListProducts.java contains a servlet that returns a list of ProductAggregates representing all of the types of products that matches the user's search query.
This servlet can be accessed by anyone, and does not require a password to use.
PasswordCheck.java is a servlet that returns 1 if the user entered in the correct password for the site, and 0 otherwise. Besides this, the servlet
does not do anything.
UpdateProduct.java is a servlet that updates the product name and manufacturer name of a specific product. This servlet can only be accessed if the user
provides the correct password for the website.
