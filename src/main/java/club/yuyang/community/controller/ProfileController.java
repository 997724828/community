package club.yuyang.community.controller;

import club.yuyang.community.dto.PaginationDTO;
import club.yuyang.community.entity.CollectExample;
import club.yuyang.community.entity.User;
import club.yuyang.community.mapper.CollectMapper;
import club.yuyang.community.service.CollectService;
import club.yuyang.community.service.NotificationService;
import club.yuyang.community.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yuyang
 * @date 2019/7/30 9:50
 */
@Controller
public class ProfileController {

    @Resource
    private QuestionService questionService;

    @Resource
    private NotificationService notificationService;

    @Resource
    private CollectService collectService;

    @Resource
    private CollectMapper collectMapper;


    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action")String action,
                          HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1")Integer page,
                          @RequestParam(name = "size",defaultValue = "5")Integer size,
                          Model model){
        User user = (User) request.getSession().getAttribute("user");

        if (user == null){
            return "redirect:/";
        }


        if ("questions".equals(action)){
            PaginationDTO paginationDTOList = questionService.myQuestionList(user.getId(),page,size);
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的问题");
            model.addAttribute("paginationDTOList",paginationDTOList);
        }else if ("replies".equals(action)){
            PaginationDTO paginationDTOList = notificationService.list(user.getId(),page,size);
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
            model.addAttribute("paginationDTOList",paginationDTOList);
        }else if ("personal".equals(action)){
            model.addAttribute("section","personal");
            model.addAttribute("sectionName","我的资料");
        }else if ("collects".equals(action)){
            PaginationDTO paginationDTOList = collectService.list(user.getId(),page,size);
            model.addAttribute("section","collects");
            model.addAttribute("sectionName","我的收藏");
            model.addAttribute("paginationDTOList",paginationDTOList);
        }
        return "profile";
    }
}
