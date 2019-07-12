package com.newapi.apivk.architecture.task

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.objects.messages.LongpollParams

class GetLongPoolServerTask : Task<UserActor, LongpollParams> {

    lateinit var vkApiClient: VkApiClient

    override suspend fun execute(input: UserActor): LongpollParams =
            vkApiClient.messages().getLongPollServer(input).execute()

    fun addParams(vkApiClient: VkApiClient): GetLongPoolServerTask {
        this.vkApiClient = vkApiClient
        return this
    }
}
