package tamajit;

import java.io.*;
import java.util.*;
import java.net.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

import java.net.URL;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class ExtractUtil {

	public static List<String> process(String input, String raw_archive_type, String output, boolean removePath) {

		List<String> unzipped_list;

		try {

			String InputZiplist_fname = input;
			String OutputZip_HDFS_dir = output;
			// OutputZip_HDFS_dir = OutputZip_HDFS_dir+"/"+ new
			// Path(input).getName().replaceAll("[^a-zA-Z0-9]","_").replaceAll("^_+","");
			boolean removeJunkPath = removePath;
			Configuration conf = new Configuration();
			FileSystem fileSystem = FileSystem.get(conf);

			// Tika tika = new Tika();
			// FSDataInputStream ft = fileSystem.open(new Path(input));
			// String input_file_type = tika.detect(ft).toLowerCase();

			String input_file_type = _IdentifyArchivalType(raw_archive_type);

			System.out.printf(
					"Archival Type : [" + input_file_type + "] raw-archival type info[" + raw_archive_type + "]\n");

			if (input_file_type.equalsIgnoreCase("zip")) {
				ZipUtil zip_obj = new ZipUtil();

				/*
				 * .............................................................
				 * .........
				 *
				 * Verify the outcomes of unZip() (method of ZipUtility class)
				 *
				 * .............................................................
				 * .........
				 */

				unzipped_list = zip_obj.unZip(InputZiplist_fname, OutputZip_HDFS_dir, removeJunkPath);

				if (unzipped_list == null || unzipped_list.isEmpty()) {
					System.err.println(InputZiplist_fname + ": UNZIP failed from directory");
				} else {
					System.out
							.printf("Successfully unzipped - the input file [" + InputZiplist_fname + "] to hdfs dir ["
									+ OutputZip_HDFS_dir + "] containing=>[" + unzipped_list.size() + "] files\n");
				}

				return unzipped_list;

			}
			/*
			 * else if( input_file_type.equalsIgnoreCase("tar") ){ TarUtility
			 * tar_obj = new TarUtility();
			 * 
			 * 
			 * .................................................................
			 * .....
			 *
			 * Verify the outcomes of unTar() (method of TarUtility class)
			 *
			 * .................................................................
			 * .....
			 * 
			 * 
			 * unzipped_list =
			 * tar_obj.unTar(InputZiplist_fname,OutputZip_HDFS_dir,
			 * removeJunkPath);
			 * 
			 * if ( unzipped_list == null || unzipped_list.isEmpty()) {
			 * System.err.println(InputZiplist_fname+
			 * ": UNTAR failed from directory"); } else { System.out.println(
			 * "Successfully untared - the input file ["+InputZiplist_fname+
			 * "] to hdfs dir ["+OutputZip_HDFS_dir+"] containing=>["
			 * +unzipped_list.size()+"] files"); }
			 * 
			 * return unzipped_list;
			 * 
			 * } else if( input_file_type.equalsIgnoreCase("gzip") ) {
			 * 
			 * TarUtility gzip_obj = new TarUtility();
			 * 
			 * 
			 * .................................................................
			 * .....
			 *
			 * Verify the outcomes of unTar() (method of TarUtility class)
			 *
			 * .................................................................
			 * .....
			 * 
			 * System.out.println("before calling uncompressTarGZ()");
			 * unzipped_list =
			 * gzip_obj.uncompressTarGZ(InputZiplist_fname,OutputZip_HDFS_dir,
			 * removeJunkPath); System.out.println(
			 * "after calling uncompressTarGZ()");
			 * 
			 * if ( unzipped_list == null || unzipped_list.isEmpty()) {
			 * System.err.println(InputZiplist_fname+
			 * ": UncompressGZ failed from directory"); } else {
			 * System.out.println(
			 * "Successfully Uncompress GZ - the input file ["
			 * +InputZiplist_fname+"] to hdfs dir ["+OutputZip_HDFS_dir+
			 * "] containing=>["+unzipped_list.size()+"] files"); }
			 * 
			 * return unzipped_list;
			 * 
			 * } else if( input_file_type.equalsIgnoreCase("Empty_Archive") ){
			 * System.out.printf("The archive file is empty.\n");
			 * 
			 * }
			 */
			else {
				System.out
						.printf("Not a supported archive format [" + raw_archive_type + "]- provide zip Or tar file\n");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ArrayList<String>();
	}

	/*
	 * ------------------------------------------------------------------------
	 * - Name - _IdentifyArchivalType - Description - Derives the archival
	 * format type from the 3rd argument of the part file
	 * (Data_Move_UNIX_to_HDFS
	 * /Data_Move/UNIX_to_HDFS/ADM_P_29289_20160801.tar|tar archive) -
	 * "tar archive" ~ 3rd argument. -
	 * ------------------------------------------------------------------------
	 */
	private static String _IdentifyArchivalType(String Third_section_Of_PartfileInfo) {

		String archive_format_type = new String();

		HashMap<String, String> supported_archival_types_hashMap = new HashMap<String, String>();
		List<String> archival_type_list = new ArrayList<String>(); // To keep
																	// the
																	// precedence
																	// of lookup
																	// in place
																	// - using
																	// list<>

		// .......................................................
		//
		// populate the archival_type_list - need to update in future as well if
		// needed.
		//
		// .......................................................
		archival_type_list.add("tar");
		archival_type_list.add("bzip2");
		archival_type_list.add("gzip");
		archival_type_list.add("zip");
		archival_type_list.add("empty");
		archival_type_list.add("data");

		// .......................................................
		//
		// update the supported_archival_types_hashMap accordingly.
		//
		// .......................................................
		for (String archival_type : archival_type_list) {

			if (archival_type.matches("^(tar|bzip2)$")) {
				supported_archival_types_hashMap.put(archival_type, "Tar");

			} else if (archival_type.matches("^(zip)$")) {
				supported_archival_types_hashMap.put(archival_type, "Zip");

			} else if (archival_type.matches("^(gzip)$")) {
				supported_archival_types_hashMap.put(archival_type, "GZip");

			} else if (archival_type.matches("^(data|empty)$")) {
				supported_archival_types_hashMap.put(archival_type, "Empty_Archive");
			}
		}

		for (String each_supported_archival_type : archival_type_list) {

			// Note - The matching is case-insentive ~ (?i) is being used in the
			// pattern!!

			if (Third_section_Of_PartfileInfo.matches("(?i)^.*?" + each_supported_archival_type + ".*?$")) {

				if (supported_archival_types_hashMap.containsKey(each_supported_archival_type)) {
					archive_format_type = supported_archival_types_hashMap.get(each_supported_archival_type);
				}
				break;
			}
		}

		if (archive_format_type.isEmpty()) {
			archive_format_type = "Not supported archive format";
		}

		return archive_format_type;
	}
	// ----------------------------------------------------------------------------
}
