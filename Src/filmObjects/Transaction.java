package filmObjects;


public class Transaction 
{
	
public String transactionNumber, QR_Code, manufacturerName, productName, studentNumber, studentName, studentEmail, organizationName, checkoutDate, checkinDate;

	public Transaction(String newTransaction, String newQR, String newManufacturer, String newProduct, String newNumber, String newName, String newEmail, String newOrganization, String newCheckout, String newCheckin)
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
	}

}
