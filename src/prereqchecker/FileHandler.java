package prereqchecker;

import java.util.*;

/*
 * Class that handles file to create an adjacency list out of
 */
public class FileHandler {

    public static Graph readCoursesFromFile (String inputFile) //Class expects input file 
    {
        StdIn.setFile(inputFile);

        // Catch the total number of courses to interate through
        int numOfClasses = StdIn.readInt(); 
        StdIn.readLine(); //Clear newline buffer after int on first line
        
        //Create graph object that will contain classes from file
        Graph classGraph = new Graph();

        //Iterate through all classes (numOfClasses) 
        for(int i = 0; i < numOfClasses ; i++)
        {
            //read next class in input file 
            String classToAdd = StdIn.readString();
            StdIn.readLine(); //Clear buffer

            //Add this class to the graph
            classGraph.addVertex(classToAdd);
        }

        //Catch the number of classes with prereqs (edges) in this input file
        int numClassesWithPrereqs = StdIn.readInt();
        StdIn.readLine(); //Clear newline buffer after reading int on this line 

        //Iterate through the classes to add edges too (classes with prereqs)
        for(int j = 0; j < numClassesWithPrereqs; j++)
        {
            //read class that has prereqs and prereq for the class
            String classWithPrereq = StdIn.readString();
            String classPrereq = StdIn.readString();
            StdIn.readLine(); //clear newline buffer

            //Add the class prereq to the adjacency list of classes 
            classGraph.addEdge(classWithPrereq, classPrereq);
        }

        return classGraph;
    }

    public static void printGraphToFile (Graph classGraph, String fileName)
    {
        //Catch adjacencyList to print
        ArrayList<ClassNode> adjacencyList = classGraph.getAdjacencyList();

        //Set file to print to
        StdOut.setFile(fileName);

        for (ClassNode node : adjacencyList) //Iterate through adjacencyList
        {
            //Print class we are at
            StdOut.print(node.getClassName());

            //Print space after first class is printed 
            StdOut.print(" ");

            //Move to next node (if there is a node after)
            ClassNode ptr = node.getNext();

            //If node has links, print them (this will be the first link that is after the orig ClassNode)
            while(ptr != null)
            {
                StdOut.print(ptr.getClassName()); //Print class
                StdOut.print(" "); //Print space after class
                ptr = ptr.getNext();
            }
            
            StdOut.println(); //After the class and prereqs are printed, print new line
        }

        StdOut.close(); //Close file when we are done writing to it 
    }

    //Using input file of class and prereq along with the adjacency list, determine and print if prereq is valid 
    public static void isValidPrereq(Graph classGraph, String prereqFile, String outputFile)
    {
        StdIn.setFile(prereqFile);

        //Catch the class to add prereq (first line in file)
        String classToAddPrereq = StdIn.readString();
        StdIn.readLine(); //Clear buffer

        //Catch the prereq class (second line in file)
        String newPrereq = StdIn.readString();

        boolean isNotValidPrereq = classGraph.isThereCycleDFS(classToAddPrereq, newPrereq);

        StdOut.setFile(outputFile);
        //Print if prereq is valid to output file
        if (isNotValidPrereq)
        {
            StdOut.print("NO");
        }
        else
        {
            StdOut.print("YES");
        }

        StdOut.close(); //Close file when we are done with it
        return;
    }

    //Using eligible
    public static void eligibleClasses(Graph classGraph, String eligibleFile, String outputFile)
    {
        StdIn.setFile(eligibleFile);

        //Catch the number of eligible classes in inputfile
        int numOfTakenClasses = StdIn.readInt();
        StdIn.readLine(); //Clear buffer

        //Create an array of strings to hold the classes taken so far
        String[] currClassesComplete = new String[numOfTakenClasses];

        //Loop through eligible classes in the file and add them to the array
        for(int i = 0; i < currClassesComplete.length; i++)
        {
            String className = StdIn.readString(); //Catch class name 
            StdIn.readLine(); //Clear buffer

            currClassesComplete[i] = className; //Add class to array
        }

        //Catch all of the eligible classes 
        ArrayList<String> eligibleClasses = classGraph.getEligibleClasses(currClassesComplete);

        //Print these classes to output 
        StdOut.setFile(outputFile);

        //Traverse through array list and print 
        for(int j = 0; j < eligibleClasses.size(); j++)
        {
            String eligibleClass = eligibleClasses.get(j);
            StdOut.print(eligibleClass);
            
            //Print line to separate 
            StdOut.println();
        }

        StdOut.close(); //Close file
        return;
    }

    public static void NeedToTake (Graph classGraph, String needFile, String outputFile)
    {
        StdIn.setFile(needFile);

        //Catch the course we want to take
        String targetCourse = StdIn.readString();
        StdIn.readLine(); //Clear buffer

        //Catch the number of eligible classes in inputfile
        int numOfTakenClasses = StdIn.readInt();
        StdIn.readLine(); //Clear buffer

        //Create an array of strings to hold the classes taken so far
        String[] currClassesComplete = new String[numOfTakenClasses];

        //Loop through eligible classes in the file and add them to the array
        for(int i = 0; i < currClassesComplete.length; i++)
        {
            String className = StdIn.readString(); //Catch class name 
            StdIn.readLine(); //Clear buffer

            currClassesComplete[i] = className; //Add class to array
        }

        //Catch all of the needed classes 
        ArrayList<String> neededClasses = classGraph.getNeededClasses(currClassesComplete, targetCourse);

        //Print these classes to output 
        StdOut.setFile(outputFile);

        //Traverse through array list and print 
        for(int j = 0; j < neededClasses.size(); j++)
        {
            String neededClass = neededClasses.get(j);
            StdOut.print(neededClass);
            
            //Print line to separate 
            StdOut.println();
        }

        StdOut.close(); //Close file
        return;
    }
}
