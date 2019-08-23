package club.yuyang.community.controller;

import club.yuyang.community.entity.Thumb;
import club.yuyang.community.mapper.ThumbExtMapper;
import club.yuyang.community.mapper.ThumbMapper;
import club.yuyang.community.service.QuestionService;
import club.yuyang.community.service.ThumbService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author yuyang
 * @date 2019/8/16 8:42
 */
@Controller
public class ThumbController {

    @Resource
    ThumbService thumbService;
    @Resource
    QuestionService questionService;

    @PostMapping("/thumb")
    @ResponseBody
    public Integer thumb(@RequestParam(name = "questionId")Long questionId,
                         @RequestParam(name = "userId")Long userId,
                         @RequestParam(name = "state")String state){

        if (state.equals("1")) {
            thumbService.createThumb(questionId, userId);
            questionService.incLike(questionId);
            Integer likeCount = questionService.countByLike(questionId);
            thumbService.selectByQuestionIdAndUserId(questionId, userId);
            return likeCount;
        }else if (state.equals("2")){
            questionService.decLike(questionId);//减去一个点赞数
            thumbService.delete(questionId, userId);//数据库中删除这条点赞记录
            Integer likeCount = questionService.countByLike(questionId);//查看现有点赞数
            return likeCount;
        }
        return null;
    }
}

