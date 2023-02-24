# unicorn  
本项目，主要采用netty,mybatis,druid,springboot等开源框架技术。最终期望，本项目成为一个高负载，高并发的TCP服务器。本项目开发主要目的，是想让自己能够更系统，全面且静下心来梳理相关知识点。与此同时，提高自己，也希望对其它准备相关项目的小伙伴，起到抛砖引玉的作用。当然，只要是代码就会BUG，你发现有什么不对或者不合理地方，非常欢迎和谢谢指正。
本项目的代码，在作者的博客网站，[迦识](http://www.wejias.com/html/article/article-list-9.html 迦识-网络编程")上有更详细的说明.

---
主要运用的框架与技术:  
springboot进行对象管理    
mybatisplus进行数据库操作   
druid做数据库连接池    
redis做数据缓存[目前还未接入]    


## Message 规则
|-----------------------------------------------------------------------------|  
|                 HEAD        |             BODY                              |  
|-----------------------------------------------------------------------------|  
|LEN|SIGN|MSG-ID|code|timestamp|seq|             BODY                         |  
|-----------------------------------------------------------------------------|  
|2  |32  |2     |4   |8        |4  |          32767-(2+32+2+4+8+4)            |  
|-----------------------------------------------------------------------------|


- HEAD 消息头
- BODY 消息体

- MSG-ID 
  消息ID 2字节.0x1~0xFFFF,请求与返回为不一样的ID.
- code
  请求时为0
  返回时默认为0，当有异常情况时根据实际情况设置
- timestamp
  时间戳，8个字节.
- seq
  序列号,4个字节.客户端维护.每个用户从登录连接开始递增.
- sign
  签名：MD5(MSG-ID&code&timestamp&seq&body)
- LEN 整个包总长度
  2+8+4+32+2+BODY.length.目前最大度32767
- body 采用pb
