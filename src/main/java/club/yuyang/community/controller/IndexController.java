package club.yuyang.community.controller;

import club.yuyang.community.dto.PaginationDTO;
import club.yuyang.community.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @author yuyang
 * @date 2019/7/27 9:38
 */
@Controller
public class IndexController {


    @Resource
    private QuestionService questionService;

    @RequestMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page",defaultValue = "1")Integer page,
                        @RequestParam(name = "size",defaultValue = "5")Integer size,
                        @RequestParam(name = "search",required = false)String search){

        PaginationDTO paginationDTOList = questionService.questionDTOList(search,page,size);
        model.addAttribute("paginationDTOList",paginationDTOList);
        model.addAttribute("search",search);
        return "index";
    }
}
