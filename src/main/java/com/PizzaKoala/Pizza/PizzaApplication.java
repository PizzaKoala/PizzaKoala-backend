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

		//포스트 삭제시 like 엔티티 완전 삭제하기-삭제할떄 포스트에 있는 like count 는 삭제하지 말자.

		// 알람
		//지금은 서버 하나에 인스턴스 하나로 구현되었지만 서버가 여러개일 경우 맴버와 연결되 인스컨스를 찾아서 알람을 보내는 방식으로 구현해야한다.
		//프론트 세팅, src-> layouts -> alarm -> index.js
		// https://github.com/KimHyoJin/Simple-SNS/commit/f926f868b4a9f1461440159034bf0815307c4ba9

		// 검색 가져오기- react 에서 하는 방법은 밑에 주소 참고하기
		//https://chatgpt.com/share/ba2c52fc-b4a6-491e-afeb-712a1aa7c3f5


		//TODO refactoring, 빠트린거 있나 살펴 보기

		//프론트엔드측 : 로컬 스토리지에 존재하는 Access 토큰 삭제 및 서버측 로그아웃 경로로 Refresh 토큰 전송
		//더 보안을 원한다면-->api 바뀌면 새 로그인(웹만) https://www.y//outube.com/watch?v=Y__6n73AlJk&list=PLJkjrxxiBSFATow4HY2qr5wLvXM6Rg-BM&index=10

/*** //oauth2/authorization/google 로 접속하면 reauestRedirectFilter가 해당 요청을 받아서 구글로그인 페이지로 리다이렉트 해준다
 * 구글 로그인! 회원가입떄 사진을 올리는데 구글로그인떄는 구글계정에서 사진을 가져온다.
 *
 * https://www.youtube.com/watch?v=OddaHR7oIWE 리액트랑 연결하는 방식 참고하기
 * yml 파일에 oauth2서비스 변수 등록을 하면 됐지만 커스텀하게 정보를 가져오기위해서 클래스를 통해 직접 진행하는 방법인 client registration방식을 사용했다
 * 2개의 토큰(Refresh/Access)을 사용하는 경우 Refresh만 쿠키로 응답 받고 프론트측에서 다시 Refresh로 Access를 받도록 진행하셔도 됩니다.
 * aouth2- 대형서비스 개발 포럼은 보안규격에서 코드나 access토큰을 프론트에서 전송하는걸 지양한다.
 * 프론트에서 책임을 맡을떄는 구글에서 유저 정보를 받아서 백으로 넘기면 백이 그 정보를 통해 jwt를 만들어서 프론트에 넘겨준다.
 * 모든 책임을 백앤드나 프론트 한쪽에서 다 맡아야한다. 카카오 dev톡에선 네이티브앱일때는 프론트가 웹일떄는 백엔드가 책임을 맡는게 적합하고 한다.
 * 하이퍼링크를 백앤드로 보내서 모든 책임을 백앤드가 맡게하는게 가장 적합한 방식
 * https://www.youtube.com/watch?v=6XZ8oJOex_U
 * https://substantial-park-a17.notion.site/2-acacb442fdce473b91ebdd4e708d5214
 * https://www.youtube.com/watch?v=9g_iN6rLQcQ 이부분은어떻게 하지.. 안했음 아직
 * https://www.youtube.com/watch?v=Xiv5KFoEW4w
 *
 *
 */






/**
 * 포스트가 삭제될떄 - 포스트, 커멘트 는 softdelete 포스트의 liked는 그냥 삭제
 * 댓글 단건 삭제는 그냥 삭제.
 *
 * 클라이언트에게 요청이 들어오면 서블렛 컨테이더에 있는 필터에 있는 delegating filter proxy 가 요청을 가로채서 시큐리티 필터로 요청을 검증한다.
 */


	}

}
