package club.yuyang.community.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuyang
 * @date 2019/7/29 20:57
 */
public class PaginationDTO<T>{

    private List<T> data;
    private boolean showPrevious;                    //是否展示上一页按钮
    private boolean showFirst;                       //是否展示第一页按钮
    private boolean showNext;                        //是否展示下一页按钮
    private boolean showEndPage;                     //是否展示最后一页按钮

    private Integer currentPage;                     //当前页码
    private List<Integer> pages = new ArrayList<>(); //显示出来的可选择页码列表
    private Integer totalPages;                      //总页码数

    public void setPagination(Integer totalPages, Integer page) {

         this.totalPages = totalPages;

         this.currentPage = page;
         pages.add(page);
         for (int i=1;i<=3;i++){
             if (page-i>0){
                 pages.add(0,page-i);
             }

             if (page + i <= totalPages){
                 pages.add(page+i);
             }
         }
         //是否展示上一页
        if (page == 1){
             showPrevious = false;
         }else {
             showPrevious = true;
         }

         //是否展示下一页
        if (page == totalPages){
             showNext = false;
         }else{
             showNext = true;
         }

        //是否展示第一页
        if (pages.contains(1)){
            showFirst = false;
        }else {
            showFirst = true;
        }

        //是否展示最后一页
        if (pages.contains(totalPages)){
            showEndPage = false;
        }else {
            showEndPage = true;
        }
    }


    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public boolean isShowPrevious() {
        return showPrevious;
    }

    public void setShowPrevious(boolean showPrevious) {
        this.showPrevious = showPrevious;
    }

    public boolean isShowFirst() {
        return showFirst;
    }

    public void setShowFirst(boolean showFirst) {
        this.showFirst = showFirst;
    }

    public boolean isShowNext() {
        return showNext;
    }

    public void setShowNext(boolean showNext) {
        this.showNext = showNext;
    }

    public boolean isShowEndPage() {
        return showEndPage;
    }

    public void setShowEndPage(boolean showEndPage) {
        this.showEndPage = showEndPage;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public List<Integer> getPages() {
        return pages;
    }

    public void setPages(List<Integer> pages) {
        this.pages = pages;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}