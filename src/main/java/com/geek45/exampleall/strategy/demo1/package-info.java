/**
 * 策略模式
 *
 * 通过handler进行动态选择具体处理的实现类
 *
 *      handlerType:   用来标识具体不同的实现。
 *      AbstractHandler:    抽象类，抽象方法，对外提供的统一实现方法。
 *      HandlerContext：     核心处理所有的适配器，初始化完成之后，将所有的适配器保存到该对象中，该对象的单例模式。需要的时候，根据标识去获取对应的handler
 *          getInstance:    获取适配器的具体实现类，如果缓存中是空的，那就通过包扫描到的所有适配器名字，获取到对应的类，然后获取类上面的注解。注解内容当做key，将类保存到缓存中
*      HandlerProcessor：    该类继承了BeanFactoryPostProcessor，项目启动的时候，扫描指定包下面的所有类，获取有指定注解的所有类，然后将类放到list中，然后将list保存在HandlerContext
 *      MyClassPathDefinitonScanner：    自定义的包扫描器
*      ScannerUtil：         包扫描工具类，使用该方法进行扫描包
 *      SpringContextUtil：      spring 的上下文对象工具类
 *      TestHandler：        测试具体实现的handler适配器
 *
 *  运行顺序，方式：
 *          1、HandlerProcessor  执行该类，调用包扫描工具类 ScannerUtil 扫描指定包路径下的所有包。
 *          2、ScannerUtil  工具类里面使用自定义的扫描工具  MyClassPathDefinitonScanner  进行扫描
 *          3、将扫描的内容整合，获取到java bean的名字，保存到map中，返回给 HandlerProcessor
 *          4、HandlerProcessor 将内容整合成list，将list保存到 HandlerContext 中。
 *          5、单元测试中，自动装载单例的HandlerContext
 *          6、HandlerContext通过传过来的type值进行获取到对应具体的TestHandler实现
 *              6.1、HandlerContext判断缓存是否为空
 *                  6.1.1、如果为空，遍历list，获取到所有的bean对象的className
 *                  6.1.2、使用spring上下文对象工具类，通过className获取到具体的bean
 *                  6.1.3、读取bean对象的类注解。
 *                  6.1.4、注解值为key，bean为v，创建缓存对象。
 *              6.2、根据传过来的值，去缓存中获取具体的bean对象。
 *              6.3、将bean对象以父对象的形式返回出去。
 *          7、获取到AbstractHandler父对象，然后调用抽象方法，自动调用到子类实现。
 *
 *
 */
package com.geek45.exampleall.strategy.demo1;