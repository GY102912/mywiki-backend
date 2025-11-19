package com.jian.community.application.service;

import com.jian.community.domain.event.PostViewEvent;
import com.jian.community.domain.repository.crud.PostViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class PostViewService {

    private final PostViewRepository postViewRepository;

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 새로운 트랜잭션 시작
    public void increaseCount(PostViewEvent event) {
        postViewRepository.increaseCount(event.postId());
    }
}
