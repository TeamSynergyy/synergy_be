package com.seoultech.synergybe.domain.post.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoultech.synergybe.domain.follow.service.FollowService;
import com.seoultech.synergybe.domain.image.Image;
import com.seoultech.synergybe.domain.image.service.ImageService;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.dto.request.CreatePostRequest;
import com.seoultech.synergybe.domain.post.dto.request.UpdatePostRequest;
import com.seoultech.synergybe.domain.post.dto.response.DeletePostResponse;
import com.seoultech.synergybe.domain.post.dto.response.ListPostResponse;
import com.seoultech.synergybe.domain.post.dto.response.PostResponse;
import com.seoultech.synergybe.domain.post.repository.PostRepository;
import com.seoultech.synergybe.domain.postlike.service.PostLikeService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.system.exception.NotExistPostException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.seoultech.synergybe.domain.user.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final FollowService followService;
    private final PostLikeService postLikeService;

    private final UserService userService;

    private final ImageService imageService;

    public PostResponse createPost(User user, CreatePostRequest request) {
        if (request.getFiles() == null) {
            log.info(">> getfiles is null");
            Post post = request.toEntity(user);
            Post savedPost = postRepository.save(post);

            return PostResponse.from(savedPost);
        } else {
            log.info(">> getfiles is NOT NULL");
            List<MultipartFile> files = request.getFiles();
            List<Image> images = imageService.storeImageList(files);

            Post post = request.toEntity(user, images);
            Post savedPost = postRepository.save(post);
            List<String> imagesUrl = imageService.getImageUrlByPostId(savedPost.getId());

            return PostResponse.from(savedPost, imagesUrl);
        }
    }

    public PostResponse updatePost(UpdatePostRequest request) {
        Post post = this.findPostById(request.getPostId());
        Post updatedPost = postRepository.save(post.updatePost(request));
        List<String> imagesUrl = imageService.getImageUrlByPostId(request.getPostId());

        return PostResponse.from(updatedPost, imagesUrl);
    }

    public DeletePostResponse deletePost(Long postId) {
        Post post = this.findPostById(postId);
        postRepository.delete(post);


        return DeletePostResponse.from(post);
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

        if (imagesUrl.isEmpty()) {
            return PostResponse.from(post);
        }

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
        // todo
        // 썸네일이 없을 경우 없는채로 처리가 되어야 함


        return ListPostResponse.from(PostResponse.from(posts), isNext);
    }



    public ListPostResponse getPostListByUser(String userId) {
        List<Post> posts = postRepository.findAllByUserId(userId);

        return ListPostResponse.from(PostResponse.from(posts));

    }



    @Transactional(readOnly = true)
//    @Cacheable(value = "posts", key = "'weekBestPostList'", cacheManager = "contentCacheManager")
    public ListPostResponse getWeekBestPostList() {
        List<Post> posts = postRepository.findAllByLikeAndDate();

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

    public ListPostResponse getRecommendPostList(User user, Long end) {
        try {
            log.info("get recommend post list start");
            String userId = user.getUserId();
            log.info("user Id {}", userId);

            RestTemplate restTemplate = new RestTemplate();
            log.info("rest template new");
            String fastApiUrl = "http://fastapi:8000"; // 컨테이너 이름과 포트
            String response = restTemplate.getForObject(fastApiUrl + "/recommend/posts/" + userId, String.class);

            log.info("Response from FastAPI: {}", response);

            List<Long> postIds = this.extractIds(response);

            // 빈 배열일 경우 빈 배열 리턴
            if (postIds.isEmpty()) {
                List<Post> posts = new ArrayList<>();
                return ListPostResponse.from(PostResponse.fromEmpty(posts));
            }

            // end 기준 end ~ end + 10 순서에 있는 게시글 가져오기
            int startIdx = end.intValue();
            int endIdx = Math.min(startIdx + 10, postIds.size());

            List<Long> result = postIds.subList(startIdx, endIdx);
            log.info(">> postIds result {} ", result);

            List<Post> posts = postRepository.findAllByIdInOrderByListOrder(result);

            return ListPostResponse.from(PostResponse.from(posts));
        } catch (Exception e) {
            log.error(">> 추천 게시글 가져오기 실패 {}", e.getMessage());
            throw new NotExistPostException();
        }
    }

    private List<Long> extractIds(String response) {
        try {
            // 받은 JSON 응답을 자바 리스트로 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            log.error(">> http cliend response body {}", response);

            return objectMapper.readValue(response, new TypeReference<List<Long>>() {});
        } catch (JsonProcessingException e) {
            log.error(">> 객체 변환 실패 {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
