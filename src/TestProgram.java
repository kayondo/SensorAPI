import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

class TestProgram
{
	private static Scanner in=new Scanner(System.in);
	public File logfile;
	
	public static void main(String [] args) throws Exception
	{
		in = new Scanner(System.in);
			
		//implemetAPI object
		
		SensorAPI api = new SensorAPI();
		
		// collect the data
        // the data will be stored in a file created
		//##########################UNCOMMMENT THIS ###########################
		System.out.println("Before collecting the data, please enter the name of the file");
		System.out.println("The file will stored in the same folder where you are running the program");
		String filename = in.nextLine();
		
		api.createFile(filename);

		api.collectData(filename); 
		
		// get the summary of the file
		api.summary(filename);
			
		
		//api.summary("out.txt");      //######################TO BE REMOVED: TEST HERE #################3
		
		
		//search from the file of the data collected
		api.search();
		
	    //read the data
		api.readData(filename);
		
		//api.readData("out.txt");
		
		

		
 	} // main 
 	
} // Client Class