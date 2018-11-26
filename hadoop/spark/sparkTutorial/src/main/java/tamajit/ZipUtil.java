package tamajit;
 
import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileNotFoundException; 
import java.io.FileOutputStream; 
import java.io.IOException; 
import java.io.InputStream; 
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList; 
import java.util.List; 
 
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry; 
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream; 
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream; 
import org.apache.commons.compress.utils.IOUtils; 

import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

import java.util.Arrays;


public class ZipUtil { 
	
    private static boolean removeJunkPath = false;
    /**
     * Create a new Zip from a root directory 
     *  
     * @param directory 
     *            the base directory 
     * @param filename 
     *            the output filename 
     * @param absolute 
     *            store absolute filepath (from directory) or only filename 
     * @return True if OK 
     */ 
    public static boolean createZipFromDirectory(String directory, String filename, boolean absolute) throws IOException { 
        File rootDir = new File(directory); 
        File saveFile = new File(filename); 

	Configuration conf = new Configuration();
	//FileSystem fileSystem = FileSystem.get(conf);
	FileSystem fileSystem = FileSystem.get(URI.create(filename), conf);//manish
        // recursive call 
        ZipArchiveOutputStream zaos; 
        try { 
		FSDataOutputStream out = fileSystem.create(new Path(saveFile.toString()));	
		zaos = new ZipArchiveOutputStream(out); 
        } catch (FileNotFoundException e) { 
            return false; 
        } 
        try { 
            recurseFiles( new Path(rootDir.toString()), new Path(rootDir.toString()), zaos, absolute); 
        } catch (IOException e2) { 
            try { 
                zaos.close(); 
            } catch (IOException e) { 
                // ignore 
            } 
            return false; 
        } 
        try { 
            zaos.finish(); 
        } catch (IOException e1) { 
            // ignore 
        } 
        try { 
            zaos.flush(); 
        } catch (IOException e) { 
            // ignore 
        } 
        try { 
            zaos.close(); 
        } catch (IOException e) { 
            // ignore 
        } 

        return true; 
    } 
 
    /**
     * Recursive traversal to add files 
     *  
     * @param root 
     * @param file 
     * @param zaos 
     * @param absolute 
     * @throws IOException 
     */ 
    private static void recurseFiles(Path root, Path file, ZipArchiveOutputStream zaos, 
            boolean absolute) throws IOException { 

	Configuration conf = new Configuration();
	//FileSystem fileSystem = FileSystem.get(conf);
	FileSystem fileSystem = FileSystem.get(URI.create(file.toString()), conf);//manish
        if ( fileSystem.isDirectory(file)) { 
            // recursive call 
            FileStatus[] files = fileSystem.listStatus(file); 
            for (FileStatus file2 : files) { 
		Path filePath_obj = new Path (file2.getPath().toString());
                recurseFiles(root, filePath_obj, zaos, absolute); 
            } 

        } else if ( (!file.getName().endsWith(".zip")) && (!file.getName().endsWith(".ZIP")) ) { 

            String filename = null; 

            if (absolute) { 
		String root_uri = root.makeQualified(fileSystem).toString()+"/" ;
		filename = file.toString().substring(root_uri.length());

            } else { 
                filename = file.getName();
            } 

            ZipArchiveEntry zae = new ZipArchiveEntry(filename); 
            zae.setSize(fileSystem.getLength(file)); 
            zaos.putArchiveEntry(zae); 
            FSDataInputStream fis = fileSystem.open( file ); 
            IOUtils.copy(fis, zaos); 
            zaos.closeArchiveEntry(); 
        } 
    } 
 
    /**
     * Create a new Zip from a list of Files (only name of files will be used) 
     *  
     * @param files 
     *            list of files to add 
     * @param filename 
     *            the output filename 
     * @return True if OK 
     */ 
    public static boolean createZipFromFiles(List<FileStatus> files, String filename) { 
        return createZipFromFiles(files.toArray(new FileStatus[] {}), filename); 
    } 
 
    /**
     * Create a new Zip from an array of Files (only name of files will be used) 
     *  
     * @param files 
     *            array of files to add 
     * @param filename 
     *            the output filename 
     * @return True if OK 
     */ 
    public static boolean createZipFromFiles(FileStatus[] files, String filename) { 
        File saveFile = new File(filename); 
        ZipArchiveOutputStream zaos; 
        try { 
		Configuration conf = new Configuration();
	        //FileSystem fileSystem = FileSystem.get(conf); manish
		FileSystem fileSystem = FileSystem.get(URI.create(filename), conf);//manish
		FSDataOutputStream out = fileSystem.create(new Path(filename));
                zaos = new ZipArchiveOutputStream(out);
            	//zaos = new ZipArchiveOutputStream(new FileOutputStream(saveFile)); 
        } catch (Exception e) { 
            return false; 
        } 
        for (FileStatus file : files) { 
            try { 
                addFile(file, zaos); 
            } catch (IOException e) { 
                try { 
                    zaos.close(); 
                } catch (IOException e1) { 
                    // ignore 
                } 
                return false; 
            } 
        } 
        try { 
            zaos.finish(); 
        } catch (IOException e1) { 
            // ignore 
        } 
        try { 
            zaos.flush(); 
        } catch (IOException e) { 
            // ignore 
        } 
        try { 
            zaos.close(); 
        } catch (IOException e) { 
            // ignore 
        } 
        return true; 
    } 
 
    /**
     * Recursive traversal to add files 
     *  
     * @param file 
     * @param zaos 
     * @throws IOException 
     */ 
    private static void addFile(FileStatus file, ZipArchiveOutputStream zaos) throws IOException { 
        String filename = null; 
        filename = file.getPath().getName(); 
	Configuration conf = new Configuration();
        //FileSystem fileSystem = FileSystem.get(conf); manish
	FileSystem fileSystem = FileSystem.get(URI.create(filename), conf);//manish
        
	ZipArchiveEntry zae = new ZipArchiveEntry(filename);
        zae.setSize(fileSystem.getLength(file.getPath()));
        zaos.putArchiveEntry(zae);
        FSDataInputStream fis = fileSystem.open( file.getPath() );
        IOUtils.copy(fis, zaos);
        zaos.closeArchiveEntry();
    } 
 
	/**
	* Extract all files from Tar into the specified directory 
	*  
	* @param tarFile 
	* @param directory 
	* @return the list of extracted filenames 
	* @throws IOException 
	*/ 
	public static List<String> unZip(String tarFile, String unzipped_directory) throws IOException {
		List<String> result = new ArrayList<String>(); 

		File directory =  new File( unzipped_directory );

		Configuration conf = new Configuration();
		//FileSystem fileSystem = FileSystem.get(conf); manish
		Path new_unZipped_FilePath ;
		FileSystem fileSystem = FileSystem.get(URI.create(new Path(tarFile).toString()), conf);//manish

		FSDataInputStream inputStream = fileSystem.open(new Path(tarFile)); 
		ZipArchiveInputStream in = new ZipArchiveInputStream(inputStream); 
		ZipArchiveEntry entry = in.getNextZipEntry(); 
		while (entry != null) {
			if (entry.isDirectory()) { 
				entry = in.getNextZipEntry(); 
				continue; 
			} 
			File curfile = new File(directory, entry.getName()); 
			File parent = curfile.getParentFile(); 
			if (!parent.exists()) { 
				parent.mkdirs(); 
			} 

			String new_file_name = curfile.toString();
			if(removeJunkPath){
				new_file_name = curfile.toString().replace(unzipped_directory,"").replaceAll("[^A-Za-z0-9.]","_").replaceAll("^_+","");
			}

			FSDataOutputStream out = fileSystem.create(new Path(unzipped_directory,new_file_name)); 
			IOUtils.copy(in, out); 
			out.close(); 

			new_unZipped_FilePath = new Path ( unzipped_directory,new_file_name );
			result.add( ( fileSystem.getFileStatus( new_unZipped_FilePath ).getPath() ).toString() ); 

			entry = in.getNextZipEntry(); 
		} 
		in.close(); 
		return result; 
	}

    	public List<String> unZip(String tarFile, String unzipped_directory, boolean removeDirStructure) throws IOException{
		if(removeDirStructure){
			removeJunkPath = removeDirStructure;
		}
		return unZip(tarFile, unzipped_directory);
	}
}
