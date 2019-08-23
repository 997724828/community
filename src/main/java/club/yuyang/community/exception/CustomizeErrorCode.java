package club.yuyang.community.exception;

/**
 * @author yuyang
 * @date 2019/8/1 14:14
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode{

     QUESTION_NOT_FOUND(2001,"你所查询的问题不存在，换个试试吧！"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NOT_LOGIN(2003,"当前操作需要登录，请登录后重试"),
    SYS_ERROR(2004,"不要瞎点啦，我这垃圾代码不够健壮啊！"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"你回复的评论不存在"),
    CONTENT_IS_EMPTY(2007,"回复内容不能为空"),
    READ_OTHERS_NOTIFICATION(2008,"差点读到别人的通知消息了你！"),
    NOTIFICATION_NOT_FOUND(2009,"你所查找的通知不存在"),
    FILE_UPLOAD_FAIL(2010,"图片上传失败"),
    QUESTION_IS_OTHERS(2011,"你还想篡改别人的问题呢你，你咋不上天呢！");

    @Override
    public String getMessage(){
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }


    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
