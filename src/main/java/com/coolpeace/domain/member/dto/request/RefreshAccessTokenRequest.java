package com.coolpeace.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshAccessTokenRequest(
        @NotBlank(message = "리프레시 토큰을 입력해야 합니다.")
        String refreshToken
) {
}
