package com.uttara.project;

class CategoryAlreadExistException extends Exception
{
	public CategoryAlreadExistException(String message)
	{
		super(message);
	}
}
class InvalidCategoryNameException extends Exception
{
	public InvalidCategoryNameException(String message)
	{
		super(message);
	}
}
class CategoryNotExistException extends Exception
{
	public CategoryNotExistException(String message) 
	{
		super(message);
	}
}
class TitleAlreadyExistException extends Exception
{
	public TitleAlreadyExistException(String message) 
	{
		super(message);
	}
}
class InvalidRemainderDateException extends Exception
{
	public InvalidRemainderDateException(String message) 
	{
		super(message);
	}
}
class TitleDoesNotExistException extends Exception
{
	public TitleDoesNotExistException(String message)
	{
		super(message);
	}
}