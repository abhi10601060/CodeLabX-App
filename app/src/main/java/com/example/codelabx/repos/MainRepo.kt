package com.example.codelabx.repos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class MainRepo  : WebSocketListener() {

    private lateinit var webSocket: WebSocket

     private var stdoutLiveData = MutableLiveData<String>("initial")
     val stdout : LiveData<String>
        get() = stdoutLiveData

    fun setWebSocket(ws: WebSocket){
        webSocket = ws
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Log.d("ABHI", "onMessage: $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d("ABHI", "onMessage: $text")
        this.stdoutLiveData.postValue(text)
        Log.d("WEBSOCKET", "text in live: " + this.stdoutLiveData.value)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        Log.d("ABHI", "onMessage: $bytes")
//        this.stdoutLiveData.value = bytes.toString()
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Log.d("ABHI", "onMessage: ${response.message}")
    }
}