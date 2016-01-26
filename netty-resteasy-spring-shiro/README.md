resteasy-spring-netty
=====================

Resteasy + Spring + Netty sample

* Inject resteasy provider / controllers as spring bean
* Authentication


=====================

1. Run at Main.java
2. Test http://localhost:8082/resteasy/hello/world

#### 教程

1. jax-rs规范用法: http://www.vogella.com/tutorials/REST/article.html
2. resteasy 教程: http://code.freedomho.com/9541.html

#### 名次解释

1. RESTEasy：RESTEasy是JBoss的一个开源项目，提供各种框架帮助你构建RESTful Web Services和RESTful Java应用程序。它是JAX-RS规范的一个完整实现并通过JCP认证。
2. JAX-RS: 是一套用java实现REST服务的规范。（全名Java API for RESTful Web Services）
3. JAX-RS标注的内容：

    @Path，标注资源类或方法的相对路径

    @GET，@PUT，@POST，@DELETE，标注方法是用的HTTP请求的类型

    @Produces，标注返回的MIME媒体类型

    @Consumes，标注可接受请求的MIME媒体类型

    @PathParam，@QueryParam，@HeaderParam，@CookieParam，@MatrixParam，@FormParam,分别标注方法的参数来自于HTTP请求的不同位置，例如@PathParam来自于URL的路径，@QueryParam来自于URL的查询参数，@HeaderParam来自于HTTP请求的头信息，@CookieParam来自于HTTP请求的Cookie。

4. REST: 含状态传输（英文：Representational State Transfer，简称REST）,是一种软件架构风格。具体看 http://zh.wikipedia.org/wiki/REST