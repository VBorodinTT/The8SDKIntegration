package sdk.integration

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import com.weare8.android.sdk.The8CloudSdk
import com.weare8.android.sdk.The8CloudSdkRequestListener

class SettingsActivity : Activity() {

    lateinit var notificationStatus: CheckBox
    lateinit var progressBar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        notificationStatus = findViewById(R.id.notification_switcher)
        progressBar = findViewById(R.id.progress_bar)

        findViewById<View>(R.id.offers).setOnClickListener { The8CloudSdk.openSponsorsHub(this@SettingsActivity) }
        findViewById<View>(R.id.socials).setOnClickListener { The8CloudSdk.showSocialAccounts(this@SettingsActivity) }
        findViewById<View>(R.id.paypal).setOnClickListener { The8CloudSdk.showPaypalInfo(this@SettingsActivity) }
        findViewById<View>(R.id.settings).setOnClickListener { The8CloudSdk.showSettings(this@SettingsActivity) }

        findViewById<View>(R.id.support).setOnClickListener {
            The8CloudSdk.feedbackSupport(
                    this@SettingsActivity,
                    object : The8CloudSdkRequestListener {
                        override fun onResult(p0: Any?) {
                            progressBar.visibility = View.GONE
                        }

                        override fun onError() {
                            progressBar.visibility = View.GONE
                            // show error message
                        }

                        override fun onStart() {
                            progressBar.visibility = View.VISIBLE
                        }
                    }
            )
        }

        findViewById<View>(R.id.receive_offers).setOnClickListener {
            The8CloudSdk.setPushNotificationStatus(
                    !notificationStatus.isChecked,
                    object : The8CloudSdkRequestListener {
                        override fun onResult(p0: Any?) {
                            progressBar.visibility = View.GONE
                            notificationStatus.isChecked = !notificationStatus.isChecked
                        }

                        override fun onError() {
                            progressBar.visibility = View.GONE
                            // show error message
                        }

                        override fun onStart() {
                            progressBar.visibility = View.VISIBLE
                        }
                    })
        }

        The8CloudSdk.getPushNotificationStatus(
                object : The8CloudSdkRequestListener {
                    override fun onResult(p0: Any?) {
                        progressBar.visibility = View.GONE
                        notificationStatus.isChecked = p0 as Boolean
                    }

                    override fun onError() {
                        progressBar.visibility = View.GONE
                        // show error message
                    }

                    override fun onStart() {
                        progressBar.visibility = View.VISIBLE
                    }
                })
    }

    companion object {

        fun newInstance(context: Context) = Intent(context, SettingsActivity::class.java)
    }
}