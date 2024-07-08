package com.PizzaKoala.Pizza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PizzaApplication {
//			1차 배포 계획- 회원가입(사진아직 안널음), 로그인(기본, 구글), crud(post-s3), 피드(좋아요순, 최신순), 알림- 웹소켓, 좋아요, 댓글-1차

	public static void main(String[] args) {

		SpringApplication.run(PizzaApplication.class, args);
		//TODO fresh랑 acess토큰,  로그아웃 >:(
		//redis
		//프론트엔드측 : 로컬 스토리지에 존재하는 Access 토큰 삭제 및 서버측 로그아웃 경로로 Refresh 토큰 전송
		//더 보안을 원한다면-->api 바뀌면 새 로그인(웹만) https://www.youtube.com/watch?v=Y__6n73AlJk&list=PLJkjrxxiBSFATow4HY2qr5wLvXM6Rg-BM&index=10


		//TODO 월요일까지 구글 로그인!!, 레포지토리에 올리기
		//aouth2- 대형서비스 개발 포럼은 보안규격에서 코드나 access토큰을 프론트에서 전송하는걸 지양한다.
		//모든 책임을 백앤드나 프론트 한쪽에서 다 맡아야한다. 카카오 dev톡에선 네이티브앱일때는 프론트가 웹일떄는 백엔드가 책임을 맡는게 적합하고 한다.
		//하이퍼링크를 백앤드로 보내서 모든 책임을 백앤드가 맡게하는게 가장 적합한 방식
		//https://www.youtube.com/watch?v=6XZ8oJOex_U
		//https://substantial-park-a17.notion.site/2-acacb442fdce473b91ebdd4e708d5214

		//TODO s3 사진-프로필, 포스트 :(
		//TODO git 에 올리기
		// TODO 알람도 해야햇!!!!
		// TODO unlike도 해야함

		//TODO 화요일- 피드- 좋아요순
		//TODO comment-사진도 가져와야함, 삭제하기
		// TODO calender도 떠야해 :(

		//TODO refactoring, 빠트린거 있나 살펴 보기

		//일- s3 -profile, post, comment, 피드-좋아요순
		// 월 - 로그아웃, 토큰-리프레쉬, 언라이크, 댓글 삭제
		// 화 - 캘린더, 검색 가져오기.
		// 수
		//TODO

		//클라이언트에게 요청이 들어오면 서블렛 컨테이더에 있는 필터에 있는 delegating filter proxy 가 요청을 가로채서 시큐리티 필터로 요청을 검증한다.

	}

}
