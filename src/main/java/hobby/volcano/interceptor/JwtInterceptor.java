package hobby.volcano.interceptor;

import hobby.volcano.common.CustomEnum;
import hobby.volcano.service.JwtIssueService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtIssueService jwtIssueService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("JwtInterceptor preHandle");
        String jwtToken = request.getHeader(CustomEnum.JWT_TOKEN.getContent());
//        log.info("jwtToken : {}",jwtToken);
        if(jwtToken.equals(CustomEnum.ADMIN_JWT_KEY.getContent())) {
            MDC.put(CustomEnum.USER_ID.getContent(),"12400454");
        }
        else if(jwtIssueService.tokenValidCheck(jwtToken)) {
            Claims body = jwtIssueService.getClaims(jwtToken).getBody();
            MDC.put(CustomEnum.USER_ID.getContent(),body.get(CustomEnum.USER_ID.getContent()).toString());
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request,response,handler,modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request,response,handler,ex);
    }
}
