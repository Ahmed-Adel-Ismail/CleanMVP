package com.appzoneltd.lastmile.customer.features.pickup.recipient;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;

/**
 * invalidate spinners of services ,  types , countries and cities
 * <p>
 * Created by Wafaa on 10/4/2016.
 */
class RecipientSpinnersInvalidator extends RecipientDetailsInvalidator {

    RecipientSpinnersInvalidator(RecipientDetailsViewModel viewModel) {
        super(viewModel);
        Command<View, Void> command = createOnInvalidateShippingSpinnerCommand();
        put((long) R.id.shipping_service, command);
        command = createOnInvalidateServiceTypesSpinnerCommand();
        put((long) R.id.shipment_service_type, command);
        command = createOnInvalidateCountrySpinnerCommand();
        put((long) R.id.country, command);
        command = createOnInvalidateCitySpinnerCommand();
        put((long) R.id.city, command);
    }

    private Command<View, Void> createOnInvalidateShippingSpinnerCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                if (getViewModel().getShippingServiceArray() != null
                        && getViewModel().isValidateShippingService()) {
                    invalidateShippingServiceSpinner((Spinner) view);
                }
                return null;
            }

            private void invalidateShippingServiceSpinner(Spinner spinner) {
                ArrayAdapter<String> spinnerArrayAdapter = getViewModel().getSpinnerManager()
                        .createSpinnerAdapter(getViewModel().getShippingServiceArray());
                spinner.setAdapter(spinnerArrayAdapter);
                spinner.setSelection(getViewModel().getServiceSelectedPosition());
                spinner.setOnItemSelectedListener(getViewModel().getEventListener());
                spinner.setOnTouchListener(getViewModel().getEventListener());
            }

        };
    }

    private Command<View, Void> createOnInvalidateServiceTypesSpinnerCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                if (getViewModel().getShippingServiceTypesArray() != null) {
                    invalidateServiceTypesSpinner((Spinner) view);
                }
                return null;
            }

            private void invalidateServiceTypesSpinner(Spinner spinner) {
                ArrayAdapter<String> spinnerArrayAdapter =
                        getViewModel().getSpinnerManager().createSpinnerAdapter(getViewModel()
                                .getShippingServiceTypesArray());
                spinner.setAdapter(spinnerArrayAdapter);
                spinner.setSelection(getViewModel().getServiceTypeSelectedPosition());
                spinner.setOnItemSelectedListener(getViewModel().getEventListener());
                spinner.setOnTouchListener(getViewModel().getEventListener());
                spinner.setFocusable(true);
                spinner.setFocusableInTouchMode(true);
            }
        };
    }

    private Command<View, Void> createOnInvalidateCountrySpinnerCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                if (getViewModel().getCountriesArray() != null) {
                    invalidateCountrySpinner((Spinner) view);
                }
                return null;
            }

            private void invalidateCountrySpinner(Spinner spinner) {
                ArrayAdapter<String> spinnerArrayAdapter =
                        getViewModel().getSpinnerManager()
                                .createSpinnerAdapter(getViewModel().getCountriesArray());
                spinner.setAdapter(spinnerArrayAdapter);
                spinner.setSelection(getViewModel().getCountrySelectedPosition());
                spinner.setOnItemSelectedListener(getViewModel().getEventListener());
                spinner.setOnTouchListener(getViewModel().getEventListener());
            }
        };
    }

    private Command<View, Void> createOnInvalidateCitySpinnerCommand() {
        return new Command<View, Void>() {
            @Override
            public Void execute(View view) {
                Spinner spinner = (Spinner) view;
                if (getViewModel().getCitiesArray() != null) {
                    invalidateCitySpinner(spinner);
                }
                return null;
            }

            private void invalidateCitySpinner(Spinner spinner) {
                ArrayAdapter<String> spinnerAdapter = getViewModel().getSpinnerManager()
                        .createSpinnerAdapter(getViewModel().getCitiesArray());
                spinner.setAdapter(spinnerAdapter);
                spinner.setSelection(getViewModel().getCitySelectedPosition());
                spinner.setOnItemSelectedListener(getViewModel().getEventListener());
                spinner.setOnTouchListener(getViewModel().getEventListener());
            }
        };
    }

}
