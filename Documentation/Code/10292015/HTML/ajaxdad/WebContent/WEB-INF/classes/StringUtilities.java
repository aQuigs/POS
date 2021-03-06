public class StringUtilities
{

    public static int parseMenuId(String menuId) throws NumberFormatException
    {
        return Integer.parseInt(menuId);
    }

    public static double parseMenuItemCost(String cost) throws NumberFormatException
    {
        return Double.parseDouble(cost);
    }

    public static String makeEmptyStringNull(String str)
    {
        if (str != null && str.length() == 0)
            return null;
        return str;
    }

    //@formatter:off
//	/**
//	 * This function will allow the user to determine if someone put something like
//	 * Drop                 Table in the certain word to prevent injection
//	 * @param CertainWord
//	 * @param Target
//	 * @return
//	 */
//	public static boolean DoesItContainCertainWord(String CertainWord, String Target)
//	{
//		return (Target.replaceAll("\\s+","").toLowerCase().contains(CertainWord.replaceAll("\\s+","").toLowerCase()));
//	}
//	
//	/**
//	 * This replaces double quotes
//	 * @param theString
//	 * @return
//	 */
//    public static String ReplaceDoubleQuotes(String theString)
//    {
//        return theString.replace("\"\"", "\"\"\"\"");
//    }
//
//    /**
//     * Replaces Single Quotes
//     * @param theString
//     * @return
//     */
//    public static String ReplaceSingleQuotes(String theString)
//    {
//        return theString.replace("\'", "\'\'");
//    }
//
//    public static String NumbersOnly(String theString)
//    {
//        char[] digits = theString.toCharArray();
//
//        String onlyNumbers = "";
//
//        for (int i = 0; i < digits.length; i++)
//        {
//            if (digits[i] == '0' || digits[i] == '1' || digits[i] == '2' || digits[i] == '3' || digits[i] == '4' || digits[i] == '5' || digits[i] == '6' || digits[i] == '7' || digits[i] == '8' || digits[i] == '9')
//            {
//                onlyNumbers = onlyNumbers + digits[i];
//            }
//        }
//
//        return onlyNumbers;
//    }
//
//    public static String NumbersAndLettersOnly(String theString)
//    {
//    	return theString.replaceAll("[^\\w\\s]","");
//    }
//
//    public static int CountStringOccurrences(String text, String pattern)
//    {
//        // Loop through all instances of the String 'text'.
//        int count = 0;
//        int i = 0;
//        while ((i = text.indexOf(pattern, i)) != -1)
//        {
//            i += pattern.length();
//            count++;
//        }
//        return count;
//    }
//
//    public static String ToXMLSafeString(String theString)
//    {
//        return theString.trim().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&apos;");
//    }
//
//    public static boolean TryParseInt32(String theString)
//    {
//        try {  
//            Integer.parseInt(theString);  
//            return true;  
//         } catch (NumberFormatException e) {  
//            return false;  
//         }  
//    }
//
//    /* gotta figure out if this is necessary in MySQL
//    public static String ToSQLDateString(String text)
//    {
//        return "CONVERT(datetime, '" + text + "')"; 
//    }
//    */
//
//    public static String ToSQLSingleQuoteStringOrNull(String text)
//    {
//        if (text == null || text.trim().equals(""))
//        {
//            return "NULL";
//        }
//        else
//        {
//            return "'" + text.replace("'", "''") + "'";
//        }
//    }
//    
//
//
//
//    public static String ToFormattedPhoneNumber(String text)
//    {
//        String number = NumbersOnly(text);
//
//        if(number.length() == 10)
//        {
//            number = "(" + number.substring(0, 3) + ") " + number.substring(3, 6) + "-" + number.substring(6);
//        }
//        else if (number.length() == 11 && number.subSequence(0,1).equals("1"))
//        {
//            number = number.substring(0, 1) + " (" + number.substring(1, 3) + ") " + number.substring(4, 3) + "-" + number.substring(7);
//        }
//
//        return number;
//    }
//
//
//
//    /// <summary>
//    /// Turns a sentence String into PascalCase.
//    /// Example: "a big red dog" -> "ABigRedDog".
//    /// </summary>
//    public static String PascalCase(String text)
//    {
//        String[] words = text.trim().split(" ");
//        String outputWord = "";
//
//        for (String word : words)
//        {
//            outputWord += word.substring(0, 1).toUpperCase() + word.substring(1);
//        }
//
//        return outputWord;
    // }
//    @formatter:on
}
