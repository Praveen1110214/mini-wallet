package com.example.miniwallet.util;

import org.slf4j.Logger;

public class LoggerUtil {
    private LoggerUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Logs the entry of a service method.
     *
     * @param logger    the logger instance
     * @param methodName the method name
     * @param className the class name
     */
    public static void logEntry(
            Logger logger,
            String methodName,
            String className) {

        logger.info(
                "Executing method ===> MethodName: {} ===> ClassName: {}",
                methodName,
                className
        );
    }

    /**
     * Logs the entry of a controller API.
     *
     * @param logger   the logger instance
     * @param apiName  the API name
     * @param className the class name
     */
    public static void logEntryController(
            Logger logger,
            String apiName,
            String className) {

        logger.info(
                "Executing api ===> APIName: {} ===> ClassName: {}",
                apiName,
                className
        );
    }

    /**
     * Logs the exit of a service method.
     *
     * @param logger     the logger instance
     * @param methodName the method name
     * @param className  the class name
     */
    public static void logExit(
            Logger logger,
            String methodName,
            String className) {

        logger.info(
                "===> Exiting MethodName: {}, ClassName: {}",
                methodName,
                className
        );
    }

    /**
     * Logs the exit of a controller API.
     *
     * @param logger   the logger instance
     * @param apiName  the API name
     * @param className the class name
     */
    public static void logExitController(
            Logger logger,
            String apiName,
            String className) {

        logger.info(
                "===> Exiting APIName: {}, ClassName: {}",
                apiName,
                className
        );
    }

    /**
     * Logs an exception and the exit of a service method.
     *
     * @param logger    the logger instance
     * @param methodName the method name
     * @param className the class name
     * @param exception the exception
     */
    public static void logExitWithException(
            Logger logger,
            String methodName,
            String className,
            Exception exception) {

        logger.error(
                "Error in MethodName: {}, ClassName: {}",
                methodName,
                className,
                exception
        );

        logger.info(
                "===> Exiting MethodName: {}, ClassName: {} with Exception",
                methodName,
                className
        );
    }
}
