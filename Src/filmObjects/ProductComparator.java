package filmObjects;

import java.util.Comparator;

public class ProductComparator {
	
	//Sorts a List of Products by Manufacturer Name from low to high alphabetically
	public class SortByManufacturerNameLow implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			return firstProduct.ManufacturerName.compareToIgnoreCase(secondProduct.ManufacturerName);
		}
	}
	
	//Sorts a List of Products by Manufacturer Name from high to low alphabetically
	public class SortByManufacturerNameHigh implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			return secondProduct.ManufacturerName.compareToIgnoreCase(firstProduct.ManufacturerName);
		}
	}
	
	//Sorts a List of Products by Product Name from low to high alphabetically
	public class SortByProductNameLow implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			return firstProduct.ProductName.compareToIgnoreCase(secondProduct.ProductName);
		}
	}
	
	//Sorts a List of Products by Product Name from high to low alphabetically.
	public class SortByProductNameHigh implements Comparator<Product>
	{
		public int compare(Product firstProduct, Product secondProduct)
		{
			return secondProduct.ProductName.compareToIgnoreCase(firstProduct.ProductName);
		}
	}

}
