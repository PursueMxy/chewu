package com.weimu.chewu.utils.yuyin;

import com.baidu.tts.client.TtsMode;

/**
 * Created by huangjinfu on 18/8/14.
 */

public class YuYinConfig {
    // ================== 初始化参数设置开始 ==========================
    /**
     * 发布时请替换成自己申请的appId appKey 和 secretKey。注意如果需要离线合成功能,请在您申请的应用中填写包名。
     * 本demo的包名是com.baidu.tts.sample，定义在build.gradle中。
     */
    public static final String appId = "11676588";

    public static final String appKey = "sVrzSrTqG76xLyHKtLXExyut";

    public static final String secretKey = "mwB5KTy4TVLNChlacmj4021ALuUuPTfq";

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    public static final TtsMode ttsMode = TtsMode.ONLINE;

    // 离线发音选择，VOICE_FEMALE即为离线女声发音。
    // assets目录下bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat为离线男声模型；
    // assets目录下bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat为离线女声模型
    public static final String offlineVoice = OfflineResource.VOICE_MALE;

    // ===============初始化参数设置完毕，更多合成参数请至getParams()方法中设置 =================
}
