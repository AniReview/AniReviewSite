package animation.loveAni;

import animation.anime.Anime;
import animation.anime.AnimeRepository;
import animation.loveAni.dto.DeleteLoveAniDataResponse;
import animation.loveAni.dto.DeleteLoveAniResponse;
import animation.loveAni.dto.LoveRequest;
import animation.loveAni.dto.LoveResponse;
import animation.member.Member;
import animation.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LoveAniService {

    private final LoveAniRepository loveAniRepository;
    private final MemberRepository memberRepository;
    private final AnimeRepository animeRepository;

    public LoveResponse create(String memberToken, LoveRequest loveRequest) {
        Member member = findByMemberId(memberToken);

        Anime anime = findByAniId(loveRequest.aniId());

        if(loveAniRepository.existsByMemberIdAndAnimeId(member.getId(), loveRequest.aniId())){
            throw new NoSuchElementException("이미 좋아요를 눌렀습니다.");
        }

        LoveAni loveAni = loveAniRepository.save(new LoveAni(member, anime));

        return new LoveResponse(
                loveRequest.aniId(),
                member.getId(),
                loveAni.getId(),
                LocalDateTime.now()
        );
    }

    public DeleteLoveAniResponse delete(String memberLoginId, Long loveId) {
        Member member = findByMemberId(memberLoginId);

        LoveAni loveAni = loveAniRepository.findById(loveId)
                .orElseThrow(() -> new NoSuchElementException("해당 좋아요를 찾을 수 없습니다."));

        findByAniId(loveAni.getAnime().getId());

        if(!loveAni.getMember().getId().equals(member.getId())){
            throw new NoSuchElementException("해당 좋아요를 삭제할 권한이 없습니다.");
        }

        loveAniRepository.deleteById(loveId);

        return new DeleteLoveAniResponse("좋아요가 삭제되었습니다.",
                new DeleteLoveAniDataResponse(
                        loveAni.getAnime().getId(),
                        loveId,
                        member.getId()
        ));
    }

    private Member findByMemberId(String memberLoginId){
        return memberRepository.findByLoginId(memberLoginId)
                .orElseThrow(() -> new NoSuchElementException("회원가입이 되어있지 않은 회원입니다."));
    }

    private Anime findByAniId(Long aniId){
        return animeRepository.findById(aniId)
                .orElseThrow(()-> new NoSuchElementException("해당 애니가 없습니다"));
    }
}
