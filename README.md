# FilmProject

## Summary

This is a project that was made by Skyler Brivic, Ishraq Chowdhury and Alexander Rakovshik. It consists of a database that stores equipment rentals from the Clark University theater department (which is called the "Little Center"), a website to check items out and view available products, and a server to handle backend logic. We made this product as part of a semester-long project, and we reported our progress to the director of the Little Center throughout the semester. This website was originally going to be added to the Clark University server, but due to issues with finding a spot on the Clark server to host this website, it has yet to be put online. As such, this repository is currently the only place where the website we made can be viewed.

## Project Components

The folder "textFiles" contains two files. One is a text document containing the mySql commands needed to create the database and tables used for the project.

The other file ("information.txt") contains the database credentials that the file DatabaseInterface.java reads in order to connect to the database

IMPORTANT NOTE: The file queryGenerator.java takes as input a text file where each line is of the form manufacturer name|product name|quantity. It outputs a text file containing the MySQL queries that would insert those elements into the database. The program will prompt you for the absolute pathnames of input and output files.

The folder "webPages" contains 3 files containing  HTML, CSS and JavaScript. More specifically, each file is a jsp document (JavaServer Pages).

The file userWelcomePage.jsp is the homepage for the site. 

The file login.jsp contains the login screen (for administrators) and all the administrative functions of the site, such as checking products in and out.

The file viewProducts.jsp is the page where regular users can view the inventory of all products and see which products are available.

The folder "Src" contains two folders, which are the names of the two java packages that hold all the java files used for the backend of the website. The first package in "Src" is called filmObjects, and the second package in "Src" is called filmProjectServlets.

The filmObjects package contains 8 java files which contain objects that are used by the servlets (however, none of the java files in this package are servlets - they are all regular object files). 

The first file is DatabaseInterface.java. This class allows a user to connect to the MySQL database using the credentials in the file information.txt. It is the file that all the servlets depend on to be able to access the database. 

KeywordMatcher.java creates an object of type KeywordMatcher, which can check if a String is empty (if it contains only whitespace or no characters) using the method isEmpty,
or can use the method matchDataStrings to compare two Strings to see if the second string appears in its entirety anywhere in the first string

The filmProjectServlets package contains 9 java files which represent servlets that the client can connect to. 

AddProduct.java contains a servlet that adds a product to the database, provided that the user entered in the correct site password

AdministratorDataView.java contains a servlet that returns a list of transactions that represent all the transactions that match the criteria that the administrator has searched for. This file also requires a correct site password to be used.

ChangePassword.java contains a servlet that changes the site's password to a new password, provided that the user entered in the correct current password for the website.

CheckinProduct.java contains a servlet that checks in the product specified by a QR Code. This servlet can also only be accessed if the user provides the correct password for the website

CheckoutProduct.java contains a servlet that checks out the product specified by a QR Code, and creates a record of the student who is checking the product out. This servlet can also only be accessed if the user provides the corrext password for the website.

DeleteProduct.java contains a servlet that deletes a specific product from the database of products. This servlet can only be accessed if the user provides the correct password for the website.

ListProducts.java contains a servlet that returns a list of ProductAggregates representing all of the types of products that match the user's search query. This servlet can be accessed by anyone, and does not require a password to use.

PasswordCheck.java is a servlet that returns 1 if the user entered in the correct password for the site, and 0 otherwise. Besides this, the servlet does not do anything.

UpdateProduct.java is a servlet that updates the product name and manufacturer name of a specific product. This servlet can only be accessed if the user provides the correct password for the website.

### Dependencies:

This project requires the gson 2.6.2 jar and the MySQL connector jar (v. 5.1.47) to be in the build path and class path for the project in order to build and run the project.
