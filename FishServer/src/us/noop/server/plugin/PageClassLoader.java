package us.noop.server.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import us.noop.server.Main;
import us.noop.server.Server;
import us.noop.server.pages.Page;
import us.noop.server.util.FileManager;

public class PageClassLoader extends ClassLoader {
	
	private Main instance;
	
	public PageClassLoader(Main instance, Server sv){
		try {
			File[] dir = FileManager.getFile(true, "externalpages").listFiles();
			ArrayList<Page> loadedPages = new ArrayList<Page>();
			for(File f : dir){
				try {
					loadedPages.add(loadClass(f));
				} catch (InstantiationException e) {
					instance.getLogger().logEx(e);
				} catch (IllegalAccessException e) {
					instance.getLogger().logEx(e);
				} catch (IllegalArgumentException e) {
					instance.getLogger().logEx(e);
				} catch (InvocationTargetException e) {
					instance.getLogger().logEx(e);
				} catch (NoSuchMethodException e) {
					instance.getLogger().logEx(e);
				} catch (SecurityException e) {
					instance.getLogger().logEx(e);
				} catch (ClassFormatError e) {
					instance.getLogger().logEr(e);
				}
			}
			for(Page p : loadedPages){
				sv.addPage(p);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Page loadClass(File file) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassFormatError, IOException{
		byte[] data = new byte[(int) file.length()];
		FileInputStream f = new FileInputStream(file);
		f.read(data);
		f.close();
		return (Page) defineClass(null, data, 0, data.length).getConstructor().newInstance();
	}
}
