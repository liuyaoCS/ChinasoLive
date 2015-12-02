package com.recorder.net;

import retrofit.RestAdapter;

public class NetworkService {
	static NetworkServiceAPI instance;

	static public NetworkServiceAPI getInstance(){
		if (instance != null)
			return instance;

		RestAdapter restAdapter = new RestAdapter.Builder()
	    .setEndpoint("http://nfe.mgt.chinaso365.com/live-video.frontend/")
	    .build();
		instance = restAdapter.create(NetworkServiceAPI.class);
		
		return instance;
	}
}
