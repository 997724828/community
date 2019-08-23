package club.yuyang.community.controller;

import club.yuyang.community.dto.CommentDTO;
import club.yuyang.community.dto.QuestionDTO;
import club.yuyang.community.dto.ResultDTO;
import club.yuyang.community.entity.Collect;
import club.yuyang.community.entity.Thumb;
import club.yuyang.community.entity.User;
import club.yuyang.community.entity.UserExample;
import club.yuyang.community.enums.CommentTypeEnum;
import club.yuyang.community.exception.CustomizeErrorCode;
import club.yuyang.community.exception.CustomizeException;
import club.yuyang.community.mapper.UserMapper;
import club.yuyang.community.service.CollectService;
import club.yuyang.community.service.CommentService;
import club.yuyang.community.service.QuestionService;
import club.yuyang.community.service.ThumbService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author yuyang
 * @date 2019/7/31 10:16
 */
@Controller
public class QuestionController {
    @Resource
    QuestionService questionService;
    @Resource
    CommentService commentService;
    @Resource
    ThumbService thumbService;
    @Resource
    CollectService collectService;
    @Resource
    UserMapper userMapper;

    /**
     *
     * 登录状态
     * @param questionId
     * @param userId
     * @param model
     * @return
     */
    @GetMapping("/question/{questionId}/{userId}")
    public String question(@PathVariable(name = "questionId")Long questionId,
                           @PathVariable(name = "userId",required = false)Long userId,
                           Model model,
                           HttpServletRequest request){

        User  inUser = userMapper.selectByPrimaryKey(userId);//防止改动url中的用户id信息，判断改动的用户id是否是当前session中的用户
        User seUser = (User)request.getSession().getAttribute("user");
        if (inUser.getId() != seUser.getId()) {
            throw new CustomizeException(CustomizeErrorCode.SYS_ERROR);
        }

        QuestionDTO questionDTO = questionService.getById(questionId);//展示详细问题
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);//展示相关问题
        List<CommentDTO> comments = commentService.listByTargetId(questionId,CommentTypeEnum.QUESTION);//展示该问题的所有回复
        //累加阅读数
        questionService.incView(questionId);

        // 查看该用户是否给该问题点赞
        Thumb thumb = thumbService.selectByQuestionIdAndUserId(questionId, userId);
        if (thumb == null){
            model.addAttribute("state","1");//页面显示未点赞,点赞变成已点赞
        }else {
            model.addAttribute("state", "2");//页面显示已点赞,点击取消点赞
        }

        // 查看该用户是否收藏该问题
        Collect collect = collectService.selectByQuestionIdAndUserId(questionId,userId);
        if (collect == null){
            model.addAttribute("cState","1");//页面显示未收藏,点击后变成已收藏
        }else {
            model.addAttribute("cState", "2");//页面显示已收藏,点击取消收藏
        }

        model.addAttribute("questionDTO",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";

    }

    /**
     * 未登录状态
     * @param questionId
     * @param model
     * @return
     */
    @GetMapping("/question/{questionId}")
    public String question(@PathVariable(name = "questionId")Long questionId,
                           Model model) {

        QuestionDTO questionDTO = questionService.getById(questionId);//展示详细问题
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);//展示相关问题
        List<CommentDTO> comments = commentService.listByTargetId(questionId, CommentTypeEnum.QUESTION);//展示该问题的所有回复
        //累加阅读数
        questionService.incView(questionId);

        model.addAttribute("state", "0");//question.html页面显示未点赞,点赞跳转用户登录
        model.addAttribute("cState", "0");//question.html页面显示未收藏,收藏跳转用户登录

        model.addAttribute("questionDTO", questionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }
}
