//package com.seoultech.synergybe.domain.post.dto.response;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class ListPostResponse {
//    private List<GetPostResponse> content;
//
//    @JsonProperty("hasNext")
//    private boolean hasNext;
//
//    public static ListPostResponse from(List<GetPostResponse> getPostRespons, boolean isNext) {
//        return new ListPostResponse(getPostRespons, isNext);
//    }
//
//    public static ListPostResponse from(List<GetPostResponse> getPostRespons) {
//        return new ListPostResponse(getPostRespons);
//    }
//
//    public ListPostResponse(List<GetPostResponse> getPostRespons) {
//        this.content = getPostRespons;
//    }
//}
