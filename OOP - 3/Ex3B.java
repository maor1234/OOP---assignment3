package OOP_matala3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ex3B {
	/**
	 * creates number of text files
	 * @param n number of files
	 * @return array of files names
	 */
	public static String[] createFiles(int n) {
		Random r = new Random(123); 
		String[] ans= new String[n];
		for (int i = 0; i < n; i++) {
			try {
				int numLines = r.nextInt(1000);
				FileWriter fr = new FileWriter("File_"+(i+1)+".txt");
				BufferedWriter br = new BufferedWriter(fr);
				ans[i] =  "File_"+(i+1);
				for (int j = 0; j < numLines; j++) {
					br.write("Hello World\n");
				}
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ans;
	}

	/**
	 * deletes file name
	 * @param fileNames files names
	 */
	public static void deleteFiles(String[] fileNames) {

		for (int i = 0; i < fileNames.length; i++) {
			try{ 
				Files.deleteIfExists(Paths.get(fileNames[i])); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}

	/**
	 * creates files, for each file prints the total number of line in all files using thread.
	 *  prints the time of threads (without creating and deleting file time).
	 *  at the end - deletes all files
	 * @param numFiles number of files
	 */
	public static void countLinesThreads(int numFiles) {

		String [] fileNames = createFiles(numFiles);
		LineCounter [] tr = new LineCounter[numFiles];
		long start = System.currentTimeMillis();
		int k = 0;
		String temp = "";

		for (int i = 0; i < fileNames.length; i++) {
			temp = fileNames[i];
			tr[k++] = new LineCounter(temp+".txt");
			tr[i].start();
		}
	/*	for (Thread thread : tr) {
			try {
				thread.join();
			}catch (InterruptedException e) {
			}
		}*/
		for (Thread thread : tr) {
			while(thread.isAlive()) {
			}
		}
		int sum =0;
		for (int i = 0; i < tr.length; i++) {
			sum += tr[i].count;
		}
		long end = System.currentTimeMillis();
		System.out.println(String.format("Threads time = %d ms, lines = %d",end-start,sum));
		deleteFiles(fileNames);
	}
	/**
	 * creates files using ThreadPool, prints total number of lines in all files
	 *  prints the time of threads (without creating and deleting file time).
	 *  at the end - deletes all files
	 * @param num number of files
	 */
	public static void countLinesThreadPool(int num) {

		String files[] = createFiles(num);
		int sumOfRows = 0;
		ExecutorService pool = Executors.newFixedThreadPool(num);
		Future<Integer> results[] = new Future[num];
		long Start = System.currentTimeMillis();
		for (int i = 0; i < num; i++) {
			results[i] = pool.submit(new Task(files[i]+".txt"));
			/*try {
				sumOfRows += results[i].get();
			} catch (InterruptedException ex) {
				Logger.getLogger(Ex3B.class.getName()).log(Level.SEVERE, null, ex);
			} catch (ExecutionException ex) {
				Logger.getLogger(Ex3B.class.getName()).log(Level.SEVERE, null, ex);
			}*/
		}
		for (int i = 0; i < num; i++) {
			//results[i] = pool.submit(new Task(files[i]+".txt"));
			try {
				sumOfRows += results[i].get();
			} catch (InterruptedException ex) {
				Logger.getLogger(Ex3B.class.getName()).log(Level.SEVERE, null, ex);
			} catch (ExecutionException ex) {
				Logger.getLogger(Ex3B.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		pool.shutdown();
		long End = System.currentTimeMillis();
		System.out.println(String.format("ThreadPool time = %d ms, lines = %d",End - Start, sumOfRows));
		deleteFiles(files);
	}
	/**
	 * class which implements Callable to read/write files using threadPool
	 * @author user
	 *
	 */
	static class Task implements Callable<Integer>{
		String fileName;
		public int count;
		public Task(String fileName){
			this.fileName = fileName;
			count = 0;
		}

		@Override
		public Integer call() throws Exception {
			try {
				FileReader fr = new FileReader(fileName);
				BufferedReader br = new BufferedReader(fr);

				while(br.readLine()!= null) {
					count++;
				}
				br.close();
				fr.close();
			} 
			catch (IOException ex) {
				System.out.println("Error reading file" + ex);
			}
			return count;
		}
	}

	/**
	 * calculates the total number of lines without threads
	 * creates files, reads files one by one, prints the total number and run time (without creating and deleting file time). 
	 * @param numFiles number of files
	 */
	public static void countLinesOneProcess(int numFiles) {
		String[] files = createFiles(numFiles);
		int sum = 0;
		long Start = System.currentTimeMillis();
		for (int i = 0; i < numFiles; i++) {
			try {
				FileReader fr = new FileReader(files[i]+".txt");
				BufferedReader br = new BufferedReader(fr);
				while( br.readLine()!=null) {
					sum++;
				}
				br.close();
				fr.close();
			} catch (IOException ex) {
				System.out.println("Error reading file" + ex);
			}
		}
		long End = System.currentTimeMillis();
		System.out.println(String.format("OneProcess time = %d ms, lines = %d",End - Start, sum));
		deleteFiles(files);
	}

	public static void main(String[] args) {
		int num = 1000;
		countLinesThreads(num);
		countLinesOneProcess(num);
		countLinesThreadPool(num);
	}
}
