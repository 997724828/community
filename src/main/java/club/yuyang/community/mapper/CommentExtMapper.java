package club.yuyang.community.mapper;

import club.yuyang.community.entity.Comment;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}