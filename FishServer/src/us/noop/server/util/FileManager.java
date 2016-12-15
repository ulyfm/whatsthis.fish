package us.noop.server.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
	public static File getFile(boolean dir, String path, String def) throws IOException{
		File f = new File(path);
		if(!f.exists()){
			if(dir){
				f.mkdirs();
			}else{
				f.createNewFile();
				FileWriter fw = new FileWriter(f);
				fw.write(def);
				fw.flush();
				fw.close();
			}
		}
		return f;
	}
	public static File getFile(boolean dir, String path) throws IOException{
		return getFile(dir, path, "");
	}
}
