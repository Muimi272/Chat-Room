# Chat-Room / 聊天室

English
-------

Project name  
Chat-Room

Short description  
A simple console-based chat room written in pure Java. No GUI; runs in the terminal/console. The project contains separate server and client implementations. The server listens on TCP port 10000 (hard-coded). The client reads the server address from address.json and connects to port 10000. No external frameworks or third‑party libraries are required.

Prerequisites
- Java Development Kit (JDK) 8 or later
- No external dependencies

Repository layout (precise paths used in this repo)
- server/                 # server-side .java files (Server Main and supporting classes)
  - Main.java
  - Admin.java
  - ClientHandler.java
  - ChatLogger.java
- client/                 # client-side .java files (Client Main and supporting classes)
  - Main.java
  - Client.java
  - Receive.java
- address.json            # required by the client (see format below)
- log.txt                 # created/appended by server logging (ChatLogger)
- README.md

Notes about duplicate class name  
Both server and client use a top-level class named Main in the default package. To compile and run them without class name conflicts, compile/run server sources and client sources separately (see commands below).

Build & Run (exact commands)

1) Build and run the server
- From the repository root, compile only the server-side sources into an output directory:
  ```bash
  javac -d out server/*.java
  ```
- Run the server Main (run from the compiled server classes):
  ```bash
  java -cp out Main
  ```
- Expected console output:
  ```
  服务器启动在端口 10000...
  ```
- The server will create or append to log.txt and start an admin console on the server's standard input.

2) Build and run the client
- Prepare address.json in the repository root (see format below).
- Use the Setter under Client-IP-Setter to generate or change the address.json .
- From the repository root, compile only the client-side sources into an output directory:
  ```bash
  javac -d out client/*.java
  ```
- Run the client Main (run from the compiled client classes):
  ```bash
  java -cp out Main
  ```
- The client will prompt for an ID and then attempt to connect to the server at port 10000.

address.json (required by the client)
- The client reads the first line of address.json into a String s and uses s.substring(3) as the host. To make this work, put the host after a two-character prefix and a colon, for example:
  ```
  ip:127.0.0.1
  ```
  or
  ```
  ip:localhost
  ```
- With "ip:127.0.0.1", s.substring(3) yields "127.0.0.1" which the client uses as the hostname.

Message protocol (implementation details)
- All messages are text lines that end with a newline ("\n"). The server and clients use these message prefixes:
  - `join:<username>加入了聊天\n`   -- client sends when joining
  - `chat:<username>:<message>\n`   -- client sends chat messages
  - `away:<username>退出了聊天\n`   -- client sends when leaving
- Admin messages and broadcasts are prefixed with "[Admin]" or "[All]" on the server side.

Admin console (server-side)
- Server starts an admin console thread that reads input from the server's stdin. Supported admin commands (enter exactly):
  - \exit        : shut down the server (System.exit(0))
  - \name        : change admin display name (console will prompt for new ID)
  - \showAll     : show currently online users and count
  - \kick        : kick a user (console prompts for username and reason; kicked user receives "Admin:你已被踢出聊天\n")
  - \broadcast   : broadcast a message to all clients (console will prompt for text)
  - \help        : show admin command help
- Any other text typed in the admin console is broadcast as an admin message.

Client usage
- After joining, type a line and press Enter to send a chat message.
- To leave the chat from the client, enter:
  ```
  \exit
  ```
  This sends an away message and closes the connection.
- If kicked, the client will receive "Admin:你已被踢出聊天\n" and the client will disconnect.

Logging
- Server logs are appended to log.txt by ChatLogger. Each entry includes a timestamp and the message.

Limitations & recommendations
- Port 10000 is hard-coded in both server and client and cannot be changed via command-line arguments. To change the port, modify the source code.
- Client address parsing expects address.json with the host at s.substring(3). Consider improving client code to use a standard format (plain host, JSON, or command-line argument).
- No authentication or encryption. This project is for learning/demo purposes only.

Contributing
- Fork the repository, create a feature branch, implement changes, and open a pull request.

License
- This project is licensed under the GNU General Public License v3.0 (GPLv3). See LICENSE for details.

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
- Java JDK 8 或更高
- 无外部依赖

仓库结构（本仓库中使用的路径）
- server/                 
  - Main.java
  - Admin.java
  - ClientHandler.java
  - ChatLogger.java
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
  javac -d out server/*.java
  ```
- 运行服务器 Main：
  ```
  java -cp out Main
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
  javac -d out client/*.java
  ```
- 运行客户端 Main：
  ```
  java -cp out Main
  ```

address.json（客户端必需）
- 客户端读取 address.json 的第一行为字符串 s，并使用 s.substring(3) 作为主机名。请按如下格式写入第一行（纯文本）：
  ```
  ip:127.0.0.1
  ```
  或
  ```
  ip:localhost
  ```

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
  - \help        ：命令帮助

客户端使用
- 输入内容回车发送消息
- 输入 \exit 离开聊天室并断开连接
- 被管理员踢出会收到 "Admin:你已被踢出聊天\n"

日志
- ChatLogger 将日志追加到 log.txt，含时间戳和内容

限制建议
- 端口 10000 为硬编码，仅可修改源码变更
- address.json 的解析建议用更稳健方式
- 无认证、无加密（仅演示或学习用）

贡献
- Fork 仓库、新建分支、实现后提 PR

许可证
- 本项目采用 GNU 通用公共许可证 (GPLv3)，请参见 LICENSE 文件获取完整授权条款。

联系方式
- 作者：Muimi272
- GitHub：https://github.com/Muimi272

---
