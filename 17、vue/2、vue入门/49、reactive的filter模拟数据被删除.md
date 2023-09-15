## 模拟删除

当点击删除的时候，可以先把数据从reactive中过滤掉，然后再发网络请求后端删除。

```vue
const tableData = reactive({
	loading: false,
	data: userData
});


tableData.data = tableData.data.filter((u)=>u.tenantSystemId !== data.tenantSystemId) //u为遍历的数据  data为当前删除的数据
```