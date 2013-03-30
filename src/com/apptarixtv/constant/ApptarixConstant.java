package com.apptarixtv.constant;

public class ApptarixConstant {
	
	public static final String APP_ID ="215615625228341";//Appatrixtv123
//	public static final String APP_ID ="386296548094348";// ;// ichangemycity
	public static final String[] PERMISSIONS = new String[] {
			"publish_stream","publish_actions", "read_stream", "offline_access" ,"email","user_photos","user_interests", "friends_interests","friends_relationships","friends_relationship_details"};
	
//public static final String ALL_CHANNEL_URL="http://services.whatsonindia.com/guideStar/guideStarHost.svc" +
//		"/channel?apikey=491173598037c270f1fe2d1ccbd6b58c7ed459be&responseformat=json&responselanguage=English&channelname=all" +
//		"&context=applicationName=apptarix";
public static final String PROGRAMM_SCHEDULE_GRID="http://services.whatsonindia.com/guideStar/guideStarHost.svc/schedulegrid?" +
		"apikey=93f271ad5efd7ea64a800c9acfe0b34fc19f58e3&channellist=Colors,StarPlus&responseformat=JSON&responselanguage=english" +
		"&";
public static final String FB_USER_CHECK="http://50.19.169.217/ApptarixTV/api/appUsers/isRegistered?apikey=ac2fdfd5fec83138415b9f98c82f0aac&fb_user_id=";

public static final String REGISTER_USER="http://50.19.169.217/ApptarixTV/api/appUsers/register";
public static final String FB_CONNECT="http://50.19.169.217/ApptarixTV/api/appUsers/fbcontacts";
public static final String API_KEY="ac2fdfd5fec83138415b9f98c82f0aac";
public static final String ALL_CHANNEL_URL="http://50.19.169.217/ApptarixTV/api/channels/getlist?apikey=";
}
