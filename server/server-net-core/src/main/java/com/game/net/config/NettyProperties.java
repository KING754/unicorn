package com.game.net.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author KING
 * @date 2022/12/27
 */

@Data
@Slf4j
public class NettyProperties {
    private final static int DEFAULT_CORE_SIZE = 4;
    public static final String OS_NAME = System.getProperty("os.name");
    private static final String SYS_LINUX_FLAG = "linux";
    private static final String SYS_WINDOWS_FLAG = "windows";

    private static boolean isLinuxPlatform = false;
    private static boolean isWindowsPlatform = false;

    static {
        if (OS_NAME != null && OS_NAME.toLowerCase().contains(SYS_LINUX_FLAG)) {
            isLinuxPlatform = true;
        }

        if (OS_NAME != null && OS_NAME.toLowerCase().contains(SYS_WINDOWS_FLAG)) {
            isWindowsPlatform = true;
        }
    }


    private static int getCoreSize() {
        try {
            int coreSize = Runtime.getRuntime().availableProcessors();
            if (coreSize <= 0) {
                return DEFAULT_CORE_SIZE;
            }
        } catch (Exception e) {
            log.error("NettyProperties getCoreSize have error.{}", e);
        }
        return DEFAULT_CORE_SIZE;
    }

    public static int getBossThreadCount() {
        return NettyProperties.getCoreSize();
    }

    public static int getWorkThreadCount() {
        return NettyProperties.getCoreSize() * 2 + 1;
    }


    public static boolean isLinux() {
        return isLinuxPlatform;
    }



}
