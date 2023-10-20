package com.seoultech.synergybe.domain.post;

import com.seoultech.synergybe.domain.comment.Comment;
import com.seoultech.synergybe.domain.post.dto.request.UpdatePostRequest;
import com.seoultech.synergybe.domain.postlike.PostLike;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.system.common.BaseTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE post_id = ?")
public class Post extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    private String title;

    private String content;

    private String authorName;


    @OneToMany(
            mappedBy = "post"
    )
    private List<PostLike> likes = new ArrayList<>();

    @OneToMany(
            mappedBy = "post",
            fetch = FetchType.LAZY
    )
    private List<Comment> comments = new ArrayList<>();


    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Builder
    public Post(User user, String title, String content, List<PostLike> likes, List<Comment> comments) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.comments = comments;
        this.authorName = user.getUsername();
        this.isDeleted = false;
    }

    public Post updatePost(UpdatePostRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();

        return this;
    }
}
