package com.coolpeace.domain.member.controller;

import com.coolpeace.domain.member.dto.request.MemberLoginRequest;
import com.coolpeace.domain.member.dto.request.MemberRegisterEmailCheckRequest;
import com.coolpeace.domain.member.dto.request.MemberRegisterRequest;
import com.coolpeace.domain.member.dto.request.RefreshAccessTokenRequest;
import com.coolpeace.domain.member.dto.response.MemberLoginResponse;
import com.coolpeace.domain.member.dto.response.MemberRefreshAccessTokenResponse;
import com.coolpeace.domain.member.service.MemberService;
import com.coolpeace.global.jwt.security.MemberPrincipal;
import com.coolpeace.global.resolver.AuthJwtCredential;
import com.coolpeace.global.resolver.AuthJwtPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> login(
            @Valid @RequestBody MemberLoginRequest loginRequest
    ) {
        return ResponseEntity.ok(memberService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody MemberRegisterRequest registerRequest
    ) {
        memberService.registerAsOwner(registerRequest);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @GetMapping("/register/check/email")
    public ResponseEntity<Void> memberEmailCheck(
            @Valid MemberRegisterEmailCheckRequest emailCheckRequest
    ) {
        memberService.emailCheck(emailCheckRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthJwtPrincipal MemberPrincipal memberPrincipal,
            @AuthJwtCredential String accessToken
    ) {
        memberService.logout(memberPrincipal.getMemberEmail(), accessToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<MemberRefreshAccessTokenResponse> refreshAccessToken(
            @Valid @RequestBody RefreshAccessTokenRequest request
    ) {
        return ResponseEntity.ok(
                MemberRefreshAccessTokenResponse.from(memberService.refreshAccessToken(request.refreshToken()))
        );
    }
}
