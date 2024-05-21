package com.seoultech.synergybe.domain.post;

import com.seoultech.synergybe.domain.comment.Comment;
import com.seoultech.synergybe.domain.common.entity.IsDeleted;
import com.seoultech.synergybe.domain.image.Image;
import com.seoultech.synergybe.domain.post.vo.PostAuthorName;
import com.seoultech.synergybe.domain.post.vo.PostContent;
import com.seoultech.synergybe.domain.post.vo.PostThumbnailImageId;
import com.seoultech.synergybe.domain.post.vo.PostTitle;
import com.seoultech.synergybe.domain.postlike.PostLike;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.common.BaseTime;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.seoultech.synergybe.domain.common.constants.DeletedStatus.IS_DELETED_DEFAULT;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE post_id = ?")
public class Post extends BaseTime {
    @Id
    @Column(name = "post_id")
    private String id;

//    @Column(name = "post_sequence")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long postSequence;

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

    @Embedded
    private IsDeleted isDeleted = new IsDeleted(IS_DELETED_DEFAULT);

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
