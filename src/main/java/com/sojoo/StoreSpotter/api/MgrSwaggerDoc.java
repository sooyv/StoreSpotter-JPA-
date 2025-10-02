package com.sojoo.StoreSpotter.api;

public class MgrSwaggerDoc {
    public static final class User {

        public static class Api {

            public static class page {
                public static final String LOGIN_SUMMARY  = "로그인 페이지 조회";
                public static final String LOGIN_DESC     = "로그인 페이지 뷰를 반환합니다. (ModelAndView)";

                public static final String SIGNUP_SUMMARY = "회원가입 페이지 조회";
                public static final String SIGNUP_DESC    = "회원가입 페이지 뷰를 반환합니다. (ModelAndView)";

                public static final String FIND_INFO_SUMMARY = "회원정보 찾기 페이지 조회";
                public static final String FIND_INFO_DESC    = "회원정보 찾기 페이지 뷰를 반환합니다. (ModelAndView)";
            }

            public static class sendMailCode {
                public static final String Summary = "메일 인증 코드 전송";
                public static final String Desc = "회원가입을 진행할 이메일 인증을 위한 인증 코드를 전송합니다.";
            }
            public static class findUserAccount {
                public static final String Summary = "사용자 이메일 조회";
                public static final String Desc = "사용자 메일 찾기를 진행합니다.";
            }
            public static class sendReissuePwtCode {
                public static final String Summary = "비밀번호 재발급 인증메일 전송";
                public static final String Desc = "비밀번호를 재설정할 이메일을 입력한 후 인증 코드를 전송합니다.";
            }
            public static class sendReissuePwt {
                public static final String Summary = "비밀번호 재발급 메일 전송";
                public static final String Desc = "임시 비밀번호를 메일로 전송합니다.";
            }

        }
    }

    public static final class Mypage {
        public static class Api {

            public static class page {
                public static final String MYPAGE_SUMMARY = "마이페이지 조회";
                public static final String MYPAGE_DESC = "마이페이지 뷰를 반환합니다.";

            }

            public static class liked {
                public static final String Summary = "좋아요(❤) 추가";
                public static final String Desc = "추천 위치를 좋아요 목록에 추가합니다.";
            }

            public static class editLiked {
                public static final String Summary = "좋아요 이름 수정";
                public static final String Desc = "좋아요 한 위치의 이름을 수정합니다.";
            }

            public static class deleteLiked {
                public static final String Summary = "좋아요 삭제";
                public static final String Desc = "좋아요 목록에서 한 개의 좋아요를 삭제합니다.";
            }

            public static class modifyName {
                public static final String Summary = "회원 이름 변경";
                public static final String Desc = "마이페이지에서 회원 이름을 변경합니다.";
            }

            public static class modifyPhone {
                public static final String Summary = "회원 전화번호 변경";
                public static final String Desc = "마이페이지에서 회원 전화번호를 변경합니다.";
            }

            public static class withdraw {
                public static final String Summary = "회원 탈퇴";
                public static final String Desc = "회원 탈퇴를 진행합니다.";
            }


        }
    }

    public static final class Admin {

        public static class Api {

            public static class page {
                public static final String ADMIN_SUMMARY = "어드민 페이지 조회";
                public static final String ADMIN_DESC = "어드민 페이지 뷰를 반환합니다. (ModelAndView)";
            }

            public static class industriesApi {
                public static final String Summary = "전체(업종/전국) 상가정보 API 호출";
                public static final String Desc = "API를 호출하여 전체(업종/전국) 상가정보를 저장합니다.";
            }

            public static class convApi {
                public static final String Summary = "편의점 전국 상가정보 API 호출";
                public static final String Desc = "API를 호출하여 편의점의 상가정보를 저장합니다.";
            }

            public static class cafeApi {
                public static final String Summary = "카페 전국 상가정보 API 호출";
                public static final String Desc = "API를 호출하여 카페의 상가정보를 저장합니다.";
            }

            public static class dataPair {
                public static final String Summary = "전체(업종/전국) 상가정보 평균거리 비교";
                public static final String Desc = "전체(업종/전국) 평균거리를 비교 저장합니다.";
            }

            public static class convPair {
                public static final String Summary = "편의점 상가정보 평균거리 비교";
                public static final String Desc = "지역별 편의점 평균거리를 비교 저장합니다.";
            }

            public static class cafePair {
                public static final String Summary = "카페 상가정보 평균거리 비교";
                public static final String Desc = "지역별 카페 평균거리를 비교 저장합니다.";
            }
        }
    }

    public static final class Auth {

        public static class Api {

            public static class login {
                public static final String Summary = "로그인 진행";
                public static final String Desc = "로그인 진행 및 토큰 발급";
            }

            public static class logout {
                public static final String Summary = "로그아웃 진행";
                public static final String Desc = "로그아웃 및 토큰을 삭제합니다.";
            }

            public static class signup {
                public static final String Summary = "회원가입 진행";
                public static final String Desc = "회원가입 진행 및 회원 정보를 디비에 저장합니다.";
            }
        }
    }





        public static class Response {
        public static final String status = "응답 코드";
        public static final String errorCode = "응답 메시지";
        public static final String msg = "응답 상세 메시지";
//        public static final String data = "응답 데이터";

        public static class Success {
            public static final String Code = "200";
            public static final String Desc = "성공";
        }

    }
}
