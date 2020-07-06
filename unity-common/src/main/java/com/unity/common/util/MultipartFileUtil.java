package com.unity.common.util;


import com.unity.common.exception.UnityRuntimeException;
import com.unity.common.pojos.SystemResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.net.URLEncoder;

/**
 * 上传文件工具类
 * <p>
 * create by zhangxiaogang at 2019/3/1 16:42
 */
@Slf4j
public class MultipartFileUtil {


    /**
     * 模仿文件表单上传创建MultipartFile
     *
     * @param file 源文件
     * @return 可以进行上传的文件
     * @author zhangxiaogang
     * @since 2019/3/1 16:44
     */
    public static MultipartFile createFileItem(File file) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "file";
        FileItem item;
        try {
            item = factory.createItem(textFieldName, MediaType.MULTIPART_FORM_DATA_VALUE, true,
                    URLEncoder.encode(file.getName(), "UTF-8"));
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192))
                    != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            log.info("文件转化异常");
            throw new UnityRuntimeException(SystemResponse.FormalErrorCode.SERVER_ERROR, "文件转化异常");
        }
        return new CommonsMultipartFile(item);
    }

    /**
     * 模仿文件表单上传创建MultipartFile
     *
     * @param fis      源文件流
     * @param fileName 文件名称
     * @return 可以进行上传的文件
     * @author zhangxiaogang
     * @since 2019/3/1 16:44
     */
    public static MultipartFile createFileItemByInputStream(InputStream fis, String fileName) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "file";
        FileItem item;
        try {
            item = factory.createItem(textFieldName, MediaType.MULTIPART_FORM_DATA_VALUE, true,
                    URLEncoder.encode(fileName, "UTF-8"));
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192))
                    != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            log.info("文件转化异常");
            throw new UnityRuntimeException(SystemResponse.FormalErrorCode.SERVER_ERROR, "文件转化异常");
        }
        return new CommonsMultipartFile(item);
    }
}
