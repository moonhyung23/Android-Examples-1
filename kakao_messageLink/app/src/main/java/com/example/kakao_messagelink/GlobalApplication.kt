package com.example.kakao_messagelink

import android.app.Application
import android.widget.Button

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "{NATIVE_APP_KEY}")
    }


    private fun sendKakaoLink(userReview: UserReview) {
        // 메시지 템플릿 만들기 (피드형)
        val defaultFeed = FeedTemplate(
            content = Content(
                title = userReview.product.title,
                description = userReview.review.content,
                imageUrl = userReview.review.image,
                link = Link(
                    mobileWebUrl = "https://play.google.com/store/apps/details?id=com.mtjin.nomoneytrip"
                )
            ), social = Social(
                likeCount = userReview.review.recommendList.size
            ),
            buttons = listOf(
                Button(
                    "앱으로 보기",
                    Link(
                        androidExecParams = mapOf(
                            "key1" to "value1",
                            "key2" to "value2"
                        ) // 내 앱에서 파라미터보낼건 필요없음 (앱 다운로드만 유도할것이다)
                    )
                )
            )
        )

        //메시지 보내기 (뒤에서 살펴볼것)
}