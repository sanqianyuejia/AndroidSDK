package com.kuaishangtong.service;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.kuaishangtong.utils.Constants;
import com.kuaishangtong.utils.HttpURLUtils;

public class PersonService extends BaseService {
	
	public JSONObject personCreate(String id, String name, String tag, int passtype) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(Constants.API_KEY, super.getClient().getKey());
		parameters.put(Constants.API_SECRET, super.getClient().getSecret());
		parameters.put(Constants.ID, id);
		parameters.put(Constants.NAME, name);
		parameters.put(Constants.TAG, tag);
		parameters.put(Constants.PASSTYPE, String.valueOf(passtype));	// DEFAULT ONLY SUPPORT DIGITS  
		
		String tokenResult = HttpURLUtils.doPost(super.getClient().getServerString()+Constants.URL_PERSON_CREATE, parameters);
		JSONObject tokenJson = (JSONObject) JSONObject.parseObject(tokenResult);
		
		return tokenJson;
	}
	
	public JSONObject personRemove(String id, String name) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(Constants.API_KEY, super.getClient().getKey());
		parameters.put(Constants.API_SECRET, super.getClient().getSecret());
		parameters.put(Constants.ID, id);
		parameters.put(Constants.NAME, name);
		
		String tokenResult = HttpURLUtils.doPost(super.getClient().getServerString()+Constants.URL_PERSON_DELETE, parameters);
		JSONObject tokenJson = (JSONObject) JSONObject.parseObject(tokenResult);
		
		return tokenJson;
	}
	
	public JSONObject personGetInfo(String id, String name) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(Constants.API_KEY, super.getClient().getKey());
		parameters.put(Constants.API_SECRET, super.getClient().getSecret());
		parameters.put(Constants.ID, id);
		parameters.put(Constants.NAME, name);
		
		String tokenResult = HttpURLUtils.doPost(super.getClient().getServerString()+Constants.URL_PERSON_GETINFO, parameters);
		JSONObject tokenJson = (JSONObject) JSONObject.parseObject(tokenResult);
		
		return tokenJson;
	}
	
	public JSONObject personGetAuthCode(String id, String name) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(Constants.API_KEY, super.getClient().getKey());
		parameters.put(Constants.API_SECRET, super.getClient().getSecret());
		parameters.put(Constants.ID, id);
		parameters.put(Constants.NAME, name);
		
		String tokenResult = HttpURLUtils.doPost(super.getClient().getServerString()+Constants.URL_PERSON_GET_AUHCODE, parameters);
		JSONObject tokenJson = (JSONObject) JSONObject.parseObject(tokenResult);
		
		return tokenJson;
	}
	
	public JSONObject personAddSpeech(String id, String name, String codec, int sr, boolean bVerify, String rule, byte[] data) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(Constants.API_KEY, super.getClient().getKey());
		parameters.put(Constants.API_SECRET, super.getClient().getSecret());
		parameters.put(Constants.ID, id);
		parameters.put(Constants.NAME, name);
		parameters.put(Constants.CODEC, codec);
		parameters.put(Constants.SAMPLERATE, String.valueOf(sr));
		parameters.put(Constants.VERIFY, String.valueOf(bVerify));
		parameters.put(Constants.SPEECH_RULE, rule);
		
		
		String tokenResult = HttpURLUtils.doUploadFile(super.getClient().getServerString()+Constants.URL_SPEECH_ADD, parameters, 
				Constants.FILEPARAM, "./testfile.wav", "multipart/form-data;", data);
		JSONObject tokenJson = (JSONObject) JSONObject.parseObject(tokenResult);
		
		return tokenJson;
	}
	
	public JSONObject personRemoveSpeeches(String id, String name) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(Constants.API_KEY, super.getClient().getKey());
		parameters.put(Constants.API_SECRET, super.getClient().getSecret());
		parameters.put(Constants.ID, id);
		parameters.put(Constants.NAME, name);
		
		String tokenResult = HttpURLUtils.doPost(super.getClient().getServerString()+Constants.URL_SPEECH_DELETE_PERSON, parameters);
		JSONObject tokenJson = (JSONObject) JSONObject.parseObject(tokenResult);
		
		return tokenJson;
	}
	
	public JSONObject personRemoveSpeech(String md5) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(Constants.API_KEY, super.getClient().getKey());
		parameters.put(Constants.API_SECRET, super.getClient().getSecret());
		parameters.put(Constants.MD5, md5);
		
		String tokenResult = HttpURLUtils.doPost(super.getClient().getServerString()+Constants.URL_SPEECH_DELETE, parameters);
		JSONObject tokenJson = (JSONObject) JSONObject.parseObject(tokenResult);
		
		return tokenJson;
	}
	
	public JSONObject personGetSpeeches(String id, String name) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(Constants.API_KEY, super.getClient().getKey());
		parameters.put(Constants.API_SECRET, super.getClient().getSecret());
		parameters.put(Constants.ID, id);
		parameters.put(Constants.NAME, name);
		
		String tokenResult = HttpURLUtils.doPost(super.getClient().getServerString()+Constants.URL_SPEECH_FIND_PERSON, parameters);
		JSONObject tokenJson = (JSONObject) JSONObject.parseObject(tokenResult);
		
		return tokenJson;
	}
	
	public JSONObject personGetLogs(String id, String name, int limit) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(Constants.API_KEY, super.getClient().getKey());
		parameters.put(Constants.API_SECRET, super.getClient().getSecret());
		parameters.put(Constants.ID, id);
		parameters.put(Constants.NAME, name);
		parameters.put(Constants.PERSON_LIMIT, String.valueOf(limit));
		
		String tokenResult = HttpURLUtils.doPost(super.getClient().getServerString()+Constants.URL_PERSON_FIND_LOGS, parameters);
		JSONObject tokenJson = (JSONObject) JSONObject.parseObject(tokenResult);
		
		return tokenJson;
	}
	
	public JSONObject personReserveSpeeches(String id, String name, int number) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(Constants.API_KEY, super.getClient().getKey());
		parameters.put(Constants.API_SECRET, super.getClient().getSecret());
		parameters.put(Constants.ID, id);
		parameters.put(Constants.NAME, name);
		parameters.put(Constants.NUMBER, String.valueOf(number));
		
		
		String tokenResult = HttpURLUtils.doPost(super.getClient().getServerString()+Constants.URL_SPEECH_RESERVE_SPEECHES, parameters);
		JSONObject tokenJson = (JSONObject) JSONObject.parseObject(tokenResult);
		
		return tokenJson;
	}
}
