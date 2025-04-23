package animation.member;

import animation.character.Character;
import animation.character.CharacterRepository;
import animation.loginUtils.JwtProvider;
import animation.loginUtils.SecurityUtils;
import animation.member.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final CharacterRepository characterRepository;
    private final JwtProvider jwtProvider;
    private final MemberQueryRepository memberQueryRepository;

    // 중복코드 함수로 빼기
    @Transactional
    public void increaseFavoriteCountIfExists(Character character) {
        if (character != null) {
            characterRepository.increaseFavoriteCountById(character.getId());
        }
    }

    // 중복코드 함수로 빼기
    @Transactional
    public void decreaseFavoriteCountIfExists(Character character) {
        if (character != null) {
            characterRepository.decreaseFavoriteCountById(character.getId());
        }
    }

    // 중복코드 함수로 빼기
    private void validateNotDeleted(Member member) {
        if (member.isDeleted()) {
            throw new RuntimeException("삭제된 회원입니다.");
        }
    }

    // 로그인 로직 예외처리
    private Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId).orElseThrow(
                () -> new NoSuchElementException("회원을 찾을 수 없습니다."));
    }

    public MemberResponse create(String profileImageUrl, MemberCreateRequest memberCreateRequest) {

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
                profileImageUrl,
                memberCreateRequest.introduce()
                );

        memberRepository.save(member);

        increaseFavoriteCountIfExists(character);

        // dto로 감싸서 return
        return new MemberResponse(
                member.getId(),
                member.getLoginId(),
                member.getCharacter() != null ? member.getCharacter().getName() : null,
                member.getBirth(),
                member.getImageUrl(),
                member.getIntroduce());
    }

    public MemberLoginResponse login(MemberLoginRequest loginRequest) {
        Member member = findByLoginId(loginRequest.LoginId());

        member.findByPassword(loginRequest.password());

        return new MemberLoginResponse(jwtProvider.createToken(loginRequest.LoginId()));
    }

    @Transactional
    public MemberDeleteResponse deleteMember(String loginId) {
        Member member = findByLoginId(loginId);

        // 캐릭터 FavoriteCount 감소
        decreaseFavoriteCountIfExists(member.getCharacter());

        validateNotDeleted(member);

        member.deleteMember();

        return new MemberDeleteResponse(member.getLoginId(), member.isDeleted());
    }

    @Transactional
    public MemberResponse myCharUpdate(Long charId,String loginId) {

        Member member = findByLoginId(loginId);

        decreaseFavoriteCountIfExists(member.getCharacter());

        Character newCharacter = characterRepository.findById(charId).orElseThrow(() ->
                new NoSuchElementException("존재하지 않는 캐릭터 id"));

        member.UpdateMyChar(newCharacter);

        increaseFavoriteCountIfExists(newCharacter);

        return new MemberResponse(
                member.getId(),
                member.getLoginId(),
                member.getCharacter().getName(),
                member.getBirth(),
                member.getImageUrl(),
                member.getIntroduce());

    }

    @Transactional
    public MemberListResponse findAll(Pageable pageable, String keyWord) {
        List<MemberSimpleDto> list = memberQueryRepository.findAll(pageable, keyWord);

        return new MemberListResponse(list);
    }

    public MemberDetailResponse findByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new NoSuchElementException("존재하지 않는 memberId : " + memberId));

        if (member.isDeleted()) {
            throw new RuntimeException("이미 삭제된 회원입니다.");
        }

        String myChar = "";
        if (member.getCharacter() != null) {
            myChar = member.getCharacter().getName();
        }

        return new MemberDetailResponse(
                member.getId(),
                member.getNickName(),
                myChar,
                member.getBirth(),
                member.getImageUrl(),
                member.getFriendCount()
                );
    }

    @Transactional
    public MemberResponse profileUpdate(String loginId,MemberProfileUpdateRequest request) {
        Member member = findByLoginId(loginId);

        validateNotDeleted(member);

        member.updateProfile(request.nickName(), request.birth(), request.introduce());

        return new MemberResponse(
                member.getId(),
                member.getLoginId(),
                member.getCharacter() != null ? member.getCharacter().getName() : null,
                member.getBirth(),
                member.getImageUrl(),
                member.getIntroduce());
    }


}
