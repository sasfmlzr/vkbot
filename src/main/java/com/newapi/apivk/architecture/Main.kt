package com.newapi.apivk.architecture

import com.newapi.apivk.architecture.task.GetLastMessagesTask
import com.newapi.apivk.architecture.task.GetLongPoolServerTask
import com.newapi.apivk.architecture.task.SendMessageTask
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import com.vk.api.sdk.objects.messages.Message
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select

class Main {

    @ExperimentalCoroutinesApi
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val transportClient = HttpTransportClient.getInstance()
            val vkApiClient = VkApiClient(transportClient)

            val token1 = "f6f4f4a03176295a12b1b0ada23d3b12e0b6264e4b590fc42bcbd7d429d3d16e6f4bd1f77778e20999e58"
            val userId1 = 294987132
            var newPts = 0
            val userActor = UserActor(userId1, token1)

            runBlocking {

                val getLongpoolParametersTask = async {
                    GetLongPoolServerTask().addParams(vkApiClient).execute(userActor)
                }

                launch(CoroutineName(userActor.id.toString())) {
                    while (true) {
                        val response = GetLastMessagesTask()
                                .addParams(getLongpoolParametersTask.await(), vkApiClient, newPts)
                                .execute(userActor)
                        newPts = response.newPts
                        delay(1000)
                        if (response.conversations.isNotEmpty()) {
                            val messageChannel = sendMessages(response.messages.items)

                            val messageWorkerA = makeMessageWorker("messageWorker-1", messageChannel)
                            val messageWorkerB = makeMessageWorker("messageWorker-2", messageChannel)

                            var isMessageWorkerAActive = true
                            var isMessageWorkerBActive = true
                            while (isMessageWorkerAActive || isMessageWorkerBActive) {
                                select<Unit> {
                                    if (isMessageWorkerAActive) {
                                        messageWorkerA.onReceiveOrNull { message ->
                                            if (messageWorkerA.isClosedForReceive) isMessageWorkerAActive = false
                                            if (message != null) {
                                                if (message.fromId != userActor.id) {
                                                    SendMessageTask().addParams(message.text, message.peerId, vkApiClient).execute(userActor)
                                                }
                                            }
                                        }
                                    }
                                    if (isMessageWorkerBActive) {
                                        messageWorkerB.onReceiveOrNull { message ->
                                            if (messageWorkerB.isClosedForReceive) isMessageWorkerBActive = false
                                            if (message != null) {
                                                if (message.fromId != userActor.id) {
                                                    SendMessageTask().addParams(message.text, message.peerId, vkApiClient).execute(userActor)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        fun CoroutineScope.sendMessages(messages: List<Message>) =
                produce(CoroutineName("MessageQueue")) {
                    for (message in messages) send(message)
                }

        // convert this to a producer of completed coffee orders
        private fun CoroutineScope.makeMessageWorker(tag: String, messages: ReceiveChannel<Message>) = produce(CoroutineName(tag)) {
            for (message in messages) {
                send(message)
            }
        }
    }
}