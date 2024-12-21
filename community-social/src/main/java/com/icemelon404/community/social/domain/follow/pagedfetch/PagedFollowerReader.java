package com.icemelon404.community.social.domain.follow.pagedfetch;

import com.icemelon404.community.commons.dto.ConcretePagedRequest;
import com.icemelon404.community.social.domain.follow.entity.Follow;

import java.util.List;

public interface PagedFollowerReader {

    List<Follow> getFollowers(long followee, ConcretePagedRequest request);
}
