package com.rbmsuggested;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    private Constants() {}
    static public final Map<String, Integer> TIMEZONES_FEATURES_ORDERED = new HashMap<String, Integer>(){{
        put("America/New_York", 449);
        put("America/Los_Angeles", 450);
        put("America/Chicago", 451);
        put("Europe/London", 452);
        put("Europe/Berlin", 453);
        put("America/Toronto", 454);
        put("America/Denver", 455);
        put("Australia/Sydney", 456);
        put("America/Vancouver", 457);
        put("America/Detroit", 458);
    }};
    static public final Integer[] FOOD_IDS_FEATURES_ORDERED = {
                2999,
                2997,
                3000,
                3002,
                3003,
                3004,
                3005,
                3006,
                3007,
                3008,
                3009,
                3010,
                3015,
                3012,
                3030,
                3016,
                3014,
                2995,
                3013,
                3017,
                3019,
                3020,
                3022,
                3024,
                3025,
                3027,
                3028,
                3031,
                2992,
                3011,
                2994,
                2996,
                2998,
                3080,
                3041,
                3043,
                3044,
                3045,
                3046,
                3047,
                3048,
                3049,
                3050,
                3051,
                3069,
                3055,
                3056,
                3057,
                3060,
                3061,
                3063,
                3035,
                3075,
                3038,
                3074,
                3067,
                3068,
                3036,
                3062,
                3081,
                3064,
                3066,
                3073,
                3077,
                3078,
                3079,
                3082,
                3083,
                3072,
                3039,
                3085,
                3086,
                3087,
                3088,
                3089,
                3090,
                3091,
                3094,
                3095,
                3097,
                3033,
                3034,
                3065,
                3105,
                3106,
                3107,
                3108,
                3109,
                3110,
                3112,
                3113,
                3114,
                3115,
                3117,
                3118,
                3121,
                3122,
                3124,
                3125,
                3126,
                3141,
                3102,
                3128,
                3127,
                3129,
                3130,
                3131,
                3134,
                3135,
                3136,
                3138,
                3139,
                3143,
                3146,
                3145,
                3101,
                3147,
                3148,
                3149,
                3151,
                3152,
                3154,
                3156,
                3153,
                3157,
                3158,
                3120,
                3099,
                3100,
                3140,
                3103,
                3123,
                3171,
                3179,
                3172,
                3173,
                3174,
                3177,
                3170,
                3178,
                3180,
                3175,
                3181,
                3182,
                3183,
                3184,
                3187,
                3168,
                3188,
                3189,
                3215,
                3191,
                3192,
                3193,
                3194,
                3196,
                3198,
                3199,
                3200,
                3201,
                3202,
                3218,
                3203,
                3204,
                3205,
                3207,
                3208,
                3209,
                3210,
                3211,
                3212,
                3213,
                3214,
                3219,
                3220,
                3190,
                3222,
                3223,
                3161,
                3164,
                3166,
                3169,
                3275,
                3274,
                3590,
                3592,
                3593,
                3591,
                3716,
                3725,
                3727,
                3730,
                3710,
                3707,
                3726,
                3724,
                3709,
                3714,
                3718,
                3729,
                3732,
                3721,
                3728,
                3731,
                3733,
                3711,
                3719,
                3712,
                3708,
                3715,
                3720,
                3704,
                3723,
                3735,
                3737,
                3759,
                3792,
                3739,
                3767,
                3754,
                3783,
                3762,
                3780,
                3786,
                3773,
                3750,
                3776,
                3775,
                3784,
                3787,
                3758,
                3761,
                3747,
                3785,
                3774,
                3748,
                3778,
                3771,
                3743,
                3782,
                3749,
                3753,
                3781,
                3768,
                3777,
                3757,
                3788,
                3763,
                3764,
                3740,
                3742,
                3760,
                3755,
                3793,
                3741,
                3745,
                3769,
                3746,
                3751,
                3770,
                3779,
                3736,
                3037,
                3589,
                3738,
                3703,
                3159,
                3098,
                3160,
                3765,
                3162,
                3717,
                3104,
                3185,
                3722,
                3587,
                3794,
                3132,
                3795,
                3032,
                3133,
                3272,
                3155,
                3744,
                3206,
                3217,
                3221,
                3752,
                3756,
                3772,
                3789,
                3070,
                3076,
                3092,
                3093,
                3111,
                3116,
                3119,
                3137,
                3705,
                3790,
                3706,
                3001,
                3018,
                3023,
                3713,
                3766,
                3791,
                5504,
                5505,
                5506,
                5508,
                5509,
                5510,
                5513,
                5514,
                5515,
                5516,
                5517,
                5519,
                5520,
                5521,
                5522,
                5523,
                5524,
                3176,
                3588,
                5507,
                5511,
                5512,
                5518,
                5527,
                5529,
                5530,
                5532,
                5533,
                5534,
                5535,
                5536,
                5537,
                5538,
                5539,
                5540,
                5541,
                5542,
                5543,
                5544,
                5546,
                5547,
                5548,
                5549,
                5550,
                5551,
                5552,
                5553,
                5554,
                5555,
                5556,
                5557,
                5558,
                5559,
                5560,
                5561,
                5562,
                5563,
                5564,
                5565,
                5566,
                5567,
                5569,
                5570,
                5571,
                5572,
                5573,
                5574,
                5575,
                5526,
                2991,
                3052,
                3096,
                3144,
                3186,
                3195,
                3273,
                5525,
                5528,
                5568,
                37361,
                37362,
                37363,
                37364,
                37365,
                37366,
                37367,
                37368,
                37369,
                37370,
                37371,
                37372,
                37373,
                37374,
                2993,
                3054,
                5531,
                37340,
                37341,
                37342,
                37343,
                37344,
                37345,
                37346,
                37347,
                37348,
                37349,
                37350,
                37351,
                37352,
                37353,
                37354,
                37355,
                37356,
                37357,
                37358,
                37359,
                37360
    };
}
