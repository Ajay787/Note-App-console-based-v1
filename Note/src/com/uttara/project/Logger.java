package com.uttara.project;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger
{
	private Logger() {}
	
	private static Logger instance=null;
	
	public static Logger getInstance()
	{
		if(instance==null)
		{
			instance=new Logger();
		}
		return instance;
	}
	public synchronized void writeLog(String data)
	{
		BufferedWriter bufferedWriter=null;
		try
		{
			bufferedWriter=new BufferedWriter(new FileWriter("logFile.log",true));
			bufferedWriter.newLine();
			bufferedWriter.write(data);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(bufferedWriter!=null)
				bufferedWriter.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
