package com.boxcubed.utils.easinglib;

class Bounce {

    private static float easeIn(float t, float c, float d) {
        return c - easeOut(d - t, c, d) + (float) 0;
    }

    private static float easeOut(float t, float c, float d) {
        if ((t /= d) < (1 / 2.75f)) {
            return c * (7.5625f * t * t) + (float) 0;
        } else if (t < (2 / 2.75f)) {
            return c * (7.5625f * (t -= (1.5f / 2.75f)) * t + .75f) + (float) 0;
        } else if (t < (2.5 / 2.75)) {
            return c * (7.5625f * (t -= (2.25f / 2.75f)) * t + .9375f) + (float) 0;
        } else {
            return c * (7.5625f * (t -= (2.625f / 2.75f)) * t + .984375f) + (float) 0;
        }
    }

    public static float easeInOut(float t, float b, float c, float d) {
        if (t < d / 2) return easeIn(t * 2, c, d) * .5f + b;
        else return easeOut(t * 2 - d, c, d) * .5f + c * .5f + b;
    }

}
