import java.text.*;
import java.util.Comparator;
import java.util.Date;

public class ProductComparator 
{

	public class SortByProductNumLow implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			return Integer.parseInt(firstProduct.QR_Code) - Integer.parseInt(secondProduct.QR_Code);
		}
	}
	
	public class SortByProductNumHigh implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			return Integer.parseInt(secondProduct.QR_Code) - Integer.parseInt(firstProduct.QR_Code);
		}
	}
	
	public class SortByTransactionLow implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			return Integer.parseInt(firstProduct.TransactionNumber) - Integer.parseInt(secondProduct.TransactionNumber);
		}
	}
	
	public class SortByTransactionHigh implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			return Integer.parseInt(secondProduct.TransactionNumber) - Integer.parseInt(firstProduct.TransactionNumber);
		}
	}
	
	public class SortByProductNameLow implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			return firstProduct.ProductName.compareToIgnoreCase(secondProduct.ProductName);
		}
	}
	
	public class SortByProductNameHigh implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			return secondProduct.ProductName.compareToIgnoreCase(firstProduct.ProductName);
		}
	}
	
	public class SortByManufacturerNameLow implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			return firstProduct.ManufacturerName.compareToIgnoreCase(secondProduct.ManufacturerName);
		}
	}
	
	public class SortByManufacturerNameHigh implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			return secondProduct.ManufacturerName.compareToIgnoreCase(firstProduct.ManufacturerName);
		}
	}
	
	public class SortByStudentNumberLow implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			return Integer.parseInt(firstProduct.currentUser) - Integer.parseInt(secondProduct.currentUser);
		}
	}
	
	public class SortByStudentNumberHigh implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			return Integer.parseInt(secondProduct.currentUser) - Integer.parseInt(firstProduct.currentUser);
		}
	}
	
	public class SortByCheckoutDateLow implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{

			if(firstProduct.checkoutDate.equals("N/A"))
				return -1;
			if(secondProduct.checkoutDate.equals("N/A"))
				return 1;
			try {
			Date firstDate = new SimpleDateFormat("yyyy-MM-dd").parse(firstProduct.checkoutDate);
			Date secondDate = new SimpleDateFormat("yyy-MM-dd").parse(secondProduct.checkoutDate);
			return firstDate.compareTo(secondDate);
			
			}
			catch(ParseException e)
			{}
			return -1;
		}
	}
	public class SortByCheckoutDateHigh implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			if(firstProduct.checkoutDate.equals("N/A"))
				return 1;
			if(secondProduct.checkoutDate.equals("N/A"))
				return -1;
			try {
			Date firstDate = new SimpleDateFormat("yyyy-MM-dd").parse(firstProduct.checkoutDate);
			Date secondDate = new SimpleDateFormat("yyy-MM-dd").parse(secondProduct.checkoutDate);
			return secondDate.compareTo(firstDate);
			
			}
			catch(ParseException e)
			{}
			return -1;
		}
	}
	public class SortByCheckinDateLow implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			if(firstProduct.checkinDate.equals("N/A"))
				return -1;
			if(secondProduct.checkinDate.equals("N/A"))
				return 1;
			try {
			Date firstDate = new SimpleDateFormat("yyyy-MM-dd").parse(firstProduct.checkinDate);
			Date secondDate = new SimpleDateFormat("yyy-MM-dd").parse(secondProduct.checkinDate);
			return firstDate.compareTo(secondDate);
			
			}
			catch(ParseException e)
			{}
			return -1;
		}
	}
	
	public class SortByCheckinDateHigh implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			if(firstProduct.checkinDate.equals("N/A"))
				return 1;
			if(secondProduct.checkinDate.equals("N/A"))
				return -1;
			try {
			Date firstDate = new SimpleDateFormat("yyyy-MM-dd").parse(firstProduct.checkinDate);
			Date secondDate = new SimpleDateFormat("yyy-MM-dd").parse(secondProduct.checkinDate);
			return secondDate.compareTo(firstDate);
			
			}
			catch(ParseException e)
			{}
			return -1;
		}
	}
	
}
	

