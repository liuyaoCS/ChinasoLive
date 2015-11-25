package recorder.net;

import java.util.List;

import recorder.net.model.LiveVideoInfo;
import recorder.net.model.StopVideoInfo;
import recorder.net.model.UserCheckInfo;
import recorder.net.model.VideoIdInfo;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface NetworkServiceAPI {

	
	//get video list
	@GET("/videoinfo//showinfo")
	void getVideoList(Callback<List<LiveVideoInfo>> cb);

	//create video
	@GET("/video//createvideo")
	void createVideo(@Query("uid") String uid, @Query("name") String name,@Query("avatar") String avatar,Callback<VideoIdInfo> cb);

	//stop video
	@GET("/video/deletevideo")
	void stopVideo(@Query("activityId") String activityId, Callback<StopVideoInfo> cb);

	//get token
	@GET("/rong/createtoken")
	void getToken(@Query("uid") String uid, Callback<UserCheckInfo> cb);

}
