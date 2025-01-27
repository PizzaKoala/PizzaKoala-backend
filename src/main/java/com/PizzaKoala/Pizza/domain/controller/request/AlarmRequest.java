package com.PizzaKoala.Pizza.domain.controller.request;

import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.global.entity.AlarmType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class AlarmRequest {
    private AlarmType alarmType;   // 알림 타입
    private Long receiverId;       // 알림을 받을 사용자 ID
    private Member sender;         // 알림을 보낸 사용자 정보
    private Long postId;           // 포스트 ID (좋아요, 댓글 알림 시 필요)
    private Long commentId;        // 댓글 ID (댓글 알림 시 필요)

    public AlarmRequest(AlarmType alarmType, Long receiverId, Member sender, Long postId, Long commentId) {
        this.alarmType = alarmType;
        this.receiverId = receiverId;
        this.sender = sender;
        this.postId = postId;
        this.commentId = commentId;
    }
}
