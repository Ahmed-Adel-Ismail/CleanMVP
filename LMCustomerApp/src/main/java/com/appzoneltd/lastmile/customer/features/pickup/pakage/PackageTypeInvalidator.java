package com.appzoneltd.lastmile.customer.features.pickup.pakage;

import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.system.AppResources;

/**
 * a {@link CommandExecutor} that contains {@link Command Commands} to invalidate the Package type
 * section in {@link PackageDetailsViewModel}
 * <p/>
 * Created by Ahmed Adel on 10/3/2016.
 */
class PackageTypeInvalidator extends CommandExecutor<Long, View> {


    private final int whiteColor = AppResources.color(R.color.white);
    private final int grayColor = AppResources.color(R.color.concrete);
    private SeekBar.OnSeekBarChangeListener onSeekBarChanged;

    private boolean seekBarEnabled;
    private int seekBarValue;
    private int boxWeightTextViewPosition;

    PackageTypeInvalidator() {
        Command<View, Void> command = createOnInvalidateSeekBar();
        put((long) R.id.package_details_seekbar, command);
        command = createOnInvalidateDocumentCommand();
        put((long) R.id.document, command);
        command = createOnInvalidateBoxCommand();
        put((long) R.id.box, command);
        command = createOnInvalidateBoxWeightValueCommand();
        put((long) R.id.package_details_box_weight_value, command);
        command = createOnInvalidateSmallestWeightCommand();
        put((long) R.id.smallest_weight, command);
        command = createOnInvalidateBiggestWeightCommand();
        put((long) R.id.biggest_weight, command);
    }

    private Command<View, Void> createOnInvalidateSeekBar() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                SeekBar seekBar = (SeekBar) view;
                seekBar.setEnabled(seekBarEnabled);
                seekBar.setOnSeekBarChangeListener(onSeekBarChanged);
                boxWeightTextViewPosition = getSeekBarTextValuePosition(seekBar);
                seekBar.setProgress(seekBarValue);
                return null;
            }

            private int getSeekBarTextValuePosition(SeekBar seekBar) {
                int left = seekBar.getLeft() + seekBar.getPaddingLeft();
                int right = seekBar.getRight() - seekBar.getPaddingRight();
                return (((right - left) * seekBar.getProgress()) / seekBar.getMax()) +
                        seekBar.getLeft();
            }
        };
    }

    private Command<View, Void> createOnInvalidateDocumentCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                RadioButton radioBtn = (RadioButton) view;
                radioBtn.setChecked(!seekBarEnabled);
                return null;
            }
        };
    }

    private Command<View, Void> createOnInvalidateBoxCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                RadioButton radioBtn = (RadioButton) view;
                radioBtn.setChecked(seekBarEnabled);
                return null;
            }
        };
    }


    private Command<View, Void> createOnInvalidateBoxWeightValueCommand() {
        return new BoxWeightValueInvalidator(this).setDelayMillis(20);
    }


    private Command<View, Void> createOnInvalidateSmallestWeightCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                TextView textView = (TextView) view;
                if (seekBarValue <= 1) {
                    textView.setTextColor(whiteColor);
                } else {
                    textView.setTextColor(grayColor);
                }
                return null;
            }
        };
    }

    private Command<View, Void> createOnInvalidateBiggestWeightCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                TextView textView = (TextView) view;
                if (seekBarValue == 25) {
                    textView.setTextColor(whiteColor);
                } else {
                    textView.setTextColor(grayColor);
                }
                return null;
            }
        };
    }

    void setSeekBarEnabled(boolean seekBarEnabled) {
        this.seekBarEnabled = seekBarEnabled;
    }

    void setSeekBarValue(int seekBarValue) {
        this.seekBarValue = seekBarValue;
    }

    void setOnSeekBarChanged(SeekBar.OnSeekBarChangeListener onSeekBarChanged) {
        this.onSeekBarChanged = onSeekBarChanged;
    }

    boolean isSeekBarEnabled() {
        return seekBarEnabled;
    }

    int getSeekBarValue() {
        return seekBarValue;
    }

    int getBoxWeightTextViewPosition() {
        return boxWeightTextViewPosition;
    }


}
