# The8CloudSDK Integration guide

## Overview

*The8CloudSDK* delivers sponsorships to users via native ad placement or in app alerts. This takes the user through the simple UX of receiving a direct sponsorship, sharing across their social and getting paid. Once users have accepted a sponsorship, they may enter the Sponsorship Feed at any time to manage ongoing offers and see new ones at any time via a customizable button. This button is exposed only after they have received their first offer. The button placement is entirely at host app discretion.

## Prerequisites & Assumptions

The SDK has the following requirements:
* Minimum Android version support: 4.1

The following items should be handled to ensure a fully functioning SDK:

 * Facebook SDK framework must be added to your application
 * Twitter and Facebook applications are created and configured with proper permissions so that App can authenticate users, read basic user profiles, post to Twitter, read post engagements.
 * Facebook permissions "user_posts", "email", "public_profile", "read_insights", "user_friends", "pages_show_list" (See more about Facebook permissions [here](https://developers.facebook.com/docs/facebook-login/permissions))
 * See more about setup of Twitter app [here](https://developer.twitter.com/)

## Installation

Add file **the8sdk.aar** to your project */libs* directory

Add following lines to module-level gradle file:

    dependencies {
        api fileTree(include: ['*.aar'], dir: 'libs')
        ...
    }

Add third party libraries:

    dependencies {
    ...

        //Google
        implementation 'com.google.android.material:material:1.0.0'
        implementation 'com.android.support:multidex:1.0.3'

        // Google play services
        implementation 'com.google.android.gms:play-services-auth:16.0.1'
        implementation 'com.google.android.gms:play-services-gcm:16.0.0'
        implementation 'com.google.android.gms:play-services-base:16.0.1'
        implementation 'com.google.android.gms:play-services-identity:16.0.0'

        implementation ('com.google.firebase:firebase-core:16.0.5')
        implementation ('com.google.firebase:firebase-messaging:17.3.4')

        // Glide image loading
        implementation 'com.github.bumptech.glide:glide:4.8.0'
        implementation 'com.github.bumptech.glide:okhttp-integration:4.8.0'
        implementation 'jp.wasabeef:glide-transformations:3.3.0'

        // RxJava
        implementation 'io.reactivex.rxjava2:rxjava:2.2.2'
        implementation 'io.reactivex.rxjava2:rxandroid:2.1.0'
        implementation 'io.reactivex.rxjava2:rxkotlin:2.3.0'

        // Retrofit
        implementation 'com.squareup.retrofit2:retrofit:2.4.0'
        implementation 'com.squareup.okhttp3:okhttp:3.11.0'
        implementation 'com.squareup.okhttp3:logging-interceptor:3.11.0'
        implementation 'com.squareup.retrofit2:converter-gson:2.4.0'
        implementation 'com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0'

        // Gson
        implementation 'com.google.code.gson:gson:2.8.2'

        // Facebook, Twitter
        implementation 'com.facebook.android:facebook-android-sdk:4.37.0'
        implementation('com.twitter.sdk.android:twitter:3.2.0@aar') {
            transitive = true
        }

        //Kotlin
        implementation "org.jetbrains.kotlin:kotlin-stdlib:1.3.11"
        implementation 'net.yslibrary.keyboardvisibilityevent:keyboardvisibilityevent:2.1.0'
        implementation "org.jetbrains.anko:anko-cardview-v7:0.10.7"
        implementation "org.jetbrains.anko:anko-sdk15:0.10.7"
        implementation "org.jetbrains.anko:anko-support-v4-commons:0.10.7"

        //Other
        implementation 'net.yslibrary.keyboardvisibilityevent:keyboardvisibilityevent:2.1.0'
        implementation 'com.jakewharton.timber:timber:4.5.1'
    }

## Setup Facebook

 Facebook SDK requires to add the following lines into your Manifest.xml

    <meta-data
        android:name="com.facebook.sdk.ApplicationId"
        android:value="@string/facebook_app_id" />

## Integration

### Initialization
  Before usage, the SDK should be initialized with your Application ID. You should set all necessary API keys for social networks.

  Add this into your Application.onCreate() method:

        The8CloudSdkInfo the8CloudSdkInfo = new The8CloudSdkInfo();
        the8CloudSdkInfo.hostAppId = HOST_APP_ID;
        the8CloudSdkInfo.setTwitterKeys(TWITTER_KEY, TWITTER_SECRET);
        the8CloudSdkInfo.youtubeKey = YOUTUBE_KEY;

        The8CloudSdk.initSdk(this, the8CloudSdkInfo);

  And when you are ready to use SDK in your actvity call

        The8CloudSdk.login(this, () -> {
          });

 Each time you calls **The8CloudSdk.login** SDK tries to setup it’s internal state. When this process finished SDK sends **The8CloudSDKStatusChangedNotification**.

 Pass the **The8CloudSdkStateChangeListener** to **The8CloudSdk.login**  and in callback check state of SDK with functions:
 - **The8CloudSdk.isSdkReady** - ready/not-ready (if not ready you can hide your SposorHub button for opening of SponsorHub)
 - **The8CloudSdk.isSdkEnabled** - if false, it means that SDK was not initialized correctly or SDK usage has been disabled on backend side
 - **The8CloudSdk.isSdkNativeAdChannelEnabled** - if false, it means that you should not load (and display) offers as Native Ads.

 After initializing, the SDK is ready for use.

 To present offers list UI call next method of **The8CloudSdk** in your Activity:

    The8CloudSdk.openSponsorsHub(context);

### Targeting

Our SDK supports targeting for ads.
You should use

        the8CloudSdkInfo.targetingInfo = new The8CloudTargetingInfo();
        the8CloudSdkInfo.targetingInfo.gender = "male";
        the8CloudSdkInfo.targetingInfo.followersCount = 666;

before SDK initialization.

or you can set it when you are requesting native ad

        val targeting = The8CloudTargetingInfo()
        targeting.age = 35
        The8CloudSdk.getNativeAds(1, 2, targeting, listener)


### UI Customization

 SDK permits to change a lot of UI details like fonts, colors, intro pages, showing/hiding UI elements.

* Change background for main feed

        the8CloudSdkInfo.customBackgroundResourceId = R.drawable.background;
* Set your own text and images for intro pages

        IntroDataList idata = new IntroDataList();
        idata.add(new IntroData(R.drawable.intro1, "On Triller, you make awesome videos…now you can get sponsored by brands that love you!", ""));
        idata.add(new IntroData(R.drawable.intro2, "Get rewarded for the amazing things you dream up!", ""));
        idata.add(new IntroData(R.drawable.intro3, "So let your ideas fly!", ""));
        the8CloudSdkInfo.introData = idata;

* Set your fonts for different text styles

        The8CloudSdk.setCustomFont(The8CloudFont.BLACK, Typeface.createFromAsset(getAssets(), "Gotham_Black.otf"));

* Set your accent color

        the8CloudSdkInfo.mainColor = getResources().getColor(R.color.colorMain);

* We have some design presets for certain clients which could be activated

        the8CloudSdkInfo.design = SDKConstants.DESIGN_TRILLER;

### Functionality customization

SDK allows to extend its functionality

* If you connect SDK to Triller app you could set user identifiers

with initialization

        the8CloudSdkInfo.trillerUserId = "userid";
        the8CloudSdkInfo.trillerUserName = "username";
        the8CloudSdkInfo.trillerUserAvatar = "https://...avatar.jpg";

or later by dirrect call

        The8CloudSdk.setTrillerUserInfo("userId", "username", "https://...avatar.jpg")


* Set your own content creator tool

        The8CloudSdk.registerCustomContentCreator("My", new Intent(this, ContentWithUrl.class));
        The8CloudSdk.registerCustomContentCreator("My (File)", Intent(this, ContentAsFile::class.java))

    This Activities will be started when user selects your tool and you should provide functionality to create content and return it as result like this:

        setResult(RESULT_OK, Intent()
            .putExtra(SDKConstants.CONTENT_URL, "https://.....mp4")
            .putExtra(SDKConstants.CONTENT_CAPTION, "Nice caption")
            .putExtra(SDKConstants.CONTENT_THUMBNAIL, "https://......jpg"))

    or this

        setResult(RESULT_OK, Intent()
            .putExtra(SDKConstants.CONTENT_CAPTION, "Nice caption")
            .putExtra(SDKConstants.CONTENT_FILE_NAME, uri.toString()))

* Specialy for Triller we have registration of posting tools.

        The8CloudSdk.setTrillerPostingTool(new Intent(this, PostToTriller.class));

  This intent will be started when user posts to Triller and there you will get all info in fileds

          intent.getStringExtra(SDKConstants.TRILLER_CONTENT_URL)
          intent.getStringExtra(SDKConstants.TRILLER_CONTENT_CAPTION)
          intent.getLongExtra(SDKConstants.CONTENT_CAMPAIGN_ID, 0L)
          intent.getLongExtra(SDKConstants.CONTENT_OFFER_ID, 0L)

  After posting completed you should return result with post url and id:

        setResult(RESULT_OK, Intent()
           .putExtra(SDKConstants.TRILLER_POST_URL, "https://v.triller.co/....")
           .putExtra(SDKConstants.TRILLER_POST_ID, "12345ABCDE"))

* Add you own button to SDK toolbar

        The8CloudSdk.addToolbarButton(R.drawable.custom_settings, v -> openSettings());

* Catch the events about user actions

        The8CloudSdk.registerEventsListener(new The8CloudEventsListener() {
            @Override
            public void onOfferAccepted(long campaignId) {
            }

            @Override
            public void onOfferDeclined(long campaignId) {
            }
        });

* Catch the logs

        The8CloudSdk.registerLogListener(...);

## Native Ads

SDK feed could be received as a native ad. You just should call

                The8CloudTargetingInfo targeting = new The8CloudTargetingInfo()
                targeting.age = 35
                The8CloudSdk.getNativeAds(1, 2, targeting, listener)

And show recieved info as you want, where you want.

The result of request will be a list of *The8CloudNativeAdContent* items.
Use it to populate your native ad view. There you could use fields:

        nativeAdContent.offer.nativeAdMediaSubtitle
        nativeAdContent.offer.nativeAdMediaTitle
        nativeAdContent.offer.brand
        nativeAdContent.offer.postMedia
        nativeAdContent.offer.fullscreenTakeoverMedia
        nativeAdContent.offer.fullscreenTakeoverMediaWording
        nativeAdContent.offer.mandatoryPlatforms
        nativeAdContent.image

Their names say by themselfs

Don't forget to call

        The8CloudSdk.registerNativeAdView(view, nativeAdContent)

when you show some ad unit. We need it to track views.

## Additions

* Use *The8CloudSdk.isAnyOfferSubmitted()* to get information that user successfully interacted with SDK and accepted some offer
* Use *The8CloudSdk.openSponsorsHub(context)* to open SDK main UI screen
* Use *The8CloudSdk.showSocialAccounts(context)* to open page where user could connect them cosial accounts
* Use *The8CloudSdk.showSettings(context)* to open page where user could fill up his personal info
* Use *The8CloudSdk.showPaypalInfo(context)* to open page where user could fill up his paypal account e-mail
* Use *The8CloudSdk.feedbackSupport(...)* to let user to make a feedback
* Use *The8CloudSdk.setPushNotificationStatus* and *The8CloudSdk.getPushNotificationStatus* to operate with notifications
* use *The8CloudSdk.setDeepLink(context, data)* to pass the incoming deep links connected with our SDK. It is necessary for push notifications

## Users with flagged content

If your user has flagged content you can inform our SDK about it
Just add this info into Targetting params when you request Native ad

        targeting.hasFlaggedContent = true

or set it when you initializing sdk:

        the8CloudSdkInfo.hasFlaggedContent = true;

or

        The8CloudSdk.setTrillerUserInfo(..., true)

