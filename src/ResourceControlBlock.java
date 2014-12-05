import java.util.LinkedList;

/*
 * 	Process and Resource Management
 * 	Yifan Zhang
 *
 */

public class ResourceControlBlock {
	
	private LinkedList<ProcessControlBlock> waitList;	// keep track of process that are block from this resource
	private boolean resourceStatus;		// keep track of resource status, true for 'free' and false for 'allocated'
	private String resourceID;
	
	// constructor for the class which initializes the resource given the parameter
	public ResourceControlBlock(String resourceID)
	{
		waitList = new LinkedList<ProcessControlBlock>();
		resourceStatus = true;
		this.resourceID = resourceID;
	}
	
	// method that return the resource ID
	public String getResourceID()
	{
		return resourceID;
	}
	
	// method that update the resource status given the parameter
	public void updateResourceStatus(boolean resourceStatus)
	{
		this.resourceStatus = resourceStatus;
	}
	
	// method that return the resource status
	public boolean getResourceStatus()
	{
		return resourceStatus;
	}
	
	// method that inserts the process into the waitList of the resource given the parameter
	public void insertToWaitList(ProcessControlBlock process)
	{
		waitList.add(process);
	}
	
	// method that returns the process removed from the resource waitList
	public ProcessControlBlock removeFromWaitList()
	{
		return waitList.remove();
	}
	
	// method that returns the entire waitList of the resource
	public LinkedList<ProcessControlBlock> getWaitList()
	{
		return waitList;
	}

}
