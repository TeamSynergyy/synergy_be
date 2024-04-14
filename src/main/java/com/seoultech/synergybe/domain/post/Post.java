package com.seoultech.synergybe.domain.post;

import com.seoultech.synergybe.domain.comment.Comment;
import com.seoultech.synergybe.domain.image.Image;
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

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE post_id = ?")
public class Post extends BaseTime {
    @Id
    @Column(name = "post_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String authorName;

    private Long thumbnailImageId;

    @OneToMany
    @JoinColumn(name = "post_id")
    private List<Image> images;

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
    public Post(String id, User user, String title, String content, Long thumbnailImageId) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.thumbnailImageId = thumbnailImageId;
        this.authorName = user.getName();
        this.isDeleted = false;
    }

    public Post updatePost(UpdatePostRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();

        return this;
    }

    public void deletePostLike(PostLike postLike) {
        likes.removeIf(postLike1 -> postLike1.getId().equals(postLike.getId()));
    }
}
