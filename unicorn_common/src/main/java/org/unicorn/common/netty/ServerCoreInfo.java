package org.unicorn.common.netty;

/**
 * 计算CPU个数，以及根据服务器运算性质定义线程数
 * 
 * @author KING
 * @date 2018年8月21日
 */

public class ServerCoreInfo {
    public static final int DEFAULT_NETTY_CORE_THREAD_NUM = 1;      //监听线程
    public static final int DEFAULT_NETTY_WORK_THREAD_NUM;
    public static final int MACHINE_CORE_NUM;
    
    static{
        int count = Runtime.getRuntime().availableProcessors();
        MACHINE_CORE_NUM = count;
        /**运算密集性,则降低该值,IO密集性,则可以提高该值*/
        DEFAULT_NETTY_WORK_THREAD_NUM = MACHINE_CORE_NUM+1;
    }
}
