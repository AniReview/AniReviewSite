package animation.loveAni.dto;

public record DeleteLoveAniDataResponse(
        Long aniId,
        Long loveId,
        Long memberId
) {
}
