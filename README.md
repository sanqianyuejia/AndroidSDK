# 声纹识别开发平台Android SDK
作为一种核心的语音技术，声纹识别技术在通信技术、语音技术得到飞速发展的今天有着巨大的应用潜力，相关产品已经应用于公共安全、社保身份认证、社区矫正，金融投资服务领域的身份辨认，智能手机移动商务和行业用户满意度调查等不同领域。

声纹识别开发平台由快商通声纹(www.shengwenyun.com)研发，平台提供一个分布式声纹识别解决方案，可根据应用规模适当增减服务器，灵活性非常强大，在以下领域有成功案例：

- 司法社区矫正
- 远程教育身份认证
- 高校教学实验平台

![](voiceplusplus.jpg)

## 平台特征介绍

- 文本相关，方便快捷。快商通声纹识别系统基于文本相关声纹识别，用户只需要一句简短口令，即可实时验证身份。
- 领先算法，精准识别。采用独特的声纹识别处理算法，声纹验证准确率高达99.5%，支持防录音攻击、环境降噪、动态数字口令。
- 分布式部署，扩展性强。分布式部署声纹识别服务器，支持上百万的声纹容量，易容扩展，而且可以根据应用规模适当调整硬件配置。
- 简约API，快速集成。提供一组RestFul风格API，支持多终端、多平台集成调用。可针对特定语言定制SDK开发包。

## 快速上手
请参考以下步骤进行开发

1. 申请api key和secret（请发邮件至lixm@shengwenyun.com，邮件标题置为“快商通声纹识别开发平台：API调用申请”）
2. 参照example目录中的例子，编写应用程序
3. 上线运营。上线之前，请联系管理员（lixm@shengwenyun.com）

## 开发示例——注册说话人声纹

import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.IOException;  

import utils.Constants;  
import client.Client;  
import model.Person;  
import model.Speech;  

public class Test {
    
		int ret = -1;		
		String idString = "6db60c3d588f3f40";			// 群组编号
		String nameString = "test";			// 说话人别名，同一群组内必须唯一
		String pwdString = "*";	// 口令内容	
		
		// Create server
		Client client = new Client("53de3f4d0d34cb2f86d01d0cfd21f138", "53de3f4d0d34cb2f86d01d0cfd21f138");
		client.setServer("open.shengweyun.cn", 81, "1", Constants.TEXT_DEPENDENT);
		
		// Delete Person
		Person person = new Person(client, idString, nameString);
		person.setPassType(Constants.VOICEPRINT_TYPE_RANDOM_DIGITS);	// 随机数字口令
		if ((ret = person.delete()) != Constants.RETURN_SUCCESS) {
			System.err.println(person.getLastErr()+":"+String.valueOf(ret));			
		}
		
		// Create Person
		if ( (ret = person.getInfo()) != Constants.RETURN_SUCCESS) {
			if ( (ret = person.create()) != Constants.RETURN_SUCCESS) {
				System.err.println(person.getLastErr()+":"+String.valueOf(ret));
			}
		}
		
		// 获取person对应的系统信息
		if ((ret = client.getSysInfo(person)) != Constants.RETURN_SUCCESS) {
			System.err.println(client.getLastErr()+":"+String.valueOf(ret));			
		}
		
		// Create Speech
		Speech speech = new Speech("pcm/raw", 16000, true);		
		speech.setRule(pwdString);
		
		// Add Speech to person
		if ( (ret = person.getInfo()) != Constants.RETURN_SUCCESS) {
			System.err.println(person.getLastErr()+":"+String.valueOf(ret));
		} else if (person.getFlag() == false) {
			System.out.println(person.getId()+"\t"+person.getName()+"\t"+person.getStep()+"/"+client.getRegSteps());
			for (String filepath : args) {
				if ( (ret = person.getAuthCode()) != Constants.RETURN_SUCCESS) {
					System.err.println(person.getLastErr()+":"+String.valueOf(ret));
				} else {
					speech.setRule(person.getAuthCodeString());
					speech.setData(readWavform("wav/"+filepath));		// readWavform是读文件到byte缓冲的函数
					if ((ret = person.addSpeech(speech)) != Constants.RETURN_SUCCESS) {
						System.err.println(person.getLastErr()+":"+String.valueOf(ret));
					}
				}
			}
			
			// Register voiceprint for speaker
			if ((ret = client.registerVoiceprint(person)) != Constants.RETURN_SUCCESS) {
				System.err.println(client.getLastErr()+":"+String.valueOf(ret));
			}
			
			// Output result
			System.out.println(person.getId()+"\t"+person.getName()+": Register voiceprint success.");
		}
		
		// Verify voiceprint for speaker
		VerifyRes res = new VerifyRes();
		speech.setRule("5318"); 
		speech.setData(readWavform("wav/ver_4digits_5318.wav"));
		if ((ret = client.verifyVoiceprint(person, speech, res)) != Constants.RETURN_SUCCESS) {
			System.err.println(client.getLastErr()+":"+String.valueOf(ret));
		}
		
		// Output result
		System.out.println(person.getId()+"\t"+person.getName()+": "+res.getResult()+"-"+res.getSimilarity());
		
		// Identify voiceprint for speaker
		if ((ret = client.identifyVoiceprint_2(person, speech, res)) != Constants.RETURN_SUCCESS) {
			System.err.println(client.getLastErr()+":"+String.valueOf(ret));
		}
		
		// Output result
		System.out.println(person.getId()+"\t"+person.getName()+": "+res.getResult()+"-"+res.getSimilarity());
}

## 错误代码对照表
<table cellpadding="0" cellspacing="1" border="0" style="width:100%" class="tableborder">
<tbody><tr>
<th>错误代码</th>
<th>错误信息</th>
<th>详细描述</th>
</tr>

<tr>
<td class="td"><strong>1001</strong></td>
<td class="td">Auth fail</td>
<td class="td">Key或Secret错误</td>
</tr>

<tr>
<td class="td"><strong>1002</strong></td>
<td class="td">network error</td>
<td class="td">网络错误</td>
</tr>

<tr>
<td class="td"><strong>1003</strong></td>
<td class="td">internal error</td>
<td class="td">未知错误</td>
</tr>

<tr>
<td class="td"><strong>1004</strong></td>
<td class="td">argument error</td>
<td class="td">参数错误</td>
</tr>

<tr>
<td class="td"><strong>1005</strong></td>
<td class="td">database error</td>
<td class="td">数据库错误</td>
</tr>

<tr>
<td class="td"><strong>1006</strong></td>
<td class="td">system file lost</td>
<td class="td">系统文件丢失</td>
</tr>

<tr>
<td class="td"><strong>2001</strong></td>
<td class="td">person already exist</td>
<td class="td">说话人已存在</td>
</tr>

<tr>
<td class="td"><strong>2002</strong></td>
<td class="td">person not exist</td>
<td class="td">说话人不存在</td>
</tr>

<tr>
<td class="td"><strong>2003</strong></td>
<td class="td">has no persons</td>
<td class="td">没有任何说话人</td>
</tr>

<tr>
<td class="td"><strong>2004</strong></td>
<td class="td">person has no speeches</td>
<td class="td">该说话人没有登记语音</td>
</tr>

<tr>
<td class="td"><strong>3001</strong></td>
<td class="td">voiceprint state error</td>
<td class="td">声纹训练状态错误</td>
</tr>

<tr>
<td class="td"><strong>3002</strong></td>
<td class="td">voiceprint already registered</td>
<td class="td">说话人声纹已注册</td>
</tr>

<tr>
<td class="td"><strong>3003</strong></td>
<td class="td">voiceprint has no speeches</td>
<td class="td">该说话人不存在登记语音</td>
</tr>

<tr>
<td class="td"><strong>3004</strong></td>
<td class="td">verify error</td>
<td class="td">验证异常</td>
</tr>

<tr>
<td class="td"><strong>3005</strong></td>
<td class="td">voiceprint not trained</td>
<td class="td">没有登记声纹</td>
</tr>

<tr>
<td class="td"><strong>3006</strong></td>
<td class="td">identify error</td>
<td class="td">声纹辨认发生错误</td>
</tr>

<tr>
<td class="td"><strong>4001</strong></td>
<td class="td">speech too short</td>
<td class="td">语音太短</td>
</tr>

<tr>
<td class="td"><strong>4002</strong></td>
<td class="td">speech too long</td>
<td class="td">语音太长</td>
</tr>

<tr>
<td class="td"><strong>4003</strong></td>
<td class="td">speech sample rate error</td>
<td class="td">语音采样率错误，目前仅支持8k</td>
</tr>

<tr>
<td class="td"><strong>4004</strong></td>
<td class="td">speech already exist</td>
<td class="td">语音已存在</td>
</tr>

<tr>
<td class="td"><strong>4005</strong></td>
<td class="td">speech not exist</td>
<td class="td">语音不存在</td>
</tr>

<tr>
<td class="td"><strong>4006</strong></td>
<td class="td">speech process error</td>
<td class="td">语音处理错误</td>

<tr>
<td class="td"><strong>5001</strong></td>
<td class="td">asr recognize error</td>
<td class="td">语音识别错误</td>
</tr>

<tr>
<td class="td"><strong>5002</strong></td>
<td class="td">asr not matched</td>
<td class="td">语音内容不匹配</td>

</tr>

</tbody></table>
	

## 下载

### 从 release 版本下载

下载地址：https://github.com/sanqianyuejia/JavaSDK/releases

## 许可证

Copyright (c) 2013 快商通信息技术有限公司

基于 MIT 协议发布:

* [www.opensource.org/licenses/MIT](http://www.opensource.org/licenses/MIT)
