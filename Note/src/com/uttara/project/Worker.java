package com.uttara.project;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class Worker extends TimerTask
{
	private static List<String> assingTaskList;
	

	@Override
	public void run()
	{
		NoteModel.getTodayNotes();
		try
		{
			Runtime.getRuntime().exec("cls/clear"); //to clear the screen
			System.out.print("\033[H\033[2J");
			System.out.flush();
		}
		catch (IOException e)
		{
			Logger.getInstance().writeLog(new Date().toString()+":"+e.getMessage());
		}
		//System.out.println();
		//System.out.println();
		System.out.println("Remainder is running...............");
		System.out.println();
		if(assingTaskList==null||assingTaskList.size()==0)
			System.out.println("You don't have any today's task");
		else 
		{
			for(String s:assingTaskList)
				System.out.println(s);
			assingTaskList.clear();
		}
		System.out.println();
		System.out.println("Remainder will remind you again after 2 min.......");
		System.out.println();
		System.out.println();
	}
	public synchronized static void setAssingTaskList(List<String> list)
	{
		if(list==null)
			throw new IllegalArgumentException("Null assinged as task");
		assingTaskList=list;
	}

}
