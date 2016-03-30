package com.javapapers.java.social.facebook;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.concurrent.TimeUnit ;


public class MainMenu extends HttpServlet {

	private static final long serialVersionUID = -2712636124494512L;
	
	@Resource(lookup = "jdbc/InnovationDatabase")
	DataSource ds;
	Connection connection = null;
	PreparedStatement pstmt = null;
	MED_Model Infor = new MED_Model();
	
	
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		
		System.out.println("===Function MainMenu===");
		
		String code = null;
		code = request.getParameter("code");
		if (code == null || code.equals("")) {
			throw new RuntimeException(
					"ERROR: Didn't get code parameter in callback.");
		}
		
		FBConnection fbConnection = new FBConnection();
		String accessToken = fbConnection.getAccessToken(code);
		
		
//==set User Agent
		   Infor.setMED_USER_AGENT(request.getHeader("User-Agent"));
//==set Device
		   Infor.setMED_OS(FindDevice(request.getHeader("User-Agent")));

////==Get Information Meraki Devices

		  HttpSession session = request.getSession(true);
		  System.out.println("CLIENT IP : "+session.getAttribute("client_ip"));
		  
		  Infor.setMED_CLIENT_IP((String)session.getAttribute("client_ip"));
		  Infor.setMED_CLIENT_MAC((String)session.getAttribute("client_mac"));
		  Infor.setMED_GATEWAY_ID((String)session.getAttribute("gateway_id"));
		  Infor.setMED_NODE_ID((String)session.getAttribute("node_id"));
		  Infor.setMED_NODE_MAC((String)session.getAttribute("node_mac"));
		  
		  String user_continue_url = (String)session.getAttribute("user_continue_url"); 
		  String base_grant_url = (String)session.getAttribute("base_grant_url");
   

		FBGraph fbGraph = new FBGraph(accessToken);
		String graph = fbGraph.getFBGraph();
		Map<String, String> fbProfileData = fbGraph.getGraphData(graph);
		
//set Fackebook ID
		Infor.setMED_FB_ID(fbProfileData.get("id"));
//set Facebook Name 
		Infor.setMED_FB_NAME(fbProfileData.get("name"));
//set Email
		Infor.setMED_FB_EMAIL(fbProfileData.get("email"));
//set gender
		Infor.setMED_FB_GENDER(fbProfileData.get("gender"));
//set age range min
		if(fbProfileData.get("age_range_min") != null)
			Infor.setMED_FB_AGE_RANGE_MIN(Integer.parseInt(fbProfileData.get("age_range_min")));
//set age range min
		if(fbProfileData.get("age_range_max") != null)
			Infor.setMED_FB_AGE_RANGE_MAX(Integer.parseInt(fbProfileData.get("age_range_max")));
//Display Servlet
	//	ServletOutputStream out = res.getOutputStream();
		//out.println("<h1><center>Login Successfully</center></h1>");
	//	out.println("<div><center>Enjoy your Free wifi at IBM Innovation Center.</center></div>");
		/*
		out.println("<h1><center>Facebook Login</center></h1>");
		out.println("<h1><center>Success</center></h1>");
		out.println("<h2><center>Information Facebook</center></h2>");
		out.println("<div><center><b>Facebook ID : </b>"+ Infor.getMED_FB_ID());
		out.println("<div><center><b>Facebook Login :</b> "+ Infor.getMED_FB_NAME());
		out.println("<div><center><b>Email :</b> "+ Infor.getMED_FB_EMAIL());
		out.println("<div><center><b>Age Range Min :</b> "+ (Infor.getMED_FB_AGE_RANGE_MIN() == 0 ? "-" : Infor.getMED_FB_AGE_RANGE_MIN()));
		out.println("<div><center><b>Age Range Max :</b> "+ (Infor.getMED_FB_AGE_RANGE_MAX() == 0 ? "-" : Infor.getMED_FB_AGE_RANGE_MAX()));
		out.println("<div><center><b>Gender :</b> "+ Infor.getMED_FB_GENDER());
		
		out.println("<h2><center>Device Information</center></h2>");
		out.println("<div><center><b>IP Address : </b>" + Infor.getMED_CLIENT_IP());
		out.println("<div><center><b>MAC Address : </b>" + Infor.getMED_CLIENT_MAC());
		out.println("<div><center><b>User-Agent : </b>" + Infor.getMED_USER_AGENT());
		out.println("<div><center><b>Devices : </b>" + Infor.getMED_OS());
		out.println("<div><center><b>Node ID : </b>" + Infor.getMED_NODE_ID());
		out.println("<div><center><b>Node MAC : </b>" + Infor.getMED_NODE_MAC());
		out.println("<div><center><b>Gateway ID : </b>" + Infor.getMED_GATEWAY_ID());
		out.println("<div><center><br><a href=https://n25.network-auth.com/splash/grant?continue_url=http://www.google.com&duration=3600><button type='button' style='background-color: #e6e6e6; width: 50%; height: 6%; font-size: x-large;'>Connecting to the Internet</button></a></center>");
		out.println("<div></div>");
		
		*/
		//out.println("<div><center><br><a href=https://n25.network-auth.com/splash/grant?continue_url=http://www.google.com&duration=3600><button type='button' style='background-color: #e6e6e6; width: 50%; height: 6%; font-size: x-large;'>Connecting to the Internet</button></a></center>");
		String forwardUrl = "summary.jsp";

		request.getRequestDispatcher(forwardUrl).forward(request, response);
		InsetInformtoDB();				

	}
	
	public String FindDevice(String DC){
		
		   if (DC != null) {
			   if (DC.indexOf("Windows") > -1 ) {
				   DC = "Windows" ;
			   }else if (DC.indexOf("Android") > -1 ) {
				   DC = "Android" ;
			   }else if (DC.indexOf("Mac OS") > -1 ) {
				   DC = "Apple" ;
			   }else {
				   DC = ("Other") ; 	   
			   }
		   }
		return DC;
	}
	
	public void InsetInformtoDB(){
		
		if (Infor.getMED_FB_ID() != null) {
			try {
				connection = ds.getConnection();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				System.out.println("Can't Access Database Please try agian!");
				e2.printStackTrace();
			}
			try {
				
				String sql = "insert into meraki_device(MED_CLIENT_IP,MED_CLIENT_MAC,MED_CREATE_DATE,MED_FB_AGE_RANGE_MAX,MED_FB_AGE_RANGE_MIN,MED_FB_EMAIL,MED_FB_GENDER,MED_FB_ID,MED_FB_NAME,MED_GATEWAY_ID,MED_NODE_ID,MED_NODE_MAC,MED_OS,MED_USER_AGENT) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
				
				pstmt = connection.prepareStatement(sql);
				pstmt.setString(1, Infor.getMED_CLIENT_IP());
				pstmt.setString(2, Infor.getMED_CLIENT_MAC());
				pstmt.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()+ TimeUnit.HOURS.toMillis(7)));
				pstmt.setInt(4, Infor.getMED_FB_AGE_RANGE_MAX());
				pstmt.setInt(5,Infor.getMED_FB_AGE_RANGE_MIN());
				pstmt.setString(6, Infor.getMED_FB_EMAIL());
				pstmt.setString(7, Infor.getMED_FB_GENDER());
				pstmt.setString(8, Infor.getMED_FB_ID());
				pstmt.setString(9, Infor.getMED_FB_NAME());
				pstmt.setString(10, Infor.getMED_GATEWAY_ID());
				pstmt.setString(11, Infor.getMED_NODE_ID());
				pstmt.setString(12, Infor.getMED_NODE_MAC());
				pstmt.setString(13, Infor.getMED_OS());
				pstmt.setString(14, Infor.getMED_USER_AGENT());
				pstmt.execute();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					pstmt.close();
//					out.println("<div><center>Error SQL </center></div>");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			try {
				connection.close();
//				out.println("<div><center>connection.close</center></div>");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
		
		
	}
	
	
	
}
	



