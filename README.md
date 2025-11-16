# Chat-Room / 聊天室

English
-------

Project Name  
Chat-Room

Short Description  
A console-based chat room written in pure Java. No GUI; runs in the terminal/console. The project contains separate server and client implementations. The server listens on TCP port 10000 (hard-coded). The client reads the server address from address.json and connects to port 10000. No external frameworks or third-party libraries are required.

Prerequisites
- Java Development Kit (JDK) 8 or later, recommended JDK 23
- No external dependencies

Repository Structure
- server/                 
  - Main.java
  - Admin.java
  - ClientHandler.java
  - ChatLogger.java
  - legalMembers.ser
- client/                 
  - Main.java
  - Client.java
  - Receive.java
  - address.json            
  - log.txt                 
- README.md

Notes about duplicate Main class name  
Both server and client use a top-level class named Main. Please compile and run server source code and client source code separately (see commands below).

Build & Run (exact commands)

1) Build and run the server
- In the repository root directory, compile only the server-side source code in the server directory:
  ```
  javac -encoding UTF-8 -d out server/*.java
  ```
- Run the server Main:
  ```
  java -Dfile.encoding=UTF-8 -cp out Main
  ```
- Example console output:
  ```
  Server started on port 10000...
  ```

2) Build and run the client
- Prepare address.json in the repository root directory
- Use the Setter under Client-IP-Setter to generate or change address.json
- Compile only the client-side source code in the client directory:
  ```
  javac -encoding UTF-8 -d out client/*.java
  ```
- Run the client Main:
  ```
  java -Dfile.encoding=UTF-8 -cp out Main
  ```

address.json (required by client)
- The client reads the first line of address.json as string s, and uses s.substring(3) as the hostname and port. Please write the first line in the following format (plain text):
  ```
  ip:127.0.0.1:10000
  ```
  or
  ```
  ip:localhost:10000
  ```

legalMembers.ser (required by server)

The server reads the content of legalMembers.ser, which stores a serialized CopyOnWriteArrayList legalUsers.
Use MembersCreator under Serve-Members-Creator to create a serialized file containing only the test user.
Use the /add, /remove, and /showLegal commands in the server admin console to view or modify the serialized file.
- Note that usernames are separated by '\n', so a newline is still required at the end of the file.
- Please try to keep the length of usernames and passwords within 20 characters, otherwise they cannot be fully displayed when using the \showLegal command.

Message Protocol
- All messages are text lines ending with a newline ("\n"). Message prefixes:
  - `join:<username>joined the chat\n`
  - `chat:<username>:<message>\n`
  - `away:<username>left the chat\n`

Admin Console (server-side)
- Supported commands:
  - \exit        : Shut down the server
  - \name        : Change admin display ID
  - \showAll     : Show current online users and count
  - \kick        : Kick out a user
  - \broadcast   : Broadcast a message
  - \add         : Add a connectable username
  - \remove      : Remove a connectable username
  - \showLegal   : Show all connectable usernames and passwords
  - \help        : Command help

Client Usage
- Enter content and press Enter to send a message
- Enter \exit to leave the chat room and disconnect
- If the entered username and password are incorrect or don't exist, you will receive "[Admin]No permission to join this chat room!\n"
- If kicked by admin, you will receive "[Admin]You have been kicked out of the chat\n"

Logging
- ChatLogger appends logs to log.txt, including timestamp and content

Limitations & Recommendations
- Port 10000 is hard-coded and can only be changed by modifying the source code
- No strong authentication, no encryption (for demonstration or learning purposes only)

Contributing
- Fork the repository, create a new branch, implement changes and submit a PR

License
- This project is licensed under the GNU General Public License v3.0 (GPLv3). Please see the LICENSE file for the full license terms.

Contact
- Author: Muimi272
- GitHub: https://github.com/Muimi272


中文（Chinese）
-------------

项目名称  
Chat-Room（聊天室）

简短描述  
基于纯 Java 的控制台聊天室。项目包含独立的服务端与客户端实现。服务器在硬编码的 TCP 端口 10000 上监听。客户端从 address.json 读取服务器地址并连接到 10000 端口。无需任何外部框架或第三方库。

先决条件
- Java JDK 8 或更高，建议使用JDK 23
- 无外部依赖

仓库结构（本仓库中使用的路径）
- server/                 
  - Main.java
  - Admin.java
  - ClientHandler.java
  - ChatLogger.java
  - legalMembers.ser
- client/                 
  - Main.java
  - Client.java
  - Receive.java
  - address.json            
  - log.txt                 
- README.md

关于同名 Main 的说明  
服务端和客户端都使用名为 Main 的顶级类。请分别编译并运行服务端源码与客户端源码（见下方命令）。

构建与运行（精确命令）

1）构建并运行服务器
- 在仓库根目录下，仅编译 server 目录下的服务端源码：
  ```
  javac -encoding UTF-8 -d out server/*.java
  ```
- 运行服务器 Main：
  ```
  java -Dfile.encoding=UTF-8 -cp out Main
  ```
- 控制台示例输出：
  ```
  服务器启动在端口 10000...
  ```

2）构建并运行客户端
- 在仓库根目录准备 address.json
- 使用 Client-IP-Setter 下的 Setter 生成或更改 address.json
- 仅编译 client 目录下的客户端源码：
  ```
  javac -encoding UTF-8 -d out client/*.java
  ```
- 运行客户端 Main：
  ```
  java -Dfile.encoding=UTF-8 -cp out Main
  ```

address.json（客户端必需）
- 客户端读取 address.json 的第一行为字符串 s，并使用 s.substring(3) 作为主机名和端口。请按如下格式写入第一行（纯文本）：
  ```
  ip:127.0.0.1:10000
  ```
  或
  ```
  ip:localhost:10000
  ```

legalMembers.ser（服务端必需）
- 序列化储存 CopyOnWriteArrayList<User> legalUsers。
- 使用Serve-Members-Creator下的MembersCreator创建只含有test用户的序列化文件。
- 在服务端中使用 /add 、 /remove 、 /showLegal 命令查阅或更改序列化文件。
  
消息协议
- 所有消息为以换行符（"\n"）结尾的文本行。消息前缀：
  - `join:<username>加入了聊天\n`
  - `chat:<username>:<message>\n`
  - `away:<username>退出了聊天\n`

管理员控制台（服务端）
- 支持如下命令：
  - \exit        ：关闭服务器
  - \name        ：更改管理员显示 ID
  - \showAll     ：显示当前在线用户及数量
  - \kick        ：踢出用户
  - \broadcast   ：广播消息
  - \add         ：添加可连接的用户名
  - \remove      ：删除可连接的用户名
  - \showLegal   ：显示所有可连接的用户名及密码
  - \help        ：命令帮助

客户端使用
- 输入内容回车发送消息
- 输入 \exit 离开聊天室并断开连接
- 输入的用户名和密码如果错误或者不存在将会收到 "[Admin]无权限加入此聊天室！\n"
- 被管理员踢出会收到 "[Admin]你已被踢出聊天\n"

日志
- ChatLogger 将日志追加到 log.txt，含时间戳和内容

限制建议
- 端口 10000 为硬编码，仅可修改源码变更
- 无强认证、无加密（仅演示或学习用）

贡献
- Fork 仓库、新建分支、实现后提 PR

许可证
- 本项目采用 GNU 通用公共许可证 (GPLv3)，请参见 LICENSE 文件获取完整授权条款。

联系方式
- 作者：Muimi272
- GitHub：https://github.com/Muimi272
