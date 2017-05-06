package com.base.presentation.base.presentation;

import com.base.abstraction.annotations.scanners.FieldAnnotationScanner;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.interfaces.Initializable;
import com.base.presentation.annotations.interfaces.Sync;
import com.base.presentation.models.Model;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * a class that holds the logic of updating / syncing between {@link Model} and {@link ViewModel}
 * <p>
 * Created by Ahmed Adel on 12/5/2016.
 */
class SyncTransactionsGroup implements
        Clearable,
        PresentationUpdater,
        Initializable<PresenterUpdater<?, ?, ?>> {

    private final Map<String, SyncTransaction> syncTransactions = new HashMap<>();
    private PresenterUpdater<?, ?, ?> presenterUpdater;

    @Override
    public void initialize(PresenterUpdater<?, ?, ?> presenterUpdater) {
        this.presenterUpdater = presenterUpdater;
        putViewModelSyncTransactions(presenterUpdater.getViewModel());
        putModelSyncTransactions(presenterUpdater.getModel());
    }


    private void putViewModelSyncTransactions(ViewModel viewModel) {
        new FieldAnnotationScanner<Sync>(Sync.class) {
            @Override
            protected void onAnnotationFound(Field element, Sync annotation) {
                String value = annotation.value();
                element.setAccessible(true);
                SyncTransaction transaction = syncTransactions.get(value);
                if (transaction == null) {
                    transaction = new SyncTransaction();
                    transaction.value = value;
                }
                transaction.viewModelField = element;
                syncTransactions.put(value, transaction);
            }
        }.execute(viewModel);
    }

    private void putModelSyncTransactions(Model model) {
        new FieldAnnotationScanner<Sync>(Sync.class) {
            @Override
            protected void onAnnotationFound(Field element, Sync annotation) {
                String value = annotation.value();
                element.setAccessible(true);
                SyncTransaction transaction = syncTransactions.get(value);
                if (transaction == null) {
                    transaction = new SyncTransaction();
                    transaction.value = value;
                }
                transaction.modelField = element;
                syncTransactions.put(value, transaction);
            }
        }.execute(model);
    }


    @Override
    public void clear() {
        syncTransactions.clear();
        presenterUpdater = null;
    }

    @Override
    public void updateModel() {
        ViewModel viewModel = presenterUpdater.getViewModel();
        Model model = presenterUpdater.getModel();
        for (SyncTransaction transaction : syncTransactions.values()) {
            transaction.onUpdateModel(model, viewModel);
        }
    }

    @Override
    public void updateViewModel() {
        ViewModel viewModel = presenterUpdater.getViewModel();
        Model model = presenterUpdater.getModel();
        for (SyncTransaction transaction : syncTransactions.values()) {
            transaction.onUpdateViews(model, viewModel);
        }
    }
}
