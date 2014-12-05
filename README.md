ProcessAndResourceControlManager
================================

1.	Introduction 
For this project, I am going to design and implement a simplified process and resource manager that is able to handle operations such as create/destroy process, request/release resource, and time-out interrupt.  However, the problem is that I do not have the actual processes or the hardware and instead, my solution to this would be to use test files to simulate currently running processes and the hardware that causes interrupts.  I will develop a test shell to test the manager which reads command from test files, invokes kernel function, and displays the reply in an output file stating which process is running or if there is any errors.

2.	Data Structures 

Process Control Block
+-------------------------+
|      Process ID         | 
|     Type: String        |
+-------------------------+
|     Resource List       | -> |R1| -> |R2| -> |R3| -> |R4| 
| Type: LinkedList of RCB |
+-------------------------+
|         Status          |
| Type: int (0 for ready, |
| 1 for running, 2 for    |
| blocked)                |
+-------------------------+
|        Parent           |
|Type: pointer to another |
|PCB who is its parent    |
+-------------------------+
|       Children          | -> |child#1| -> |child#2| ->… -> |child#N|
|Type: LinkedList of its  | or
|child(PCB)               | -> NULL (if no children) 
+-------------------------+
|        Priority         |
|       Type: int         |
+-------------------------+
The Process Control Block is a structure having a process ID, a pointer to a resource list, process status, parent, child, and priority.  Firstly, the PCB contains a process ID, which is unique to that process only.  Secondly, there is a pointer to a resource list which is a linkedList of resources that are allocated to the process.  The maximum size of the resource list can only be 4 since it is given in the instruction by professor Bic that there will only be 4 resources labeled.  Thirdly, there will be a process status which has a type and a list, and there are three types either running, ready, or block.  If the types are running or ready, then that PCB points to the ready list; else, if the type is block, then PCB removes its pointer from the ready list and points itself to the RCB of the resource that it is blocked from.  Fourth, a pointer to its parent, which is also a PCB and each PCB can only have one parent.  Fifth, a pointer to a linkedList of its children or NULL if the PCB doesn’t have children.  Lastly, there is the priority in the PCB which indicates if it is either 1 or 2, except for the Init PCB which is the only PCB that has priority 0.

Resource Control Block
+--------------------+
|   Resource ID      | 
|   Type: String     |
+--------------------+
|      Status        |
| Type: Boolean      |
| (True for ‘free’   |
| and false for      |
| ‘allocated’)       |
+--------------------+
|    Waiting List    | -> |Blocked#1| -> |Blocked#2| ->…-> |Blocked#N|
| Type: LinkedList   | or 
| of blocked         | -> NULL (if no process waiting for the resource)
| processes(PCB)     | 
+--------------------+
The Resource Control Block is a structure having a Resource ID, resource Status, and a pointer to a waiting list.  Firstly, the RCB contains a resource ID, which is unique to that resource only and there are only 4 possible resources ID since we only 4 resources.  Secondly, resource status indicates if the resource is currently 'free' or 'allocated' to a PCB represented using the Boolean function with True for 'free' and False for 'allocated'.  Lastly, there is a pointer to the waiting list which is a linkedList of processes that are blocked because the resource is currently allocated to another PCB or NULL if there is no PCB waiting for the resource.  Once the resource becomes available again, the resource is allocated to the next PCB in the linkedList or changes its status to 'free' if there is no other PCB waiting for the resource.

Ready List
+---------------+
| Priority 2    | -> |PCB Priority 2 #1| -> … -> |PCB Priority 2 #N|  
| (linkedList   | or
| of PCB with   | -> NULL (if there is no PCB with priority 2)
| priority 2)   |
+---------------+
| Priority 1    | -> |PCB Priority 1 #1| -> … -> |PCB Priority 1 #N| 
| (linkedList   | or 
| of PCB with   | -> NULL (if there is no PCB with priority 1)
| priority 1)   |
+---------------+
| Priority 0    | -> |Init PCB|
+---------------+
The Ready List is a structure of an array with 3 indexes, with index 0 representing priority 0, index 1 representing priority 1, and index 2 representing priority 2.  Priority 0 index has a pointer to PCB Init and is the only PCB in priority 0.  The Init PCB is not allowed to request resource and can never be blocked since it acts as a “dummy” process when there is no other resource ready to run.  For priority 1, it has a pointer to a linkedList of processes with priority 1 or NULL if there is no process with priority 1.  For priority 2, it has a pointer to a linkedList of processes with priority 2 or NULL if there is no process with priority 2.    

3.	System Architecture

The main functions are create process, destroy process, request resource, release resource, Scheduler, and Time-out Interrupts.  

Create process function creates a new process by first creating a new PCB data structure, then initializing it using parameters, then linking itself to the parent, then inserting itself at the end of the Ready List in the corresponding priority linkedList, and finally calling the Scheduler so the Scheduler can schedule the next process to run when create a process is called in the input file.  

Destroy process function can destroy a process in any status (ready, running, or blocked) and when the function is called, it will call another function Kill_Tree which recursively destroy the children of the process called; and process can be destroyed by any of its ancestors (like parent process, grandparent process, great-grandparent process, etc.) or by itself.  First, I pass in the Process ID of the process that I would like to destroy, then I call the Kill_Tree function and for all the child process, it will recursively call itself, delete the PCB and update all pointers.   Lastly, the Destroy function calls the Scheduler so the Scheduler can schedule the next process to run.

Request resource function gives the process the resource if it is ‘free’ or inserts the process to the end of the waiting list if it is ‘allocated’ to another process.  If the status of the resource is ‘free’, first change the status of the RCB to ‘allocated’ then insert it to the resource list of the PCB that is requesting the resource.  If the status of the RCB is ‘allocated’, first changes the status type of the PCB requesting the resource from running to blocked and change the pointer so that the PCB points to the RCB instead of the Ready List.  Next, remove the PCB from the Ready List’s linkedList, and insert itself to the waiting list of the RCB.  Lastly, Request resource function calls the Scheduler so the Scheduler can schedule the next process to run.

Release resource function has the process release the resource and passes it to the next process in the waiting list or set the status of the resource to ‘free’ if there is no other process waiting for the resource. First, the PCB removes the RCB from its resource list so that it is no longer holding the RCB.  Next, if the waiting list of the RCB is empty, then it changes the status of the RCB to ‘free’.  If there are processes waiting for the resource, then it removes the first PCB in the waiting list.  Next, the status type of the PCB is changed from blocked to ready and changes the status list pointer of the PCB so it points to the Ready List.  Afterward, the RCB is inserted to the end of the resource list of the PCB and the PCB is inserted to the end of the Ready List in the corresponding priority linkedList.  Lastly, Release resource function calls the Scheduler so the Scheduler can schedule the next process to run.

Scheduler function is called at the end of every kernel call to determine which process to run next.  It first find the highest priority process and if the new process has a higher priority than the one that is currently running or the currently running process status type is not running or if the currently running process does not exist anymore, then it does a context switch by switching to the new process and the new process will be running.  
Time-out Interrupts function stops the process that is currently running and moves it to the end of the linkedList.  First, the function finds the running process PCB and removes it from the Ready List.  Next, the status type of the PCB is changed from running to ready and inserts the update PCB to the end of the linkedList.  Lastly, Time-out Interrupts function calls the Scheduler so the Scheduler can schedule the next process to run.
 