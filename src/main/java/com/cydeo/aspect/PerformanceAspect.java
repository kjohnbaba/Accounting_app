package com.cydeo.aspect;

import com.cydeo.annotations.ExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    private static final Logger logger = Logger.getLogger(PerformanceAspect.class.getName());

    @Around("@annotation(executionTime)")
    public Object measure(ProceedingJoinPoint joinPoint, com.cydeo.annotations.ExecutionTime executionTime) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        logger.info("Execution Started for method: {}" + methodName);

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;
        logger.info("Execution finished for method: {}. Time taken:{} ms" + duration + executionTime);

        return result;
    }

}
