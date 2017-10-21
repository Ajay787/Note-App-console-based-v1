package com.uttara.project;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import java.util.Scanner;
import java.util.Set;

import com.itextpdf.text.log.SysoCounter;


public class StartApp
{
	private static boolean start;   //first extra new line will not be printed
	
	public static void main(String[] args)
	{
		
		int choice=0;
		Scanner scannerChoice=new Scanner(System.in);
				
		try {
						 		
				outter:while(choice!=7)
				{
					if(start)
					System.out.println();
					System.out.println("Note_App_Menu");
					System.out.println("Choose one the option from menu");
					System.out.println("1.To Create a categories");
					System.out.println("2.Load the categories");
					System.out.println("3.Add Note");
					System.out.println("4.Search");
					System.out.println("5.Edit/Remove Categories/Notes");
					System.out.println("6.To export as pdf/excel");
					System.out.println("7.Send an e-mail");
					System.out.println("8.To Exit");
					System.out.println("Note: Remainder will run when press option to exit(select option 8).....");
					System.out.println("Enter your choice...");
					start=true;
					while(!scannerChoice.hasNextInt())
					{
						System.out.println("Invalid input. Try again...");
						scannerChoice.next();
						continue outter;
					}
					choice=scannerChoice.nextInt();
					
					switch (choice)
					{
					case 1:System.out.println("Create categotries");
							createCategories();
						break;

					case 2: System.out.println("Load Categories");
							loadCategories();
						break;
						
					case 3: System.out.println("Add Note");
							addNote();
						break;
						
					case 4:System.out.println("Search");
							search();
					break;
					
					case 5:
							Set<String> setOfCategories=new NoteModel().getSetOfCategories();
						if(setOfCategories.size()==0)
						{
							System.out.println("You have not add not added any category yet...");
							System.out.println("After adding category name you can edit/remove....");
							System.out.println("Redirecting main menu....");
							System.out.println();
							main(null);
						}
						
							Scanner scanner=new Scanner(System.in);
							outter2:while(true)
							{
								System.out.println("Choose the option");
								System.out.println("1.To edit Categories/Notes");
								System.out.println("2.To remove Categories/Notes");
								System.out.println("3. To go back to main menu...");
								System.out.println("Enter your choice...");
								while(!scanner.hasNextInt())
								{
									System.out.println("Invalid input");
									System.out.println("Try again...");
									System.out.println();
									scanner.next();
									continue outter2;
								}
								int choice1=scanner.nextInt();
								switch(choice1)
								{
									case 1:editCategoryNote();
									break;
									case 2: removeCategoryNote();
									break;
									case 3:break outter2;
									default:System.out.println("Invalid option.");
											System.out.println("Try again...");
											System.out.println();
											continue outter2;
								}
							}
							
							
						break;
						
					case 6:System.out.println("export as pdf/excel");
							export();
						break;
						
					case 7:System.out.println("Sent an e-mail");
							sendEmail();
						break;
						
					case 8:System.out.println("Bye");
							new NoteModel(true);				//used to trigger timer in static initializer of NoteModel
						break outter;
					default: System.out.println("Invalid option...");
							System.out.println("Try again...");
						continue outter;
					}
										
				}
						
					
			}
			catch (Throwable t)
			{
			
				Logger.getInstance().writeLog((new Date()).toString()+":"+t.getMessage());
			
			}
	}
	
	
	
	
	public static void createCategories()  //creating new category
	{
		Scanner scanner=new Scanner(System.in);
		String categoryName;
		while(true)
		{
			System.out.println("Enter the new category name");
			categoryName=scanner.nextLine();
			if(Validate.isValidateName(categoryName))
			{
				if(NoteModel.categoryExists(categoryName))
				{
					System.out.println(categoryName+" category already exist");
					System.out.println();
					continue;
				}
				try
				{
					NoteBeans noteBeans=new NoteBeans();
					noteBeans.setCategoryName(categoryName);
					new NoteModel().addCategory(noteBeans);
					System.out.println("Category "+categoryName+" created successfully...");
					break;
				}
				catch (CategoryAlreadExistException|InvalidCategoryNameException e)
				{
					System.out.println(e.getMessage());
					System.out.println();
				}
				
			}
			else
			{
				System.out.println("Invalid category name. Minimum 3 character one word allowed.Shouldn't start with digit..");
				System.out.println("Try Again.... ");
				System.out.println();
			}
		}
				
				
		
	}
	
	
	
	
	public static void loadCategories()  //loading existing categories
	{
		while(true)
		{
			Set<String> setOfCategories=new NoteModel().getSetOfCategories();
			if(setOfCategories.size()==0)
			{
				System.out.println("You have not add not added any category yet...");
				System.out.println("Try after adding categories....");
				System.out.println();
				break;
				
				
			}
			else
			{
				System.out.println("Category added so far...");
				for(String s:setOfCategories)
				{
					System.out.println(s);
				}
			}
			if (setOfCategories.size()>0)
			{
				System.out.println();
				Scanner scanner=new Scanner(System.in);
				System.out.println("Enter the category name");
				String categoryName=scanner.nextLine();
				if (Validate.isValidateName(categoryName))
				{
							
					List<String> list=new LinkedList<String>();
					NoteBeans noteBeans=new NoteBeans();
					noteBeans.setCategoryName(categoryName);
					try
					{
						list=new NoteModel().getAllNotesDetailsOfCategory(noteBeans);
						System.out.println();
						for(String string:list)
							System.out.println(string);
						
						
						try
						{
							Thread.currentThread().sleep(5000);
							System.out.println("Redirecting to menu in 5000ms....");
						}
						catch (InterruptedException e)
						{
							Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
						}
						break;
						
					} 
					catch (CategoryNotExistException e)
					{
						Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
						System.out.println(e.getMessage()+"....");
						System.out.println();
					}
					
				
				}
				else
				{
					System.out.println("Invalid Category name.....");
					System.out.println("Try again....");
					System.out.println();
				}
					
			}
			
		}
			
			
		
	}
	
	
	
	
	public static void addNote()   //creating new note
	{
		outter:while(true)
		{
				Scanner scanner=new Scanner(System.in);
				Scanner scanner2=new Scanner(System.in);
				String noteTitle,noteDescription,categoryName,noteTag;
				Status noteStatus;
				Date remainderDate = null;
				
				Set<String> setOfCategories=new NoteModel().getSetOfCategories();
				if(setOfCategories.size()==0)
				{
					System.out.println("You have not add not added any category yet...");
					System.out.println("After adding category name you can add new note....");
					System.out.println("Redirecting main menu....");
					System.out.println();
					try
					{
						Thread.currentThread();
						Thread.sleep(6000);
					} catch (InterruptedException e)
					{
						Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
					}
					
					return;
				}
				
				
				
				
				while(true)  //for category name
				{
					System.out.println("Enter the existing category name.");
					categoryName=scanner.nextLine();
					if(!Validate.isValidateName(categoryName))
					{
						System.out.println();
						System.out.println("Invalid Category name "+categoryName);
						System.out.println("Try Again.... ");
						System.out.println();
						continue;
					}
					if(NoteModel.categoryExists(categoryName))
					{
						break;
					}
					else
					{
						System.out.println();
						System.out.println(categoryName+" category doesn't exist...");
						System.out.println("Provide a existing category name");
						System.out.println("Try Again.... ");
						System.out.println();
						
					}
				}
				while(true)  //for note title
				{
					System.out.println("Enter the Note title");
					noteTitle=scanner.nextLine();
					if(!Validate.isValidateName(noteTitle))
					{
						System.out.println();
						System.out.println("Invalid Note title "+noteTitle);
						System.out.println("Try Again.... ");
						System.out.println();
						continue;
					}
					if(!NoteModel.titleExits(noteTitle, categoryName))
					{
						break;
					}
					else
					{
						System.out.println();
						System.out.println(noteTitle+" note already exist...");
						System.out.println("Provide a new note title");
						System.out.println("Try Again.... ");
						System.out.println();
						
					}
				}
					
				//note description requires no validation
				System.out.println("Enter the description of "+noteTitle+" note.");
				noteDescription=scanner.nextLine();
				
				
				
			
				while(true) //for note Tag
				{
					System.out.println("Enter note tag. like->first,important,must");
					noteTag=scanner.nextLine();
					if(Validate.isValidateName(noteTag))
					{
						break;
					}
					else
					{
						System.out.println("Invalid note Tag");
						System.out.println("Try Again.... ");
						System.out.println();
					}
				}
				
				inner:while(true)  //to add status
				{
					System.out.println("Choose the note Status");
					System.out.println("1.Started");
					System.out.println("2.InProgress");
					System.out.println("3.Finished");
					System.out.println("You input...");
					while(!scanner2.hasNextInt())
					{
						scanner2.next();
						System.out.println("Invalid input..");
						System.out.println("Try again...");
					}
					int choice=scanner2.nextInt();
					switch (choice)
					{
					case 1:noteStatus=Status.STARTED;
							break inner;
							
					case 2:noteStatus=Status.INPROGRESS;
							break inner;
					
					case 3:noteStatus=Status.FINISHED;
							break inner;
					default:System.out.println("Invalid input...");
							System.out.println("Try again...");
					}
				
				}
				
				
				System.out.println("Want to add remainder to this node");
				System.out.println("If yes,then type Yes/yes or NO/no to ignore");
				String addRemainder=scanner.nextLine();
				if (addRemainder.toLowerCase().equals("yes"))
				{
					inner:while(true)
					{
						System.out.println("Enter the remainder date in form of dd/mm/yyyy");
						System.out.println("Note: FOR TESTING PURPOSE TODAY'S DATE IS ALLOWED...");
						String date=scanner.nextLine();
						SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
						try
						{
							remainderDate=simpleDateFormat.parse(date);
							if(NoteModel.isValidForFutureDate(remainderDate))
								break;
								else
								{
									System.out.println("Invalid date. Remainder can be set for future date only...");
									System.out.println("Note: FOR TESTING PURPOSE TODAY'S DATE IS ALLOWED...");
									System.out.println("Try again...");
									System.out.println();
									continue inner;
								}
							
							
						}
						catch (ParseException e)
						{
							System.out.println(e.getMessage());
						}
					}			
				}
				
				NoteBeans noteBeans=new NoteBeans();
				noteBeans.setCategoryName(categoryName);
				noteBeans.setNoteTitle(noteTitle);
				noteBeans.setNoteDescription(noteDescription);
				noteBeans.setNoteTag(noteTag);
				noteBeans.setNoteStatus(noteStatus);
				noteBeans.setRemainderDate(remainderDate);
				
				try
				{
					new NoteModel().addNote(noteBeans);
					System.out.println("Note "+noteTitle+" created successfully in Category "+categoryName);
					break outter;
				}
				catch (CategoryNotExistException | TitleAlreadyExistException | InvalidRemainderDateException e)
				{
						System.out.println(e.getMessage());
						System.out.println();
				}
		}
		
	}
	
	
	
	public static void search()   //searching new anything -> category,notetitle
	{
		String search;
		Scanner scanner=new Scanner(System.in);
		while(true)
		{
			System.out.println("Enter the part of title/category to search.");
			System.out.println("Enter minimum 3 letters..");
			search=scanner.nextLine();
			if(Validate.isValidateName(search))
			{
				NoteBeans noteBeans=new NoteBeans();
				noteBeans.setSearch(search);
				NoteModel noteModel=new NoteModel();
				NoteBeans categoryBean=noteModel.searchCategory(noteBeans);
				NoteBeans titleBean=noteModel.searchTitle(noteBeans);
				List<String> cList=categoryBean.getCategoryList();
				List<String> tList=titleBean.getTitleList();
				if(cList.size()==0)
				{
					System.out.println("No category found that conatins "+search);
				}
				else
				{
					System.out.println();
					System.out.println("Categories conatining "+search+" are:");
					for(String s:cList)
					System.out.println(s);
				}
				if (tList.size()==0)
				{
					System.out.println("No title found that contains "+search);
				}
				else
				{
					System.out.println();
					System.out.println("Title conatining "+search+" are:");
					for(String s:tList)
					System.out.println(s);
				}
				
				
				
				try  //So that menu display is delayed and user can see output
				{
					System.out.println();
					System.out.println("Redirecting to main menu in 7000ms...");
					Thread.currentThread();
					Thread.sleep(7000);
				} 
				catch (InterruptedException e)
				{
					Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
				}
				
				
				break;
				
			}
			else
			{
				System.out.println("Invalid input..");
				System.out.println("Try Again.... ");
				System.out.println();
			}
		}
		
	}
	
	
	
	
	public static void editCategoryNote()  //edit or remove categories name
	{
		Scanner scanner=new Scanner(System.in);
		Scanner scannerLine=new Scanner(System.in);
		outter:while(true)
		{
			System.out.println("Choose the option");
			System.out.println("1.To edit Category Name");
			System.out.println("2.To edit Note");
			System.out.println("3.Go to main menu");
			while(!scanner.hasNextInt())
			{
				System.out.println("Invalid option....");
				System.out.println("Try again...");
				System.out.println();
				scanner.next();
				continue outter;
			}
			int choice=scanner.nextInt();
			
			switch (choice)
			{
				case 1:inner:while(true)
						{
								String oldCategoryName;
								String newCategoryName;
								System.out.println("Enter the CategoryName you want to modify... ");
								oldCategoryName=scannerLine.nextLine();
								if(!Validate.isValidateName(oldCategoryName))
								{
									System.out.println("Invalid category name");
									System.out.println();
									continue inner;
								}
								if(!NoteModel.categoryExists(oldCategoryName))
								{
									System.out.println(oldCategoryName +" category name doesn't exist...");
									System.out.println("Try again...");
									System.out.println();
									continue inner;
								}
								System.out.println("Enter new cateforyName");
								newCategoryName=scannerLine.nextLine();
								if(!Validate.isValidateName(newCategoryName))
								{
									System.out.println("Invalid category name");
									System.out.println();
									continue inner;
								}
								NoteBeans noteBeans=new NoteBeans();
								noteBeans.setCategoryName(oldCategoryName);
								
								System.out.println("NOTE: All note belonging to this category will belong to new changed category.");
								System.out.println("Do you want to continue... Yes/No");	
								String ans=scanner.next().toLowerCase();
								if(!(ans.equals("yes")||ans.equals("no")))
								{
									System.out.println("Invalid option selected...");
									System.out.println();
									continue inner;
								}								
								if(ans.equals("no"))
									break outter;							
								
								try
								{
									new NoteModel().changeCategoryName(noteBeans, newCategoryName);
									System.out.println("Successfully changed category name.......");
									System.out.println("Going back in 5000millisec.......");
									Thread.currentThread().sleep(5000); //waits for user to output
									continue outter;
								}
								catch (InvalidCategoryNameException | CategoryNotExistException | CategoryAlreadExistException e)
								{
									System.out.println(e.getMessage());
									Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
								} catch (InterruptedException e)
								{
									Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
								} 
						}
							

			
				
				
				case 2:System.out.println("Edit details of note");
					inner:while(true)
					{
						System.out.println("Choose the option...");
						System.out.println("1.To change Title of note");
						System.out.println("2.To change Description of note");
						System.out.println("3.To go back..");
						System.out.println("Enter your option...");
						while(!scanner.hasNextInt())
						{
							System.out.println("Invalid input...");
							System.out.println("Try again....");
							System.out.println();
							scanner.next();
							continue inner;
						}
							
							choice=scanner.nextInt();
							switch (choice)
							{
								case 1:
									inner1:while(true)
									{
											String categoryName,oldTitle,newTitle;
											System.out.println("Enter the category name");
											categoryName=scannerLine.nextLine();
											if(!Validate.isValidateName(categoryName))
											{
												System.out.println("Invalid category name. Minimum name length is 3 without digit at beginning");
												System.out.println();
												continue inner1;
											}
											if(!NoteModel.categoryExists(categoryName))
											{
												System.out.println(categoryName +" category name doesn't exist...");
												System.out.println("Try again...");
												System.out.println();
												continue inner1;
											}
											inneroldtitle:while(true)
											{
												System.out.println("Enter the old note title");
												oldTitle=scannerLine.nextLine();
												if(!Validate.isValidateName(oldTitle))
												{
													System.out.println("Invalid title name. Minimum name length is 3 without digit at beginning");
													System.out.println();
													continue inneroldtitle;
												}
												if(!NoteModel.titleExits(oldTitle, categoryName))
												{
													System.out.println(oldTitle +" note title doesn't exist...");
													System.out.println("Try again...");
													System.out.println();
													continue inneroldtitle;
												}
												break inneroldtitle;
											}
											innerNewTitle:while(true)
											{
												System.out.println("Enter the new title");
												newTitle=scannerLine.nextLine();
												if(!Validate.isValidateName(newTitle))
												{
													System.out.println("Invalid title name. Minimum name length is 3 without digit at beginning");
													System.out.println();
													continue innerNewTitle;
												}
												if(NoteModel.titleExits(newTitle, categoryName))
												{
													System.out.println(newTitle +" already exist...");
													System.out.println("Try again...");
													System.out.println();
													continue innerNewTitle;
												}
												break innerNewTitle;
											}
											NoteBeans noteBeans2=new NoteBeans();
											noteBeans2.setCategoryName(categoryName);
											noteBeans2.setNoteTitle(oldTitle);
										try
										{
											new NoteModel().changeTitle(noteBeans2, newTitle);
											System.out.println("Title successfully changed");
											System.out.println("Going back in 5000ms........");
											Thread.currentThread().sleep(5000);
											System.out.println();
											noteBeans2=null;
											continue outter;
											
										} catch (TitleAlreadyExistException | TitleDoesNotExistException | CategoryNotExistException e)
										{
											System.out.println(e.getMessage());								
											Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
											System.out.println("");
											
										} 
										catch (InterruptedException e)
										{
											Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
										}
									}
								
								
							case 2:
									NoteBeans noteBeans2=null;
									inner2:while(true)
									{
																			
										String newDescription,titleName,categoryName;
										System.out.println("Enter the category name");
										categoryName=scannerLine.nextLine();
										if(!Validate.isValidateName(categoryName))
										{
											System.out.println("Invalid category name. Minimum name length is 3 without digit at beginning");
											System.out.println();
											continue inner2;
										}
										if(!NoteModel.categoryExists(categoryName))
										{
											System.out.println(categoryName +" category name doesn't exist...");
											System.out.println("Try again...");
											System.out.println();
											continue inner2;
										}
										innerTitle:while(true)
										{
											System.out.println("Enter the note title");
											titleName=scannerLine.nextLine();
											if(!Validate.isValidateName(titleName))
											{
												System.out.println("Invalid note title. Minimum title length is 3 without digit at beginning");
												System.out.println();
												continue innerTitle;
											}
											if(!NoteModel.titleExits(titleName, categoryName))
											{
												System.out.println(titleName+" note title doesn't exist...");
												System.out.println("Try again...");
												System.out.println();
												continue innerTitle;
											}
											break innerTitle;
										}
										System.out.println("Enter the new Description");
										newDescription=scannerLine.nextLine();
										noteBeans2=new NoteBeans();
										noteBeans2.setCategoryName(categoryName);
										noteBeans2.setNoteTitle(titleName);
																
										try
										{
											new NoteModel().changeDescription(noteBeans2, newDescription);
											System.out.println("Description successfully changed...");
											System.out.println("Going back in 5000ms......");
											Thread.currentThread().sleep(5000);
											System.out.println();
											continue outter;
										} 
										catch (TitleDoesNotExistException | CategoryNotExistException e)
										{
											System.out.println(e.getMessage());
											Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
											System.out.println("");
										} catch (InterruptedException e)
										{
											Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
										}
									}
							case 3:continue outter;
									
							default:System.out.println("Invalid option..");
									System.out.println("Try again....");
									System.out.println();
									continue inner;
							
							}
						
					}
				case 3:main(null);
				
				default:System.out.println("Invalid option..");
					 	continue outter;

			}
		}
		
	}
	
	public static void removeCategoryNote()
	{
		Scanner scanner=new Scanner(System.in);
		Scanner scannerLine=new Scanner(System.in);
		outter:while(true)
		{
			System.out.println("Choose from the following option");
			System.out.println("1. To remove category");
			System.out.println("2. To remove note");
			System.out.println("3. To go main menu");
			System.out.println("Enter the option...");
			while(!scanner.hasNextInt())
			{
				System.out.println("Invalid option...");
				System.out.println("Try again...");
				System.out.println();
				scanner.next();
				continue outter;
			}
			int choice=scanner.nextInt();
			switch (choice)
			{
				case 1:
						inner:while(true)
						{
							System.out.println("Enter the category name to remove");
							String categoryName=scanner.next();
							if (!Validate.isValidateName(categoryName))
							{
								System.out.println("Invalid category name. Minimum name length is 3 without digit at beginning");
								continue inner;
							}
							if(!NoteModel.categoryExists(categoryName))
							{
								System.out.println(categoryName+" category name doesn't exist...");
								System.out.println("Try again...");
								System.out.println();
								continue inner;
							}
							NoteBeans noteBeans=new NoteBeans();
							noteBeans.setCategoryName(categoryName);
							System.out.println("Note: All notes belonging to this category will be lost...");
							System.out.println("Do you still want to continue.....Yes/NO");
							String ans=scanner.next().toLowerCase();
							if(!(ans.equals("yes")||ans.equals("no")))
							{
								System.out.println("Invalid input.... Try again");
								continue inner;
							}
							if(ans.equals("no")) 
							{
								System.out.println("You asked to abort  deletion of category...");
								System.out.println();
								break inner;
							}
							else
							{
								try
								{
									new NoteModel().removeCategory(noteBeans);
									System.out.println("Successfully removed");
									System.out.println();
									continue outter;
								}
								catch (CategoryNotExistException e)
								{
									Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
									System.out.println(e.getMessage());
									System.out.println();
								}
								
							}
						}
				
					break;
					
				case 2:	String categoryName,noteTitle;
						inner:while(true)
						{
							System.out.println("Enter the category name to remove...");
							categoryName=scanner.next();
							if (!Validate.isValidateName(categoryName))
							{
								System.out.println("Invalid category name. Minimum name length is 3 without digit at beginning");
								continue inner;
							}
							if(!NoteModel.categoryExists(categoryName))
							{
								System.out.println(categoryName+" category name doesn't exist...");
								System.out.println("Try again...");
								System.out.println();
								continue inner;
							}
							
							innerTitle:while(true)
							{
								System.out.println("Enter the Note Title...");
								noteTitle=scannerLine.nextLine();
								if (!Validate.isValidateName(noteTitle))
								{
									System.out.println("Invalid note title. Minimum name length is 3 without digit at beginning");
									continue innerTitle;
								}
								if(!NoteModel.titleExits(noteTitle, categoryName))
								{
									System.out.println(noteTitle +" note title doesn't exist...");
									System.out.println("Try again...");
									System.out.println();
									continue innerTitle;
								}
								break innerTitle;
							}
							
							
							NoteBeans noteBeans=new NoteBeans();
							noteBeans.setCategoryName(categoryName);
							noteBeans.setNoteTitle(noteTitle);
							System.out.println("Note: All details belonging to this note be lost...");
							System.out.println("Do you still want to continue.....Yes/NO");
							String ans=scanner.next().toLowerCase();
							if(!(ans.equals("yes")||ans.equals("no")))
							{
								System.out.println("Invalid input.... Try again");
								continue inner;
							}
							if(ans.equals("no")) 
							{
								System.out.println("You asked to abort deletion process for note...");
								System.out.println();
								continue outter;
							}
							else
							{
								try
								{
									new NoteModel().removeNote(noteBeans);
									System.out.println("Successfully removed title...");
									System.out.println();
									continue outter;
								} 
								catch (TitleDoesNotExistException | CategoryNotExistException e)
								{
									Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
								}
							}
							
							
						}

				case 3:main(null);
				default:System.out.println("Invalid option...");
						System.out.println("Try again...");
						System.out.println();
			}
			
		}
		
	}
	
	
	public static void export()    //to export pdf or excel
	{
		Set<String> setOfCategories=new NoteModel().getSetOfCategories();
		if(setOfCategories.size()==0)
		{
			System.out.println("You have not add not added any category yet...");
			System.out.println("After adding category name you can export....");
			try
			{
				System.out.println("Redirecting main menu in 6000ms....");
				System.out.println();
				Thread.currentThread();
				Thread.sleep(6000);
			} catch (InterruptedException e)
			{
				Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());
			}
			
			return;
		}
		
		Scanner scanner=new Scanner(System.in);
		Scanner scannerLine=new Scanner(System.in);
		String categoryName;
		outter:while(true)
		{
			System.out.println("Export Option");
			System.out.println("1. To export as pdf");
			System.out.println("2. To export as xls");
			System.out.println("3. To go back");
			System.out.println("Your choice...");
			if(!scanner.hasNextInt())
			{
				
				System.out.println("Invalid option..");
				System.out.println("Try again");
				System.out.println();
				scanner.next();
				continue outter;
			}
			int choice=scanner.nextInt();
			switch (choice)
			{
			case 1:System.out.println("You selected to export as pdf");
					inner:while(true)
					{
						System.out.println("Enter the category name to export pdf");
						categoryName=scannerLine.nextLine();
						if (!Validate.isValidateName(categoryName))
						{
							System.out.println("Invalid category name...");
							System.out.println("Try again...");
							System.out.println();
							continue inner;
						}
						if (!NoteModel.categoryExists(categoryName))
						{
							System.out.println(categoryName+" category not found. Enter a existing category");
							System.out.println("Try again");
							System.out.println();
						}
						else
							break inner;			
					}
					NoteBeans noteBeans=new NoteBeans();
					noteBeans.setCategoryName(categoryName);
					try
					{
						new NoteModel().generatePdf(noteBeans);
						System.out.println("Pdf generaed for category "+categoryName+" in A5 size");
						System.out.println("Generated pdf file name is "+categoryName+".pdf");
						System.out.println();
					}
					catch (CategoryNotExistException e)
					{
						Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());	
						System.out.println(e.getMessage());
					}
					
				
					break;
			
			case 2:System.out.println("To export as excel selected...");
					inner:while(true)
					{
						System.out.println("Enter the category name to export pdf");
						categoryName=scannerLine.nextLine();
						if (!Validate.isValidateName(categoryName))
						{
							System.out.println("Invalid category name...");
							System.out.println("Try again...");
							System.out.println();
							continue inner;
						}
						if (!NoteModel.categoryExists(categoryName))
						{
							System.out.println(categoryName+" category not found. Enter a existing category");
							System.out.println("Try again");
							System.out.println();
						}
						else
							break inner;			
					}
					NoteBeans noteBeans1=new NoteBeans();
					noteBeans1.setCategoryName(categoryName);
					try
					{
						new NoteModel().generateXls(noteBeans1);;
						System.out.println("XLS generaed for category "+categoryName+" in A5 size");
						System.out.println("Generated xls file name is "+categoryName+".xls");
						System.out.println();
					}
					catch (CategoryNotExistException e)
					{
						Logger.getInstance().writeLog((new Date()).toString()+":"+e.getMessage());	
						System.out.println(e.getMessage());
					}
					break;
					
			case 3: break outter;
			default:System.out.println("Invalid option...");
					System.out.println("Try again...");
					System.out.println();
				//break;
			}
		}
		
		
	}
	
	
	public static void sendEmail()   //send the remainder via mail
	{
		Scanner scanner=new Scanner(System.in);
		System.out.println("Enter email-id to whom you want to send");
		String emailId=scanner.nextLine();
		System.out.println("Enter the subject");
		String subject=scanner.nextLine();
		System.out.println("Enter the message");
		String message=scanner.nextLine();
		NoteBeans noteBeans=new NoteBeans();
		noteBeans.setEmailId(emailId);
		noteBeans.setMailMessage(message);
		noteBeans.setMailSubject(subject);
		NoteModel noteModel=new NoteModel();
		Boolean wasSent=noteModel.sendMail(noteBeans);
		if(wasSent==false)
		{
			System.out.println();
			System.out.println("Mail was not sent check the log file for more details");
		}
		main(null);
				
		
	}

}
