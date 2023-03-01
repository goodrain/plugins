<div align="center"> <a href="https://git.talkweb.com.cn/pengbo23812/wutong-admin-builder-ui"> <img alt="TalkwebAdmin Logo" width="200" height="200" src="./public/resource/img/logo.png"> </a> <br> <br>

<h1>Vue talkweb admin</h1>
</div>

https://wutong.talkweb.com.cn/kanban/doc.html#/home

**English** | [中文](./README.zh-CN.md)

## Introduction

Vue Talkweb Admin is a free and open source middle and back-end template. Using the latest `vue3`, `vite2`, `TypeScript` and other mainstream technology development, the out-of-the-box middle and back-end front-end solutions can also be used for learning reference.

## Feature

- **State of The Art Development**：Use front-end front-end technology development such as Vue3/vite2
- **TypeScript**: Application-level JavaScript language
- **Theming**: Configurable themes
- **International**：Built-in complete internationalization program
- **Mock Server** Built-in mock data scheme
- **Authority** Built-in complete dynamic routing permission generation scheme.
- **Component** Multiple commonly used components are encapsulated twice

## Documentation

[Document](https://git.talkweb.com.cn/pengbo23812/wutong-admin-builder-ui)

## Preparation

- [node](http://nodejs.org/) and [git](https://git-scm.com/) - Project development environment
- [Vite](https://vitejs.dev/) - Familiar with vite features
- [Vue3](https://v3.vuejs.org/) - Familiar with Vue basic syntax
- [TypeScript](https://www.typescriptlang.org/) - Familiar with the basic syntax of `TypeScript`
- [Es6+](http://es6.ruanyifeng.com/) - Familiar with es6 basic syntax
- [Vue-Router-Next](https://next.router.vuejs.org/) - Familiar with the basic use of vue-router
- [Ant-Design-Vue](https://2x.antdv.com/docs/vue/introduce-cn/) - ui basic use
- [Mock.js](https://github.com/nuysoft/Mock) - mockjs basic syntax

## Install and use

- Get the project code

```bash
git clone https://git.talkweb.com.cn/pengbo23812/wutong-admin-builder-ui
```

- Installation dependencies

```bash
cd vue-talkweb-admin

pnpm install

```

- run

```bash
pnpm serve
```

- build

```bash
pnpm build
```

## Change Log

[CHANGELOG](./CHANGELOG.zh_CN.md)


## 文件修改
```
├── build/               
│   ├── constant.ts
│   ├── config/
│   │    └── themeConfig.ts                       # 主题配置项
│   ├── generate/
│   │    ├── generateModifyVars.ts                # design
│   ├── vite/
│   │    ├── plugin/
│   │    │    └── mock.ts                         # mock地址
│   │    │    └── theme.ts                        # design
├── src/               
│   ├── api/                                      # ts模型
│   │    ├── model/                 
│   │    │    ├── userModel.ts
│   │    ├── menu.ts 
│   │    └── user.ts   
│   ├── assets/                                   # logo
│   ├── design/                                   # 主题配置项
│   │    ├── ant/
│   │    │    └── index.less                      # 修改修改
│   │    ├── var/
│   │    │    └── index.less                      # 修改变量名称
│   ├── enums/                                    # 可以直接覆盖
│   │    ├── cacheEnum.ts                         # 新增修改变量
│   │    └── httpEnum.ts                          # 修改接口返回code值
│   ├── components/                                
│   │    ├── Application
│   │    │    ├── src                         
│   │    │    │    └── AppLogo.vue                           
│   │    ├── SimpleMenu                         
│   │    │    ├── src                         
│   │    │    │    ├── components                         
│   │    │    │    │    └── menu.less                       
│   ├── layouts/                                  
│   │    ├── default/                 
│   │    │    ├── index.vue                       # 引用外部文件               
│   │    │    ├── header/
│   │    │    │    ├── index.less
│   │    │    │    ├── components/                 
│   │    │    │    │    ├── user-dropdown/                 
│   │    │    │    │    │    └── index.vue        # 修改下拉列表
│   │    │    ├── tabs/
│   │    │    │    └── index.less        # 修改下拉列表
│   ├── locales/                                  
│   │    ├── lang/                              
│   ├── lang/                                  
│   │    ├── zh-CN/                              
│   │    │    └── zh_CN.ts                        # 获取外部配置项
│   ├── router/                                  
│   │    ├── index.ts                              
│   │    ├── guard/                              
│   │    │    └── permissionGuard.ts             # 修改登录方式，数据拦截，去除token 等功能
│   │    ├── helper/                              
│   │    │    └── routeHelper.ts                 # view引用路经
│   ├── settings/                                  
│   │    ├── componentSetting.ts                  # 分页配置
│   │    ├── designSetting.ts                     # 样式前缀
│   │    ├── projectSetting.ts                    # 项目默认配置
│   │    ├── localeSetting.ts                     # 语言
│   │    └── siteSetting.ts                       # 文档
│   ├── store/                        
│   │    ├── modules/                              
│   │    │    ├── permission.ts                   # 用于演示-开始 可以删除
│   │    │    └── user.ts                         # token 返回变量修改
│   ├── enums/                        
│   │    ├── pageEnum.ts                          # 页面链接
│   ├── utils/                          
│   │    ├── auth/                              
│   │    │    └── index.ts                        # 新增获取租户信息 TenantId
│   │    ├── cache/                              
│   │    │    └── persistent.ts                   # 新增一些缓存字段
│   │    ├── http/
│   │    │    ├── axios/                                
│   │    │    │    ├── checkStatus.ts             # 异常处理
│   │    │    │    ├── axiosTransform.ts          # 新增TenantId字段
│   │    │    │    └── index.ts                   # 新增租户，错误处理，返回字段等判断
│   ├── sys/                           
│   │    ├── error-log/                              
│   │    │    └── index.vue                       # 删除接口请求
├── types/                                                            
├── package.json                            
└── README.md                                     # 文档
```
**Pull Request:**

1. Fork code!
2. Create your own branch: `git checkout -b feat/xxxx`
3. Submit your changes: `git commit -am 'feat(function): add xxxxx'`
4. Push your branch: `git push origin feat/xxxx`
5. submit`pull request`

## Git Contribution submission specification

- reference [vue](https://github.com/vuejs/vue/blob/dev/.github/COMMIT_CONVENTION.md) specification ([Angular](https://github.com/conventional-changelog/conventional-changelog/tree/master/packages/conventional-changelog-angular))

  - `feat` Add new features
  - `fix` Fix the problem/BUG
  - `style` The code style is related and does not affect the running result
  - `perf` Optimization/performance improvement
  - `refactor` Refactor
  - `revert` Undo edit
  - `test` Test related
  - `docs` Documentation/notes
  - `chore` Dependency update/scaffolding configuration modification etc.
  - `workflow` Workflow improvements
  - `ci` Continuous integration
  - `types` Type definition file changes
  - `wip` In development

## Related warehouse

If these plugins are helpful to you, you can give a star support

- [vite-plugin-mock](https://github.com/anncwb/vite-plugin-mock) - Used for local and development environment data mock
- [vite-plugin-html](https://github.com/anncwb/vite-plugin-html) - Used for html template conversion and compression
- [vite-plugin-style-import](https://github.com/anncwb/vite-plugin-style-import) - Used for component library style introduction on demand
- [vite-plugin-theme](https://github.com/anncwb/vite-plugin-theme) - Used for online switching of theme colors and other color-related configurations
- [vite-plugin-imagemin](https://github.com/anncwb/vite-plugin-imagemin) - Used to pack compressed image resources
- [vite-plugin-compression](https://github.com/anncwb/vite-plugin-compression) - Used to pack input .gz|.brotil files
- [vite-plugin-svg-icons](https://github.com/anncwb/vite-plugin-svg-icons) - Used to quickly generate svg sprite

## License

[MIT © Talkweb-2020](./LICENSE)
