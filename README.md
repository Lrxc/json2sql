#### 抄袭至:[GitHub - biyanwen/json2sql: 将Json转为sql](https://github.com/biyanwen/json2sql)

再次版本上做了一些改变,主要如下:

- 支持直接修改字段名
- 支持添加新的字段


setting.xml 配置
```cassandraql
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">

    <mirrors>
        <mirror>
            <id>huaweicloud</id>
            <mirrorOf>*</mirrorOf>
            <url>http://repo.huaweicloud.com/repository/maven/</url>
        </mirror>
    </mirrors>

    <!-- 省略其他配置 -->
    <servers>
        <server>
            <id>ossrh</id>
            <username>rxc</username>
            <password>Sonatype1234@</password>
        </server>
    </servers>

    <profiles>
        <profile>
            <id>ossrh</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <!-- 生成密钥时输入的passphrase -->
                <gpg.passphrase>123123123</gpg.passphrase>
                <!-- gpg.exe的绝对路径，在gpg安装目录下的bin目录中 -->
                <gpg.executable>C:\Program Files (x86)\GnuPG\bin\gpg.exe</gpg.executable>
                <!-- 上一步执行gpg -list-key命令输出的路径，一般在C:\User\当前用户\AppData\Roaming\gnupg -->
                <gpg.homedir>C:\Users\wangxin13\AppData\Roaming\gnupg</gpg.homedir>
            </properties>
        </profile>
    </profiles>
</settings>
```