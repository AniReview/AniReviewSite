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

        // 요청받은 캐릭터 id로 캐릭터 찾기 (최애캐)
        Character character = characterRepository.findById(
                memberCreateRequest.charId()).orElseThrow(() ->
                new NoSuchElementException("존재하지 않는 캐릭터 id" + memberCreateRequest.charId()));

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

        characterRepository.increaseFavoriteCountById(character.getId());


        // dto로 감싸서 return
        return new MemberCreateResponse(
                member.getId(),
                member.getLoginId(),
                member.getCharacter().getName(),
                member.getBirth(),
                member.getImageUrl());
    }
}
