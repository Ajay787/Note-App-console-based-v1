package com.uttara.project;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class NoteBeans implements Serializable,Comparable<NoteBeans>
{
	private String categoryName;
	private String noteTitle;
	private String noteDescription;
	private String noteTag;
	private Status noteStatus;
	private Date remainderDate;
	private String search;
	private List<String> categoryList;
	private List<String> titleList;
	private String emailId;
	private String mailSubject;
	private String mailMessage;
	
	
	public String getEmailId()
	{
		return emailId;
	}

	public void setEmailId(String emailId)
	{
		this.emailId = emailId;
	}

	public String getMailSubject()
	{
		return mailSubject;
	}

	public void setMailSubject(String mailSubject)
	{
		this.mailSubject = mailSubject;
	}

	public String getMailMessage()
	{
		return mailMessage;
	}

	public void setMailMessage(String mailMessage)
	{
		this.mailMessage = mailMessage;
	}

	public String getSearch()
	{
		return search;
	}

	public void setSearch(String search)
	{
		this.search = search;
	}

	public List<String> getCategoryList()
	{
		return categoryList;
	}

	public void setCategoryList(List<String> categoryList)
	{
		this.categoryList = categoryList;
	}

	public List<String> getTitleList()
	{
		return titleList;
	}

	public void setTitleList(List<String> titleList)
	{
		this.titleList = titleList;
	}

	public NoteBeans() {}

	public String getCategoryName()
	{
		return categoryName;
	}

	public void setCategoryName(String category)
	{
		this.categoryName = category;
	}

	public String getNoteTitle()
	{
		return noteTitle;
	}

	public void setNoteTitle(String noteTitle)
	{
		this.noteTitle = noteTitle;
	}

	public String getNoteDescription()
	{
		return noteDescription;
	}

	public void setNoteDescription(String noteDescription)
	{
		this.noteDescription = noteDescription;
	}

	public String getNoteTag()
	{
		return noteTag;
	}

	public void setNoteTag(String noteTag)
	{
		this.noteTag = noteTag;
	}

	public Status getNoteStatus()
	{
		return noteStatus;
	}

	public void setNoteStatus(Status noteStatus)
	{
		this.noteStatus = noteStatus;
	}

	public Date getRemainderDate()
	{
		return remainderDate;
	}

	public void setRemainderDate(Date remainderDate)
	{
		this.remainderDate = remainderDate;
	}
	
	@Override
	public String toString()
	{
		
		return categoryName+" "+noteTitle;
	}
	@Override
	public int hashCode()
	{
		
		return (categoryName+noteTag+noteTitle+noteDescription+noteStatus).hashCode();
	}
	
	@Override
	public int compareTo(NoteBeans o)
	{
		
		return 0;
	}
	@Override
	public boolean equals(Object obj)
	{
		String string=(String) obj;
		return string.equals(this.noteTitle);
	}
	
}
