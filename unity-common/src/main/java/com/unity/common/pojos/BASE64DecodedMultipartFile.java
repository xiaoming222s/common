package com.unity.common.pojos;

/**
 * <p>
 * create by qinhuan at 2018/10/29 11:39 AM
 */

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * base64转MultipartFile
 */
public class BASE64DecodedMultipartFile implements MultipartFile {

    private final byte[] imgContent;
    private final String header;

    public BASE64DecodedMultipartFile(byte[] imgContent, String header) {
        this.imgContent = imgContent;
        this.header = header;
    }

    @Override
    public String getName() {
        return header;
    }

    @Override
    public String getOriginalFilename() {
        return header;
    }

    @Override
    public String getContentType() {
        return FilenameUtils.getExtension(header.toLowerCase());
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }

    /**
     * base64转MultipartFile文件
     *
     * @param base64
     * @return
     */
    public static MultipartFile base64ToMultipart(String base64, String originalFilename) {
        try {
            String[] baseStrs = base64.split(",");
            Base64 decoder = new Base64();
            byte[] b = new byte[0];
            b = decoder.decode(baseStrs[0]);

            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }

            return new BASE64DecodedMultipartFile(b, originalFilename);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}