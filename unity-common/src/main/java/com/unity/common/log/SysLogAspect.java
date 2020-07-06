package com.unity.common.log;

import com.alibaba.fastjson.JSON;
import com.unity.common.pojos.AuthUser;
import com.unity.common.util.IPUtils;
import com.unity.springboot.support.holder.LoginContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 系统日志：切面处理类
 */
@Aspect
@Component
public class SysLogAspect {


    private static final Logger logger = LoggerFactory.getLogger(SysLogAspect.class);

    @Autowired
    private SysLogDao mongoDao;

    private static int STATUS_CODE = 1; // 方法执行的状态；1:正常，0：异常
    private static Long EXECUTE_TIME = 0L; // 方法执行时长

    //定义切点 @Pointcut
    //在注解的位置切入代码
    @Pointcut("@annotation(com.unity.common.log.MyLog)")
    public void logPoinCut() {
    }

    //声明环绕通知
    @Around("logPoinCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 定义返回对象、得到方法需要的参数
        Object[] args = joinPoint.getArgs();
        long startTime = System.currentTimeMillis();
        Object obj = joinPoint.proceed(args);
        // 获取执行的方法名
        long endTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
        // 打印耗时的信息
        this.printExecTime(methodName, startTime, endTime);
        return obj;
    }

    /**
     * 打印方法执行耗时的信息，如果超过了一定的时间，才打印
     *
     * @param methodName
     * @param startTime
     * @param endTime
     */
    private void printExecTime(String methodName, long startTime, long endTime) {
        long diffTime = endTime - startTime;
        //超过1秒的记录
        if (diffTime > 1000) {
            logger.info(methodName + ":" + diffTime + " :ms");
        }
        // 方法执行时长  毫秒(ms)
        EXECUTE_TIME = diffTime;
    }

    //切面 配置通知
    @AfterReturning(value = "logPoinCut()", returning = "result")
    public void AfterReturning(JoinPoint joinPoint, Object result) {
        STATUS_CODE = 1;
        saveSysLog(joinPoint, null);
    }

    @AfterThrowing(value = "logPoinCut()", throwing = "e")
    public void logException(JoinPoint joinPoint, Exception e) {
        STATUS_CODE = 0;
        try {
            saveSysLog(joinPoint, e.toString());
        } catch (Exception ex) {
            logger.error("记录操作日志出现异常：{}", ex.getMessage());
        }
    }

    public void saveSysLog(JoinPoint joinPoint, String errorMsg) {

        SysLog sysLog = new SysLog();

        sysLog.setStatusCode(STATUS_CODE);
        // 错误信息
        if (STATUS_CODE == 1) {
            sysLog.setErrorMsg("操作正常");
        } else {
            sysLog.setErrorMsg(errorMsg);
        }
        sysLog.setExecuteTime(EXECUTE_TIME);
        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        //获取操作
        MyLog myLog = method.getAnnotation(MyLog.class);
        if (myLog != null) {
            String value = myLog.value();
            sysLog.setOperation(value);//保存获取的操作
        }
        //获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
        String methodName = method.getName();
        sysLog.setMethod(className + "." + methodName);

        //请求的参数
        Object[] args = joinPoint.getArgs();
        //将参数所在的数组转换成json
        String params = JSON.toJSONString(args);
        sysLog.setParams(params);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date();
        String str = sdf.format(d);
        sysLog.setCreateDate(str);
        sysLog.setGmtCreate(d.getTime());
        // 访问id
        sysLog.setIp(IPUtils.getCurentIP());
        try {
            AuthUser authUser = LoginContextHolder.getRequestAttributes();
            sysLog.setUsername(authUser.getId() + "." + authUser.getName());
        } catch (Exception e) {
            logger.error("记录操作日志 获取登陆用户失败:{}", sysLog);
            sysLog.setUsername("未登陆");
        }
        // 保存到 mongodb
        mongoDao.save(sysLog);
//        logger.info("\n------>>>当前id: " + sysLog.getId().toString());
    }

}
