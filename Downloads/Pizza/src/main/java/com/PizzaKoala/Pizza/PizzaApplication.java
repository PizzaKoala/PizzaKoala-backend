package com.PizzaKoala.Pizza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PizzaApplication {
//			1차 배포 계획- 회원가입, 로그인(기본, 구글), crud(post-s3), 피드(좋아요순, 최신순), 알림- 웹소켓, 좋아요, 댓글-1차

	public static void main(String[] args) {

		SpringApplication.run(PizzaApplication.class, args);
		//TODO :logout
		//redis
		//프론트엔드측 : 로컬 스토리지에 존재하는 Access 토큰 삭제 및 서버측 로그아웃 경로로 Refresh 토큰 전송
		//더 보안을 원한다면-->api 바뀌면 새 로그인(웹만) https://www.youtube.com/watch?v=Y__6n73AlJk&list=PLJkjrxxiBSFATow4HY2qr5wLvXM6Rg-BM&index=10


		//TODO 구글 로그인! 회원가입떄 사진을 올리는데 구글로그인떄는 구글계정에서 사진을 가져온다.
		//https://www.youtube.com/watch?v=OddaHR7oIWE 리액트랑 연결하는 방식 참고하기
		//yml 파일에 oauth2서비스 변수 등록을 하면 됐지만 커스텀하게 정보를 가져오기위해서 클래스를 통해 직접 진행하는 방법인 client registration방식을 사용했다
		//2개의 토큰(Refresh/Access)을 사용하는 경우 Refresh만 쿠키로 응답 받고 프론트측에서 다시 Refresh로 Access를 받도록 진행하셔도 됩니다.
		// !, 레포지토리에 올리기
		//aouth2- 대형서비스 개발 포럼은 보안규격에서 코드나 access토큰을 프론트에서 전송하는걸 지양한다.
		//모든 책임을 백앤드나 프론트 한쪽에서 다 맡아야한다. 카카오 dev톡에선 네이티브앱일때는 프론트가 웹일떄는 백엔드가 책임을 맡는게 적합하고 한다.
		//하이퍼링크를 백앤드로 보내서 모든 책임을 백앤드가 맡게하는게 가장 적합한 방식
		//https://www.youtube.com/watch?v=6XZ8oJOex_U
		//https://substantial-park-a17.notion.site/2-acacb442fdce473b91ebdd4e708d5214
		//https://www.youtube.com/watch?v=9g_iN6rLQcQ 이부분은어떻게 하지.. 안했음 아직
		//https://www.youtube.com/watch?v=Xiv5KFoEW4w

		// TODO 알람도 해야햇!!!!
		// TODO unlike도 해야함
		// TODO calender도 떠야해 :(
		//TODO refactoring, 빠트린거 있나 살펴 보기


		// 월 - 로그아웃, 토큰-리프레쉬, 언라이크, 댓글 삭제
		// 화 - 캘린더, 검색 가져오기.
		// 수
		//TODO
		//포스트가 삭제될떄 - 포스트, 커멘트 는 softdelete 포스트의 liked는 그냥 삭제
		//댓글 단건 삭제는 그냥 삭제.

		//클라이언트에게 요청이 들어오면 서블렛 컨테이더에 있는 필터에 있는 delegating filter proxy 가 요청을 가로채서 시큐리티 필터로 요청을 검증한다.

	}

}
