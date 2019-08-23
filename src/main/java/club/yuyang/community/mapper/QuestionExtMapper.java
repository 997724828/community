package club.yuyang.community.mapper;

import club.yuyang.community.dto.QuestionQueryDTO;
import club.yuyang.community.entity.Question;

import java.util.List;

/**
 * @author yuyang
 * @date 2019/8/2 10:09
 */
public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
    int incLike(Question record);
    int decLike(Question record);
    int incCollect(Question record);
    int decCollect(Question question);

    List<Question> selectRelated(Question question);

    //搜索后的问题列表
    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);


}
