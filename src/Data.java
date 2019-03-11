import java.util.LinkedList;
import java.util.ListIterator;

public class Data {
    String time;
    LinkedList<Float> values;

    public Data(String time, LinkedList<Float> values) {
        this.time = time;
        this.values = values;
    } 
    public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public LinkedList<Float> getValues() {
		return values;
	}

	public void setValues(LinkedList<Float> values) {
		this.values = values;
	}

	@Override
    public String toString() {
        
        String toprint=time;
        
        for (int i = 0; i < values.size(); i++) 
        {
            toprint=toprint+", "+values.get(i);  
        } 
        
        toprint = toprint+"\n";

        return toprint; //To change body of generated methods, choose Tools | Templates.
    }
	
	public ListIterator<Data> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}
    
    
}
