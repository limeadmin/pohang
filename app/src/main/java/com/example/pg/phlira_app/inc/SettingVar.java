package com.example.pg.phlira_app.inc;


/**
 * Created by pg on 2017-05-02.
 * 작성자 : 서봉교
 * 전역변수 설정
 */

public class SettingVar {
    public static final String MAIN_TITLE = "포항지역건축사회";

    //메인 도메인 설정
    private static final String MAIN_DOMAIN = "http://www.phkira.or.kr";
    //모바일 홈페이지 세션 셋팅
    public static final String SET_SESSION = MAIN_DOMAIN + "/m/inc/set_session.php";
    //모바일 홈페이지 메인
    public static final String MAIN_WEBSITE = MAIN_DOMAIN + "/m/index.php";
    //푸쉬메세지 토큰 등록
    public static final String REG_SERVER_FILE = MAIN_DOMAIN + "/sbg_fcm/register.php";
    //메세지 리스트 불러오기
    public static final String MSG_LOAD_SERVER_FILE = MAIN_DOMAIN + "/sbg_fcm/msg_load.php";
    //특정 메세지 내용 불러오기
    public static final String READ_MSG_SERVER_FILE = MAIN_DOMAIN + "/sbg_fcm/read_msg.php";
    //메세지 첨부이미지 경로
    public static final String MSG_IMG_PATH = MAIN_DOMAIN + "/sbg_fcm/data/pohang/";
    //인증 유효성 검사
    public static final String REG_MEMBER_CHECK = MAIN_DOMAIN + "/sbg_fcm/reg_check.php";
    //인증후 회원정보를 fcm DB에 저장 및 중복값 삭제
    public static final String CHECK_FCM_DB = MAIN_DOMAIN + "/sbg_fcm/check_fcm_db.php";
    //건축설계노트 주소
    public static final String ARC_NOTE_URL = "http://tmiweb.kr/ph/index.html";
    //광고이미지 경로
    public static final String AD_IMG_PATH = MAIN_DOMAIN + "/sbg_fcm/adimg/pohang/ad_img.jpg";


    //폰트이름
    public static final String FONT_FILE = "fonts/NotoSansKR-Regular-Hestia.otf";
    //토스트형식 팝업창
    public static final int TOAST_POPUP = 1;

    //퓌쉬 알림 팝업창 띄울지 유무
    public static boolean msgPopup = true;

    //푸쉬알림창에서 바로 앱이 실행된 경우 메세지 확인창으로 이동할때 체크할 변수
    public static String moveMsg = "";

    //폰번호 저장
    public static String phNumber = "";
    //id,license 값 저장
    public static String id = "";
    public static String license = "";

    //메세지 DB(fcm_msg) 항목들
    public static final String TAG_RESULTS = "result";
    public static final String MSG_DB_NUM = "num";
    public static final String MSG_DB_ID = "id";
    public static final String MSG_DB_KIND = "kind";
    public static final String MSG_DB_SUBJECT = "subject";
    public static final String MSG_DB_MSG = "msg";
    public static final String MSG_DB_IMG = "img";
    public static final String MSG_DB_WDATE = "wdate";
    //메세지 리스트 로드시 읽음 유무 체크
    public static final String MSG_CONFIRM = "mconf";


    //옵션설정 항목들
    public static boolean ALAM_POPUP = true;
    public static boolean ALAM_SOUND = true;
    public static boolean ALAM_VIBRATE = true;
}
