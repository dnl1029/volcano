package hobby.volcano.service;

import hobby.volcano.common.CommonErrorCode;
import hobby.volcano.common.CustomEnum;
import hobby.volcano.common.RestApiException;
import hobby.volcano.dto.MemberEditRequestDto;
import hobby.volcano.dto.UserIdRequestDto;
import hobby.volcano.entity.Member;
import hobby.volcano.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static hobby.volcano.common.CommonErrorCode.INVALID_PARAMETER;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Optional<Member> getMember(UserIdRequestDto userIdRequestDto) {
        Optional<Member> member = memberRepository.findById(userIdRequestDto.getUserId())
                .filter(f->f.getStatusYn().equals("Y"));
        return member;
    }

    public List<Member> getMemberAll() {
        List<Member> allMember = memberRepository.findAll();
        List<Member> sortedMembers = allMember.stream().sorted(Comparator.comparing(Member::getUserId)).collect(Collectors.toList());
        return sortedMembers;
    }

    public Member editMemberAll(MemberEditRequestDto memberEditRequestDto) {
        UserIdRequestDto userIdRequestDto = UserIdRequestDto.builder()
                .userId(memberEditRequestDto.getUserId())
                .build();

        Member member = getMember(userIdRequestDto)
                .orElseThrow(()-> new RestApiException(CommonErrorCode.USER_NOT_FOUND));
        Member editedMember = Member.builder()
                .userId(member.getUserId())
                .userName(memberEditRequestDto.getUserName() != null ? memberEditRequestDto.getUserName() : member.getUserName())
                .imageFileName(memberEditRequestDto.getImageFileName() != null ? memberEditRequestDto.getImageFileName() : member.getImageFileName())
                .statusYn(memberEditRequestDto.getStatusYn() != null ? memberEditRequestDto.getStatusYn() : member.getStatusYn())
                .role(memberEditRequestDto.getRole() != null ? memberEditRequestDto.getRole() : member.getRole())
                .crtTm(member.getCrtTm())
                .crtNm(member.getCrtNm())
                .chgNm(memberEditRequestDto.getUserName() != null ? memberEditRequestDto.getUserName() : CustomEnum.SYSTEM.getContent())
                .chgTm(LocalDateTime.now())
                .build();

        Member savedMember = memberRepository.save(editedMember);
        return savedMember;
    }

    public Member createMember(MemberEditRequestDto memberEditRequestDto) {
        Optional<Member> member = memberRepository.findById(memberEditRequestDto.getUserId());
        if(member.isPresent()) {
            throw new RestApiException(INVALID_PARAMETER);
        }
        Member tempMember = Member.builder()
                .userId(memberEditRequestDto.getUserId())
                .userName(memberEditRequestDto.getUserName())
                .imageFileName(memberEditRequestDto.getImageFileName())
                .statusYn(memberEditRequestDto.getStatusYn())
                .role(memberEditRequestDto.getRole())
                .crtTm(LocalDateTime.now())
                .crtNm(memberEditRequestDto.getUserName() != null ? memberEditRequestDto.getUserName() : CustomEnum.SYSTEM.getContent())
                .chgTm(LocalDateTime.now())
                .chgNm(memberEditRequestDto.getUserName() != null ? memberEditRequestDto.getUserName() : CustomEnum.SYSTEM.getContent())
                .build();
        Member savedMember = memberRepository.save(tempMember);
        return savedMember;
    }


}
