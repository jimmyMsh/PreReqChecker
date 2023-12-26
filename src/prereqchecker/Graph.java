package prereqchecker;

import java.util.*;

public class Graph {
    // List to store all vertices of graph
private ArrayList<ClassNode> adjacencyList;

    public Graph() 
    {
        adjacencyList = new ArrayList<>();
    }

    // method that adds vertice to graph
    public void addVertex(String className) 
    {
        // Create a new ClassNode with the given className and add it to the list
        adjacencyList.add(new ClassNode(className, null));
    }

    // Method to add an edge between two vertices in the graph
    public void addEdge(String fromClassName, String toPrereq) 
    {
        // Iterate through the adjacency list to find the 'from' node
        for (ClassNode node : adjacencyList) 
        {
            if (node.getClassName().equals(fromClassName)) 
            {
                // Once found, add 'to' to the linked list of 'from'
                // Check if there is a node after the class already
                if(node.getNext() == null) //classNode doesnt have edge yet
                {
                    node.setNext(new ClassNode(toPrereq, null)); //sets prereq after the intial class
                }
                else //If there is a node (prereq) after the initial class already
                {
                    //Make a temp pointer to iterate through connected links
                    ClassNode ptr = node;

                    while(ptr.getNext() != null) //Traverse list until last pointer
                    {
                        ptr = ptr.getNext();
                    }
                    ptr.setNext(new ClassNode(toPrereq, null)); //sets the last edge next to the new edge to add
                }
                break;
            }
        }
    }

    //Method to remove edge from a node (if we are calling to remove an edge, assume that there is an edge to this class)
    public void removeEdge(String fromClassName, String prereqToDel)
    {
        // Iterate through the adjacency list to find the 'from' node
        for (ClassNode node : adjacencyList) 
        {
            if (node.getClassName().equals(fromClassName)) 
            {
                //Make a temp pointer to iterate through connected links to find node to delete 
                ClassNode ptr = node;

                //Traverse list until node after pointer is node to delete
                while(!ptr.getNext().getClassName().equals(prereqToDel)) 
                {
                    ptr = ptr.getNext();
                }

                //sets the pointer to one after the node to delete. 
                //If there is only one edge or edge is last, makes pointer point to null.
                ptr.setNext(ptr.getNext().getNext()); 
            }
            break;
        }
    }

    //Method to return completed AdjacencyList 
    public ArrayList<ClassNode> getAdjacencyList ()
    {
        return adjacencyList;
    }

/**
 * Performs a Depth-First Search (DFS) to detect cycles in the graph.
 * A cycle in this context represents a situation where a course indirectly requires itself as a prerequisite,
 * making it impossible to complete. This method checks if there exists any course in the graph
 * for which one of its prerequisites (direct or indirect) is the course itself.
 * 
 * For example, if Course A requires Course B, and Course B requires Course A,
 * then it's impossible to complete either course, as each one requires the other to be completed first.
 * 
 * This method is particularly useful for determining if adding a new prerequisite relationship 
 * between two courses would create such an impossible situation.
 * 
 * will use a recursive helper method do determine if there is a cycle
 **/
    public boolean isThereCycleDFS (String ClassName, String Prereq)
    {
        //Create two arrays (1) that holds all of the visited nodes thus far (2) array that retains the nodes vistied in a cycle
        int[] allVisitedNodes = new int[adjacencyList.size()]; //Tracks all visited nodes 
        int[] currentVisitedPath = new int[adjacencyList.size()]; //Tracks ndoes visited in one cycle 

        //Add  prereq to the class temporarily- then see if there is a cycle
        addEdge(ClassName, Prereq);

        for (int i = 0; i < adjacencyList.size(); i++)
        {
            if(allVisitedNodes[i] == 0)
            {
                if(isCycle(adjacencyList.get(i), allVisitedNodes, currentVisitedPath) == true)
                {
                return true;
                }
            }
        }
        
       return false;
    }

    private boolean isCycle (ClassNode node, int[] allVisitedNodes, int[] currentVisitedPath)
    {
        //Catch index of node to refer to array of visted nodes and current path
        int indexOfNode = findIndexOfClassNode(node.getClassName());

        //Mark this node as visited 
        allVisitedNodes[indexOfNode] = 1;
        //Mark node visted on this path
        currentVisitedPath[indexOfNode] = 1;
        
        //Loop through edges of this node
        ClassNode ptr = node.getNext(); //Start with next node if there is one
       
        //Variable to hold index of the adjacent node 
        int indexOfAdjacentNode;
        //If ptr null (i.e there is no edges to this node) will not go through this loop
        while(ptr != null) 
        {
            //Find index of node adjacent 
            indexOfAdjacentNode = findIndexOfClassNode(ptr.getClassName());

            //If this adjacent node has not been visited yet 
            if(allVisitedNodes[indexOfAdjacentNode] == 0)
            {
                //If any adjacent nodes return true, there is a cycle and stop
                if(isCycle(adjacencyList.get(indexOfAdjacentNode), allVisitedNodes, currentVisitedPath) == true)
                {
                    return true;
                }
            }
            //If this node has been visted and has been visited in the current path, there is a cycle (return true)
            else if(currentVisitedPath[indexOfAdjacentNode] == 1)
            {
                return true;
            }

            ptr = ptr.getNext(); //Moves to next pointer
        }

        //If we go through all siblings and there is no cycle, turn the last visted node in this path that added to the stack back to unvisited 
        currentVisitedPath[indexOfNode] = 0;
        return false;
    }

    //Finds index of class based off of class name
    public int findIndexOfClassNode(String className) 
    {
        for (int i = 0; i < adjacencyList.size(); i++) 
        {
            ClassNode node = adjacencyList.get(i);
            if (node.getClassName().equals(className)) 
            {
                return i;
            }
        }
    return -1; // Return -1 if the class is not found
    }


    /*
    *Pass list of currently completed classes to helper method that will go through all of the
    *related classes in DFS. After the adjacencyList is traversed and all classes are properly marked, will
    *go through the unmarked classes and check if the prereqs have been completed. If they have, add this to the list of classes that can be complete
    *will return array of strings corresponding to the classes that you are now eligible to take.
    */
    public ArrayList<String> getEligibleClasses(String[] currClassesComplete)
    {
        //Create an array that CAN hold all of "completed" nodes thus far - could be all nodes
        //Must be able to refer to this later to see what nodes haven't been completed 
        int[] completedNodes = new int[adjacencyList.size()];

        //Loop through all of the currClassesComplete
        for(int i = 0; i < currClassesComplete.length; i++)
        {
            //Get class name as string 
            String className = currClassesComplete[i];

            //Pass the class name to method that goes through and marks classes and prereqs as complete
            markCurrClassesDFS(className, completedNodes);
        }

        // Capture all classes we could take (if the prereqs are marked as complete)
        ArrayList<String> eligibleClasses = new ArrayList<>();

        //Go through and find classes we have not already completed- if all prereqs are complete it is eligible 
        for (int j = 0; j < completedNodes.length; j++) 
        {

            // Check if the class at index 'j' has not been taken (indicated by 0)
            if (completedNodes[j] == 0) 
            {
                // Pointer to traverse the adjacency list for prereq classes
                ClassNode ptr = adjacencyList.get(j).getNext();
                // Flag to indicate whether to skip to the next class in the for loop
                boolean skipClass = false;

                //Go through prereqs (if there are)
                while (ptr != null) 
                {
                    // Check if the current class in the adjacency list is complete
                    int ptrIndex = findIndexOfClassNode(ptr.getClassName());

                    if (completedNodes[ptrIndex] == 0) 
                    {
                        // If a prerequisite is not complete, set flag to skip this class
                        skipClass = true;
                        break; 
                    }

                // Move to the next class node in the adjacency list
                ptr = ptr.getNext(); 
                }

                if (skipClass) 
                {
                // Skip the current iteration of the for loop and check the next class
                continue; 
                }
                //If all prerreqs are taken - and we have not taken the class yet- add it to elgible classes
                else
                {
                    //Add class the class at this iteration to elegible 
                    String eligibleClass = adjacencyList.get(j).getClassName();
                    eligibleClasses.add(eligibleClass);
                }
            }
        }

        return eligibleClasses; //Return arraylist of elgible classes 
    }


    //Will loop through array of claases given and mark the current classes - along with the classes prereqs - as complete 
    private void markCurrClassesDFS (String className, int[] completedNodes)
    {
        int indexOfClass = findIndexOfClassNode(className);

        //Debugging:
        //StdOut.print("The following class is marked as taken " + node.getClassName() +" ");
        //StdOut.println();

        //Mark this node as complete 
        completedNodes[indexOfClass] = 1;

        //Loop through edges of this node
        ClassNode ptr = adjacencyList.get(indexOfClass).getNext(); //Start with next node if there is one
       
        //Variable to hold index of the adjacent node 
        int indexOfAdjacentNode;
        //If ptr null (i.e there is no edges to this node) will not go through this loop
        while(ptr != null)
        {
            //Debugging:
            //StdOut.print("The Next node is " + ptr.getClassName() +" ");

            //Find index of node adjacent 
            indexOfAdjacentNode = findIndexOfClassNode(ptr.getClassName());

            //If this adjacent node has not been visited yet 
            if(completedNodes[indexOfAdjacentNode] == 0)
            {
                markCurrClassesDFS(adjacencyList.get(indexOfAdjacentNode).getClassName(), completedNodes); //Go mark these related classes as complete 
            }

            ptr = ptr.getNext(); //Move to next pointer 
        }
    }


    public ArrayList<String> getNeededClasses (String[] currClassesComplete, String targetCourse)
    {
        //Create an array that CAN hold all of "completed" nodes thus far - could be all nodes
        //Must be able to refer to this later to see what nodes haven't been completed 
        int[] completedNodes = new int[adjacencyList.size()];

        //Loop through all of the currClassesComplete
        for(int i = 0; i < currClassesComplete.length; i++)
        {
            //given this class, get it's index in adjacencyList
            int indexOfClass = findIndexOfClassNode(currClassesComplete[i]);

            //Pass this adjacency node to method that goes through and marks classes and prereqs as complete
            markCurrClassesDFS(adjacencyList.get(indexOfClass).getClassName(), completedNodes);
        }

        //Capture all classes we need to take
        ArrayList<String> neededClasses = new ArrayList<>();

        //Use helper method to collect needed classes
        collectNeededCoursesDFS(targetCourse, completedNodes, neededClasses);

        //Return caught classes
        return neededClasses;
    }

    private void collectNeededCoursesDFS (String className, int[] completedNodes, ArrayList<String> neededClasses)
    {
        // Find the index of the current course in the adjacency list
        int index = findIndexOfClassNode(className);

        // If the course is already taken, marked by '1', no need to explore its prerequisites
        if (completedNodes[index] == 1) return;

        // Access the first prerequisite (if any) of the current course
        ClassNode node = adjacencyList.get(index).getNext();

        // Iterate over the prerequisites of the current course
        while (node != null) {
            String nextCourse = node.getClassName();
            int nextIndex = findIndexOfClassNode(nextCourse);

            // Add the prerequisite course to the needed list if it hasn't been taken yet
            // and if it's not already in the neededCourses list (to avoid duplicates)
            if (completedNodes[nextIndex] == 0 && !neededClasses.contains(nextCourse)) {
                neededClasses.add(nextCourse);
                // Recursively call the method to explore the prerequisites of the prerequisite
                collectNeededCoursesDFS(nextCourse, completedNodes, neededClasses);
            }

            // Move to the next prerequisite in the list
            node = node.getNext();
        }
    }
}
