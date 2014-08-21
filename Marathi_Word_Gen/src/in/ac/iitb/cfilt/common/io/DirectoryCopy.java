package in.ac.iitb.cfilt.common.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DirectoryCopy {


	/**
	 * Method 	: copyDirectory
	 * Purpose	: copies all files from source to target path. Creates directories if 
	 * necessary. Source files and directory will remain in there earlier location. 
	 * @param srcPath
	 * @param dstPath
	 * @throws IOException
	 */
	
	public static void copyDirectory(File srcPath, File dstPath) throws IOException {
		moveResources(srcPath, dstPath, true);
	}
	
	/**
	 * Method 	: moveDirectory
	 * Purpose	: moves all files from source to target path. Creates directories if 
	 * necessary. Source files and directory will be deleted after copying. 
	 * @param srcPath
	 * @param dstPath
	 * @throws IOException
	 */
	public static void moveDirectory(File srcPath, File dstPath) throws IOException {
		moveResources(srcPath, dstPath, false);
	}
	
	/**
	 * Method 	: moveDirectory
	 * Purpose	: 
	 * @param srcPath
	 * @param dstPath
	 * @param keepSource
	 * @throws IOException
	 */
	private static void moveResources(File srcPath, File dstPath, boolean keepSource) throws IOException{
		if (srcPath.isDirectory()){
			if (!dstPath.exists()){
				dstPath.mkdirs();
			}

			String files[] = srcPath.list();
			for(int i = 0; i < files.length; i++){
				moveResources(new File(srcPath, files[i]), 
						new File(dstPath, files[i]),
						keepSource);
			}
			
			if(!keepSource) {	// Delete this directory
				delete(srcPath);
			}
		}
		else{
			if(!srcPath.exists()){
				System.out.println("File or directory does not exist:" + srcPath.getAbsolutePath());
				//System.exit(0);
			}
			else
			{
				InputStream in = new FileInputStream(srcPath);
				OutputStream out = new FileOutputStream(dstPath); 
				// Transfer bytes from in to out
				byte[] buf = new byte[1024];

				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			}
		}
	}

	public static boolean delete(File resource) throws IOException{
		if(resource.isDirectory()){	 
			File[] childFiles = resource.listFiles();	 
			for(File child : childFiles){
				delete(child);
			}
		}
		return resource.delete();
	}
	
	public static void main(String[] args) throws IOException {
		File srcDir = new File("temp/output");
		File dstDir = new File("temp1/output");
		
		copyDirectory(srcDir, dstDir);			// Copy
		//moveDirectory(srcDir, dstDir, true);	// Dont delete source. Same as copy dir.		
		//moveDirectory(srcDir, dstDir, false);	// delete source
	}
}

