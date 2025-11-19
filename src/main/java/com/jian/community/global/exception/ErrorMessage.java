package com.jian.community.global.exception;

public class ErrorMessage {

    public static final String INVALID_SESSION = "세션이 만료되었거나 존재하지 않습니다.";
    public static final String INVALID_CREDENTIALS = "인증 정보가 올바르지 않습니다.";
    public static final String EMAIL_ALREADY_EXISTS = "이미 사용 중인 이메일입니다.";
    public static final String NICKNAME_ALREADY_EXISTS = "이미 사용 중인 닉네임입니다.";
    public static final String COMMENT_NOT_EXISTS = "댓글을 찾을 수 없습니다.";
    public static final String ACCESS_DENIED = "접근 권한이 없습니다.";
    public static final String POST_NOT_EXISTS = "게시글을 찾을 수 없습니다.";
    public static final String POST_VIEW_NOT_EXISTS = "게시글 조회수 정보를 찾을 수 없습니다.";
    public static final String USER_NOT_EXISTS = "사용자를 찾을 수 없습니다.";
    public static final String TOO_MANY_REQUESTS = "비정상적으로 많은 요청을 보냈습니다. 잠시 후 다시 시도해주세요.";
    public static final String FILE_STORE_FAILED = "파일 저장에 실패했습니다.";
    public static final String INVALID_INPUT = "입력값이 유효하지 않습니다.";
    public static final String INTERNAL_SERVER_ERROR = "서버에서 에러가 발생했습니다.";
    public static final String UNAUTHORIZED_POST_WRITER = "게시글 작성자에게만 권한이 있습니다.";
    public static final String UNAUTHORIZED_COMMENT_WRITER = "댓글 작성자에게만 권한이 있습니다.";
    public static final String UNAUTHORIZED_POST_LIKE_USER = "게시글 좋아요 생성자에게만 권한이 있습니다.";
}
