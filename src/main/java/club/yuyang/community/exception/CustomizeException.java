package club.yuyang.community.exception;

/**
 * @author yuyang
 * @date 2019/8/1 13:56
 */
public class CustomizeException extends RuntimeException{

    private String message;
    private Integer code;

    public CustomizeException(ICustomizeErrorCode errorCode){
        this.message=errorCode.getMessage();
        this.code=errorCode.getCode();
    }


    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
