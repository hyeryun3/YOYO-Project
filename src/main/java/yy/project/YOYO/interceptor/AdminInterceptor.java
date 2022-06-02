package yy.project.YOYO.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import yy.project.YOYO.domain.User;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("관리자 인증 체크 인터셉트 실행 {}", requestURI);
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("loginUser");
        if (user.getRole().equals("admin")) {
            log.info("관리자가 맞습니다. ");
            return true;
        } else {
            log.info("관리자가 아닙니다.");
            response.sendRedirect("/?redirectURL=" + requestURI);
            return false;
        }
    }

}
