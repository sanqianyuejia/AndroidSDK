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

## 开发示例——说话人声纹操作：登记、认证和识别

    //1.初始化
    private Client client=null;    
    private Person person=null;
    private Speech speech=null;
    private VPRService vservice=null;

    //初始化
    client = new Client(clientKey,clientSecret);
    client.setServer(serverHost,serverPort,serverVersion);

    person = new Person(client, group_id, username); 
    person.setPassType(keyway); // 随机数字口令

    speech = new Speech("pcm/raw", 16000, true);

    // 获取服务实例
    this.vservice=VPRService.getInstance();

    //2.参数设置
    // 设置服务参数
    this.vservice.setServiceParam(client, person, speech);

    // 设置录音回调接口
    this.vservice.setRecorderListener(recorderListener);

    //设置声纹服务回调接口
    this.vservice.setVPRListener(vprListener);

    // 设置声纹服务模式，serviceMode可为VPRService.REGISTER、VPRService.VERIFY和VPRService.IDENTIFY
    // 调用该方法会初始化声纹口令和声纹服务模式
    this.vservice.initService(serviceMode);

    //3.接口函数实现
    // 声纹服务接口函数实现
    private VPRListener vprListener =new VPRListener(){

        @Override
        //初始化声纹服务
        public void onServiceInit(boolean flag,int stepNum,int statusNum, String keyString) {
            //当flag为true，此处提示用户初始朗读的口令keyString和服务所需步数statusNum
            //（在VPRService.REGISTER服务模式下，可提示当前进度步数stepNum）
            //当flag为false，可在onServiceError打印错误信息
        }

        @Override
        //录音上传结果
        public void onSpeechResult(boolean flag) {
            //当flag为false，可提示用户口令朗读错误
        }

        @Override
        //服务结果回调
        public void onServiceEnd(boolean flag, Person person, double similarity) {
            //当flag为false，可提示用户验证失败
            //当flag为true，可提示用户进行下一步操作，如跳转
            //(在VPRService.IDENTIFY服务模式下，可提示识别结果person)
            //similarity可以帮助开发者获取声纹相似度

        }


        @Override
        //在onSpeechResult和onServiceEnd后被调用，表示当前进度情况
        public void onFlowStepChanged(int stepNum, int statusNum, String keyString) {
            //此处可提示用户当前进度步数stepNum，服务总步数statusNum，
            //并刷新当前口令keyString

        }

        @Override
        //服务出错
        public void onServiceError(VPRError error) {
            //此处可打印声纹服务的一些错误，例如口令，网络连接等。
            Log.d("ServiceError",error.getErrorStr());
        }

    };

    // 录音接口函数实现
    private RecorderListener recorderListener =new RecorderListener(){

        // 录音开始时调用
        @Override    
        public void onRecordBegin() {
            // 此处显示录音启动图标，提示用户开始录音
        }

        // 返回实时录音音量，soud为音量大小，取值范围为0-2^15（32768）
        @Override    
        public void onSoundChanged(final float sound) {
            // 此处显示实时音量图标，提示用户音量大小变化 
        }

        // 录音结束时调用，并可在此处开启声纹服务
        @Override
        public void onRecordEnd() {
            // 此处提示用户录音结束

            // 可在此处开启声纹服务
            vservice.startService();
        }

        // 录音异常时调用
        @Override
        public void onRecordError(VPRError error) {
            // 此处可打印录音服务异常
            Log.d(“RecordError",error.getErrorStr());
        } 

    };

    //4.功能使用
    //开始录音
    vservice.startRecord();

    //停止录音，并在它的回调函数onRecordEnd()中启动声纹服务vservice.startService()
    vservice.stopRecord();
    
    //5.其他操作
    //主要用来在VPRService.REGISTER模式下会重新注册登记声纹信息
    vservice.resetService(serviceMode);
    //用来设置声纹服务中的person值，也可调用setServiceParam()方法
    vservice.setPerson(person);
    
## 开发示例——说话人操作
    
        //1.初始化
    private Client client=null;    
    private VPRService vservice=null;
    
    //初始化
    client = new Client(clientKey,clientSecret);
    client.setServer(serverHost,serverPort,serverVersion);
    // 获取服务实例
    this.vservice=VPRService.getInstance();
    // 设置服务参数，(进行说话人操作时，必须先设置client参数)
    this.vservice.setServiceParam(client, null, null);
    
    //以下为说话人操作，(注意，以下方法会等待说话人操作的线程结束后才返回结果)
    //获取群组内所登记和未登记声纹的说话人信息，当结果为空时，返回空集
    List<person>=this.vservice.getPersonGroup(limit,groupId);
    //查询说话人是否在该群组内存在
    boolean exist=this.vservice.personExist(groupId,userName);
    //删除说话人信息及其声纹信息
    boolean delete=this.vservice.deletePerson(groupId,userName);
    //查询说话人信息，当结果为空时，返回null
    Person person=this.vservice.getPersonInfo(groupId,userName);
    //设置说话人信息
    boolean setinfo=this.vservice.setPersonInfo(gropId,userName,tag);

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
