package com.example.a211124_chat_paging

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.*

class Rv_Adapter_Chat(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //아이템뷰 리스트
    var List_chat = ArrayList<Chat>()


    //아이템클릭 리스너 객체
    //리사이클러뷰를 클릭 할 수 있는 리스너 객체
    //리사이클러뷰의 아이템뷰의 클릭을 감지해준다.
    private var listener: OnItemClickListener? = null

    //아이템 클릭 인터페이스
    interface OnItemClickListener {
        fun onItemClick(v: View, data: Chat, pos: Int)
    }

    //아이템 클릭 인터페이스를 전달
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    /*Rv_Adatper 부분에 작성 */


    //ViewType을 사용해서 ProgressBar 추가
    //뷰홀더 타입 번호
    private val VIEW_TYPE_ITEM = 1 //아이템뷰1
    private val VIEW_TYPE_LOADING = -1  //로딩 프로그레스바 아이템뷰

    //프로그레스바 아이템 삭제
    fun deleteLastIndex() {
        //등록된 친구가 있을 때만
        if (List_chat.size != 0) {
            //마지막 아이템뷰의 뷰 타입이 -1인 경우에만 삭제
            if (List_chat.get(List_chat.size - 1).viewType == -1) {
                List_chat.removeAt(List_chat.lastIndex) // 로딩이 완료되면 프로그레스바를 지움
            }
        }
    }

    // 뷰의 타입을 정해주는 곳이다.
    override fun getItemViewType(position: Int): Int {
        return when (List_chat[position].viewType) {
            1 -> VIEW_TYPE_ITEM  // 아이템뷰1
            2 -> VIEW_TYPE_ITEM //  아이템뷰2
            else -> VIEW_TYPE_LOADING    //프로그레스바 아이템뷰
        }
    }

    //1) onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        return when (viewType) {
            //뷰타입 1번: 아이템뷰1
            VIEW_TYPE_ITEM -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_item_chat, parent, false)
                ViewHolder_chat(view)
            }
            //나머지 뷰타입 번호 -> 프로그레스바 아이템뷰
            else -> {
                //프로그레스바
                view =
                    LayoutInflater.from(context).inflate(R.layout.rv_progress, parent, false)
                ViewHolder_progress(view)
            }
        }
    }

    //2) onBindViewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (List_chat[position].viewType) {
            //1번 뷰타입 -> 아이템뷰이름1
            1 -> {
                (holder as Rv_Adapter_Chat.ViewHolder_chat).bind(List_chat[position])
            }

            //나머지 뷰타입번호 -> 로딩 프로그레스바
            else -> {
                (holder as Rv_Adapter_Chat.ViewHolder_progress).bind(List_chat[position])
            }
        }
    }

    //아이템(모집 글) 갯수 출력
    override fun getItemCount(): Int = List_chat.size

    //아이템 추가
    fun addItem(item: Chat?) {
        if (item != null) {
            List_chat.add(item)
        } //item:리스트에들어갈데이터객체
    }

    //리사이클러뷰 가장 위에 추가
    fun addItemFirst(item: Chat?) {
        if (item != null) {
            List_chat.add(0, item)
        } //item:리스트에들어갈데이터객체
    }

    //아이템 수정
    fun edit_Item(position: Int, item: Chat?) {
        if (item != null) {
            List_chat.set(position, item)
        }
    }

    //아이템 삭제
    fun removeItem(position: Int) {
        List_chat.removeAt(position)
    }

    //아이템 조회
    fun getItem(position: Int): Chat? {
        return List_chat.get(position)
    }

    //아이템 갯수 조회
    fun getSize(): Int {
        return this.List_chat.size
    }

    //아이템 리스트 조회
    fun getList(): ArrayList<Chat> {
        return this.List_chat
    }

    //아이템 리스트 초기화
    fun clear() {
        this.List_chat.clear()
    }

    inner class ViewHolder_chat(view: View) : RecyclerView.ViewHolder(view) {
        //1)오늘 날짜
        private val tv_todayDate: TextView = itemView.findViewById(R.id.tv_TodayDate_rvItemMyChat)

        //2)채팅 내용
        private val tv_content: TextView = itemView.findViewById(R.id.tv_content_rvItemMyChat)

        //3)채팅 보낸 시간
        private val tv_time: TextView = itemView.findViewById(R.id.tv_time_rv_ItemMyChat)

        //4)채팅 읽은 사람 수
        private val tv_rp_cnt: TextView = itemView.findViewById(R.id.tv_rp_cnt_rv_ItemMyChat)

        //5)초대 정보
        private val tv_inviteInfor: TextView =
            itemView.findViewById(R.id.tv_inviteInfor_rvItemMyChat)

        //아이템 View(모집 글)에 모집 글 정보 입력
        fun bind(item: Chat) {
            //1.오늘 날자
            tv_todayDate.text = item.chat_todayDate
            //2.채팅 내용
            tv_content.text = item.chat_content
            //3.채팅 보낸 시간
            tv_time.text = item.chat_time
            //4.채팅 읽은 사람 수
            tv_rp_cnt.text = item.chat_rp_cnt
            //5.채팅 초대자 정보
            tv_inviteInfor.text = item.chat_invite_chatInfor

            //1번 -> 채팅
            if (item.viewType == 1) {
                //오늘날자(텍스트뷰)비활성화
                tv_todayDate.visibility = View.GONE
                tv_inviteInfor.visibility = View.GONE
            }
            //2번 -> 채팅, 날짜, 초대정보
            if (item.viewType == 2) {
                //1)오늘 날짜
                tv_todayDate.text = API.dateFormat(item.chat_time)

                //2) 채팅방 초대 정보
                tv_inviteInfor.text = item.chat_inviteInfor
            }


            //리사이클러뷰 아이템뷰 리스너
            itemView.setOnClickListener {
                //클릭한 아이템뷰의 인덱스 번호 변수에 저장
                val pos = adapterPosition
                //클릭이 되서 아이템뷰의 인덱스 번호를 받은 경우에만 실행
                if (pos != RecyclerView.NO_POSITION) {
                    //1.리사이클러뷰 수정, 삭제 팝업뷰 옵션버튼
                    listener?.onItemClick(itemView, item, pos)
                }
            }
        }
    }

    //ProgressBar 뷰홀더 클래스 작성
    inner class ViewHolder_progress(view: View) : RecyclerView.ViewHolder(view) {
        //1)View1
        lateinit var progressbar: ProgressBar;


        fun bind(item: Chat) {
        }
    }


}

