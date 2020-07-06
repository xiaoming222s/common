package com.unity.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * 裁剪、缩放图片工具类
 *
 * <p>
 * create by jiaww at 2018/10/27 16:11
 */
public class PicCut {

    public static File cut(int x, int y, int width, int height, InputStream is, String subpath,InputStream is1) throws IOException {
        ImageInputStream iis = null;
        try {
            String fileSuffix = getFileSuffix(is1);
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(StringUtils.isEmpty(fileSuffix) ? "jpg":fileSuffix); //ImageReader声称能够解码指定格式
            ImageReader reader = it.next();
            iis = ImageIO.createImageInputStream(is); //获取图片流
            reader.setInput(iis, true); //将iis标记为true（只向前搜索）意味着包含在输入源中的图像将只按顺序读取
            ImageReadParam param = reader.getDefaultReadParam(); //指定如何在输入时从 Java Image I/O框架的上下文中的流转换一幅图像或一组图像
            Rectangle rect = new Rectangle(x, y, width, height); //定义空间中的一个区域
            param.setSourceRegion(rect); //提供一个 BufferedImage，将其用作解码像素数据的目标。
            BufferedImage bi = reader.read(0, param); //读取索引imageIndex指定的对象
            File file = new File(subpath);
            ImageIO.write(bi, fileSuffix, file); //保存新图片
            return file;
        } finally {
            if (is1 != null)
                is.close();
            if (is != null)
                is.close();
            if (iis != null)
                iis.close();
        }
    }


    private static String getFileSuffix(final InputStream is) throws IOException {
        String result = "";
        String hex="";
        byte[] bt = new byte[2];
        is.read(bt);
        hex=bytesToHexString(bt);
        is.close();
        if(hex.equals("ffd8")){
            result="jpg";
        }else if(hex.equals("4749")){
            result="gif";
        }else if(hex.equals("8950")){
            result="png";
        }else {
            result="png";
        }

        return result;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


}
