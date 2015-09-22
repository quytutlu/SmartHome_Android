package nguyenquytu.smarthome;

import nguyenquytu.slidingmenu.PullToRefreshListView;
import nguyenquytu.slidingmenu.PullToRefreshListView.OnRefreshListener;
import nguyenquytu.smarthome.R;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import Object.ThietBi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DieuKhienArduino extends Fragment {
	String url = "";
	ArrayList<ThietBi> tb;
	PullToRefreshListView lv;
	RowThietBiAdapter adapter;
	private String idNguoiDung="";
	public DieuKhienArduino(String idNguoiDung) {
		this.idNguoiDung=idNguoiDung;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dieu_khien_arduino,
				container, false);
		tb = new ArrayList<ThietBi>();
		if (!tb.isEmpty()) {
			tb.clear();
		}
		url="http://smarthometl.com/index.php?cmd=laytrangthai&id="+idNguoiDung;
		new ParseJSONTask().execute();
		lv = (nguyenquytu.slidingmenu.PullToRefreshListView) rootView
				.findViewById(R.id.listrf);
		lv.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				if (!tb.isEmpty()) {
					tb.clear();
				}
				url="http://smarthometl.com/index.php?cmd=laytrangthai&id="+idNguoiDung;
				new ParseJSONTask().execute();
			}
		});
		adapter=new RowThietBiAdapter(getActivity(), R.layout.row_thiet_bi, tb,idNguoiDung);
		lv.setAdapter(adapter);
		return rootView;
	}

	private class ParseJSONTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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
				JSONArray array = object.getJSONArray("list");
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject = array.getJSONObject(i);
					ThietBi temp = new ThietBi();
					temp.id=jsonObject.getString("id");
					temp.TenThietBi = jsonObject.getString("TenThietBi");
					temp.TrangThai = jsonObject.getString("TrangThai");
					temp.ReadOnly=jsonObject.getString("ReadOnly");
					tb.add(temp);
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result == false) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("Lỗi!");
				builder.setMessage("Kiểm tra kết nối mạng");
				builder.show();
				adapter.notifyDataSetChanged();
				lv.onRefreshComplete();
				return;
			}
			adapter.notifyDataSetChanged();
			lv.onRefreshComplete();
		}
	}
}