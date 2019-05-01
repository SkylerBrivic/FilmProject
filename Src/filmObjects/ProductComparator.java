package filmObjects;

import java.util.Comparator;

public class ProductComparator {
	
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

}

