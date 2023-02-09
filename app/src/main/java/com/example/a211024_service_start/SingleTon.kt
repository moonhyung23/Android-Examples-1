package com.example.a211024_service_start

class SingleTon {

    object API {
        //서비스가 실행되었는지 확인 변수
        var SERVICE_CONNECTED = false

        //1번 -> 클라이언트에서 서버로 메세지 전송
        var sendMsg_flag = 0

        //5-2) 채팅 관련
        var chat_room_idx = "" //채팅 방 번호

        //서버의 응답(메세지)을 기다리다  add_flag가 1일 때 핸들러에서 리사이클러뷰에 메세지를 추가하는 변수
        //1번 -> 상대방이 보낸 메세지를 추가
        var add_Flag: Int = 0

        //서버에서 보내온 메세지를 저장한 변수
        var readMsg_content: String = ""

        //클라이언트에서 보낸 채팅 관련 정보
        //스플릿으로 짤라서 채팅 관련 정보를 보낸다
        //채팅 관련 정보 1)채팅 내용, 회원 ID
        var SendMsg_split: String = ""
    }
}