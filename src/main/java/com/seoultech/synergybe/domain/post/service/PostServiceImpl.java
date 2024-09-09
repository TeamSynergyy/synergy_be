package com.seoultech.synergybe.domain.post.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoultech.synergybe.domain.comment.dto.response.GetCommentResponse;
import com.seoultech.synergybe.domain.common.generator.IdGenerator;
import com.seoultech.synergybe.domain.common.generator.IdPrefix;
import com.seoultech.synergybe.domain.common.generator.TokenGenerator;
import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.follow.service.FollowService;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.controller.dto.request.CreatePostRequest;
import com.seoultech.synergybe.domain.post.controller.dto.request.UpdatePostRequest;
import com.seoultech.synergybe.domain.post.controller.dto.response.GetPostResponse;
import com.seoultech.synergybe.domain.post.exception.PostBadRequestException;
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
import org.springframework.cache.annotation.Cacheable;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final FollowService followService;
    private final PostLikeService postLikeService;
    private final IdGenerator idGenerator;
    private final TokenGenerator tokenGenerator;
    private final UserService userService;
    private final PostValidator postValidator;
//    private final ImageService imageService;

    /**
     * - 게시글 생성
     * - 게시글 저장 객체에 저장을 위임
     * Post 생성에 필요한 인자들
     *
     * @param userToken
     * @param request
     * @return
     */
    @Override
    @Transactional
    public String createPost(String userToken, CreatePostRequest request) {
        User user = userService.getUserByToken(userToken);
        if (request.files() != null) {
            throw new IllegalArgumentException("file은 입력받지 않습니다.");
        }
        Long postId = idGenerator.generateId();
        String postToken = tokenGenerator.generateToken(IdPrefix.POST);
        Post post = Post.builder()
                .id(postId)
                .postToken(postToken)
                .title(request.title())
                .content(request.content())
                .user(user)
                .build();
        postRepository.save(post);

        return post.getPostToken();
    }

    @Override
    @Transactional
    public String updatePost(String userToken, UpdatePostRequest request) {
        User user = userService.getUserByToken(userToken);
        Post post = postRepository.findByPostToken(request.postToken());
        postValidator.validateUser(user, post);
        post.updatePost(request.title(), request.content());
        return post.getPostToken();
    }

    private void validateUser(User user, Post post) {
        if (!post.getUser().equals(user)) {
            throw new PostBadRequestException("인증되지 않은 유저입니다.");
        }
    }

    @Override
    @Transactional
    public void deletePost(String userToken, String postToken) {
        Post post = postRepository.findByPostToken(postToken);
        User user = userService.getUserByToken(userToken);
        postValidator.validateUser(user, post);
        postRepository.delete(post);
    }

    @Override
    public ListResponse<GetPostResponse> getMyLikedPostList(String userToken) {
        Long userId = userService.getUserByToken(userToken).getId();
        List<Post> postList = postRepository.findMyLikedPostList(userId);

        return PostMapperEntityToDto.postListToResponse(postList);
    }

    @Override
    public ListResponse<GetPostResponse> getRecentPostList(String offsetToken) {
        // offset은 시작 지점
        // 0부터 시작하며 다음 요청시마다 10씩 증가해야함
        Post offsetPost = postRepository.findByPostToken(offsetToken);
        Long offset = offsetPost.getId();
        List<Post> posts = postRepository.findAllByCreateAtAndLimit(offset);
        long totalCount = postRepository.countSize();

        boolean hasNext;
        int pageSize = 10;

        if (totalCount > pageSize + offset) {
            hasNext = true;
        } else {
            hasNext = false;
        }

        return PostMapperEntityToDto.postListToResponse(posts, hasNext);
    }

    @Override
    public ListResponse<GetPostResponse> getPostListByUser(String userToken) {
        Long userId = userService.getUserByToken(userToken).getId();
        List<Post> posts = postRepository.findAllByUserId(userId);

        return PostMapperEntityToDto.postListToResponse(posts);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "Post", cacheManager = "testCacheManager")
    public ListResponse<GetPostResponse> getWeekBestPostList() {
        List<Post> posts = postRepository.findAllByLikeAndDate();

        return PostMapperEntityToDto.postListToResponse(posts);
    }

    @Override
    public ListResponse<GetPostResponse> getFeed(String offsetToken, String userToken) {
        List<Long> followingIds = followService.getFollowingIdList(userToken);
        List<Post> allPosts = new ArrayList<>();

        for (Long userId : followingIds) {
            // 각 팔로잉 유저 기준 10개씩 fetch
            List<Post> postList = findAllByFollowingIdAndEndId(userId, offsetToken);
            allPosts.addAll(postList);
        }
        log.info("post size {}",allPosts.size());
        //  리스트 내에서 createAt 기준으로 내림차순 정렬을 진행합니다
        Stream<Post> sortedDescPostStream = allPosts.stream().sorted(Comparator.comparing(Post::getId).reversed());
        // Stream<Post> to List
        List<Post> sortedDescPostList = sortedDescPostStream.toList();

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

        return PostMapperEntityToDto.postListToResponse(lastTenPosts, isNext);
    }

    @Override
    public Page<Post> searchAllPosts(String keyword, Pageable pageable) {
        Specification<Post> spec = search(keyword);
        Page<Post> posts = postRepository.findAll(spec, pageable);

        return posts;
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

    public ListResponse<GetPostResponse> getRecommendPostList(String userToken, String postToken) {
        try {
            log.info("get recommend post list start");
            Long userId = userService.getUserByToken(userToken).getId();
            log.info("user Id {}", userId);

            Long end = postRepository.findByPostToken(postToken).getId();
            RestTemplate restTemplate = new RestTemplate();

            log.info("rest template new");
            String fastApiUrl = "http://fastapi:8000"; // 컨테이너 이름과 포트
            String response = restTemplate.getForObject(fastApiUrl + "/recommend/posts/" + userId, String.class);

            log.info("Response from FastAPI: {}", response);

            List<String> postIds = this.extractIds(response);

            // 빈 배열일 경우 빈 배열 리턴
            if (postIds.isEmpty()) {
                List<Post> posts = new ArrayList<>();
                return PostMapperEntityToDto.postListToResponse(posts);
            }

            // end 기준 end ~ end + 10 순서에 있는 게시글 가져오기
            int startIdx = end.intValue();
            int endIdx = Math.min(startIdx + 10, postIds.size());

            List<String> result = postIds.subList(startIdx, endIdx);
            log.info(">> postIds result {} ", result);

            List<Post> posts = postRepository.findAllByIdInOrderByListOrder(result);

            ListResponse<GetPostResponse> getPostResponseListResponse = new ListResponse(posts);

            return getPostResponseListResponse;
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

    @Override
    public GetPostResponse getPost(String postToken) {
        Post post = postRepository.findByPostToken(postToken);
        List<GetCommentResponse> commentResponses = post.getComments().stream()
                .map(comment -> new GetCommentResponse(
                        comment.getCommentToken(),
                        comment.getUser().getUserToken(),
                        comment.getPost().getPostToken(),
                        comment.getComment().getContent(),
                        comment.getUpdateAt()
                ))
                .toList();

        return GetPostResponse.builder()
                .postToken(post.getPostToken())
                .title(post.getTitle().getTitle())
                .content(post.getContent().getContent())
                .userToken(post.getUser().getUserToken())
                .likes(post.getLikes().size())
                .authorName(post.getAuthorName().getAuthorName())
                .createAt(post.getCreateAt())
                .updateAt(post.getUpdateAt())
                .commentList(commentResponses)
                .build();
    }

    private List<Post> findAllByFollowingIdAndEndId(Long userId, String offsetToken) {
        return postRepository.findAllByIdsAndEndId(userId, offsetToken);
    }
}
