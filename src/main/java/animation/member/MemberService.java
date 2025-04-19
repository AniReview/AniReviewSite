package animation.member;

import animation.character.Character;
import animation.character.CharacterRepository;
import animation.loginUtils.JwtProvider;
import animation.loginUtils.SecurityUtils;
import animation.member.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final CharacterRepository characterRepository;
    private final JwtProvider jwtProvider;

    // 로그인 로직 예외처리
    private Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId).orElseThrow(
                () -> new NoSuchElementException("회원을 찾을 수 없습니다."));
    }

    public MemberCreateResponse create(String profileImageUrl,MemberCreateRequest memberCreateRequest) {

        // 캐릭터 찾기 (charId가 null이면 character도 null)
        Character character = null;

        if (memberCreateRequest.charId() != null) {
            character = characterRepository.findById(
                    memberCreateRequest.charId()).orElseThrow(() ->
                    new NoSuchElementException("존재하지 않는 캐릭터 LoginId" + memberCreateRequest.charId()));
        }


        // 요청받은 비밀번호 해쉬화 하기
        String hashPassword = SecurityUtils.sha256EncryptHex2(memberCreateRequest.password());

        // 멤버 만들기
        Member member = new Member(
                memberCreateRequest.loginId(),
                hashPassword,
                memberCreateRequest.nickName(),
                character,
                memberCreateRequest.birth(),
                profileImageUrl);

        memberRepository.save(member);

        if (character != null) {
            characterRepository.increaseFavoriteCountById(character.getId());
        }


        // dto로 감싸서 return
        return new MemberCreateResponse(
                member.getId(),
                member.getLoginId(),
                member.getCharacter() != null ? member.getCharacter().getName() : null,
                member.getBirth(),
                member.getImageUrl());
    }

    public MemberLoginResponse login(MemberLoginRequest loginRequest) {
        Member member = findByLoginId(loginRequest.LoginId());

        member.findByPassword(loginRequest.password());

        return new MemberLoginResponse(jwtProvider.createToken(loginRequest.LoginId()));
    }


}
