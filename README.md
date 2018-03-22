## 简介  
基于SpringBoot + SpringDataJPA打造的快速构建Restul API的项目。

## 功能  
* 最具Java风格的项目结构、配置文件、精简的POM
* 统一响应结果封装  
* 统一异常处理
* 简单的普通用户登录注册
* 基于[JWT](https://www.jianshu.com/p/576dbf44b2ae)的接口权限认证
* 基于logback的生产环境日志按天滚动记录
* 常用基础方法抽象封装

## 项目结构  
- aspect: AOP切面类
- config: 项目配置
- controller: API路由控制层
- dataobject: javebean对象
    * dto: data transfer object数据传输对象(在controller-service-repository三层之间传输)  
    * model: 数据持久模型(对应数据库字段)
    * ro: request object数据请求对象(前端传过来的数据的封装)
    * vo: view obejct视图对象(返回给前端数据的封装)
- enum: 枚举类
- exception: 自定义异常类
    * handler: 异常处理类
- repository: 数据仓库(采用的SpringDataJPA作为的数据持久层)
- service: 业务接口
    * impl: 业务实现
- utils: 项目工具集合

## 开发环境
* JDK 8
* Maven 3
* Mysql 5.7
