package filmObjects;


//a Transaction object stores the transaction number of a transaction, the Product ID Number, manufacturer name and product name of the product associated with that transaction,
//the student number, student name, email address, and organization name of the student who checked the product out, and the checkout date, the actual checkin date, and expected checkin date of the product
public class Transaction 
{
	
public String transactionNumber, Product_ID, manufacturerName, productName, studentNumber, studentName, studentEmail, organizationName, checkoutDate, actualCheckinDate, expectedCheckinDate;

	public Transaction(String newTransaction, String newProductID, String newManufacturer, String newProduct, String newNumber, String newName, String newEmail, String newOrganization, String newCheckout, String newActualCheckin, String newExpectedDate)
	{
		transactionNumber = newTransaction;
		Product_ID = newProductID;
		manufacturerName = newManufacturer;
		productName = newProduct;
		studentNumber = newNumber;
		studentName = newName;
		studentEmail = newEmail;
		organizationName = newOrganization;
		checkoutDate = newCheckout;
		actualCheckinDate = newActualCheckin;
		expectedCheckinDate = newExpectedDate;
	}

}
