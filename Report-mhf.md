# UESTC-Blog实验报告

> 云计算环境下的博客系统开发第二组期末报告

## 运用

1. Tomcat
2. Mysql
3. JDBC
4. Thymeleaf
5. Servlet

## 实现功能

1. 基础的博客内容：文章的增删改查
2. 新增功能：
	1. JDBC工具类及MySQL账号的配置
	2. 用户的登录注册
	3. 用户的权限设置
	4. 用户头像的上传和更改
	5. 不同权限的操作区分
	6. 用户的留言功能
	7. 管理员的通知功能
	8. 对文章内容的Markdown渲染
	9. 对代码块的高亮设置
	10. 使用Thymeleaf实现前后端分离

## 具体实现

### JDBC操作数据库

JDBC (Java Database Connectivity) 是一个用于Java语言的数据库连接API，它允许Java应用程序以一种标准的方式与各种数据库进行连接和操作。

由于之后所有的对数据库中数据的更新都需要一些共同的操作，这里将其打包为`JDBCUtils`类，之后需要使用时调用即可

```java
public class JDBCUtils {
    private static String url;
    private static String user;
    private static String password;
    private static String driver;

    static{
        //读取资源文件，获取值
        try {
            //1.创建properties集合类
            //2.获取src路径下的文件方式：Classloader 类加载器
            //3.加载文件
            //4.获取数据，赋值
            //5.注册驱动
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
	
    // 加载驱动连接数据库
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // 关闭连接由于部分操作不需要结果集，故这里写了两个关闭连接的接口
    public static void close(ResultSet ress, Statement stat, Connection conn){
        // 关闭连接
    }

    public static void close(Statement stat, Connection conn){
        // 关闭连接
    }
}
```

添加一个jdbc.properties来设置数据库的基本信息

```
url=jdbc:mysql://localhost:3306/uestc_blog?useUnicode=true&characterEncoding=utf8
user=/*用户名*/
password=/*数据库密码*/
driver=com.mysql.cj.jdbc.Driver
```



### 文章的增删改查

#### 数据库

```mysql
create table articles
(
    id      int auto_increment
        primary key,
    title   varchar(35)  not null,
    descr   varchar(150) null,
    time    varchar(25)  null,
    content text         null
)
    charset = utf8mb4;
```



#### JDBC(本部分最后采用陶浩轩的方案，这里展示本人的实现)

bean中添加一个`Article`类

```Java
public class Article {
    public int id;
    private String title;
    private String descr;
    private String time;
    private String content;

    public Article() { super(); }

    public void setArticle(int id, String title, String descr, String time) {
        // 新增一篇文章
    }

    public void setContent(String content) { this.content = content; }
    public int getId() { return this.id; }
    public String getTitle() { return this.title; }
    public String getDescription() { return this.descr; }
    public String getArticleTime() { return this.time; }
    public String getContent() { return this.content; }
}
```

DAO层中实现增删改查

```Java
public class ArticleDAO {
    // 列出所有的文章
    public List<Article> selectAll() {
        List<Article> articles = new ArrayList<>();
        // 调用JDBC的组件

        try {
            con = JDBCUtils.getConnection();
            String sql = "select * from articles";
            // 将数据调入结果集
            res = pstmt.executeQuery();

            while (res.next()) {
                // 将结果集中的数据存入articles中
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, pstmt, con);
        }
        return articles;
    }

    // 通过Id查寻某一篇特定的文章(JDBC部分类似于selectAll)
    public Article select(int Id) {
        Article article = new Article();

        try {
            String sql = "select * from articles where id = ?";
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, pstmt, con);
        }
        return article;
    }

    // 增加一篇文章，返回是否增加成功
    public boolean insert(Article article) {

        try {
            String sql = "INSERT INTO articles (title, descr, time, content) VALUES (?, ?, ?, ?);";
            // 表示增加成功
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(pstmt, con);
        }
        return false;
    }

    // 修改文章，返回是否修改成功
    public boolean update(int Id, String column, String newData) {
        try {
            String sql = "UPDATE articles SET " + column + " = ? WHERE id = ?;";
            int affectedRows = pstmt.executeUpdate();
            // 表示修改成功
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(pstmt, con);
        }
        return false;
    }

    // 删除文章，返回是否删除成功
    public boolean delete(int Id) {
        try {
            String sql = "DELETE FROM articles WHERE id = ?;";

            int affectedRows = pstmt.executeUpdate();
            // 表示删除成功
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(pstmt, con);
        }
        return false;
    }
```

#### Servlet

newArticle

```Java
/*当接收到"/newArticle"时，调用该类的doPost方法*/
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 此处修改接受文章的编码方式为UTF-8

        // 此处接收前端提交的数据

        // 此处调用articleDAO中的insert
        // 重定向回console
        response.sendRedirect("console");
    }

```

deleteArticle

```Java
/*当接收到"/deleteArticle"时，调用该类的doPost方法*/
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 接受文章ID
        String ID = request.getParameter("articleId");
        int id;
        // 为空报错
        if (ID != null && !ID.equals("")) {
            id = Integer.parseInt(ID);
        } else {
            System.out.println("id is empty when delete");
            return;
        }
        
        // 此处调用articleDAO中的delete
        
        // 重定向回console
        response.sendRedirect("console");
    }
```

updateArticle

```Java
/*当接收到"/updateArticle"时，调用该类的doPost方法*/
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 此处修改接受文章的编码方式为UTF-8

        // 此处接收前端提交的数据

        // 此处调用articleDAO中的update
        // 重定向回console
        response.sendRedirect("console");
    }
```

ShowArticleContent

```Java
/*当接收到"/showArticleContent"时，调用该类的doGet方法*/
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// 此处修改接受文章的编码方式为UTF-8
    	// 此处获取主页中的留言和消息便于更新后渲染

        HttpSession session = request.getSession();
		
    	// 获取文章Id
        String ArticleId = request.getParameter("ArticleId");

        // 此处调用此处调用articleDAO中的select

        if(article.getContent() != null && !article.getContent().isEmpty()) {
            request.setAttribute("article", article);
        }


        // 此处返回边栏需要渲染的信息到前端
    
    	// 将content设为2，用于更改主页面所显示的块
        request.setAttribute("content", 2);

        super.processTemplate("home", request, response);
    }
```

#### 前端

> Thymeleaf 是一个现代化的Java模板引擎，它可以用来创建Web页面的HTML表示。Thymeleaf 提供的在服务器端和客户端（通过浏览器）预渲染页面的能力。这意味着可以在服务器上完全解析和格式化模板，然后将它们发送到客户端浏览器，或者在客户端浏览器中动态地解析和渲染模板，进而达到前后端分离的效果。

##### console实现文章的增删改（此处采用陶浩轩的的实现）

通过调用`Servlet`中的deleteArticle,newArticle,updateArticle实现

##### 文章的markdown显示

通过调用外部的marked.js实现

要想调用这个库，我们需要对其进行基本的设置

```javascript
var marked = require('marked');
        marked.setOptions({
            renderer: new marked.Renderer(),
            gfm: true,
            tables: true,
            breaks: false,
            pedantic: false,
            sanitize: false,
            smartLists: true,
            smartypants: false
        });

        marked(markdownText, {
            gfm: true,
            tables: true,
            breaks: false,
            pedantic: false,
            sanitize: false,
            smartLists: true,
            smartypants: false
        });
```

编写了一个显示文章内容的`<div>`

```html
<div class="article" th:if="${content} == 2">
    <h1 th:text="${article.getTitle()}"></h1>
    <p th:text="${article.getDescription()}"></p>
    <p class="time" th:text="${article.getArticleTime()}"></p>
    <input id="context" type="hidden" th:value="${article.getContent()}">
    <div id="box" style="background-color: rgb(28,28,28);">
    </div>
</div>
```

在这里我们运用了Thymaleaf动态的显示获取的文章内容

我们还需要一个`change()`函数将`context`中的内容渲染为markdown格式再显示再`box`中

```javascript
function change()
{
    //获取textarea值
    var text = document.getElementById("context").value;
    //转换为marked格式并且显示在页面
    var text = document.getElementById("box").innerHTML = marked(text);
}
```

最后，新增一个事件监听器，它监听DOM内容加载完成的事件，具体解释见下

```javascript
document.addEventListener('DOMContentLoaded', function() {
    // 'DOMContentLoaded' 是事件名，这里使用了一个匿名函数作为事件处理函数。
    // 当DOM内容加载完成时，这个函数会被调用。
    change();
    // 'change()' 是一个自定义的函数，它可能执行一些操作，比如改变DOM元素的内容或样式。
    // 由于这个函数的具体行为没有给出，所以无法确定它的具体功能。
});
```

##### 代码块高亮

先初始化高亮代码

```javascript
hljs.highlightAll();
marked.setOptions({
    highlight: function (code, language) {
        const validLanguage = hljs.getLanguage(language) ? language : 'javascript';
        return hljs.highlight(code, { language: validLanguage }).value;
    }
})
```

编写一个`highlight.css`以设置代码块中高亮的颜色

在这个css中，模仿了github中代码的高亮风格配置了颜色，具体的配置可以去代码处查看

### 关于用户

#### 数据库

```mysql
create table users
(
    id        int auto_increment
        primary key,
    name      varchar(20)   not null,
    password  varchar(200)  not null,
    email     varchar(32)   not null,
    authority int default 1 not null
);
```



#### JDBC

由于部分代码与之前大致相同，所以部分实现只作简单说明

dean中添加一个`user`类

```Java
package com.blog.bean;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private int authority;

    public User(){ super(); }

    // 此处是一些初始化方法
}
```

DAO层中`UserDAO`实现增删改查

```Java
package com.blog.dao;

import com.blog.bean.Msg;
import com.blog.bean.User;
import com.blog.utils.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public void insert(User user){
        // 增加一个用户，中间设有为空报错
    }

    public User getUserById(int Id) {
        // 通过id寻找用户，如果id不存在返回空
    }

    public User getUserByName(String name) {
       // 通过名字寻找用户，如果不存在返回空
    }

    public User getUserByEmail(String email) {
        // 通过邮箱寻找用户，如果不存在返回空
    }

    public List<User> selectall() {
        // 返回数据库中所有的user
    }

    public void delete(int id){
        // 删除该id的用户
    }
}
```

#### 关于权限

1. 用户共有四种权限：
	- 0：表示处于只能浏览的状态
	- 1：表示处于能浏览，并可以向管理员发出申请留言权限的状态
	- 2：表示处于能浏览，并可以在留言板处留言的状态
	- 3：表示管理员状态，可以发通知，修改用户的权限
2. 用户权限的变动：
	- 当用户直接以游客的方式进入主界面时，处于状态0
	- 当用户注册账号且未获得留言权限时时，状态变为1
	- 当用户获得留言权限时，状态变为2
	- 用户权限为3的管理员可以在后台修改其他用户的权限
3. 不同权限是的主页面状态：
	- 0：没有任何多余按钮
	- 1：在留言板处有一个申请按钮，用于发出申请留言权限
	- 2：在留言板处有一个留言按钮
	- 3：在通知栏处有一个通知按钮，并在右下角显示申请留言权限的用户名，点击该用户名表示同意申请

#### Servlet

UpdateUser, DeleteUser与文章的大致相同，下面一些User相关的独有的接口

UpdateImg

```Java
/*用于用户上传头像，将上传的头像重命名为用户的昵称，并保存至/img文件夹中*/
// 当接收到"/updateImg"时，执行该类的doPost方法
@WebServlet("/updateImg")
@MultipartConfig
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        // 此处设置传入参数的编码为UTF-8

        // 更改img文件夹中的头像文件
        // 获取头像保存位置的路径
        Path filePath;
		
        // 将上传的头像命名为用户的昵称
        int dot = upfilename.lastIndexOf(".");
        String ext = upfilename.substring(dot + 1);
        String newUpfile = name + "." + ext;

        while (true) {
            filePath = Paths.get(fileRoot, newUpfile);
            if (Files.exists(filePath)) {
                // 此处如果之前用户已上传头像，表示其将做修改，为防止命名重复，于是先删除原有头像
                
            } else {
                try {
                    // 上传头像
                    System.out.println("Try upload " + fileRoot + "\\" + newUpfile);
                    part.write(fileRoot + "\\" + newUpfile);

                    String realRoot = getServletContext().getRealPath("img");
                    Path realPath = Paths.get(realRoot, newUpfile);
                    if (Files.exists(realPath)) {
                        Files.delete(realPath);
                    }
                    Files.copy(Paths.get(fileRoot, newUpfile), realPath);
                    break;
                } catch (IOException e) {
                    if (!Files.exists(rootPath)) {
                        Files.createDirectories(rootPath);
                    } else {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
		// 重定向至console
        String set = request.getParameter("set");
        if (set !=null && set.equals("0")) {
            System.out.println("Only update image files");
            response.sendRedirect("console");
        }
    }
```

Login

```Java
/*用于用户登录*/
// 当接收到"/login"时，执行该类的doPost方法
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // 设置编码为UTF-8
    
    // 存入前端上传的信息;

    System.out.println(email + '\n' + password);

    // 此处通过邮箱寻找用户，若存在，则将其存入user，若不存在，则user为空

    // 检查密码是否输入正确
    if(user != null && Objects.equals(user.getPassword(), password)){
        HttpSession session = request.getSession();
        // 存在则将user上传至会议，并重定向至show
        session.setAttribute("user", user);
        response.sendRedirect("show");
        System.out.println("User " + user.getName() + " login successfully!");
    } else {
        // 不存在就返回错误信息不在前端用Thymeleaf渲染
        request.setAttribute("tipMsg", "email or password is wrong!");
        System.out.println("Login unsuccessfully!");
        super.processTemplate("login", request, response);
    }
}
```

Register

```Java
/*用于用户注册*/
// 当接收到"/register"时，执行该类的doPost方法
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // 设置编码为UTF-8

    // 存入前端上传的信息
    UserDAO userDAO = new UserDAO();
    
    // 判断该邮箱是否已经被注册
    User flag = userDAO.getUserByEmail(email);
    if(flag == null){
        System.out.println("Register successfully!");
        // 若没有被注册，则进行注册，完成后跳转至login界面进行登录
        super.processTemplate("login",request,response);
    } else{
        // 若已经被注册，则通过Thymeleaf渲染报错信息至register界面
        System.out.println("Register unsuccessfully!");
        request.setAttribute("tipMsg", "Email has been used!");
    }
}
```

#### 前端

> 利用Thymeleaf，可以实现在不同权限的用户登录后产生不同的主界面

login.html(加密部分由陶浩轩完成)

```html
<div class="login">
<!--表单用于提交请求-->
<form action="login" id="loginForm">
    <p>Login</p>
    <input id="email" type="text" placeholder="email" required="required" name="email" autocapitalize="off"
           onkeyup="this.value=this.value.replace(/\s+/g,'')"/>
    <input id="passwd" type="password" placeholder="password" required="required" autocapitalize="off"
           onkeyup="this.value=this.value.replace(/\s+/g,'')"/>
    <p class="tip" th:text="${tipMsg}"></p>
    <!--运用Thymeleaf渲染-->
    <br th:if="${errorMsg == null}">
    <input type="hidden" id="password" name="password">
</form>
<input type="submit" class="btn" value="Submit" onclick="encryptPassword()" id="button">
</div>
```

register.html与login.html大致相同，详细的信息可以去参考代码

console.html由陶浩轩完成，主要就是将前端的信息上传至Servlets中处理，再将处理后的信息通过thymeleaf渲染

### 关于留言和通知

#### 数据库

```mysql
create table msgs
(
    id     int auto_increment
        primary key,
    msg    varchar(100) not null,
    userid int          not null
)
    charset = utf8mb4;

create table ntss
(
    id  int auto_increment
        primary key,
    nts varchar(100) not null
)
    charset = utf8mb4;
```



#### JDBC

bean中添加一个Msg类和Nts类(由于差别不大，所以只展示Msg类)

```Java
package com.blog.bean;

import com.blog.bean.User;
import com.blog.dao.UserDAO;

public class Msg {
    private int id;
    private String msg;
    private int userId;
    public Msg(){super();}

    // 此处初始化数据
    
    // 返回留言对应的用户头像路径
    public String getImgPath() { return "img\\" + getUserName() + ".jpg"; }
}

```

DAO层和bean一样，也只展示MsgDAO类

```Java
package com.blog.dao;

import com.blog.bean.Msg;
import com.blog.utils.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MsgDAO {
    public List<Msg> selectall() {
        // 列出所有的留言
        String sql = "select * from msgs order by id desc;";

    }

    public List<Msg> latest5Msg() {
        // 列出最新的5条留言
        String sql = "select * from msgs order by id desc limit 5;";

    }
    
    public boolean send(Msg inf)
    {
        // 发送留言
        String insertSql = "insert into msgs (msg,userid) values('"+msg+"','"+userId+"')";
}

```

#### Servlet

SendMsg

```Java
// 当接收到"/sendMsg"时，执行该类的doPost方法
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // 设置编码为UTF-8
    // 获取前端上传的数据
    String msg = request.getParameter("msg");

    HttpSession session = request.getSession();
    User user = (User) session.getAttribute("user");
    int userId = user.getId();

    System.out.println("User " + userId + " send msg: " + msg);

    //调用send方法
    boolean flag = msgDAO.send(m5g);
    if (!flag) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('留言失败')");
        out.println("</script>");
    }
	// 重定向至留言界面
    response.sendRedirect("board");
}
```

UpdateNts与之前所编写的原理大致相同

#### 前端

基本由陶浩轩实现，实现方式与之前console的原理大致相同

## 工作量统计

| 功能      | 描述                            | 学时 |
| --------- | ------------------------------- | ---- |
| 基础功能  | 文章的增删改查                  | 8    |
| 新增功能1 | 用户的登录注册                  | 5    |
| 新增功能2 | 用户的权限设置                  | 3    |
| 新增功能3 | 用户头像的上传和更改            | 3    |
| 新增功能4 | 不同权限的操作区分              | 4    |
| 新增功能5 | 用户的留言功能 管理员的通知功能 | 3    |
| 新增功能6 | 对文章内容的Markdown渲染        | 2    |
| 新增功能7 | 对代码块的高亮设置              | 2    |
| 新增功能8 | 使用Thymeleaf实现前后端分离     | 4    |

