package com.base.presentation.base.presentation;

import com.base.abstraction.exceptions.TestException;
import com.base.presentation.references.Property;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.models.Model;

import java.lang.reflect.Field;

/**
 * a {@link Sync} transaction Object
 * <p>
 * Created by Ahmed Adel on 12/5/2016.
 */
class SyncTransaction {

    String value;
    Field viewModelField;
    Field modelField;


    void onUpdateModel(Model model, ViewModel viewModel) {
        if (viewModelField == null || modelField == null) {
            return;
        }
        try {
            Object newValue = viewModelField.get(viewModel);
            if (newValue instanceof Property) {
                updateModelProperty(model, (Property) newValue);
            } else {
                modelField.set(model, newValue);
            }
        } catch (IllegalAccessException e) {
            new TestException().execute(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void updateModelProperty(Model model, Property newValue) throws IllegalAccessException {
        Property modelProp = (Property) modelField.get(model);
        modelProp.set(newValue.get());
    }


    void onUpdateViews(Model model, ViewModel viewModel) {
        if (viewModelField == null || modelField == null) {
            return;
        }

        try {
            Object newValue = modelField.get(model);
            if (newValue instanceof Property) {
                updateViewModelProperty(viewModel, (Property) newValue);
            } else {
                viewModelField.set(viewModel, newValue);
            }

        } catch (IllegalAccessException e) {
            new TestException().execute(e);
        }

    }

    @SuppressWarnings("unchecked")
    private void updateViewModelProperty(ViewModel viewModel, Property newValue) throws IllegalAccessException {
        Property viewModelProp = (Property) viewModelField.get(viewModel);
        viewModelProp.set(newValue.get());
    }
}
