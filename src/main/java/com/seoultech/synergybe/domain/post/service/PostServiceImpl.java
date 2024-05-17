package com.seoultech.synergybe.domain.post.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoultech.synergybe.domain.comment.dto.response.GetCommentResponse;
import com.seoultech.synergybe.domain.common.PageInfo;
import com.seoultech.synergybe.domain.common.idgenerator.IdGenerator;
import com.seoultech.synergybe.domain.common.idgenerator.IdPrefix;
import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.follow.service.FollowService;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.dto.request.CreatePostRequest;
import com.seoultech.synergybe.domain.post.dto.request.UpdatePostRequest;
import com.seoultech.synergybe.domain.post.dto.response.GetListPostResponse;
import com.seoultech.synergybe.domain.post.dto.response.GetPostResponse;
import com.seoultech.synergybe.domain.post.exception.PostBadRequestException;
import com.seoultech.synergybe.domain.post.exception.PostNotFoundException;
import com.seoultech.synergybe.domain.post.repository.PostReader;
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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final FollowService followService;
    private final PostLikeService postLikeService;
    private final IdGenerator idGenerator;
    private final UserService userService;
    private final PostReader postReader;
    private final PostValidator postValidator;
//    private final ImageService imageService;

    @Override
    public GetPostResponse createPost(String userId, CreatePostRequest request) {
        User user = userService.getUser(userId);
        if (request.files() == null) {
            log.info(">> getfiles is null");
            String postId = idGenerator.generateId(IdPrefix.POST);
            Post post = Post.builder()
                    .id(postId)
                    .title(request.title())
                    .content(request.content())
                    .user(user)
                    .build();
            Post savedPost = postRepository.save(post);

            return GetPostResponse.builder()
                    .postId(savedPost.getId())
                    .build();
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
        return GetPostResponse.builder().build();
    }

    @Transactional
    public GetPostResponse updatePost(String userId, UpdatePostRequest request) {
        // todo
        // user 검증
        User user = userService.getUser(userId);
        Post post = findPostById(request.postId());

        postValidator.validateUser(user, post);
        validateUser(user, post);
        post.updatePost(request.title(), request.content());
//        List<String> imagesUrl = imageService.getImageUrlByPostId(request.getPostId());

//        return PostResponse.from(updatedPost, imagesUrl);
        return GetPostResponse.builder()
                .postId(post.getId())
                .build();
    }

    private void validateUser(User user, Post post) {
        if (!post.getUser().equals(user)) {
            throw new PostBadRequestException("인증되지 않은 유저입니다.");
        }
    }

    public void deletePost(String userId, String postId) {
        Post post = this.findPostById(postId);
        User user = userService.getUser(userId);
        postValidator.validateUser(user, post);
        validateUser(user, post);
        postRepository.delete(post);
    }



    public List<Post> findAllByFollowingIdAndEndId(String userId, Long end) {
        return postRepository.findAllByFollowingIdAndEndId(userId, end);
    }


//    public GetListPostResponse getMyLikedPostList(String userId) {
//        List<Post> postList = postRepository.findAllByLikeAndDate();
//
//
//        return PostMapperEntityToDto.postListToResponse(postList);
//    }

    public GetListPostResponse getPostRecentList(Long offset) {
//        List<Post> posts = postRepository.findAllByEndId(end);

        // offset은 시작 지점
        // 0부터 시작하며 다음 요청시마다 10씩 증가해야함
        List<Post> posts = postRepository.findAllByCreateAtAndLimit(offset);
        int totalCount = postRepository.countTotalPostSize();

        boolean hasNext;
        int pageSize = 10;

        if (totalCount > pageSize + offset) {
            hasNext = true;
        } else {
            hasNext = false;
        }
        // todo
        // 썸네일이 없을 경우 없는채로 처리가 되어야 함

        return PostMapperEntityToDto.postListToResponse(posts, hasNext);
    }



    public ListResponse<GetPostResponse> getPostListByUser(String userId) {
        List<Post> posts = postRepository.findAllByUserId(userId);
        ListResponse<GetPostResponse> getPostResponseListResponse = new ListResponse(posts);

        return getPostResponseListResponse;
    }



//    @Transactional(readOnly = true)
////    @Cacheable(value = "posts", key = "'weekBestPostList'", cacheManager = "contentCacheManager")
//    public ListPostResponse getWeekBestPostList() {
//        List<Post> posts = postRepository.findAllByLikeAndDate();
//
//        return ListPostResponse.from(GetPostResponse.from(posts));
//    }

    public ListResponse<GetPostResponse> getFeed(Long end, User user) {
        List<String> followingIds = followService.findFollowingIdsByUserId(user.getId());
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

        ListResponse<GetPostResponse> getPostResponseListResponse = new ListResponse(lastTenPosts);

        return getPostResponseListResponse;
    }

    public Page<Post> searchAllPosts(String keyword, Pageable pageable) {
        // query 생성
        Specification<Post> spec = this.search(keyword);

        Page<Post> posts = postRepository.findAll(spec, pageable);
        // 위에서 post를 바로 images url을 넣어서 전달해야함


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

    public ListResponse<GetPostResponse> getRecommendPostList(User user, Long end) {
        try {
            log.info("get recommend post list start");
            String userId = user.getId();
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

    public GetPostResponse getPost(String postId) {
        Post post = findPostById(postId);
        List<GetCommentResponse> commentResponses = post.getComments().stream()
                .map(comment -> new GetCommentResponse(
                        comment.getId(),
                        comment.getUser().getId(),
                        comment.getPost().getId(),
                        comment.getComment().getContent(),
                        comment.getUpdateAt()
                ))
                .toList();

        return GetPostResponse.builder()
                .postId(post.getId())
                .title(post.getTitle().getTitle())
                .content(post.getContent().getContent())
                .userId(post.getUser().getId())
                .likes(post.getLikes().size())
                .authorName(post.getAuthorName().getAuthorName())
                .createAt(post.getCreateAt())
                .updateAt(post.getUpdateAt())
                .commentList(commentResponses)
                .build();
    }

    public Post findPostById(String postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("존재하지 않는 게시글입니다."));
    }

    public GetListPostResponse getWeekBestPostList() {
        List<GetPostResponse> postList = postRepository.findAllByMostLikedAndRecentOneWeek();
        PageInfo pageInfo = PageInfo.of(postList.size());
        return new GetListPostResponse(postList, pageInfo);
    }
}
