package com.android.wassally.helpers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.wassally.R;

public class DialogUtils {
    private static Dialog dialog;
    public static void showDialog(Context context, String dialogMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.progress,null);
        TextView message = view.findViewById(R.id.progress_message_tv);
        message.setText(dialogMessage);

        builder.setView(view);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void dismissDialog(){
        dialog.dismiss();
    }
}
