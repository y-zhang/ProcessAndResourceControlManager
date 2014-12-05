import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;


/*
 * 	Process and Resource Management
 * 	Yifan Zhang
 *  
 */

public class MainClass {

	public static void main(String[] args) throws FileNotFoundException {
		
		File file = new File("C:/test1.txt");	//get the .txt file to read from the specified location

		FileReader fr = new FileReader(file);
		Scanner scanner = new Scanner(fr);
		PrintWriter printWriter = new PrintWriter("C:/test1-out.txt");	// PrintWriter, which will output a .txt file onto the specificed location
        
		printWriter.println("Init is running");	// outputs the string "Init is running" to symbolize the start of program
		Commands commands = new Commands();	// creates a new Commands class
		while(scanner.hasNext())	// while loop, which returns true if there's another token in its input
		{
			String command = scanner.next();	// finds the next token in the scanner and set it as a String called command
			if(command.equalsIgnoreCase("init"))	// compares if the command string equals "init" while ignoring capitalization
			{
				Commands newCommands = new Commands();	// creates a new Commands class
				commands = newCommands;	// have the old command variable set to the newly created Command class
				printWriter.println();	// an empty line just for spacing to separate one simulation from another
				printWriter.println("Init is running");	// the start of a new simulation
			}
			else if(command.equalsIgnoreCase("quit"))	// compares if the command string equals "quit" while ignoring capitalization
			{
				printWriter.println("process terminated");	// print "process terminated" into output file if quit command if called
				break;
			}
			else if(command.equalsIgnoreCase("cr"))
			{
				String pid = scanner.next();	// finds the next token in the scanner and set it as a String called pid, which is the process ID
				int prio = Integer.parseInt(scanner.next());	// finds the next token in the scanner and parse it as an integers.  Then set it as an int variable prio for the priority of process
				commands.CreateProcess(pid, prio);		// calls the CreateProcess method in Commands class while passing pid and prio as parameters to create a new ProcessControlBlock
				printWriter.println(commands.outputString());
			}
			else if(command.equalsIgnoreCase("de"))
			{
				commands.DestroyProcess(scanner.next());
				printWriter.println(commands.outputString());
			}
			else if(command.equalsIgnoreCase("req"))
			{
				commands.RequestResource(scanner.next());
				printWriter.println(commands.outputString());
			}
			else if(command.equalsIgnoreCase("rel"))
			{
				commands.ReleaseResource(scanner.next());
				printWriter.println(commands.outputString());
			}
			else if(command.equalsIgnoreCase("to"))
			{
				commands.TimeOut();
				printWriter.println(commands.outputString());
			}
		}

		scanner.close();
		printWriter.close();
		
	}

}
