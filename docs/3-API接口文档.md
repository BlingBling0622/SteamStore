# API接口文档

## 目录
1. [用户认证接口](#一用户认证接口)
2. [商品管理接口](#二商品管理接口)
3. [购物车接口](#三购物车接口)
4. [订单接口](#四订单接口)
5. [个人库接口](#五个人库接口)

---

## 接口通用说明

### 基础信息
- **协议**: HTTP/HTTPS
- **请求格式**: application/x-www-form-urlencoded (表单) / application/json
- **响应格式**: HTML页面 或 JSON
- **字符编码**: UTF-8
- **Base URL**: `http://localhost:8080`

### 认证方式
- **Session认证**: 通过Spring Security管理Session
- **Cookie**: JSESSIONID自动携带
- **受保护路径**: `/dashboard`, `/cart`, `/checkout`
- **公开路径**: `/`, `/store`, `/login`, `/register`

### 通用响应码
| HTTP状态码 | 说明 |
|-----------|------|
| 200 | 请求成功 |
| 302 | 重定向（未登录跳转登录页） |
| 400 | 请求参数错误 |
| 401 | 未授权（未登录） |
| 403 | 禁止访问（CSRF验证失败） |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 一、用户认证接口

### 1.1 用户注册

**接口地址**: `/register`

**请求方式**: POST

**请求参数**:
```
POST /register
Content-Type: application/x-www-form-urlencoded

username=testuser&email=test@example.com&password=123456
```

| 参数名 | 类型 | 是否必填 | 说明 |
|-------|------|---------|------|
| username | String | 是 | 用户名，3-50字符，唯一 |
| email | String | 是 | 邮箱地址，唯一 |
| password | String | 是 | 密码，至少6位 |

**响应结果**:
- **成功**: 重定向到 `/login?registered` (302)
- **失败**: 返回注册页面，显示错误信息

**业务逻辑**:
1. 验证用户名、邮箱唯一性
2. 密码BCrypt加密
3. 保存到数据库
4. 重定向到登录页

**示例**:
```bash
curl -X POST http://localhost:8080/register \
  -d "username=newuser" \
  -d "email=new@example.com" \
  -d "password=123456"
```

---

### 1.2 用户登录

**接口地址**: `/login`

**请求方式**: POST

**请求参数**:
```
POST /login
Content-Type: application/x-www-form-urlencoded

username=testuser&password=123456
```

| 参数名 | 类型 | 是否必填 | 说明 |
|-------|------|---------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |

**响应结果**:
- **成功**: 重定向到 `/dashboard` (302)
- **失败**: 重定向到 `/login?error` (302)

**Session信息**:
登录成功后，服务器创建Session，返回Cookie:
```
Set-Cookie: JSESSIONID=ABC123...; Path=/; HttpOnly
```

**业务逻辑**:
1. Spring Security验证用户名密码
2. BCrypt验证密码哈希
3. 创建Authentication对象
4. 存入SecurityContext和Session
5. 重定向到个人库

**示例**:
```bash
curl -X POST http://localhost:8080/login \
  -c cookies.txt \
  -d "username=testuser" \
  -d "password=123456"
```

---

### 1.3 用户登出

**接口地址**: `/logout`

**请求方式**: POST

**请求参数**:
```
POST /logout
Content-Type: application/x-www-form-urlencoded
Cookie: JSESSIONID=...

_csrf=<csrf_token>
```

| 参数名 | 类型 | 是否必填 | 说明 |
|-------|------|---------|------|
| _csrf | String | 是 | CSRF令牌（自动携带） |

**响应结果**:
- **成功**: 重定向到 `/login?logout` (302)

**业务逻辑**:
1. 清除SecurityContext
2. 销毁Session
3. 清除Cookie
4. 重定向到登录页

---

## 二、商品管理接口

### 2.1 获取商品列表

**接口地址**: `/store`

**请求方式**: GET

**请求参数**: 无

**响应结果**: HTML页面

**返回数据**（通过Thymeleaf模板）:
- `products`: 所有商品列表（List<Product>）
- `ownedIds`: 已拥有商品ID集合（Set<Long>）- 仅登录用户
- `cartCount`: 购物车数量（Integer）- 仅登录用户

**业务逻辑**:
1. 查询所有商品
2. 如果用户已登录，查询已拥有商品ID
3. 如果用户已登录，查询购物车数量
4. 渲染store.html模板

**示例**:
```bash
curl http://localhost:8080/store
```

---

### 2.2 获取商品详情

**接口地址**: `/store/game/{id}`

**请求方式**: GET

**路径参数**:

| 参数名 | 类型 | 是否必填 | 说明 |
|-------|------|---------|------|
| id | Long | 是 | 商品ID |

**响应结果**: HTML页面

**返回数据**:
- `product`: 商品详细信息（Product对象）
- `owned`: 是否已拥有（Boolean）- 仅登录用户
- `inCart`: 是否在购物车（Boolean）- 仅登录用户
- `cartCount`: 购物车数量（Integer）- 仅登录用户

**业务逻辑**:
1. 根据ID查询商品
2. 如果商品不存在，抛出异常（404）
3. 如果用户已登录，检查是否已拥有
4. 如果用户已登录，检查是否在购物车
5. 渲染game-detail.html模板

**示例**:
```bash
curl http://localhost:8080/store/game/1
```

---

### 2.3 获取精选游戏

**接口地址**: `/` (首页)

**请求方式**: GET

**请求参数**: 无

**响应结果**: HTML页面

**返回数据**:
- `featured`: 精选游戏列表（最多5个）
- `allProducts`: 所有商品列表
- `ownedIds`: 已拥有商品ID集合 - 仅登录用户
- `cartCount`: 购物车数量 - 仅登录用户

**业务逻辑**:
1. 查询featured=true的商品
2. 查询所有商品用于"浏览商店"部分
3. 如果用户已登录，查询已拥有商品和购物车
4. 渲染index.html模板

---

## 三、购物车接口

### 3.1 添加到购物车

**接口地址**: `/cart/add`

**请求方式**: POST

**认证**: 需要登录

**请求参数**:
```
POST /cart/add
Content-Type: application/x-www-form-urlencoded
Cookie: JSESSIONID=...

productId=5
```

| 参数名 | 类型 | 是否必填 | 说明 |
|-------|------|---------|------|
| productId | Long | 是 | 要添加的商品ID |

**响应结果**:
- **成功**: 重定向到 `/store/game/{productId}` 并显示成功消息 (302)
- **失败**: 返回错误页面

**业务逻辑**:
1. 验证用户登录
2. 检查商品是否已在购物车（防重复）
3. 查询商品是否存在
4. 创建购物车项
5. 保存到数据库
6. 重定向到商品详情页

**示例**:
```bash
curl -X POST http://localhost:8080/cart/add \
  -b cookies.txt \
  -d "productId=5"
```

---

### 3.2 查看购物车

**接口地址**: `/cart`

**请求方式**: GET

**认证**: 需要登录

**请求参数**: 无

**响应结果**: HTML页面

**返回数据**:
- `cartItems`: 购物车项列表（List<CartItem>）
- `cartTotal`: 购物车总价（Double）
- `cartCount`: 购物车商品数量（Integer）

**业务逻辑**:
1. 验证用户登录
2. 查询用户的所有购物车项
3. 关联查询商品信息
4. 计算总价（含折扣）
5. 渲染cart.html模板

**CartItem数据结构**:
```java
{
  "id": 1,
  "user": { ... },
  "product": {
    "id": 5,
    "name": "Grand Theft Auto V",
    "price": 29.99,
    "discountPercent": 0,
    "headerImageUrl": "https://..."
  }
}
```

**示例**:
```bash
curl http://localhost:8080/cart -b cookies.txt
```

---

### 3.3 移除购物车商品

**接口地址**: `/cart/remove`

**请求方式**: POST

**认证**: 需要登录

**请求参数**:
```
POST /cart/remove
Content-Type: application/x-www-form-urlencoded
Cookie: JSESSIONID=...

productId=5
```

| 参数名 | 类型 | 是否必填 | 说明 |
|-------|------|---------|------|
| productId | Long | 是 | 要移除的商品ID |

**响应结果**:
- **成功**: 重定向到 `/cart` (302)

**业务逻辑**:
1. 验证用户登录
2. 查询购物车项
3. 删除购物车项
4. 重定向到购物车页面

**示例**:
```bash
curl -X POST http://localhost:8080/cart/remove \
  -b cookies.txt \
  -d "productId=5"
```

---

## 四、订单接口

### 4.1 创建订单（结账）

**接口地址**: `/checkout`

**请求方式**: POST

**认证**: 需要登录

**请求参数**: 无（从用户购物车读取）

**响应结果**: HTML页面（订单确认页）

**返回数据**:
- `order`: 订单对象（Order）

**Order对象结构**:
```java
{
  "id": 1,
  "user": { ... },
  "products": [
    {
      "id": 5,
      "name": "Grand Theft Auto V",
      "price": 29.99,
      ...
    }
  ],
  "total": 29.99,
  "purchasedAt": "2026-06-23T15:41:31"
}
```

**业务逻辑**（事务）:
1. 验证用户登录
2. 查询用户购物车
3. 验证购物车非空
4. 计算总价
5. 创建Order实体
6. 关联Products（多对多）
7. 保存订单
8. **清空购物车**
9. 提交事务
10. 渲染checkout-success.html

**异常情况**:
- 购物车为空 → 抛出`IllegalStateException`
- 事务失败 → 回滚所有操作

**示例**:
```bash
curl -X POST http://localhost:8080/checkout \
  -b cookies.txt
```

---

### 4.2 查看订单历史

**接口地址**: `/dashboard` (个人库页面包含订单历史)

**请求方式**: GET

**认证**: 需要登录

**请求参数**: 无

**响应结果**: HTML页面

**返回数据**:
- `user`: 用户信息
- `purchasedGames`: 已购买游戏列表（List<Product>）
- `gameCount`: 已购买游戏数量（Integer）

**业务逻辑**:
1. 验证用户登录
2. 查询用户所有订单
3. 提取所有订单中的商品
4. 去重处理
5. 渲染dashboard.html

---

## 五、个人库接口

### 5.1 查看个人游戏库

**接口地址**: `/dashboard`

**请求方式**: GET

**认证**: 需要登录

**请求参数**: 无

**响应结果**: HTML页面

**返回数据**:
- `user`: 当前用户信息
- `purchasedGames`: 已购买游戏列表
- `gameCount`: 游戏总数

**purchasedGames数据结构**:
```java
[
  {
    "id": 1,
    "name": "Elden Ring",
    "headerImageUrl": "https://...",
    "price": 59.99,
    "developer": "FromSoftware",
    "publisher": "Bandai Namco"
  },
  {
    "id": 17,
    "name": "Dark Souls III",
    ...
  }
]
```

**业务逻辑**:
1. 验证用户登录
2. 调用`StoreService.getPurchasedProducts(user)`
3. 查询用户所有订单
4. 提取所有商品并去重
5. 渲染dashboard.html

**示例**:
```bash
curl http://localhost:8080/dashboard -b cookies.txt
```

---

### 5.2 检查游戏拥有状态（内部接口）

**接口说明**: 这不是一个独立的API接口，而是在其他接口中使用的内部方法

**方法**: `StoreService.getOwnedProductIds(User user)`

**返回**: `Set<Long>` - 用户拥有的所有商品ID集合

**使用场景**:
- 商店列表页：标记已拥有游戏
- 商品详情页：显示"✓ 已在库中"而非"添加到购物车"

**SQL查询**:
```sql
SELECT DISTINCT op.product_id
FROM orders o
JOIN order_products op ON o.id = op.order_id
WHERE o.user_id = ?;
```

---

## 六、调试接口

### 6.1 会话调试接口

**接口地址**: `/debug/session`

**请求方式**: GET

**认证**: 可选

**请求参数**: 无

**响应格式**: JSON

**响应示例（已登录）**:
```json
{
  "authenticated": true,
  "username": "zhaoyingchen33",
  "principal": "User",
  "userId": 1,
  "email": "mc3184046135@outlook.com",
  "ownedProductCount": 2,
  "ownedProductIds": [1, 17],
  "cartCount": 0
}
```

**响应示例（未登录）**:
```json
{
  "authenticated": false,
  "message": "Not logged in"
}
```

**用途**:
- 调试认证状态
- 检查用户游戏库
- 验证Session是否有效

---

## 七、国际化支持

### 7.1 语言切换

**方法**: URL参数

**参数**: `?lang={语言代码}`

**支持语言**:
| 语言代码 | 语言名称 |
|---------|---------|
| en | 英语 |
| zh | 中文 |
| ja | 日语 |
| ko | 韩语 |
| fr | 法语 |
| de | 德语 |
| ru | 俄语 |
| es | 西班牙语 |

**示例**:
```
http://localhost:8080/store?lang=zh  # 切换到中文
http://localhost:8080/store?lang=ja  # 切换到日语
```

**响应**: 页面自动翻译为目标语言

---

### 7.2 货币自动切换

**机制**: 根据语言自动切换货币和汇率

**货币映射**:
| 语言 | 货币 | 符号 | 汇率（相对USD） |
|-----|------|------|----------------|
| en | USD | $ | 1.0 |
| zh | CNY | ¥ | 7.2 |
| ja | JPY | ¥ | 150 |
| ko | KRW | ₩ | 1300 |
| fr | EUR | € | 0.92 |
| de | EUR | € | 0.92 |
| ru | RUB | ₽ | 90 |
| es | EUR | € | 0.92 |

**价格计算**:
```
显示价格 = 原价(USD) × 汇率 × (1 - 折扣%)
```

---

## 八、错误处理

### 错误页面

| 错误类型 | HTTP状态码 | 页面 |
|---------|-----------|------|
| 页面不存在 | 404 | error/404.html |
| 未授权访问 | 401 | 重定向到/login |
| 服务器错误 | 500 | error/500.html |

### 错误响应示例

**商品不存在**:
```
HTTP/1.1 404 Not Found

错误信息: Product not found
```

**购物车为空**:
```
HTTP/1.1 400 Bad Request

错误信息: Cart is empty
```

---

## 九、接口测试示例

### 测试流程1：完整购买流程

```bash
# 1. 注册用户
curl -X POST http://localhost:8080/register \
  -d "username=buyer1" \
  -d "email=buyer1@test.com" \
  -d "password=123456"

# 2. 登录
curl -X POST http://localhost:8080/login \
  -c cookies.txt \
  -d "username=buyer1" \
  -d "password=123456"

# 3. 浏览商店
curl http://localhost:8080/store -b cookies.txt

# 4. 查看游戏详情
curl http://localhost:8080/store/game/5 -b cookies.txt

# 5. 添加到购物车
curl -X POST http://localhost:8080/cart/add \
  -b cookies.txt \
  -d "productId=5"

# 6. 查看购物车
curl http://localhost:8080/cart -b cookies.txt

# 7. 结账
curl -X POST http://localhost:8080/checkout \
  -b cookies.txt

# 8. 查看个人库
curl http://localhost:8080/dashboard -b cookies.txt

# 9. 调试接口验证
curl http://localhost:8080/debug/session -b cookies.txt
```

---

## 十、接口汇总表

| 序号 | 接口路径 | 方法 | 功能 | 是否需要登录 |
|-----|---------|------|------|------------|
| 1 | `/register` | POST | 用户注册 | 否 |
| 2 | `/login` | POST | 用户登录 | 否 |
| 3 | `/logout` | POST | 用户登出 | 是 |
| 4 | `/` | GET | 首页 | 否 |
| 5 | `/store` | GET | 商品列表 | 否 |
| 6 | `/store/game/{id}` | GET | 商品详情 | 否 |
| 7 | `/cart` | GET | 查看购物车 | 是 |
| 8 | `/cart/add` | POST | 添加到购物车 | 是 |
| 9 | `/cart/remove` | POST | 移除购物车商品 | 是 |
| 10 | `/checkout` | POST | 创建订单 | 是 |
| 11 | `/dashboard` | GET | 个人游戏库 | 是 |
| 12 | `/debug/session` | GET | 会话调试 | 否 |

---

**文档版本**: v1.0  
**API版本**: v1  
**编写日期**: 2026-06-23  
**Base URL**: http://localhost:8080  
**认证方式**: Session (Cookie: JSESSIONID)
