package sdk.integration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.weare8.android.sdk.SDKConstants

class PostToTriller : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("test", "PostToTriller called")
        Log.e("test", "PostToTriller url ${intent.getStringExtra(SDKConstants.TRILLER_CONTENT_URL)}")
        Log.e("test", "PostToTriller caption ${intent.getStringExtra(SDKConstants.TRILLER_CONTENT_CAPTION)}")
        Log.e("test", "PostToTriller campaign id ${intent.getLongExtra(SDKConstants.CONTENT_CAMPAIGN_ID, 0L)}")
        Log.e("test", "PostToTriller offer id ${intent.getLongExtra(SDKConstants.CONTENT_OFFER_ID, 0L)}")

        setResult(RESULT_OK, Intent()
                .putExtra(SDKConstants.TRILLER_POST_URL, "https://v.triller.co/mXnL0l")
                .putExtra(SDKConstants.TRILLER_POST_ID, "12345ABCDE"))

        finish()
    }
}