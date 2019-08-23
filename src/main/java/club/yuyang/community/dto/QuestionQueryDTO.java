package club.yuyang.community.dto;

/**
 * 问题搜索按钮返回对象
 * @author yuyang
 * @date 2019/8/12 18:48
 */
public class QuestionQueryDTO {
    private String search;
    private Integer page;
    private Integer size;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
