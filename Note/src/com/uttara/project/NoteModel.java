package com.uttara.project;

import java.io.*;
import java.text.*;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import com.itextpdf.text.*;
import com.itextpdf.text.log.SysoCounter;
import com.itextpdf.text.pdf.*;

import javax.mail.*;
import javax.mail.internet.*;

public class NoteModel
{
	public NoteModel(){} //no args constructor
	
	public NoteModel(boolean startTheTimer)
	{
		if(startTheTimer)     //to start the timer
		{
			List<String> listofNote=new LinkedList<String>();
			Timer timer=new Timer();
			timer.schedule(new Worker(), 1,120000);
		}
	}
	
	private static boolean noteListContainsTodayDate(List<String> list)
	{
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MMM/yyyy");
		Date date=new Date();
		String todayDate=simpleDateFormat.format(date);			//getting today's date and formatting it
		todayDate=todayDate.trim();
		
		
		Iterator<String> iterator=list.iterator();
		String iString,gettingReqiredRemainderDate;
		while(iterator.hasNext())
		{
			iString=iterator.next();
			if(iString.trim().startsWith("NOTE_REMAINDER:"))
			{
			
				
				iString=iString.trim().replaceAll("NOTE_REMAINDER:", "");
				gettingReqiredRemainderDate=iString.substring(8, 10)+"/"+iString.substring(4,7)+"/"+iString.substring(24, iString.length());
				gettingReqiredRemainderDate=gettingReqiredRemainderDate.trim();
				//System.out.println("Today's date"+todayDate+"  remainder date"+gettingReqiredRemainderDate);
				if(gettingReqiredRemainderDate.equals(todayDate))
					return true;
				
			}
			
		}
		return false;
	}
	
	public static void getTodayNotes()
	{
		int count=1;
		BufferedReader bufferedReader=null;
		File file=new File("");
		File directory=new File(file.getAbsolutePath());
		File[] files=directory.listFiles();
		List<String> finalList=new LinkedList<String>();
		List<String> checkList=new LinkedList<String>();
		String iString;
		boolean flag=false;
		for(File loopFile:files)
		{
			if(loopFile.getName().endsWith(".cat"))
				try
				{
					bufferedReader=new BufferedReader(new FileReader(loopFile));
					while((iString=bufferedReader.readLine()) != null)
					{
						if(iString.startsWith("#NOTE_TITLE:"))
						{
							flag=true;
						}
						if (iString.trim().equals("@endNote"))
						{
							
							flag=false;
							boolean isValid=noteListContainsTodayDate(checkList);
							if(!isValid)
							{
								checkList.clear();
							}
							else
							{	
								
								finalList.add("SL.NO.: "+count+" of today's remainder.....");
								count++;
								finalList.addAll(checkList);  //if note has today's date then adding to final list
								finalList.add("");
								checkList.clear();
							}
							
						}
						if(flag)   //adding each note in to checklist 
						{
							checkList.add(iString);
						}
						
						
					}
					
				}
				catch ( IOException e)
				{
					Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());

				}
				finally
				{
					if(bufferedReader!=null)
						try
						{
							bufferedReader.close();
						} catch (IOException e)
						{
							Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
						}
				}
		}
		
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MMM/yyyy");
		Date date=new Date();
		String todayDate=simpleDateFormat.format(date);
		
		
		
		if(finalList.size()==0)
		{
			finalList.add("You don't have any remainder for today ("+todayDate+")");
			finalList.add("");
		}
		else
		{
			finalList.add(0,"Your all remainder for today ("+todayDate+") are....");
			finalList.add(1,"");
		}
		Worker.setAssingTaskList(finalList);	
		
	}
	
	
	
	public static boolean categoryExists(String categoryName)
	{
		File file=new File(categoryName+".cat");
		if(file.exists())
			return true;
		return false;
	}
	public static boolean titleExits(String title,String categoryName)
	{
		title=title.toLowerCase();
		File file=new File(categoryName+".cat");
		List<String> list=new LinkedList<String>(); 
		BufferedReader bufferedReader=null;
		StringBuilder allTitle=new StringBuilder();
		try
		{
			bufferedReader=new BufferedReader(new FileReader(file));
			String iString;
			while((iString=bufferedReader.readLine())!=null)
			{
				if(iString.contains("#NOTE_TITLE:"))
					allTitle.append(iString.trim());
			}
			
			list.addAll(Arrays.asList(allTitle.toString().toLowerCase().split("#note_title:")));
			if(list.contains(title))
				return true;
			return false;
		}
		catch (IOException e)
		{
			Logger logger=Logger.getInstance();
			logger.writeLog((new Date()).toString()+" "+e.getMessage());
			return false;
		}
		finally 
		{
			if(bufferedReader!=null)
				try
				{
					bufferedReader.close();
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
		}
	}
	public static boolean isValidForFutureDate(Date indate)
	{
		Calendar calendar=Calendar.getInstance();
		int date=calendar.get(Calendar.DATE);
		int month=calendar.get(Calendar.MONTH);
		int year=calendar.get(Calendar.YEAR);
		String todayDateString=""+date+"/"+(month+1)+"/"+year;		
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
		try
		{
			Date today=simpleDateFormat.parse(todayDateString);
			int comp=today.compareTo(indate);
			if(comp<=0)
				return true;
		} catch (ParseException e)
		{
			Logger.getInstance().writeLog(new Date()+":"+e.getMessage());
		}
		return false;
	}
	
	public void addCategory(NoteBeans noteBeans) throws CategoryAlreadExistException,InvalidCategoryNameException
	{
		
		String categoryName=noteBeans.getCategoryName().trim();
		if(categoryName.contains(" "))
		{
			throw new InvalidCategoryNameException("Category name should be of one word");
		}

		
		BufferedWriter bufferedWriter=null;
		if (categoryExists(categoryName))
		{
			throw new CategoryAlreadExistException(categoryName+" category already exists...");
		}
		else 
		{
			Date categortyDate=new Date();
			categoryName+=".cat";
			File file=new File(categoryName);
			try
			{
				bufferedWriter=new BufferedWriter(new FileWriter(file));
				bufferedWriter.write("#CATEGORY_DATE_CREATED:"+categortyDate.toString());
				bufferedWriter.newLine();
				bufferedWriter.flush();
				
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally {
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
	
	
	
	public Set<String> getSetOfCategories()
	{
		File temp=new File(".");
		File categoryDirectory =new File(temp.getAbsolutePath());
		File[] files=categoryDirectory.listFiles();
		Set<String> setOfCategory=new TreeSet<String>();
		for(File file:files)
		{
			if(file.getName().endsWith(".cat"))
				setOfCategory.add(file.getName().replace(".cat", ""));
				
		}
		return setOfCategory;
	}
	
	
	public List<String> getAllNotesDetailsOfCategory(NoteBeans noteBeans) throws CategoryNotExistException
	{
		String categoryName=noteBeans.getCategoryName();
		if(!categoryExists(categoryName))
			throw new CategoryNotExistException("Category not exist");
		List<String> allNotes=new LinkedList<String>();
		
		
		File file=new File(categoryName+".cat");
		BufferedReader bufferedReader=null;
		String string;
		try
		{
			bufferedReader=new BufferedReader(new FileReader(file));
			while((string=bufferedReader.readLine())!=null)
			{
				string=string.replace("#CATEGORY_DATE_CREATED:", "Category "+categoryName+" was created on:  ");
				string=string.replace("#NOTE_TITLE:", "Note Title:  ").replace("NOTE_DESCRIPTION:", "Description:  ").replace("NOTE_TAG:", "TAG:  ");				
				string=string.replace("NOTE_REMAINDER:", "Remainder Date:  ").replace("NOTE_STATUS:", "Status:  ");
				string=string.replace("NOTE_CREATED_ON:", "Created on:  ").replace("@endNote", "");
				allNotes.add(string);
				
				
			}
		}
		catch (IOException e)
		{
			Logger.getInstance().writeLog((new Date()).toString()+e.getMessage());
		}
		finally
		{
			if(bufferedReader!=null)
				try
				{
					bufferedReader.close();
				} catch (IOException e)
				{
					Logger.getInstance().writeLog((new Date()).toString()+e.getMessage());
				}
		}
		if(allNotes.size()==1)
		{
			allNotes.add("");
			allNotes.add("No note added to this category");
		}
			
				
		return allNotes;
	}
	
	public void addNote(NoteBeans noteBeans)throws CategoryNotExistException,TitleAlreadyExistException,InvalidRemainderDateException
	{
		String categoryName=noteBeans.getCategoryName().trim();
		String noteTitle=noteBeans.getNoteTitle().trim();
		if(!categoryExists(categoryName))
		{
			throw new CategoryNotExistException(categoryName +" doesnot Exist");
		}
		if(titleExits(noteTitle, categoryName))
		{
			throw new TitleAlreadyExistException("Title Already Exist");
		}
		String description=noteBeans.getNoteDescription();
		String noteTag=noteBeans.getNoteTag();
		Date remainderDate=noteBeans.getRemainderDate();
		if(remainderDate!=null)
		{
			if(!NoteModel.isValidForFutureDate(remainderDate))
			{
				throw new InvalidRemainderDateException("INVALID DATE... RAMAINDER CAN BE SET FOR FUTURE, TODAY IS ALLOWED FOR TESTING PURPOSE...");
			}
		}
		
		
		
		File file=new File(categoryName+".cat");
		BufferedWriter bufferedWriter=null;
		try
		{
			bufferedWriter=new BufferedWriter(new FileWriter(file, true));
			bufferedWriter.newLine();
			bufferedWriter.write("#NOTE_TITLE:"+noteTitle);
			bufferedWriter.newLine();
			bufferedWriter.write("NOTE_DESCRIPTION:"+description);
			bufferedWriter.newLine();
			bufferedWriter.write("NOTE_TAG:"+noteTag);
			bufferedWriter.newLine();
			bufferedWriter.write("NOTE_STATUS:"+noteBeans.getNoteStatus());
			bufferedWriter.newLine();
			if(remainderDate!=null)
			{
				bufferedWriter.write("NOTE_REMAINDER:"+remainderDate);
				bufferedWriter.newLine();
			}			
			bufferedWriter.write("NOTE_CREATED_ON:"+(new Date()).toString());
			bufferedWriter.newLine();
			bufferedWriter.write("@endNote");
			bufferedWriter.newLine();
			bufferedWriter.flush();
		}
		catch (IOException e)
		{
			// TODO: handle exception
		}
		finally
		{
			if(bufferedWriter!=null)
				try
				{
					bufferedWriter.close();
				}
				catch (IOException e)
				{
					Logger logger=Logger.getInstance();
					logger.writeLog((new Date()).toString()+" "+e.getMessage());
				}
				
		}
	}
	
	
	public NoteBeans searchCategory(NoteBeans noteBeans)
	{
		String categoryPattern =noteBeans.getSearch();
		File file=new File("");
		String path=file.getAbsolutePath();
		File currentFolder=new File(path);
		List<String> categoryMatches=new LinkedList<String>();
		
		File[] files=currentFolder.listFiles();
		
		for(File file2:files)
		{
			if(file2.getName().contains(categoryPattern)&&file2.getName().endsWith("cat"))
				categoryMatches.add(file2.getName().replaceAll(".cat", ""));
				
		}
		NoteBeans noteResult=new NoteBeans();
		noteResult.setCategoryList(categoryMatches);
		return noteResult;
	}
	
	
	public NoteBeans searchTitle(NoteBeans noteBeans)
	{
		List<String> titleMatches=new LinkedList<String>();
		File file=new File("");
		String path=file.getAbsolutePath();
		File currentFolder=new File(path);
		File[] files=currentFolder.listFiles();
		String titlePattern=noteBeans.getSearch().toLowerCase();
		for(File file2:files)
		{
			if(file2.getName().endsWith("cat"))
			{
				BufferedReader bufferedReader=null;
				try
				{
					bufferedReader=new BufferedReader(new FileReader(file2));
					String iString;
					while((iString=bufferedReader.readLine())!=null)
					{
						if(iString.contains("#NOTE_TITLE:"))
							if(iString.toLowerCase().contains(titlePattern))
								titleMatches.add(iString.replace("#NOTE_TITLE:" ,""));
					}
					
					
				} 
				catch (IOException e)
				{
					Logger logger=Logger.getInstance();
					logger.writeLog((new Date()).toString()+":"+e.getMessage());
				}
				finally
				{
					if(bufferedReader!=null)
					{
						try
						{
							bufferedReader.close();
						} catch (IOException e)
						{
							Logger logger=Logger.getInstance();
							logger.writeLog((new Date()).toString()+":"+e.getMessage());
						}
					}
				}
			}
				
				
		}
		NoteBeans noteResult=new NoteBeans();
		noteResult.setTitleList(titleMatches);
		return noteResult;
		
	}
			
	public void changeCategoryName(NoteBeans noteBeans,String newCategoryName) throws InvalidCategoryNameException,CategoryNotExistException,CategoryAlreadExistException
	{
		String oldName=noteBeans.getCategoryName().trim();
		if(newCategoryName.contains(" "))
		{
			throw new InvalidCategoryNameException("Category name should be of one word");
		}
		if(!categoryExists(oldName))
		{
			throw new CategoryNotExistException(oldName +" doesnot Exist");
		}
		if (categoryExists(newCategoryName))
		{
			throw new CategoryAlreadExistException(newCategoryName+" category already exists...");
		}
		File oldFile=new File(oldName+".cat");
		File newFile=new File(newCategoryName+".cat");
		oldFile.renameTo(newFile);

	}

	public void changeTitle(NoteBeans noteBeans,String newTitle) throws TitleAlreadyExistException,TitleDoesNotExistException,CategoryNotExistException
	{
		String categoryName=noteBeans.getCategoryName();
		String oldTitle=noteBeans.getNoteTitle();
		if(!categoryExists(categoryName))
		{
			throw new CategoryNotExistException(categoryName+" category doesnot exist");
		}
		if (!titleExits(oldTitle, categoryName))
		{
			throw new TitleDoesNotExistException(oldTitle +"  old title doesNotExist");
		}
		
		if (titleExits(newTitle, categoryName))
		{
			throw new TitleAlreadyExistException(newTitle +"  new title already exist...");
		}
		String oldMessage="#NOTE_TITLE:"+oldTitle;
		String newMessage="#NOTE_TITLE:"+newTitle;
		List<String> list=new LinkedList<String>();
		
		
		BufferedReader bufferedReader=null;
		try
		{
			bufferedReader=new BufferedReader(new FileReader(categoryName+".cat"));
			String iString;
			while((iString=bufferedReader.readLine())!=null)
			{
				list.add(iString.trim());
			}
		}
		catch (IOException e) 
		{
			Logger logger=Logger.getInstance();
			logger.writeLog((new Date()).toString()+":"+e.getMessage());
		}
		finally
		{
			if(bufferedReader!=null)
				try
				{
					bufferedReader.close();
				} catch (IOException e)
				{
					Logger logger=Logger.getInstance();
					logger.writeLog((new Date()).toString()+":"+e.getMessage());
				}
		}
		
		BufferedWriter bufferedWriter=null;
		Iterator<String> iterator=list.iterator();
		int count=0;
		
		try
		{
			bufferedWriter=new BufferedWriter(new FileWriter(categoryName+".cat"));
				while(iterator.hasNext())
				{
					String string=iterator.next();
					count++;
					
					if(string.trim().equals(oldMessage))
					{
						iterator.remove();
						
						bufferedWriter.write(newMessage);
						bufferedWriter.newLine();
						continue;
					}
					bufferedWriter.write(string);
					bufferedWriter.newLine();
							
				}
				bufferedWriter.flush();
			
		}
		catch (IOException e) 
		{
			Logger logger=Logger.getInstance();
			logger.writeLog((new Date()).toString()+":"+e.getMessage());
		}
		finally
		{
			if(bufferedWriter!=null)
				try
				{
					bufferedWriter.close();
				} catch (IOException e)
				{
					Logger logger=Logger.getInstance();
					logger.writeLog((new Date()).toString()+":"+e.getMessage());
				}
		}
		
	}



	public void changeDescription(NoteBeans noteBeans,String newDescription) throws TitleDoesNotExistException,CategoryNotExistException
	{
		String categoryName=noteBeans.getCategoryName();
		String title=noteBeans.getNoteTitle();
		if(!categoryExists(categoryName))
		{
			throw new CategoryNotExistException(categoryName+" category doesnot exist");
		}
		if (!titleExits(title, categoryName))
		{
			throw new TitleDoesNotExistException(title +"  title doesNotExist");
		}
		
	
		String searchMessage="#NOTE_TITLE:"+title;
		String newMessage="NOTE_DESCRIPTION:"+newDescription;
		List<String> list=new LinkedList<String>();
		
		
		BufferedReader bufferedReader=null;
		try
		{
			bufferedReader=new BufferedReader(new FileReader(categoryName+".cat"));
			String iString;
			while((iString=bufferedReader.readLine())!=null)
			{
				list.add(iString.trim());
			}
		}
		catch (IOException e) 
		{
			Logger logger=Logger.getInstance();
			logger.writeLog((new Date()).toString()+":"+e.getMessage());
		}
		finally
		{
			if(bufferedReader!=null)
				try
				{
					bufferedReader.close();
				} 
				catch (IOException e)
				{
					Logger logger=Logger.getInstance();
					logger.writeLog((new Date()).toString()+":"+e.getMessage());
				}
		}
		
		BufferedWriter bufferedWriter=null;
		Iterator<String> iterator=list.iterator();
		boolean titleFound=false;
		int count=0;
		while(iterator.hasNext())   //doing modification of description in list
		{
			count++;
			String iString=iterator.next();
			if(iString.equals(searchMessage))
			{
				titleFound=true;
			}
			if (titleFound)
			{
				if (iString.contains("NOTE_DESCRIPTION"))
				{
					iterator.remove();
					list.add(count-1, newMessage);
					break;
				}
			}		
		}
		iterator=list.iterator();
		
		
			try
			{
				bufferedWriter=new BufferedWriter(new FileWriter(categoryName+".cat"));
				while(iterator.hasNext())
				{
					bufferedWriter.write(iterator.next().trim());
					bufferedWriter.newLine();
					bufferedWriter.flush();
				}
			}
			catch (IOException e)
			{
				Logger logger=Logger.getInstance();
				logger.writeLog((new Date()).toString()+":"+e.getMessage());
			}
			finally 
			{
				if(bufferedWriter!=null)
					try
					{
						bufferedWriter.close();
					} catch (IOException e)
					{
						Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
					}
			}	
	}

	
	public void removeCategory(NoteBeans noteBeans) throws CategoryNotExistException
	{
		String categoryName=noteBeans.getCategoryName();
		if (!categoryExists(categoryName))
		{
			throw new CategoryNotExistException("Invalid category name. "+categoryName+ "  doesnot exists.");
		}
		File file=new File(categoryName+".cat");
		file.delete();
	}
	
	
	
	public void removeNote(NoteBeans noteBeans) throws TitleDoesNotExistException,CategoryNotExistException
	{
		String categoryName,noteTitle;
		categoryName=noteBeans.getCategoryName();
		noteTitle=noteBeans.getNoteTitle();
		String titleSearch="#NOTE_TITLE:"+noteTitle;
		
		if(!categoryExists(categoryName))
			throw new CategoryNotExistException(" Category:"+categoryName + " DoesNot Exist");
		
		if (!titleExits(noteTitle, categoryName))
			throw new TitleDoesNotExistException("Note" +noteTitle+" does not exist");
	
		BufferedWriter bufferedWriter=null;
		BufferedReader bufferedReader=null;
		List<String> list=new LinkedList<String>();
		
		try
		{
			bufferedReader=new BufferedReader(new FileReader(categoryName+".cat"));
			String iString;
			while((iString=bufferedReader.readLine())!=null)
			{
				list.add(iString.trim());
			}
		}
		catch (IOException e)
		{
			Logger.getInstance().writeLog(((new Date()).toString()+":"+e.getMessage()));
		}
		finally {
			if(bufferedReader!=null)
				try
				{
					bufferedReader.close();
				} catch (IOException e)
				{
					Logger.getInstance().writeLog(((new Date()).toString()+":"+e.getMessage()));
				}
		}
		
		Iterator<String> iterator =list.iterator();
		boolean flag=false;
		while(iterator.hasNext())
		{
			String read=iterator.next();
			if(titleSearch.equals(read))
			{
				iterator.remove();
				flag=true;
				continue;
				
			}
			if(flag)
			{
				iterator.remove();			
			}
			if(read.equals("@endNote")&&flag)
			{
				flag=false;
			}
		}
		
		
			try
			{
				bufferedWriter=new BufferedWriter(new FileWriter(categoryName+".cat"));
				for(String string:list)
				{
					bufferedWriter.write(string.trim());
					bufferedWriter.newLine();
					bufferedWriter.flush();
				}
			}
			catch (IOException e)
			{
				Logger.getInstance().writeLog(((new Date()).toString()+":"+e.getMessage()));
			}
			finally
			{
				if(bufferedWriter!=null)
					try
					{
						bufferedWriter.close();
					} 
					catch (IOException e)
					{
						Logger.getInstance().writeLog(((new Date()).toString()+":"+e.getMessage()));
					}
			}
		
	
	}
	
	
	public void generatePdf(NoteBeans noteBeans) throws CategoryNotExistException
	{
		String categoryName=noteBeans.getCategoryName();
		if(!categoryExists(categoryName))
			throw new CategoryNotExistException(categoryName+" not found");
		List<String> list=getAllNotesDetailsOfCategory(noteBeans);
		Document document=null;
		
		try
		{
			document=new Document(PageSize.A5,50,50,50,50);
			PdfWriter.getInstance(document, new FileOutputStream(categoryName+".pdf"));
			document.open();
			
			Paragraph category = new Paragraph(categoryName.toUpperCase(),  FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLDITALIC, new CMYKColor(0, 255, 255,17)));
			Chapter chapter = new Chapter(category, 1);
			chapter.setNumberDepth(0); //Setting number depth to 0 will not display the chapter number on page
			document.add(chapter);
			
						
			for(String string:list)
			{
				if(string.contains("Note Title:"))
					document.add(new Paragraph("  "));
				document.add(new Paragraph(string));
			}
			
			document.add(new Paragraph("  "));
			
		} 
		catch (FileNotFoundException | DocumentException e)
		{
			Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
		}
		finally 
		{
			if(document!=null)
				document.close();
		}
			
	}
	
	
	
	public void generateXls(NoteBeans noteBeans) throws CategoryNotExistException
	{
		String categoryName=noteBeans.getCategoryName();
		if(!categoryExists(categoryName))
			throw new CategoryNotExistException(categoryName+" not found");
		List<String> list=getAllNotesDetailsOfCategory(noteBeans);
		
		
		FileOutputStream fileOutputStream=null;
		
		HSSFWorkbook workbook=null;
		HSSFRow row=null;
		
		try
		{
			workbook=new HSSFWorkbook();
			HSSFSheet sheet=workbook.createSheet();
			sheet.setColumnWidth(0, 8000);  //NOTE_TITLE
			sheet.setColumnWidth(1, 15000);	//NOTE_DESCRIPTION
			sheet.setColumnWidth(2, 8000);	//NOTE_TAG
			sheet.setColumnWidth(3, 6000);	//NOTE_STATUS
			sheet.setColumnWidth(4, 10000);  //NOTE_REMAINDER
			sheet.setColumnWidth(5, 10000);  //NOTE_CREATED_ON
			
			
			
			HSSFFont font = workbook.createFont();				//defining the font for category
			font.setFontHeightInPoints((short) 30);
			font.setFontName("Helvica");
			font.setBold(true);
			font.setItalic(true);		      
			font.setColor(HSSFColor.BLUE_GREY.index);
			HSSFCellStyle styleCategory = workbook.createCellStyle();
			styleCategory.setFont(font);						      //Setting font into style
			
			
			row=sheet.createRow((short)0);			
			row.setHeight((short) 760);
			HSSFCell cell=row.createCell(1);			
			cell.setCellValue(categoryName.toUpperCase());
			cell.setCellStyle(styleCategory);						//setting font for category name

			
			
			
			font = workbook.createFont();							//defining the font for category creation date
			font.setFontHeightInPoints((short) 14);
		    font.setFontName("Helvica");
		    font.setItalic(true);		      
		    font.setColor(HSSFColor.BLUE_GREY.index);
		    HSSFCellStyle creationStyle = workbook.createCellStyle();
		    creationStyle.setFont(font);								//Set font into style
			

			
			row=sheet.createRow((short)1);			
			row.setHeight((short) 400);
			cell=row.createCell(1);	
			String dateCreated=list.get(0);
			cell.setCellValue(dateCreated);
			cell.setCellStyle(creationStyle);	
			
			
			
			font = workbook.createFont();							//defining the font for heading
			font.setFontHeightInPoints((short) 16);
			font.setFontName("Helvica");
			font.setItalic(true);		      
			font.setColor(HSSFColor.BROWN.index);
			HSSFCellStyle styleHead = workbook.createCellStyle();
			styleHead.setFont(font);								//Set font into style
			
			
			row=sheet.createRow(4);
			
			
			row.setHeight((short) 400);
			cell=row.createCell(0);
			cell.setCellValue("NOTE_TITLE");
			cell.setCellStyle(styleHead);
			
			row.setHeight((short) 400);
			cell=row.createCell(1);
			cell.setCellValue("NOTE_DESCRIPTION");
			cell.setCellStyle(styleHead);
			
			row.setHeight((short) 400);
			cell=row.createCell(2);
			cell.setCellValue("NOTE_TAG");
			cell.setCellStyle(styleHead);
			
			row.setHeight((short) 400);
			cell=row.createCell(3);
			cell.setCellValue("NOTE_STATUS");
			cell.setCellStyle(styleHead);
			
			row.setHeight((short) 400);
			cell=row.createCell(4);
			cell.setCellValue("NOTE_REMAINDER_DATE");
			cell.setCellStyle(styleHead);
			
			row.setHeight((short) 400);
			cell=row.createCell(5);
			cell.setCellValue("NOTE_CREATED_ON");
			cell.setCellStyle(styleHead);
			
			
			font = workbook.createFont();					//defining the font for values
			font.setFontHeightInPoints((short) 15);
			font.setFontName("TimesNewRoman");
			font.setBold(false);
			//font.setItalic(true);		      
			font.setColor(HSSFColor.BLACK.index);
			//Set font into style
			HSSFCellStyle styleValue = workbook.createCellStyle();
			styleValue.setFont(font);
		
			//adding the values
			
			Iterator<String> iterator=list.iterator();
			int rowCount=6;   //till 3 is heading
			
			boolean flag=true;
			
			while(iterator.hasNext())
			{
				String iString=iterator.next();
				
				if(flag)
				{
					row=sheet.createRow(rowCount);
					flag=!flag;
				}
				
				if(iString.contains("Note Title:"))
				{
					row.setHeight((short) 310);
					cell=row.createCell(0);
					cell.setCellValue(iString.replaceAll("Note Title:", ""));
					cell.setCellStyle(styleValue);
				}
				
				if(iString.startsWith("Description:"))
				{
					row.setHeight((short) 310);
					cell=row.createCell(1);
					cell.setCellValue(iString.replaceAll("Description:", ""));
					cell.setCellStyle(styleValue);
				}
				if(iString.contains("TAG:"))
				{
					row.setHeight((short) 310);
					cell=row.createCell(2);
					cell.setCellValue(iString.replaceAll("TAG:", ""));
					cell.setCellStyle(styleValue);
				}
				
				if(iString.contains("Status:"))
				{
					row.setHeight((short) 310);
					cell=row.createCell(3);
					cell.setCellValue(iString.replaceAll("Status:", ""));
					cell.setCellStyle(styleValue);
				}
				
				if(iString.contains("Remainder Date:"))
				{
					row.setHeight((short) 310);
					cell=row.createCell(4);
					cell.setCellValue(iString.replaceAll("Remainder Date:", ""));
					cell.setCellStyle(styleValue);
				}
				if(iString.contains("Created on:"))
				{
					row.setHeight((short) 310);
					cell=row.createCell(5);
					cell.setCellValue(iString.replaceAll("Created on:", ""));
					cell.setCellStyle(styleValue);
					flag=!flag;
					rowCount++;
				}
				
			}
			
			
			
		
			
			
			
			fileOutputStream=new FileOutputStream(categoryName+".xls");
			workbook.write(fileOutputStream);
			
			
		} catch (IOException e)
		{
			Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
		}
		finally
		{
			if(workbook!=null)
				try
				{
					workbook.close();
				} catch (IOException e)
				{
					Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
				}
			if(fileOutputStream!=null)
				try
				{
					fileOutputStream.close();
				} catch (IOException e)
				{
					Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
				}
		}
				
	}
	
	
	public boolean sendMail(NoteBeans noteBeans)
	{
		String toEmailId=noteBeans.getEmailId();
		String subject=noteBeans.getMailSubject();
		String message=noteBeans.getMailMessage();
		BufferedReader bufferedReader=null;
		String password=null;
		String fromEmailId=null;
		String read;
		try
		{
			bufferedReader=new BufferedReader(new FileReader("config.txt"));
			while((read=bufferedReader.readLine())!=null)
			{
				if(read.startsWith("email-id:"))
				{
					fromEmailId=read.replaceAll("email-id:", "").trim();
				}
				if(read.startsWith("email-password:"))
				{
					password=read.replaceAll("email-password:", "").trim();
				}
			}
		}
		catch ( IOException e)
		{
			Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
		}
		finally
		{
			if(bufferedReader!=null)
				try
				{
					bufferedReader.close();
				} catch (IOException e)
				{
					Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
				}
		}
		
		//System.out.println(fromEmailId);
		//System.out.println(toEmailId);
		//System.out.println(password);
		
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		Properties properties=new Properties();
		properties.setProperty("mail.smtps.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        properties.setProperty("mail.smtps.auth", "true");
		

		Session s = Session.getInstance(properties, null);
		s.setDebug(true);

		MimeMessage mimeMessage = new MimeMessage(s);
		Transport tr =null;

		try {

			InternetAddress from = new InternetAddress(fromEmailId.trim(), "Ajay Kumar");
																				
			InternetAddress to = new InternetAddress(toEmailId.trim());

			mimeMessage.setSentDate(new Date());
			mimeMessage.setFrom(from);
			mimeMessage.addRecipient(Message.RecipientType.TO, to);

			mimeMessage.setSubject(subject);
			mimeMessage.setContent(message, "utf-8");

			tr = s.getTransport("smtp");
			tr.connect("smtp.gmail.com", fromEmailId, password);
			mimeMessage.saveChanges();
			tr.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			return true;
			
		} 
		catch (Exception e) 
		{
			Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
			return false;
		}
		finally
		{
			if(tr!=null)
				try
				{
					tr.close();
				} catch (MessagingException e)
				{
					Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
				}
				
		}
	}
				
}
	
