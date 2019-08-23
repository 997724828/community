package club.yuyang.community.service;

import club.yuyang.community.dto.NotificationDTO;
import club.yuyang.community.dto.PaginationDTO;
import club.yuyang.community.entity.Notification;
import club.yuyang.community.entity.NotificationExample;
import club.yuyang.community.entity.User;
import club.yuyang.community.enums.NotificationStatusEnum;
import club.yuyang.community.enums.NotificationTypeEnum;
import club.yuyang.community.exception.CustomizeErrorCode;
import club.yuyang.community.exception.CustomizeException;
import club.yuyang.community.mapper.NotificationMapper;
import club.yuyang.community.mapper.UserMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * @author yuyang
 * @date 2019/8/9 22:57
 */
@Service
public class NotificationService {

    @Resource
    private NotificationMapper notificationMapper;

    @Resource
    private UserMapper userMapper;

    public PaginationDTO list(Long userId, Integer page, Integer size) {

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();

        Integer totalPage;

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalCount = notificationMapper.countByExample(notificationExample);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        }else {
            totalPage = (totalCount / size) + 1;
        }


        if (page < 1){
            page=1;
        }

        if (page > totalPage){
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage,page);
        //实现分页
        Integer offset = size * (page-1);
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));

        if (notifications.size() == 0){
            return paginationDTO;
        }else {
            List<NotificationDTO> notificationDTOS = new ArrayList<>();

            for (Notification notification : notifications) {
                NotificationDTO notificationDTO = new NotificationDTO();
                BeanUtils.copyProperties(notification,notificationDTO);
                notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
                notificationDTOS.add(notificationDTO);
            }
            paginationDTO.setData(notificationDTOS);
            return paginationDTO;
        }
    }

    //消息通知数
    public Long unReadCount(Long userId) {
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return Long.valueOf(notificationMapper.countByExample(example));
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (notification.getReceiver() != user.getId()){
            //当消息接收人不是自己的时候
            throw new CustomizeException(CustomizeErrorCode.READ_OTHERS_NOTIFICATION);
        }

        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
