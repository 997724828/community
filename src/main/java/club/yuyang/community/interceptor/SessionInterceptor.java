package club.yuyang.community.interceptor;

import club.yuyang.community.entity.Collect;
import club.yuyang.community.mapper.UserMapper;
import club.yuyang.community.entity.User;
import club.yuyang.community.entity.UserExample;
import club.yuyang.community.service.CollectService;
import club.yuyang.community.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author yuyang
 * @date 2019/7/30 23:12
 */
@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Resource
    private UserMapper userMapper;
    @Resource
    private NotificationService notificationService;
    @Resource
    private CollectService collectService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    UserExample userExample = new UserExample();
                    userExample.createCriteria()
                            .andTokenEqualTo(token);
                    List<User> users = userMapper.selectByExample(userExample);
                    if (users.size() != 0) {
                        request.getSession().setAttribute("user", users.get(0));
                        //获得消息通知数
                        Long unReadCount = notificationService.unReadCount(users.get(0).getId());
                        request.getSession().setAttribute("unReadCount",unReadCount);
                        //获得收藏数
                        int collectCount = collectService.getCollectCount(users.get(0).getId());
                        request.getSession().setAttribute("collectCount",collectCount);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
