package com.unity.common.pojos;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 安全的配置，
 * <p>
 * create by Jung at 2018年02月19日23:03:25
 */
@Component
@ConfigurationProperties(prefix = "system.configuration")
@Data
public class SystemConfiguration {
    /**
     * 文件上传路径
     */

    private String uploadPath;
    /**
     * 二维码生成路径
     */
    private String qrCodePath;
    /**
     * 二维码中间压缩图片路径
     */
    private String logo;


    private String multipartPath;

    //环境域名
    private String domainName;

    /**
     * fsatdfs 服务器文件读取头信息
     */
    private String fastdfsFileReadPathHead;

    /**
     * 监督检查默认图片
     */
    private String supervisionInspectionDefaultPic;
}