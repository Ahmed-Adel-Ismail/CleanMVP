package com.base.abstraction.system;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * a Class to access all Application Resources via Application context
 * <p>
 * Created by Ahmed Adel on 8/31/2016.
 */
@SuppressWarnings("ALL")
public class AppResources extends Resources {

    private AppResources() {
        super(initAssetManager(), initDisplayMetrics(), initConfiguration());
    }

    private static Configuration initConfiguration() {
        return App.getInstance().getResources().getConfiguration();
    }

    private static AssetManager initAssetManager() {
        return App.getInstance().getResources().getAssets();
    }

    private static DisplayMetrics initDisplayMetrics() {
        return App.getInstance().getResources().getDisplayMetrics();
    }

    public static AppResources getResources() {
        return new AppResources();
    }

    public static CharSequence text(int id) throws NotFoundException {
        return getResources().getText(id);
    }

    public static CharSequence quantityText(int id, int quantity)
            throws NotFoundException {
        return getResources().getQuantityText(id, quantity);
    }

    public static String string(int id) throws NotFoundException {

        return getResources().getString(id);
    }

    public static String string(int id, Object... formatArgs)
            throws NotFoundException {

        return getResources().getString(id, formatArgs);
    }

    public static String quantityString(int id, int quantity,
                                        Object... formatArgs) throws NotFoundException {

        return getResources().getQuantityString(id, quantity, formatArgs);
    }

    public static String quantityString(int id, int quantity)
            throws NotFoundException {

        return getResources().getQuantityString(id, quantity);
    }

    public static CharSequence text(int id, CharSequence def) {

        return getResources().getText(id, def);
    }

    public static CharSequence[] textArray(int id) throws NotFoundException {

        return getResources().getTextArray(id);
    }

    public static String[] stringArray(int id) throws NotFoundException {

        return getResources().getStringArray(id);
    }

    public static int[] intArray(int id) throws NotFoundException {

        return getResources().getIntArray(id);
    }

    public static TypedArray typedArray(int id) throws NotFoundException {

        return getResources().obtainTypedArray(id);
    }

    public static float dimension(int id) throws NotFoundException {

        return getResources().getDimension(id);
    }

    public static int dimensionPixelOffset(int id) throws NotFoundException {

        return getResources().getDimensionPixelOffset(id);
    }

    public static int dimensionPixelSize(int id) throws NotFoundException {

        return getResources().getDimensionPixelSize(id);
    }

    public static float fraction(int id, int base, int pbase) {

        return getResources().getFraction(id, base, pbase);
    }

    public static Drawable drawable(int id) throws NotFoundException {

        return getResources().getDrawable(id);
    }

    public static Drawable drawableForDensity(int id, int density)
            throws NotFoundException {

        return getResources().getDrawableForDensity(id, density);
    }

    public static Movie movie(int id) throws NotFoundException {

        return getResources().getMovie(id);
    }

    public static int color(int id) throws NotFoundException {

        return getResources().getColor(id);
    }

    public static ColorStateList colorStateList(int id)
            throws NotFoundException {

        return getResources().getColorStateList(id);
    }

    public static boolean bool(int id) throws NotFoundException {

        return getResources().getBoolean(id);
    }

    public static int integer(int id) throws NotFoundException {

        return getResources().getInteger(id);
    }

    public static XmlResourceParser layout(int id) throws NotFoundException {

        return getResources().getLayout(id);
    }

    public static XmlResourceParser animation(int id) throws NotFoundException {

        return getResources().getAnimation(id);
    }

    public static XmlResourceParser xml(int id) throws NotFoundException {

        return getResources().getXml(id);
    }

    public static InputStream rawResource(int id) throws NotFoundException {

        return getResources().openRawResource(id);
    }

    public static InputStream rawResource(int id, TypedValue value)
            throws NotFoundException {

        return getResources().openRawResource(id, value);
    }

    public static AssetFileDescriptor rawResourceFd(int id)
            throws NotFoundException {

        return getResources().openRawResourceFd(id);
    }

    public static void value(int id, TypedValue outValue, boolean resolveRefs)
            throws NotFoundException {

        getResources().getValue(id, outValue, resolveRefs);
    }

    public static void valueForDensity(int id, int density,
                                       TypedValue outValue, boolean resolveRefs) throws NotFoundException {

        getResources().getValueForDensity(id, density, outValue, resolveRefs);
    }

    public static void value(String name, TypedValue outValue,
                             boolean resolveRefs) throws NotFoundException {

        getResources().getValue(name, outValue, resolveRefs);
    }

    public static TypedArray attributes(AttributeSet set, int[] attrs) {

        return getResources().obtainAttributes(set, attrs);
    }

    public static void updateConfig(Configuration config, DisplayMetrics metrics) {

        getResources().updateConfiguration(config, metrics);
    }

    public static DisplayMetrics displayMetrics() {

        return getResources().getDisplayMetrics();
    }

    public static Configuration configuration() {

        return getResources().getConfiguration();
    }

    public static int identifier(String name, String defType, String defPackage) {

        return getResources().getIdentifier(name, defType, defPackage);
    }

    public static String resourceName(int resid) throws NotFoundException {

        return getResources().getResourceName(resid);
    }

    public static String resourcePackageName(int resid)
            throws NotFoundException {

        return getResources().getResourcePackageName(resid);
    }

    public static String resourceTypeName(int resid) throws NotFoundException {

        return getResources().getResourceTypeName(resid);
    }

    public static String resourceEntryName(int resid) throws NotFoundException {
        return getResources().getResourceEntryName(resid);
    }

    public static void bundleExtras(XmlResourceParser parser, Bundle outBundle)
            throws XmlPullParserException, IOException {
        getResources().parseBundleExtras(parser, outBundle);
    }

    public static void bundleExtra(String tagName, AttributeSet attrs,
                                   Bundle outBundle) throws XmlPullParserException {
        getResources().parseBundleExtra(tagName, attrs, outBundle);
    }

    /**
     * get the {@code String} value that has the same name in the {@code R} file like
     * the passed {@code id}
     *
     * @param resourceId the resourceId value that has the same name as the {@code String} name
     * @return the {@code String} value
     */
    public static String stringWithSameId(int resourceId) {
        String resourceName = AppResources.resourceEntryName(resourceId);
        return string(resourceName);
    }

    public static String string(String resourceName) {
        int resourceId = stringId(resourceName);
        return getResources().getString(resourceId);
    }

    /**
     * get the {@code String} resource id by a given name
     *
     * @param resourceName the name of the resource id
     * @return the {@code String} resource id itself
     */
    @StringRes
    public static int stringId(String resourceName) {
        return getResources().getIdentifier(resourceName, "string",
                App.getInstance().getPackageName());
    }

    /**
     * get the resource id by a given name
     *
     * @param resourceName the name of the resource id
     * @return the resource id itself
     */
    @IdRes
    public static int id(String resourceName) {
        return getResources().getIdentifier(resourceName, "id", App.getInstance().getPackageName());

    }

    /**
     * get the resource layout id by a given name
     *
     * @param resourceName the name of the layout
     * @return the resource id itself
     */
    @LayoutRes
    public static int layout(String resourceName) {
        return getResources().getIdentifier(resourceName, "layout", App.getInstance().getPackageName());

    }


}
