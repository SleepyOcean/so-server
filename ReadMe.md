# so-server

<!-- PROJECT SHIELDS -->

[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![MIT License][license-shield]][license-url]

<!-- PROJECT LOGO -->
<br />


<p align="center">
  <a href="https://github.com/SleepyOcean/so-server/">
    <img src="https://cdn.jsdelivr.net/gh/SleepyOcean/ImageRepo@master/uPic/so-server-banner1.0.png" alt="Logo">
  </a>

  <h3 align="center">SO-SERVER</h3>
  <p align="center">
  为so博客、so系统资源性能监控平台、云文件系统、沉洋实验室等提供完整服务。
    <br />
    <a href="https://github.com/SleepyOcean/so-server"><strong>探索本项目的文档 »</strong></a>
    <br />
    <br />
    <a href="https://github.com/SleepyOcean/so-server">查看Demo</a>
    ·
    <a href="https://github.com/SleepyOcean/so-server/issues">报告Bug</a>
    ·
    <a href="https://github.com/SleepyOcean/so-server/issues">提出新特性</a>
  </p>

</p>


 本篇README.md面向开发者
 
## 目录
- [so-server](#so-server)
  - [目录](#目录)
  - [一、上手指南](#一上手指南)
  - [二、项目结构](#二项目结构)
  - [三、开发的架构](#三开发的架构)
  - [四、部署](#四部署)
  - [五、使用到的技术](#五使用到的技术)
  - [贡献者](#贡献者)
  - [关于作者](#关于作者)
  - [版权说明](#版权说明)
  - [鸣谢](#鸣谢)

## 一、上手指南

1）获取最新代码

```bash
# git 拉取最新代码
$ git clone https://github.com/SleepyOcean/so-server.git
```

2）将项目导入idea

3）安装maven依赖

4）按需运行各个服务


## 二、项目结构


```
so-server
├── so-blog-service               
├── so-common
├── so-crawler-service
├── so-file-system-service
├── so-jpql-template-engine
└── so-security-starter
```

- **so-common** 是其他服务公共的依赖，是公共基础包，包含了子模块共用和共有的工具类或模块
- **so-blog-service** 是为so-blog提供基础服务的service，包含文章的增删改查等等
- **so-crawler-service** 是数据爬取服务，为假数据制造来源
- **so-file-system-service** 是文件服务，提供文件增删改查，主要特色：图床服务、私有云文件管理服务、常见多媒体文件的引用（视频、音乐、文本等等）
- **so-jpql-template-engine** 是jpql模版引擎，简化多条件SQL查询的框架，详情请参见[复杂多条件SQL语句模板引擎](https://github.com/SleepyOcean/so-jpql-template-engine)
- **so-security-starter** 是权限身份相关服务，为外界访问提供统一的权限服务


## 三、开发的架构 



## 四、部署




## 五、使用到的技术

- [spring框架](https://spring.io/)

## 贡献者

请阅读**CONTRIBUTING.md** 查阅为该项目做出贡献的开发者。

**如何参与开源项目**

贡献使开源社区成为一个学习、激励和创造的绝佳场所。你所作的任何贡献都是**非常感谢**的。

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


## 关于作者

csdn博客: []() 

官网: [沉洋官网](https://blog.sleepyocean.cn)

 *您也可以在贡献者名单中参看所有参与该项目的开发者。*

## 版权说明

该项目签署了MIT 授权许可，详情请参阅 [LICENSE.txt](https://github.com/SleepyOcean/so-server/blob/master/LICENSE.txt)

## 鸣谢

- [GitHub Pages](https://pages.github.com)


<!-- links -->
[your-project-path]:SleepyOcean/so-server
[forks-shield]: https://img.shields.io/github/forks/SleepyOcean/so-server.svg?style=flat-square
[forks-url]: https://github.com/SleepyOcean/so-server/network/members
[stars-shield]: https://img.shields.io/github/stars/SleepyOcean/so-server.svg?style=flat-square
[stars-url]: https://github.com/SleepyOcean/so-server/stargazers
[license-shield]: https://img.shields.io/github/license/SleepyOcean/so-server.svg?style=flat-square
[license-url]: https://github.com/SleepyOcean/so-server/blob/master/LICENSE.txt



