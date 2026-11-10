# 发布到 GitHub（原仓库保持不动）

推荐新仓库名：`weather-system-monolith`

当前材料只是独立工程文件，还**不是**已发布的 GitHub 仓库。请不要把 ZIP 上传到原仓库覆盖历史。

## 使用 GitHub 网页 + Git（不必安装 gh CLI）

1. 登录 GitHub，选择 **New repository**，仓库名 `weather-system-monolith`，建议先设为 **Private**，不要勾选自动生成 README / .gitignore / license。
2. 解压这个 ZIP 到本机，在包含 `pom.xml` 的目录打开 PowerShell。
3. 输入以下命令（把 `YOUR_USERNAME` 改成自己的 GitHub 用户名；如果用户名是 `Yik11Yueuii` 可直接使用）：

```powershell
git init
git branch -M main
git add .
git commit -m "Initial monolithic learning edition"
git remote add origin https://github.com/Yik11Yueuii/weather-system-monolith.git
git push -u origin main
```

如果你此前没有配置 Git 身份，`git commit` 可能提示设置 `user.name` 与 `user.email`。GitHub 登录由本机的 Git Credential Manager 或 GitHub CLI 完成，不要在聊天中发送密码或访问令牌。

## 发布前检查

- `git status` 确认待提交仅是新项目文件。
- 确认 `application.yml` 仅含 demo/live 配置、无私人密钥。
- `mvn test`、`mvn spring-boot:run` 在本机验证。
- 访问 http://localhost:8080 ，确认 `演示数据 · 非实时` 的标签。
- 切换 `WEATHER_MODE=live` 需要网络及 Open-Meteo 可用性，不保证第三方时时在线。

发布后把新仓库地址发到新聊天，我们会**在另一个干净目录真正从零手敲**，这个仓库只当参考答案使用。
