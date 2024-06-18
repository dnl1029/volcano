package hobby.volcano.controller;

import hobby.volcano.common.CommonErrorCode;
import hobby.volcano.common.RestApiException;
import hobby.volcano.dto.MemberEditRequestDto;
import hobby.volcano.dto.UserIdRequestDto;
import hobby.volcano.entity.Member;
import hobby.volcano.service.JwtIssueService;
import hobby.volcano.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final JwtIssueService jwtIssueService;


    @Operation(summary = "login and get gwt token api", description = "최초 로그인 및 jwt 토큰만료시 로그인화면에서 사용하는 api. 성공시 jwt 토큰 return")
    @PostMapping("login")
    public String getMemberIsPresent(@RequestBody UserIdRequestDto userIdRequestDto) {
        String jwt = jwtIssueService.createJwt(userIdRequestDto);
        return jwt;
    }

    @Operation(summary = "get userName api", description = "userId를 입력시, userName 리턴해주는 api.")
    @PostMapping("getMember/name")
    public ResponseEntity<String> getMemberName(@RequestBody UserIdRequestDto userIdRequestDto) {
        Optional<Member> member = memberService.getMember(userIdRequestDto);
        if (member.isPresent() && member.get().getUserName() == null) {
            return ResponseEntity.status(HttpStatus.OK).body(userIdRequestDto.getUserId() + "는 등록된 이름이 없습니다.");
        }
        else if (member.isPresent() && member.get().getUserName() != null) {
            return ResponseEntity.status(HttpStatus.OK).body(userIdRequestDto.getUserId() + "는 등록된 이름이 있습니다.");
        }
        else {
            throw new RestApiException(CommonErrorCode.USER_NOT_FOUND);
        }
    }

    @Operation(summary = "get imageFileName api", description = "userId를 입력시, imageFileName 리턴해주는 api.")
    @PostMapping("getMember/image")
    public ResponseEntity<String> getMemberImage(@RequestBody UserIdRequestDto userIdRequestDto) {
        Optional<Member> member = memberService.getMember(userIdRequestDto);
        if (member.isPresent() && member.get().getImageFileName() == null) {
            return ResponseEntity.status(HttpStatus.OK).body(userIdRequestDto.getUserId() + "는 등록된 프로필 이미지가 없습니다.");
        }
        else if (member.isPresent() && member.get().getImageFileName() != null) {
            return ResponseEntity.status(HttpStatus.OK).body(userIdRequestDto.getUserId() + "는 등록된 프로필 이미지가 있습니다.");
        }
        else {
            throw new RestApiException(CommonErrorCode.USER_NOT_FOUND);
        }
    }

    @Operation(summary = "edit member api", description = "userId, userName, imageFileName, statusYn, role에 대해 수정할수 있는 api")
    @PostMapping("member/edit/all")
    public Member editMemberAll(@RequestBody MemberEditRequestDto memberEditRequestDto) {
        Member member = memberService.editMemberAll(memberEditRequestDto);
        return member;
    }

    @Operation(summary = "create member api", description = "설정화면에서 member를 신규로 추가하는 api.")
    @PostMapping("member/create")
    public Member createMember(@RequestBody MemberEditRequestDto memberEditRequestDto) {
        Member member = memberService.createMember(memberEditRequestDto);
        return member;
    }

    @Operation(summary = "get all members api", description = "설정화면에서 전체 멤버리스트를 조회하는 api.")
    @GetMapping("member/getAll")
    public List<Member> getMemberAll() {
        List<Member> memberAll = memberService.getMemberAll();
        return memberAll;
    }

}
