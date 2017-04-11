package com.appzoneltd.lastmile.driver.subfeatures;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.presentation.references.CheckedProperty;
import com.base.abstraction.system.AppResources;

/**
 * a class that generates a {@link Snackbar} ready to be displayed ... you will just need to
 * invoke {@link Snackbar#show()} ... if the {@link View} passed to the constructor is not
 * available any more, the {@link #execute(String)} method will throw
 * {@link CheckedReferenceClearedException}
 * <p>
 * Created by Ahmed Adel on 1/1/2017.
 */
public class SnackbarGenerator implements Command<String, Snackbar> {

    private final CheckedProperty<View> view = new CheckedProperty<>();

    public SnackbarGenerator(View view) {
        this.view.set(view);
    }

    @Override
    public Snackbar execute(String text) throws CheckedReferenceClearedException {
        SpannableStringBuilder stringBuilder = createWhiteSnackbarText(text);
        Snackbar snackbar = Snackbar.make(view.get(), stringBuilder, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(AppResources.color(R.color.colorPrimaryDark));
        return snackbar;
    }


    @NonNull
    private SpannableStringBuilder createWhiteSnackbarText(@NonNull String snackBarText) {
        int color = AppResources.color(android.R.color.white);
        ForegroundColorSpan whiteSpan = new ForegroundColorSpan(color);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(snackBarText);
        stringBuilder.setSpan(whiteSpan, 0, snackBarText.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return stringBuilder;
    }
}
