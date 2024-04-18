package com.sojoo.StoreSpotter.config.timeTrace;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Log4j2
@Component
@Aspect
public class TimeTraceAspect {

//    @Pointcut("execution(* com.example.demo.repository..*(..))")
    @Pointcut("@annotation(com.sojoo.StoreSpotter.config.timeTrace.TimeTrace)")
    private void timeTracePointcut() {
    }

    @Around("timeTracePointcut()")
    public Object traceTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();

        try {
            stopWatch.start();
            return joinPoint.proceed(); // 실제 타겟 호출
        } finally {
            stopWatch.stop();
            log.info("{} - Total time = {}s",
                    joinPoint.getSignature().toShortString(),
                    stopWatch.getTotalTimeSeconds());
        }
    }
}