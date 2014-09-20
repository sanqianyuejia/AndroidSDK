package com.kuaishangtong.utils;

public class Constants {
	public static final int TEXT_DEPENDENT = 0;
	public static final int TEXT_INDEPENDENT = 1;
	public static final int TEXT_PROMPT = 2;	
	
	public static final int VOICEPRINT_TYPE_RANDOM_DIGITS = 0;
	public static final int VOICEPRINT_TYPE_FIXED_TEXT = 1;
	public static final int VOICEPRINT_TYPE_RANDOM_TEXT = 2;
	
	public static final String TEXT_DEPENDENT_STR = "dependent";
	public static final String TEXT_INDEPENDENT_STR = "independent";
	public static final String TEXT_PROMPT_STR = "prompt";
	
	public static final int RETURN_SUCCESS = 0;
	public static final int RETURN_FAIL = 1;
	public static final int LOCAL_ID_NULL = 2;
	public static final int SYS_AUTHORIZATION_ERROR = 1001;
	public static final int SYS_NETWORK_ERROR 		= 1002;
	public static final int SYS_INTERNAL_ERROR 		= 1003;
	public static final int SYS_ARGUMENTS_ERROR 	= 1004;
	public static final int SYS_DATABASE_ERROR 		= 1005;
	
	public static final int PERSON_ALREADY_EXIST = 2001;
	public static final int PERSON_NOT_EXIST     = 2002;
	public static final int PERSON_NO_PERSONS    = 2003;
	public static final int PERSON_NO_SPEECHES   = 2004;
	
	public static final int VOICEPRINT_STATE_ERROR        = 3001;
	public static final int VOICEPRINT_ALREADY_REGISTERED = 3002;
	public static final int VOICEPRINT_NO_SPEECHES        = 3003;
	public static final int VOICEPRINT_VERIFY_ERROR       = 3004;
	public static final int VOICEPRINT_NOT_TRAINED 		  = 3005;
	public static final int VOICEPRINT_IDENTIFY_ERROR	  = 3006; 
	
	public static final int SPEECH_TOO_SHORT        = 4001;
	public static final int SPEECH_TOO_LONG         = 4002;
	public static final int SPEECH_SAMPLERATE_ERROR = 4003;
	public static final int SPEECH_ALREADY_EXIST    = 4004;
	public static final int SPEECH_NOT_EXIST        = 4005;
	public static final int SPEECH_PROCESS_ERROR    = 4006;
	
	public static final int ASR_RECOGNIZE_ERROR = 5001;
	public static final int ASR_NOT_MATCHED     = 5002;
	
	
    public static final String API_SECRET = "api_secret";
    public static final String API_KEY = "api_key";
    public static final String ID = "id";
    public static final String IDENTY = "identy";
    public static final String MD5 = "md5";
    public static final String NAME = "name";
	public static final String NUMBER = "number";
    public static final String TAG = "tag";
    public static final String CODEC = "codec";
    public static final String SAMPLERATE = "sr";
    public static final String SAMPLE_RATE = "samplerate";
    public static final String FILEPARAM = "file";
    public static final String UPDATE = "update";    
    public static final String VERIFY = "verify";
    public static final String SUCCESS = "success";
    public static final String RESULT = "result";
    public static final String FLAG = "flag";
    public static final String SIMILARITY = "similarity";
    public static final String ERROR = "error";
    public static final String ERROR_CODE = "error_code";
	public static final String TRAIN_MODE = "mode";
	public static final String SPEECH_RULE = "rule";
	public static final String PERSON_LIMIT = "limit";
	public static final String UPDATE_TIME = "updatetime";
	public static final String MATCH = "match";
	public static final String SCORE = "score";
	public static final String PASSTYPE = "passtype";
	public static final String STEP = "step";
	public static final String RSTEPS = "rnumber";
	public static final String VSTEPS = "vnumber";
	public static final String AUTHCODE = "authcode";
    
    // server: http://192.168.1.253:8888/1/
    public static final String URL_PERSON_CREATE = "/person/create.json";
    public static final String URL_PERSON_DELETE = "/person/delete.json";
    public static final String URL_PERSON_GETINFO = "/person/find.json";
    public static final String URL_PERSON_FIND_ALL = "/person/find_all.json";
    public static final String URL_PERSON_FIND_LOGS = "/person/find_logs.json";
    public static final String URL_PERSON_GET_AUHCODE = "/person/getauthcode.json";
    
    public static final String URL_SPEECH_ADD = "/speech/add.json";
    public static final String URL_SPEECH_FIND = "/speech/find.json";
    public static final String URL_SPEECH_FIND_PERSON = "/speech/find_person.json";
    public static final String URL_SPEECH_DELETE = "/speech/delete.json";
    public static final String URL_SPEECH_DELETE_PERSON = "/speech/delete_person.json";
    public static final String URL_SPEECH_RESERVE_SPEECHES = "/speech/reserve.json";
    
    public static final String URL_MODEL_REGISTER = "/model/train.json";
    public static final String URL_MODEL_VERIFY = "/model/verify.json";
    public static final String URL_MODEL_IDENTIFY = "/model/identify.json";
    public static final String URL_MODEL_IDENTIFY_2 = "/model/identify_2.json";
    
    public static final String URL_SYSTEM_GETINFO = "/system/getinfo.json";
}
