package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.UnityAds

object AdsManager {
    private const val GAME_ID = "6037529"
    private const val BANNER_ID = "Banner_Android"
    private const val INTERSTITIAL_ID = "Interstitial_Android"
    private const val REWARDED_ID = "Rewarded_Android"

    private var isInterstitialLoaded = false
    private var isRewardedLoaded = false

    fun initialize(context: Context) {
        UnityAds.initialize(context, GAME_ID, false, object : IUnityAdsInitializationListener {
            override fun onInitializationComplete() {
                Log.d("AdsManager", "Unity Ads initialized")
                loadInterstitial()
                loadRewarded()
            }
            override fun onInitializationFailed(error: UnityAds.UnityAdsInitializationError?, message: String?) {
                Log.e("AdsManager", "Unity Ads failed: $message")
            }
        })
    }

    private fun loadInterstitial() {
        UnityAds.load(INTERSTITIAL_ID, object : com.unity3d.ads.IUnityAdsLoadListener {
            override fun onUnityAdsAdLoaded(placementId: String) { isInterstitialLoaded = true }
            override fun onUnityAdsFailedToLoad(placementId: String, error: com.unity3d.ads.UnityAds.UnityAdsLoadError?, message: String?) { isInterstitialLoaded = false }
        })
    }

    private fun loadRewarded() {
        UnityAds.load(REWARDED_ID, object : com.unity3d.ads.IUnityAdsLoadListener {
            override fun onUnityAdsAdLoaded(placementId: String) { isRewardedLoaded = true }
            override fun onUnityAdsFailedToLoad(placementId: String, error: com.unity3d.ads.UnityAds.UnityAdsLoadError?, message: String?) { isRewardedLoaded = false }
        })
    }

    fun showInterstitial(activity: Activity) {
        if (isInterstitialLoaded) {
            UnityAds.show(activity, INTERSTITIAL_ID, object : com.unity3d.ads.IUnityAdsShowListener {
                override fun onUnityAdsShowStart(placementId: String) {}
                override fun onUnityAdsShowClick(placementId: String) {}
                override fun onUnityAdsShowComplete(placementId: String, state: com.unity3d.ads.UnityAds.UnityAdsShowCompletionState) {
                    isInterstitialLoaded = false
                    loadInterstitial()
                }
                override fun onUnityAdsShowFailure(placementId: String, error: com.unity3d.ads.UnityAds.UnityAdsShowError?, message: String?) {
                    isInterstitialLoaded = false
                    loadInterstitial()
                }
            })
        } else {
            loadInterstitial()
        }
    }

    fun showRewarded(activity: Activity) {
        if (isRewardedLoaded) {
            UnityAds.show(activity, REWARDED_ID, object : com.unity3d.ads.IUnityAdsShowListener {
                override fun onUnityAdsShowStart(placementId: String) {}
                override fun onUnityAdsShowClick(placementId: String) {}
                override fun onUnityAdsShowComplete(placementId: String, state: com.unity3d.ads.UnityAds.UnityAdsShowCompletionState) {
                    isRewardedLoaded = false
                    loadRewarded()
                }
                override fun onUnityAdsShowFailure(placementId: String, error: com.unity3d.ads.UnityAds.UnityAdsShowError?, message: String?) {
                    isRewardedLoaded = false
                    loadRewarded()
                }
            })
        } else {
            loadRewarded()
        }
    }
}
