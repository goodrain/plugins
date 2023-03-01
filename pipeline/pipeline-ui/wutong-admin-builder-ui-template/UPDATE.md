## 文件修改

```
├── build/
│   ├── config/
│   │    └── themeConfig.ts                       # 主题配置项
├── src/
│   ├── api/                                      # ts模型
│   │    ├── model/
│   │    │    ├── userModel.ts
│   │    ├── menu.ts
│   │    └── user.ts
│   ├── assets/                                   # logo
│   ├── design/                                   # 主题配置项
│   │    ├── var/
│   │    │    └── index.less                      # 修改变量名称
│   ├── enums/                                    # 可以直接覆盖
│   │    ├── cacheEnum.ts                         # 新增修改变量
│   │    └── httpEnum.ts                          # 修改接口返回code值
│   ├── layouts/
│   │    ├── header/
│   │    │    ├── components/
│   │    │    │    ├── user-dropdown/
│   │    │    │    │    └── index.vue             # 修改下拉列表
│   ├── lang/
│   │    ├── zh-CN/
│   │    │    └── layout.ts                       # 新增
│   ├── router/
│   │    ├── index.ts
│   │    ├── guard/
│   │    │    └── permissionGuard.ts             # 修改登录方式，数据拦截，去除token 等功能
│   ├── settings/
│   │    ├── componentSetting.ts                 # 分页配置
│   │    ├── designSetting.ts                    # 样式前缀
│   │    ├── projectSetting.ts                   # 项目默认配置
│   │    ├── localeSetting.ts                    # 语言
│   │    └── siteSetting.ts                      # 文档
│   ├── store/
│   │    ├── modules/
│   │    │    ├── permission.ts                  # 用于演示-开始 可以删除
│   │    │    └── user.ts                        # token 返回变量修改
│   ├── utils/
│   │    ├── auth/
│   │    │    └── index.ts                       # 新增获取租户信息 TenantId
│   │    ├── cache/
│   │    │    └── persistent.ts                  # 新增一些缓存字段
│   │    ├── http/
│   │    │    ├── axios/
│   │    │    │    ├── axiosTransform.ts         # 新增TenantId字段
│   │    │    │    └── index.ts                  # 新增租户，错误处理，返回字段等判断
│   ├── sys/
│   │    ├── error-log/
│   │    │    └── index.vue                      # 删除接口请求
├── types/
├── package.json
└── README.md                                     # 文档
```
