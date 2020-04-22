

## 1. MVVM

- Model是数据模型部分
- View是界面展示部分
- ViewModel是连接数据模型和界面展示的桥梁

## 2.

```
graph TD
UI控制层-->B(View Model层)
B(View Model层)-->仓库层
仓库层-->本地数据源
仓库层-->网络数据源
本地数据源-->持久化文件
网络数据源-->Webservice
```
