package com.icemelon404.community.social.domain.followrequest;

public interface RequestStore {
    void save(FollowRequest relation);
    boolean has(FollowRequest relation);
    void remove(FollowRequest relation);
}
