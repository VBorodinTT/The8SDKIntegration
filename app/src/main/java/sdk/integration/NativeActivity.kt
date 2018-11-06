package sdk.integration

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.weare8.android.nativeAd.The8CloudNativeAdContent
import com.weare8.android.nativeAd.The8CloudNativeAdListener
import com.weare8.android.sdk.The8CloudSdk

class NativeActivity : Activity() {

    lateinit var contents: LinearLayout
    private lateinit var listener: The8CloudNativeAdListener
    private lateinit var vSettingsButton: View
    private lateinit var vNotificationsBadge: View
    private lateinit var handler: Handler
    private lateinit var adRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_native)

        contents = findViewById(R.id.native_content)
        vSettingsButton = findViewById(R.id.view_settings)
        vNotificationsBadge = findViewById(R.id.view_badge)

//        vAllOffersButton.setOnClickListener { The8CloudSdk.openSponsorsHub(this@NativeActivity) }
        vSettingsButton.setOnClickListener { startActivity(SettingsActivity.newInstance(this)) }

        handler = Handler()

        adRunnable = object : Runnable {
            override fun run() {
                The8CloudSdk.getNativeAds(1, 2, listener)

                handler.postDelayed(this, 300000)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        contents.removeAllViews()
        vNotificationsBadge.visibility = View.INVISIBLE
        init8Sdk()
    }

    override fun onPause() {
        super.onPause()
        The8CloudSdk.clearBadgeListener()
        handler.removeCallbacks(adRunnable)
    }


    private fun setDeepLink() {
        val intent = intent
        val data = intent.data
        if (data != null && The8CloudSdk.isSdkEnabled()) {
            The8CloudSdk.setDeepLink(this, data.toString())
        }
    }

    private fun initBadges() {
        The8CloudSdk.registerBadgeListener {
            runOnUiThread {
                vNotificationsBadge.visibility = View.VISIBLE
            }
        }

        The8CloudSdk.checkNotificationsAvailable()

        The8CloudSdk.registerContentAttachedListener {
            Log.e("!TEST!", it.id.toString())
            Log.e("!TEST!", it.campaignId.toString())
            Log.e("!TEST!", it.mediaUrl)
        }
    }

    private fun init8Sdk() {
        initAdManager()
        setupAd()
        initBadges()
        setDeepLink()
    }


    private fun setupAd() {
        handler.post(adRunnable)

        val anyOfferSubmitted = The8CloudSdk.isAnyOfferSubmitted()
        vSettingsButton.visibility = if (anyOfferSubmitted) View.VISIBLE else View.GONE

    }

    private fun initAdManager() {
        listener = object : The8CloudNativeAdListener {
            override fun ready(content: ArrayList<The8CloudNativeAdContent>) {
                contents.removeAllViews()
                var view = View.inflate(this@NativeActivity, R.layout.content, null)
                contents.addView(view)
                for (nativeAdContent in content) {
                    view = View.inflate(this@NativeActivity, R.layout.content, null)
                    contents.addView(view)

                    view = View.inflate(this@NativeActivity, R.layout.native_ad, null)
                    contents.addView(view)

                    (view.findViewById<View>(R.id.native_ad_body) as TextView).text = nativeAdContent.offer.nativeAdMediaSubtitle
                    (view.findViewById<View>(R.id.native_ad_title) as TextView).text = nativeAdContent.offer.nativeAdMediaTitle
                    if (nativeAdContent.image.isNotEmpty())
                        Picasso.with(this@NativeActivity).load(nativeAdContent.image).into(view.findViewById<View>(R.id.native_ad_logo) as ImageView)
                    The8CloudSdk.registerNativeAdView(view, nativeAdContent)

                    view = View.inflate(this@NativeActivity, R.layout.content, null)
                    contents.addView(view)
                }
                view = View.inflate(this@NativeActivity, R.layout.content, null)
                contents.addView(view)

                val anyOfferSubmitted = The8CloudSdk.isAnyOfferSubmitted()
                vSettingsButton.visibility = if (anyOfferSubmitted) View.VISIBLE else View.GONE
            }

            override fun error() {
                contents.removeAllViews()
                var view = View.inflate(this@NativeActivity, R.layout.content, null)
                contents.addView(view)
                view = View.inflate(this@NativeActivity, R.layout.content, null)
                contents.addView(view)
            }
        }
    }
}
