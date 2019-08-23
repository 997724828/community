package club.yuyang.community.controller;

import club.yuyang.community.dto.NotificationDTO;
import club.yuyang.community.entity.User;
import club.yuyang.community.enums.NotificationTypeEnum;
import club.yuyang.community.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yuyang
 * @date 2019/8/10 9:34
 */
@Controller
public class NotificationController {

    @Resource
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String notification(@PathVariable(name = "id")Long id,
                               HttpServletRequest request){
        //用户没登陆直接跳转到首页
        User user = (User) request.getSession().getAttribute("user");

        if (user == null){
            return "redirect:/";
        }

        NotificationDTO notificationDTO = notificationService.read(id,user);
        if (NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType() ||
                NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()){
            return "redirect:/question/"+notificationDTO.getOuterid();
        }else {
            return "redirect:/";
        }
    }
}
