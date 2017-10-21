package com.uttara.project;

public class Validate
{
	public static boolean isValidateName(String s)
	{
		if(s==null||s.trim().length()<=2)
			return false;
		char c=s.charAt(0);
		if(Character.isDigit(c))
			return false;
		return true;
	}
	
}
