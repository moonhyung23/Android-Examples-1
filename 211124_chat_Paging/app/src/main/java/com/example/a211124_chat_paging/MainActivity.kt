package com.example.a211124_chat_paging

import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.request.StringRequest
import com.android.volley.toolbox.Volley
import com.example.a211124_chat_paging.databinding.ActivityMainBinding
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    //Class
    var page = 1       // 현재 페이지(1페이지 부터 시작 )
    var limit = 20 // 한 번에 가져올 아이템 수
    var request_flag = true //요청확인 flag
    var all_row = 0 //전체 페이징할 로우 개수
    var paging_row = 0 //한 페이지에 갖고온 로우 개수


    val lm by lazy { LinearLayoutManager(applicationContext) } //레이아웃매니저
    lateinit var rv_adapter: Rv_Adapter_Chat

    var select_chat_url = "http://3.37.253.243/sports_friend/chating/ex_chat.php"

    //"본인댓글조회성공" -> 나의 댓글 페이징
    //"댓글조회성공" -> 다른 사람 댓글 페이징
    var message = ""   //  서버에서 보낸  메세지 (저장, 수정, 삭제,)

    //0번 포지션 이동 X
    //1번 마지막 포지션 이동
    // -처음에만 마지막 포지션으로 이동
    var flag_moveLastPos = 1

    var firstVisibleItemPosition: Int = 0

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initRecycler()




        binding.rvChatRoomInfor.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 화면에 보이는 첫번째 아이템의 position
                firstVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                // 스크롤이 끝에 도달했는지 확인
                //최상단
                if (!binding.rvChatRoomInfor.canScrollVertically(-1)) {
                    if (request_flag) {
                        Log.e("체크", "아이템마지막인덱스")
                        page++
                        //서버에 데이터 요청
                        Selcet_chat_volley(applicationContext, select_chat_url)
                    }
                }
                /*//최하단
                if (!binding.rvChatRoomInfor.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount) {
                    //요청할 데이터가 있는 경우에만

                }*/
            }
        })

        Selcet_chat_volley(applicationContext, select_chat_url)


    }

    //리사이클러뷰 초기화 메서드
    private fun initRecycler() {
        //리사이클러뷰 어댑터 객체 생성
        rv_adapter = Rv_Adapter_Chat(applicationContext)
        //리사이클러뷰에 어댑터 연결
        binding.rvChatRoomInfor.adapter = rv_adapter
        binding.rvChatRoomInfor.setHasFixedSize(true)
        rv_adapter.notifyDataSetChanged()
        //레이아웃 매니저 세팅
        binding.rvChatRoomInfor.layoutManager = lm
        lm.orientation = LinearLayoutManager.VERTICAL
    }

    private fun Selcet_chat_volley(context: Context, url: String) {
        // RequestQueue 생성 및 초기화
        val requestQueue = Volley.newRequestQueue(context)

        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->


                //응답 값
                Log.e(TAG, "응답: " + response.trim())

                if (response.trim() == "페이징할데이터없음") {
                    //페이징할 데이터 여부에 상관없이 스크롤뷰 최하단을 감지해서 page를 +1 해주었기 때문에
                    //페이징할 데이터가  없는 경우 다시 page를 -1 해준다.
                    page -= 1
                    //페이징 중지 -> 더이상 데이터 X
                    request_flag = false
                    rv_adapter.notifyDataSetChanged()
                    Toast.makeText(applicationContext, "마지막페이지입니다", Toast.LENGTH_SHORT).show()
                    return@Listener
                }

                Json_parse(response.trim())

            },

            //요청 실패
            Response.ErrorListener { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                // override fun getParams(): Map<String, String> {
                //서버에 보낼 데이터
                params["page"] = page.toString()
                params["limit"] = limit.toString()

                return params
            }
        }
        // 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request)
    }

    //JsonArray -> JsonObject로 변환
    private fun Json_parse(loadData_json: String) {
        //읽은 채팅, 읽지 않은 채팅 구분해서
        // 채팅 읽은 사람 -1을 한 값을 넣은 변수
        var edit_rp_cnt = ""
        //view Type에 따라서 사용해야 할 아이템뷰 변경
        var viewType = 0


        try {
            val jsonObject = JSONObject(loadData_json)
            //php에서 저장할 때 jsonArray의 키
            val jsonArray = jsonObject.getJSONArray("ar_chatInfor")
            var chat_content = ""
            var created_date = ""

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                //jsonObject에서 키 값에 따라서 데이터 추출
                var chat_user_idx = item.getString("chat_user_idx") //채팅 작성자 인덱스번호
                chat_content = item.getString("chat_content") //채팅 내용
                var chat_uuid = item.getString("chat_uuid") //채팅 idx 번호
                if (item.getString("created_date") != "") {
                    created_date = item.getString("created_date").substring(0, 16) //채팅 작성 날짜
                }
                var chat_room_uuid = item.getString("chat_room_uuid") //채팅 방 idx 번호
                var rp_cnt = item.getInt("rp_cnt").toString() //채팅 읽은 사람 수
                var chat_sendNickname = item.getString("chat_sendNickname")//채팅 보낸사람 닉네임
                var chat_userImg_url = item.getString("chat_userImg_url")//채팅 보낸사람 프로필 이미지 url
                var chat_viewType = item.getInt("chat_viewType")//채팅 뷰 타입
                var invite_Infor = item.getString("invite_Infor")//초대 정보
                var chat_id = item.getString("chat_id")
                all_row = item.getInt("all_row")
                paging_row = item.getInt("paging_row")
                message = item.getString("message")

                //리사이클러뷰에 추가
                rv_adapter.addItemFirst(
                    Chat(
                        chat_room_uuid, //채팅 방 idx 번호
                        chat_user_idx, //채팅 보낸 사람 닉네임
                        chat_userImg_url,        //채팅 보낸 사람 이미지
                        chat_content, //채팅 내용
                        created_date, //채팅 작성 시간
                        chat_uuid, //채팅 idx 번호
                        invite_Infor, //초대정보
                        1, //뷰타입
                        edit_rp_cnt, // 채팅 읽은 사람 수
                        chat_id //채팅 인덱스
                    )
                )
            }

            if (message == "채팅내역조회성공") {
                //페이징 계속 진행
                request_flag = true
            }


            //전체 조회 하지말고 페이징 된  아이템만 로드하기
//            rv_adapter.notifyItemRangeInserted((page - 1) * limit, limit)
            rv_adapter.notifyDataSetChanged()
            if (rv_adapter.getSize() != 0) {
                //1번 -> 리사이클러뷰 마지막 포지션으로 이동
                if (flag_moveLastPos == 1) {
                    //리사이클러뷰 아이템 뷰 스크롤 이동
                    binding.rvChatRoomInfor.scrollToPosition(rv_adapter.getSize() - 1)
                    //0번 -> 마지막 포지션 이동 X
                    flag_moveLastPos = 0
                } else {
                   //
                    (binding.rvChatRoomInfor.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                        paging_row,
                        paging_row
                    )

                }
            }


        } catch (e: JSONException) {
            Log.e("ParseError", "error : ", e)
        }
    }


}