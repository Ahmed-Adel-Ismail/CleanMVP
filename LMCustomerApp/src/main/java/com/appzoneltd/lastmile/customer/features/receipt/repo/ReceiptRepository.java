package com.appzoneltd.lastmile.customer.features.receipt.repo;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.presentation.repos.base.Repository;
import com.base.usecases.annotations.RequestsHandler;
import com.base.usecases.annotations.ResponsesHandler;


/**
 * Created by Wafaa on 12/24/2016.
 */

@Address(R.id.addressReceiptRepository)
@RequestsHandler(ReceiptRepositoryRequestsHandler.class)
@ResponsesHandler(ReceiptRepositoryResponsesHandler.class)
public class ReceiptRepository extends Repository {

}
