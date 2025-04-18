package animation.member;

import animation.character.Character;
import animation.character.CharacterRepository;
import animation.loginUtils.SecurityUtils;
import animation.member.dto.MemberCreateRequest;
import animation.member.dto.MemberCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final CharacterRepository characterRepository;

    public MemberCreateResponse create(MemberCreateRequest memberCreateRequest) {

        // 캐릭터 찾기 (charId가 null이면 character도 null)
        Character character = null;

        if (memberCreateRequest.charId() != null) {
            character = characterRepository.findById(
                    memberCreateRequest.charId()).orElseThrow(() ->
                    new NoSuchElementException("존재하지 않는 캐릭터 id" + memberCreateRequest.charId()));
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
                memberCreateRequest.imageUrl());

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
}
