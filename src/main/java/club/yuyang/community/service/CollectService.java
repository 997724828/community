package club.yuyang.community.service;

import club.yuyang.community.dto.CollectDTO;
import club.yuyang.community.dto.PaginationDTO;
import club.yuyang.community.entity.Collect;
import club.yuyang.community.entity.CollectExample;
import club.yuyang.community.entity.Question;
import club.yuyang.community.mapper.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuyang
 * @date 2019/8/20 17:41
 */
@Service
public class CollectService {
    @Resource
    CollectMapper collectMapper;
    @Resource
    CollectExtMapper collectExtMapper;
    @Resource
    QuestionMapper questionMapper;
    @Resource
    QuestionExtMapper questionExtMapper;
    @Resource
    UserMapper userMapper;

    public Collect selectByQuestionIdAndUserId(Long questionId, Long userId) {
        Collect collect = new Collect();
        collect.setQuestionId(questionId);
        collect.setUserId(userId);
        Collect dbCollect = collectExtMapper.selectByQuestionIdAndUserId(collect);
        return dbCollect;
    }

    public void createCollect(Long questionId, Long userId) {
        Collect collect = new Collect();
        collect.setQuestionId(questionId);
        collect.setUserId(userId);
        collect.setGmtCreate(System.currentTimeMillis());
        collectMapper.insertSelective(collect);
    }


    public Integer countByCollect(Long questionId) {
        Question question = questionMapper.selectByPrimaryKey(questionId);
        Integer collectCount = question.getCollectCount();
        return collectCount;
    }

    public void decCollect(Long questionId) {
        Question question = new Question();
        question.setId(questionId);
        question.setCollectCount(1);
        questionExtMapper.decCollect(question);
    }

    public void delete(Long questionId, Long userId) {
        Collect collect = new Collect();
        collect.setQuestionId(questionId);
        collect.setUserId(userId);
        Collect dbCollect = collectExtMapper.selectByQuestionIdAndUserId(collect);
        collectMapper.deleteByPrimaryKey(dbCollect.getId());
    }

    //获得收藏数
    public int getCollectCount(Long userId){
        CollectExample collectExample = new CollectExample();
        collectExample.createCriteria()
                .andUserIdEqualTo(userId);
        int collectCount = collectMapper.countByExample(collectExample);//获得该用户的总收藏数
        return collectCount;
    }

    //列出收藏列表
    public PaginationDTO list(Long userId, Integer page, Integer size) {

        PaginationDTO<CollectDTO> paginationDTO = new PaginationDTO<>();
        Integer totalPage;
        Integer totalCount = this.getCollectCount(userId);//获得该用户的总收藏数
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
        CollectExample collectExample = new CollectExample();
        collectExample.createCriteria()
                .andUserIdEqualTo(userId);
        collectExample.setOrderByClause("gmt_create desc");
        List<Collect> collects = collectMapper.selectByExampleWithRowbounds(collectExample,new RowBounds(offset,size));
        if (collects.size() == 0){
            return paginationDTO;
        }else {
            List<CollectDTO> collectDTOS = new ArrayList<>();

            for (Collect collect : collects) {
                CollectDTO collectDTO = new CollectDTO();
                BeanUtils.copyProperties(collect, collectDTO);
                Question question = questionMapper.selectByPrimaryKey(collect.getQuestionId());//收藏的问题完整对象
                collectDTO.setQuestion(question);
                collectDTO.setUser(userMapper.selectByPrimaryKey(question.getCreator()));
                collectDTOS.add(collectDTO);
            }
            paginationDTO.setData(collectDTOS);
            return paginationDTO;
        }
    }
}
