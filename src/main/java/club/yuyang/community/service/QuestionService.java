package club.yuyang.community.service;

import club.yuyang.community.dto.PaginationDTO;
import club.yuyang.community.dto.QuestionDTO;
import club.yuyang.community.dto.QuestionQueryDTO;
import club.yuyang.community.entity.Question;
import club.yuyang.community.entity.QuestionExample;
import club.yuyang.community.entity.User;
import club.yuyang.community.exception.CustomizeErrorCode;
import club.yuyang.community.exception.CustomizeException;
import club.yuyang.community.mapper.QuestionExtMapper;
import club.yuyang.community.mapper.QuestionMapper;
import club.yuyang.community.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author yuyang
 * @date 2019/7/29 15:44
 */
@Service
public class QuestionService {

    @Resource
    UserMapper userMapper;

    @Resource
    QuestionMapper questionMapper;

    @Resource
    QuestionExtMapper questionExtMapper;

    //所有问题列表
   public PaginationDTO questionDTOList(String search,Integer page, Integer size) {

       if (StringUtils.isNotBlank(search)){
           //如果search不为空,获取出搜索框中空格分隔的关键字片段
           String[] keyWorld = StringUtils.split(search," ");
           //将关键字拼接成以|分隔的字符串，以便实现正则表达式查询数据库字段匹配
           search = Arrays.stream(keyWorld).collect(Collectors.joining("|"));
       }

       PaginationDTO paginationDTO = new PaginationDTO();
       Integer totalPages;

       QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
       questionQueryDTO.setSearch(search);

       Integer questionTotalCount = questionExtMapper.countBySearch(questionQueryDTO);
       if (questionTotalCount == 0){
           throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
       }
       if (questionTotalCount % size == 0) {
           totalPages = questionTotalCount / size;
       }else {
           totalPages = (questionTotalCount / size) + 1;
       }


       if (page < 1){
           page=1;
       }

       if (page > totalPages){
           page = totalPages;
       }

       paginationDTO.setPagination(totalPages,page);
        //实现分页
        Integer offset = size * (page-1);
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questionList = questionExtMapper.selectBySearch(questionQueryDTO);

        List<QuestionDTO> questionDTOList = new ArrayList<>();


        for (Question question : questionList){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //spring工具类中的一个可以将一个对象所有的属性数据复制到另一个拥有相同属性结构的对象中去的方法
            //代替传统的一个一个属性set注入的方法
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }


    //我的问题列表
    public PaginationDTO myQuestionList(Long userId,Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalPages;
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer questionTotalCount = questionMapper.countByExample(questionExample);
        if (questionTotalCount % size == 0) {
            totalPages = questionTotalCount / size;
        }else {
            totalPages = (questionTotalCount / size) + 1;
        }


        if (page < 1){
            page=1;
        }

        if (page > totalPages){
            page = totalPages;
        }

        paginationDTO.setPagination(totalPages,page);
        //实现分页
        Integer offset = size * (page-1);
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> myQuestionList = questionMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();


        for (Question question : myQuestionList){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //spring工具类中的一个可以将一个对象所有的属性数据复制到另一个拥有相同属性结构的对象中去的方法
            //代替传统的一个一个属性set注入的方法
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    //问题详情
    public QuestionDTO getById(Long id){

       Question question = questionMapper.selectByPrimaryKey(id);
       if (question == null){
           throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
       }
       QuestionDTO questionDTO = new QuestionDTO();
       BeanUtils.copyProperties(question,questionDTO);
       User user = userMapper.selectByPrimaryKey(question.getCreator());
       questionDTO.setUser(user);
       return questionDTO;
    }

    //编辑问题
    public void createOrUpdate(Question question) {
       if(question.getId() == null){
           //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            question.setCollectCount(0);
            questionMapper.insert(question);
       }else {
           //更新
           question.setGmtModified(System.currentTimeMillis());
           Question updateQuestion = new Question();
           updateQuestion.setGmtModified(System.currentTimeMillis());
           updateQuestion.setTitle(question.getTitle());
           updateQuestion.setDescription(question.getDescription());
           updateQuestion.setTag(question.getTag());
           QuestionExample questionExample = new QuestionExample();
           questionExample.createCriteria()
                   .andIdEqualTo(question.getId());
          int updated = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
          if (updated != 1){
              throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
          }
       }
    }

    //累加阅读数
    public void incView(Long id) {
       Question question = new Question();
       question.setId(id);
       question.setViewCount(1);
       questionExtMapper.incView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
       if (StringUtils.isBlank(questionDTO.getTag())){
           return new ArrayList<>();
       }
       String[] tags = StringUtils.split(questionDTO.getTag(),",");
        //?????????构造出数据库中正则表达式所需要填入的字符串
       String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
       Question question = new Question();
       question.setId(questionDTO.getId());//为了相关问题不包含当前问题
       question.setTag(regexpTag);

        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(question1 -> {
            QuestionDTO questionDTO1 = new QuestionDTO();
            BeanUtils.copyProperties(question1,questionDTO1);
            return questionDTO1;
        }).collect(Collectors.toList());
        return questionDTOS;
    }

    //累加点赞数
    public void incLike(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setLikeCount(1);
        questionExtMapper.incLike(question);
    }

    //查看点赞数
    public Integer countByLike(Long questionId) {
       Question question = questionMapper.selectByPrimaryKey(questionId);
       Integer likeCount = question.getLikeCount();
       return likeCount;
    }

    //减去一个点赞数
    public void decLike(Long questionId) {
       Question question = new Question();
       question.setId(questionId);
       question.setLikeCount(1);
       questionExtMapper.decLike(question);
    }

    //累加收藏数
    public void incCollect(Long questionId) {
        Question question = new Question();
        question.setId(questionId);
        question.setCollectCount(1);
        questionExtMapper.incCollect(question);
    }
}
