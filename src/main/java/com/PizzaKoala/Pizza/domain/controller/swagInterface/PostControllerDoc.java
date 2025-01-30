package com.PizzaKoala.Pizza.domain.controller.swagInterface;

import com.PizzaKoala.Pizza.domain.controller.request.PostCreateRequest;
import com.PizzaKoala.Pizza.domain.controller.request.PostModifyRequest;
import com.PizzaKoala.Pizza.domain.controller.response.PostListResponse;
import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.model.PostWithCommentsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

//@Tag(name = "게시글APIs", description = "최근 게시글,유저,내가 팔로잉,좋아요순")
public interface PostControllerDoc {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글이 완료되었습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "게시글 업로드 성공 예제",
                                    value = """
                                            {
                                              "resultCode": "SUCCESS",
                                              "result": "null"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "400", description = "사진은 1~5개만 올릴 수 있습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "실패 예제) 사진이 없거나 5장 초과일때",
                                    value = """
                                            {
                                              "resultCode": "ONE_TO_FIVE_IMAGES_ARE_REQUIRED",
                                              "result": "null"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "404", description = "토큰 안에 있는 계정이 존재하지 않을때 (계정 삭제한 후 게시글 업로드 시도했을때)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "존재하지 않는 계정입니다.",
                                    value = """
                                            {
                                              "resultCode": "MEMBER_NOT_FOUND",
                                              "result": "The user does not exist."
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "500", description = "사진 업로드(s3포함) 과정에서 업로드 실패했을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "서버 문제로 업로드에 실패했습니다. 잠시후 다시 업로드 해주세요",
                                    value = """
                                            {
                                              "resultCode": "S3_UPLOAD_FAILED",
                                              "result": "Error occurred while uploading to S3."
                                            }
                                            """
                            )))
    })
    @Operation(summary = "게시글 올리기 with 사진", description = "사진과 함께 게시글을 해주세요. 사진은 1~5개까지 올려주세요. *사진을 하나 이상 올려야 실행됩니다.*", tags = "게시글쓰기APIs")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response<Void> create(@RequestPart("files") List<MultipartFile> files,
                                 @RequestPart("request") PostCreateRequest request, Authentication authentication);

    /**
     * update a post - TODO: 이건 리턴 아무것도 안하고 그냥 프론트측에서 단건 포스트 조회 하는게 좋을까..?! 받아오는김에 다 전해줄지..
     * <p>
     * -시간되면 수정하기-
     * 사진 다 삭제하는것보다 삭제 요청받은 사진들만 삭제하고 추가 요청받은 사진들은 요청하고
     * 사진은 변경이 없다면 그냥 건들지 않는 로직으로 변경하는게 좋겠다.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글이 업데이트가 완료되었습니다.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "게시글 업데이트 성공 예제",
                                    value = """
                                            {
                                              "resultCode": "SUCCESS",
                                              "result": "null"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "400", description = "사진은 1~5개만 올릴 수 있습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "실패 예제) 사진이 없거나 5장 초과일때",
                                    value = """
                                            {
                                              "resultCode": "ONE_TO_FIVE_IMAGES_ARE_REQUIRED",
                                              "result": "null"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "다른 사람의 게시글 수정 요청",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "실패 예제) 다른 사람의 게시글은 수정할 수 없습니다.",
                                    value = """
                                            {
                                              "resultCode": "INVALID_PERMISSION",
                                              "result": "null"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글, 계정일 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = {@ExampleObject(
                                    name = "실패 예제 1) 존재하지 않는 게시글입니다.",
                                    value = """
                                            {
                                              "resultCode": "POST_NOT_FOUND",
                                              "result": "null"
                                            }
                                            """
                            ), @ExampleObject(
                                    name = "실패 예제 2) 존재하지 않는 계정입니다.",
                                    value = """
                                            {
                                              "resultCode": "MEMBER_NOT_FOUND",
                                              "result": "The user does not exist."
                                            }
                                            """
                            )
                            })),
            @ApiResponse(responseCode = "500", description = "사진 업로드(s3포함) 과정에서 업로드 실패했을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "서버 문제로 업로드에 실패했습니다. 잠시후 다시 업로드 해주세요",
                                    value = """
                                            {
                                              "resultCode": "S3_UPLOAD_FAILED",
                                              "result": "Error occurred while uploading to S3."
                                            }
                                            """
                            )))})
    @Operation(summary = "게시글 수정 기능", description = "사진과 함께 게시글을 해주세요. 사진은 1~5개까지 올려주세요. *사진을 하나 이상 올려야 실행됩니다.*",
    tags = "게시글쓰기APIs")
    @PutMapping(value = "/{postId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response<Void> Modify(    @Parameter(
            description = "해당 게시글 아이디 입력",
            examples = {
                    @ExampleObject(name = "내 게시글 - 이미 삭제되었을 경우 내 게시글 아이디를 직접 입력해주세요.", value = "6"),
                    @ExampleObject(name = "내 게시글이 아닌 게시글", value = "4"),
                    @ExampleObject(name = "존재하지않는 게시글", value = "-1")
            }
    ) @PathVariable Long postId, @RequestPart List<MultipartFile> files, @RequestPart PostModifyRequest request, Authentication authentication)throws IOException;

    /**
     * delete a post- 포스트 단건 삭제(soft delete)
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글이 업데이트가 완료되었습니다.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "성공 예제) 게시글 삭제",
                                    value = """
                                            {
                                              "resultCode": "SUCCESS",
                                              "result": "null"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "다른 사람의 게시글 삭제 요청",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "실패 예제) 다른 사람의 게시글은 삭제할 수 없습니다.",
                                    value = """
                                            {
                                              "resultCode": "INVALID_PERMISSION",
                                              "result": "null"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 계정일때 (계정 삭제한 후 게시글 업로드 시도했을때)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = {@ExampleObject(
                                    name = "실패 예제 1) 존재하지 않는 게시글입니다.",
                                    value = """
                                            {
                                              "resultCode": "POST_NOT_FOUND",
                                              "result": "null"
                                            }
                                            """
                            ), @ExampleObject(
                                    name = " 실패 예제 2) 존재하지 않는 계정입니다.",
                                    value = """
                                            {
                                              "resultCode": "MEMBER_NOT_FOUND",
                                              "result": "The user does not exist."
                                            }
                                            """
                            )
                            }))})
    @Operation(summary = "게시글 삭제 기능", description = "내 게시글을 삭제합니다.", tags = "게시글쓰기APIs")
    @DeleteMapping("/{postId}")
    public Response<Void> delete(  @Parameter(
            description = "해당 게시글 아이디 입력",
            examples = {
                    @ExampleObject(name = "내 게시글 - 이미 삭제되었을 경우 내 게시글 아이디를 직접 입력해주세요.", value = "6"),
                    @ExampleObject(name = "내 게시글이 아닌 게시글", value = "4"),
                    @ExampleObject(name = "존재하지않는 게시글", value = "-1")
            }
    ) @PathVariable Long postId, Authentication authentication);

    /**
     * get a post- 포스트 단건 조회
     * 나중에 사용자가 많을때 페이지네이션 고려해보기
     * 컨트롤러 안에 댓글이 60개보다 많을때 페이지네이션 되게만들어도 될듯..
     * 일단 60개보다 더 있다면 댓글 가져오는 comment api with api 만들어서 60개 이후 댓글 가져오는거 만들기
     * 비동기로 한다던데 필요할때 알아보기
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 단건 조회- with 모든 댓글(사용자가 많아지면 댓글은 페이지네이션으로 처리하기).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "게시글 단건 조회 성공 예제",
                                    value = """
                                            {
                                               "resultCode": "SUCCESS",
                                               "result": {
                                                 "게시글 쓴 유저 아이디": 1,
                                                 "게시글 쓴 유저 프로필 사진 url": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-11-12-21-27-13-2.jpeg",
                                                 "게시글 쓴 유저 닉네임": "MEEP",
                                                 "게시글 아이디": 3,
                                                 "제목": "박물관 다녀온 날",
                                                 "게시글 내용": "박물관 굿즈들 구경하는 재미가 있다. 아기 용가리들도 너무 귀여웠다. ",
                                                 "좋아요 수": 1,
                                                 "사진 url": [
                                                   "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2024-01-20-15-03-57.jpeg"
                                                 ],
                                                 "생성일자": "2025-01-21 20:27:13",
                                                 "수정일자": "2025-01-21 20:27:14",
                                                 "comments": [
                                                   {
                                                     "댓글 쓴 유저 아이디": 1,
                                                     "유저 닉네임": "MEEP",
                                                     "유저 프로필 사진": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-11-12-21-27-13-2.jpeg",
                                                     "댓글 아이디": 5,
                                                     "댓글 내용": "앙뇽",
                                                     "게시글 아이디": 3,
                                                     "생성날짜": "2025-01-21 23:48:57",
                                                     "수정날짜": "2025-01-21 23:48:57"
                                                   }
                                                 ]
                                               }
                                             }
                                            """
                            ))),
            @ApiResponse(responseCode = "404", description = "삭제된 게시글 요청",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = {@ExampleObject(
                                    name = "실패 예제) 존재하지 않는 게시글입니다.",
                                    value = """
                                            {
                                              "resultCode": "POST_NOT_FOUND",
                                              "result": "null"
                                            }
                                            """
                            )
                            })),
            @ApiResponse(responseCode = "401", description = "로그인 하지 않았을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = {@ExampleObject(
                                    name = "실패 예제) 로그인이 필요한 요청입니다.",
                                    value = """
                                            {
                                              "resultCode": "INVALID_TOKEN",
                                              "message": "Full authentication is required to access this resource"
                                            }
                                            """
                            )}))})
    @Operation(summary = "게시글 단건 조회", description = "게시글 아이디로 단건 조회합니다",tags = "게시글조회APIs")
     Response<PostWithCommentsDTO> getAPost(@Parameter(
            description = "해당 게시글 아이디 입력",
            examples = {
                    @ExampleObject(name = "성공예제) 게시글 검색", value = "3"),
                    @ExampleObject(name = "실패 예제) 존재하지않는 게시글", value = "-1")
            }
    ) @PathVariable Long postId);

    /**
     * my posts- 내 포스트들 리스트로 끌고 오기/ 메인 게시물
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 게시글 다건 조회.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "게시글 다건 조회 성공 예제",
                                    value = """
                                            {
                                              "resultCode": "SUCCESS",
                                              "result": {
                                                "content": [
                                                  {
                                                    "id": 3,
                                                    "title": "박물관 다녀온 날",
                                                    "imageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2024-01-20-15-03-57.jpeg",
                                                    "imageCount": 1
                                                  },
                                                  {
                                                    "id": 2,
                                                    "title": "애민이",
                                                    "imageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2024-06-17-22-15-23.jpeg",
                                                    "imageCount": 1
                                                  },
                                                  {
                                                    "id": 1,
                                                    "title": "What a meepy day.",
                                                    "imageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/47896696-f511-4697-b3f5-2a7b194a3dbc.png",
                                                    "imageCount": 3
                                                  }
                                                ],
                                                "pageable": {
                                                  "pageNumber": 0,
                                                  "pageSize": 20,
                                                  "sort": {
                                                    "empty": true,
                                                    "sorted": false,
                                                    "unsorted": true
                                                  },
                                                  "offset": 0,
                                                  "paged": true,
                                                  "unpaged": false
                                                },
                                                "totalElements": 3,
                                                "totalPages": 1,
                                                "last": true,
                                                "size": 20,
                                                "number": 0,
                                                "sort": {
                                                  "empty": true,
                                                  "sorted": false,
                                                  "unsorted": true
                                                },
                                                "numberOfElements": 3,
                                                "first": true,
                                                "empty": false
                                              }
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "로그인 하지 않았을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = {@ExampleObject(
                                    name = "실패 예제) 로그인이 필요한 요청입니다.",
                                    value = """
                                            {
                                              "resultCode": "INVALID_TOKEN",
                                              "message": "Full authentication is required to access this resource"
                                            }
                                            """
                            )
                            }))})
    @Operation(summary = "내 게시글 다건 조회", description = "나의 게시글들을 조회합니다",tags = "게시글조회APIs")
    public Response<Page<PostListResponse>> myPosts(Authentication authentication, @ParameterObject @PageableDefault(
            page = 0,
            size = 20
    ) Pageable pageable);

    /**
     * member posts-특정 맴버 포스트들 리스트로 끌고 오기
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 단건 조회- with 모든 댓글(사용자가 많아지면 댓글은 페이지네이션으로 처리하기).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "게시글 단건 조회 성공 예제",
                                    value = """
                                            {
                                              "resultCode": "SUCCESS",
                                              "result": {
                                                "content": [
                                                  {
                                                    "id": 3,
                                                    "title": "박물관 다녀온 날",
                                                    "imageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2024-01-20-15-03-57.jpeg",
                                                    "imageCount": 1
                                                  },
                                                  {
                                                    "id": 2,
                                                    "title": "애민이",
                                                    "imageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2024-06-17-22-15-23.jpeg",
                                                    "imageCount": 1
                                                  },
                                                  {
                                                    "id": 1,
                                                    "title": "What a meepy day.",
                                                    "imageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/47896696-f511-4697-b3f5-2a7b194a3dbc.png",
                                                    "imageCount": 3
                                                  }
                                                ],
                                                "pageable": {
                                                  "pageNumber": 0,
                                                  "pageSize": 20,
                                                  "sort": {
                                                    "empty": true,
                                                    "sorted": false,
                                                    "unsorted": true
                                                  },
                                                  "offset": 0,
                                                  "paged": true,
                                                  "unpaged": false
                                                },
                                                "totalElements": 3,
                                                "totalPages": 1,
                                                "last": true,
                                                "size": 20,
                                                "number": 0,
                                                "sort": {
                                                  "empty": true,
                                                  "sorted": false,
                                                  "unsorted": true
                                                },
                                                "numberOfElements": 3,
                                                "first": true,
                                                "empty": false
                                              }
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 맴버아이디 요청",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = {@ExampleObject(
                                    name = "실패 예제) 존재하지 않는 회원입니다.",
                                    value = """
                                            {
                                              "resultCode": "MEMBER_NOT_FOUND",
                                              "message": "The user does not exist., 0 not found"
                                            }
                                            """
                            )
                            })),
            @ApiResponse(responseCode = "401", description = "로그인 하지 않았을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = {@ExampleObject(
                                    name = "실패 예제) 로그인이 필요한 요청입니다.",
                                    value = """
                                            {
                                              "resultCode": "INVALID_TOKEN",
                                              "message": "Full authentication is required to access this resource"
                                            }
                                            """
                            )}))})
    @Operation(summary = "특정 맴버의 게시글들 조회", description = "맴버 아이디로 게시글 다건 조회합니다",tags = "게시글조회APIs")
    public Response<Page<PostListResponse>> memberPosts(@Parameter(
            description = "유저 아이디를 입력해주세요.",
            examples = {
                    @ExampleObject(name = "예시1-성공", value = "1"),
                    @ExampleObject(name = "예시2-실패", value = "0"),
                    @ExampleObject(name = "예시3-성공", value = "2"),
                    @ExampleObject(name = "예시4-성공", value = "3")}
    ) @PathVariable Long memberId, @ParameterObject @PageableDefault(
            page = 0,
            size = 20
    ) Pageable pageable);

    /**
     * 메인 패이지- 팔로잉 맴버들의 포스트들
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 다건 조회) 내가 팔로일하는 유저들 게시글들.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "게시글 다건 조회 성공 예제",
                                    value = """
                                            {
                                              "resultCode": "SUCCESS",
                                              "result": {
                                                "content": [
                                                  {
                                                    "id": 5,
                                                    "title": "파스스",
                                                    "imageUrl": null,
                                                    "imageCount": 0
                                                  },
                                                  {
                                                    "id": 4,
                                                    "title": "점심",
                                                    "imageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2024-01-20-15-03-43.jpeg",
                                                    "imageCount": 1
                                                  }
                                                ],
                                                "pageable": {
                                                  "pageNumber": 0,
                                                  "pageSize": 20,
                                                  "sort": {
                                                    "empty": true,
                                                    "sorted": false,
                                                    "unsorted": true
                                                  },
                                                  "offset": 0,
                                                  "paged": true,
                                                  "unpaged": false
                                                },
                                                "totalElements": 2,
                                                "totalPages": 1,
                                                "last": true,
                                                "size": 20,
                                                "number": 0,
                                                "sort": {
                                                  "empty": true,
                                                  "sorted": false,
                                                  "unsorted": true
                                                },
                                                "numberOfElements": 2,
                                                "first": true,
                                                "empty": false
                                              }
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "로그인 하지 않았을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = {@ExampleObject(
                                    name = "실패 예제) 로그인이 필요한 요청입니다.",
                                    value = """
                                            {
                                              "resultCode": "INVALID_TOKEN",
                                              "message": "Full authentication is required to access this resource"
                                            }
                                            """
                            )}))})
    @Operation(summary = "메인 페이지 조회) 팔로잉 유저들의 게시글들", description = "내가 팔로우 하는 유저들의 게시글을 가져 옵니다 20개씩 게시글을 가져옵니다. 페이지번호 0부터 시작",
    tags = "메인페이지APIs")
    public Response<Page<PostListResponse>> FollowingList(@ParameterObject @PageableDefault(
            page = 0,
            size = 20
    ) Pageable pageable, Authentication authentication);

    /**
     * 메인 패이지- 좋아요 순 포스트들
     * 로그인 없이 접속 가능한 페이지
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메인 페이지 좋아요 순 게시글들 - 로그인 전",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "게시글(좋아요순) 다건 조회 성공 예제",
                                    value = """
                                            {
                                              "resultCode": "SUCCESS",
                                              "result": {
                                                "content": [
                                                  {
                                                    "id": 1,
                                                    "title": "What a meepy day.",
                                                    "imageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/47896696-f511-4697-b3f5-2a7b194a3dbc.png",
                                                    "imageCount": 3
                                                  },
                                                  {
                                                    "id": 5,
                                                    "title": "파스스",
                                                    "imageUrl": null,
                                                    "imageCount": 0
                                                  },
                                                  {
                                                    "id": 2,
                                                    "title": "애민이",
                                                    "imageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2024-06-17-22-15-23.jpeg",
                                                    "imageCount": 1
                                                  },
                                                  {
                                                    "id": 3,
                                                    "title": "박물관 다녀온 날",
                                                    "imageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2024-01-20-15-03-57.jpeg",
                                                    "imageCount": 1
                                                  },
                                                  {
                                                    "id": 4,
                                                    "title": "점심",
                                                    "imageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2024-01-20-15-03-43.jpeg",
                                                    "imageCount": 1
                                                  }
                                                ],
                                                "pageable": {
                                                  "pageNumber": 0,
                                                  "pageSize": 20,
                                                  "sort": {
                                                    "empty": true,
                                                    "sorted": false,
                                                    "unsorted": true
                                                  },
                                                  "offset": 0,
                                                  "paged": true,
                                                  "unpaged": false
                                                },
                                                "totalElements": 5,
                                                "totalPages": 1,
                                                "last": true,
                                                "size": 20,
                                                "number": 0,
                                                "sort": {
                                                  "empty": true,
                                                  "sorted": false,
                                                  "unsorted": true
                                                },
                                                "numberOfElements": 5,
                                                "first": true,
                                                "empty": false
                                              }
                                            }
                                            """
                            )))})
    @Operation(summary = "메인 페이지 조회) 좋아요 순 게시글들", description = "로그인 없이 조회 가능합니다. 20개씩 게시글을 가져옵니다. 페이지번호 0부터 시작"
            ,tags = {"메인페이지APIs","공개APIs"}
    )
     Response<Page<PostListResponse>> LikedList(@ParameterObject @PageableDefault(
            page = 0,
            size = 20
    ) Pageable  pageable);
}
