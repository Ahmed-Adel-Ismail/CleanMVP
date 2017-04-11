package com.entities.requesters;

import java.io.Serializable;

/**
 * The parent class for all entities used as POST parameter in requests from server
 * <p/>
 * Created by Ahmed Adel on 9/21/2016.
 *
 * @deprecated since 25/10/2016 entities that will be used in POST / PUT requests does not
 * need to extend this class
 */
public class RequestEntity<T extends RequestEntity<T>> implements Serializable, Cloneable {

    private long branchId;
    private long companyId;
    private String description;
    private int isactive;
    private int isdeleted;

    public long getBranchId() {
        return branchId;
    }

    public void setBranchId(long branchId) {
        this.branchId = branchId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsactive() {
        return isactive;
    }

    public void setIsactive(int isactive) {
        this.isactive = isactive;
    }

    public int getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(int isdeleted) {
        this.isdeleted = isdeleted;
    }
}
