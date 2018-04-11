package SysFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

public class Chunk {
	public static final int MAX_LENGTH=8192;

    public ArrayList<String> readAndFragment ( String pathFile,String fileName, int CHUNK_SIZE ) throws IOException {

		String SourceFileName=pathFile+"\\"+fileName;
		String fileName2 =fileName.split("\\.")[0];
		File willBeRead = new File ( SourceFileName );
		int FILE_SIZE = (int) willBeRead.length();
		ArrayList<String> nameList = new ArrayList<String> ();
		
	  
		System.out.println("Total File Size: "+FILE_SIZE);
	  
		int NUMBER_OF_CHUNKS = 0;
		byte[] temporary = null;
	  
		try {
			InputStream inStream = null;
			int totalBytesRead = 0;
			try {
				inStream = new BufferedInputStream ( new FileInputStream( willBeRead ));
				while ( totalBytesRead < FILE_SIZE ){
					String PART_NAME ="data_"+NUMBER_OF_CHUNKS+".bin";
					int bytesRemaining = FILE_SIZE-totalBytesRead;
					if ( bytesRemaining < CHUNK_SIZE ){
						// Remaining Data Part is Smaller Than CHUNK_SIZE
						// CHUNK_SIZE is assigned to remain volume
						CHUNK_SIZE = bytesRemaining;
						System.out.println("CHUNK_SIZE: "+CHUNK_SIZE);
					}
					temporary = new byte[CHUNK_SIZE]; //Temporary Byte Array
					int bytesRead = inStream.read(temporary, 0, CHUNK_SIZE);
	     
					if ( bytesRead > 0) {// If bytes read is not empty
						totalBytesRead += bytesRead;
						NUMBER_OF_CHUNKS++;
					}
					write(temporary, pathFile+"\\"+fileName2+"_"+PART_NAME);
					nameList.add(pathFile+"\\"+fileName2+"_"+PART_NAME);
					System.out.println("Total Bytes Read: "+totalBytesRead);
				}
			}
			finally {
				inStream.close();
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return nameList;
	}
	 
	public void write(byte[] DataByteArray, String DestinationFileName){
	    try {
			OutputStream output = null;
			try {
				output = new BufferedOutputStream(new FileOutputStream(DestinationFileName));
				output.write( DataByteArray );
				//System.out.println("Writing Process Was Performed");
			}
			finally {
				output.close();
			}
		} catch(FileNotFoundException ex){
			ex.printStackTrace();
	    }catch(IOException ex){
			ex.printStackTrace();
	    }
	}
	 
	public void mergeParts ( ArrayList<String> nameList, String DESTINATION_PATH ) {

		File[] file = new File[nameList.size()];
		byte AllFilesContent[] = null;
	  
		int TOTAL_SIZE = 0;
		int FILE_NUMBER = nameList.size();
		int FILE_LENGTH = 0;
		int CURRENT_LENGTH=0;
	  
		for ( int i=0; i<FILE_NUMBER; i++) {
			file[i] = new File (nameList.get(i));
			TOTAL_SIZE+=file[i].length();
		}
	  
		try {

			AllFilesContent= new byte[TOTAL_SIZE]; // Length of All Files, Total Size
			InputStream inStream = null;
	   
			for ( int j=0; j<FILE_NUMBER; j++) {
				inStream = new BufferedInputStream ( new FileInputStream( file[j] ));
				FILE_LENGTH = (int) file[j].length();
				inStream.read(AllFilesContent, CURRENT_LENGTH, FILE_LENGTH);
				CURRENT_LENGTH+=FILE_LENGTH;
				inStream.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found " + e);
		} catch (IOException ioe){
			System.out.println("Exception while reading the file " + ioe);
		} finally {
			write (AllFilesContent,DESTINATION_PATH);
		}
		System.out.println("Merge was executed successfully.!");
	}

	public void create ( String pathFile,String fileName, int CHUNK_SIZE ) {

		boolean found=false;
		String PART_NAME;
		String fileName2 =fileName.split("//.")[0];
		byte[] bytes = new byte[CHUNK_SIZE];
		try {
			SecureRandom.getInstanceStrong().nextBytes(bytes);
		} catch (NoSuchAlgorithmException ex ) {
			ex.printStackTrace();
		}
			
		File f = new File(pathFile);
		File[] filesFolder= f.listFiles();

		
		for (File file:filesFolder) {
			if (file.isFile() && !found) {
				if (file.getName().split("_")[0].equals(fileName2)){
					found=true;
					int lastC=this.lastChunk(fileName, pathFile);
					lastC++;
					PART_NAME="data_"+lastC+".bin";
					write (bytes,pathFile+"//"+fileName2+"_"+PART_NAME);
				}	
			}
		}
		if (!found) {
			PART_NAME ="data_"+0+".bin";
			write (bytes,pathFile+"//"+fileName2+"_"+PART_NAME);
			System.out.println("[INFO] New chunk "+fileName2+"_"+PART_NAME+" created");
		}

	}

	public boolean append (int appended_size,String fileName,String pathFile){
		boolean sucess=false;

		if (!(appended_size>MAX_LENGTH)) {
			String SourceFileName;
			SourceFileName=this.lastChunkName(fileName, pathFile);
			//String SourceFileName=pathFile+"//"+fileName;
			File f = new File(SourceFileName);
			String fileName2=f.getName().split("_")[0];
			long S=f.length();
			int tempo=(int) S;
			int diference=MAX_LENGTH-tempo;
			System.out.println("[append] tempo: "+tempo);
			System.out.println("[append] diference: "+diference);
			System.out.println("[append] appended_size: "+appended_size);

			if ( (diference > 0) && (diference < appended_size)  ) {
				//Refill last chunk with null
				byte[] bytesNull = new byte[diference];
				
				if (f.exists()){
					try {
						Files.write(f.toPath(),bytesNull,StandardOpenOption.APPEND);
						System.out.println("[INFO] File has been appended");
						sucess=true;
					} catch (IOException ex) {
						ex.printStackTrace();
						sucess=false;
					}
				} else {
					System.err.println("[ERROR] File not exist. No possible to append");
					sucess=false;
				}
				// Create new Chunk
				this.create(pathFile, fileName2, appended_size);

			} else if ((diference > 0) && (diference >=appended_size)  ) {
				// Creating ramdon bytes
				byte[] bytes = new byte[appended_size];
				try {
					SecureRandom.getInstanceStrong().nextBytes(bytes);
				} catch (NoSuchAlgorithmException ex ) {
					ex.printStackTrace();
				}
				// Append operation
				if (f.exists()){
					try {
						Files.write(f.toPath(),bytes,StandardOpenOption.APPEND);
						sucess=true;
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				} else {
					System.out.println("[ERROR] File no exist. No possible to append");
					sucess=false;
				}
			} else {
				this.create(pathFile, fileName2, appended_size);
				sucess=true;
			}
		} else {
			System.err.println("[ERROR] Size file bigger than 8192 bytes");
			System.exit(-1);
			sucess=false;
		}
		return sucess;
	}

	public int lastChunk(String fileName,String pathFile){
		int temp=0;
		String binNs;
		int binNi;
		String fileName2 =fileName.split("\\.")[0];
		//String SourceFileName=pathFile+"\\"+fileName;
		File f = new File(pathFile);

		File[] filesFolder= f.listFiles();

		for (File file:filesFolder) {
			if (file.isFile()) {
				if (file.getName().split("_")[0].equals(fileName2)){
					binNs=file.getName().split("_")[2];
					binNs=binNs.split("\\.")[0];
					binNi=Integer.parseInt(binNs);
					if (binNi>temp) {
						temp=binNi;
					}

				}				
			}

		}

		return temp;
	}

	public ArrayList<String> read(String fileName,String pathFile, int startOffset, int endOffset) throws IOException {
		ArrayList<String> readArray = new ArrayList<String>();
		String sourceFileName;

		sourceFileName=pathFile+"//"+fileName;

		File file=null;
		FileInputStream fileStream=new FileInputStream(file=new File(sourceFileName));

		byte[] arr= new byte[(int)file.length()];

		fileStream.read(arr,0,arr.length);

		int counter=0;
		for (int x:arr) {
			
			if ((counter>=startOffset)&&(counter<=endOffset)) {
				readArray.add(Integer.toString(Math.abs(x)));
				//System.out.print((char)Math.abs(x));
			}
			counter++;
		}
		//System.out.println("");
		//System.out.println("readArray: "+readArray);
		//System.out.println("---------------");
		// for (String k:readArray) {
		// 	System.out.println((char)Integer.parseInt(k));
		// }
		fileStream.close();

		return readArray;
	}

	public String lastChunkName(String fileName,String pathFile){
		String nameChunkFile=null;
		int lastchunkNumber;

		lastchunkNumber=this.lastChunk(fileName, pathFile);
		nameChunkFile=pathFile+"//"+fileName+"_data_"+lastchunkNumber+".bin";

		return nameChunkFile;
		
	}

}