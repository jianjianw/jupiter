###项目启动说明
找到JupiterApplication,右键，run

###项目文件夹说明
每一个单独的模块为一个文件夹，统一位于项目jupiter文件夹下，每个项目文件夹下，可以新建相关的
1. aop ，存放aop相关的类
    annotation 存放注解
2. config ，存放项目配置文件
3. util 存放项目util类
4. exception 存放异常相关处理
5. web 存放项目
6. constant 存放常量


###启动规则
项目启动支持多环境，默认启动环境在application.properties中配置
spring.profiles.active=dev
其中 
application.properties 存放共同环境的配置
application_dev.properties 存放开发环境的配置
application_pro.properties 存放正式环境的配置

###vo po 说明
po-数据库对象
vo-业务对象
dto-数据传输对象

###异常说明
所有的异常需要在exception中的ExceptionEnum 枚举中说明，并在
service层中抛出。这样方便做全局拦截

