package filmObjects;


public class KeywordMatcher 
{
	//matchDataStrings returns true if smallerString is the empty String or has only whitespace characters in it.
	//Additionally, if longString contains all the characters of smallerString consecutively at any point, then true is returned.
	//Otherwise, false is returned (ex. matchDataStrings("television", "vis") returns true, while matchDataStrings("television", "vid") returns false)
	 public boolean matchDataStrings(String longString, String smallerString)
	    {
	    	int longStringIndex = 0, smallStringIndex = 0;
	    	if(smallerString == null || smallerString.equals(""))
	    		return true;
	    	
	    	if(longString == null || longString.equals(""))
	    		return false;
	    	
	    	String newLongString = longString.trim().replaceAll("'", "").toUpperCase();
	    	String newShortString = smallerString.trim().replaceAll("'",  "").toUpperCase();
	    	if(newShortString.length() == 0 || newLongString.length() == 0)
	    		return true;
	    	
	    	while(longStringIndex < newLongString.length())
	    	{
	    		if(newLongString.charAt(longStringIndex) != newShortString.charAt(smallStringIndex))
	    			smallStringIndex = 0;
	    		else
	    		{
	    			++smallStringIndex;
	    			if(smallStringIndex >= newShortString.length())
	    				return true;
	    		}
	    			
	    		
	    		++longStringIndex;
	    	}
	    	
	    	return false;
	    	
	    }
	 //isEmpty returns true if inputString is null, is the empty string, or contains only whitespace characters
	 //otherwise, it returns false.
		public boolean isEmpty(String inputString)
		{
			if(inputString == null || inputString.equals(""))
			{
				return true;
			}
			
			for(int i = 0; i < inputString.length(); ++i)
			{
				if(!Character.isWhitespace(inputString.charAt(i)))
					return false;
				
			}
			return true;
		}
	 
}
