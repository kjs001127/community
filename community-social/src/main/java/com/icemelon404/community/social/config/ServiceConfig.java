package com.icemelon404.community.social.config;


import com.icemelon404.community.social.domain.follow.core.FollowEventListener;
import com.icemelon404.community.social.domain.follow.core.FollowService;
import com.icemelon404.community.social.domain.follow.core.FollowServiceImpl;
import com.icemelon404.community.social.domain.follow.core.FollowStore;
import com.icemelon404.community.social.domain.follow.count.*;
import com.icemelon404.community.social.domain.follow.fetch.FollowInfoFetchService;
import com.icemelon404.community.social.domain.follow.fetch.FollowInfoFetchServiceImpl;
import com.icemelon404.community.social.domain.follow.fetch.FollowReader;
import com.icemelon404.community.social.domain.follow.pagedfetch.PagedFollowerFetchService;
import com.icemelon404.community.social.domain.follow.pagedfetch.PagedFollowerFetchServiceImpl;
import com.icemelon404.community.social.domain.follow.pagedfetch.PagedFollowerReader;
import com.icemelon404.community.social.domain.followrequest.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public FollowCountService countService(FollowCountStore reader) {
        return new FollowCountServiceImpl(reader);
    }

    @Bean
    public FollowInfoFetchService infoFetchService(FollowReader reader) {
        return new FollowInfoFetchServiceImpl(reader);
    }

    @Bean
    public FollowService followService(FollowStore store, FollowEventListener handler) {
        return new FollowServiceImpl(store, handler);
    }

    @Bean
    public FollowRequestFetchService requestFetchService(RequestReader reader) {
        return new FollowRequestFetchServiceImpl(reader);
    }

    @Bean
    public FollowRequestService requestService(RequestStore store, FollowService service) {
        return new FollowRequestServiceImpl(store, service);
    }

    @Bean
    public PagedFollowerFetchService pagedFollowerFetchService(PagedFollowerReader reader) {
        return new PagedFollowerFetchServiceImpl(reader);
    }

    @Bean
    public FollowEventListener countPushHandler(FollowCountStore modifier) {
        return new FollowCounter(modifier);
    }
}
