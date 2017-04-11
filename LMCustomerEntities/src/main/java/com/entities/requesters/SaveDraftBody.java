package com.entities.requesters;

/**
 * Created by Wafaa on 8/22/2016.
 */
public class SaveDraftBody {

    private long companyId;
    private long customerId;
    private long draftId;
    private String requestDraft;

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public void setRequestDraft(String requestDraft) {
        this.requestDraft = requestDraft;
    }


}
