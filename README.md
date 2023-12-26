## Prerequisite Checker

### Description
The Prerequisite Checker is a suite of Java classes designed to parse, process, and evaluate prerequisites for university courses patterned after the Rutgers Computer Science program. This project employs a Directed Acyclic Graph (DAG) using adjacency lists to model the complex prerequisite relationships between courses. It was an extensive exercise in understanding Java's object-oriented programming principles, data structures (particularly linked lists), graphs, and file input/output handling.

### Implementation
I designed the project's structure from scratch to make the code readable and modular. This involved thoughtful consideration of which tasks to encapsulate in separate methods and classes and choosing appropriate data structures. A key choice was using linked lists to create an adjacency list for each course, reflecting its prerequisites without redundancies.

#### Key Classes and Methods:

- `AdjList`: Constructs an adjacency list from input files detailing the number of courses and their direct prerequisites. It outputs each course followed by its immediate prerequisites space-separated.

- `ValidPrereq`: Determines if adding a proposed prerequisite relationship would still allow all courses to be possible to take. It checks for potential cycles that could make courses impossible to complete.

- `Eligible`: Identifies courses available to a student to take next based on the courses they have already completed, using the constructed graph to track direct and indirect prerequisites.

- `NeedToTake`: Given a target course, this class identifies all the required predecessors (both direct and indirect prerequisites) that a student must complete before taking the target course.

#### Supporting Classes and Methods:

- `ClassNode`: A node class representing a single course and the link to its prerequisites in the adjacency list.

- `FileHandler`: A utility class for reading courses and prerequisites from input files, processing them according to specified formats, and writing the results to output files.

- `Graph`: Defines the main structure for storing courses and their prerequisites. It includes methods for adding courses, adding prerequisites edges, conducting depth-first searches to identify cycles, and other relevant operations to support qualifications checks.

### Skills Demonstrated

- **Object-Oriented Programming**: By splitting code into classes and methods, the project shows a strong grasp of encapsulation, abstraction, and modularity.

- **Graph Theory**: The program effectively represents course prerequisites as a DAG, highlighting understanding of graph-related concepts such as depth-first search and cycle detection.

- **Data Structures**: A custom implementation of linked lists to form adjacency lists showcases the ability to work with fundamental data structures in Java.

- **File I/O**: The ability to parse structured files to extract and output data demonstrates competence in handling files in Java.

- **Error Checking**: The program is designed to handle missing inputs or incorrect file formats gracefully without crashing.

### Reflection
This project reflects a higher-order understanding and application of computer science principles. I demonstrate a balance of theoretical concepts and practical programming skills by designing the program structure, choosing appropriate data structures, and implementing robust file handling.

Each task showcases a progressive mastery over complex scenarios and tasks, proving an ability to execute requirements and an insight into designing solutions from scratch.
