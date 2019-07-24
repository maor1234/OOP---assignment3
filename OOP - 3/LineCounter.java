package OOP_matala3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * represents thread which calculates the number of lines in file
 * @author user
 *
 */
public class LineCounter extends Thread {
	String fileName;
	int count;
	/**
	 * constructor
	 * @param fileName the name of the file we need to read
	 */
	public LineCounter(String fileName) {
		this.fileName = fileName;
		count = 0;
	}
	@Override
	public void run(){

		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);

			while(br.readLine()!=null) {
				count++;
			}
			//System.out.println("count is : " +count);
			br.close();
			fr.close();
		}catch (IOException ex){
			System.out.println("Error reading file" + ex);
		}
	}
}
