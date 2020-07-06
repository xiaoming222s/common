package com.unity.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.unity.common.exception.UnityRuntimeException;
import com.unity.common.pojos.SystemResponse;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.EnumMap;
import java.util.Map;


/**
 * 二维码工具类
 * @author zqc
 * @version 2019-03-03
 */
public class QrCodeUtil {
    private static final int BLACK = -16777216;
    private static final int WHITE = -1;
    private static final int defaultWidth = 200; //二维码默认宽度
    private static final int defaultHeight = 200;//二维码默认高度
    private static final String defaultFormat = "jpg";//默认图片格式
    private static final EnumMap<EncodeHintType, Object> hints = new EnumMap(EncodeHintType.class);

    public QrCodeUtil() {
    }

    /**
     * @desc <一句话功能简述>
     * <功能详细描述>
     * @param contents 二维码包含的内容
     * @param width  二维码宽度（为空则使用默认宽高）
     * @param height 二维码高度
     * @date 2019年3月15日 13:45
     * @return	java.awt.image.BufferedImage 内存图片对象
     * @exception
     */
    private static BufferedImage encodeImg(String contents, Integer width, Integer height) {
        BufferedImage image = null;
        width = width == null || width <= 0 ? defaultWidth : width;
        height = height == null || height <= 0 ? defaultHeight : height;

        try {
            BitMatrix matrix = (new MultiFormatWriter()).encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
            image = new BufferedImage(width, height, 1);


            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    image.setRGB(x, y, matrix.get(x, y) ? -16777216 : -1);
                }
            }

            return image;
        } catch (Exception var7) {
            throw UnityRuntimeException.newInstance().code(SystemResponse.FormalErrorCode.ORIGINAL_DATA_ERR).message("二维码生成失败").build();
        }
    }


    /**
     * @desc <带logo的二维码>
     * <将logo图加入到原始二维码图片中间位置>
     * @param twodimensioncode 原始二维码队形
     * @param logoImg   要加入的logo图
     * @date 2019年3月15日 13:46
     * @return	java.awt.image.BufferedImage
     * @exception
     */
    private static BufferedImage encodeImgLogo(BufferedImage twodimensioncode, File logoImg) {

        try {

            Graphics2D g = twodimensioncode.createGraphics();
            BufferedImage logo = ImageIO.read(logoImg);
            int logoWidth = logo.getWidth((ImageObserver) null) > twodimensioncode.getWidth() * 2 / 10 ? twodimensioncode.getWidth() * 2 / 10 : logo.getWidth((ImageObserver) null);
            int logoHeight = logo.getHeight((ImageObserver) null) > twodimensioncode.getHeight() * 2 / 10 ? twodimensioncode.getHeight() * 2 / 10 : logo.getHeight((ImageObserver) null);
            int x = (twodimensioncode.getWidth() - logoWidth) / 2;
            int y = (twodimensioncode.getHeight() - logoHeight) / 2;
            g.drawImage(logo, x, y, logoWidth, logoHeight, (ImageObserver) null);
            g.drawRoundRect(x, y, logoWidth, logoHeight, 15, 15);
            g.setStroke(new BasicStroke(2.0F));
            g.setColor(Color.WHITE);
            g.drawRect(x, y, logoWidth, logoHeight);
            g.dispose();
            logo.flush();
            twodimensioncode.flush();
        } catch (Exception var9) {
            throw UnityRuntimeException.newInstance().code(SystemResponse.FormalErrorCode.ORIGINAL_DATA_ERR).message("二维码绘制LOGO失败").build();
        }

        return twodimensioncode;
    }

    /**
     * @desc <获取每行字符数>
     * <根据图片宽度计算每行字符数>
     * @param strnum    字符总长度
     * @param rowWidth  每行宽度（图片宽度）
     * @param strWidth  字符宽度
     * @date 2019年3月15日 13:49
     * @return	int 每行字符数
     * @exception
     */
    private static int getRowStrNum(int strnum, int rowWidth, int strWidth) {
        int rowstrnum = 0;
        rowstrnum = (rowWidth * strnum) / strWidth;
        return rowstrnum;
    }

    /**
     * @desc <获取总行数>
     * <根据字符宽度和图片宽度计算出总行数>
     * @param strWidth  字符总宽度
     * @param rowWidth  一行宽度(图片宽度）
     * @date 2019年3月15日 13:52
     * @return	int 总行数
     * @exception
     */
    private static int getRows(int strWidth, int rowWidth) {
        int rows = 0;
        if (strWidth % rowWidth > 0) {
            rows = strWidth / rowWidth + 1;
        } else {
            rows = strWidth / rowWidth;
        }
        return rows;
    }

    /**
     * @desc <将画布背景修改为白色>
     * <将画布背景修改为白色并加载原始二维码图片>
     * @param g 画笔
     * @param src    原始二维码图片
     * @param imageW 二维码宽度
     * @param imageH 二维码高度
     * @param fillHeight 需要填充的画布高度
     * @param color  自定义画笔颜色
     * @date 2019年3月15日 13:54
     * @return	void
     * @exception
     */
    private static void drawQr2NewImg(Graphics2D g, BufferedImage src, int imageW, int imageH, int fillHeight, Color color) {
        //设置图片背景为白色
        g.setColor(Color.WHITE);//设置笔刷白色
        g.fillRect(0, 0, imageW, fillHeight);//填充整个画布


        g.drawImage(src, 0, 0, imageW, imageH, null);
        //设置画笔的颜色为用户自定义的颜色
        g.setColor(color);
    }

    /**
     * @desc <生成带文字描述的二维码图片>
     * <生成带文字描述的二维码图片>
     * @param src   原始二维码图片
     * @param pressText 要加入的文字描述
     * @param fontSize  文字大小
     * @param fontStyle 文字样式
     * @param color 文字颜色
     * @date 2019年3月15日 13:57
     * @return	java.awt.image.BufferedImage
     * @exception
     */
    public static BufferedImage encodeImgTxt(BufferedImage src, String pressText, Integer fontSize, Integer fontStyle, Color color) {

        BufferedImage qrCodeTxtImg = null;
        fontSize = fontSize == null || fontSize <= 0 ? 20 : fontSize;
        fontStyle = fontStyle == null || fontStyle < 0 ? 0 : fontStyle;
        color = color == null ? Color.black : color;
        pressText = StringUtils.isEmpty(pressText)?"":pressText;

        try {

            pressText = new String(pressText.getBytes(), "utf-8");

            int imageW = src.getWidth(null);
            int imageH = src.getHeight(null);

            int loc_Y = imageH + 30;
            int loc_X = 15;


            qrCodeTxtImg = new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = qrCodeTxtImg.createGraphics();
            //设置字体
            Font font = new Font("宋体", fontStyle, fontSize);
            g.setFont(font);


            //获取字符串 字符的总宽度
            int strWidth = g.getFontMetrics().stringWidth(pressText);
            //每一行字符串宽度
            int rowWidth = imageW - 20;
            //获取字符高度
            int strHeight = g.getFontMetrics().getHeight();
            //字符串总个数

            //如果字符宽度大于图片宽度
            if (strWidth > rowWidth) {
                //获取每行字符数
                int rowstrnum = getRowStrNum(pressText.length(), rowWidth, strWidth);
                //获取字符总行数
                int rows = getRows(strWidth, rowWidth);
                //新建画布，高度为二维码高度加字符总高度
                qrCodeTxtImg = new BufferedImage(imageW, loc_Y + rows * strHeight, BufferedImage.TYPE_INT_RGB);
                g = qrCodeTxtImg.createGraphics();
                //设置字体
                g.setFont(font);
                //填充背景为白色
                drawQr2NewImg(g, src, imageW, imageH, loc_Y + rows * strHeight, color);
                String temp = "";
                for (int i = 0; i < rows; i++) {
                    //获取各行的String
                    if (i == rows - 1) {
                        //最后一行
                        temp = pressText.substring(i * rowstrnum);
                    } else {
                        temp = pressText.substring(i * rowstrnum, i * rowstrnum + rowstrnum);
                    }
                    if (i > 0) {
                        //第一行不需要增加字符高度，以后的每一行在换行的时候都需要增加字符高度
                        loc_Y = loc_Y + strHeight;
                    }
                    g.drawString(temp, loc_X, loc_Y);
                }
            } else {
                qrCodeTxtImg = new BufferedImage(imageW, loc_Y + strHeight, BufferedImage.TYPE_INT_RGB);
                g = qrCodeTxtImg.createGraphics();
                //设置字体
                g.setFont(font);
                drawQr2NewImg(g, src, imageW, imageH, loc_Y + strHeight, color);
                //直接绘制
                g.drawString(pressText, (imageW - strWidth) / 2, loc_Y);
            }
            g.dispose();
        } catch (Exception var9) {
            throw UnityRuntimeException.newInstance().code(SystemResponse.FormalErrorCode.ORIGINAL_DATA_ERR).message("二维码绘制文字失败").build();
        }

        return qrCodeTxtImg;
    }


    /**
     * @desc <生成二维码>
     * <生成二维码并读取到文件里>
     * @param contents 二维码包含的内容
     * @param file      二维码文件对象
     * Map参数 key 说明
     *
     * format       图片格式
     * width        生成二维码图片的宽度
     * height       生成二维码图片的高度
     * logoImg      二维码中间的Logo图
     * note         二维码下方的文字性描述
     * fontSize     描述的字体大小
     * fontStyle    描述的字体样式
     * color        描述的字体颜色
     * @date 2019年3月15日 14:19
     * @return	java.io.OutputStream
     * @exception
     */
    public static void writeToFile(String contents, File file, Map<String,Object> map) throws IOException {
        String format=defaultFormat;
        String note = "";
        int width = defaultWidth;
        int height = defaultHeight;
        File logoImg = null;
        int fontSize = 0;
        int fontStyle = -1;
        Color color = null;

        if(map != null){
            format = map.get("format") == null ? defaultFormat : String.valueOf(map.get("format"));

            width = map.get("width") == null ? width:Integer.parseInt(String.valueOf(map.get("width")));

            height = map.get("height") == null ? height:Integer.parseInt(String.valueOf(map.get("height")));

            logoImg = map.get("logoImg") == null ? null : (File) map.get("logoImg");

            note = map.get("note") == null ? note : String.valueOf(map.get("note"));
            fontSize = map.get("fontSize") == null ? fontSize : Integer.parseInt(String.valueOf(map.get("fontSize")));
            fontStyle = map.get("fontStyle") == null ? fontStyle : Integer.parseInt(String.valueOf(map.get("fontStyle")));
            color = map.get("color") == null ? null : (Color) map.get("color");
        }



        BufferedImage image = encodeImg(contents, width, height);
        if (logoImg != null && logoImg.isFile()) {
            image = encodeImgLogo(image, logoImg);
        }
        if (StringUtils.isNotEmpty(note)) {
            image = encodeImgTxt(image, note, fontSize, fontStyle, color);
        }

        try {
            ImageIO.write(image, format, file);
        } catch (IOException var5) {
            throw UnityRuntimeException.newInstance().code(SystemResponse.FormalErrorCode.ORIGINAL_DATA_ERR).message("二维码写入文件失败").build();
        }
    }

    /**
     * @desc <生成二维码>
     * <生成二维码并读取到输出流里>
     * @param contents  二维码包含的内容
     * @param stream    内存流对象
     * Map参数 key 说明
     *
     * format       图片格式
     * width        生成二维码图片的宽度
     * height       生成二维码图片的高度
     * logoImg      二维码中间的Logo图
     * note         二维码下方的文字性描述
     * fontSize     描述的字体大小
     * fontStyle    描述的字体样式
     * color        描述的字体颜色
     * @date 2019年3月15日 14:19
     * @return	java.io.OutputStream
     * @exception
     */
    public static OutputStream writeToStream(String contents, OutputStream stream,Map<String,Object> map) {
        String format=defaultFormat;
        String note = "";
        int width = defaultWidth;
        int height = defaultHeight;
        File logoImg = null;
        int fontSize = 0;
        int fontStyle = -1;
        Color color = null;

        if(map != null){
            format = map.get("format") == null ? defaultFormat : String.valueOf(map.get("format"));

            width = map.get("width") == null ? width:Integer.parseInt(String.valueOf(map.get("width")));

            height = map.get("height") == null ? height:Integer.parseInt(String.valueOf(map.get("height")));

            logoImg = map.get("logoImg") == null ? null : (File) map.get("logoImg");

            note = map.get("note") == null ? note : String.valueOf(map.get("note"));
            fontSize = map.get("fontSize") == null ? fontSize : Integer.parseInt(String.valueOf(map.get("fontSize")));
            fontStyle = map.get("fontStyle") == null ? fontStyle : Integer.parseInt(String.valueOf(map.get("fontStyle")));
            color = map.get("color") == null ? null : (Color) map.get("color");
        }

        BufferedImage image = encodeImg(contents, width, height);
        if (logoImg!=null && logoImg.isFile()) {
            image = encodeImgLogo(image, logoImg);
        }
        if (StringUtils.isNotEmpty(note)) {
            image = encodeImgTxt(image, note, fontSize, fontStyle, color);
        }

        try {
            ImageIO.write(image, format, stream);
            return stream;
        } catch (IOException var5) {
            throw UnityRuntimeException.newInstance().code(SystemResponse.FormalErrorCode.ORIGINAL_DATA_ERR).message("二维码写入流失败").build();
        }
    }

    static {
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 1);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MAX_SIZE, 350);
        hints.put(EncodeHintType.MIN_SIZE, 150);
    }
}
