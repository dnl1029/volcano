package hobby.volcano.controller;

import hobby.volcano.common.ApiResponse;
import hobby.volcano.common.CommonErrorCode;
import hobby.volcano.common.CustomEnum;
import hobby.volcano.common.RestApiException;
import hobby.volcano.dto.MemberEditRequestDto;
import hobby.volcano.dto.MyProfileResponseDto;
import hobby.volcano.dto.UserIdRequestDto;
import hobby.volcano.entity.Member;
import hobby.volcano.service.JwtIssueService;
import hobby.volcano.service.MemberService;
import hobby.volcano.service.ScoreService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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
    private final ScoreService scoreService;


    @Operation(summary = "login and get gwt token api", description = "최초 로그인 및 jwt 토큰만료시 로그인화면에서 사용하는 api. 성공시 jwt 토큰 return")
    @PostMapping("login")
    public ApiResponse getMemberIsPresent(@RequestBody UserIdRequestDto userIdRequestDto) {
        String jwt = jwtIssueService.createJwt(userIdRequestDto);
        return ApiResponse.builder().code("200").message(jwt).build();
    }

    @Operation(summary = "get my name api", description = "jwt토큰에서 userId를 읽어, 내 userName 리턴해주는 api.")
    @GetMapping("get/myName")
    public ApiResponse getMyName() {
        String myUserId = MDC.get(CustomEnum.USER_ID.getContent());
        Optional<Member> member = memberService.getMember(UserIdRequestDto.builder().userId(Integer.valueOf(myUserId)).build());
        if (member.isPresent() && member.get().getUserName() == null) {
            return ApiResponse.builder().code("200").message(myUserId + "는 등록된 이름이 없습니다.").build();
        }
        else if (member.isPresent() && member.get().getUserName() != null) {
            return ApiResponse.builder().code("200").message(member.get().getUserName()).build();
        }
        else {
            throw new RestApiException(CommonErrorCode.USER_NOT_FOUND);
        }
    }

    @Operation(summary = "미사용 api / get userName api", description = "userId를 입력시, userName 리턴해주는 api.")
    @PostMapping("getMember/name")
    public ApiResponse getMemberName(@RequestBody UserIdRequestDto userIdRequestDto) {
        Optional<Member> member = memberService.getMember(userIdRequestDto);
        if (member.isPresent() && member.get().getUserName() == null) {
            return ApiResponse.builder().code("200").message(userIdRequestDto.getUserId() + "는 등록된 이름이 없습니다.").build();
        }
        else if (member.isPresent() && member.get().getUserName() != null) {
            return ApiResponse.builder().code("200").message(member.get().getUserName()).build();
        }
        else {
            throw new RestApiException(CommonErrorCode.USER_NOT_FOUND);
        }
    }

    @Operation(summary = "get my imageFileName api", description = "jwt토큰에서 userId를 읽어, imageFileName 리턴해주는 api.")
    @GetMapping("get/myImage")
    public ApiResponse getMyImage() {
        String myUserId = MDC.get(CustomEnum.USER_ID.getContent());
        Optional<Member> member = memberService.getMember(UserIdRequestDto.builder().userId(Integer.valueOf(myUserId)).build());
        if (member.isPresent() && member.get().getImageFileName() == null) {
            return ApiResponse.builder().code("200").message(myUserId + "는 등록된 프로필 이미지가 없습니다.").build();
        }
        else if (member.isPresent() && member.get().getImageFileName() != null) {
            return ApiResponse.builder().code("200").message(member.get().getImageFileName()).build();
        }
        else {
            throw new RestApiException(CommonErrorCode.USER_NOT_FOUND);
        }
    }

    @Operation(summary = "미사용 api / get imageFileName api", description = "userId를 입력시, imageFileName 리턴해주는 api.")
    @PostMapping("getMember/image")
    public ApiResponse getMemberImage(@RequestBody UserIdRequestDto userIdRequestDto) {
        String myUserId = MDC.get(CustomEnum.USER_ID.getContent());
        Optional<Member> member = memberService.getMember(userIdRequestDto);
        if (member.isPresent() && member.get().getImageFileName() == null) {
            return ApiResponse.builder().code("200").message(userIdRequestDto.getUserId() + "는 등록된 프로필 이미지가 없습니다.").build();
        }
        else if (member.isPresent() && member.get().getImageFileName() != null) {
            return ApiResponse.builder().code("200").message(member.get().getImageFileName()).build();
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

    @Operation(summary = "delete member api", description = "설정화면에서 관리자 권한으로 멤버를 삭제하는 api.")
    @PostMapping("member/delete")
    public ApiResponse deleteMember(@RequestBody UserIdRequestDto userIdRequestDto) {
        boolean res = memberService.deleteMember(userIdRequestDto);
        if(res) {
            return ApiResponse.builder().code("200").message("member가 정상적으로 삭제되었습니다.").build();
        }
        else {
            return ApiResponse.builder().code("400").message("member가 삭제되지 않았습니다.").build();
        }
    }

    @Operation(summary = "get my profile api", description = "jwt토큰에서 userId를 읽어, 내 프로필 데이터 return해주는 api.")
    @GetMapping("get/myProfile")
    public MyProfileResponseDto getMyProfile() {
        String myUserId = MDC.get(CustomEnum.USER_ID.getContent());
        MyProfileResponseDto myProfileResponseDto = scoreService.getMyProfile(UserIdRequestDto.builder().userId(Integer.valueOf(myUserId)).build());
        return myProfileResponseDto;
    }


}
