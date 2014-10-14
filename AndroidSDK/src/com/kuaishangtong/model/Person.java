package com.kuaishangtong.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.kuaishangtong.client.Client;
import com.kuaishangtong.service.PersonService;
import com.kuaishangtong.utils.Constants;
import com.kuaishangtong.model.Speech;
import com.kuaishangtong.Object.Object;


public class Person extends Object {
	private Client client;
	private String id;
	private String name;
	private String tag;
	private String authcode;
	private boolean flag;
	private int step;
	private int passtype;
	
	private PersonService ps;
	
	public Person(Client client, String id, String name) {
		this.client = client;
		this.id = id;
		this.name = name;
		this.tag = "";
		this.authcode = "";
		this.step = 0;
		this.passtype = 0;	// DEFAULT DIGITS
	}
	
	public Person(Client client) {
		this.client = client;
	}
	
	public Person(Client client, String id) {
		this.client = client;
		this.id = id;
	}
	
	public PersonService getPersonService() {
		if (ps == null) {
			ps = (PersonService) new PersonService().setClient(this.client);
		}
		
		return ps;
	}
	
	public String getId() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
	public String getTag() {
		return this.tag;
	}
	
	public String getAuthCodeString() {
		return this.authcode;
	}
	
	public boolean getFlag() {
		return this.flag;
	}
	
	public int getPassType() {
		return this.passtype;
	}
	
	public int getStep() {
		return this.step;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	private void setAuthCodeString(String code) {
		this.authcode = code;
	}
	
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	public void setPassType(int passtype) {
		this.passtype = passtype;
	}
	
	public void setStep(int step) {
		this.step = step;
	}
	
	public int delete() {
		int ret = Constants.RETURN_SUCCESS;
		
		if (!this.id.isEmpty()) {
			JSONObject result = getPersonService().personRemove(this.id, this.name);
			
			if (!result.getBoolean(Constants.SUCCESS)) {
				ret = result.getIntValue(Constants.ERROR_CODE);
				super.setLastErr(result.getString(Constants.ERROR));
				super.setErrCode(ret);
			}
		} else {
			ret = Constants.LOCAL_ID_NULL;
			super.setLastErr("id is empty");
			super.setErrCode(0);
		}

		return ret;
	}
	
	public int create() {
		int ret = Constants.RETURN_SUCCESS;
		
		if (!this.id.isEmpty()) {
			JSONObject result = getPersonService().personCreate(this.id, this.name, this.tag, this.passtype);			
			
			if (!result.getBoolean(Constants.SUCCESS)) {
				ret = result.getIntValue(Constants.ERROR_CODE);
				super.setLastErr(result.getString(Constants.ERROR));
				super.setErrCode(ret);
			}
		} else {		
			ret = Constants.LOCAL_ID_NULL;
			super.setLastErr("id is empty");
			super.setErrCode(0);		
		}
		
		return ret;
	}
	
	public int setInfo() {
		int ret = Constants.RETURN_SUCCESS;
		
		if (!this.id.isEmpty()) {
			JSONObject result = getPersonService().personSetInfo(this.id, this.name, this.tag);			
			
			if (!result.getBoolean(Constants.SUCCESS)) {
				ret = result.getIntValue(Constants.ERROR_CODE);
				super.setLastErr(result.getString(Constants.ERROR));
				super.setErrCode(result.getIntValue(Constants.ERROR_CODE));
			}
		} else {		
			ret = Constants.LOCAL_ID_NULL;
			super.setLastErr("id is empty");
			super.setErrCode(0);
		}		
		
		return ret;
	}
	
	public int getInfo() {
		int ret = Constants.RETURN_SUCCESS;
		
		if (!this.id.isEmpty()) {
			JSONObject result = getPersonService().personGetInfo(this.id, this.name);			
			
			if (!result.getBoolean(Constants.SUCCESS)) {
				ret = result.getIntValue(Constants.ERROR_CODE);
				super.setLastErr(result.getString(Constants.ERROR));
				super.setErrCode(result.getIntValue(Constants.ERROR_CODE));
			} else {
				JSONArray person = (JSONArray) result.getJSONArray("person");
				if (person.size() > 0) {
					JSONObject p = (JSONObject) person.get(0);
					this.setId(p.getString(Constants.IDENTY));
					this.setName(p.getString(Constants.NAME));
					this.setFlag(p.getBoolean(Constants.FLAG));
					this.setStep(p.getIntValue(Constants.STEP));
					this.setTag(p.getString(Constants.TAG));
				}
			}
		} else {		
			ret = Constants.LOCAL_ID_NULL;
			super.setLastErr("id is empty");
			super.setErrCode(0);
		}		
		
		return ret;
	}
	
	public int getAuthCode() {
		int ret = Constants.RETURN_SUCCESS;
		
		if (!this.id.isEmpty()) {
			JSONObject result = getPersonService().personGetAuthCode(this.id, this.name);
			
			if (!result.getBoolean(Constants.SUCCESS)) {
				ret = result.getIntValue(Constants.ERROR_CODE);
				super.setLastErr(result.getString(Constants.ERROR));
				super.setErrCode(result.getIntValue(Constants.ERROR_CODE));
			} else {
					this.setAuthCodeString(result.getString(Constants.AUTHCODE));
			}
		} else {		
			ret = Constants.LOCAL_ID_NULL;
			super.setLastErr("id is empty");
			super.setErrCode(0);
		}		
		
		return ret;
	}
	
	public int getIdentifyAuthCode() {
		int ret = Constants.RETURN_SUCCESS;
		
		if (!this.id.isEmpty()) {
			JSONObject result = getPersonService().personGetIdentifyAuthCode(this.passtype);
			
			if (!result.getBoolean(Constants.SUCCESS)) {
				ret = result.getIntValue(Constants.ERROR_CODE);
				super.setLastErr(result.getString(Constants.ERROR));
				super.setErrCode(result.getIntValue(Constants.ERROR_CODE));
			} else {
					this.setAuthCodeString(result.getString(Constants.AUTHCODE));
			}
		} else {		
			ret = Constants.LOCAL_ID_NULL;
			super.setLastErr("id is empty");
			super.setErrCode(0);
		}		
		
		return ret;
	}  
	
	@Deprecated
	public List<Speech> getSpeeches() {
		List<Speech> speechList = new ArrayList<Speech>();
		int ret = Constants.RETURN_SUCCESS;
		
		if (!this.id.isEmpty()) {
			JSONObject result = getPersonService().personGetSpeeches(this.id, this.name);
			
			if (!result.getBoolean(Constants.SUCCESS)) {
				ret = result.getIntValue(Constants.ERROR_CODE);
				super.setLastErr(result.getString(Constants.ERROR));
				super.setErrCode(ret);
			} else {
				JSONArray speeches = (JSONArray) result.getJSONArray("speech");
				Iterator<java.lang.Object> it = speeches.iterator();
				while (it.hasNext()) {
					JSONObject object = (JSONObject) it.next();
					Speech speech = new Speech();
					speech.setId(object.getString(Constants.IDENTY));
					speech.setMD5(object.getString(Constants.MD5));
					speech.setSampleRate(object.getIntValue(Constants.SAMPLE_RATE));
					speech.setCodec(object.getString(Constants.CODEC));
					
					speechList.add(speech);
				}
			}
		} else {
			ret = Constants.LOCAL_ID_NULL;
			super.setLastErr("id is empty");
			super.setErrCode(0);
		}
		
		return speechList;
	}
	
	@Deprecated
	public List<VerifyLog> getLogs(int limit) {
		List<VerifyLog> logList = new ArrayList<VerifyLog>();
		int ret = Constants.RETURN_SUCCESS;

		JSONObject result = getPersonService().personGetLogs(this.id, this.name, limit);

		if (!result.getBoolean(Constants.SUCCESS)) {
			ret = result.getIntValue(Constants.ERROR_CODE);
			super.setLastErr(result.getString(Constants.ERROR));
			super.setErrCode(ret);
		} else {
			JSONArray logs = (JSONArray) result.getJSONArray("log");
			Iterator<java.lang.Object> it = logs.iterator();
			while (it.hasNext()) {
				JSONObject object = (JSONObject) it.next();
				VerifyLog log = new VerifyLog(); 
				log.setId(object.getString(Constants.IDENTY));
				log.setName(object.getString(Constants.NAME));
				log.setScore((float)object.getDoubleValue(Constants.SCORE));
				log.setMatch(object.getBoolean(Constants.MATCH));
				log.setUpdatetime(object.getString(Constants.UPDATE_TIME));

				logList.add(log);
			}
		}

		return logList;
	}
	
	public int addSpeech(Speech speech) {
		int ret = Constants.RETURN_SUCCESS;
		
		if (!this.id.isEmpty()) {
			JSONObject result = getPersonService().personAddSpeech(this.id, this.name, speech.getCodec(), 
					speech.getSampleRate(), speech.getVerify(), speech.getRule(), speech.getData(), this.passtype);
			
			if (!result.getBoolean(Constants.SUCCESS)) {
				ret = result.getIntValue(Constants.ERROR_CODE);
				super.setLastErr(result.getString(Constants.ERROR));
				super.setErrCode(ret);
			} else {
				speech.setMD5(result.getString(Constants.MD5));
			}
		} else {		
			ret = Constants.LOCAL_ID_NULL;
			super.setLastErr("id is empty");
			super.setErrCode(0);		
		}
		
		return ret;
	}
	
	public int removeSpeeches() {
		int ret = Constants.RETURN_SUCCESS;
		
		if (!this.id.isEmpty()) {
			JSONObject result = getPersonService().personRemoveSpeeches(this.id, this.name);			
			
			if (!result.getBoolean(Constants.SUCCESS)) {
				ret = result.getIntValue(Constants.ERROR_CODE);
				super.setLastErr(result.getString(Constants.ERROR));
				super.setErrCode(ret);
			}
		} else {		
			ret = Constants.LOCAL_ID_NULL;
			super.setLastErr("id is empty");
			super.setErrCode(0);		
		}
		
		return ret;
	}
	
	@Deprecated
	public int removeSpeech(Speech speech) {
		int ret = Constants.RETURN_SUCCESS;
		
		if (!speech.getMD5().isEmpty()) {
			JSONObject result = getPersonService().personRemoveSpeech(speech.getMD5());
			
			if (!result.getBoolean(Constants.SUCCESS)) {
				ret = result.getIntValue(Constants.ERROR_CODE);
				super.setLastErr(result.getString(Constants.ERROR));
				super.setErrCode(ret);
			}
		} else {		
			ret = Constants.LOCAL_ID_NULL;
			super.setLastErr("id is empty");
			super.setErrCode(0);		
		}
		
		return ret;
	}
	
	public int reserveSpeeches(int number) {
		int ret = Constants.RETURN_SUCCESS;
		
		if (!this.id.isEmpty()) {
			JSONObject result = getPersonService().personReserveSpeeches(this.id, this.name, number);			
			
			if (!result.getBoolean(Constants.SUCCESS)) {
				ret = result.getIntValue(Constants.ERROR_CODE);
				super.setLastErr(result.getString(Constants.ERROR));
				super.setErrCode(ret);
			}
		} else {		
			ret = Constants.LOCAL_ID_NULL;
			super.setLastErr("id is empty");
			super.setErrCode(0);
		}
		
		return ret;
	}
}