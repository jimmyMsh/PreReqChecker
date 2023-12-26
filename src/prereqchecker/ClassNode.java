package prereqchecker;

public class ClassNode {
    private String className; //class contained in this node 
    private ClassNode next;   //Reference to other class list in adjacency list 

    /*
     * Constructor
     */
    public ClassNode(String className, ClassNode next)
    {
        this.className = className;
        this.next = next;
    }

    /*
     * Default constructor
     */
    public ClassNode() {
        this(null, null);
    }

    /* Getter and setter methods */
    public String getClassName() {return className;}
    public void setClassName(String className) {this.className = className;}

    public ClassNode getNext() {return next;}
    public void setNext(ClassNode next) {this.next = next;}
}