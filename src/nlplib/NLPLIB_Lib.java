package nlplib;



public class NLPLIB_Lib {
	
	private int ndim; 							//1. 変数ベクトルの次元
	private int nicn;							//2. 不等式制約条件の数
	private int necn;							//3. 等式制約条件の数
	private int nsamp;							//4. 最小2乗問題のサンプル数
	private double rparm[] = new double[15];	//5. 手法毎に定義された実数パラメータ（１５種類）
	private int iparm[] = new int[15];			//6. 手法毎に定義された整数パラメータ（１５種類）
	private int code;							//7. 状態コード
	
	private int idmat;							//1. 使用者が自分で定義した行列のID
	private int nmat;							//2. 使用者が自分で定義した行列の数
	private int idvec;							//3. 使用者が自分で定義したベクトルのID
	private int nvec;							//4. 使用者が自分で定義したベクトルの数
	
	//Armijo Criterion
	private double xi1, xi2, tau;					//0 <xi1 < xi2 <1, 0 < xi1, 0 < tau <1  xi2はWolfeのみに利用
	
	public NLPLIB_Lib(double xi1, double xi2, double tau) {
		this.xi1 = xi1;
		this.xi2 = xi2;
		this.tau = tau;
	}


	public double getArmijo(double x[], double dx[], double alpha){
		double left, right, beta = 1.0;
		
		int i = 0; 
		while( i < 100){
			left = Math.pow(x[0] + beta * dx[0], 2) + Math.pow(x[1] + beta * dx[1]-1, 4) + x[0] + beta * dx[0];
			right = Math.pow(x[0], 2) + Math.pow(x[1]-1, 4) + x[0] +xi1 * beta * (-1) * (Math.pow(dx[0], 2) + Math.pow(dx[1], 2));
			
			//System.out.println("i="+i+", left="+left+", right="+right+", alpha="+beta);
			
			if(left <= right +0.01){
				alpha = beta;
				break;
			}else {
				beta = tau * beta;
				i++;
			}
		}
		
		return alpha;
	}
		
	
}
