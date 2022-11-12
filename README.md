----------一、idea一些初始化配置----------
1、安装插件
lombok
maven helper
free-mybatis-plugin（https://github.com/chuntungho/free-mybatis-plugin/releases）
2、提示忽略大小写配置
3、设置多行显示


----------二、关于日志----------
常有的日志框架有：jul、log4j、log4j2、logback；常用的日志门面是slf4j
不同的日志框架有不同的api，并且通过不同的代码来获取日志类Logger
如果项目迭代的后期我们想要切换日志框架，那么就需要修改代码，这是一个繁琐的操作
日志门面的出现就是为了解决这种问题，日志门面提供统一的接口，具体底层的日志框架实现是什么并不关心
引进日志门面，代码中使用的是日志门面提供的接口，后期如果修改日志框架的实现，并不需要修改代码

参考文档：https://blog.51cto.com/u_10180481/2947302

a. 单独使用log4j
（log4j的版本号都是1.x开头）
引进的依赖示例如下：
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
获取Logger类的方式：
import org.apache.log4j.Logger;
Logger logger = Logger.getLogger(Test.class);
日志配置文件名为：log4j.properties; 存放路径为resources目录下

b. 单独使用log4j2
（log4j2的版本号都是2.x开头）
引进的依赖示例如下：
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.17.1</version>
    </dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.17.1</version>
</dependency>
获取Logger类的方式：
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
Logger logger = LogManager.getLogger(Test.class);
日志配置文件名为：log4j2.properties; 存放路径为resources目录下

c. 使用日志门面slf4j，并且使用log4j作为实现
引进的依赖示例如下：
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.25</version>
</dependency>
获取Logger类的方式：
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
Logger logger = LoggerFactory.getLogger(Test.class);
日志配置文件名为：log4j.properties; 存放路径为resources目录下

d. 使用日志门面slf4j，并且使用log4j2作为实现
引进的依赖示例如下：
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-slf4j-impl</artifactId>
    <version>2.17.1</version>
</dependency>
获取Logger类的方式：
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
Logger logger = LoggerFactory.getLogger(Test.class);
日志配置文件名为：log4j2.properties; 存放路径为resources目录下