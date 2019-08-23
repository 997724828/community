package club.yuyang.community.service;

import club.yuyang.community.dto.CommentDTO;
import club.yuyang.community.entity.*;
import club.yuyang.community.enums.CommentTypeEnum;
import club.yuyang.community.enums.NotificationStatusEnum;
import club.yuyang.community.enums.NotificationTypeEnum;
import club.yuyang.community.exception.CustomizeErrorCode;
import club.yuyang.community.exception.CustomizeException;
import club.yuyang.community.mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yuyang
 * @date 2019/8/2 22:42
 */
@Service
public class CommentService {

    @Resource
    private CommentMapper commentMapper;
    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private QuestionExtMapper questionExtMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CommentExtMapper commentExtMapper;
    @Resource
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment,User commentator){
        if (comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);

            //增加评论数
            dbComment.setCommentCount(1);
            commentExtMapper.incCommentCount(dbComment);

            //获得回复的评论的问题的id,为了回复评论时在消息通知页面也跳转到问题详情
            Long questionId = dbComment.getParentId();


            //追加一条消息通知
            Notification notification = new Notification();

            notification.setNotifier(comment.getCommentator());//注入消息发送人id
            notification.setNotifierName(commentator.getName());//注入消息发送人姓名
            notification.setReceiver(dbComment.getCommentator());//注入消息接收人
            notification.setOuterTitle(dbComment.getContent());//注入评论的内容
            notification.setGmtCreate(System.currentTimeMillis());//注入消息创建时间
            notification.setType(NotificationTypeEnum.REPLY_COMMENT.getType());//注明该消息是一条回复评论的消息
            notification.setOuterid(questionId);//该消息指向的问题id
            notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());//该消息的状态为未读

            //判断是否是自己回复自己评论的情况，则不用通知自己
            if (notification.getReceiver() == comment.getCommentator()){
                return;
            }else {
                notificationMapper.insert(notification);
            }
        }else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //累加回复数
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);

            //追加一条消息通知
            Notification notification = new Notification();

            notification.setNotifier(comment.getCommentator());//注入消息发送人
            notification.setReceiver(question.getCreator());//注入消息接收人id
            notification.setNotifierName(commentator.getName());//注入消息发送人姓名
            notification.setOuterTitle(question.getTitle());//注入问题题目
            notification.setGmtCreate(System.currentTimeMillis());//注入消息创建时间
            notification.setType(NotificationTypeEnum.REPLY_QUESTION.getType());//注明该消息是一条回复问题的消息
            notification.setOuterid(question.getId());//该消息指向的问题id
            notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());//该消息的状态为未读

            //判断评论的创建人是否是问题的创建人，是的话不用通知自己
            if (notification.getNotifier() == question.getCreator()){
                return;
            }else {
                notificationMapper.insert(notification);
            }
        }
    }

    //返回问题的所有回复或者回复的二级评论
    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum typeEnum) {
        CommentExample example = new CommentExample();
        example.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(typeEnum.getType());
        example.setOrderByClause("gmt_create desc");
       List<Comment> comments = commentMapper.selectByExample(example);
       if (comments.size() == 0){
           return new ArrayList<>();
       }else {
           //java8语法
           //获取去重的评论人
           Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
          List<Long> userIds = new ArrayList<>();
          userIds.addAll(commentators);

          //获取评论人并转换为map
           UserExample userExample = new UserExample();
           userExample.createCriteria()
                   .andIdIn(userIds);
           List<User> users = userMapper.selectByExample(userExample);
           Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(),user -> user));

           //转换comment为commentDTO
           List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
               CommentDTO commentDTO = new CommentDTO();
               BeanUtils.copyProperties(comment,commentDTO);
               commentDTO.setUser(userMap.get(comment.getCommentator()));
               return commentDTO;
           }).collect(Collectors.toList());

           return commentDTOS;
       }
    }
}
