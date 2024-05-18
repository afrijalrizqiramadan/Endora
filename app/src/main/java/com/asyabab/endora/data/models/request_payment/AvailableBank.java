
package com.asyabab.endora.data.models.request_payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AvailableBank implements Serializable {

    @SerializedName("bank_code")
    @Expose
    public String bankCode;
    @SerializedName("collection_type")
    @Expose
    public String collectionType;
    @SerializedName("bank_account_number")
    @Expose
    public String bankAccountNumber;
    @SerializedName("transfer_amount")
    @Expose
    public Integer transferAmount;
    @SerializedName("bank_branch")
    @Expose
    public String bankBranch;
    @SerializedName("account_holder_name")
    @Expose
    public String accountHolderName;
    @SerializedName("identity_amount")
    @Expose
    public Integer identityAmount;

}
