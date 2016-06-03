package method;

import nlplib.NLPLIB_Lib;

public class QNewton {
	
	double initx[];
	double alpha[];
	int roopnum;
	double conv;
	double x[][], obj[];		//x座標, y座標, 目的間数値
	int code = 2002;				//正常修了:ループ回数, 直線対策失敗:2001, 反復回数上限:2002
	double norm[], d[][], dx[][];
	NLPLIB_Lib lib = new NLPLIB_Lib(0.1, 0.4, 0.5); //double xi1, double xi2, double tau
	private long time_conv, time_lim;
	private double B[][];
	private double BB[][][];
	
	public QNewton(double[] initx, double alpha, int roopnum, double conv, double B[][]) {
		this.initx = initx;
		this.alpha = new double[roopnum];
		this.alpha[0] = alpha;
		this.roopnum = roopnum;
		this.conv = conv;
		x = new double[roopnum][initx.length];
		obj = new double[roopnum];
		norm = new double[roopnum];
		d = new double[roopnum][initx.length];
		dx = new double[roopnum][initx.length];
		this.B = B;
		BB = new double[roopnum][initx.length][initx.length];
		for(int i = 0; i < initx.length; i++){
			for (int j = 0; j < initx.length; j++){
				BB[0][i][j] = B[i][j]; 
			}
		}
		dx[0][0] = 0;
		dx[0][1] = 0;
	}
	
	public double[][][] getBB() {
		return BB;
	}

	public double[] getInitx() {
		return initx;
	}

	public double[][] getD() {
		return d;
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

	public void callQNewton(){
		
		long start = System.currentTimeMillis();
		
		//0回目
		x[0][0] = this.initx[0];
		x[0][1] = this.initx[1];
		obj[0] = Math.pow(x[0][0], 2) + Math.pow(x[0][1]-1, 4) + x[0][0];
		dx[0][0] = 0.0;
		dx[0][1] = -8.0;
		norm[0] = 0;
		
		double s[] = new double[initx.length];
		double y[] = new double[initx.length];
		double bs[] = new double[initx.length];
		double bsbst[][] = new double[initx.length][initx.length];
		double stbs, sty;
		double yyt[][] = new double[initx.length][initx.length];
		
		for(int i = 1; i < roopnum; i++){
			
			//Step 2. 連立方程式Bkd = -∇f(xk)を解いて探索方向dkを求める
			this.B[0][initx.length] = -1*(x[i-1][0] - Math.pow(x[i-1][1], 2));//-1をかけて符号を反対にする
			//System.out.println("B[0][initx.length]="+B[0][initx.length]);
			this.B[1][initx.length] = -1*(-2*x[i-1][1]*(x[i-1][0]-Math.pow(x[i-1][1], 2)) + Math.pow(x[i-1][1] - 2, 3));
			//配列は参照渡し、ガウスで値がかわってしまうので新たに配列を作っておく
			double tmpB[][] = new double[initx.length][initx.length+1];
			for(int j = 0; j < initx.length; j++){
				for(int k = 0; k < initx.length + 1; k++){
					tmpB[j][k] = B[j][k];
				}
			}
			nlplib.Gauss_lib gauss_lib = new nlplib.Gauss_lib(initx.length,tmpB );//次元数nをコンストラクタに設定
			gauss_lib.gauss();//ガウスエンジン呼び出し
			d[i] = gauss_lib.getx();//解を取り出す
			
			//ステップ幅を求める（今回は固定 alpha = 1.0）
			//double xx [] = {x[i-1][0], x[i-1][1]};
			//double dxx[] = {dx[i][0], dx[i][1]};
			//alpha[i] = lib.getArmijo(xx, dxx, 0.5);
			alpha[i] = 1.0;
			
			//次の点を求める
			x[i][0] = x[i-1][0] + alpha[i]*d[i][0];
			x[i][1] = x[i-1][1] + alpha[i]*d[i][1];
			
			//dxを求める
			dx[i][0] = x[i][0] - Math.pow(x[i][1], 2);
			dx[i][1] = -2*x[i][1]*(x[i][0]-Math.pow(x[i][1], 2)) + Math.pow(x[i][1] - 2, 3);
			
			
			/////////////////////////////
			//dxをいれる
			//収束条件が微分ノルムが０に近づいた時、今は方向ベクトルのノルムでやっている？
			//yの求め方が違う？
			//Bが合っていない
			
			//目的関数の計算
			obj[i] =Math.pow(x[i][0] - Math.pow(x[i][1], 2), 2 )/2 + Math.pow(x[i][1]-2, 4)/4; 
			
			//収束条件判定
			norm[i] = Math.sqrt(Math.pow(dx[i][0], 2) + Math.pow(dx[i][1], 2));
			if( norm[i] < conv && code == 2002){
				code = i;
				long stop = System.currentTimeMillis();
				time_conv = stop - start;
			}
			
			//Bkの更新(BFGS公式)
			s[0] = x[i][0] - x[i-1][0];
			s[1] = x[i][1] - x[i-1][1];
			
			y[0] =  dx[i][0] - dx[i-1][0];
			y[1] =  dx[i][1] - dx[i-1][1];
			
			bs[0] = this.B[0][0]*s[0]+this.B[0][1]*s[1];
			bs[1] = this.B[1][0]*s[0]+this.B[1][1]*s[1];
				
			bsbst[0][0] = bs[0]*bs[0];
			bsbst[0][1] = bs[0]*bs[1];
			bsbst[1][0] = bs[1]*bs[0];
			bsbst[1][1] = bs[1]*bs[1];
			
			stbs = (s[0]*B[0][0]+s[1]*B[1][0])*s[0]+(s[0]*B[1][0]+s[1]*B[1][1])*s[1];
			sty = s[0]*y[0]+s[1]*y[1];
			
			yyt[0][0] = y[0]*y[0];
			yyt[0][1] = y[0]*y[1];
			yyt[1][0] = y[1]*y[0];
			yyt[1][1] = y[1]*y[1];
			
			for(int j = 0; j < initx.length; j++){
				for(int k = 0; k < initx.length; k++){
					B[j][k] = B[j][k] - bsbst[j][k]/stbs + yyt[j][k]/sty;
					BB[i][j][k] = BB[i-1][j][k] - bsbst[j][k]/stbs + yyt[j][k]/sty;
				}
			}
			if(i == 1){
			System.out.println("i="+i);
				System.out.println("s[0]="+s[0]);
				System.out.println("s[1]="+s[1]);
				System.out.println("y[0]="+y[0]);
				System.out.println("y[1]="+y[1]);
				System.out.println("bs[0]="+bs[0]);
				System.out.println("bs[1]="+bs[1]);
				System.out.println("sty="+sty);
				System.out.println("stbs="+stbs);
				System.out.println("B[0][0]="+B[0][0]);
				System.out.println("B[0][1]="+B[0][1]);
				System.out.println("B[1][0]="+B[1][0]);
				System.out.println("B[1][1]="+B[1][1]);
			}
			
		}
		
		long stop = System.currentTimeMillis();
		time_lim = stop - start;	
		
	}
	
}
