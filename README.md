# ITHome_Server
Java SpringBoot
API

用户相关
1.用户注册                post /user/register
2.用户登录                post /user/login                  (每日登录积分+1)
3.获取用户信息            get  /user/info
4.编辑用户信息            post /user/info
5.获取用户收藏            get  /user/collect
6.获取用户推荐记录        get  /user/recommend
7.获取用户积分记录        get  /user/score

资源相关
8.获取某专栏资源          get  /topic/:techTag
9.查看某资源信息          post  /resource/see/:resId         (浏览量+1)
10.资源评价               post  /resource/evaluate/:resId
11.资源收藏               post  /resource/collect/:resId
12.资源搜索               post  /resource/search
13.资源筛选               post  /resource/screen
14.资源推荐               post  /addRes

审核中心
15.审核资源               post  /examineRes
16.发布公告               post  /announce
17.设立/撤除管理员        post  /admin

社交板块
18.获取积分榜信息          get  /ranking
19.获取网友信息            get  /friend
20.发布个人信息            post /friend
