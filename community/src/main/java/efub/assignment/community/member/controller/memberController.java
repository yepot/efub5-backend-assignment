package efub.assignment.community.member.controller;

import efub.assignment.community.member.dto.request.CreateMemberRequestDto;
import efub.assignment.community.member.dto.response.CreateMemberResponseDto;
import efub.assignment.community.member.dto.response.MemberResponseDto;
import efub.assignment.community.member.dto.request.UpdateMemberRequestDto;
import efub.assignment.community.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class memberController {

    private final MemberService memberService;

    //멤버 조회: GET /members/{memberId}
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable("memberId") Long memberId){
        MemberResponseDto responseDto= memberService.getMember(memberId);
        return ResponseEntity.ok(responseDto);
    }

    //멤버 생성: POST /members
    @PostMapping
    public ResponseEntity<CreateMemberResponseDto> createMember(@RequestBody @Valid CreateMemberRequestDto requestDto){
        CreateMemberResponseDto responseDto= memberService.createMember(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    ///여기서 부터 다시 !! Pathvariable
    //멤버 수정: PATCH /members/profile/{memberId}
    @PatchMapping("/profile/{memberId}")
    public ResponseEntity<MemberResponseDto> updateMember(@PathVariable("memberId") Long memberId, @RequestBody @Valid UpdateMemberRequestDto requestDto){
        MemberResponseDto responseDto= memberService.updateMember(memberId, requestDto);
        return ResponseEntity.ok(responseDto);
    }


    //회원 탈퇴(논리적 삭제): PATCH /members/{memberId}
    @PatchMapping("/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable("memberId") Long memberId){
        memberService.deleteMember(memberId);
        return ResponseEntity.ok("message:성공적으로 탈퇴되었습니다.");
    }
}
