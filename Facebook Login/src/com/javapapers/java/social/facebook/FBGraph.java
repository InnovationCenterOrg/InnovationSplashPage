package com.javapapers.java.social.facebook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class FBGraph {
	private String accessToken;

	public FBGraph(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getFBGraph() {
		String graph = "";
		String g = "";
		try {
			g = "https://graph.facebook.com/me?fields=id,name,email,gender,age_range&" + accessToken;
			URL u = new URL(g);
			URLConnection c = u.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String inputLine ;
			StringBuffer b = new StringBuffer();
			while ((inputLine = in.readLine()) != null){
				b.append(inputLine + "\n");
			}
			in.close();
			graph = b.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("ERROR in getting FB graph data. " + e);
		}
		System.out.println("GetFB Graph : " +graph);
		return graph;
	}

	public Map<String, String> getGraphData(String fbGraph) {
		Map<String, String> fbProfile = new HashMap<String, String>();
		try {
			System.out.println("Function Map ================");
			JSONObject json = new JSONObject(fbGraph);
//			fbProfile.put("id",  json.getString("id"));
//			fbProfile.remove("id");
			fbProfile.put("id", json.getString("id"));
			if (json.has("name")){
//				fbProfile.remove("name");
				fbProfile.put("name", json.getString("name"));
			}
			if (json.has("email")) {
//				fbProfile.remove("email");
				fbProfile.put("email", json.getString("email"));
			}
			if (json.has("gender")) {
//				fbProfile.remove("gender");
				fbProfile.put("gender", json.getString("gender"));
			}
			if (json.has("age_rang")){
//				fbProfile.remove("age_rage");
				fbProfile.put("age_range", json.getString("age_range"));
			}
			if (json.has("{min}")){
				fbProfile.put("min", json.getString("min"));
			}
			if (json.has("node_id")){
				fbProfile.put("node_id", json.getString("node_id"));
			}
			
		System.out.println("Hash Map Competete!");
			

		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException("ERROR in parsing FB graph data. " + e);
		}
		
		return fbProfile;
	}
}
