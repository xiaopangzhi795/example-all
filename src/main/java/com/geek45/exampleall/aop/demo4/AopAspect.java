package com.geek45.exampleall.aop.demo4;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Lazy(false)
public class AopAspect {

    @Pointcut("execution(* com.geek45.exampleall.aop.demo4.*.*(..))")
    public void authonPoincut() {

    }

    @Around(value = "authonPoincut()")
    public Object authon(ProceedingJoinPoint point) throws NoSuchMethodException {
//        String methodName = point.getSignature().getName();
        System.out.println("拦截");
        Authon authon = getDeclaredAnnotation(point);
        HttpServletRequest request= ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("author")) {
                String author = cookie.getValue();
                if (!author.equals(authon.auth())) {
                    System.err.println("失败");
                    return "没有权限";
                }else{
                    System.out.println("成功");
                    try {
                        return point.proceed();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        }
        return "请先登录";
    }

    /**
     * 获取方法中声明的注解
     *
     * @param joinPoint
     * @return
     * @throws NoSuchMethodException
     */
    public Authon getDeclaredAnnotation(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        // 获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 反射获取目标类
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // 拿到方法对应的参数类型
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        // 根据类、方法、参数类型（重载）获取到方法的具体信息
        Method objMethod = targetClass.getMethod(methodName, parameterTypes);
        // 拿到方法定义的注解信息
        Authon annotation = objMethod.getDeclaredAnnotation(Authon.class);
        // 返回
        return annotation;
    }
}
