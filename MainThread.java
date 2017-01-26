import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainThread {
	
    public static HashMap<String, String> readPasswordFile() throws IOException {
        HashMap<String, String> pwds = new HashMap<String, String>();
        BufferedReader r = null;

        try {
            String line;

            r = new BufferedReader(new FileReader("password.txt"));
            while ((line = r.readLine()) != null) {
                String[] split = line.split(":");
                pwds.put(split[1], split[0]);
             }
        } 
        finally {
            try {
                if (r != null)
                    r.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return pwds;
    }
    public static void writeToFile(String filename, ArrayList<String> output) {
        BufferedWriter w = null;
                try {
                    w = new BufferedWriter(new FileWriter(filename));

                    for (int i = 0; i < output.size(); i++) {
                        w.write(output.get(i));
                        w.newLine();
                        w.flush();
                    }
                } catch (IOException e) {
                    // Catch any IO errors
                    e.printStackTrace();
                }
                finally {
                    try {
                        if (w != null)
                            w.flush();
                           w.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

    }
   
    
    public static void main(String args[]) throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException {
    	HashMap<String, String> passwordMap = readPasswordFile();
    	ArrayList<String> guessOutput = new ArrayList<String>();
    	String currentLine;

    	// Read in the dictionary on the fly and create hashes
    	BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt"));
    	MessageDigest digest = MessageDigest.getInstance("SHA-256");

    	String guess = " ";
    	while ((currentLine = reader.readLine()) != null) {
    		byte[] hash = digest.digest(currentLine.getBytes("UTF-8"));
    		StringBuffer hexString = new StringBuffer();
    		for (int i = 0; i < hash.length; i++) {
    			String hex = Integer.toHexString(0xff & hash[i]);
    			if (hex.length() == 1)
    				hexString.append('0');
    			hexString.append(hex);
        		guess = hexString.toString();
    		}
    		// Check if hashed dictionary entry is in hash map
    		if (passwordMap.containsKey(guess)){
    			// Get the user of the password
    			String user = passwordMap.get(guess);
    			// Append with password we are currently reading
    			String output = user+":"+currentLine;
    			// Add entry to ArrayList
    			guessOutput.add(output);
    			// Output to 'output.txt'
    			writeToFile("output.txt", guessOutput);
    		}
    	}
    	reader.close();
    }
}




