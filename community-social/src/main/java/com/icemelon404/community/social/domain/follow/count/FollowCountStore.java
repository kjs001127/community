package com.icemelon404.community.social.domain.follow.count;

import com.icemelon404.community.social.domain.dto.FollowCount;

public interface FollowCountStore {
    void modifyFollowCount(long userId, int amount);
    void modifyFollowerCount(long userId, int amount);

    FollowCount getFollowCount(long userId);
}
