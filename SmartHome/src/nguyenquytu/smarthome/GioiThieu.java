package nguyenquytu.smarthome;

import nguyenquytu.smarthome.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GioiThieu extends Fragment {
	public GioiThieu(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gioi_thieu, container, false);
		return rootView;
	}
}
