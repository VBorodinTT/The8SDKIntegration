package sdk.integration;

import android.app.Application;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import com.weare8.android.sdk.*;
import com.weare8.android.ui.sponsorHub.onboarding.IntroData;
import com.weare8.android.ui.sponsorHub.onboarding.IntroDataList;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        init8Sdk();
    }

    void init8Sdk() {
        The8CloudSdkInfo the8CloudSdkInfo = new The8CloudSdkInfo();
        the8CloudSdkInfo.hostAppId = "HOST_APP_ID";
        the8CloudSdkInfo.setTwitterKeys("TWITTER_KEY", "TWITTER_SECRET");
        the8CloudSdkInfo.youtubeKey = "YOUTUBE_KEY";
        the8CloudSdkInfo.trillerUserId = "1";
        the8CloudSdkInfo.trillerUserName = "2";
        the8CloudSdkInfo.trillerUserAvatar = "https://3.jpg";

//        the8CloudSdkInfo.customAPI = BuildConfig.API;
        the8CloudSdkInfo.customBackgroundResourceId = R.drawable.background;
        the8CloudSdkInfo.design = SDKConstants.DESIGN_TRILLER;

        IntroDataList idata = new IntroDataList();
        idata.add(new IntroData(R.drawable.intro1, "On Triller, you make awesome videos…now you can get sponsored by brands that love you!", ""));
        idata.add(new IntroData(R.drawable.intro2, "Get rewarded for the amazing things you dream up!", ""));
        idata.add(new IntroData(R.drawable.intro3, "So let your ideas fly!", ""));

        the8CloudSdkInfo.introData = idata;

        The8CloudSdk.setCustomFont(The8CloudFont.BLACK, Typeface.createFromAsset(getAssets(), "Gotham_Black.otf"));
        The8CloudSdk.setCustomFont(The8CloudFont.LIGHT, Typeface.createFromAsset(getAssets(), "SF_Pro_Display_Light.otf"));
        The8CloudSdk.setCustomFont(The8CloudFont.REGULAR, Typeface.createFromAsset(getAssets(), "SF_Pro_Display_Regular.otf"));
        The8CloudSdk.setCustomFont(The8CloudFont.BOLD, Typeface.createFromAsset(getAssets(), "SF_Pro_Display_Bold.otf"));

        The8CloudSdk.registerCustomContentCreator("Triller", new Intent(this, ContentWithUrl.class));
//        The8CloudSdk.registerCustomContentCreator("Triller (File)", Intent(this, ContentAsFile::class.java))
        The8CloudSdk.setTrillerPostingTool(new Intent(this, PostToTriller.class));
        The8CloudSdk.addToolbarButton(R.drawable.custom_settings, v -> startActivity(SettingsActivity.Companion.newInstance(App.this)));
        The8CloudSdk.registerEventsListener(new The8CloudEventsListener() {
            @Override
            public void onOfferAccepted(long campaignId) {
                Log.e("test", "Offer " + campaignId + " accepted");
            }

            @Override
            public void onOfferDeclined(long campaignId) {
                Log.e("test", "Offer " + campaignId + " declined");
            }
        });

        The8CloudSdk.initSdk(this, the8CloudSdkInfo, () -> {
        });
    }

}
