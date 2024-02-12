package com.yohanii.lostandfound.aop;

import com.yohanii.lostandfound.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Component
@Aspect
public class LoginMemberAOP {

    @Pointcut("execution(* com.yohanii.lostandfound.web.post..*(..)) ")
    private void webPost() {}

    @Pointcut("execution(* com.yohanii.lostandfound.web.profile..*(..))")
    private void webProfile() {}

    @Pointcut("execution(* com.yohanii.lostandfound.web.member..*(..)) && @annotation(org.springframework.web.bind.annotation.GetMapping)")
    private void webMember() {}

    @Pointcut("execution(* com.yohanii.lostandfound.web.HomeController..*(..)) && @annotation(org.springframework.web.bind.annotation.GetMapping)")
    private void webHome() {}

    @Around("webPost() || webProfile() || webMember() || webHome()")
    public Object getLoginMember(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = request.getSession(false);
        log.info("LoginmemberAOP.getLoginmember");

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Model model) {
                if (session != null) {
                    model.addAttribute("member", session.getAttribute(SessionConst.LOGIN_MEMBER));
                }
            }
        }

        return joinPoint.proceed();
    }
}
