package club.yuyang.community.controller;

import club.yuyang.community.cache.TagCache;
import club.yuyang.community.dto.QuestionDTO;
import club.yuyang.community.entity.Question;
import club.yuyang.community.entity.User;
import club.yuyang.community.exception.CustomizeErrorCode;
import club.yuyang.community.exception.CustomizeException;
import club.yuyang.community.mapper.QuestionMapper;
import club.yuyang.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.HTML;


/**
 * @author yuyang
 * @date 2019/7/28 16:56
 */
@Controller
public class PublishController {

    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionMapper questionMapper;

    /**
     * 跳转问题发布页面
     ** @return
     */
    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }

    /**
     * 问题页面发布验证
     * @param title
     * @param description
     * @param tag
     * @param request
     * @param model
     * @return
     */
    @PostMapping("/publish")
    public String doPublish(@RequestParam(value = "title",required = false)String title,
                            @RequestParam(value = "description",required = false)String description,
                            @RequestParam(value = "tag",required = false)String tag,
                            @RequestParam(value = "id",required = false)Long id,
                            HttpServletRequest request,
                            Model model){
        //为了已填写资料回显到页面
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags",TagCache.get());

        if(title == null || title ==""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description == null || description ==""){
            model.addAttribute("error","内容详情不能为空");
            return "publish";
        }
        if(tag == null || tag ==""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }else {
            //校验标签是否合法
            String invalid = TagCache.filterInvalid(tag);
            if (StringUtils.isNotBlank(invalid)){
                model.addAttribute("error","输入非法标签"+invalid);
                return "publish";
            }
        }

        User user = (User) request.getSession().getAttribute("user");

        if (user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setCreator(user.getId());
        question.setTag(tag);
        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }

    /**
     * 问题页面编辑
     * @return
     */
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id")Long id,
                       Model model,
                       HttpServletRequest request){

        Question inQuestion = questionMapper.selectByPrimaryKey(id);
        User seUser = (User)request.getSession().getAttribute("user");
        if (inQuestion.getCreator() != seUser.getId()){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_IS_OTHERS);
        }
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());//用于验证这个问题是新发布的还是编辑后要更新的

        model.addAttribute("tags",TagCache.get());
        return "publish";
    }
}
