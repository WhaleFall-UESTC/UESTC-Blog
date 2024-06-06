# 新生项目课程：云计算环境下的博客系统开发

> By WhaleFall 2023080902011 第二组实验报告

<br/>

## Environment

- 服务器：Tomcat 8.5.100
- 后端：    Java Servlet
- 数据库： MySQL 8.0.35

<br/>

<br/>

## 我的实现

1. Article-ArticleDAO 接口的实现
2. Thymeleaf 渲染页面
3. 与渲染相关的 Servlet 类实现
4. 自行配置超千行 CSS
5. 主界面实现
6. 留言板功能实现
7. 归档界面的实现
8. 控制台界面 + 后台文章增删改功能实现
9. SHA256 密文存储密码实现

<br/>

### 1. Article-ArticleDAO

我定义了一个 Article 类，里面包含了 id, title, descr, time, content 这些文章的基本信息，与数据库存储的内容对应。

接着我编写了 ArticleDAO 类，用于与 MySQL 数据库进行交互，为服务器提供了访问 articles Table 的接口

```java
public class ArticleDAO {
    public List<Article> selectall();
    public Article select(int Id);
    public boolean insert(Article article);
    public boolean update(int Id, String column, String newData);
    public boolean delete(int Id)
    
    public int countArticles();
    public List<String> findDifferentYearMonths();  // 按年-月的格式获取所有的创作时间
    public List<Article> selectArticlesByTime(String YMTime);   // 选择某年某月内的所有文章
    
    public String getContextById(int id);
}
```

这些函数实质上是通过执行 MySQL 获取的信息，比如：

```java
con = JDBCUtils.getConnection();
String sql = "select * from articles where DATE_FORMAT(time, '%Y-%m') = ? ORDER BY time DESC;";
pstmt = con.prepareStatement(sql);
pstmt.setString(1, YMTime);
res = pstmt.executeQuery();
```

<br/>

<br/>

### 2. Thymeleaf 与 Home, Archives, Board, Console

Thymleaf 是一个很方便的技术，我们在后端使用 `setAttribute` 设置参数，利用 `processTemplate` 渲染 html 页面。Thymeleaf 会自动检测 html 中需要渲染的部分，并通过设置的参数和 Java 解释器动态设值。例如，当我们以 post 方法请求 /show 时：

#### Home

```java
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
    response.setContentType("text/html;charset=utf-8");
        
    MsgDAO msgDAO = new MsgDAO();
    List<Msg> msgs = msgDAO.latest5Msg();
    NtsDAO ntsDAO = new NtsDAO();
    String nts = ntsDAO.latestNts();

    request.setAttribute("msgs", msgs);
    request.setAttribute("nts", nts); 

    HttpSession session = request.getSession();
    User u = (User) session.getAttribute("user");
    if (u == null) {
        request.setAttribute("authority", 0);
    } else {
        request.setAttribute("authority", u.getAuthority());
        System.out.println("User Authority: " + u.getAuthority());    
    }

    /* Content: Articles in the middle */
    ArticleDAO articleDAO = new ArticleDAO();
    List<Article> articles = articleDAO.selectall();
    request.setAttribute("articles", articles);
    request.setAttribute("content", 1);

    super.processTemplate("home", request, response);
}
```

html 中对应的部分：

```html
<div class="container-right">
  <div class="about-box">
    <div class="notification-box">
      <h1>通知</h1>
      <p th:text="${nts}"></p>
      <br/>
    </div>
  <div class="message-box">
    <h1>最新留言</h1>
    <div th:each="msg : ${msgs}">
      <p class="userName" th:text="${msg.getUserName()}"></p>              
      <p class="msg" th:text="${msg.getMsg()}"></p>
    </div>
  </div>
</div>
```

```html
<div th:if="${content} == 1" class="articles">
  <form action="showArticleContent" method="get" th:each="article : ${articles}" th:id="${article.getId()}">
    <button class="article">
      <h1 th:text="${article.getTitle()}"></h1>
      <p th:text="${article.getDescription()}"></p>
      <p class="time" th:text="${article.getArticleTime()}"></p>
      <input type="hidden" th:value="${article.getId()}" name="ArticleId">
    </button>
  </form>
</div>
```

我设置了 `content = 1` 确保了只有在 /show 当中才会如此渲染中间部分

我在 `login` 时在 Session 中加入了用户信息，方便了后端获取用户信息并设置权限 

<br/>

#### Archives:

```java
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    /* 设置 UTF-8 右侧边栏留言与通知 Session */
    /* 与 show 一致 */

    ArticleDAO articleDAO = new ArticleDAO();
    List<String> timeList = articleDAO.findDifferentYearMonths();

    List<List<Article>> primaryList = new ArrayList<>();

    for (String time : timeList) {
        String yearMonth = time.substring(0, 7);
        primaryList.add(articleDAO.selectArticlesByTime(yearMonth));
    }

    request.setAttribute("primaryList", primaryList);
    request.setAttribute("archives", 1);

    super.processTemplate("home", request, response);
}
```

我传入了一个以 List 为元素的 List —— primaryList，其中每个 List 内包含不同年月里的文章列表，这样更方便了 Thymeleaf 的排版。

```html
<div class="archive" th:if="${archives} == 1" th:each="subList : ${primaryList}">
  <h1 th:text="${subList.get(0).getArticleTime().substring(0, 7)}"></h1>
  <form action="showArticleContent" method="get" th:each="article : ${subList}">
    <button th:text="${article.getTitle()}"></button>
      <input type="hidden" th:value="${article.getId()}" name="ArticleId">
  </form>
</div>
```

<br/>

#### Board:

在 html 页面中，我根据用户权限将留言框与留言板分开

```java
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    /* 设置 UTF-8 右侧边栏留言与通知 Session */
    /* 与 show 一致 */

    List<Msg> msgs_all = msgDAO.selectall();
    request.setAttribute("msgs_all", msgs_all);
    request.setAttribute("board", 1);
    request.setAttribute("imgPath", getServletContext().getRealPath("img"));

    super.processTemplate("home", request, response);
}
```

```html
<div th:if="${board == 1 && (authority == 3 || authority == 2)}">
  <div class="hidediv"><h1> </h1></div>
    <form class="input-box" method="post" action="sendMsg">
      <textarea rows="3" type="text" id="msg-input" name="msg"></textarea>
      <input type="submit" value="留言">
    </form>
  </div>
```

```html
<div class="message" th:if="${board == 1}" th:each="msg : ${msgs_all}">
  <div><img class="headimage" th:src="${msg.getImgPath()}"></div>
  <div class="divh1"><h1 th:text="${msg.getUserName()}"></h1></div>
      <div class="divp">
          <p th:text="${msg.getMsg()}"></p>
      </div>
  </div>
</div>
```

<br/>

#### Console:

Console 界面结构与主页面大不相同，在新建的 console.html 中，我使用 flex 布局进行排版，分成四个板块，各承担个人信息修改，文章管理，用户管理，更新通知的功能。由于篇幅原因只展示后端代码：

```java
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
    response.setContentType("text/html;charset=utf-8");
    // 防止中文显示乱码

    HttpSession session = request.getSession();
    User u = (User) session.getAttribute("user");
    System.out.println("self in consolr: " + u.getName());
    if (u == null || u.getAuthority() == 0) {          
        response.sendRedirect("login.html");
    } else {
        request.setAttribute("self", u);
        request.setAttribute("authority", u.getAuthority());
    }
        // 防止无权限用户访问

    NtsDAO ntsDAO = new NtsDAO();
    String nts = ntsDAO.latestNts();
    request.setAttribute("nts", nts);

    ArticleDAO articleDAO = new ArticleDAO();
    List<Article> articles = articleDAO.selectall();
    request.setAttribute("articles", articles);

    UserDAO userDAO = new UserDAO();
    List<User> users = userDAO.selectall();
    request.setAttribute("users", users);

    request.setAttribute("console", 1);
    super.processTemplate("console", request, response);
    // 渲染 console.html
}
```

<br/>

<br/>

### 3. 文章增删改查

得益于之前所写的 ArticleDAO，在后端可以简单的进行这些处理

<br/>

#### 增、改：

我新写了两个 html 页面实现与用户的交互，通过表单将用户的输入传到后端

```java
String title = request.getParameter("title");
String content = request.getParameter("content");
String descr = request.getParameter("descr");

Article article = new Article();
article.newArticle(title, descr, content);      
ArticleDAO articleDAO = new ArticleDAO();
articleDAO.insert(article);
```

“改” 的部分与之类似，但多添加了一个逻辑：如果修改了文章内容，那么同时修改文章时间

```java
String content = request.getParameter("content");
if (content != null && !content.isEmpty()) {
    System.out.println("content: " + content);
    articleDAO.update(id, "content", content);

    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String time = now.format(formatter);
    articleDAO.update(id, "time", time);      
}
```

<br/>

#### 删：

在 Console 界面，删除只需一个按钮。我利用 `input` 的 `hidden` 传递文章 id

```html
<form method="post" action="deleteArticle">
    <input type="hidden" th:value="${article.getId()}" name="articleId">
    <input type="submit" value="删除">
</form>
```

在后端接收 id 后，通过 `ArticleDAO` 的 `delete` 删除

<br/>

#### 查：（此处采用孟海帆的实现）

我在数据库中新增了 TEXT 类型的 `content` ，并重写了文章接口以适配

然后孟海帆实现一个类，通过 Thymeleaf 渲染页面，再利用 Js 改成 Markdown 形式

<br/>

<br/>

### 4. SHA-256 加密

http 明文传输密码风险太大，而且大家也不会信任一个用明文存储密码的服务器，于是我采用了流行的 SHA-256 加密

为了避免竞争，导致加密未完成便上传表单，我采用了 `await` 的方法

我在 login，register，与 console 的修改密码处都实现了加密

```javascript
async function encryptPassword() {
    const password = document.getElementById('passwd');
    const encoded = new TextEncoder().encode(password.value);
    const hashBuffer = await crypto.subtle.digest('SHA-256', encoded);
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    const hashHex = hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
    console.log(hashHex);
    document.getElementById('password').value = hashHex;
}
        
var submitButton = document.getElementById('submitButtonUser');
submitButton.addEventListener('click', async function() {
      await encryptPassword();
      
      /* 。。。。。。 */
}
```

<br/>

<br/>

## 工作量统计表

|功能|描述|学时|
|--|--|--|
|基础功能|文章增删改|9|
|新增功能 1|博客排版设计与 CSS 美化|9|
|新增功能 2|Thymeleaf |3|
|新增功能 3|Java Servlet|3|
|新增功能 4|留言板|3|
|新增功能 5|控制台|6|
|新增功能 6|SHA-256|3|
