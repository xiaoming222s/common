package com.unity.common.util;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
    /**
     * 设置路径没有新建
     * @param dir
     * @author zhangwei
     * @since 2018/07/02 16:53
     */
    public static void mkdirs(String dir){
        if(StringUtils.isEmpty(dir)){
            return;
        }
        
        File file = new File(dir);
        if(file.isDirectory()){
            return;
        } else {
            file.mkdirs();
        }
    }


    public static String byte2File(byte[] buf, String filePath, String oldFileName)
    {
        String fileName = oldFileName;
        File fileTemp = new File(filePath);
        File[] tempList = fileTemp.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                if (oldFileName.equals(tempList[i].getName())){
                    String suffix = oldFileName.substring(oldFileName.lastIndexOf(".") + 1);
                    String  name = oldFileName.substring(0, oldFileName.lastIndexOf("."));
                    fileName = name + System.currentTimeMillis() + "." + suffix;
                }
            }
        }

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try
        {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory())
            {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            fos.flush();
            bos.write(buf);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bos != null)
            {
                try
                {
                    bos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return filePath + File.separator + fileName;
     }


    public static void delDir(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            File[] tmp = dir.listFiles();
            for (int i = 0; i < tmp.length; i++) {
                if (tmp[i].isDirectory()) {
                    delDir(path + File.separator + tmp[i].getName());
                } else {
                    tmp[i].delete();
                }
            }
            dir.delete();

        }
    }
}