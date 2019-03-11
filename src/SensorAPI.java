import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;


import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SensorAPI implements API{
	public File logfile;
	private Scanner in=new Scanner(System.in);

	//map the measurement name and the unit
	Map<Integer, String> map = new HashMap<Integer, String>();    //map types
	Map<Integer, String> mapUnits = new HashMap<Integer, String>();    //map units
	private BufferedReader reader;
	private BufferedReader reader2;
	private File fileCreated;
	private String startTime;
	private String endTime;



	public SensorAPI() {
		// TODO Auto-generated constructor stub
	}
	
	public void createFile(String filename) 
	{
		fileCreated = new File(filename);
		System.out.println("File" + " " + filename + " "+ "created");
		
	}

	public void collectData (String filename) throws Exception
	{

		String 	junk;				// Temporary string 
		String 	START = "start\n";	// String that signals the DataGen server to begin sending data
		int 	Hours;				// Data sample time stamp hours
		int 	Minutes;			// Data sample time stamp minutes
		int 	Seconds = 0;			// Data sample time stamp seconds
		float	Humidity = 0.0f;			// Relative Humidity
		float	Temperature = 0.0f;		// Temperature in Degrees Fahrenheit 	
		float	Pressure = 0.0f;			// Pressure in Kilo Pascals (kPa)
		int		portID;				// Server socket port
		
		//find the timespan
		int findTimeSpan = 0;

		FileWriter fw = new FileWriter("out.txt");   //filename object

		// Here we check to see if there is a port number specified on the command line.

		in = new Scanner(System.in);
		try {
			// use port number provided on the command line
			//System.out.println( "Using port:: " + args[0] );
			//portID = new Integer( args[0] );
		} // try

		catch (NumberFormatException IOError)
		{
			// means port number provided on the command line is invalid
			// we set the port number to 6789 and let the user know
			portID = 6789;
			System.out.println( "\nDefault port number:: " + portID + " will be used." );

		} // catch

		// This is used to get input from the user
		BufferedReader UserInput = new BufferedReader( new InputStreamReader(System.in));

		// This is the socket used to connect to the server
		Socket clientSocket = new Socket("localhost", 6789);

		// This is the stream used to send data to the server
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

		// This is the stream used to read data from the server for reading data
		DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());


		// let the user input the time to collect the data
		String inputTime;
		System.out.println("Please enter time in minutes of how long you want to collect the data: ");
		inputTime = in.nextLine();
		Integer intTime = Integer.parseInt(inputTime);              //input from the user as minutes (only minutes are allowed)
		Integer timeToCollectData = intTime*60;                     //in seconds

		//measurements types and units together
		String measurements;    

		//measurements types and units as two tokens
		String[] tokens;

		String[] measurementsTypesToken;     //measurement types token
		String[] measurementsUnitsToken;     //measurement units token

		//measurements from the user
		System.out.println("defaults measurements are humidity, temperature and pressure, provide all measurements types and their units separated by a comma ");
		System.out.println("For example: Temperature,Pressure F,Kpa");


		measurements = in.nextLine();
		tokens = measurements.split(" ");

		measurementsTypesToken = tokens[0].split(",");
		measurementsUnitsToken = tokens[1].split(",");

		ArrayList<String> measurementTypesList = new ArrayList<String>();  // measurements type array list
		ArrayList<String> measurementUnitsList = new ArrayList<String>();  // measurements units array list

		//measurementTypes list
		for (int i=0; i<measurementsTypesToken.length;i++)
		{
			//System.out.println("----::::::types::::::::s---" + measurementsTypesToken[i]);
			measurementTypesList.add(measurementsTypesToken[i].replaceAll(" ", ""));       //add each measurement to a linked list    //remove all spaces in a word
		}

		//measurementUnits list
		for (int i=0; i<measurementsUnitsToken.length;i++)
		{
			//System.out.println("----::::::::units::::::s---" + measurementsUnitsToken[i]);
			measurementUnitsList.add(measurementsUnitsToken[i].replaceAll(" ", ""));
		}

		//		System.out.println ("size of all measurements" + measurementTypesList.size());  
		//		System.out.println ("size of all measurements" + measurementUnitsList.size());  

		//return an error if the measurements types are not equal to the measurement units
		if(measurementTypesList.size() != measurementUnitsList.size())
		{
			System.out.println("the measurements types are not equal to the measurements units");
		}

		// Here we ask the user to press enter to begin data collection from the server...
		System.out.println( "\n\nPress enter to begin data collection...\n" );  				
		junk = UserInput.readLine();     //option


		// Send the start string to the server to begin collecting data...
		outToServer.write(measurementTypesList.size());     //size of new measurements to add

		outToServer.writeBytes(START);		

		float[] randomValues = new float [measurementTypesList.size()];    //default measurements added

//		for (int i=0; i<timeToCollectData; i++)
		for (int i=0; i<10; i++)
		{
			// Here we read the data from the socket...

			Hours = inFromServer.readInt();
			Minutes = inFromServer.readInt();
			Seconds = inFromServer.readInt();

			String time = Hours + ":" + Minutes + ":" + Seconds; 
			
			//TIME SPAN OF THE DATA COLLECTED
			if (findTimeSpan == 0)
			{
				startTime = time;
			}
			else if (findTimeSpan == 10-1)
			{
				endTime = time;
			}

			findTimeSpan ++;

			LinkedList<Float> values = new LinkedList<Float>();        //list of values



			for(int j=0;j<randomValues.length;j++)
			{
				randomValues[j]=inFromServer.readFloat();    ///no need
				values.add(randomValues[j]);                                 //get measurement values
				map.put(j, measurementTypesList.get(j));
				mapUnits.put(j, measurementUnitsList.get(j));
			}

			// each line with data and measurement values
			Data eachLine = new Data(time, values);
			System.out.println(eachLine.getTime() + eachLine.getValues());     //time with measurements types

			//create a line
			String line = eachLine.getTime();
			for(int j=0;j<values.size();j++)
			{
				line += " " + map.get(j) + " " + values.get(j) + mapUnits.get(j);
			}

			//print each line
			System.out.println(">>>>>>" + line);

			//create the file 
			fw.write(line + "\r\n");

		} // for

		// Closing the socket is a signal to the DataGen server that you don't want any more data.
		clientSocket.close();

		//close the filewriter
		fw.close();
		
		
		//copy the file from the user's file to the 
		copyFile (filename);
		
	}

  
	public void readFile(String filename, Integer time ) throws IOException
	{
		//read the file you created		
		FileReader file;
		try {
			file = new FileReader(filename);
			reader = new BufferedReader(file);

			String line;  //current line in the file
			int jump = 1; 
			System.out.println("__________"+ time);
			int percentage = time/10;

			while((line = reader.readLine()) != null)
			{
				if(jump % percentage == 0) 
				{
					System.out.println( percentage/3 + "% read " + line);
				}
				jump = jump + 1;

			}	
		}  
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}  //end readFile()

	
	
	public void copyFile (String filename)
    {	
    	FileInputStream instream = null;
    	FileOutputStream outstream = null;
 
    	try{
    		//file to copy from
    	    File outfile =new File(filename);
    	    
    	    //file to copy to
    	    File infile =new File("C://Users/jyamfashije/new_workspace/Sensor/out.txt");
 
    	    instream = new FileInputStream(infile);
    	    outstream = new FileOutputStream(outfile);
 
    	    byte[] buffer = new byte[1024];
 
    	    int length;
    	    /*copying the contents from input stream to
    	     * output stream using read and write methods
    	     */
    	    while ((length = instream.read(buffer)) > 0){
    	    	outstream.write(buffer, 0, length);
    	    }

    	    //Closing the input/output file streams
    	    instream.close();
    	    outstream.close();

    	    System.out.println("File copied successfully!!");
    	   
//    	    //clear the file
//    	    PrintWriter writer = new PrintWriter("out.txt");
//    	    writer.print("");
//    	    writer.close();
    	    
    	}catch(IOException ioe){
    		ioe.printStackTrace();
    	 }
    }
	
	public void summary(String filename) {
		System.out.println("Summary function -- 1");



		//measurements types map
		System.out.println("The measurement types in the file are");
		for (Entry<Integer, String> entry : map.entrySet()) {
			System.out.print("  " + entry.getValue());
		}
		System.out.println("");
		//measurements units map
		System.out.println("The measurement units in the file are");
		for (Entry<Integer, String> entry2 : mapUnits.entrySet()) {
			System.out.print("  " + entry2.getValue());
		}
		System.out.println("\n");


//		//display the measurements type
//		System.out.println("Measurement collected are: " + "Time" + ", " + "Humidity" + ", " +  "Temperature" + ", " + "Pressure");
//
//		//display the measurements units
//		System.out.println("Measurement units for time, temperature and pressure are: " + "hours:minutes:seconds" + ", " + "Fahrenheit" + ", " + "kPa");


		//the file size: data collected (in bytes)
		File file = new File(filename);

		if(!file.exists() || !file.isFile())
		{
			System.out.println("File does not exist");
		}
		else
		{
			long size = file.length();
			System.out.println("Filesize in bytes: " + size);
		}

		//the time span of the data
		System.out.println("The starting time when you started collecting the data is: " + startTime);
		System.out.println("The ending time when you started collecting the data is:" + endTime);
		
		

	}


	public void search() {
		System.out.println("Search function -- 2");
		String searchValue= " Temperature = 20";
		String[] tokens = searchValue.split(" ");

		for (int i=1; i<tokens.length; i++)
		{
			System.out.println("-----------");
			System.out.println(tokens[i]);
		}
	}


	public void readData(String filename) throws IOException  
	{
		System.out.println("readData function -- 3");
		System.out.println("Please enter the starting time for the data you want to read");
		String startingTime = in.nextLine();
		System.out.println("Please enter the ending time for the data you want to read");
		String endingTime = in.nextLine();
		
		String measurementsOfInterest;
		//measurements from the user
		System.out.println("enter all the measurements of interest, provide all measurements types and their units separated by a comma ");
		System.out.println("For example: Temperature,Pressure F,Kpa");     //in the assumption
		measurementsOfInterest = in.nextLine();
		
		if (startingTime.compareTo(startTime) < 0 | endingTime.compareTo(endTime) > 0 )
		{
			System.out.println("The entered time is not valid");
		}
		
		//read the file you created		
		FileReader file;
		try {
			file = new FileReader(filename);
			reader2 = new BufferedReader(file);

			String line;  //current line in the file
			Integer lineCount = 0;    //size of the file from starting time and ending time

			String[] tokens;    //get the tokens from a line
			FileWriter choosenFile = new FileWriter("choosenDataFile.txt"); 
			
			while((line = reader2.readLine()) != null)
			{
				//System.out.println("Here -------------" + line);
				lineCount++;
				//System.out.println("Here -------------" + lineCount);
				tokens = line.split(" ");     //clean each line

				String timeToken = tokens[0];
				LinkedList<String> userChoosenData = new LinkedList<String>();
				//start collecting the data if the token is equal to the starting time

				if(timeToken.compareTo(startingTime) >= 0 & timeToken.compareTo(endingTime) <=0  )
				{
					//							str1.compareTo(str2);
					//							If str1 is lexicographically less than str2, a negative number will be returned, 
					//							0 if equal or a positive number if str1 is greater. 
					//store all data applying to the condition in the linkedlist
					userChoosenData.add(line);
					choosenFile.write(line + "\r\n");
				}

				// loop through the linked list of the data choosen by the user from the starting time to the ending time
				for (int i = 0; i < userChoosenData.size(); i++) {
					System.out.println( " --------------" + userChoosenData.get(i));
				}
			}
			choosenFile.close();
		}  
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	}

	@Override
	public void readData() {
		// TODO Auto-generated method stub
		
	}

	


}
