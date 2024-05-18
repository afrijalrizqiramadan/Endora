
package com.asyabab.endora.data.models.request_payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Transaction implements Serializable {

    private final static long serialVersionUID = 8826658302962628298L;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("external_id")
    @Expose
    public String externalId;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("merchant_name")
    @Expose
    public String merchantName;
    @SerializedName("merchant_profile_picture_url")
    @Expose
    public String merchantProfilePictureUrl;
    @SerializedName("amount")
    @Expose
    public Integer amount;
    @SerializedName("payer_email")
    @Expose
    public String payerEmail;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("expiry_date")
    @Expose
    public String expiryDate;
    @SerializedName("invoice_url")
    @Expose
    public String invoiceUrl;
    @SerializedName("available_banks")
    @Expose
    public List<AvailableBank> availableBanks = null;
    @SerializedName("available_ewallets")
    @Expose
    public List<Object> availableEwallets = null;
    @SerializedName("should_exclude_credit_card")
    @Expose
    public Boolean shouldExcludeCreditCard;
    @SerializedName("should_send_email")
    @Expose
    public Boolean shouldSendEmail;
    @SerializedName("created")
    @Expose
    public String created;
    @SerializedName("updated")
    @Expose
    public String updated;
    @SerializedName("currency")
    @Expose
    public String currency;

}
