package com.jian.community.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity(name = "tb_post_like")
public class PostLike {

    @EmbeddedId
    PostLikeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public static PostLike of(Post post, User user) {
        PostLike postLike = new PostLike();
        postLike.id = PostLikeId.of(post.getId(), user.getId());
        postLike.post = post;
        postLike.user = user;
        return postLike;
    }

    public boolean isBelongsTo(Post post) {
        return this.post.equals(post);
    }

    public boolean isLikedBy(User user) {
        return this.user.equals(user);
    }

    public void delete() { this.isDeleted = true; }
}
