package nlplib;

//1.Gauss_libクラスのオブジェクト生成
// 	Gauss_lib gauss_lib = new Gauss_lib(n);//次元数nをコンストラクタに設定
//2.係数行列の引き渡し
//double T[][] = {{2,4,2,8},{4,10,3,17},{3,7,1,11}};
//gauss_lib.setT(T);//係数行列の引き渡し
//3．計算用エンジン呼び出し
//gauss_lib.gauss();//ガウスエンジン呼び出し
//4.解を得る
//x = gauss_lib.getx();//解を取り出す

public class Gauss_lib {
	//フィールド
	int n;
	//配列の初期化
	double [] x; //解
	double [][] T ;//係数行列格納用配列
	
	//コンストラクタ
	public Gauss_lib(int n, double T[][]){
		this.n = n;
		x = new double[n];//解を代入する配列
		this.T = T;//係数行列
	}
	
	public int getN(){//数値を出力
		return n;
	}

	public double[] getx(){//解の受け渡し
		return x;
	}
	

	public  void gauss() {
        int i,j,k,l,pivot;
        double p,q,m;
        double b[] = new double [n + 1];

        for(i=0;i<n;i++) 
        {
                m=0;
                pivot=i;

                for(l=i;l<n;l++) 
                {
                        if(Math.abs(T[l][i])>m) //i列の中で一番値が大きい行を選ぶ 
                        {   
                                m=Math.abs(T[l][i]);
                                pivot=l;
                        }
                }

                if(pivot!=i)    //pivotがiと違えば、行の入れ替え 
                {                          
                        for(j=0;j<n+1;j++) 
                        {
                                b[j]=T[i][j];        
                                T[i][j]=T[pivot][j];
                                T[pivot][j]=b[j];
                        }
                }
        }

        for(k=0;k<n;k++) 
        {
                p=T[k][k];              //対角要素を保存
                T[k][k]=1;              //対角要素は１になることがわかっているから

                for(j=k+1;j<n+1;j++) 
                {
                        T[k][j]/=p;
                }

                for(i=k+1;i<n;i++) 
                {
                        q=T[i][k];

                        for(j=k+1;j<n+1;j++) 
                        {
                                T[i][j]-=q*T[k][j];
                        }
                        T[i][k]=0;              //0となることがわかっているところ
                }
        }

/*解の計算*/
        for(i=n-1;i>=0;i--) 
        {
                x[i]=T[i][n];
                for(j=n-1;j>i;j--) 
                {
                        x[i]-=T[i][j]*x[j];
                }
        }
	}
}
