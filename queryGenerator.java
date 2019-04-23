import java.io.*;
import java.util.*;

public class queryGenerator 
{
public String inputFileName, outputFileName;

public queryGenerator(String newInput, String newOutput)
{
	inputFileName = newInput;
	outputFileName = newOutput;
}

public void convert()
{
	BufferedReader myReader = null;
	PrintWriter myWriter = null;
	String currentInputLine = "", currentOutputLine = "", manufacturer = "", productName = "";
	
	try
	{
		myReader = new BufferedReader(new FileReader(inputFileName));
		myWriter = new PrintWriter(new FileWriter(outputFileName));
		while((currentInputLine = myReader.readLine()) != null)
		{
			if(currentInputLine.length() < 3)
				break;
		
		if(currentInputLine.indexOf("|") == -1)
		{
			System.err.println("Error: Was expecting a |. Current line did not have a pipe symbol. Current Line was: " + currentInputLine);
			break;
		}
		manufacturer = currentInputLine.split("\\|", 2)[0].trim();
		productName = currentInputLine.split("\\|", 2)[1].trim();
		currentOutputLine = "INSERT INTO productList (manufacturer, productName, isAvailable) VALUES('" + manufacturer 
		+ "', '" + productName + "', 1);";
		myWriter.println(currentOutputLine);
	}
	}
	catch(IOException e)
	{}
	myWriter.close();
	
}

public static void main(String[] args)
{
	String input, output;
	System.out.println("Please enter in the absolute pathname of the input file. The input file should contain a series of lines of the format manufacturer|product");
	Scanner sc = new Scanner(System.in);
	input = sc.nextLine().trim();
	System.out.println("Please enter in the absolute pathname of the output file. This is where the list of generated MySql queries will be saved");
	output = sc.nextLine().trim();
	sc.close();
	queryGenerator myConverter = new queryGenerator(input, output);
	myConverter.convert();
	System.out.println("Success!");
	
}

}
