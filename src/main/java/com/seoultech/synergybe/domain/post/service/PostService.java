package com.seoultech.synergybe.domain.post.service;

import com.seoultech.synergybe.domain.follow.service.FollowService;
import com.seoultech.synergybe.domain.image.Image;
import com.seoultech.synergybe.domain.image.service.ImageService;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.dto.request.CreatePostRequest;
import com.seoultech.synergybe.domain.post.dto.request.UpdatePostRequest;
import com.seoultech.synergybe.domain.post.dto.response.ListPostResponse;
import com.seoultech.synergybe.domain.post.dto.response.PostResponse;
import com.seoultech.synergybe.domain.post.repository.PostRepository;
import com.seoultech.synergybe.domain.postlike.service.PostLikeService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.system.exception.NotExistPostException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.seoultech.synergybe.domain.user.service.UserService;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final FollowService followService;
    private final PostLikeService postLikeService;

    private final UserService userService;

    private final ImageService imageService;

    public PostResponse createPost(User user, CreatePostRequest request) {
        List<MultipartFile> files = request.getFiles();
        List<Image> images = imageService.storeImageList(files);

        Post post = request.toEntity(user, images);
        Post savedPost = postRepository.save(post);
        List<String> imagesUrl = imageService.getImageUrlByPostId(savedPost.getId());

        return PostResponse.from(savedPost, imagesUrl);
    }

    public PostResponse updatePost(User user, UpdatePostRequest request) {
        Post post = this.findPostById(request.getPostId());
        Post updatedPost = postRepository.save(post.updatePost(request));
        List<String> imagesUrl = imageService.getImageUrlByPostId(request.getPostId());

        return PostResponse.from(updatedPost, imagesUrl);
    }

    public PostResponse deletePost(User user, Long postId) {
        Post post = this.findPostById(postId);
        postRepository.delete(post);
        List<String> imagesUrl = imageService.getImageUrlByPostId(postId);

        return PostResponse.from(post, imagesUrl);
    }

    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(NotExistPostException::new);
    }

    public List<Post> findAllByFollowingIdAndEndId(String userId, Long end) {
        return postRepository.findAllByFollowingIdAndEndId(userId, end);
    }

    public PostResponse getPost(User user, Long postId) {
        Post post = this.findPostById(postId);
        List<String> imagesUrl = imageService.getImageUrlByPostId(postId);

        return PostResponse.from(post, imagesUrl);
    }


    public ListPostResponse getLikedPostList(User user) {
        List<Long> postIds = postLikeService.findLikedPostIds(user);

        List<Post> posts = postRepository.findAllById(postIds);

        return ListPostResponse.from(PostResponse.from(posts));
    }

    public ListPostResponse getPostList(Long end) {
        List<Post> posts = postRepository.findAllByEndId(end);

        int count = postRepository.countPostList(end);

        boolean isNext;
        int pageSize = 10;

        if (count > pageSize + 1) {
            isNext = true;
        } else {
            isNext = false;
        }

        return ListPostResponse.from(PostResponse.from(posts), isNext);
    }



    public ListPostResponse getPostListByUser(String userId) {
        List<Post> posts = postRepository.findAllByUserId(userId);

        return ListPostResponse.from(PostResponse.from(posts));

    }

    public ListPostResponse getFeed(Long end, User user) {
        List<String> followingIds = followService.findFollowingIdsByUserId(user.getUserId());
        log.info("followingIds Size{}",followingIds.size());
        List<Post> allPosts = new ArrayList<>();


        for (String id : followingIds) {
            // 각 팔로잉 유저 기준 10개씩 fetch
            List<Post> postList = this.findAllByFollowingIdAndEndId(id, end);
            allPosts.addAll(postList);
        }
        log.info("post size {}",allPosts.size());
//         Object to Stream<Post> : 리스트 내에서 createAt 기준으로 내림차순 정렬을 진행합니다
        Stream<Post> sortedDescPostStream = allPosts.stream().sorted(Comparator.comparing(Post::getId).reversed());
        // Stream<Post> to List
        List<Post> sortedDescPostList = sortedDescPostStream.collect(Collectors.toList());

        // 리스트 크기 계산
        int totalSize = sortedDescPostList.size();
        log.info("{}", totalSize);
        int lastTenElements = 10;

        // 가장 마지막 10개 원소 fetch
        List<Post> lastTenPosts = sortedDescPostList.subList(Math.max(totalSize - lastTenElements, 0), totalSize);

        boolean isNext;
        int pageSize = 10;

        if (totalSize > pageSize) {
            isNext = true;
        } else {
            isNext = false;
        }

        return ListPostResponse.from(PostResponse.from(lastTenPosts), isNext);
    }

    public Page<PostResponse> searchAllPosts(String keyword, Pageable pageable) {
        // query 생성
        Specification<Post> spec = this.search(keyword);

        Page<Post> posts = postRepository.findAll(spec, pageable);
        // 위에서 post를 바로 images url을 넣어서 전달해야함

        return PostResponse.from(posts);
    }

    public Specification<Post> search(String keyword) {
        return new Specification<Post>() {

            @Override
            public Predicate toPredicate(Root<Post> postRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);

                // Post table과 User table을 Left join 수행
//                Join<Post, User> userJoin = postRoot.join("user", JoinType.LEFT);

                try {
                    return cb.or(
                            //join한 table을 통해 authorname 얻음
                            cb.like(postRoot.get("title"), "%" + keyword + "%"),
                            cb.like(postRoot.get("content"), "%" + keyword + "%"),
                            cb.like(postRoot.get("authorName"), "%" + keyword + "%")
                    );
                } catch (Exception e) {
                    log.error("search toPredicate Error {}", e.getMessage());
                    throw new NotExistPostException();
                }
            }
        };
    }
}
