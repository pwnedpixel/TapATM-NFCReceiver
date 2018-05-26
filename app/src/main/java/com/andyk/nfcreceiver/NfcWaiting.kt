package com.andyk.nfcreceiver

import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonReader
import kotlinx.android.synthetic.main.activity_nfc_waiting.*
import org.json.JSONObject

class NfcWaiting : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_waiting)
    }

    override fun onResume() {
        super.onResume()
        val intent = intent
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            val rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES)

            val message = rawMessages[0] as NdefMessage // only one message transferred
            mTextView.text = String(message.records[0].payload)

            val service = ServiceVolley()
            val apiController = APIController(service)

            val path = "/"
            val dataFromPhone= JSONObject(String(message.records[0].payload))

            apiController.post(path, dataFromPhone) { response ->
                System.out.println("response: " + response.toString())
                mTextView.text = (JSONObject(response.toString()).get("responseMessage")).toString()
            }

        } else
            mTextView.text = "Waiting for NDEF Message"
    }
}


