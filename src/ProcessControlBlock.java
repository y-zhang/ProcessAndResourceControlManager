import java.util.LinkedList;

/*
 * 	Process and Resource Management
 * 	Yifan Zhang
 *
 */


public class ProcessControlBlock {
	
	private String pid;		
	private ProcessControlBlock parent;		
	private int priority;
	private int statusType;
	private int statusList;
	private LinkedList<ResourceControlBlock> resourceList;
	private LinkedList<ProcessControlBlock> childrenList;

	// constructor for the class which passes pid, priority, statusType, and statusList as parameter and initializes the process
	public ProcessControlBlock(String pid, int priority, int statusType, int statusList)
	{
		this.pid = pid;
		this.priority = priority;
		this.statusType = statusType;
		this.statusList = statusList;
		resourceList = new LinkedList<ResourceControlBlock>();
		childrenList = new LinkedList<ProcessControlBlock>();
	}
	
	// method which returns the process ID 
	public String getPID()
	{
		return pid;		
	}
	
	// method which passes int statusType as the parameter and update the global statusType variable as the parameter variable
	// 3 possible int value: 0 for ready, 1 for running, 2 for blocked
	public void updateStatusType(int statusType)
	{
		this.statusType = statusType;		
	}
	
	// method which returns the statusType of the process
	public int getStatusType()
	{
		return statusType;		
	}
	
	// method which update the statusList pointer of the process 
	// 4 possible int value: 0 for ready list, 1 for R1 blocked list, 2 for R2 blocked list, 3 for R3 blocked list, 4 for R4 blocked list
	public void updateStatusList(int statusList)
	{
		this.statusList = statusList;		
	}
	
	// method which returns the statusList variable
	public int getStatusList()
	{
		return statusList;	
	}
	
	// method which set the global parent variable as the parameter parent
	public void setParent(ProcessControlBlock parent)
	{
		this.parent = parent;
	}
	
	// method for getting the parent of the process
	public ProcessControlBlock getParent()
	{
		return parent;
	}
	
	// method for getting the priority of the process
	public int getPriority()
	{
		return priority;
	}
	
	// method for inserting the resource passed in the parameter into the resourceList
	public void insertResource(ResourceControlBlock resource)
	{
		resourceList.add(resource);
	}
	
	// method for removing the resource from the resourceList given its resourceID
	public ResourceControlBlock removeResource(String resourceID)
	{
		ResourceControlBlock removedResource = null;
		
		for(int i = 0; i < resourceList.size(); i++)
		{
			if(resourceList.get(i).getResourceID().equals(resourceID))
			{
				removedResource = resourceList.get(i);
				resourceList.remove(i);
			}
		}
		return removedResource;
	}
	
	// method that returns the resourceList of the process
	public LinkedList<ResourceControlBlock> getResourceList()
	{
		return resourceList;
	}
	
	// method that inserts child passed from the parameter into the childrenList of the process
	public void insertChild(ProcessControlBlock child)
	{
		childrenList.add(child);
	}
	
	// method that returns the process removed from the childrenList
	public ProcessControlBlock removeChild()
	{
		return childrenList.remove();
	}
	
	// method that returns the childrenList of the process 
	public LinkedList<ProcessControlBlock> getChildrenList()
	{
		return childrenList;
	}
	
}
