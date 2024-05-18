
package com.asyabab.endora.data.models.request_payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {

    private final static long serialVersionUID = -4444010566466020798L;
    @SerializedName("location_id")
    @Expose
    public String locationId;
    @SerializedName("shipmentable_type")
    @Expose
    public String shipmentableType;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("total_shipment")
    @Expose
    public Integer totalShipment;
    @SerializedName("total")
    @Expose
    public Integer total;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("type_uid")
    @Expose
    public String typeUid;
    @SerializedName("invoice_number")
    @Expose
    public String invoiceNumber;
    @SerializedName("expire_date")
    @Expose
    public String expireDate;
    @SerializedName("shipmentable_id")
    @Expose
    public String shipmentableId;
    @SerializedName("transaction")
    @Expose
    public Transaction transaction;

}
