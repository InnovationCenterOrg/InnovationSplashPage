package com.javapapers.java.social.facebook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class FBConnection extends HttpServlet {
	//public static final String FB_APP_ID = "860579597397496";
	//public static final String FB_APP_SECRET = "ad72240ff0f61554766ddce196b94e03";
	public static final String FB_APP_ID = "1668985786720380";
	public static final String FB_APP_SECRET = "e1749c56c7295811146a4897c8342a33";
	public static final String REDIRECT_URI = "https://innovationsplashpage.eu-gb.mybluemix.net/Facebook_Login/fbhome";

	public static final Logger log = Logger.getLogger(FBConnection.class.getName());
	
	static String accessToken = "";
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("Accesss Class : FBConnection");
		
		String fbLoginUrl = "";
		try {
			fbLoginUrl = "https://www.facebook.com/dialog/oauth?" + "client_id="
					+ FBConnection.FB_APP_ID + "&redirect_uri="
					+ URLEncoder.encode(FBConnection.REDIRECT_URI, "UTF-8")
					+ "&scope=public_profile,email";
			
			System.out.println("Accesss FBConnection complete! : " + FBConnection.FB_APP_ID  );
			response.sendRedirect(fbLoginUrl);
			
		} catch (UnsupportedEncodingException e) {
			log.info("ERROR FBConnection : "+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public String getFBGraphUrl(String code) {
		String fbGraphUrl = null;
		try {
			fbGraphUrl = "https://graph.facebook.com/oauth/access_token?"
					+ "client_id=" + FBConnection.FB_APP_ID + "&redirect_uri="
					+ URLEncoder.encode(FBConnection.REDIRECT_URI, "UTF-8")
					+ "&client_secret=" + FB_APP_SECRET + "&code=" + code;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return fbGraphUrl;
	}

	public String getAccessToken(String code) {

		System.err.print(code);
			URL fbGraphURL;
			try {
				fbGraphURL = new URL(getFBGraphUrl(code));
			} catch (MalformedURLException e) {
				e.printStackTrace();
				throw new RuntimeException("Invalid code received " + e);
			}
			
			System.out.println("Accesss getAccessToken complete!");
			
			URLConnection fbConnection;
			StringBuffer b = null;
			try {
				fbConnection = fbGraphURL.openConnection();
				BufferedReader in = null;
				in = new BufferedReader(new InputStreamReader(
						fbConnection.getInputStream()));
				String inputLine;
				b = new StringBuffer();
				while ((inputLine = in.readLine()) != null)
					b.append(inputLine + "\n");
				in.close();
		} catch (IOException e) {
			e.printStackTrace();

			throw new RuntimeException("Unable to connect with Facebook " + e);
		}

			accessToken = b.toString();
			if (accessToken.startsWith("{")) {
				throw new RuntimeException("ERROR: Access Token Invalid: "
						+ accessToken);
			}
			
			System.out.println(accessToken);
	
		return accessToken;
	}
}
