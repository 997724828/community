package club.yuyang.community.dto;

/**
 * @author yuyang
 * @date 2019/8/11 9:49
 */
public class FileDTO {
    private int success; // 0失败 | 1成功
    private String message;
    private String url;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
