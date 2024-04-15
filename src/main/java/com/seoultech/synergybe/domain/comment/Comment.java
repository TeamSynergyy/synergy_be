package com.seoultech.synergybe.domain.comment;

import com.seoultech.synergybe.domain.comment.dto.request.CommentRequest;
import com.seoultech.synergybe.domain.comment.vo.CommentContent;
import com.seoultech.synergybe.domain.comment.vo.CommentInfo;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.system.common.BaseTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE comment_id = ?")
public class Comment extends BaseTime {
    @Id
    @Column(name = "comment_id")
    private String id;

    @Embedded
    private CommentContent comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @Embedded
    private CommentInfo commentInfo;

    @Builder
    public Comment(String id, String comment, User user, Post post, Comment parentComment, int depth, int orderNumber,
                   boolean isDeleted, boolean isChildComment) {
        this.id = id;
        this.comment = new CommentContent(comment);
        this.user = user;
        this.post = post;
        this.parentComment = parentComment;
        this.commentInfo = new CommentInfo(isDeleted, isChildComment, depth, orderNumber, parentComment, post);
    }

    public Comment updateComment(CommentRequest request) {
        this.comment = this.comment.updateContent(request.getComment());

        return this;
    }

    public void addPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }
}
