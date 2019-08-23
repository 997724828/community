package club.yuyang.community.mapper;

import club.yuyang.community.entity.Collect;

/**
 * @author yuyang
 * @date 2019/8/20 17:54
 */
public interface CollectExtMapper {
    Collect selectByQuestionIdAndUserId(Collect collect);
}
