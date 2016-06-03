package nlplib;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import method.QNewton;
import method.STEEP;

/**
 * Servlet implementation class NLPLIB_Servlet
 */
//@WebServlet("/NLPLIB_Servlet")
public class NLPLIB_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NLPLIB_Servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendRedirect("index.html");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//共通パラメータ受け取り
		request.setCharacterEncoding("UTF-8");
		int opttype = Integer.parseInt(request.getParameter("opttype"));	//1 最急降下法
		int varnum = Integer.parseInt(request.getParameter("varnum"));		//変数の数
		double initx[] = new double[varnum];								//初期値
		initx[0] = Double.parseDouble(request.getParameter("initx1"));
		initx[1] = Double.parseDouble(request.getParameter("initx2"));
		
		int search = Integer.parseInt(request.getParameter("search"));		//直線探索基準
		//今回は固定値 α=0.5
		double alpha;
		if(search == 2){
			alpha = 1.0;
		}
		else{
			alpha = 0.5;
		} 
		
		int roopnum = Integer.parseInt(request.getParameter("roopnum"));
		double conv = Double.parseDouble(request.getParameter("conv"));
		
		//最適化手法の選択
		if(opttype == 1){
			STEEP steep = new STEEP(initx, alpha, roopnum, conv);			//実際は目的関数、偏微分も渡す
			steep.callSTEEP();
			request.setAttribute("res",steep);
			this.getServletContext().getRequestDispatcher("/NLPLIB_STEEP.jsp").forward(request,response);
		}
		else if(opttype == 2){
			//B0の受け取り
			double B[][] = new double[varnum][varnum+1];
			B[0][0] = Double.parseDouble(request.getParameter("b11"));
			B[0][1] = Double.parseDouble(request.getParameter("b12"));
			B[1][0] = Double.parseDouble(request.getParameter("b21"));
			B[1][1] = Double.parseDouble(request.getParameter("b22"));
			
			QNewton qnewton = new QNewton(initx, alpha, roopnum, conv, B);			//実際は目的関数、偏微分も渡す
			qnewton.callQNewton();
			request.setAttribute("res",qnewton);
			this.getServletContext().getRequestDispatcher("/NLPLIB_QNewton.jsp").forward(request,response);
		}
	}

}
