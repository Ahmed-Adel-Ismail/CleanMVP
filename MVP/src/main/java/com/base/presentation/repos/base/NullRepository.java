package com.base.presentation.repos.base;

import com.base.abstraction.R;
import com.base.usecases.annotations.RequestsHandler;
import com.base.usecases.annotations.ResponsesHandler;

/**
 * a {@code null} representation for a {@link Repository}, this is a dummy class
 * <p>
 * Created by Ahmed Adel on 11/23/2016.
 */
@RequestsHandler(NullRepositoryRequestsHandler.class)
@ResponsesHandler(NullRepositoryResponsesHandler.class)
public class NullRepository extends Repository {

    @Override
    public final long getActorAddress() {
        return R.id.addressNullRepository;
    }
}
