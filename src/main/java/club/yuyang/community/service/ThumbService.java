package club.yuyang.community.service;

import club.yuyang.community.entity.Thumb;
import club.yuyang.community.mapper.ThumbExtMapper;
import club.yuyang.community.mapper.ThumbMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author yuyang
 * @date 2019/8/16 0:25
 */
@Service
public class ThumbService {
    @Resource
    ThumbExtMapper thumbExtMapper;
    @Resource
    ThumbMapper thumbMapper;

    public Thumb selectByQuestionIdAndUserId(Long questionId, Long userId) {
        Thumb thumb = new Thumb();
        thumb.setQuestionId(questionId);
        thumb.setUserId(userId);
        Thumb dbthumb = thumbExtMapper.selectByQuestionIdAndUserId(thumb);
        return dbthumb;
    }

    public void createThumb(Long questionId, Long userId) {
        Thumb thumb = new Thumb();
        thumb.setQuestionId(questionId);
        thumb.setUserId(userId);
        thumb.setGmtTime(System.currentTimeMillis());
        thumbMapper.insertSelective(thumb);
    }

    public void delete(Long questionId, Long userId) {
        Thumb thumb = new Thumb();
        thumb.setQuestionId(questionId);
        thumb.setUserId(userId);
        Thumb dbthumb = thumbExtMapper.selectByQuestionIdAndUserId(thumb);
        thumbMapper.deleteByPrimaryKey(dbthumb.getId());
    }
}
