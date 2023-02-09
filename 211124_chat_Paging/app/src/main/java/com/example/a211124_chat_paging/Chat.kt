package com.example.a211124_chat_paging

class Chat {

    //공통
    var viewType: Int = 0
    var extra: String = "" //생성자 겹쳐서 넣음

    //채팅 관리번호
    // 1 -> 채팅방 생성
    // 2 -> 채팅 보내기
    var status_num: Int = 0

    //1.채팅방 관련 변수
    var chatRoom_attend_idx: String = "" //채팅 방 참여자 idx
    var chatRoom_host_idx: String = "" //채팅 방 방장 idx
    var chatRoom_name: String = "" //채팅 방 이름(채팅 방 참여자 이름)
    var chatRoom_content: String = "" //채팅 방 최근 채팅 내용
    var chatRoom_chatCnt: String = "" //채팅 개수
    var chatRoom_time: String = "" // 최근 채팅 보낸/받은 시간
    var chatRoom_Mainimg_url: String = "" //채팅 방 메인 이미지
    var chatRoom_person_cnt: String = "" //채팅 방 인원 수
    var chat_room_idx: String = "" //채팅 방 번호
    var chat_created_date: String = "" //채팅 생성 날자
    var chat_room_sort_date: String = "" //채팅 방 정렬에 필요한 날짜


    //2.채팅 내용 관련 변수
    var chat_userId: String = ""
    var chat_sendNickname: String = ""
    var chat_profileImgUrl: String = ""
    var chat_content: String = ""
    var chat_time: String = ""
    var chat_uuid: String = "" //채팅  uuid
    var chat_id: String = "" //채팅  id
    var chat_todayDate: String = "" //오늘 날짜
    var chat_Send_nickname: String = "" //채팅 보낸 사람 닉네임
    var chat_invite_chatInfor: String = "" //채팅방 초대 정보
    var chat_inviteInfor: String = "" //초대 정보
    var chat_rp_cnt: String = "" // 채팅 읽은 사람 수 표시시

    //채팅 번호
    // 1번 -> 채팅, 날짜, 초대정보
    // 2번 -> 채팅
    // 3번 -> 초대정보
    var chat_ViewType: Int = 0

    //3.대화상대 초대 시 보여주는 친구목록 관련 변수
    var invite_profile_img: String = ""
    var invite_nickname: String = ""
    var invite_user_idx: String = ""

    //체크박스 체크번호
    //1번 -> 체크
    //2번 -> 체크 X
    var invite_cb_flag: Int = 0


    //1.채팅 방 정보 생성자
    //-리사이클러뷰에 채팅방 정보 추가 할 때 사용
    constructor(
        chatRoom_attend_idx: String,
        chatRoom_host_idx: String,
        chatRoom_name: String,
        chatRoom_content: String,
        chatRoom_chatCnt: String,
        chatRoom_time: String,
        chatRoom_person_cnt: String,
        chat_room_idx: String,
        viewType: Int
    ) {
        this.chatRoom_attend_idx = chatRoom_attend_idx
        this.chatRoom_host_idx = chatRoom_host_idx
        this.chatRoom_name = chatRoom_name
        this.chatRoom_content = chatRoom_content
        this.chatRoom_chatCnt = chatRoom_chatCnt
        this.chatRoom_time = chatRoom_time
        this.chatRoom_person_cnt = chatRoom_person_cnt
        this.chat_room_idx = chat_room_idx
        this.viewType = viewType
    }


    //채팅 방  처음 생성시 서버에 보낼 데이터 생성자
    //채팅방 정보 + 처음 보낸 채팅
    constructor(
        status_num: Int,
        invite_user_idx: String,
        invite_nickname: String,
        chat_content: String,
        chatRoom_host_idx: String,
        chat_room_idx: String,
        chat_uuid: String,
        chat_created_date: String,//채팅 생성 날자
        chat_userNickname: String, //채팅 보낸 유저 닉네임
        chatroom_name: String, //채팅 방이름
        chat_profileImgUrl: String, //채팅 보낸 유저 프로필사진
        chat_viewType: Int, //채팅 보낸 유저 뷰타입
        chat_rp_cnt: String

    ) {
        this.status_num = status_num
        this.invite_user_idx = invite_user_idx
        this.invite_nickname = invite_nickname
        this.chat_content = chat_content
        this.chatRoom_host_idx = chatRoom_host_idx
        this.chat_room_idx = chat_room_idx
        this.chat_uuid = chat_uuid
        this.chat_created_date = chat_created_date
        this.chat_sendNickname = chat_userNickname
        this.chatRoom_name = chatroom_name
        this.chat_profileImgUrl = chat_profileImgUrl
        this.chat_ViewType = chat_viewType
        this.chat_rp_cnt = chat_rp_cnt

    }

    // -채팅방을 나갈 때 서버에 데이터를 보내는 생성자.
    // -이미 생성된 방에 채팅 보낼 때 사용하는 생성자
    constructor(
        status_num: Int,
        invite_user_idx: String,
        invite_nickname: String,
        chat_content: String,
        chatRoom_host_idx: String,
        chat_room_idx: String,
        chat_uuid: String,
        chat_created_date: String,//채팅 생성 날자
        chat_userNickname: String, //채팅 보낸 유저 닉네임
        chat_profileImgUrl: String, //채팅 보낸 사람 프로필사진
        chat_viewType: Int, //채팅 뷰타입
        chat_rp_cnt: String
    ) {
        this.status_num = status_num
        this.invite_user_idx = invite_user_idx
        this.invite_nickname = invite_nickname
        this.chat_content = chat_content
        this.chatRoom_host_idx = chatRoom_host_idx
        this.chat_room_idx = chat_room_idx
        this.chat_uuid = chat_uuid
        this.chat_created_date = chat_created_date
        this.chat_sendNickname = chat_userNickname
        this.chat_profileImgUrl = chat_profileImgUrl
        this.chat_ViewType = chat_viewType
        this.chat_rp_cnt = chat_rp_cnt
    }

    //-채팅방 입장 시 서버에 데이터를 보낼 때 사용하는 생성자.
    //-초대 정보를 서버에 보낼 때 사용하는 생성자.
    //-채팅방 나가기(완전X)에 사용하는 생성자
    constructor(
        status_num: Int,
        invite_user_idx: String,
        invite_nickname: String,
        chat_content: String,
        chatRoom_host_idx: String,
        chat_room_idx: String,
        chat_uuid: String,
        chat_created_date: String,//채팅 생성 날자
        chat_userNickname: String, //채팅 보낸 유저 닉네임
        chat_profileImgUrl: String, //채팅 보낸 사람 프로필사진
        chat_viewType: Int, //채팅 뷰타입
    ) {
        this.status_num = status_num
        this.invite_user_idx = invite_user_idx
        this.invite_nickname = invite_nickname
        this.chat_content = chat_content
        this.chatRoom_host_idx = chatRoom_host_idx
        this.chat_room_idx = chat_room_idx
        this.chat_uuid = chat_uuid
        this.chat_created_date = chat_created_date
        this.chat_sendNickname = chat_userNickname
        this.chat_profileImgUrl = chat_profileImgUrl
        this.chat_ViewType = chat_viewType
    }

    //처음 채팅방 정보 volley사용해서 조회할 때 사용
    //2.채팅 내용 생성자
    //2-1)내가 보낸 채팅 내용 생성자
    //-Volley를 이용한 채팅 정보 조회 할 때 사용
    constructor(
        chat_room_idx: String,
        chat_userId: String,
        chat_profileImgUrl: String,
        chat_content: String,
        chat_time: String,
        chat_uuid: String,
        invite_Infor: String,
        viewType: Int,
        chat_rp_cnt: String,
        chat_id: String
    ) {
        this.chat_room_idx = chat_room_idx
        this.chat_userId = chat_userId
        this.chat_profileImgUrl = chat_profileImgUrl
        this.chat_content = chat_content
        this.chat_time = chat_time
        this.chat_uuid = chat_uuid
        this.chat_inviteInfor = invite_Infor
        this.viewType = viewType
        this.chat_rp_cnt = chat_rp_cnt
        this.chat_id = chat_id
    }


    //3.채팅 내용 생성자
    // -채팅방 나간 사람 정보
    constructor(
        chat_room_idx: String,
        chat_userId: String,
        chat_profileImgUrl: String,
        chat_content: String,
        chat_time: String,
        chat_idx: String,
        invite_Infor: String,
        viewType: Int,
        chat_id: String
    ) {
        this.chat_room_idx = chat_room_idx
        this.chat_userId = chat_userId
        this.chat_profileImgUrl = chat_profileImgUrl
        this.chat_content = chat_content
        this.chat_time = chat_time
        this.chat_uuid = chat_idx
        this.chat_inviteInfor = invite_Infor
        this.viewType = viewType
        this.chat_id = chat_id
    }


    //3-2)소켓통신으로 받아온 채팅 정보 리사이클러뷰에 추가
    // -채팅방 나간사람 정보
    // -채팅방 초대정보 정보

    constructor(
        chat_room_idx: String,
        chat_userId: String,
        chat_sendNickname: String,
        chat_profileImgUrl: String,
        chat_content: String,
        chat_time: String,
        chat_idx: String,
        chat_todayDate: String,
        viewType: Int,
        chat_inviteInfor: String,
        extra: String
    ) {
        this.chat_room_idx = chat_room_idx
        this.chat_userId = chat_userId
        this.chat_sendNickname = chat_sendNickname
        this.chat_profileImgUrl = chat_profileImgUrl
        this.chat_content = chat_content
        this.chat_time = chat_time
        this.chat_uuid = chat_idx
        this.chat_todayDate = chat_todayDate
        this.viewType = viewType
        this.chat_inviteInfor = chat_inviteInfor
        this.extra = extra
    }

    // -내가 보낸 채팅 정보
    // -상대방이 보낸 채팅 정보
    // -volley사용해서 상대방이 보낸 채팅 정보조회 할 때 (날짜 추가)
    // -volley사용해서 상대방이 보낸 채팅 정보조회 할 때 (날짜 추가X)
    constructor(
        chat_room_idx: String,
        chat_userId: String,
        chat_sendNickname: String,
        chat_profileImgUrl: String,
        chat_content: String,
        chat_time: String,
        chat_uuid: String,
        chat_todayDate: String,
        viewType: Int,
        chat_inviteInfor: String,
        chat_rp_cnt: String,
        chat_id: String,
        extra: String,

        ) {
        this.chat_room_idx = chat_room_idx
        this.chat_userId = chat_userId
        this.chat_sendNickname = chat_sendNickname
        this.chat_profileImgUrl = chat_profileImgUrl
        this.chat_content = chat_content
        this.chat_time = chat_time
        this.chat_uuid = chat_uuid
        this.chat_todayDate = chat_todayDate
        this.viewType = viewType
        this.chat_inviteInfor = chat_inviteInfor
        this.chat_rp_cnt = chat_rp_cnt
        this.chat_id = chat_id
        this.extra = extra
    }


    //3.채팅방 대화상대 초대시 보여주는 친구목록 정보 생성자
    //  채팅 방 정보에 보여주는 채팅방 참여자 정보 생성자
    constructor(
        viewType: Int,
        invite_profile_img: String,
        invite_nickname: String,
        invite_user_idx: String,
    ) {
        this.viewType = viewType
        this.invite_profile_img = invite_profile_img
        this.invite_nickname = invite_nickname
        this.invite_user_idx = invite_user_idx
    }


}