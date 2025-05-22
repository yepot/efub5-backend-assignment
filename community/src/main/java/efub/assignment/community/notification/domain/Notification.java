package efub.assignment.community.notification.domain;

import efub.assignment.community.member.domain.Member;
import efub.assignment.community.messageRooom.domain.MessageRoom;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    // 알람 종류 (댓글 또는 쪽지방)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    // 참조 ID (댓글 ID 또는 쪽지방 ID)
    private Long referenceId;

    // 알림 시각
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime notifiedAt;

    // 알림을 받은 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Notification(NotificationType type, Long referenceId, LocalDateTime notifiedAt, Member member){
        this.type=type;
        this.referenceId=referenceId;
        this.notifiedAt=notifiedAt;
        this.member=member;

    }

}
