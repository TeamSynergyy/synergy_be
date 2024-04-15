package com.seoultech.synergybe.domain.comment.vo;

import com.seoultech.synergybe.domain.comment.Comment;
import com.seoultech.synergybe.domain.post.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentInfo {
    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "is_child_comment")
    private boolean isChildComment;

    @Column(name = "depth")
    private int depth;

    @Column(name = "order_number")
    private int orderNumber;

    public CommentInfo(boolean isDeleted, boolean isChildComment, int depth, int orderNumber, Comment parentComment, Post post) {
        validateParentComment(parentComment, post);
        this.isDeleted = isDeleted;
        this.isChildComment = isChildComment;
        this.depth = depth;
        this.orderNumber = orderNumber;
    }

    private void validateParentComment(Comment parentComment, Post post) {
        if (parentComment == null) {
            this.isChildComment = false;
            this.depth = 0;
            this.orderNumber = post.getComments().size();
        } else {
            this.isChildComment = true;
            this.depth = parentComment.getCommentInfo().getDepth() + 1;
            this.orderNumber = parentComment.getCommentInfo().getOrderNumber();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentInfo that = (CommentInfo) o;
        return isDeleted == that.isDeleted && isChildComment == that.isChildComment && depth == that.depth && orderNumber == that.orderNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isDeleted, isChildComment, depth, orderNumber);
    }
}
