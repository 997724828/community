package club.yuyang.community.enums;

/**
 * 管理消息通知的已读未读状态
 * @author yuyang
 * @date 2019/8/9 22:28
 */
public enum NotificationStatusEnum {
    UNREAD(0),READ(1);

    private int status;

    public int getStatus() {
        return status;
    }

    NotificationStatusEnum(int status) {
        this.status = status;
    }
}
