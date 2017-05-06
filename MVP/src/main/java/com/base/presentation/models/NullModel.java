package com.base.presentation.models;

import com.base.abstraction.annotations.interfaces.UpdatesHandler;
import com.base.presentation.annotations.interfaces.Repository;
import com.base.usecases.annotations.ResponsesHandler;
import com.base.presentation.repos.base.NullRepository;

/**
 * an {@link Model} that represents a {@code null} value
 * <p/>
 * Created by Ahmed Adel on 9/18/2016.
 */
@UpdatesHandler(OnNullModelUpdatesHandler.class)
@ResponsesHandler(OnNullModelResponsesHandler.class)
@Repository(NullRepository.class)
public final class NullModel extends Model {


}
