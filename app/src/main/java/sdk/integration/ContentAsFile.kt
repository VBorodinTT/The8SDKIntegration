package sdk.integration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.weare8.android.sdk.SDKConstants

class ContentAsFile : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "video/*"
        startActivityForResult(photoPickerIntent, 111)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 111) {
            val uri = data?.data

            setResult(RESULT_OK, Intent()
                    .putExtra(SDKConstants.CONTENT_CAPTION, "Nice caption")
                    .putExtra(SDKConstants.CONTENT_FILE_NAME, uri.toString()))
            finish()
        }
    }
}