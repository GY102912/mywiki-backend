package com.jian.community.presentation.validation;

public class ValidationMessage {

    /*
      @NotBlank
     */
    public static final String EMAIL_REQUIRED = "이메일은 필수 입력 항목입니다.";
    public static final String PASSWORD_REQUIRED = "비밀번호는 필수 입력 항목입니다.";
    public static final String NICKNAME_REQUIRED = "닉네임은 필수 입력 항목입니다.";
    public static final String PROFILE_IMAGE_URL_REQUIRED = "프로필 이미지 링크는 필수 입력 항목입니다.";
    public static final String POST_TITLE_REQUIRED = "게시글 제목은 필수 입력 항목입니다.";
    public static final String POST_CONTENT_REQUIRED = "게시글 내용은 필수 입력 항목입니다.";
    public static final String COMMENT_CONTENT_REQUIRED = "댓글 내용은 필수 입력 항목입니다.";

    /*
      @Pattern
     */
    public static final String INVALID_EMAIL = "올바르지 않은 이메일 형식입니다.";
    public static final String INVALID_PASSWORD = "비밀번호는 8자 이상이며, 영문과 숫자를 모두 포함해야 합니다.";
    public static final String INVALID_NICKNAME = "닉네임은 2~20자의 한글, 영문, 숫자만 사용할 수 있습니다.";

    /*
      @Size
     */
    public static final String INVALID_POST_TITLE = "게시글 제목은 최대 26자까지 입력할 수 있습니다.";

    /*
      @ValidImage
     */
    public static final String INVALID_IMAGE = "유효하지 않은 이미지 파일입니다.";
    public static final String INVALID_IMAGE_EXTENSION = "jpg, png 형식의 이미지 파일만 저장할 수 있습니다.";
    public static final String IMAGE_MUST_NOT_EMPTY = "비어 있는 이미지 파일은 저장할 수 없습니다.";
    public static final String IMAGE_TOO_LARGE = "최대 5MB의 이미지 파일만 저장할 수 있습니다.";
}
