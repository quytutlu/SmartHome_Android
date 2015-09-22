package Object;

public class ThietBi {
	public String id;
	public String TenThietBi;
	public String TrangThai;
	public String ReadOnly;
	public ThietBi(){
		id="";
		TenThietBi="";
		TrangThai="";
		ReadOnly="";
	}
	public ThietBi(String id,String TenThietBi,String TrangThai,String ReadOnly){
		this.id=id;
		this.TenThietBi=TenThietBi;
		this.TrangThai=TrangThai;
		this.ReadOnly=ReadOnly;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return TenThietBi+"__"+TrangThai;
	}
}
