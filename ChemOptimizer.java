/**
 * The ChemOptimizer class takes "Chem Cash" and tells the user how most effectively to use them based on
 * the user's grades.
 * @author: Pranav Harathi
 * Date: 01-24-15
 */

import java.util.*;
import java.io.*;
// Takes chem grades and number of chem cash and determines how they should be used

public class ChemOptimizer {
	private int numCash;
	public Map<String, List<Integer>> gradeMap;
    private double average;
	// Four types of grades: D (x1 Daily), Q (x3 Daily), L (x1 Major), T (x3 Major)
	// Major = Daily x 3
	// D: x1, Q: x3, L: x3, T: x9
    /*
	 * Chem cash has four abilities: replace daily grade with 100, +10 to a quiz, +10 to lab, or +3 to a test
	 */

    public ChemOptimizer(int num) {
    	gradeMap = new HashMap<String, List<Integer>>();
    	numCash = num;
    }

    /** 
     * Adds a grade to corresponding list of types
     * @param type the type of grade (D, Q, L, T)
     * @param value the actual grade (0-100)
     */
    public void addGrade(String type, int value) {
        // Cases to check:
        // 1. "STOP"
        // 
        if(type.equalsIgnoreCase("STOP"))
            return;
        //adds the grade to a new set to be used later
    	List<Integer> grade = new ArrayList<Integer>();
    	grade.add(value);

    	// Cases: type has not been created, type has empty set, type has existing numbers
    	if(gradeMap.containsKey(type)) // if set exists (and grade type has been added)
    	{
    		gradeMap.get(type).addAll(grade);
    	}
    	else // if type has not been added
    	{
    		gradeMap.put(type, grade);
    	}
    }

    /** 
     * Calculates average based on existing hashmap and PISD grade weighting policy
     * @param gradeMap Map of grades and types
     * @return the six weeks average based on the previously inputted grades
     */
    // need to check what to return if category has not been created.
    public double average(Map<String, List<Integer>> gradeMap) {
        double avg = 0.0;
        // Iterates and adds to a weighted total and count
        int dailyTotal = 0;
        int testTotal = 0;
        int dailyCount = 0;
        int testCount = 0;
        Iterator<String> keys = gradeMap.keySet().iterator();
        while(keys.hasNext())
        {
            String type = keys.next();
            Iterator<Integer> grades = gradeMap.get(type).iterator();
            int typeTotal = 0;
            int typeCount = 0;
            while(grades.hasNext())
            {
                typeTotal += grades.next();
                typeCount++;
            }
            switch (type) {
                case "L" : testTotal += typeTotal;
                            testCount += typeCount;
                            break;
                case "Q" : typeTotal *= 3;
                            typeCount *= 3;
                            dailyTotal += typeTotal;
                            dailyCount += typeCount;
                            break;
                case "T" : typeTotal *= 3;
                            typeCount *= 3;
                            testTotal += typeTotal;
                            testCount += typeCount;
                            break;
                default : dailyTotal += typeTotal;
                            dailyCount += typeCount;
                            break;
            }
        }
        if(testCount == 0 && dailyCount == 0)
            return 0.0;
        else if(testCount == 0 && dailyCount > 0)
            return (dailyTotal * 1.0 / dailyCount);
        else if(dailyCount == 0 && testCount > 0)
            return (testTotal * 1.0 / testCount);
        else
            return ((testTotal * 1.0 / testCount) * 3.0 + (dailyTotal * 1.0 / dailyCount))/4; 
    }

    /** 
     * Calculates how to use chem cash
     * @return new numerical average
     */
    // GG i think this is just broken when you don't a grade in every type.
    // Fixing atm.
    public double addChemCash() {
        // check if empty HashMap
        if(gradeMap.size() == 0)
        {
            System.out.println("No grades entered.");
            return 0.0;
        }
        this.average = average(gradeMap);

        // Test each rule and see which yields the highest grade
        double newAverage = 0.0;
        for(int i = 0; i < numCash; i++)
        {

            // Try all possibilities for chem cash use
            HashMap<String, Double> possibilities = new HashMap<String, Double>();
            // Rewrite to try all AVAILABLE possibilities.
            Set<String> typesAvailable = gradeMap.keySet();
            Iterator<String> types = typesAvailable.iterator();
            while(types.hasNext())
            {
                String type = types.next();
                if(gradeMap.get(type).size() < 1)
                    continue;
                double avg = average(chemHelper(type));
                possibilities.put(type, avg);
            }
            /*
            for(int j = 0; j < 4; j++)
            {
                double avg = 0;
                String type = ""; 
                switch (j) {
                    case 0: avg = average(chemHelper("D"));
                            type = "D";
                            break;
                    case 1: avg = average(chemHelper("Q"));
                            type = "Q";
                            break;
                    case 2: avg = average(chemHelper("L"));
                            type = "L";
                            break;
                    case 3: avg = average(chemHelper("T"));
                            type = "T";
                            break;
                    default: break;
                }
                possibilities.put(type, avg);
            }*/
            // ID max
            double max = 0.0;
            String maxType = "";
            Iterator<String> iter = possibilities.keySet().iterator();
            while(iter.hasNext())
            {
                String t = iter.next();
                if(possibilities.get(t) > max)
                {
                    max = possibilities.get(t);
                    maxType = t;
                }
            }
            newAverage = max;
            if(newAverage == this.average)
            {
                System.out.println("No need to use Chem Cash.");
                break;
            }

            // Print out info
            System.out.println("Use Chem Cash " + (i+1) + " for the " + maxType + " category");
            System.out.println("New average: " + max);

            // Actually change gradeMap
            this.gradeMap = chemHelper(maxType);

        }
        return newAverage;
    }


    /**
     * Replaces daily grade, adds 10 points to a lab or quiz OR 3 points to a Test
     * @param type D, Q, L, or T
     * @param gradeMap Map with grades
     * @return new average
     */
    public Map<String, List<Integer>> chemHelper(String type) {
        // Finds lowest grade in category
        Map<String, List<Integer>> gMap = new HashMap<>();
        deepCopy(this.gradeMap, gMap);
        List<Integer> gs = gMap.get(type);
        int lowest = 100;
        int index = -1;
        for(int i = 0; i < gs.size(); i++)
        {
            if(gs.get(i) <= lowest)
            {
                lowest = gs.get(i);
                index = i;
            }
        }
        

        // applies chem cash rules
        if(type.equals("D"))
            gs.set(index, 100);
        else if(type.equals("Q") || type.equals("L"))
            gs.set(index, gs.get(index) + 10);
        else if(type.equals("T"))
            gs.set(index, gs.get(index) + 3);

            // corrects if over 100
        if(gs.get(index) > 100)
            gs.set(index, 100);
        
        return gMap;
    }

    /**
     * Copies a hashmap to another hashmap deeply, since there isn't a native method
     * @param from Hashmap to copy from
     * @param to Hashmap to copy to
     */
    public void deepCopy(Map<String, List<Integer>> from, Map<String, List<Integer>> to)
    {
        Iterator<String> iter = from.keySet().iterator();
        while(iter.hasNext())
        {
            String key = iter.next();
            to.put(key, new ArrayList<Integer>(from.get(key)));
        }
    }
}