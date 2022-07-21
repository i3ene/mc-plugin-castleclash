package de.nanoinsel.castleclash.utils;

import org.bukkit.ChatColor;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class FieldAccessor {

    public static void changeField(Class<?> c, String f, Object v) {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe) unsafeField.get(null);

            Field field = c.getDeclaredField(f);
            Object staticFieldBase = unsafe.staticFieldBase(field);
            long staticFieldOffset = unsafe.staticFieldOffset(field);
            unsafe.putObject(staticFieldBase, staticFieldOffset, v);
        } catch (Exception ex) {
            System.out.println(ChatColor.RED + "" + ex);
        }
    }

    public static Object changeMethod(Class<?> c, String m, Object o, Object... args) {
        try {
            Class[] argTypes = Arrays.stream(args).map(x -> x.getClass()).toArray(Class[]::new);
            Method method = c.getDeclaredMethod(m, argTypes);
            method.setAccessible(true);
            return method.invoke(o, args);
        } catch (Exception ex) {
            System.out.println(ChatColor.RED + "" + ex);
        }
        return null;
    }

}
