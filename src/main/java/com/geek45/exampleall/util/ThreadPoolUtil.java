package com.geek45.exampleall.util;

import java.io.Serializable;
import java.util.concurrent.*;

/**
 * 线程池创建辅助类
 */
public class ThreadPoolUtil implements Serializable {
    private static final long serialVersionUID = 1578650421701L;
    /**
     * 默认的最大线程池数量
     */
    private static int THREAD_MAX = Integer.MAX_VALUE;
    /**
     * 默认的最小线程池数量
     */
    private static int THREAD_MIN = 1;
    /**
     * 默认的队列数量
     */
    private static int DEFAULT_QUEUE_SIZE = 1024;
    /**
     * 扩容的线程休眠时的存活时间
     */
    private static int DEFAULT_ALIVE_TIME = 30;
    /**
     * 存活时间的单位
     */
    private static TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    //拒绝策略
    /**
     * 默认的，放不进去会舍弃并抛出异常
     */
    private static RejectedExecutionHandler default_AbortPolicy(){
        return new ThreadPoolExecutor.AbortPolicy();
    }
    /**
     * 放不进去会舍去，不会抛出异常
     */
    private static RejectedExecutionHandler default_DiscardPolicy(){
        return new ThreadPoolExecutor.DiscardPolicy();
    }
    /**
     * 放不进去的会移除最早添加的，然后重试
     */
    private static RejectedExecutionHandler default_DiscardOldestPolicy(){
        return new ThreadPoolExecutor.DiscardOldestPolicy();
    }
    /**
     * 放不进去的由调用线程来执行
     */
    private static RejectedExecutionHandler default_CallerRunsPolicy(){
        return new ThreadPoolExecutor.CallerRunsPolicy();
    }

    //阻塞队列
    //有界--------》需要定义队列的大小
    /**
     * 一个由数组结构组成的有界阻塞队列。
     * @param size 队列大小
     */
    private static BlockingQueue ArrayBlockingQueue(int size){
        return new ArrayBlockingQueue(size);
    }
    /**
     * 一个由链表结构组成的有界阻塞队列。
     * @param size 队列大小
     */
    private static BlockingQueue LinkedBlockingQueue(int size){
        return new LinkedBlockingQueue(size);
    }
    /**
     * 一个由链表结构组成的双向阻塞队列。
     * @param size 队列大小
     */
    private static BlockingQueue LinkedBlockingDeque(int size){
        return new LinkedBlockingDeque(size);
    }
    //无界--------》队列无限大，慎用
    /**
     * 一个支持优先级排序的无界阻塞队列。
     */
    private static BlockingQueue PriorityBlockingQueue(){
        return new PriorityBlockingQueue();
    }
    /**
     * 一个使用优先级队列实现的无界阻塞队列。
     */
    private static BlockingQueue DelayQueue(){
        return new DelayQueue();
    }
    /**
     * 一个由链表结构组成的无界阻塞队列。
     */
    private static BlockingQueue LinkedTransferQueue(){
        return new LinkedTransferQueue();
    }
    //特殊队列，不存放任何元素，用来转接
    /**
     * 一个不存储元素的阻塞队列。
     */
    private static BlockingQueue SynchronousQueue(){
        return new SynchronousQueue();
    }


    /**
     * 创建默认的线程池，此线程会抛弃放不进去的任务，并抛出RejectedExecutionException异常
     * 该方法创建的线程池参数是：
     * 最大线程池数量是核心线程池数量左移一位的大小
     * 超时时间是默认30秒
     * 队列是默认的LinkedBlockingQueue（是一个用链表实现的有界阻塞队列。此队列的默认和最大长度为）
     * 队列大小是，默认为1024，如果核心线程池数量大于了这个默认队列数量，就使用最大线程池数量。否则就用默认的
     * @param coreSize  核心线程池大小
     * @return
     */
    public static ThreadPoolExecutor createDefaultThreadPool(int coreSize) {
        coreSize = getCoreSize(coreSize);
        int maxSize = getMaxSize(coreSize);
        return new ThreadPoolExecutor(coreSize, maxSize, DEFAULT_ALIVE_TIME, DEFAULT_TIME_UNIT, new LinkedBlockingQueue(DEFAULT_QUEUE_SIZE < coreSize ? maxSize : DEFAULT_QUEUE_SIZE));
    }

    /**
     * 创建默认的线程池，此线程池会丢弃放不进去的任务，但不会抛出异常
     * 该方法创建的线程池参数是：
     * 最大线程池数量是核心线程池数量左移一位的大小
     * 超时时间是默认30秒
     * 队列是默认的LinkedBlockingQueue（是一个用链表实现的有界阻塞队列。此队列的默认和最大长度为）
     * 队列大小是，默认为1024，如果核心线程池数量大于了这个默认队列数量，就使用最大线程池数量。否则就用默认的
     * @param coreSize  核心线程池大小
     * @return
     */
    public static ThreadPoolExecutor createDefaultThreadPoolNoEx(int coreSize) {
        coreSize = getCoreSize(coreSize);
        int maxSize = getMaxSize(coreSize);
        return new ThreadPoolExecutor(coreSize, maxSize, DEFAULT_ALIVE_TIME, DEFAULT_TIME_UNIT, new LinkedBlockingQueue(DEFAULT_QUEUE_SIZE < coreSize ? maxSize : DEFAULT_QUEUE_SIZE), default_DiscardPolicy());
    }

    /**
     * 创建默认的线程池，如果任务放不进去的时候，会丢弃掉最前面的任务，然后重新放任务
     * 该方法创建的线程池参数是：
     * 最大线程池数量是核心线程池数量左移一位的大小
     * 超时时间是默认30秒
     * 队列是默认的LinkedBlockingQueue（是一个用链表实现的有界阻塞队列。此队列的默认和最大长度为）
     * 队列大小是，默认为1024，如果核心线程池数量大于了这个默认队列数量，就使用最大线程池数量。否则就用默认的
     * @param coreSize  核心线程池大小
     * @return
     */
    public static ThreadPoolExecutor createDefaultThreadPoolDiscardPrevia(int coreSize) {
        coreSize = getCoreSize(coreSize);
        int maxSize = getMaxSize(coreSize);
        return new ThreadPoolExecutor(coreSize, maxSize, DEFAULT_ALIVE_TIME, DEFAULT_TIME_UNIT, new LinkedBlockingQueue(DEFAULT_QUEUE_SIZE < coreSize ? maxSize : DEFAULT_QUEUE_SIZE), default_DiscardOldestPolicy());
    }

    /**
     * 创建自定义的线程池
     * @param coreSize 核心线程池大小
     * @param maxSize 最大线程池大小
     * @param keepAliveTime 扩容线程池存活时间
     * @param timeUnit 时间单位
     * @param threadQueue 阻塞队列
     */
    public static ThreadPoolExecutor createThreadPool(int coreSize, int maxSize, int keepAliveTime, TimeUnit timeUnit, BlockingDeque threadQueue) {
        coreSize = getCoreSize(coreSize);
        maxSize = getMaxSize(maxSize);
        return new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime, timeUnit, threadQueue);
    }

    /**
     * 创建自定义的线程池
     * @param coreSize 核心线程池大小
     * @param maxSize 最大线程池大小
     * @param keepAliveTime 扩容线程池存活时间
     * @param timeUnit 时间单位
     * @param threadQueue 阻塞队列
     * @param rejectedExecutionHandler 拒绝策略
     */
    public static ThreadPoolExecutor createThreadPool(int coreSize, int maxSize, int keepAliveTime, TimeUnit timeUnit, BlockingDeque threadQueue, RejectedExecutionHandler rejectedExecutionHandler) {
        coreSize = getCoreSize(coreSize);
        maxSize = getMaxSize(maxSize);
        return new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime, timeUnit, threadQueue, rejectedExecutionHandler);
    }

    /**
     *  检测时间单位，防止异常
     */
    private static TimeUnit getTimeUnit(TimeUnit timeUnit) {
        if(timeUnit==null)
            return ThreadPoolUtil.DEFAULT_TIME_UNIT;
        return timeUnit;
    }

    /**
     * 校验核心线程池数量
     */
    private static int getCoreSize(int coreSize) {
        return Math.max(coreSize, THREAD_MIN);
    }

    /**
     * 校验最大线程池数量
     */
    public static int getMaxSize(int coreSize) {
        int maxSize = coreSize << 1;
        return Math.max(maxSize, THREAD_MAX);
    }

    /**
     * 校验最大线程池数量
     */
    public static int getMaxSize(int coreSize, int maxSize) {
        return Math.max(maxSize, coreSize);
    }

    /**
     * 获取当前线程池闲置线程数
     * @param poolExecutor 线程池对象
     */
    public static Integer getLeisureNum(ThreadPoolExecutor poolExecutor) {
        if (poolExecutor != null) {
            return poolExecutor.getCorePoolSize() - poolExecutor.getActiveCount();
        }
        return -1;
    }

    /**
     * 线程池是否为闲置状态
     */
    public static boolean isLeisure(ThreadPoolExecutor poolExecutor) {
        if (poolExecutor != null) {
            return poolExecutor.getActiveCount() == 0;
        }
        return false;
    }

    /**
     * 监控线程池任务，新获取一个线程，用来监控当前线程池的状态，可以随时开启关闭
     * @param poolExecutor 线程池对象
     * @param time  检测线程池状态间隔时间
     * @param timeUnit 时间单位
     * @return 监控线程池对象的线程，记得线程是未启动状态，如果需要开启监控，需要自行启动哦
     */
    public static Thread monitorThreadPool(ThreadPoolExecutor poolExecutor, int time, TimeUnit timeUnit) {
        System.err.println("启动线程池监控系统");
        if (poolExecutor != null) {
            Thread thread = new Thread(() -> {
                retry:
                try {
                    for (; ; ) {
                        Integer active = poolExecutor.getActiveCount();
                        Integer core = poolExecutor.getCorePoolSize();
                        Integer max = poolExecutor.getMaximumPoolSize();
                        BlockingQueue<Runnable> que = poolExecutor.getQueue();
                        Integer queSize = que.size();
                        System.err.println(String.format("当前线程池数据：核心大小：%d,活动线程数量：%d,最大线程数量:%d,队列大小:%d", core, active, max, queSize));
                        Thread.sleep(TimeUnit.MILLISECONDS.convert(time, timeUnit));
                    }
                } catch (Exception e) {
                    System.err.println("线程池监控系统被外部强制退出");
                }
            });
            return thread;
        }
        System.err.println("当前线程池不能被监控");
        return null;
    }


}
