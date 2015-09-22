package nguyenquytu.smarthome;

import java.security.MessageDigest;
import java.util.ArrayList;

import nguyenquytu.smarthome.R;

import org.json.JSONObject;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class DangNhap extends Activity {
	ProgressDialog dialog;
	EditText TenDangNhap, MatKhau;
	String url = "";
	String id = "";
	String UserName="";
	String Active = "";
	String VaiTro = "";
	String Email = "";
	String SDT = "";
	private UiLifecycleHelper uihelper;

	void showMsg(String string) {
		Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT)
				.show();
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {

			onSessionStateChange(session, state, exception);
			//session.close();
		}
	};

	void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) 
		{
			Log.i("facebook", "Logged out...");
			
			Request.newMeRequest(session, new Request.GraphUserCallback() {

				@Override
				public void onCompleted(GraphUser user, Response response) {

					if (user != null) {
						
						Session s=Session.getActiveSession();
						s.closeAndClearTokenInformation();
						//Intent t = new Intent(DangNhap.this, MainActivity.class);
						//t.putExtra("idNguoiDung", "127");
						//startActivity(t);
						//showMsg("Tên đăng nhâp: " + user.getName());
						//showMsg("Email: " + user.getProperty("email") + "");
						// showMsg(user.getProperty("gender") + "");
						//showMsg(user.getId() + "");
						String json="{\"name\":\""+user.getName()+"\",\"id\":\""+user.getId()+"\"}";
						//Toast.makeText(getApplicationContext(),json, Toast.LENGTH_LONG).show();
						//new DangNhapVoiFace(json, user.getProperty("email")+"").execute();
						url = "http://smarthometl.com/index.php?cmd=dangnhapvoiface&tendangnhap="+json+"&email="+user.getProperty("email")+"";
						new ParseJSONTask().execute();
						//Intent t = new Intent(DangNhap.this, info.androidhive.slidingmenu.DangNhapVoiFace.class);
						//t.putExtra("json", json);
						//t.putExtra("email", user.getProperty("email")+"");
						//startActivity(t);
					}else {
						showMsg("its null");
						showMsg(response.getError().getErrorMessage());
					}
				}
			}).executeAsync();

		}else if (state.isClosed()) {
			Log.i("facebook", "Logged out...");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		uihelper.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uihelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		super.onPause();
		uihelper.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		uihelper.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uihelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dangnhap);
		Init();
		uihelper = new UiLifecycleHelper(this, callback);
		uihelper.onCreate(savedInstanceState);

		ArrayList<String> permission = new ArrayList<String>();
		permission.add("email");
		permission.add("public_profile");
		permission.add("user_friends");

		LoginButton btn = (LoginButton) findViewById(R.id.fbbtn);
		btn.setPublishPermissions(permission);

		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"nguyenquytu.smarthome", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void Init() {
		dialog = new ProgressDialog(this);
		TenDangNhap = (EditText) findViewById(R.id.editTextTenDangNhap);
		MatKhau = (EditText) findViewById(R.id.editTextMatKhau);
	}

	public void Onclick(View v) {
		switch (v.getId()) {
		case R.id.buttonDangNhap:
			url = "http://smarthometl.com/index.php?cmd=dangnhap&tendangnhap="
					+ TenDangNhap.getText();
			url += "&matkhau=" + MatKhau.getText();
			new ParseJSONTask().execute();
			break;
		}
	}
	
	private class ParseJSONTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("Loading...");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			WebServiceHandler webServiceHandler = new WebServiceHandler();
			String jsonstr = webServiceHandler.getJSONData(url);
			if (jsonstr == null) {
				return false;
			}
			try {
				JSONObject object = new JSONObject(jsonstr);
				Active = object.getString("active");
				UserName=object.getString("TenDangNhap");
				id = object.getString("id");
				VaiTro = object.getString("VaiTro");
				Email = object.getString("Email");
				SDT = object.getString("SDT");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			if (result == false) {
				//Toast.makeText(DangNhap.this, id, Toast.LENGTH_LONG).show();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						DangNhap.this);
				builder.setTitle("Lỗi!");
				builder.setMessage("Kiểm tra kết nối mạng");
				builder.show();
				return;
			}
			if (!id.equals("-1")) {
				if (!Active.equals("1")) {
					Toast.makeText(DangNhap.this, "Tài khoản đã hết hạn!",
							Toast.LENGTH_LONG).show();
				} else {
//					Toast.makeText(DangNhap.this, "Thành công"+id,
//					Toast.LENGTH_SHORT).show();
					Intent t = new Intent(DangNhap.this, MainActivity.class);
					t.putExtra("idNguoiDung", id);
					startActivity(t);
				}

			} else {
				Toast.makeText(DangNhap.this, "Đăng nhập thất bại",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
