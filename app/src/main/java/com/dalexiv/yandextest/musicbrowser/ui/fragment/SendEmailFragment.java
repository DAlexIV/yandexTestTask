package com.dalexiv.yandextest.musicbrowser.ui.fragment;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dalexiv.yandextest.musicbrowser.R;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;
import icepick.State;

/**
 * Created by dalexiv on 7/19/16.
 */

public class SendEmailFragment extends Fragment {
    @BindView(R.id.submit_email)
    Button submitButton;
    @BindView(R.id.email_edittext)
    EditText editText;
    private Unbinder unbinder;

    @State
    String emailText;

    public static SendEmailFragment newInstance() {
        return new SendEmailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_email, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Configuring actionbar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null) {
            editText.setText(emailText);
        }


        RxView.clicks(submitButton)
                .map(click -> editText.getText().toString())
                .filter(text -> text.length() > 0)
                .subscribe(text -> {
                    Intent intent = new Intent(Intent.ACTION_SEND)
                            .setType("message/rfc822")
                            .putExtra(Intent.EXTRA_EMAIL, new String[]{"dalexiv@yandex.ru"})
                            .putExtra(Intent.EXTRA_SUBJECT, "Music browser")
                            .putExtra(Intent.EXTRA_TEXT, text);
                    startActivity(Intent.createChooser(intent, "Отправить письмо"));
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
