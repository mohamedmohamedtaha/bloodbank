
package com.example.manasatpc.bloadbank.u.data.model.notification.notificationscount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationsCount {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private DataNotificationsCount data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataNotificationsCount getData() {
        return data;
    }

    public void setData(DataNotificationsCount data) {
        this.data = data;
    }

}
