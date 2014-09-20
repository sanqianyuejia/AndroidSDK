package com.kuaishangtong.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.kuaishangtong.service.ClientService;
import com.kuaishangtong.utils.Constants;
import com.kuaishangtong.model.Speech;
import com.kuaishangtong.model.Person;
import com.kuaishangtong.Object.Object;
import com.kuaishangtong.client.VerifyRes;

public class Client extends Object {
	private String key;
	private String secret;
	private String version;
	private int type;
	private int reg_steps;
	private int ver_steps;
	private String server;

	private ClientService cs;

	/**
	 * Constructor
	 * 
	 * @param key
	 * @param secret
	 * @param version
	 *            : ����汾��
	 * @param type
	 *            : �����ֶ�
	 */
	public Client(String key, String secret) {
		this.key = key;
		this.secret = secret;
		this.reg_steps = 10;
		this.ver_steps = 2;
	}

	public ClientService getClientService() {
		if (cs == null) {
			cs = (ClientService) new ClientService().setClient(this);
		}

		return cs;
	}

	/**
	 * Set Server's parameters
	 * 
	 * @param host
	 * @param port
	 * @return
	 */
	public synchronized int setServer(String host, int port, String version,
			int type) {
		this.version = version;
		this.type = type;
		this.server = "http://" + host + ":" + String.valueOf(port) + "/"
				+ this.version;

		return Constants.RETURN_SUCCESS;
	}

	public String getServerString() {
		return this.server;
	}
	
	public int getSysInfo(int type) {
		int ret = Constants.RETURN_SUCCESS;
		
		JSONObject result = getClientService().clientGetSysInfo(type);			
		if (!result.getBoolean(Constants.SUCCESS)) {
			ret = result.getIntValue(Constants.ERROR_CODE);
			super.setLastErr(result.getString(Constants.ERROR));
			super.setErrCode(result.getIntValue(Constants.ERROR_CODE));
		} else {
			JSONArray authcode = (JSONArray) result.getJSONArray("authcode");
			if (authcode.size() > 0) {
				JSONObject p = (JSONObject) authcode.get(0);
				this.setRegSteps(p.getIntValue(Constants.RSTEPS));
				this.setVerSteps(p.getIntValue(Constants.VSTEPS));
			}
		}
				
		return ret;
	}
	
	@Deprecated
	public List<Person> getPersons(int limit) {
		List<Person> personList = new ArrayList<Person>();
		int ret = Constants.RETURN_SUCCESS;

		JSONObject result = getClientService().personFindAll(limit);

		if (!result.getBoolean(Constants.SUCCESS)) {
			ret = result.getIntValue(Constants.ERROR_CODE);
			super.setLastErr(result.getString(Constants.ERROR));
			super.setErrCode(ret);
		} else {
			JSONArray persons = (JSONArray) result.getJSONArray("person");
			// Iterator<JSONObject> it = persons.iterator();
			Iterator<java.lang.Object> it = persons.iterator();
			while (it.hasNext()) {
				JSONObject object = (JSONObject) it.next();
				Person person = new Person(this); 
				person.setId(object.getString(Constants.IDENTY));
				person.setName(object.getString(Constants.NAME));
				person.setTag(object.getString(Constants.TAG));
				person.setFlag(object.getBoolean(Constants.FLAG));

				personList.add(person);
			}
		}

		return personList;
	}

	/**
	 * Verify speaker's voiceprint
	 * 
	 * @param person
	 * @param speech
	 * @return
	 */
	public synchronized int verifyVoiceprint(Person person, Speech speech,
			VerifyRes res) {
		int ret = Constants.RETURN_SUCCESS;

		if (!person.getId().isEmpty()) {
			JSONObject result = getClientService().clientVerifyVoiceprint(
					person.getId(), person.getName(), speech.getCodec(), speech.getSampleRate(),
					speech.getVerify(), speech.getRule(), speech.getData());

			if (!result.getBoolean(Constants.SUCCESS)) {
				ret = result.getIntValue(Constants.ERROR_CODE);
				super.setLastErr(result.getString(Constants.ERROR));
				super.setErrCode(ret);
			} else {
				res.setResult(result.getBoolean(Constants.RESULT));
				res.setSimilarity(result.getDouble(Constants.SIMILARITY));
			}
		} else {
			ret = Constants.LOCAL_ID_NULL;
			super.setLastErr("id is empty");
			super.setErrCode(0);
		}

		return ret;
	}

	public synchronized int identifyVoiceprint(Person person, Speech speech,
			VerifyRes res) {
		int ret = Constants.RETURN_SUCCESS;

		JSONObject result = getClientService().clientIdentifyVoiceprint(person.getId(),
				speech.getCodec(), speech.getSampleRate(), speech.getData());

		if (!result.getBoolean(Constants.SUCCESS)) {
			ret = result.getIntValue(Constants.ERROR_CODE);
			super.setLastErr(result.getString(Constants.ERROR));
			super.setErrCode(ret);
		} else {
			person.setId(result.getString(Constants.ID));
			person.setName(result.getString(Constants.NAME));
			res.setResult(result.getBoolean(Constants.RESULT));
			res.setSimilarity(result.getDouble(Constants.SIMILARITY));
		}

		return ret;
	}
	
	public synchronized int identifyVoiceprint_2(Person person, Speech speech,
			VerifyRes res) {
		int ret = Constants.RETURN_SUCCESS;

		JSONObject result = getClientService().clientIdentifyVoiceprint_2(person.getId(),
				speech.getCodec(),speech.getSampleRate(), speech.getVerify(), speech.getRule(), speech.getData());

		if (!result.getBoolean(Constants.SUCCESS)) {
			ret = result.getIntValue(Constants.ERROR_CODE);
			super.setLastErr(result.getString(Constants.ERROR));
			super.setErrCode(ret);
		} else {
			person.setId(result.getString(Constants.ID));
			person.setName(result.getString(Constants.NAME));
			res.setResult(result.getBoolean(Constants.RESULT));
			res.setSimilarity(result.getDouble(Constants.SIMILARITY));
		}

		return ret;
	}

	/**
	 * Update speaker's voiceprint
	 * 
	 * @param voiceprint
	 * @return
	 */
	public synchronized int updateVoiceprint(Person person) {
		int ret = Constants.RETURN_SUCCESS;

		if (!person.getId().isEmpty()) {
			JSONObject result = getClientService().clientRegisterVoiceprint(
					person.getId(), person.getName(), true);

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

	/**
	 * Rgister speaker's voiceprint
	 * 
	 * @param voiceprint
	 * @return
	 */
	public synchronized int registerVoiceprint(Person person) {
		int ret = Constants.RETURN_SUCCESS;

		if (!person.getId().isEmpty()) {
			JSONObject result = getClientService().clientRegisterVoiceprint(
					person.getId(), person.getName(), false);

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

	public String getKey() {
		return this.key;
	}

	public String getSecret() {
		return this.secret;
	}
	
	public int getRegSteps() {
		return this.reg_steps;
	}
	
	public int getVerSteps() {
		return this.ver_steps;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	private void setRegSteps(int steps) {
		this.reg_steps = steps;
	}
	
	private void setVerSteps(int steps) {
		this.ver_steps = steps;
	}

	public String getType() {
		if (this.type == Constants.TEXT_INDEPENDENT) {
			return Constants.TEXT_INDEPENDENT_STR;
		} else if (this.type == Constants.TEXT_PROMPT) {
			return Constants.TEXT_PROMPT_STR;
		} else {
			return Constants.TEXT_DEPENDENT_STR;
		}
	}
}