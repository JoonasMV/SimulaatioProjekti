package simu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/*** 
 * @author Olli Varila
 * <p>
 * Utility class for writing PalvelupisteData to file
 * <p>
 * @deprecated Ei käytetä koska käytetään tietokantaa
 */
public final class SimuFileHandler {

	
	/***
	 * Utility class not meant to be instantiated.
	 */
	private SimuFileHandler() {
		
	}
	
	/***
	 * Write data to file.
	 *<p>
	 * You can choose to append to file or overwrite the file.
	 *<p>
	 *@deprecated
	 * 
	 * @param file 	 File where data is written
	 * @param data	 List of PalvelupisteData objects written to the file
	 * @param append Append to file or not
	 * 
	 * @throws NullPointerException If any of the arguments is null
	 */
	@Deprecated
	public static void write(File file, List<PalvelupisteData> data, boolean append) {
		
		try(
			FileOutputStream fileOut = new FileOutputStream(file, append);
			ObjectOutputStream objWriter = new ObjectOutputStream(fileOut);
			){
			for(PalvelupisteData dataPoint : data) {
				objWriter.writeObject(dataPoint);
			}
			System.out.printf("Wrote data to file %s\n", file.toString());
		} catch (FileNotFoundException e) {
			System.err.println("File not found for writing");
		} catch (IOException e) {
			System.err.println("Something went wrong when writing to file");
		}
	}
	
	/***
	 * Read data from file.
	 * 
	 * @deprecated
	 * 
	 * @param file File to be read from
 	 * @return	   List containing PalvelupisteData, empty if nothing is read
 	 * 
 	 * @throws NullPointerException If file is null
	 */
	@Deprecated
	public static List<PalvelupisteData> read(File file) {
		List<PalvelupisteData> data = new ArrayList<PalvelupisteData>();
		try (
			 FileInputStream inputFile = new FileInputStream(file);
			 ObjectInputStream reader = new ObjectInputStream(inputFile);
			){
			while(inputFile.available() > 0) {
				try {
					PalvelupisteData dataPoint = (PalvelupisteData) reader.readObject();
					data.add(dataPoint);
				} catch (ClassNotFoundException e) {
					System.out.println("Class not found when casting to PalvelupisteData");
					e.printStackTrace();
				} 
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found for reading");
		} catch (IOException e) {
			System.out.println("Something went wrong with reading");
		}
		
		return data;
	}
	
	/***
	 * Creates a file in the specified path.
	 * @see {@link java.io.File}
	 * 
	 * @deprecated
	 * 
	 * @param pathname Path for the file
	 * @return		   File that was created
	 * 
	 * @throws NullPointerException If the pathname argument is null
	 */
	@Deprecated
	public static File createFile(String pathname) {
		
		return new File(pathname);
	}
}
