// Tests ChemOptimizer class
// In progress... was working the exception catch for if "Enter grade" is not followed by an integer. Need to make it loop.
// Include way to enter in a target grade and figure out what you need on a test, lab, quiz, etc. to get that grade.


import java.util.*;
import java.io.*;

public class ChemTester {
	
	public static void main(String[] args) throws IOException {
		System.out.println("This is a Chem Cash Calculator with a command line UI.");
		System.out.println("Please enter grades and categories when prompted (only one word/number per answer).");
		System.out.println("There are four grade categories: ");
		System.out.println("D = Daily Grade");
		System.out.println("Q = Quiz Grade");
		System.out.println("L = Lab Grade");
		System.out.println("T = Test Grade");
		System.out.println("Type STOP to finish entering grades.");

		Scanner scan = new Scanner(System.in);
		System.out.println();
		int numCash = 0;
		String numCashStr = "";
		while(true)
		{
			System.out.print("Enter the number of Chem Cash you have: ");
			numCashStr = scan.next();
			if(numCashStr.equalsIgnoreCase("STOP"))
			{
				System.out.println("The program will now exit.");
				System.out.println("Please run the .jar file again if you want to use the Chem Cash Calculator.");
				return;
			}
			try {
				numCash = Integer.parseInt(numCashStr);
				break;
			}
			catch(Exception e)
			{
				System.out.println("Please enter a numerical value.");
				continue;
			}
		}
		ChemOptimizer chem = new ChemOptimizer(numCash);
		System.out.println();
		outerloop:
		while(true)
		{
			System.out.print("Enter grade: ");
			String gradeStr = scan.next();
			if(gradeStr.equals("STOP"))
			{
				System.out.print("Are you sure (Y or N)? ");
				String confirm = scan.next();
				if(confirm.equals("N")) {}
				else
					break;
			}
			int grade = 0;
			try {
				grade = Integer.parseInt(gradeStr);
			}
			catch(Exception e) {
				System.out.println("Please enter a numerical value.");
				continue;
			}
			System.out.print("Enter Category: ");
			String category = scan.next();
			if(category.equalsIgnoreCase("STOP"))
			{
				System.out.print("Are you sure (Y or N)? ");
				String confirm = scan.next();
				if(confirm.equals("N")) {}
				else
					break;	
			}
			else if(category.equals("D") || category.equals("Q") || category.equals("L") || category.equals("T")) {}
			else {
				while(!(category.equals("D") || category.equals("Q") || category.equals("L") || category.equals("T")))
				{
					System.out.println("Please enter either D, Q, L, or T.");
					System.out.print("Enter Category: ");
					category = scan.next();
					if(category.equalsIgnoreCase("STOP"))
					{
						System.out.print("Are you sure (Y or N)? ");
						String confirm = scan.next();
						if(confirm.equals("N")) {}
						else
							break outerloop;		
					}
				}
			}
			chem.addGrade(category, grade);
		}
		System.out.println("Here is your Chem Cash Report: ");
		System.out.println();
		//System.out.println(chem.replaceDaily(chem.gradeMap));
		System.out.println(chem.addChemCash());
	}
}