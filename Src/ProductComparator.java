import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProductComparator
{
	public class SortByProductNumLow implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			return (new BigInteger(firstProduct.QR_Code).compareTo(new BigInteger(secondProduct.QR_Code)));		}
	}
	
	public class SortByProductNumHigh implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			return (new BigInteger(secondProduct.QR_Code).compareTo(new BigInteger(firstProduct.QR_Code)));		}
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
	
	public class SortByAvailabilityLow implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			if(firstProduct.checkinDate.equals("N/A"))
				return -1;
			else
				return 1;
		}
	}
	public class SortByAvailabilityHigh implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			if(firstProduct.checkinDate.equals("N/A"))
				return 1;
			else
				return -1;
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
	
}
