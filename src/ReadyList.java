import java.util.LinkedList;

/*
 * 	Process and Resource Management
 * 	Yifan Zhang
 * 
 */


public class ReadyList {
	
	private LinkedList<ProcessControlBlock> priority0List;
	private LinkedList<ProcessControlBlock> priority1List;
	private LinkedList<ProcessControlBlock> priority2List;
	
	// constructor for the ReadyList class which initializes it
	public ReadyList()
	{
		priority0List = new LinkedList<ProcessControlBlock>();
		ProcessControlBlock initProcess = new ProcessControlBlock("init", 0, 1,0);
		priority0List.add(initProcess);
		priority1List = new LinkedList<ProcessControlBlock>();
		priority2List = new LinkedList<ProcessControlBlock>();
	}
	
	// method that returns the LinkedList of priority 0	
	public LinkedList<ProcessControlBlock> getPriority0List()
	{
		return priority0List;
	}
	
	// method that returns the LinkedList of priority 1
	public LinkedList<ProcessControlBlock> getPriority1List()
	{
		return priority1List;
	}
	
	// method that returns the LinkedList of priority 2
	public LinkedList<ProcessControlBlock> getPriority2List()
	{
		return priority2List;
	}
		
	// method that inserts a process passed in the parameter into its corresponding priority list
	public void insertProcess(ProcessControlBlock process)
	{
		
		if(process.getPriority() == 1)
		{
			priority1List.add(process);
		}
		else if(process.getPriority() == 2)
		{
			priority2List.add(process);
		}
		else if(process.getPriority() == 0)
		{
			priority0List.add(process);
		}
	}
	
	// method that removes a process passed in the parameter from its corresponding priority list
	public void removeProcess(ProcessControlBlock removeP)
	{		
		if(removeP.getPriority() == 0)		
		{
			priority0List.remove();
		}
		else if(removeP.getPriority() == 1)		
		{
			for(int i = 0; i < priority1List.size(); i++)
			{
				if(priority1List.get(i).getPID().equals(removeP.getPID()))
				{
					priority1List.remove(i);
				}
			}
		}
		else if(removeP.getPriority() == 2)
		{
			for(int j = 0; j < priority2List.size(); j++)
			{
				if(priority2List.get(j).getPID().equals(removeP.getPID()))
				{
					priority2List.remove(j);
				}
			}
		}		
	}
}
