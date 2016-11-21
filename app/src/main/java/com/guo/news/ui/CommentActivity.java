package com.guo.news.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.guo.news.R;
import com.guo.news.data.model.CommentModel;
import com.guo.news.data.remote.ResultTransformer;
import com.guo.news.data.remote.ServiceHost;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CommentActivity extends AppCompatActivity{

    public static final String KEY_CONTENT_ID = "contentId";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.coordinator)
    CoordinatorLayout coordinator;
    @Bind(R.id.comment)
    EditText comment;
    private String mContentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        mContentId = getIntent().getStringExtra(KEY_CONTENT_ID);
//        if (mContentId == null) {
//            finish();
//        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(CommentActivity.this)
                        .setMessage("Scratch content has not saved!")
                        .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("Abandon", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();


            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        comment.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(comment, InputMethodManager.SHOW_FORCED);
    }

    @OnClick(R.id.publish)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.publish:
                String commentStr =  comment.getText().toString().trim();
                if (TextUtils.isEmpty(commentStr)) {
                    Toast.makeText(CommentActivity.this, "comment is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                CommentModel commentModel = new CommentModel();
                commentModel.content = commentStr;
                commentModel.news_id = mContentId;

                ServiceHost.getService().addComment(commentModel)
                        .subscribeOn(Schedulers.io())
                        .map(new ResultTransformer<String>())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                Toast.makeText(CommentActivity.this, s, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
                break;
        }
    }
}
