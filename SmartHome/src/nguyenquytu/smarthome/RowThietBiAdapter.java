package nguyenquytu.smarthome;

import java.util.ArrayList;

import nguyenquytu.smarthome.R;


import Object.ThietBi;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class RowThietBiAdapter extends ArrayAdapter<ThietBi>{

	private Context context;
	@SuppressWarnings("unused")
	private int layoutId;
	ArrayList<ThietBi> arrThietBi;
	ProgressDialog dialog;
	String lableBatTat;
	String idNguoiDung;
	String url;
	Vibrator vibrator;
	
	public RowThietBiAdapter(Context context, int layoutId,ArrayList<ThietBi> arrThietBi,String idNguoiDung) {
		super(context, layoutId, arrThietBi);
		this.context = context;
		this.layoutId = layoutId;
		this.arrThietBi = arrThietBi;
		this.idNguoiDung=idNguoiDung;
		dialog = new ProgressDialog(context);
		vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
	}
	@SuppressLint("ViewHolder") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_thiet_bi, parent, false);
		TextView TenThietBi=(TextView) rowView.findViewById(R.id.tvThietBi);
		final ToggleButton TrangThai=(ToggleButton) rowView.findViewById(R.id.swTrangThai);
		TextView TrangThaiReadOnly=(TextView) rowView.findViewById(R.id.tvReadOnly);
		final String idThietBi=arrThietBi.get(position).id;
		final int index=position;
		TrangThai.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TrangThai.isChecked()){
					url="http://smarthometl.com/index.php?cmd=batthietbi&id="+idNguoiDung+"&idthietbi="+idThietBi;
					lableBatTat=arrThietBi.get(index).TenThietBi;
					lableBatTat+=" đang bật...";
				}else{
					url="http://smarthometl.com/index.php?cmd=tatthietbi&id="+idNguoiDung+"&idthietbi="+idThietBi;
					lableBatTat=arrThietBi.get(index).TenThietBi;
					lableBatTat+=" đang tắt...";
				}
				vibrator.vibrate(100);
				new ParseJSONTaskBatTat().execute();
			}
		});
		TenThietBi.setText(arrThietBi.get(position).TenThietBi);
		TrangThaiReadOnly.setText(arrThietBi.get(position).ReadOnly);
		if(TrangThaiReadOnly.getText().equals("1")){
			TrangThai.setVisibility(View.GONE);
			TrangThaiReadOnly.setVisibility(View.VISIBLE);
			TrangThaiReadOnly.setText(arrThietBi.get(position).TrangThai);
		}else{
			TrangThai.setVisibility(View.VISIBLE);
			TrangThaiReadOnly.setVisibility(View.GONE);
			if(arrThietBi.get(position).TrangThai.equals("1")){
				TrangThai.setChecked(true);
			}else{
				TrangThai.setChecked(false);
			}
		}
		return rowView;
	}
	private class ParseJSONTaskBatTat extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage(lableBatTat);
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

				return true;
			} catch (Exception e) {
				Toast.makeText(getContext(), "Không kết nối được server",
						Toast.LENGTH_SHORT).show();
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
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getContext());
				builder.setTitle("Lỗi!");
				builder.setMessage("Kiểm tra kết nối mạng");
				builder.show();
				return;
			}
		}
	}

}
