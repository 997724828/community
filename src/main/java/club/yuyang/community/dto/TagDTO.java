package club.yuyang.community.dto;

import java.util.List;

/**
 * @author yuyang
 * @date 2019/8/9 9:30
 */
public class TagDTO {
    private String categoryName;//标签所属层级
    private List<String> tags;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
