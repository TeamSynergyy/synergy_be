package com.seoultech.synergybe.domain.post.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoultech.synergybe.domain.common.idgenerator.IdGenerator;
import com.seoultech.synergybe.domain.common.idgenerator.IdPrefix;
import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.follow.service.FollowService;
//import com.seoultech.synergybe.domain.image.service.ImageService;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.dto.request.CreatePostRequest;
import com.seoultech.synergybe.domain.post.dto.request.UpdatePostRequest;
import com.seoultech.synergybe.domain.post.dto.response.DeletePostResponse;
import com.seoultech.synergybe.domain.post.dto.response.ListPostResponse;
import com.seoultech.synergybe.domain.post.dto.response.GetPostResponse;
import com.seoultech.synergybe.domain.post.exception.PostNotFoundException;
import com.seoultech.synergybe.domain.post.repository.PostRepository;
import com.seoultech.synergybe.domain.postlike.service.PostLikeService;
import com.seoultech.synergybe.domain.user.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.seoultech.synergybe.domain.user.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

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
    private final IdGenerator idGenerator;
    private final UserService userService;

//    private final ImageService imageService;

    public GetPostResponse createPost(User user, CreatePostRequest request) {
        if (request.files() == null) {
            log.info(">> getfiles is null");
            String postId = idGenerator.generateId(IdPrefix.POST);
            Post post = Post.builder()
                    .id(postId).title(request.title()).user(user)
                    .build();
            Post savedPost = postRepository.save(post);
            GetPostResponse getPostResponse = GetPostResponse.builder().build();

            return getPostResponse;
//        } else {
//            log.info(">> getfiles is NOT NULL");
//            List<MultipartFile> files = request.files();
////            List<Image> images = imageService.storeImageList(files);
//
////            Post post = request.toEntity(user, images);
//            Post post = request.toEntity(user);
//            Post savedPost = postRepository.save(post);
////            List<String> imagesUrl = imageService.getImageUrlByPostId(savedPost.getId());
//
////            return PostResponse.from(savedPost, imagesUrl);
//            return GetPostResponse.from(savedPost);
        }
    }

    @Transactional
    public GetPostResponse updatePost(UpdatePostRequest request) {
        Post post = findPostById(request.postId());
        post.updatePost(request.title(), request.content());
//        List<String> imagesUrl = imageService.getImageUrlByPostId(request.getPostId());

//        return PostResponse.from(updatedPost, imagesUrl);
        GetPostResponse getPostResponse = GetPostResponse.builder().build();
        return getPostResponse;
    }

    public void deletePost(String postId) {
        Post post = this.findPostById(postId);
        postRepository.delete(post);
    }

    public Post findPostById(String postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("존재하지 않는 게시글입니다."));
    }

    public List<Post> findAllByFollowingIdAndEndId(String userId, Long end) {
        return postRepository.findAllByFollowingIdAndEndId(userId, end);
    }


    public ListResponse<GetPostResponse> getLikedPostList(User user) {
        List<String> postIds = postLikeService.findLikedPostIds(user);

        List<Post> posts = postRepository.findAllById(postIds);

        return new ListResponse(posts);
    }

    public ListResponse<GetPostResponse> getPostList(String end) {
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


//        return ListPostResponse.from(GetPostResponse.from(posts), isNext);
        return new ListResponse(posts);
    }



    public ListPostResponse getPostListByUser(String userId) {
        List<Post> posts = postRepository.findAllByUserId(userId);

        return ListPostResponse.from(GetPostResponse.from(posts));

    }



    @Transactional(readOnly = true)
//    @Cacheable(value = "posts", key = "'weekBestPostList'", cacheManager = "contentCacheManager")
    public ListPostResponse getWeekBestPostList() {
        List<Post> posts = postRepository.findAllByLikeAndDate();

        return ListPostResponse.from(GetPostResponse.from(posts));
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

        return ListPostResponse.from(GetPostResponse.from(lastTenPosts), isNext);
    }

    public Page<GetPostResponse> searchAllPosts(String keyword, Pageable pageable) {
        // query 생성
        Specification<Post> spec = this.search(keyword);

        Page<Post> posts = postRepository.findAll(spec, pageable);
        // 위에서 post를 바로 images url을 넣어서 전달해야함

        return GetPostResponse.from(posts);
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
                    throw new PostNotFoundException("존재하지 않는 게시글입니다.");
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

            List<String> postIds = this.extractIds(response);

            // 빈 배열일 경우 빈 배열 리턴
//            if (postIds.isEmpty()) {
//                List<Post> posts = new ArrayList<>();
//                return ListPostResponse.from(GetPostResponse.fromEmpty(posts));
//            }

            // end 기준 end ~ end + 10 순서에 있는 게시글 가져오기
            int startIdx = end.intValue();
            int endIdx = Math.min(startIdx + 10, postIds.size());

            List<String> result = postIds.subList(startIdx, endIdx);
            log.info(">> postIds result {} ", result);

            List<Post> posts = postRepository.findAllByIdInOrderByListOrder(result);

            return ListPostResponse.from(GetPostResponse.from(posts));
        } catch (Exception e) {
            log.error(">> 추천 게시글 가져오기 실패 {}", e.getMessage());
            throw new PostNotFoundException("존재하지 않는 게시글입니다.");
        }
    }

    private List<String> extractIds(String response) {
        try {
            // 받은 JSON 응답을 자바 리스트로 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            log.error(">> http cliend response body {}", response);

            return objectMapper.readValue(response, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            log.error(">> 객체 변환 실패 {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public GetPostResponse getPost(String postId) {
        GetPostResponse getPostResponse = GetPostResponse.builder().build();
        return getPostResponse;
    }
}
