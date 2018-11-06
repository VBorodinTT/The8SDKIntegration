package sdk.integration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.weare8.android.sdk.SDKConstants

class ContentWithUrl : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("test", "ContentWithUrl campaign id ${intent.getLongExtra(SDKConstants.CONTENT_CAMPAIGN_ID, 0L)}")

        setResult(RESULT_OK, Intent()
                .putExtra(SDKConstants.CONTENT_URL, "https://www.bengalcats.co/wp-content/uploads/2017/03/bengal-kitten-cuddling.mp4")
                .putExtra(SDKConstants.CONTENT_CAPTION, "Nice caption")
                .putExtra(SDKConstants.CONTENT_THUMBNAIL, "https://www.peta.org/wp-content/uploads/2010/06/iStock_000008440542XSmall1.jpg"))
        finish()
    }
}