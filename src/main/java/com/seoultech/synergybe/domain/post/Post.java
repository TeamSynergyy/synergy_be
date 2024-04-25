package com.seoultech.synergybe.domain.post;

import com.seoultech.synergybe.domain.comment.Comment;
import com.seoultech.synergybe.domain.image.Image;
import com.seoultech.synergybe.domain.post.dto.request.UpdatePostRequest;
import com.seoultech.synergybe.domain.post.vo.PostAuthorName;
import com.seoultech.synergybe.domain.post.vo.PostContent;
import com.seoultech.synergybe.domain.post.vo.PostThumbnailImageId;
import com.seoultech.synergybe.domain.post.vo.PostTitle;
import com.seoultech.synergybe.domain.postlike.PostLike;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.common.BaseTime;
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
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private PostTitle title;

    @Embedded
    private PostContent content;

    @Embedded
    private PostAuthorName authorName;

    @Embedded
    private PostThumbnailImageId information;

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

    @Builder
    public Post(String id, User user, String title, String content, String thumbnailImageId) {
        this.id = id;
        this.user = user;
        this.title = new PostTitle(title);
        this.content = new PostContent(content);
        this.information = new PostThumbnailImageId(thumbnailImageId);
        this.authorName = new PostAuthorName(user.getName().getName());
    }

    public void deletePostLike(PostLike postLike) {
        likes.removeIf(postLike1 -> postLike1.getId().equals(postLike.getId()));
    }

    public void updatePost(String title, String content) {
        this.title = new PostTitle(title);
        this.content = new PostContent(content);

    }
}
