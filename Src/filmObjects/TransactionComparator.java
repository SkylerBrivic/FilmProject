
package filmObjects;

import java.text.*;
import java.util.Comparator;
import java.util.Date;
import java.math.*;

public class TransactionComparator 
{

	public class SortByProductNumLow implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			return (new BigInteger(firstProduct.QR_Code).compareTo(new BigInteger(secondProduct.QR_Code)));
		}
	}
	
	public class SortByProductNumHigh implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			return (new BigInteger(secondProduct.QR_Code).compareTo(new BigInteger(firstProduct.QR_Code)));		}
	}
	
	public class SortByTransactionLow implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			return (new BigInteger(firstProduct.transactionNumber).compareTo(new BigInteger(secondProduct.transactionNumber)));		}
	}
	
	public class SortByTransactionHigh implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			return (new BigInteger(secondProduct.transactionNumber).compareTo(new BigInteger(firstProduct.transactionNumber)));
		}
	}
	
	public class SortByProductNameLow implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			return firstProduct.productName.compareToIgnoreCase(secondProduct.productName);
		}
	}
	
	public class SortByProductNameHigh implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			return secondProduct.productName.compareToIgnoreCase(firstProduct.productName);
		}
	}
	
	public class SortByManufacturerNameLow implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			return firstProduct.manufacturerName.compareToIgnoreCase(secondProduct.manufacturerName);
		}
	}
	
	public class SortByManufacturerNameHigh implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			return secondProduct.manufacturerName.compareToIgnoreCase(firstProduct.manufacturerName);
		}
	}
	
	public class SortByStudentNumberLow implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			return firstProduct.studentNumber.compareToIgnoreCase(secondProduct.studentNumber);
	}
	
		}
	public class SortByStudentNumberHigh implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			return secondProduct.studentNumber.compareToIgnoreCase(firstProduct.studentNumber);
		}
	}
	public class SortByCheckoutDateLow implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{

			if(firstProduct.checkoutDate.equals("N/A"))
				return -1;
			if(secondProduct.checkoutDate.equals("N/A"))
				return 1;
			try {
			Date firstDate = new SimpleDateFormat("yyyy-MM-dd").parse(firstProduct.checkoutDate);
			Date secondDate = new SimpleDateFormat("yyyy-MM-dd").parse(secondProduct.checkoutDate);
			return firstDate.compareTo(secondDate);
			
			}
			catch(ParseException e)
			{}
			return -1;
		}
	}
	public class SortByCheckoutDateHigh implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			if(firstProduct.checkoutDate.equals("N/A"))
				return 1;
			if(secondProduct.checkoutDate.equals("N/A"))
				return -1;
			try {
			Date firstDate = new SimpleDateFormat("yyyy-MM-dd").parse(firstProduct.checkoutDate);
			Date secondDate = new SimpleDateFormat("yyyy-MM-dd").parse(secondProduct.checkoutDate);
			return secondDate.compareTo(firstDate);
			
			}
			catch(ParseException e)
			{}
			return -1;
		}
	}
	public class SortByCheckinDateLow implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			if(firstProduct.checkinDate.equals("N/A"))
				return -1;
			if(secondProduct.checkinDate.equals("N/A"))
				return 1;
			try {
			Date firstDate = new SimpleDateFormat("yyyy-MM-dd").parse(firstProduct.checkinDate);
			Date secondDate = new SimpleDateFormat("yyyy-MM-dd").parse(secondProduct.checkinDate);
			return firstDate.compareTo(secondDate);
			
			}
			catch(ParseException e)
			{}
			return -1;
		}
	}
	
	public class SortByCheckinDateHigh implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			if(firstProduct.checkinDate.equals("N/A"))
				return 1;
			if(secondProduct.checkinDate.equals("N/A"))
				return -1;
			try {
			Date firstDate = new SimpleDateFormat("yyyy-MM-dd").parse(firstProduct.checkinDate);
			Date secondDate = new SimpleDateFormat("yyyy-MM-dd").parse(secondProduct.checkinDate);
			return secondDate.compareTo(firstDate);
			
			}
			catch(ParseException e)
			{}
			return -1;
		}
	}
	
	public class SortByExpectedCheckinDateHigh implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			if(firstProduct.expectedCheckinDate.equals("N/A"))
				return 1;
			if(secondProduct.expectedCheckinDate.equals("N/A"))
				return -1;
			try {
			Date firstDate = new SimpleDateFormat("yyyy-MM-dd").parse(firstProduct.expectedCheckinDate);
			Date secondDate = new SimpleDateFormat("yyyy-MM-dd").parse(secondProduct.expectedCheckinDate);
			return secondDate.compareTo(firstDate);
			
			}
			catch(ParseException e)
			{}
			return -1;
		}
	}
		
	

public class SortByExpectedCheckinDateLow implements Comparator<Transaction>
{
	public int compare(Transaction firstProduct, Transaction secondProduct)
	{
		if(firstProduct.expectedCheckinDate.equals("N/A"))
			return 1;
		if(secondProduct.expectedCheckinDate.equals("N/A"))
			return -1;
		try {
		Date firstDate = new SimpleDateFormat("yyyy-MM-dd").parse(firstProduct.expectedCheckinDate);
		Date secondDate = new SimpleDateFormat("yyyy-MM-dd").parse(secondProduct.expectedCheckinDate);
		return firstDate.compareTo(secondDate);
		
		}
		catch(ParseException e)
		{}
		return -1;
	}
	}
	



	
	public class SortByStudentNameLow implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			return firstProduct.studentName.compareToIgnoreCase(secondProduct.studentName);
		}
	}
	
	public class SortByStudentNameHigh implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			return secondProduct.studentName.compareToIgnoreCase(firstProduct.studentName);
		}
	}
	
	public class SortByEmailLow implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			return firstProduct.studentEmail.compareToIgnoreCase(secondProduct.studentEmail);
		}
	}
	
	public class SortByEmailHigh implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			return secondProduct.studentEmail.compareToIgnoreCase(firstProduct.studentEmail);
		}
	}
	public class SortByOrganizationLow implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			return firstProduct.organizationName.compareToIgnoreCase(secondProduct.organizationName);
		}
	}
	public class SortByOrganizationHigh implements Comparator<Transaction>
	{
		public int compare(Transaction firstProduct, Transaction secondProduct)
		{
			return secondProduct.organizationName.compareToIgnoreCase(firstProduct.organizationName);
		}
	}
	
	}
	

	
