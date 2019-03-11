import java.util.LinkedList;
import java.util.ListIterator;

public class Lines 
{

    static LinkedList<Data> allLines=new LinkedList<>();
    static Data oneline;
   
    public Lines()
    {
    	//
    }
    public Lines(Data oneline)
    {
    	this.oneline = oneline;
    }
    public void addLines(Data oneline) 
    {    
        allLines.add(oneline);
    }

	public LinkedList<Data> getAllLines() {
		return allLines;
	}

	public void setAllLines(LinkedList<Data> allLines) {
		Lines.allLines = allLines;
	}

	public Data getOneline() {
		return oneline;
	}

	public void setOneline(Data oneline) {
		Lines.oneline = oneline;
	}
    
    public void printLine()
    {
        
        System.out.println("iterate the linked list: ");
        ListIterator<Data> listIterator = allLines.listIterator();
        while (listIterator.hasNext()) {
            System.out.println(listIterator.next());
        }
    	
    	
    
        
        
//    	allLines.
//    	System.out.println(data.getTime() + " " + data.getValues());
//    
//    	//System.out.println( "Time: " + Hours + ":" + Minutes + ":" + Seconds + "\t- " + " Hum: " + Humidity + " Temp: " + Temperature + "F"  + " Pres: " + Pressure + "kPa" ) ; 
//			
//    	 for (int i = 0; i < allLines.size(); i++) {
//	            System.out.println(allLines.get(i));
//	        }
	    
    }
	@Override
	public String toString() {
		return "Lines [getAllLines()=" + getAllLines() + ", getOneline()=" + getOneline() + "]";
	}
	
    
}