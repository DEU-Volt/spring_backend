package deu.se.volt.microservices.core.order.util;

public class ResponseMessage {
    public static final String NOT_FOUND_PRODUCT = "상품을 찾을 수 없습니다.";
    public static final String ORDER_REG_SUCCESS = "주문 등록 성공";
    public static final String PRODUCT_SUCCESS = "상품 조회 성공";
    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "로그인 실패";
    public static final String READ_USER = "회원 정보 조회 성공";
    public static final String NOT_FOUND_USER = "회원을 찾을 수 없습니다.";
    public static final String EXIST_USER = "이미 동일한 아이디를 가진 회원이 존재합니다";
    public static final String CREATED_USER = "회원 가입 성공";
    public static final String UPDATE_USER = "회원 정보 수정 성공";
    public static final String DELETE_USER = "회원 탈퇴 성공";
    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";
    public static final String DB_ERROR = "데이터베이스 에러";
    public static final String INPUT_ERROR = "유효하지 않은 입력값이 전달되었습니다";
    public static final String NOT_FOUND_ORDER = "주문을 찾을 수 없습니다.";
    public static final String ORDER_SUCCESS = "주문 조회 성공";
}