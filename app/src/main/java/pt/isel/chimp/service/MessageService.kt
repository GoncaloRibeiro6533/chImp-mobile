package pt.isel.chimp.service

import android.os.Message

interface MessageService {

}
class MockMessageService : MessageService {

    private val messages =
        mutableListOf(
            Message()
        )



}