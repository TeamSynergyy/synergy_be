package com.seoultech.synergybe.domain.comment.dto.request;


import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequest(
        @NotBlank(message = "유저Id는 필수 항목입니다.")
        String userId,
        @NotBlank(message = "게시글Id는 필수 항목입니다.")
        String postId,
        @NotBlank(message = "댓글은 필수 항목입니다.")
        String comment
){

}
