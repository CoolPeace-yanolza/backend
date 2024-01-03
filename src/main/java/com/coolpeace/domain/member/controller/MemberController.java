package com.coolpeace.domain.member.controller;

import com.coolpeace.domain.member.dto.request.MemberLoginRequest;
import com.coolpeace.domain.member.dto.request.MemberRegisterEmailCheckRequest;
import com.coolpeace.domain.member.dto.request.MemberRegisterRequest;
import com.coolpeace.domain.member.dto.request.RefreshAccessTokenRequest;
import com.coolpeace.domain.member.dto.response.MemberLoginResponse;
import com.coolpeace.domain.member.dto.response.MemberRefreshAccessTokenResponse;
import com.coolpeace.domain.member.dto.response.MemberRegisterResponse;
import com.coolpeace.domain.member.service.MemberService;
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
        return ResponseEntity.ok(MemberLoginResponse.from(memberService.login(loginRequest)));
    }

    @PostMapping("/register")
    public ResponseEntity<MemberRegisterResponse> register(
            @Valid @RequestBody MemberRegisterRequest registerRequest
    ) {
        return ResponseEntity.created(URI.create("/"))
                .body(MemberRegisterResponse.from(memberService.registerAsOwner(registerRequest)));
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
            String email, String accessToken
    ) {
        memberService.logout(email, accessToken);
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
