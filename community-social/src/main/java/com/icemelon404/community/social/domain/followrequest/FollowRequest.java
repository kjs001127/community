package com.icemelon404.community.social.domain.followrequest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FollowRequest {
    private final long sender;
    private final long receiver;
}
