package com.icemelon404.community.social.infra;

import com.icemelon404.community.commons.dto.AscPagedRequest;
import com.icemelon404.community.commons.dto.ConcretePagedRequest;
import com.icemelon404.community.commons.exception.DuplicateResourceException;
import com.icemelon404.community.commons.exception.NoSuchResourceException;
import com.icemelon404.community.social.domain.follow.entity.Follow;
import com.icemelon404.community.social.domain.follow.core.FollowStore;
import com.icemelon404.community.social.domain.follow.fetch.FollowReader;
import com.icemelon404.community.social.domain.follow.pagedfetch.PagedFollowerReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;


import java.util.List;

@RequiredArgsConstructor
public class JdbcFollowRepository implements FollowReader, FollowStore, PagedFollowerReader {

    private final JdbcTemplate template;

    @Override
    public void add(Follow follow) {
        try {
            template.update("INSERT INTO follow (follower, followee, random) VALUES (?,?, ?)",
                    follow.getFollower(), follow.getFollowee(), follow.hashCode());
        } catch (DuplicateKeyException e) {
            throw new DuplicateResourceException(follow.getFollower() + "->" + follow.getFollowee() + " 는 팔로우 상태입니다");
        }
    }

    @Override
    public boolean has(Follow follow) {
        return template.queryForObject("SELECT COUNT(*) FROM follow WHERE follower= ?", Integer.class, follow.getFollower()) > 0;
    }

    @Override
    public void remove(Follow follow) {
        int removeSuccess = template.update("DELETE FROM follow WHERE follower= ? AND followee= ?", follow.getFollower(), follow.getFollowee());
        if (removeSuccess == 0)
            throw new NoSuchResourceException(follow.getFollower() + " -> " + follow.getFollowee() + " 팔로우 관계가 존재하지 않습니다");
    }

    @Override
    public List<Follow> getFollowers(long followee, AscPagedRequest<Long> request) {
        return template.query("SELECT * FROM follow WHERE followee= ? AND follower > ? LIMIT ?",
                (row, rowNum) -> new Follow(row.getLong("follower"), row.getLong("followee")),
                followee, request.getBoundary(), request.getSize());
    }

    @Override
    public List<Follow> getFollows(long follower, AscPagedRequest<Long> request) {
        return template.query("SELECT * FROM follow WHERE follower= ? AND followee > ? LIMIT ?",
                (row, rowNum) -> new Follow(row.getLong("follower"), row.getLong("followee")),
                follower, request.getBoundary(), request.getSize());
    }

    @Override
    public List<Follow> getFollowers(long followee, ConcretePagedRequest request) {
        Boundary boundary = getPage(request);
        return template.query("SELECT * FROM follow WHERE followee= ? AND random >= ? AND random < ?",
                (row, rowNum) -> new Follow(row.getLong("follower"), row.getLong("followee")),
                followee, boundary.low(), boundary.high());
    }

    private record Boundary(int low, int high){}

    private Boundary getPage(ConcretePagedRequest request) {
        int low = Integer.MAX_VALUE / request.getTotalPage() * request.getRequestPage();
        int maxPageIdx = request.getTotalPage() - 1;
        if (request.getRequestPage() == maxPageIdx)
            return new Boundary(low, Integer.MAX_VALUE);
        int high = Integer.MAX_VALUE / request.getTotalPage() * (request.getRequestPage() + 1);
        return new Boundary(low, high);
    }
}
