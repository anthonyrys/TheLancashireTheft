package core;

import java.lang.reflect.Method;

public class Easings {
    public static Method getEasing(String name) {
        Method tween = null;
        try {
            tween = Easings.class.getMethod(name, double.class);

        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();

        }

        return tween;
    }

    public static double easeInSine(double progress) {
        return 1 - Math.cos((progress * Math.PI) / 2);
    }

    public static double easeInCubic(double progress) {
        return progress * progress * progress;
    }

    public static double easeInQuint(double progress) {
        return progress * progress * progress * progress;
    }

    public static double easeInCir(double progress) {
        return 1 - Math.sqrt(1 - Math.pow(progress, 2));
    }

    public static double easeOutSine(double progress) {
        return Math.sin((progress * Math.PI) / 2);
    }

    public static double easeOutCubic(double progress) {
        return 1 - Math.pow(1 - progress, 3);
    }

    public static double easeOutQuint(double progress) {
        return  1 - Math.pow(1 - progress, 5);
    }

    public static double easeOutCir(double progress) {
        return Math.sqrt(1 - Math.pow(progress - 1, 2));
    }
}
