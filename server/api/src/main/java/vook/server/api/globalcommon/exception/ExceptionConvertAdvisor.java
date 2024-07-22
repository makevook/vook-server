package vook.server.api.globalcommon.exception;

import jakarta.validation.ConstraintViolationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionConvertAdvisor {

    @Around("""
            @within(vook.server.api.globalcommon.annotation.DomainLogic) || 
            @within(vook.server.api.globalcommon.annotation.UseCase) ||
            @within(vook.server.api.globalcommon.annotation.ModelFactory)
            """)
    public Object convertException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (ConstraintViolationException e) {
            throw new ParameterValidateException(e);
        }
    }
}
