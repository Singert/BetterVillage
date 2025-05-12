<<<<<<< HEAD

Installation information
=======

This template repository can be directly cloned to get you started with a new
mod. Simply create a new repository cloned from this one, by following the
instructions provided by [GitHub](https://docs.github.com/en/repositories/creating-and-managing-repositories/creating-a-repository-from-a-template).

Once you have your clone, simply open the repository in the IDE of your choice. The usual recommendation for an IDE is either IntelliJ IDEA or Eclipse.

If at any point you are missing libraries in your IDE, or you've run into problems you can
run `gradlew --refresh-dependencies` to refresh the local cache. `gradlew clean` to reset everything 
{this does not affect your code} and then start the process again.

Mapping Names:
============
The MDK is configured to use the official mapping names from Mojang for methods and fields 
in the Minecraft codebase. These names are covered by a specific license. All modders should be aware of this
license. For the latest license text, refer to the mapping file itself, or the reference copy here:
https://github.com/NeoForged/NeoForm/blob/main/Mojang.md

MDG Legacy:
==========
This template uses [ModDevGradle Legacy](https://github.com/neoforged/ModDevGradle). Documentation can be found [here](https://github.com/neoforged/ModDevGradle/blob/main/LEGACY.md).

Additional Resources: 
==========
Community Documentation: https://docs.neoforged.net/  
NeoForged Discord: https://discord.neoforged.net/
=======
# BetterVillage
an attempt for a better village of mc based on MDK-Forge-1.20.1-ModDevGradle.git

要使用 **NeoForge** 为 **Minecraft 1.20.1** 开发一个修改“村庄及村民行为逻辑”并接入 **DeepSeek API** 的 Mod，可以按照以下步骤分阶段启动开发：

---

## 🔧 第一阶段：NeoForge Mod 开发环境配置

### 1. 安装工具

* **Java 17**：Minecraft 1.20.1 使用 Java 17
* **VSCode**（或 IntelliJ IDEA / Eclipse）
* **Gradle 7.6+**（可通过 NeoForge 自动安装）
* **Git**

### 2. 下载 NeoForge MDK

* 前往 [NeoForged 官网](https://neoforged.net/) 或其 GitHub，下载适用于 `1.20.1` 的 `NeoForge MDK`

### 3. 创建项目

解压 MDK 后结构类似：

```
build.gradle
settings.gradle
src/
gradlew
```

### 4. 修改 `build.gradle`

设置自己的 Mod ID、版本等，并添加 `mixin` 支持（如果你要修改村民 AI 可能需要 mixin）。

```groovy
dependencies {
    minecraft 'net.neoforged:forge:1.20.1-xxx' // 替换成最新版本
}
```

---

## 🧠 第二阶段：了解和修改村民行为逻辑

### 村民 AI 入口点

Minecraft 的村民行为基于 `Brain<Villager>` 和 `MemoryModuleType` / `Activity` 构建的行为树：

* 类：`net.minecraft.world.entity.npc.Villager`
* 核心方法：`Villager#registerGoals()`、`Villager#getBrain()`
* AI 行为存在于：

    * `VillagerAi`
    * `PoiTypes`（职业站点）
    * `MemoryModuleTypes`
    * `SensorTypes`

### 修改方法

#### 方案 1：使用 Mixin 修改行为（推荐方式）

使用 [Mixin](https://github.com/SpongePowered/Mixin) 注入村民逻辑，比如替换某些 `Brain` 模块、增加传感器。

#### 方案 2：重写 `Goal`（只适合简单行为替换）

可通过 `GoalSelector#addGoal()` 添加新行为。

---

## 🤖 第三阶段：接入 DeepSeek API（假设你指的是 DeepSeek-v1 模型）

### 假设你有 DeepSeek 服务运行方式为：

* 提供 RESTful API 接口（如 `http://localhost:port/api/chat`）

### 使用 Java 发送 HTTP 请求

推荐使用 [`OkHttp`](https://square.github.io/okhttp/) 或 `Java 11` 的 `HttpClient` 实现请求：

```java
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("http://localhost:8000/api/chat"))
    .header("Content-Type", "application/json")
    .POST(HttpRequest.BodyPublishers.ofString("{\"message\":\"你好村民\"}"))
    .build();
```

将返回值用作村民的对话生成或行为逻辑判断的一部分。

---

## 🧩 第四阶段：逻辑设计建议

| 目标       | 实现建议                                      |
| -------- | ----------------------------------------- |
| 自定义对话    | 创建 `custom chat GUI`，使用 DeepSeek 返回文本填充   |
| 村民行为动态决策 | 利用 DeepSeek 返回的结果，映射为不同 AI 行为（如逃跑、交易等）    |
| 村民记忆     | 将 DeepSeek 的上下文或历史消息缓存为 `MemoryModule` 数据 |

---

## 📦 第五阶段：打包与测试

* 使用 `gradlew build` 生成 `.jar` 文件
* 将其放入 `.minecraft/mods` 中运行测试
* 推荐使用 \[CurseForge App] 或 `MultiMC` 创建隔离测试环境

---

是否需要我为你提供：

* NeoForge 项目模板（带 mixin 和 okhttp 支持）
* 自定义 `Villager` AI 示例
* DeepSeek 的 Java 接入封装类？

你打算先从行为逻辑，还是先从 DeepSeek 对话系统开始开发？
