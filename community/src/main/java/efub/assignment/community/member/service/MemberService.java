package efub.assignment.community.member.service;

import efub.assignment.community.global.exception.dto.CommunityException;
import efub.assignment.community.global.exception.dto.ExceptionCode;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.domain.MemberStatus;
import efub.assignment.community.member.dto.request.CreateMemberRequestDto;
import efub.assignment.community.member.dto.response.CreateMemberResponseDto;
import efub.assignment.community.member.dto.response.MemberResponseDto;
import efub.assignment.community.member.dto.request.UpdateMemberRequestDto;
import efub.assignment.community.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    //멤버 단건 조회: GET member/{memberId}
    @Transactional(readOnly=true)
    public MemberResponseDto getMember(Long memberId){
        Member member= memberRepository.findByMemberId(memberId)
                .orElseThrow(()->new IllegalArgumentException("해당 멤버를 찾을 수 없습니다."));
        return MemberResponseDto.from(member);
    }

    //멤버 생성
    @Transactional
    public CreateMemberResponseDto createMember(CreateMemberRequestDto requestDto){

        // 이메일 형식 유효성 검사
        if (!isValidEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다: " + requestDto.getEmail());
        }
        Member member=requestDto.toEntity();
        Member savedMember= memberRepository.save(member);

        return CreateMemberResponseDto.from(savedMember);
    }

    private boolean isValidEmail(String email) {
        // 간단한 이메일 정규식
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }

    //멤버 수정
    @Transactional
    public MemberResponseDto updateMember(Long memberId, UpdateMemberRequestDto requestDto){
        Member member= memberRepository.findByMemberId(memberId)
                .orElseThrow(()->new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        member.updateMember(requestDto.getEmail(), requestDto.getPassword(), requestDto.getNickname(), requestDto.getUniversity(), requestDto.getStudentId());
        Member updatedMember= memberRepository.save(member);
        return MemberResponseDto.from(updatedMember);
    }

    //멤버 논리적 삭제 (status 변경)
    @Transactional
    public void deleteMember(Long memberId){
        Member member= memberRepository.findByMemberId(memberId)
                .orElseThrow(()->new IllegalArgumentException("삭제할 유저를 찾을 수 없습니다."));
        member.changeStatus(MemberStatus.DEACTIVATED);
        memberRepository.save(member);
    }

    @Transactional(readOnly=true)
    public Member findByMemberId(Long memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(()-> new CommunityException(ExceptionCode.ACCOUNT_NOT_FOUND));
    }

    @Transactional(readOnly=true)
    public Member findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
                .orElseThrow(()-> new CommunityException(ExceptionCode.ACCOUNT_NOT_FOUND));    }
}


