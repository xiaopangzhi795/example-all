package com.geek45.exampleall.aop.demo3;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@Lazy(value = false)
public class RubikAspect {

    /**
     * ps:这个方法的实际作用在于头上面的注解，没有其他特殊用处。
     * 该方法标识的包路径，表名这个切点生效的范围。
     *
     * 定义切入点：对要拦截的方法进行定义与限制，如包、类
     *
     * 1、execution(public * *(..)) 任意的公共方法
     * 2、execution（* set*（..）） 以set开头的所有的方法
     * 3、execution（* com.lingyejun.annotation.LoggerApply.*（..））com.lingyejun.annotation.LoggerApply这个类里的所有的方法
     * 4、execution（* com.lingyejun.annotation.*.*（..））com.lingyejun.annotation包下的所有的类的所有的方法
     * 5、execution（* com.lingyejun.annotation..*.*（..））com.lingyejun.annotation包及子包下所有的类的所有的方法
     * 6、execution(* com.lingyejun.annotation..*.*(String,?,Long)) com.lingyejun.annotation包及子包下所有的类的有三个参数，第一个参数为String类型，第二个参数为任意类型，第三个参数为Long类型的方法
     * 7、execution(@annotation(com.lingyejun.annotation.Lingyejun))
     */
    @Pointcut("execution(* com.geek45.exampleall.aop.demo3.*.*(..))")
    private void cutMethod() {}

    /**
     * 前置通知：在目标方法执行前调用
     */
    @Before("cutMethod()")
    public void begin() {
        System.err.println("来了老弟");
    }

    /**
     * 后置通知：在目标方法执行后调用，若目标方法出现异常，则不执行
     */
    @AfterReturning("cutMethod()")
    public void afterReturning() {
        System.err.println("欢迎下次光临");
    }

    /**
     * 后置/最终通知：无论目标方法在执行过程中出现一场都会在它之后调用
     */
    @After("cutMethod()")
    public void after() {
        System.err.println("慢走啊老弟");
    }

    /**
     * 异常通知：目标方法抛出异常时执行
     */
    @AfterThrowing(value = "cutMethod()", throwing = "ex")
    public void afterThrowing(Exception ex){
        System.err.println("非常抱歉老弟，你看这事闹的:" + ex.getMessage());
        System.out.println("你继续~");
//        joinPoint.proceed();
    }

    /**
     * 环绕通知：灵活自由的在目标方法中切入代码
     */
    @Around("cutMethod()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取目标方法的名称
        String methodName = joinPoint.getSignature().getName();
        // 获取方法传入参数
        Object[] params = joinPoint.getArgs();
        Name name = getDeclaredAnnotation(joinPoint);
//        System.out.println("==@Around== lingyejun blog logger --》 method name " + methodName + " args " + params[0]);
        System.err.println("吱哇 开门啦：你可以干这件事：" + methodName);
        // 模拟进行验证
        if (params != null && params.length > 0 && params[0].equals("老钱")) {
            System.out.println("我老婆名字是：》 " + name.name() + "年龄是：" + name.age() + "  你最美了~");
        } else {
            System.out.println(params[0] + "说：她的名字是 --》 " + name.name() + "年龄是：" + name.age() + " 她真美");
        }
        // 执行源方法
        joinPoint.proceed();
    }

    /**
     * 获取方法中声明的注解
     *
     * @param joinPoint
     * @return
     * @throws NoSuchMethodException
     */
    public Name getDeclaredAnnotation(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        // 获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 反射获取目标类
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // 拿到方法对应的参数类型
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        // 根据类、方法、参数类型（重载）获取到方法的具体信息
        Method objMethod = targetClass.getMethod(methodName, parameterTypes);
        // 拿到方法定义的注解信息
        Name annotation = objMethod.getDeclaredAnnotation(Name.class);
        // 返回
        return annotation;
    }
}
