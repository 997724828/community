package club.yuyang.community.mapper;

import club.yuyang.community.entity.Thumb;

/**
 * @author yuyang
 * @date 2019/8/16 15:58
 */
public interface ThumbExtMapper {
    Thumb selectByQuestionIdAndUserId(Thumb thumb);
}
