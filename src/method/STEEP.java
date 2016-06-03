package method;

import nlplib.NLPLIB_Lib;

public class STEEP {
	
	double initx[];
	double alpha[];
	int roopnum;
	double conv;
	double x[][], obj[];		//x座標, y座標, 目的間数値
	int code = 2002;				//正常修了:ループ回数, 直線対策失敗:2001, 反復回数上限:2002
	double norm[], dx[][];
	NLPLIB_Lib lib = new NLPLIB_Lib(0.1, 0.4, 0.5); //double xi1, double xi2, double tau
	private long time_conv, time_lim;
	
	public STEEP(double[] initx, double alpha, int roopnum, double conv) {
		this.initx = initx;
		this.alpha = new double[roopnum];
		this.alpha[0] = alpha;
		this.roopnum = roopnum;
		this.conv = conv;
		x = new double[roopnum][roopnum];
		obj = new double[roopnum];
		norm = new double[roopnum];
		dx = new double[roopnum][roopnum];
	}
	
	public double[][] getDx() {
		return dx;
	}

	public double[] getNorm() {
		return norm;
	}

	public double[][] getX() {
		return x;
	}

	public int getCode() {
		return code;
	}

	public double[] getObj() {
		return obj;
	}

	public double[] getAlpha() {
		return alpha;
	}
	

	public double getConv() {
		return conv;
	}

	public long getTime_conv() {
		return time_conv;
	}

	public long getTime_lim() {
		return time_lim;
	}

	public void callSTEEP(){
		
		long start = System.currentTimeMillis();
		
		//0回目
		x[0][0] = this.initx[0];
		x[0][1] = this.initx[1];
		obj[0] = Math.pow(x[0][0], 2) + Math.pow(x[0][1]-1, 4) + x[0][0];
		dx[0][0] = 0;
		dx[0][1] = 0;
		norm[0] = 0;
		
		for(int i = 1; i < roopnum; i++){
			
			//探索方向を求める
			dx[i][0] = 2*x[i-1][0] + 1;
			dx[i][1] = 4*(Math.pow(x[i-1][1]-1,3));
			
			//ステップ幅を求める（今回は固定 alpha = 0.5）
			double xx [] = {x[i-1][0], x[i-1][1]};
			double dxx[] = {dx[i][0], dx[i][1]};
			alpha[i] = lib.getArmijo(xx, dxx, 0.5);
			//alpha[i] = 0.5;
			
			//次の点を求める
			x[i][0] = x[i-1][0] + alpha[i]*dx[i][0]*(-1);
			x[i][1] = x[i-1][1] + alpha[i]*dx[i][1]*(-1);
			
			//目的関数の計算
			obj[i] = Math.pow(x[i][0], 2) + Math.pow(x[i][1]-1, 4) + x[i][0];
			
			//収束条件判定
			norm[i] = Math.sqrt(Math.pow(dx[i][0], 2) + Math.pow(dx[i][1], 2));
			if( norm[i] < conv && code == 2002){
				code = i;
				long stop = System.currentTimeMillis();
				time_conv = stop - start;
			}
			
		}
		
		long stop = System.currentTimeMillis();
		time_lim = stop - start;	
		
	}
	


}
