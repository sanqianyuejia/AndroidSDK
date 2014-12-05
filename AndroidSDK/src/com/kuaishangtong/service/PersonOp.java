package com.kuaishangtong.service;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.kuaishangtong.client.Client;
import com.kuaishangtong.model.Person;
import com.kuaishangtong.utils.Constants;

public class PersonOp {
	private Client client=null;
	private Person person=null;
	private boolean paused;
	
	private int ret = -1;
	
	private List<Person> personList;
	private int limit;
	private String groupid;
	private int errcode = 0;
	
	public PersonOp(Client client){
		this.client=client;
	}
	
	public List<Person> getPersonGroup(int limit,String groupid){
		if(client == null)
		{
			Log.d("getPersonGroup","client is null");
			return personList;
		}
		this.personList = null;
		this.limit=limit;
		this.groupid=groupid;
		
		Thread groupThread=null;
		groupThread=new Thread(new GroupThread());
		
		paused=false;
		groupThread.start();
		
		
		while(personList == null);
		
		return personList;
	}
	
	public boolean personExist(String groupId,String userName){
		if(client == null)
		{
			Log.d("personExist","client is null");
			return false;
		}
		this.people=new Person(client, groupId, userName);
		this.ret = -1;
		
		Thread existThread=null;
		existThread=new Thread(new GetInfoThread());
		
		paused=false;
		existThread.start();
		
		
		while(ret == -1);
		if(ret == Constants.RETURN_SUCCESS)
			return true;
		else
			return false;
	}
	
	private Person people;
	
	public boolean deletePerson(String groupId,String userName){
		if(client == null){
			Log.d("deletePerson","client is null");
			return false;
		}
		
		this.people=new Person(client, groupId, userName);
		this.ret=-1;
		
		Thread deleteThread=null;
		deleteThread=new Thread(new DeleteThread());
		
		paused=false;
		deleteThread.start();
		
		while(ret == -1);
		if(ret == Constants.RETURN_SUCCESS)
			return true;
		else
			return false;
	} 
	
	public Person getPersonInfo(String groupId,String userName){
		if(client == null){
			Log.d("getPersonInfo","client is null");
			return null;
		}
		
		this.people=new Person(client, groupId, userName);
		this.ret = -1;
		
		Thread getinfoThread=null;
		getinfoThread=new Thread(new GetInfoThread());
		
		paused=false;
		getinfoThread.start();
		
		while(ret == -1);
		if(ret == Constants.RETURN_SUCCESS)
			return this.people;
		else
			return null;
	}
	
	public boolean setPersonInfo(String groupId,String userName,String tag){
		if(client == null){
			Log.d("setPersonInfo","client is null");
			return false;
		}
		
		this.people = new Person(client, groupId, userName);
		this.people.setTag(tag);
		
		this.ret = -1;
		Thread setInfoThread=null;
		setInfoThread=new Thread(new SetInfoThread());
		
		paused=false;
		setInfoThread.start();
		
		while(ret == -1);
		if(ret == Constants.RETURN_SUCCESS)
			return true;
		else
			return false;
	}
	
	private class GroupThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(!paused){
				try{
					personList=client.getGroup(limit, groupid);
				}catch(Exception e){
					Log.d("getPersonGroupError",e.getMessage());
					personList = new ArrayList<Person>();
				}
			}
		}	
	}
	
	private class GetInfoThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(!paused){       
				try{
					if ( (ret = people.getInfo()) != Constants.RETURN_SUCCESS) {
						Log.d("person.getInfo()",people.getLastErr()+":"+String.valueOf(ret));
					}
				}
				catch(RuntimeException e)
				{
					Log.d("person.getInfo()/Error",e.getMessage());
					ret=-11;
				}
			}
		}
	}
	
	private class SetInfoThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(!paused){
				try{
					if((ret=people.setInfo())!= Constants.RETURN_SUCCESS)
					{
						Log.d("person.setInfo()",people.getLastErr()+":"+String.valueOf(ret));	
					}
				}catch(Exception e){
					Log.d("person.setInfo()/Error",e.getMessage());
					ret=-11;
				}
			}
		}
	}
	
	private class DeleteThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(!paused){        
				try{
					if ((ret = people.delete()) != Constants.RETURN_SUCCESS) {	
						Log.d("Delete Person",people.getLastErr()+":"+String.valueOf(ret));			
					}
				}
				catch(RuntimeException e)
				{
					Log.d("deletePersonError",e.getMessage());
					ret=-11;
				}
			}
		}	
	}
}
