package com.android.wassally;

import android.text.TextUtils;

import com.android.wassally.model.Order;

public class UserInputValidation {

    public static boolean isNewOrderAllSet(Order order){

        String fromGovernate = order.getFromGovernate();
        String fromCity = order.getFromCity();
        String fromAddress = order.getFromAddress();
        String senderPhoneNumber = order.getSenderPhoneNumber();
        String toGovernate = order.getToGovernate();
        String toCity = order.getToCity();
        String toAddress = order.getToAddress();
        String receiverName = order.getReceiverName();
        String receiverPhoneNumber = order.getReceiverPhoneNumber();
        int duration = order.getDuration();
        int packageWeight = order.getWeight();

        if (TextUtils.isEmpty(fromGovernate)||
                TextUtils.isEmpty(fromCity)||
                TextUtils.isEmpty(fromAddress)||
                TextUtils.isEmpty(senderPhoneNumber)||
                TextUtils.isEmpty(toGovernate)||
                TextUtils.isEmpty(toCity)||
                TextUtils.isEmpty(toAddress)||
                TextUtils.isEmpty(receiverName)||
                TextUtils.isEmpty(receiverPhoneNumber)||
                duration ==0 ||
                packageWeight == 0){
            return false;
        }else {
            return true;
        }
    }
}
