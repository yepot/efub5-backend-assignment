package efub.assignment.community.notification.service;

import efub.assignment.community.comment.domain.Comment;
import efub.assignment.community.comment.repository.CommentRepository;
import efub.assignment.community.notification.domain.Notification;
import efub.assignment.community.notification.domain.NotificationType;
import efub.assignment.community.notification.dto.response.NotificationResponse;
import efub.assignment.community.notification.repository.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@org.springframework.transaction.annotation.Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public List<NotificationResponse> getNotifications(Long memberId) {
        List<Notification> notifications = notificationRepository.findAllByMember_MemberIdOrderByNotifiedAtDesc(memberId);

        return notifications.stream().map(notification -> {
            if (notification.getType() == NotificationType.COMMENT) {
                Comment comment = commentRepository.findById(notification.getReferenceId())
                        .orElseThrow(() -> new EntityNotFoundException("댓글 없음"));
                String boardName = comment.getPost().getBoard().getBoardName();
                String content = comment.getContent();
                return new NotificationResponse(
                        "댓글",
                        boardName + " 게시판, 새로운 댓글이 달렸어요: " + content,
                        notification.getNotifiedAt()
                );
            } else {
                return new NotificationResponse(
                        "쪽지방",
                        "새로운 쪽지방이 생겼어요",
                        notification.getNotifiedAt()
                );
            }
        }).toList();
    }

}
