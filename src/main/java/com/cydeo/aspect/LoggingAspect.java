package com.cydeo.aspect;

import com.cydeo.service.CompanyService;
import com.cydeo.service.SecurityService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LoggingAspect {

    SecurityService securityService;
    CompanyService companyService;

    public LoggingAspect( SecurityService securityService, CompanyService companyService) {
        this.securityService = securityService;
        this.companyService = companyService;
    }

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.cydeo.service.CompanyService.activateCompany(..)) || execution(* com.cydeo.service.CompanyService.deactivateCompany(..) )")
    public void companyActivationPointcut() {
    }

    @After("companyActivationPointcut()")
    public void logAfterCompanyActivation(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        Long companyId = (args.length > 0 && args[0] != null) ? (Long) args[0] : null;
        String companyName = companyService.findById(companyId).getTitle();

        String username = securityService.getLoggedInUser().getUsername();
        String firstname = securityService.getLoggedInUser().getFirstname();
        String lastname = securityService.getLoggedInUser().getLastname();

        logger.info("Method {} executed for company: {} by user: {} {} email: {}", methodName, companyName, firstname, lastname, username);

    }


    @Pointcut("execution(* com.cydeo..*(..)) throws RuntimeException")
    public void runtimeExceptionPointcut() {
    }

    @Around("@annotation(com.cydeo.annotations.ExecutionTime)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.error("Exception BEFORE method: {}  -> Param: {}", joinPoint.getSignature().toLongString(), joinPoint.getKind());
        Object result;
        result = joinPoint.proceed();
        logger.error("Exception AFTER method: {}  -> Param: {}", joinPoint.getSignature().toShortString(), result);
        return result;
    }

    @AfterThrowing(pointcut = "runtimeExceptionPointcut()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, RuntimeException ex) {
        logger.error("Exception method: -> {}. Exception: -> {}. Message: -> ({})", joinPoint.getSignature().getName(), ex.getClass().getSimpleName(), ex.getMessage());
    }

}
