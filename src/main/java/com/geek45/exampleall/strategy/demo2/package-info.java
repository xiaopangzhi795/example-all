/**
 * 策略模式2
 *
 *  使用抽象类，实现接口，并给出统一的返回值。
 *  不同实现集成抽象类，选择性实现。如果抽象类已经实现的，可以不用实现。抽象类没有实现的，属于必须实现的方法。
 *  调用接口的地方，统一装载HandlerService进行调用，HandlerService内部去获取不同的实现，然后给出结果。
 *
 *  运行顺序，方式：
 *      项目运行的时候，HandlerConfig自动装载配置文件中的配置，将类型对应的实现类名字自动装载进来。
 *      接口装载HandlerService的实现：HandlerServiceImpl，根据@Qualifier("default")来精准装载，否则有多个实现会装载错误。
 *      调用HandlerServiceImpl里面的实现
 *      HandlerServiceImpl通过type去获取对应的实现类名字。
 *      判断缓存是否为空
 *          如果为空，通过spring上下文对象工具类，获取AbstractHandler抽象类下的所有子类，并自动返回service名字为key，bean对象为v的map对象。
 *      通过实现类的名字，从缓存中拿到具体的实现转化为接口，返回给调用者
 *      调用者拿到具体的接口实现，调用对应的方法，然后获取到具体的内容。
 *
 */
package com.geek45.exampleall.strategy.demo2;