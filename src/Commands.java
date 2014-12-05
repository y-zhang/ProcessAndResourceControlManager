
/*
 * 	Process and Resource Management
 * 	Yifan Zhang
 *
 */


public class Commands {
	
	private ReadyList readyList;
	private ResourceControlBlock R1;
	private ResourceControlBlock R2;
	private ResourceControlBlock R3;
	private ResourceControlBlock R4;
	
	// constructor for Commands class which create a new readyList and 4 new Resources
	public Commands()
	{
		readyList = new ReadyList();
		R1 = new ResourceControlBlock("R1");
		R2 = new ResourceControlBlock("R2");
		R3 = new ResourceControlBlock("R3");
		R4 = new ResourceControlBlock("R4");
	}
	
	// method that creates a new process given the parameters
	public void CreateProcess(String pid, int priority)
	{
		ProcessControlBlock newProcess = new ProcessControlBlock(pid, priority, 0, 0);		// creates a new process with pid, priority, statusType and statusList initialized to 0
		if(!pid.equals("init"))		
		{
			ProcessControlBlock parent = getCurrentlyRunningProcess();		// get parent, which is the currently running process
			parent.insertChild(newProcess);		// insert newly create process into the childrenList of parent
			newProcess.setParent(parent);		// set parent pointer for the newly created process
		}
		readyList.insertProcess(newProcess);	// insets the newly create process into the readyList
		Scheduler();		// calls the Scheduler method
	}
	
	// method that recursively destroy a process given the parameter
	public void DestroyProcess(String processID)
	{
		ProcessControlBlock p = getPointerToPCB(processID);		
		killTree(p);		// call the killTree method and pass the process to delete as parameter
		System.out.println(readyList.getPriority1List().size()+" "+readyList.getPriority2List().size());
		// the next 4 if/else if statement check if a resource status is true but has process waiting on the waitList, it removes the process from the waitList and handles it
		if(R1.getResourceStatus() == true && !R1.getWaitList().isEmpty())
		{
			R1.updateResourceStatus(false);
			ProcessControlBlock p1 = R1.removeFromWaitList();
			p1.updateStatusType(0);
			p1.updateStatusList(0);
			p1.insertResource(R1);
			readyList.insertProcess(p1);
		}
		else if(R2.getResourceStatus() == true && !R2.getWaitList().isEmpty())
		{
			R2.updateResourceStatus(false);
			ProcessControlBlock p2 = R2.removeFromWaitList();
			p2.updateStatusType(0);
			p2.updateStatusList(0);
			p2.insertResource(R2);
			readyList.insertProcess(p2);
		}
		else if(R3.getResourceStatus() == true && !R3.getWaitList().isEmpty())
		{
			R3.updateResourceStatus(false);
			ProcessControlBlock p3 = R3.removeFromWaitList();
			p3.updateStatusType(0);
			p3.updateStatusList(0);
			p3.insertResource(R3);
			readyList.insertProcess(p3);
		}
		else if(R4.getResourceStatus() == true && !R4.getWaitList().isEmpty())
		{
			R4.updateResourceStatus(false);
			ProcessControlBlock p4 = R4.removeFromWaitList();
			p4.updateStatusType(0);
			p4.updateStatusList(0);
			p4.insertResource(R4);
			readyList.insertProcess(p4);
		}
		updateCurrentlyRunning();		// calls updateCurrentlyRunning method to update the currently running process 
		Scheduler();		// calls the Scheduler method to schedule the next method to run
	}
	
	// method which recursively destroy the children of the process passed in the parameter
	public void killTree(ProcessControlBlock p)
	{
		ProcessControlBlock removedPCB = null;
		while(!p.getChildrenList().isEmpty())
		{
			removedPCB = p.removeChild();
			killTree(removedPCB);
		}
		if(p.getChildrenList().isEmpty())
		{
			removedPCB = p;
		}
		readyList.removeProcess(removedPCB);
		if(removedPCB.getStatusList() == 0)
		{
			readyList.removeProcess(removedPCB);
		}
		else if(removedPCB.getStatusList() == 1)
		{
			for(int i = 0; i < R1.getWaitList().size(); i++)
			{
				if(R1.getWaitList().get(i).getPID().equals(removedPCB.getPID()))
				{
					R1.getWaitList().remove(i);
				}
			}
		}
		else if(removedPCB.getStatusList() == 2)
		{
			for(int i = 0; i < R2.getWaitList().size(); i++)
			{
				if(R2.getWaitList().get(i).getPID().equals(removedPCB.getPID()))
				{
					R2.getWaitList().remove(i);
				}
			}
		}
		else if(removedPCB.getStatusList() == 3)
		{
			for(int i = 0; i < R3.getWaitList().size(); i++)
			{
				if(R3.getWaitList().get(i).getPID().equals(removedPCB.getPID()))
				{
					R3.getWaitList().remove(i);
				}
			}
		}
		else if(removedPCB.getStatusList() == 4)
		{
			for(int i = 0; i < R4.getWaitList().size(); i++)
			{
				if(R4.getWaitList().get(i).getPID().equals(removedPCB.getPID()))
				{
					R4.getWaitList().remove(i);
				}
			}
		}
		
		while(!removedPCB.getResourceList().isEmpty())
		{
			ResourceControlBlock freedResource = removedPCB.getResourceList().remove();
			freedResource.updateResourceStatus(true);
		}
	}
	
	// method which updates which process is currently running
	public void updateCurrentlyRunning()
	{
		if(!readyList.getPriority2List().isEmpty())
		{
			readyList.getPriority2List().getFirst().updateStatusType(1);
		}
		else if(readyList.getPriority2List().isEmpty() && !readyList.getPriority1List().isEmpty())
		{
			readyList.getPriority1List().getFirst().updateStatusType(1);
		}
		else if(readyList.getPriority2List().isEmpty() && readyList.getPriority1List().isEmpty())
		{
			readyList.getPriority0List().getFirst().updateStatusType(1);
		}
	}
	
	// method for requesting a resource given parameter 
	public void RequestResource(String resourceID)
	{
		ProcessControlBlock self = getCurrentlyRunningProcess();
		ResourceControlBlock r = getRCB(resourceID);
		if(r.getResourceStatus() == true)
		{
			r.updateResourceStatus(false);
			self.insertResource(r);
		}
		else
		{
			self.updateStatusType(2);
			readyList.removeProcess(self);
			if(r.getResourceID().equals("R1"))
			{
				self.updateStatusList(1);
			}
			else if(r.getResourceID().equals("R2"))
			{
				self.updateStatusList(2);
			}
			else if(r.getResourceID().equals("R3"))
			{
				self.updateStatusList(3);
			}
			else
			{
				self.updateStatusList(4);
			}
			updateCurrentlyRunning();
			r.insertToWaitList(self);
		}
		Scheduler();
	}
	
	// method which releases the resource given parameter
	public void ReleaseResource(String resourceID)
	{
		ResourceControlBlock r = getRCB(resourceID);
		ProcessControlBlock self = getCurrentlyRunningProcess();
		self.removeResource(resourceID);
		
		if(r.getWaitList().isEmpty())
		{
			r.updateResourceStatus(true);
		}
		else
		{
			ProcessControlBlock q = r.removeFromWaitList();
			q.updateStatusType(0);
			q.updateStatusList(0);
			q.insertResource(r);
			readyList.insertProcess(q);
		}
		Scheduler();
	}
	
	// returns the resource given parameter
	public ResourceControlBlock getRCB(String resourceID)
	{
		if(resourceID.equals("R1"))
		{
			return R1;
		}
		else if(resourceID.equals("R2"))
		{
			return R2;
		}
		else if(resourceID.equals("R3"))
		{
			return R3;
		}
		else
		{
			return R4;
		}
	}
	
	// method that is called at the end of every kernel call which determines the next process to run
	public void Scheduler()
	{
		ProcessControlBlock self = getCurrentlyRunningProcess();
		ProcessControlBlock higherPrioProcess = getHigherPriorityProcess(self);
		
		if(!self.getPID().equals(higherPrioProcess))
		{
			if(self.getPriority() < higherPrioProcess.getPriority() || self.getStatusType() != 1 || self == null)
			{
				preempt(higherPrioProcess, self);
			}
			
		}
	}
	
	// method called from the Scheduler that switches which process runs now
	public void preempt(ProcessControlBlock higherPrioProcess, ProcessControlBlock self)
	{
		self.updateStatusType(0);
		higherPrioProcess.updateStatusType(1);
	}
	
	// method which times out a currently running process and puts it in the end of the readyList
	public void TimeOut()
	{
		ProcessControlBlock q = getCurrentlyRunningProcess();
		readyList.removeProcess(q);
		q.updateStatusType(0);
		readyList.insertProcess(q);
		updateCurrentlyRunning();
		Scheduler();
	}
	
	// method that return the process which is either in the readyList or the waitList of the resource that it's blocked from given parameter
	public ProcessControlBlock getPointerToPCB(String processID)
	{
		ProcessControlBlock pointerToPCB = null;
		if(processID.equals("init"))
		{
			return readyList.getPriority0List().get(0);
		}
		else
		{
			for(int i = 0; i < readyList.getPriority1List().size(); i++)
			{
				if(readyList.getPriority1List().get(i).getPID().equals(processID))
				{
					return readyList.getPriority1List().get(i);
				}
			}
			for(int j = 0; j < readyList.getPriority2List().size(); j++)
			{
				if(readyList.getPriority2List().get(j).getPID().equals(processID))
				{
					return readyList.getPriority2List().get(j);
				}
			}
			for(int k = 0; k < R1.getWaitList().size(); k++)
			{
				if(R1.getWaitList().get(k).getPID().equals(processID))
				{
					return R1.getWaitList().get(k);
				}
			}
			for(int l = 0; l < R2.getWaitList().size(); l++)
			{
				if(R2.getWaitList().get(l).getPID().equals(processID))
				{
					return R2.getWaitList().get(l);
				}
			}
			for(int m = 0; m < R3.getWaitList().size(); m++)
			{
				if(R3.getWaitList().get(m).getPID().equals(processID))
				{
					return R3.getWaitList().get(m);
				}
			}
			for(int n = 0; n < R4.getWaitList().size(); n++)
			{
				if(R4.getWaitList().get(n).getPID().equals(processID))
				{
					return R4.getWaitList().get(n);
				}
			}
		}
		
		return pointerToPCB;
	}
	
	// method that returns the currently running process in the readyList
	public ProcessControlBlock getCurrentlyRunningProcess()
	{
		ProcessControlBlock currRunProcess = null;
		
		for(int i = 0; i < readyList.getPriority0List().size(); i++)
		{
			if(readyList.getPriority0List().get(i).getStatusType() == 1)
			{
				return readyList.getPriority0List().get(i);
			}
		}
		for(int j = 0; j < readyList.getPriority1List().size(); j++)
		{
			if(readyList.getPriority1List().get(j).getStatusType() == 1)
			{
				return readyList.getPriority1List().get(j);
			}
		}
		for(int k = 0; k < readyList.getPriority2List().size(); k++)
		{
			if(readyList.getPriority2List().get(k).getStatusType() == 1)
			{
				return readyList.getPriority2List().get(k);
			}
		}		
		return currRunProcess;
	}
	
	// method that return the higher priority process give parameter
	public ProcessControlBlock getHigherPriorityProcess(ProcessControlBlock currRunningProcess)
	{
		 if(currRunningProcess.getPriority() == 1)
		 {
			 if(!readyList.getPriority2List().isEmpty())
			 {
				 return readyList.getPriority2List().getFirst();
			 }
		 }
		 else if(currRunningProcess.getPriority() == 0)
		 {
			 if(!readyList.getPriority2List().isEmpty())
			 {
				 return readyList.getPriority2List().getFirst();
			 }
			 else if(!readyList.getPriority1List().isEmpty())
			 {
				 return readyList.getPriority1List().getFirst();
			 }
		 }
		 return currRunningProcess;
	}
	
	// method that returns which process is currently running
	public String outputString()
	{
		if(readyList.getPriority2List().isEmpty()&&readyList.getPriority1List().isEmpty())
		{			
			return "Init is running";
		}
		else
		{
			return getCurrentlyRunningProcess().getPID()+" is running";
		}
	}

}
