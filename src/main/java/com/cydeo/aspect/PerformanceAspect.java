package com.cydeo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    @Pointcut("@annotation(com.cydeo.annotation.ExecutionTime)")
    public void executionTimePC() {
    }

    @Around("executionTimePC()")
    public Object aroundAnyExecutionTimeAdvice(ProceedingJoinPoint proceedingJoinPoint) {

        long beforeTime = System.currentTimeMillis();
        Object result = null;
        log.info("Method: {} - Execution starts:", proceedingJoinPoint.getSignature());

        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            log.error("Error occurred during execution: {}", e.getMessage());
            log.info("Execution time logger cannot proceed with class: {}", proceedingJoinPoint.getSourceLocation().getFileName());        }

        long afterTime = System.currentTimeMillis();

        log.info("Method: {} - Time taken to execute: {} ms"
                , proceedingJoinPoint.getSignature().toShortString(), (afterTime - beforeTime));

        return result;
    }

}