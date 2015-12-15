package com.immomo.momo.android.activity;

import java.util.regex.Pattern;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.immomo.momo.android.BaseActivity;
import com.immomo.momo.android.R;
import com.immomo.momo.android.activity.maintabs.MainTabActivity;
import com.immomo.momo.android.adapter.SimpleListDialogAdapter;
import com.immomo.momo.android.dialog.SimpleListDialog;
import com.immomo.momo.android.dialog.SimpleListDialog.onSimpleListItemClickListener;
import com.immomo.momo.android.util.TextUtils;
import com.immomo.momo.android.view.HandyTextView;
import com.immomo.momo.android.view.HeaderLayout;
import com.immomo.momo.android.view.HeaderLayout.HeaderStyle;

public class LoginActivity extends BaseActivity implements OnClickListener,
		onSimpleListItemClickListener {

	private HeaderLayout mHeaderLayout;
	private EditText mEtAccount;
	private EditText mEtPwd;
	private HandyTextView mHtvForgotPassword;
	private HandyTextView mHtvSelectCountryCode;
	private Button mBtnBack;
	private Button mBtnLogin;

	private static final String[] DEFAULT_ACCOUNTS = new String[] {
			"+8612345678901", "86930007@qq.com", "86930007" };
	private static final String DEFAULT_PASSWORD = "123456";
	private String mAreaCode = "+86";
	private String mAccount;
	private String mPassword;

	private SimpleListDialog mSimpleListDialog;
	private String[] mCountryCodes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initViews();
		initEvents();
	}

	@Override
	protected void initViews() {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.login_header);
		mHeaderLayout.init(HeaderStyle.DEFAULT_TITLE);
		mHeaderLayout.setDefaultTitle("登录", null);
		mEtAccount = (EditText) findViewById(R.id.login_et_account);
		mEtPwd = (EditText) findViewById(R.id.login_et_pwd);
		mHtvForgotPassword = (HandyTextView) findViewById(R.id.login_htv_forgotpassword);
		TextUtils.addUnderlineText(this, mHtvForgotPassword, 0,
				mHtvForgotPassword.getText().length());
		mHtvSelectCountryCode = (HandyTextView) findViewById(R.id.login_htv_selectcountrycode);
		TextUtils.addUnderlineText(this, mHtvSelectCountryCode, 0,
				mHtvSelectCountryCode.getText().length());
		mBtnBack = (Button) findViewById(R.id.login_btn_back);
		mBtnLogin = (Button) findViewById(R.id.login_btn_login);
	}

	@Override
	protected void initEvents() {
		mHtvForgotPassword.setOnClickListener(this);
		mHtvSelectCountryCode.setOnClickListener(this);
		mBtnBack.setOnClickListener(this);
		mBtnLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_htv_forgotpassword:
			startActivity(FindPwdTabsActivity.class);
			break;

		case R.id.login_htv_selectcountrycode:
			mCountryCodes = getResources()
					.getStringArray(R.array.country_codes);
			mSimpleListDialog = new SimpleListDialog(LoginActivity.this);
			mSimpleListDialog.setTitle("选择国家区号");
			mSimpleListDialog.setTitleLineVisibility(View.GONE);
			mSimpleListDialog.setAdapter(new SimpleListDialogAdapter(
					LoginActivity.this, mCountryCodes));
			mSimpleListDialog
					.setOnSimpleListItemClickListener(LoginActivity.this);
			mSimpleListDialog.show();
			break;

		case R.id.login_btn_back:
			finish();
			break;

		case R.id.login_btn_login:
			login();
			break;
		}
	}

	@Override
	public void onItemClick(int position) {
		mAccount = null;
		String text = TextUtils.getCountryCodeBracketsInfo(
				mCountryCodes[position], mAreaCode);
		mEtAccount.requestFocus();
		mEtAccount.setText(text);
		mEtAccount.setSelection(text.length());

	}

	private boolean matchEmail(String text) {
		if (Pattern.compile("\\w[\\w.-]*@[\\w.]+\\.\\w+").matcher(text)
				.matches()) {
			return true;
		}
		return false;
	}

	private boolean matchPhone(String text) {
		if (Pattern.compile("(\\d{11})|(\\+\\d{3,})").matcher(text).matches()) {
			return true;
		}
		return false;
	}

	private boolean matchMoMo(String text) {
		if (Pattern.compile("\\d{7,9}").matcher(text).matches()) {
			return true;
		}
		return false;
	}

	private boolean isNull(EditText editText) {
		String text = editText.getText().toString().trim();
		if (text != null && text.length() > 0) {
			return false;
		}
		return true;
	}

	private boolean validateAccount() {
		mAccount = null;
		if (isNull(mEtAccount)) {
			showCustomToast("请输入陌陌号码或登录邮箱");
			mEtAccount.requestFocus();
			return false;
		}
		String account = mEtAccount.getText().toString().trim();
		if (matchPhone(account)) {
			if (account.length() < 3) {
				showCustomToast("账号格式不正确");
				mEtAccount.requestFocus();
				return false;
			}
			if (Pattern.compile("(\\d{3,})|(\\+\\d{3,})").matcher(account)
					.matches()) {
				mAccount = account;
				return true;
			}
		}
		if (matchEmail(account)) {
			mAccount = account;
			return true;
		}
		if (matchMoMo(account)) {
			mAccount = account;
			return true;
		}
		showCustomToast("账号格式不正确");
		mEtAccount.requestFocus();
		return false;
	}

	private boolean validatePwd() {
		mPassword = null;
		String pwd = mEtPwd.getText().toString().trim();
		if (pwd.length() < 4) {
			showCustomToast("密码不能小于4位");
			mEtPwd.requestFocus();
			return false;
		}
		if (pwd.length() > 16) {
			showCustomToast("密码不能大于16位");
			mEtPwd.requestFocus();
			return false;
		}
		mPassword = pwd;
		return true;
	}

	private void login() {
		if ((!validateAccount()) || (!validatePwd())) {
			return;
		}
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showLoadingDialog("正在登录,请稍后...");
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					Thread.sleep(2000);
					if ((DEFAULT_ACCOUNTS[0].equals(mAccount)
							|| DEFAULT_ACCOUNTS[1].equals(mAccount) || DEFAULT_ACCOUNTS[2]
								.equals(mAccount))
							&& DEFAULT_PASSWORD.equals(mPassword)) {
						return true;
					}
				} catch (InterruptedException e) {

				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				dismissLoadingDialog();
				if (result) {
					Intent intent = new Intent(LoginActivity.this,
							MainTabActivity.class);
					startActivity(intent);
					finish();
				} else {
					showCustomToast("账号或密码错误,请检查是否输入正确");
				}
			}
		});
	}
}
