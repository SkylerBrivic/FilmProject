package filmObjects;

//a Product object represents the product ID number of a product, the manufacturer name of a product, the product name of the product,
//the checkoutDate of the product (if it is currently checked out), and the checkinDate of the product (both date Strings are in yyyy-MM-dd format).
public class Product {
	
	public String QR_Code, ManufacturerName, ProductName, checkoutDate, checkinDate;
	
	public Product()
	{
		QR_Code = ManufacturerName = ProductName = checkoutDate = checkinDate = "";
		
	}
	public Product(String new_QR, String newManufacturer, String newProduct, String newOutDate, String newReturnDate)
	{
		QR_Code = new_QR;
		ManufacturerName = newManufacturer;
		ProductName = newProduct;
		checkoutDate = newOutDate;
		checkinDate = newReturnDate;
	}
	
}

