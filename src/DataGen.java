/*
 * Decompiled with CFR 0_114.
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
//import java.io.InputStream;
import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.PrintStream;
//import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Random;

class DataGen {
    DataGen() {
    }

    public static void main(String[] arrstring) throws Exception {
        int n;
        int n2 = 1000;
        if (arrstring.length > 0) {
            try {
                n = new Integer(arrstring[0]);             
            }
            catch (Exception var16_3) {
                n = 6789; 
                System.out.println("\nSpecified port number on command line " + arrstring[0] + " is invalid as port number.");
                System.out.println("\nDefault port number:: " + n + " will be used.");
            }
        } else {
            n = 6789;
        }
        System.out.println("\n\n*******************************************************");
        System.out.println("DataGen Server V1.0 ");
        System.out.println("Anthony J. Lattanze - Carnegie Mellon University");
        System.out.println("Computer Science for Practicing Engineers (17630)");
        System.out.println("\nServer listening on port: " + n);
        System.out.println("You can specify a different port on the command line.");
        System.out.println("*******************************************************");
        System.out.println("\n\nWaiting for a client connection...");
        System.out.println("Press Ctrl-C to stop DataGen.");
        do {
            ServerSocket serverSocket = new ServerSocket(6789);
            Socket socket = serverSocket.accept();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            float f = DataGen.GetRandomNumber() * 100.0f;
            //float f2 = DataGen.GetRandomNumber() * 100.0f;
            //float f3 = 101.3f;
            System.out.println("\n\n*******************************************************");
            System.out.println("Waiting for OK to send data stream...");
            System.out.println("******************************************************\n\n");
            
            int measurementSize = bufferedReader.read();     //get the additional measurements size from the client
            
            int size = measurementSize;
            System.out.println("----------------" + size);      //total size of all measurements to provide to the client
            //String string = bufferedReader.readLine();
            boolean bl = false;
            while (!bl) {
            	
            	float[] randomValues = new float [size];
            	
            	for (int i = 0; i<size; i++)
            	{
            		randomValues[i] = f+(DataGen.CoinToss() ? DataGen.GetRandomNumber() * -1.0f : DataGen.GetRandomNumber());
            	}
                Calendar calendar = Calendar.getInstance();
                int n3 = calendar.get(11);
                int n4 = calendar.get(12);
                int n5 = calendar.get(13);
                try {
                    dataOutputStream.writeInt(n3);
                    dataOutputStream.writeInt(n4);
                    dataOutputStream.writeInt(n5);
                    for (int i = 0; i<size; i++)
                    {
                    	dataOutputStream.writeFloat(randomValues[i]);
                    	
                    }
                }
                catch (Exception var19_21) {
                    bl = true;
                    serverSocket.close();
                }
                try {
                    Thread.currentThread();
                    Thread.sleep(n2);
                }
                catch (Exception var19_22) {}
            }
            System.out.println("\n\n*******************************************************");
            System.out.println("Client socket closed... service ended.");
            System.out.println("Waiting for a client connection...");
            System.out.println("Press Ctrl-C to stop DataGen.");
            System.out.println("*******************************************************\n\n");
        } while (true);
    }

    private static float GetRandomNumber() {
        Random random = new Random();
        Float f = Float.valueOf(-1.0f);
        while ((double)f.floatValue() < 0.1) {
            f = Float.valueOf(random.nextFloat());
        }
        return f.floatValue();
    }

    private static boolean CoinToss() {
        Random random = new Random();
        return random.nextBoolean();
    }
    
}