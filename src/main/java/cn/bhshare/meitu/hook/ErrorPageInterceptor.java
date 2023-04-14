package cn.bhshare.meitu.hook;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

import static cn.bhshare.meitu.util.GetIpAddr.getIpAddr;

/**
 * @author hgs
 * @version ErrorPageInterceptor.java, v 0.1 2018/03/04 20:52 hgs Exp $
 * <p>
 * 错误页面拦截器
 * 替代EmbeddedServletContainerCustomizer在war中不起作用的方法
 */
@Component
@Slf4j
public class ErrorPageInterceptor extends HandlerInterceptorAdapter {
    private List<Integer> errorCodeList = Arrays.asList(400, 404, 403, 500, 501);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        if (!(request.getRequestURI().contains("/images") || request.getRequestURI().contains("/layui/") || request.getRequestURI().contains("/css/")
                || request.getRequestURI().contains("/js/") || request.getRequestURI().contains("/fonts/"))) {
            log.info("-------------------- IP: " + getIpAddr(request) + ", 访问页面：" + request.getRequestURI() + " --------------------");
        }
        if (errorCodeList.contains(response.getStatus())) {
            response.sendRedirect("/error/" + response.getStatus());
            return false;
        }
        return super.preHandle(request, response, handler);
    }
}