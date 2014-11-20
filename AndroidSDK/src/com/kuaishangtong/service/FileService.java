package com.kuaishangtong.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class FileService {
	private Context context;
	 
	/** SD卡是否存在**/ 
	private boolean hasSD = false; 
	/** SD卡的路径**/ 
	private String SDPATH; 
	/** 当前程序包的路径**/ 
	private String FILESPATH; 
	
	private String MyVoicePrintDir;
	

	public FileService(Context context) {
		super();
		this.context = context;
		
		hasSD = Environment.getExternalStorageState().equals( 
				android.os.Environment.MEDIA_MOUNTED); 
		SDPATH = Environment.getExternalStorageDirectory().getPath(); 
		MyVoicePrintDir=SDPATH+"/VoicePrintTemp";
		FILESPATH = this.context.getFilesDir().getPath(); 
	}

	/**
	 * 将字符串保存到文件中
	 * @param fileName 文件名
	 * @param fileContent 文件内容
	 */
	public void write(String fileName, String fileContent) throws Exception {
		FileOutputStream stream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		stream.write(fileContent.getBytes());
		stream.close();
	}
	
	/**
	 * 从文件中读取文件内容
	 * @param fileName 文件名
	 * @return 返回文件内容
	 * @throws Exception
	 */
	public String read(String fileName) throws Exception {
		byte[] data = null;
		try{
		ByteArrayOutputStream memStream = new ByteArrayOutputStream();
		FileInputStream stream = context.openFileInput(fileName);
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = stream.read(buffer)) != -1){
			memStream.write(buffer, 0, len);
		}
		data = memStream.toByteArray();
		
		stream.close();
		memStream.close();
		
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return new String(data);
	}
	
	/**
	 * 写文件到SD卡根目录
	 * @param FileName 文件名
	 * @param FileContent 文件内容
	 * @throws Exception
	 */
	public void writeToSDCard(String FileName, String FileContent) throws Exception {
		//File file = new File(Environment.getExternalStorageDirectory(), FileName);
		File file = new File(MyVoicePrintDir, FileName);
		FileOutputStream stream = new FileOutputStream(file);
		stream.write(FileContent.getBytes());
		stream.close();		
	}
	
	/**
	 * 读文件从SD卡根目录
	 * @param FileName 文件名
	 * @return 返回文件内容
	 * @throws Exception
	 */
	public String readFromSDCard(String FileName) throws Exception {			
		ByteArrayOutputStream memStream = new ByteArrayOutputStream();
		File file = new File(Environment.getExternalStorageDirectory(), FileName);		
		FileInputStream stream = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = stream.read(buffer)) != -1){
			memStream.write(buffer, 0, len);
		}
		byte[] data = memStream.toByteArray();		
		stream.close();
		memStream.close();
		return new String(data);
	}
	
	
	
	
	
	public static String SDPATH2 = Environment.getExternalStorageDirectory().getPath()
            + "/VoicePrintTemp/";//获取文件夹
    //保存图片
    public static void saveBitmap(Bitmap bm, String picName) {
        Log.e("", "保存图片");
        Log.d("text", SDPATH2);
        try {
            if (!isFileExist("")) {
                File tempf = createSDDir("");
            }
            File f = new File(SDPATH2, picName + ".JPEG"); 
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.e("", "已经保存");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    //创建文件夹
    public static File createSDDir(String dirName) throws IOException {
        File dir = new File(SDPATH2 + dirName);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
 
        	if (!dir.exists()) {
                //若不存在，创建目录，可以在应用启动的时候创建
        		dir.mkdirs();
               }
            //System.out.println("createSDDir:" + dir.getAbsolutePath());
            //System.out.println("createSDDir:" + dir.mkdir());
        }
        return dir;
    }
 
    public static boolean isFileExist(String fileName) {
        File file = new File(SDPATH2 + fileName);
        file.isFile();
        return file.exists();
    }
    
    //删除文件
    public static void delFile(String fileName){
        File file = new File(SDPATH2 + fileName);
        if(file.isFile()){
            file.delete();
        }
        file.exists();
    }
    
    //删除文件夹和文件夹里面的文件
    public static void deleteDir() {
        File dir = new File(SDPATH2);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
         
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDir(file.getPath()); // 递规的方式删除文件夹
        }
       // dir.delete();// 删除目录本身
    }
    
    public static void deleteDir(String dirName) {
        File dir = new File(dirName);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
         
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDir(dirName); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }
 
    public static List<File> readWavFile() {
    	List<File> files=new ArrayList<File>();
    	
        File dir = new File(SDPATH2+"FinalAudio/");
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return null;
         
        for (File file : dir.listFiles()) {
        	if (file.isFile())   
        	{
            	files.add(file);
        	}
        }
        return files;
    }
    
    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
 
            return false;
        }
        return true;
    }
    
    
    
  //创建文件夹
    public static File createDir(String dirName) throws IOException {
    	String dirpath= Environment.getExternalStorageDirectory().getPath()
        + "/"+dirName+"/";
        File dir = new File(dirpath);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
 
        	if (!dir.exists()) {
                //若不存在，创建目录，可以在应用启动的时候创建
        		dir.mkdirs();
               }
            //System.out.println("createSDDir:" + dir.getAbsolutePath());
            //System.out.println("createSDDir:" + dir.mkdir());
        }
        return dir;
    }
    //删除文件夹及文件夹里面的文件
    public static void deleteDir2(String dirpath) {
        File dir = new File(dirpath);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
         
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDir2(file.getPath()); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }
    
}
