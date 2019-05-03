package filmObjects;


//a Transaction object stores the transaction number of a transaction, the QR Code, manufacturer name and product name of the product associated with that transaction,
//the student number, student name, email address, and organization of the student who checked the product out, and the checkout date, checkin date, and expected checkin date of the product
public class Transaction 
{
	
public String transactionNumber, QR_Code, manufacturerName, productName, studentNumber, studentName, studentEmail, organizationName, checkoutDate, checkinDate, expectedCheckinDate;

	public Transaction(String newTransaction, String newQR, String newManufacturer, String newProduct, String newNumber, String newName, String newEmail, String newOrganization, String newCheckout, String newCheckin, String newExpectedDate)
	{
		transactionNumber = newTransaction;
		QR_Code = newQR;
		manufacturerName = newManufacturer;
		productName = newProduct;
		studentNumber = newNumber;
		studentName = newName;
		studentEmail = newEmail;
		organizationName = newOrganization;
		checkoutDate = newCheckout;
		checkinDate = newCheckin;
		expectedCheckinDate = newExpectedDate;
	}

}
