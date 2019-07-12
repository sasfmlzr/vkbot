package com.newapi.apivk.architecture.task

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import kotlin.random.Random

class SendMessageTask : Task<UserActor, Unit> {

    lateinit var vkApiClient: VkApiClient

    lateinit var message: String
    private var peerId: Int = 0

    override suspend fun execute(input: UserActor): Unit {
        vkApiClient.messages().send(input).message(message).peerId(peerId)
                .randomId(Random.nextInt(0, 10000))
                .execute()
    }


    fun addParams(message: String, peerId: Int, vkApiClient: VkApiClient): SendMessageTask {
        this.message = message
        this.peerId = peerId
        this.vkApiClient = vkApiClient
        return this
    }
}
