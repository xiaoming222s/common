package com.unity.common.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 文件上传结果信息
 * <p>
 * <p>
 * create by gengjiajia at 2018/09/20 09:53
 */
@Data
@Builder(builderMethodName = "newInstance")
@AllArgsConstructor
public class FastdfsFileDTO {

    private String groupName;
    private String filePath;
    private String fileUrl;
    private String fileName;
    private String base64;
    private String originalFilename;

    public FastdfsFileDTO() {

    }
}
