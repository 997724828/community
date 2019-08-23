package club.yuyang.community.dto;

/**
 * 用于将ajax传递的json字符串反序列化成对象（CommentController）
 * @author yuyang
 * @date 2019/8/2 17:55
 */
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
