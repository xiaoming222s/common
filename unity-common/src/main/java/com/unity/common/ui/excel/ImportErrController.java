//package com.unity.common.ui.excel;
//
//import com.alibaba.druid.util.Base64;
//import com.alibaba.druid.util.StringUtils;
//import com.unity.common.base.SessionHolder;
//import com.unity.common.exception.UnityRuntimeException;
//import com.unity.common.util.Encodes;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletResponse;
//
//@Slf4j
//@Controller
//@RequestMapping("importErr")
//public class ImportErrController {
//
//    @Resource
//    private RedisTemplate<String, Object> redisTemplate;
//
//    @RequestMapping("/excel")
//    public String moduleEntrance(String key, HttpServletResponse res) {
//        String name = "导入数据校验失败";
//        //响应头信息
//        res.setCharacterEncoding("UTF-8");
//        res.setContentType("application/ms-excel; charset=UTF-8");
//        res.setHeader("Content-disposition", "attachment; filename=" + Encodes.urlEncode(name) + ".xlsx");
//
////        String keyinfo = ConstString.IMPORT_EXCEL_ERR+"_"+ SessionHolder.getToken();
////        System.out.println("====="+keyinfo+"======");
////        log.info("b==="+redisTemplate.opsForValue().get(keyinfo));
////        byte[] b = Base64.altBase64ToByteArray((String)redisTemplate.opsForValue().get(keyinfo));
////        redisTemplate.delete(keyinfo);
////        try{
////            res.getOutputStream().write(b);
////        }
////        catch(Exception ex){
////            String fileName="出错了";
////            ExportEntity excel =  ExcelEntity.exportEntity(res);
////            excel.exportError(fileName,ex);
////        }
//
//        try {
//            if (StringUtils.isEmpty(key)) {
//                throw new UnityRuntimeException("key");
//            }
//            byte[] b = Base64.altBase64ToByteArray(SessionHolder.getCatch().get(key));
//            log.info("b=文件大小=========="+b.length);
//            //文件下載后直接刪除
//            SessionHolder.getCatch().remove(key);
//            res.getOutputStream().write(b);
//        } catch (Exception ex) {
//            String fileName = "出错了";
//            ExportEntity excel = ExcelEntity.exportEntity(res);
//            excel.exportError(fileName, ex);
//        }
//        return "";
//    }
//}
