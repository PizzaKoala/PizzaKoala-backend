package com.PizzaKoala.Pizza.service;

import com.PizzaKoala.Pizza.domain.Repository.MemberRepository;
import com.PizzaKoala.Pizza.domain.Repository.PostRepository;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.entity.Post;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import com.PizzaKoala.Pizza.domain.service.PostService;
import com.PizzaKoala.Pizza.fixture.MemberEntityFixture;
import com.PizzaKoala.Pizza.fixture.PostEntityFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@SpringBootTest
public class PostServiceTest {
    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private MemberRepository memberRepository;




    @Test
    void 포스트작성이_성공한_경우(){
        String title = "title";
        String email = "email";
        String desc = "desc";
        // Create a list of mock files
        List<MultipartFile> files = Arrays.asList(
                new MockMultipartFile("files", "meepyday1.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 1".getBytes()),
                new MockMultipartFile("files", "meepyday2.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 2".getBytes()),
                new MockMultipartFile("files", "meepyday3.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 3".getBytes()),
                new MockMultipartFile("files", "meepyday4.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 4".getBytes()),
                new MockMultipartFile("files", "meepyday5.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 5".getBytes())
        );

        //mock
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(mock(Member.class)));
        when(postRepository.save(any())).thenReturn(mock(Post.class));
        Assertions.assertDoesNotThrow(()->postService.create(email,title,desc));
    }

    @Test
    void 포스트작성시_요청한_유저가_존재하지않는_경우(){
        String title = "title";
        String email = "email";
        String desc = "desc";
        // Create a list of mock files
        List<MultipartFile> files = Arrays.asList(
                new MockMultipartFile("files", "meepyday1.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 1".getBytes()),
                new MockMultipartFile("files", "meepyday2.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 2".getBytes()),
                new MockMultipartFile("files", "meepyday3.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 3".getBytes()),
                new MockMultipartFile("files", "meepyday4.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 4".getBytes()),
                new MockMultipartFile("files", "meepyday5.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 5".getBytes())
        );

        //mock
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(postRepository.save(any())).thenReturn(mock(Post.class));
        PizzaAppException e = Assertions.assertThrows(PizzaAppException.class, () -> postService.create(title, email, desc));
        Assertions.assertEquals(e.getErrorCode(), ErrorCode.MEMBER_NOT_FOUND);

    }

    @Test
    void 포스트수정이_성공한_경우(){
        String title = "title";
        String email = "email";
        String desc = "desc";
        Long postId=1L;
        Long id=1L;
        // Create a list of mock files
        List<MultipartFile> files = Arrays.asList(
                new MockMultipartFile("files", "meepyday1.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 1".getBytes()),
                new MockMultipartFile("files", "meepyday2.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 2".getBytes()),
                new MockMultipartFile("files", "meepyday3.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 3".getBytes()),
                new MockMultipartFile("files", "meepyday4.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 4".getBytes()),
                new MockMultipartFile("files", "meepyday5.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 5".getBytes())
        );

        Post mockPostEntity=PostEntityFixture.get(email, postId,id);
        Member member = mockPostEntity.getMember();

        //mock
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPostEntity));
        when(postRepository.saveAndFlush(any())).thenReturn(mockPostEntity);

        Assertions.assertDoesNotThrow(()->postService.modify(email,title,desc,postId));
    }

    @Test
    void 포스트수정시_포스트가_존재하지않는_경우(){
        String title = "title";
        String email = "email";
        String desc = "desc";
        Long postId=1L;
        Long id=1L;

        // Create a list of mock files
        List<MultipartFile> files = Arrays.asList(
                new MockMultipartFile("files", "meepyday1.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 1".getBytes()),
                new MockMultipartFile("files", "meepyday2.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 2".getBytes()),
                new MockMultipartFile("files", "meepyday3.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 3".getBytes()),
                new MockMultipartFile("files", "meepyday4.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 4".getBytes()),
                new MockMultipartFile("files", "meepyday5.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 5".getBytes())
        );

        Post mockPostEntity=PostEntityFixture.get(email, postId,id);
        Member member = mockPostEntity.getMember();

        //mock
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        PizzaAppException e=Assertions.assertThrows(PizzaAppException.class, () -> postService.modify(email, title, desc, postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }
    @Test
    void 포스트수정시_권한이_없는_경우(){
        String title = "title";
        String email = "email";
        String desc = "desc";
        Long id=1L;
        Long postId=1L;
        // Create a list of mock files
        List<MultipartFile> files = Arrays.asList(
                new MockMultipartFile("files", "meepyday1.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 1".getBytes()),
                new MockMultipartFile("files", "meepyday2.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 2".getBytes()),
                new MockMultipartFile("files", "meepyday3.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 3".getBytes()),
                new MockMultipartFile("files", "meepyday4.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 4".getBytes()),
                new MockMultipartFile("files", "meepyday5.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 5".getBytes())
        );

        Post mockPostEntity=PostEntityFixture.get(email, postId,id);
        Member writer = MemberEntityFixture.getSignUp("Meep", "meep@kakao.com", "pw",2L);

        //mock
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(writer));
        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPostEntity));

        PizzaAppException e=Assertions.assertThrows(PizzaAppException.class, () -> postService.modify(email, title, desc, postId));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 포스트삭제가_성공한_경우(){
        String email = "email";
        Long postId=1L;


        Post mockPostEntity=PostEntityFixture.get(email, postId,1L);
        Member member = mockPostEntity.getMember();

        //mock
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPostEntity));

        Assertions.assertDoesNotThrow(()->postService.delete(email,postId));
    }

    @Test
    void 포스트삭제시_포스트가_존재하지않는_경우(){
        String email = "email";
        Long postId=1L;


        Post mockPostEntity=PostEntityFixture.get(email, postId,1L);
        Member member = mockPostEntity.getMember();

        //mock
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        PizzaAppException e=Assertions.assertThrows(PizzaAppException.class, () -> postService.delete(email,postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }
    @Test
    void 포스트삭제시_권한이_없는_경우(){
        String email = "email";
        Long postId=1L;


        Post mockPostEntity=PostEntityFixture.get(email, postId,1L);
        Member writer = MemberEntityFixture.getSignUp("Meep", "meep@kakao.com", "pw",2L);

        //mock
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(writer));
        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPostEntity));

        PizzaAppException e=Assertions.assertThrows(PizzaAppException.class, () -> postService.delete(email, postId));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 내피드목록요청이_성공한_경우() {
        org.springframework.data.domain.Pageable pageable = mock(Pageable.class);
        Member member = mock(Member.class);
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        when(postRepository.findAllByMemberAndNotDeleted(member, pageable)).thenReturn(Page.empty());
        Assertions.assertDoesNotThrow(() -> postService.my("", pageable));
    }
}
