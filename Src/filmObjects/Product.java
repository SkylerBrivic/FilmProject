package filmObjects;

//a Product object represents the product ID number of a product, the manufacturer name of a product, the product name of the product,
//the checkoutDate of the product (if it is currently checked out), and the checkinDate of the product (both date Strings are in yyyy-MM-dd format).
public class Product {
	
	public String Product_ID, ManufacturerName, ProductName, checkoutDate, checkinDate;
	
	public Product()
	{
		Product_ID = ManufacturerName = ProductName = checkoutDate = checkinDate = "";
		
	}
	public Product(String new_Product_ID, String newManufacturer, String newProduct, String newOutDate, String newReturnDate)
	{
		Product_ID = new_Product_ID;
		ManufacturerName = newManufacturer;
		ProductName = newProduct;
		checkoutDate = newOutDate;
		checkinDate = newReturnDate;
	}
	
}

