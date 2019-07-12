package com.newapi.apivk.architecture.task

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.objects.messages.LongpollParams
import com.vk.api.sdk.objects.messages.responses.GetLongPollHistoryResponse

class GetLastMessagesTask : Task<UserActor, GetLongPollHistoryResponse> {

    lateinit var vkApiClient: VkApiClient

    lateinit var longpollParams: LongpollParams

    private var pts: Int = 0

    override suspend fun execute(input: UserActor): GetLongPollHistoryResponse {

        val response = vkApiClient.messages().getLongPollHistory(input).ts(longpollParams.ts)
        return if (pts == 0) {
            response.execute()
        } else {
            response.pts(pts).execute()
        }
    }

    fun addParams(longpollParams: LongpollParams, vkApiClient: VkApiClient, pts: Int): GetLastMessagesTask {
        this.longpollParams = longpollParams
        this.pts = pts
        this.vkApiClient = vkApiClient
        return this
    }
}
