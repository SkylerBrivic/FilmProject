package filmObjects;


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

