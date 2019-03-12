
public class Product {
	
	public String QR_Code;
	public String TransactionNumber;
	public String ProductName;
	public String ManufacturerName;
	public String currentUser;
	public String checkoutDate;
	public String checkinDate;
	
	public Product(String new_QR, String newTransactionNumber, String newProduct, String newManufacturer, String newUser, String newOutDate, String newReturnDate)
	{
		QR_Code = new_QR;
		TransactionNumber = newTransactionNumber;
		ProductName = newProduct;
		ManufacturerName = newManufacturer;
		currentUser = newUser;
		checkoutDate = newOutDate;
		checkinDate = newReturnDate;
	}
	
}
