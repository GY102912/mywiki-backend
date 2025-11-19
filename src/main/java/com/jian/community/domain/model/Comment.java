package com.jian.community.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity(name = "tb_comment")
public class Comment extends MinimalEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, updatable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "depth", nullable = false)
    private Integer depth = 0;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public static Comment of(Post post, User user, String content) {
        Comment comment = new Comment();
        comment.post = post;
        comment.user = user;
        comment.content = content;
        return comment;
    }

    public void update(String content) {
        this.content = content;
    }

    public boolean isBelongsTo(Post post) {
        return this.post.equals(post);
    }

    public boolean isWrittenBy(User writer){
        return this.user.equals(writer);
    }

    public void delete() { this.isDeleted = true; }
}
