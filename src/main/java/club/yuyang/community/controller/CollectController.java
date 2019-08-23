package club.yuyang.community.controller;

import club.yuyang.community.service.CollectService;
import club.yuyang.community.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author yuyang
 * @date 2019/8/20 18:11
 */
@Controller
public class CollectController {
    @Resource
    CollectService collectService;
    @Resource
    QuestionService questionService;

    @PostMapping("/collect")
    @ResponseBody
    public Integer collect(@RequestParam(name = "questionId")Long questionId,
                         @RequestParam(name = "userId")Long userId,
                         @RequestParam(name = "state")String state){

        if (state.equals("1")) {
            collectService.createCollect(questionId, userId);
            questionService.incCollect(questionId);
            Integer collectCount = collectService.countByCollect(questionId);
//            collectService.selectByQuestionIdAndUserId(questionId, userId);
            return collectCount;
        }else if (state.equals("2")){
            collectService.decCollect(questionId);//减去一个收藏数
            collectService.delete(questionId, userId);//数据库中删除这条收藏记录
            Integer collectCount = collectService.countByCollect(questionId);//查看现有点赞数
            return collectCount;
        }
        return null;
    }
}
